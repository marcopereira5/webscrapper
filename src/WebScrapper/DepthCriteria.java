package WebScrapper;

import org.jsoup.select.Elements;

/**
 * Implements the Depth Criteria
 */
public class DepthCriteria implements IterationStrategy {

    @Override
    public int visitedPagesCount() {
        return 0;
    }

    @Override
    public int depthCount() {
        return 1;
    }

    @Override
    public boolean checkLinks(Elements links, int count) {
        return true;
    }
}
