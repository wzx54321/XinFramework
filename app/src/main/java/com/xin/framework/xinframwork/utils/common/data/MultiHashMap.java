package com.xin.framework.xinframwork.utils.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 允许一个Key对应多个值的Map
 *
 * @param <K>
 */
@SuppressWarnings("SuspiciousMethodCalls")
public class MultiHashMap<K, BaseModel> {

    private HashMap<K, ArrayList<BaseModel>> model;

    public MultiHashMap() {
        this(16);
    }

    public MultiHashMap(int capacity) {
        model = new HashMap<>(capacity < 8 ? 8 : capacity);
    }

    public void clear() {
        model.clear();
    }

    public boolean containsKey(K key) {
        return model.containsKey(key);
    }

    public boolean containsValue(BaseModel value) {
        return model.containsValue(value);
    }

    public Set<Entry<K, ArrayList<BaseModel>>> entrySet() {
        return model.entrySet();
    }

    public ArrayList<BaseModel> get(K key) {
        return model.get(key);
    }

    public boolean isEmpty() {
        return model.isEmpty();
    }

    public Set<K> keySet() {
        return model.keySet();
    }

    public void put(K key,
                    BaseModel value) {
        ArrayList<BaseModel> ls = model.get(key);
        if (ls == null) {
            ls = new ArrayList<>();
            model.put(key,
                    ls);
        }
        if (!ls.contains(value)) {
            ls.add(value);
        }
    }

    public void put(K key,
                    BaseModel value, int index) {
        ArrayList<BaseModel> ls = model.get(key);
        if (ls == null) {
            ls = new ArrayList<>();
            model.put(key,
                    ls);
        }
        if (!ls.contains(value)) {
            ls.add(index, value);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public void putAll(K key, List<BaseModel> value) {
        ArrayList<BaseModel> ls = model.get(key);
        if (ls == null) {
            ls = new ArrayList<>();
            model.put(key,
                    ls);
        }
        if (!ls.contains(value)) {
            ls.addAll(value);
        }

    }

    public ArrayList<BaseModel> remove(K key) {
        return model.remove(key);
    }

    public void remove(K key,
                       BaseModel val) {
        ArrayList<BaseModel> ls = model.get(key);
        if (ls != null) {
            ls.remove(val);
        }
    }

    /**
     * 低效率, 需要遍历
     *
     * @param val
     */
    public void removeValue(BaseModel val) {
        for (Entry<K, ArrayList<BaseModel>> e : model.entrySet()) {
            ArrayList<BaseModel> ls = e.getValue();
            for (int i = 0; i < ls.size(); ) {
                BaseModel v = ls.get(i);
                if (v == val || (v != null && v.equals(val))) {
                    e.getValue()
                            .remove(val);
                    continue;
                }
                ++i;
            }
        }
    }

    public int size() {
        return model.size();
    }

    public Collection<ArrayList<BaseModel>> values() {
        return model.values();
    }


}
