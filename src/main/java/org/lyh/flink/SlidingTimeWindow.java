package org.lyh.flink;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

/**
 * @author lyh
 * @version 2021-03-15 21:09
 */
public class SlidingTimeWindow {

    private static final int WINDOW_SIZE = 3;

    private static final int WINDOW_SLIDE = 3;

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = LocalStreamEnvironment.getExecutionEnvironment();

        DataStreamSource<Integer> dataStreamSource = env.fromElements(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        );

        dataStreamSource.countWindowAll(WINDOW_SIZE, WINDOW_SLIDE)
                .process(new ProcessAllWindowFunction<Integer, Object, GlobalWindow>() {

                    @Override
                    public void process(Context context, Iterable<Integer> elements, Collector<Object> out) throws Exception {
                        int sum = 0;
                        for (Integer element : elements) {
                            sum += element;
                        }
                        out.collect(sum);
                    }
                }).print();

        env.setParallelism(1).execute("SlidingTimeWindow job");
    }
}
