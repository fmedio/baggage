package baggage.hypertoolkit.views;

import baggage.hypertoolkit.html.*;

import java.io.IOException;
import java.io.PrintWriter;

import static baggage.hypertoolkit.html.Html.*;

public abstract class Pager implements Renderable {
    private CssClass cssClass;
    private int totalValues;
    private int valuesPerPage;
    private int currentStartValue;
    private int maxPagesToDisplay;
    private String urlTarget;
    private int totalPages;
    private int currentPage;
    private Tag activeLeftArrow, inactiveLeftArrow;
    private Tag activeRightArrow, inactiveRightArrow;

    public Pager(int totalValues, int valuesPerPage, int currentStartValue, int maxPagesToDisplay, String urlTarget, CssClass cssClass) {
        this.totalValues = totalValues;
        this.valuesPerPage = valuesPerPage;
        this.currentStartValue = currentStartValue;
        this.maxPagesToDisplay = maxPagesToDisplay;
        this.urlTarget = urlTarget;
        this.cssClass = cssClass;
        totalPages = (int) Math.ceil((float) this.totalValues / (float) this.valuesPerPage);
        currentPage = (int) Math.floor((float) this.currentStartValue / (float) this.valuesPerPage);
        activeLeftArrow = img("images/left-arrow.png");
        activeRightArrow = img("images/right-arrow.png");
        inactiveLeftArrow = img("images/inactive-left-arrow.png");
        inactiveRightArrow = img("images/inactive-right-arrow.png");
    }

    protected abstract baggage.Bag<String, String> makeParams(int firstValue);

    static class Interval {
        public int startPage, endPage;
    }

    public void render(PrintWriter printWriter) throws IOException {
        Tag pager = Html.div(cssClass);
        if (totalValues <= valuesPerPage) {
            Renderable.NULL.render(printWriter);
            return;
        }

        Interval pageInterval = computeInterval();
        if (currentPage != 0) {
//            pager.add(makeLink("<<", 0));
//            pager.add(nbsp());
            pager.add(makeLink(activeLeftArrow, (currentPage - 1) * valuesPerPage));
        } else {
//            pager.add("<<");
//            pager.add(nbsp());
            pager.add(inactiveLeftArrow);
        }
        pager.add(nbsp());
//        pager.add("..");
        pager.add(nbsp());


        for (int i = pageInterval.startPage; i <= pageInterval.endPage; i++) {
            if (i == currentPage) {
                pager.add(strong(Integer.toString(i + 1)));
            } else {
                pager.add(makeLink(text(Integer.toString(i + 1)), valuesPerPage * i));
            }
            if (i != pageInterval.endPage) {
                pager.add(nbsp());
            }
        }

        pager.add(nbsp());
//        pager.add("..");
        pager.add(nbsp());

        if (currentPage != totalPages - 1) {
            pager.add(makeLink(activeRightArrow, (currentPage + 1) * valuesPerPage));
//            pager.add(nbsp());
//            pager.add(makeLink(">>", (totalPages - 1) * valuesPerPage));
        } else {
            pager.add(inactiveRightArrow);
//            pager.add(nbsp());
//            pager.add(">>");
        }

        pager.render(printWriter);
    }

    private Renderable makeLink(Renderable contents, int firstDesiredValue) {
        return new Link(urlTarget, makeParams(firstDesiredValue)).add(contents);
    }

    protected Interval computeInterval() {
        Interval pageInterval = new Interval();

        if (totalPages < maxPagesToDisplay) {
            pageInterval.startPage = 0;
            pageInterval.endPage = totalPages - 1;
        } else if (currentPage - (maxPagesToDisplay / 2) < 0) {
            pageInterval.startPage = 0;
            pageInterval.endPage = maxPagesToDisplay - 1;
        } else if (totalPages - currentPage < (maxPagesToDisplay / 2)) {
            pageInterval.startPage = totalPages - maxPagesToDisplay;
            pageInterval.endPage = totalPages - 1;
        } else {
            pageInterval.startPage = (int) (currentPage - Math.floor((float) maxPagesToDisplay / 2f));
            pageInterval.endPage = (int) (currentPage + Math.floor((float) maxPagesToDisplay / 2f) - 1);
        }
        return pageInterval;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}

