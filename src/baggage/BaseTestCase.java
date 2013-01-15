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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Fabrice Medio
 */

public abstract class BaseTestCase extends TestCase {
    private LinkedBlockingQueue<String> eventQueue;
    protected int randomInt;
    protected long randomLong;
    protected byte randomByte;
    protected String randomString;
    protected SecretKey secretKey = new SecretKeySpec(new byte[]{35, -37, -53, -105, 107, -37, -14, -64, 28, -74, -98, 124, -8, -7, 68, 54}, "AES");

    protected void setUp() throws Exception {
        eventQueue = new LinkedBlockingQueue<String>();
        randomInt = (int) (Math.random() * 100000d);
        randomByte = (byte) (Math.random() * 127d);
        randomLong = randomLong();
        randomString = randomString(32);
    }

    protected long randomLong() {
        return (long) (Math.random() * 100000d);
    }

    protected String randomString(int byteLength) {
        byte[] bytes = makeRandomBytes(byteLength);

        return Base64.encodeBase64String(bytes);
    }

    protected byte[] makeRandomBytes(int howMany) {
        byte[] bytes = new byte[howMany];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (128d * Math.random());
        }
        return bytes;
    }

    protected static void assertFailure(Class<? extends Exception> klass, Fallible fallible) {
        try {
            fallible.execute();
        } catch (Exception e) {
            if (!klass.isAssignableFrom(e.getClass())) {
                throw new RuntimeException("Expected a " + klass.getSimpleName() + ", got a " + e.getClass().getSimpleName(), e);
            }
            return;
        }
        Assert.fail("Expected a " + klass.getSimpleName() + ", got no exceptions at all");
    }


    protected void logEventQueue() {
        for (String event : eventQueue) {
            Log.info(this, event);
        }
    }

    protected void addEvent(String event) {
        eventQueue.add(event);
    }

    protected void expectEvent(String event) throws Exception {
        String result = eventQueue.take();
        if (!event.equals(result)) {
            Assert.fail("Expected event [" + event + "], got [" + result + "]");
        }
    }

    protected int eventCount() {
        return eventQueue.size();
    }

    protected void assertContains(String substring, String string) {
        if (!string.contains(substring)) {
            throw new AssertionFailedError("Should have contained '" + string + "', was '" + substring);
        }
    }


    protected static void assertRoundsTo(double expected, double right, double roundingFactor) {
        String message = right + " doesn't round to " + expected;
        assertRoundsTo(message, expected, right, roundingFactor);
    }

    protected static void assertRoundsTo(String message, double expected, double right, double roundingFactor) {
        boolean isWithinBounds = right > expected - roundingFactor && right < expected + roundingFactor;
        if (!isWithinBounds) {
            Assert.fail(message);
        }
    }

    protected void assertEventsSoFar(String... events) {
        Assert.assertEquals(Joiner.on("\n").join(events), Joiner.on("\n").join(this.eventQueue));
    }

    protected void assertNoEventsSoFar() {
        if (eventQueue.size() != 0) {
            Assert.fail("There are " + eventQueue.size() + " pending events, should be none");
        }
    }

    protected static final void assertNotEquals(Object left, Object right) {
        if (left.equals(right)) {
            Assert.fail(left + " is equal to " + right);
        }
    }

    protected static final void assertEquals(byte[] expected, byte[] actual) {
        if (!Arrays.equals(expected, actual)) {
            Assert.fail("Expected " + toString(expected) + ", got " + toString(actual));
        }
    }

    private static String toString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(Integer.toString(bytes[i]));
        }
        return sb.toString();
    }

    protected static void block() {
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
        }
    }

    protected static void attempt(Runnable task) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(task);
        executorService.shutdown();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> list(T... tees) {
        return Lists.newArrayList(tees);
    }
}
