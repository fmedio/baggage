package baggage.hypertoolkit.views;

import baggage.BaseTestCase;
import baggage.ListBag;
import baggage.hypertoolkit.html.CssClass;

import static org.mockito.Mockito.mock;

public class PagerTest extends BaseTestCase {
    private static class MockPager extends Pager {
        public MockPager(int totalValues, int valuesPerPage, int currentStartValue, int maxPagesToDisplay, String urlTarget) {
            super(totalValues, valuesPerPage, currentStartValue, maxPagesToDisplay, urlTarget, mock(CssClass.class));
        }

        protected baggage.Bag<String, String> makeParams(int firstValue) {
            return new ListBag<String, String>();
        }
    }

    public void testSeriesIsSmallerThanRequiredDisplayedPages() throws Exception {
        Pager pager = new MockPager(31, 5, 0, 10, "foo");
        assertEquals(7, pager.getTotalPages());
        Pager.Interval interval = pager.computeInterval();
        assertEquals(0, interval.startPage);
        assertEquals(6, interval.endPage);
    }

    public void testEndPage() throws Exception {
        Pager pager = new MockPager(75, 5, 71, 10, "foo");
        assertEquals(15, pager.getTotalPages());
        Pager.Interval interval = pager.computeInterval();
        assertEquals(14, interval.endPage);
        assertEquals(5, interval.startPage);
    }

    public void testInterval() throws Exception {
        Pager pager = new MockPager(100, 5, 50, 10, "foo");
        assertEquals(20, pager.getTotalPages());
        assertEquals(10, pager.getCurrentPage());
        Pager.Interval interval = pager.computeInterval();
        assertEquals(5, interval.startPage);
        assertEquals(14, interval.endPage);

        pager = new MockPager(100, 5, 9, 10, "foo");
        interval = pager.computeInterval();
        assertEquals(0, interval.startPage);
        assertEquals(9, interval.endPage);

        pager = new MockPager(100, 5, 92, 10, "foo");
        interval = pager.computeInterval();
        assertEquals(10, interval.startPage);
        assertEquals(19, interval.endPage);
    }
}
