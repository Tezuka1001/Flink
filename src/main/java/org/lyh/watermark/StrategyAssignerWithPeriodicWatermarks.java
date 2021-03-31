package org.lyh.watermark;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.lyh.model.StrategyKafkaEntity;

/**
 * @author lyh
 * @version 2021-03-31 12:06
 */
public class StrategyAssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<StrategyKafkaEntity> {

    private long currentMaxTimestamp = -1L;

    @Override
    public Watermark getCurrentWatermark() {
        final long maxTimeLimit = 3500L;
        return new Watermark(currentMaxTimestamp - maxTimeLimit);
    }

    @Override
    public long extractTimestamp(StrategyKafkaEntity element, long previousElementTimestamp) {
        long currentTimestamp = element.getCreatTime();
        currentMaxTimestamp = Math.max(currentMaxTimestamp, currentTimestamp);
        return currentTimestamp;
    }

}
