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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class Counts<T> implements Iterable<T> {
    private Map<T, Long> counts;
    private long sum;

    public Counts() {
        counts = new LinkedHashMap<T, Long>();
    }

    public Counts(Iterator<T> iterator) {
        this();
        while (iterator.hasNext()) {
            increment(iterator.next());
        }
    }


    public long get(T tee) {
        Long result = counts.get(tee);
        return result == null ? 0l : result;
    }

    public long increment(T tee) {
        return increment(tee, 1);
    }

    public long increment(T tee, long howMany) {
        Long result = counts.get(tee);
        result = result == null ? howMany : result + howMany;
        counts.put(tee, result);
        sum += howMany;
        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return counts.keySet().iterator();
    }

    public int size() {
        return counts.size();
    }

    public long sum() {
        return sum;
    }

    public void tally(Counts<T> counts) {
        for (T tee : counts) {
            this.increment(tee);
        }
    }

    public void add(Counts<T> counts) {
        for (T tee : counts) {
            this.increment(tee, counts.get(tee));
        }
    }
}
