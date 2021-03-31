package org.lyh;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;


/**
 * @author lyh
 * @version 2021-03-31 10:52
 */
public class GeneralTest {

    @Test
    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        try {
            list.parallelStream().forEach((e) -> {
                System.out.println(Thread.currentThread().getName());
                int i = 1/0;
                System.out.println(e);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
