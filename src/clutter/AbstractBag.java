/*
 * The MIT License
 *
 * Copyright (c) 2010 Fabrice Medio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package clutter;

import java.util.*;

public abstract class AbstractBag<C extends Collection<V>, K, V> implements Bag<K, V> {
    private LinkedHashMap<K, C> map;

    protected abstract C newCollection();

    public AbstractBag() {
        this.map = new LinkedHashMap<K, C>();
    }

    public AbstractBag(Map<K, V[]> map) {
        this();
        for (K key : map.keySet()) {
            for (V value : map.get(key)) {
                this.put(key, value);
            }
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public V get(Object key) {
        Collection<V> collection = map.get(key);
        if (collection != null) {
            return collection.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public C getValues(Object key) {
        C collection = map.get(key);
        if (collection == null) {
            return newCollection();
        } else {
            return collection;
        }
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            return value;
        }
        C collection = map.get(key);
        if (collection == null) {
            collection = newCollection();
        }
        collection.add(value);
        map.put(key, collection);
        return value;
    }

    @Override
    public V remove(Object o) {
        C collection = map.get(o);
        if (collection == null) {
            return null;
        } else {
            Iterator<V> iterator = collection.iterator();
            V value = iterator.next();
            iterator.remove();
            map.remove(o);
            return value;
        }
    }

    @Override
    public void putAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void putAll(Bag<K, V> bag) {
        for (K key : bag.keySet()) {
            for (V value : bag.getValues(key)) {
                this.put(key, value);
            }
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Iterable<Map.Entry<K, V>> entries() {
        return new Iterable<Map.Entry<K, V>>() {
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {
                    Map.Entry<K, V> cachedEntry = null;
                    Iterator<K> keys = map.keySet().iterator();
                    K currentKey = null;
                    Iterator<V> values = null;

                    boolean isDirty = true;

                    public boolean hasNext() {
                        ensureFreshness();
                        return cachedEntry != null;
                    }

                    public Map.Entry<K, V> next() {
                        ensureFreshness();
                        isDirty = true;
                        return cachedEntry;
                    }

                    private void ensureFreshness() {
                        if (isDirty) {
                            if (values == null || !values.hasNext()) {
                                if (keys.hasNext()) {
                                    currentKey = keys.next();
                                    values = map.get(currentKey).iterator();
                                } else {
                                    cachedEntry = null;
                                    isDirty = false;
                                    return;
                                }
                            }
                            cachedEntry = new AbstractMap.SimpleEntry<K, V>(currentKey, values.next());

                        }
                        isDirty = false;
                    }

                    public void remove() {
                    }
                };
            }
        };
    }
}
