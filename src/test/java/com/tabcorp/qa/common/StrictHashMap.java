package com.tabcorp.qa.common;

import org.apache.commons.lang3.BooleanUtils;

import java.util.LinkedHashMap;

public class StrictHashMap<K, V> extends LinkedHashMap<K, V> {

    public V get(Object key) {
        if (!super.containsKey(key)) {
            throw new RuntimeException(String.format("Map key '%s' does not exist in: %s", key, this.keySet()));
        }
        return super.get(key);
    }

    public boolean getBoolean(Object key) {
        return BooleanUtils.toBoolean(get(key).toString());
    }
}
