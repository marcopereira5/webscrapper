package WebScrapper;

import org.jsoup.select.Elements;

/**
 * Implements the visited pages criteria
 */
public class VisitedPagesCriteria implements IterationStrategy {

    @Override
    public int visitedPagesCount() {
        return 1;
    }

    @Override
    public int depthCount() {
        return 0;
    }

    @Override
    public boolean checkLinks(Elements links, int count) {
        return true;
    }
}
