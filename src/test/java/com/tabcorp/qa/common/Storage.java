package com.tabcorp.qa.common;

import org.assertj.core.api.Assertions;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Storage {
    private static Map<KEY, Object> map;

    public enum KEY {
        CATEGORIES,
        SUBCATEGORIES,
        EVENT_IDS,
        EVENT_NAMES,
        PRODUCT_IDS,
        API_ACCESS_TOKEN,
        CUSTOMER
    }

    public static void init() {
        map = new HashMap<>();
    }

    public static void put(KEY key, Object value) {
        map.put(key, value);
    }

    public static Object get(KEY key) {
        return Helpers.nonNullGet(map, key);
    }

    public static void add(KEY key, Object value) {
        Deque<Object> entries;
        entries = (null == map.get(key)) ? new LinkedList<>() : (Deque) map.get(key);
        entries.add(value);
        map.put(key, entries);
    }

    public static Object removeFirst(KEY key) {
        Deque<Object> entries = getDoubleEndQue(key);
        return entries.removeFirst();
    }

    public static Object getLast(KEY key) {
        Deque<Object> entries = getDoubleEndQue(key);
        return entries.getLast();
    }

    public static Object getFirst(KEY key) {
        Deque<Object> entries = getDoubleEndQue(key);
        return entries.getFirst();
    }

    private static Deque<Object> getDoubleEndQue(KEY key) {
        Deque<Object> entries = (Deque) map.get(key);
        Assertions.assertThat(entries).as(String.format("Data in storage key=%s", key)).isNotEmpty();
        return entries;
    }

}
