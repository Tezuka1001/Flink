package org.lyh.util;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

import static org.lyh.constant.Const.KafkaConst.*;

/**
 * @author lyh
 * @version 2021-03-23 19:55
 */
public class KafkaUtil {

    public static KafkaProducer<String, String> kafkaProducer;

    static {

        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER);
        props.put("key.serializer", KEY_SERIALIZER);
        props.put("value.serializer", VALUE_SERIALIZER);
        kafkaProducer = new KafkaProducer<>(props);
    }
}
