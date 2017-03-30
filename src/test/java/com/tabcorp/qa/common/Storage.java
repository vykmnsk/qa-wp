package com.tabcorp.qa.common;

import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private static Map<KEY, Object> map;

    public enum KEY {
        EVENT_IDS,
        PRODUCT_ID,
        CUSTOMER_ID
    }

    public static void init() {
        map = new HashMap<>();
    }

    public static void put(KEY key, Object value) {
        map.put(key, value);
    }

    public static void add(KEY key, Object value) {
        List<Object> entries;
        entries = (null == map.get(key)) ? new ArrayList<>() : (List) map.get(key) ;
        entries.add(value);
        map.put(key, entries);
    }

    public static Object get(KEY key) {
        return Helpers.nonNullGet(map, key);
    }

    public static Object getLast(KEY key) {
        List<Object> entries = (List) map.get(key);
        Assertions.assertThat(entries).as(String.format("Data in storage key=%s", key)).isNotEmpty();
        return entries.get(entries.size() - 1);
    }

}
