package com.tabcorp.qa.common;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static Map<KEY, Object> map;

    public enum KEY {
        EVENT_ID,
        PRODUCT_ID
    }

    public static void init() {
        map = new HashMap<>();
    }

    public static void put(KEY key, Object value){
        map.put(key, value);
    }

    public static Object get(KEY key){
        return Helpers.noNullGet(map, key);
    }


}
