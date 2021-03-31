package org.lyh.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.shaded.guava18.com.google.common.base.Joiner;
import org.apache.flink.shaded.guava18.com.google.common.collect.Maps;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;
import org.lyh.deserialization.StrategyDeserializationSchema;
import org.lyh.model.StrategyEsEntity;
import org.lyh.model.StrategyKafkaEntity;
import org.lyh.util.JsonUtils;
import org.lyh.watermark.StrategyAssignerWithPeriodicWatermarks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.lyh.constant.Const.KafkaConst.BROKER;
import static org.lyh.constant.Const.KafkaConst.STRATEGY_TOPIC;

/**
 * @author lyh
 * @version 2021-03-30 16:16
 */
public class StrategyStatistics {

    private static final int WINDOW_SIZE = 1;

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = LocalStreamEnvironment.getExecutionEnvironment();

        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER);
        props.put("auto.offset.reset", "latest");

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //可以通过这里设置生成watermark的周期
        env.getConfig().setAutoWatermarkInterval(200);

        env.addSource(new FlinkKafkaConsumer011<>(STRATEGY_TOPIC, new StrategyDeserializationSchema(), props))
                .filter((FilterFunction<StrategyKafkaEntity>) value -> value != null
                        && value.getEffectStrategyId() != null
                )
                .returns(StrategyKafkaEntity.class)
                .assignTimestampsAndWatermarks(new StrategyAssignerWithPeriodicWatermarks())
                .timeWindowAll(Time.minutes(WINDOW_SIZE))
                .process(new ProcessAllWindowFunction<StrategyKafkaEntity, StrategyEsEntity, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<StrategyKafkaEntity> elements, Collector<StrategyEsEntity> out) {

                        Map<Long, Integer> interceptNum = Maps.newHashMap();
                        Map<Long, Set<String>> phoneDistinctNumMap = Maps.newHashMap(), uuidDistinctNum = Maps.newHashMap();
                        elements.forEach((element) -> {

                            Long effectStrategyId = element.getEffectStrategyId();

                            if (!interceptNum.containsKey(effectStrategyId)) {
                                interceptNum.put(effectStrategyId, 0);
                                phoneDistinctNumMap.put(effectStrategyId, new HashSet<>());
                                uuidDistinctNum.put(effectStrategyId, new HashSet<>());
                            }

                            interceptNum.computeIfPresent(effectStrategyId, (k, v) -> v + 1);
                            if (!StringUtils.isNullOrWhitespaceOnly(element.getBindedPhone())) {
                                phoneDistinctNumMap.get(effectStrategyId).add(element.getBindedPhone());
                            }
                            if (!StringUtils.isNullOrWhitespaceOnly(element.getUuid())) {
                                uuidDistinctNum.get(effectStrategyId).add(element.getUuid());
                            }
                        });

                        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(context.window().getStart()),
                                ZoneId.systemDefault());
                        String date = Joiner.on("-").join(time.getYear(), time.getMonthValue(), time.getDayOfMonth());
                        int hour = time.getHour();
                        int minute = time.getMinute();
                        int tenMinute = minute - (minute % 10);

                        interceptNum.forEach((k, v) -> out.collect(new StrategyEsEntity(k, v,
                                phoneDistinctNumMap.get(k).size(), uuidDistinctNum.get(k).size(), date, hour, minute,
                                tenMinute, context.window().getStart())));
                    }
                })
                .addSink(new SinkFunction<StrategyEsEntity>() {

                    @Override
                    public void invoke(StrategyEsEntity value, Context context) {

                        System.out.println("current time : " + LocalDateTime.now());
                        System.out.println("sink to elasticsearch : " + JsonUtils.toJson(value));
                    }
                });

        env.execute("StrategyStatistics");
    }
}
