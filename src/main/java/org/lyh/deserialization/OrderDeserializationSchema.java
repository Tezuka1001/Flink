package org.lyh.deserialization;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.lyh.model.OrderEntity;
import org.lyh.util.JsonUtils;

/**
 * @author lyh
 * @version 2021-03-31 12:01
 */
public class OrderDeserializationSchema implements DeserializationSchema<OrderEntity> {

    @Override
    public OrderEntity deserialize(byte[] message) {
        return JsonUtils.fromJson(new String(message), OrderEntity.class);
    }

    @Override
    public boolean isEndOfStream(OrderEntity nextElement) {
        return false;
    }

    @Override
    public TypeInformation<OrderEntity> getProducedType() {
        return TypeInformation.of(OrderEntity.class);
    }

}
