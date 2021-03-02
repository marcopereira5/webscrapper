package WebScrapper;

import org.jsoup.select.Elements;

/**
 * Interface for the strategy pattern where we will implement the 3 stop criteria
 */
public interface IterationStrategy {
    /**
     * Count's the visited pages
     * @return 1 or 0
     */
    int visitedPagesCount();

    /**
     * Count's the depth
     * @return 1 or 0
     */
    int depthCount();

    /**
     * Check's if the links size are lesser then the criteria
     * @param links links to get the size
     * @param count criteria
     * @return boolean
     */
    boolean checkLinks(Elements links, int count);
}
