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

package baggage;

public class Bags {

    public static <K, V> Bag<K, V> newBag() {
        Bag<K, V> bag = new ListBag<K, V>();
        return bag;
    }

    public static <K, V> Bag<K, V> newBag(K k1, V v1) {
        Bag<K, V> bag = new ListBag<K, V>();
        bag.put(k1, v1);
        return bag;
    }

    public static <K, V> Bag<K, V> newBag(K k1, V v1, K k2, V v2) {
        Bag<K, V> bag = new ListBag<K, V>();
        bag.put(k1, v1);
        bag.put(k2, v2);
        return bag;
    }

    public static <K, V> Bag<K, V> newBag(K k1, V v1, K k2, V v2, K k3, V v3) {
        Bag<K, V> bag = new ListBag<K, V>();
        bag.put(k1, v1);
        bag.put(k2, v2);
        bag.put(k3, v3);
        return bag;
    }

    public static <K, V> Bag<K, V> newBag(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Bag<K, V> bag = new ListBag<K, V>();
        bag.put(k1, v1);
        bag.put(k2, v2);
        bag.put(k3, v3);
        bag.put(k4, v4);
        return bag;
    }

    public static <K, V> Bag<K, V> newBag(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Bag<K, V> bag = new ListBag<K, V>();
        bag.put(k1, v1);
        bag.put(k2, v2);
        bag.put(k3, v3);
        bag.put(k4, v4);
        bag.put(k5, v5);
        return bag;
    }
}
