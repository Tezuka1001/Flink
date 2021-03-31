package org.lyh;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.lyh.model.OrderEntity;
import org.lyh.model.StrategyKafkaEntity;
import org.lyh.util.JsonUtils;

import java.util.Random;

import static org.lyh.constant.Const.KafkaConst.ORDER_TOPIC;
import static org.lyh.constant.Const.KafkaConst.STRATEGY_TOPIC;
import static org.lyh.util.KafkaUtil.kafkaProducer;


/**
 * @author lyh
 * @version 2021-03-31 11:47
 */
public class KafkaProducer {

    private static final int MAX_BOUND = 10000;

    private static long ORDER_ID_INCREASE = 1;

    private static long USER_ID_INCREASE = 1000_000_000;

    @Test
    public void sendStrategyMsg() throws InterruptedException {

        while (true) {
            Thread.sleep(10);
            mockStrategyMsg();
        }
    }

    @Test
    public void sendOrderMsg() throws InterruptedException {

        while (true) {
            Thread.sleep(1000);
            mockOrderMsg();
        }
    }

    private static void mockStrategyMsg() {

        StrategyKafkaEntity entity = initStrategyEntity();
        String msg = JsonUtils.toJson(entity);
        writeToKafka(STRATEGY_TOPIC, msg);
    }

    private static void mockOrderMsg() {

        OrderEntity orderEntity = initOrderEntity();
        String msg = JsonUtils.toJson(orderEntity);
        writeToKafka(ORDER_TOPIC, msg);
    }

    private static OrderEntity initOrderEntity() {

        long random = new Random().nextInt(MAX_BOUND);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCity(random % 5);
        orderEntity.setOrderPrice(random);
        orderEntity.setOrderId(ORDER_ID_INCREASE++);
        orderEntity.setUserId(USER_ID_INCREASE++);
        return orderEntity;
    }

    private static StrategyKafkaEntity initStrategyEntity() {

        long random = new Random().nextInt(MAX_BOUND);
        StrategyKafkaEntity entity = new StrategyKafkaEntity();
        entity.setBindedPhone(String.valueOf(random));
        entity.setUuid(DigestUtils.md5Hex(String.valueOf(random)));
        entity.setCreatTime(System.currentTimeMillis());
        entity.setEffectStrategyId(random % 5);
        return entity;
    }

    private static void writeToKafka(String topic, String msg) {

        kafkaProducer.send(new ProducerRecord<>(topic, msg));
        kafkaProducer.flush();
        System.out.println("发送数据: " + msg);
    }
}
