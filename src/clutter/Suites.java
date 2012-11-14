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

import com.google.common.base.Predicate;
import com.thoughtworks.ashcroft.runtime.JohnAshcroft;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.runner.SimpleTestCollector;

import java.lang.reflect.Modifier;
import java.util.Enumeration;

public class Suites {
    public static class UnitTests {
        public static Test suite() throws Exception {
            System.setProperty("ashcroft", "true");
            System.setSecurityManager(new JohnAshcroft());
            return makeSuite(new UnitTestPredicate());
        }
    }

    public static class IntegrationTests {
        public static Test suite() throws Exception {
            return makeSuite(new IntegrationTestPredicate());
        }
    }

    public static class PerformanceTests {
        public static Test suite() throws Exception {
            return makeSuite(new PerformanceTestPredicate());
        }
    }

    public static class SeleniumTests {
        public static Test suite() throws Exception {
            return makeSuite(new SeleniumTestPredicate());
        }
    }

    private static Test makeSuite(Predicate<Class> predicate) throws ClassNotFoundException {
        TestSuite testSuite = new TestSuite();
        SimpleTestCollector testCollector = new SimpleTestCollector();
        Enumeration enumeration = testCollector.collectTests();
        while (enumeration.hasMoreElements()) {
            Class klass = Class.forName((String) enumeration.nextElement());
            if (predicate.apply(klass)) {
                testSuite.addTestSuite(klass);
            }
        }
        return testSuite;
    }


    private static class UnitTestPredicate implements Predicate<Class> {
        public boolean apply(Class candidate) {
            return candidate.getAnnotation(IntegrationTest.class) == null &&
                    candidate.getAnnotation(PerformanceTest.class) == null &&
                    candidate.getAnnotation(SeleniumTest.class) == null &&
                    candidate.getAnnotation(Ignored.class) == null &&
                    isTestCase(candidate);
        }
    }

    private static class IntegrationTestPredicate implements Predicate<Class> {
        public boolean apply(Class candidate) {
            return candidate.getAnnotation(IntegrationTest.class) != null && isTestCase(candidate);
        }
    }

    private static class PerformanceTestPredicate implements Predicate<Class> {
        public boolean apply(Class candidate) {
            return candidate.getAnnotation(PerformanceTest.class) != null && isTestCase(candidate);
        }
    }

    private static class SeleniumTestPredicate implements Predicate<Class> {
        public boolean apply(Class candidate) {
            return candidate.getAnnotation(SeleniumTest.class) != null && isTestCase(candidate);
        }
    }

    private static boolean isTestCase(Class candidate) {
        return TestCase.class.isAssignableFrom(candidate) && !Modifier.isAbstract(candidate.getModifiers());
    }


}
