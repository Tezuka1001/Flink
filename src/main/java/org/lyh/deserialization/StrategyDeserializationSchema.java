package org.lyh.deserialization;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.lyh.model.StrategyKafkaEntity;
import org.lyh.util.JsonUtils;

/**
 * @author lyh
 * @version 2021-03-31 12:02
 */
public class StrategyDeserializationSchema implements DeserializationSchema<StrategyKafkaEntity> {

    @Override
    public StrategyKafkaEntity deserialize(byte[] message) {
        return JsonUtils.fromJson(new String(message), StrategyKafkaEntity.class);
    }

    @Override
    public boolean isEndOfStream(StrategyKafkaEntity nextElement) {
        return false;
    }

    @Override
    public TypeInformation<StrategyKafkaEntity> getProducedType() {
        return TypeInformation.of(StrategyKafkaEntity.class);
    }
}
