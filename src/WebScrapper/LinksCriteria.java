package WebScrapper;

import org.jsoup.select.Elements;

/**
 * Implements the Links criteria
 */
public class LinksCriteria implements IterationStrategy{
    @Override
    public int visitedPagesCount() {
        return 0;
    }

    @Override
    public int depthCount() {
        return 0;
    }

    @Override
    public boolean checkLinks(Elements links, int count) {
        if (links.size() > count){
            return false;
        }

        return true;
    }
}
