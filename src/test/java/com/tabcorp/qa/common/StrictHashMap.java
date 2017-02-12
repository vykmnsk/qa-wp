package com.tabcorp.qa.common;

import java.util.HashMap;

public class StrictHashMap<K, V> extends HashMap<K, V> {

    public V get(Object key) {
        if (!super.containsKey(key)) {
            throw new RuntimeException(String.format("Map key %s does not exist in: %s", key, this.keySet()));
        }
        return super.get(key);
    }
}
