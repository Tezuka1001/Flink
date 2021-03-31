package org.lyh.flink;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava18.com.google.common.collect.Maps;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;
import org.lyh.deserialization.OrderDeserializationSchema;
import org.lyh.model.OrderEntity;
import org.lyh.util.JsonUtils;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;

import static org.lyh.constant.Const.KafkaConst.BROKER;
import static org.lyh.constant.Const.KafkaConst.ORDER_TOPIC;

/**
 * @author lyh
 * @version 2021-03-18 16:16
 */
public class TopK {

    private static final int K = 5;

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = LocalStreamEnvironment.getExecutionEnvironment();

        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER);
        props.put("auto.offset.reset", "latest");

        env.addSource(new FlinkKafkaConsumer011<>(ORDER_TOPIC, new OrderDeserializationSchema(), props))
                .keyBy(((KeySelector<OrderEntity, Long>) OrderEntity::getCity))
                .process(new KeyedProcessFunction<Long, OrderEntity, Object>() {

                    ValueState<Object> state;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        state = getRuntimeContext().getState(new ValueStateDescriptor<>("top k", Object.class));
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void processElement(OrderEntity value, Context ctx, Collector<Object> out) throws Exception {

                        Map<Long, Queue<Long>> queueMap = (Map<Long, Queue<Long>>) state.value();
                        if (queueMap == null) {
                            queueMap = Maps.newHashMap();
                        }

                        Queue<Long> queue = queueMap.get(value.getCity());
                        if (queue == null) {
                            queue = new PriorityQueue<>(K);
                            queueMap.put(value.getCity(), queue);
                        }

                        if (queue.size() < K) {
                            queue.add(value.getOrderPrice());
                        } else {
                            if (queue.peek().compareTo(value.getOrderPrice()) < 0) {
                                queue.poll();
                                queue.add(value.getOrderPrice());
                            }
                        }
                        state.update(queueMap);
                        out.collect(queueMap);
                    }

                })
                .addSink(new SinkFunction<Object>() {

                    @Override
                    public void invoke(Object value, Context context) throws Exception {
                        System.out.println(JsonUtils.toJson(value));
                    }
                });

        env.execute("Top K Job");
    }
}
