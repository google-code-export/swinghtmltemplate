package ru.swing.html;

import org.jdesktop.observablecollections.ObservableMap;
import org.jdesktop.observablecollections.ObservableMapListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maybe to use as model elements store in child tag.
 * Or to remove.
 * Currently unused
 */
public class WrapperMap extends HashMap {

    private ObservableMap wrappedMap;

    public WrapperMap(ObservableMap wrappedMap) {
        this.wrappedMap = wrappedMap;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean res = super.containsKey(key);
        if (!res) {
            return wrappedMap.containsKey(key);
        }
        return res;
    }


    @Override
    public boolean containsValue(Object value) {
        boolean b = super.containsValue(value);
        if (!b) {
            return wrappedMap.containsValue(value);
        }
        return b;
    }

    @Override
    public Set keySet() {
        Set values = wrappedMap.keySet();
        values.addAll(super.keySet());
        return values;
    }

    @Override
    public Collection values() {
        Collection values = wrappedMap.values();
        values.addAll(super.values());
        return values;
    }

    @Override
    public int size() {
        return super.size() + wrappedMap.size();
    }

    @Override
    public Object get(Object key) {
        Object o = super.get(key);
        if (o==null) {
            return wrappedMap.get(key);
        }
        return o;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && wrappedMap.isEmpty();
    }

    @Override
    public Set entrySet() {
        Set set = wrappedMap.entrySet();
        set.addAll(super.entrySet());
        return set;
    }
}
