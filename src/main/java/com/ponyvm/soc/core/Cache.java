package com.ponyvm.soc.core;

import java.util.LinkedHashMap;
import java.util.Map;

class Cache<I> extends LinkedHashMap<Integer, I> {
    private int capacity;

    public Cache(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    public I get(int key) {
//        return super.getOrDefault(key, );
        return super.get(key);
    }

    public void put(int key, I value) {
        super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, I> eldest) {
        return size() > capacity;
    }
}