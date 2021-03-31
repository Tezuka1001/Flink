package org.lyh.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author liuyuanhu
 */
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String values, Class<T> cls) {
        return GSON.fromJson(values, cls);
    }

}

