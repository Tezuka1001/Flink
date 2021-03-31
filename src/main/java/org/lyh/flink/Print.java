package org.lyh.flink;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static org.lyh.constant.Const.HOSTNAME;
import static org.lyh.constant.Const.PORT;

/**
 * @author lyh
 * @version 2021-03-23 17:28
 */
public class Print {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = LocalStreamEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.socketTextStream(HOSTNAME, PORT);

        source.print();

        env.execute("hello world");

    }
}
