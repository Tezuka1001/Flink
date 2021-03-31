package org.lyh.constant;

/**
 * @author lyh
 * @version 2021-03-23 17:29
 */
public class Const {

    public static final String HOSTNAME = "localhost";

    public static final int PORT = 9000;

    public static class KafkaConst {

        public static final String BROKER = "localhost:9092";

        public static final String ORDER_TOPIC = "org.lyh.test.order";

        public static final String STRATEGY_TOPIC = "org.lyh.test.strategy";

        public static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";

        public static final String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    }

}
