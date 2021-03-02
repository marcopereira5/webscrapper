package WebScrapper;

import Persistence.Logs;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class determines what is a webpage
 * A webpage is determined by an URL and a Title
 * It also as an attribute called notFound for when the page is not up
 */
public class Webpage implements Serializable {
    private String url, title;
    private boolean notFound;
    private Logs log;

    /**
     * Creates a webpage
     * @param url
     * @param title
     * @param notFound
     */
    Webpage(String url, String title, boolean notFound) {
        this.url = url;
        this.title = title;
        this.notFound = notFound;
        log = null;
    }

    /**
     * Returns the page URL
     * @return string
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the page title
     * @return string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the attribute not found
     * @return boolean
     */
    public boolean isNotFound() {
        return notFound;
    }
    
    /**
     * Returns the title
     * @return string
     */
    @Override
    public String toString() {
        return title;
    }
    
    /**
     * Method to compare two objects of the same class
     * @param o object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Webpage webpage = (Webpage) o;
        return Objects.equals(url, webpage.url) &&
                Objects.equals(title, webpage.title);
    }
    
    /**
     * Hashcode
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(url, title);
    }
    
    /**
     * Set's the log of the website
     * @param log log to applied
     */
    public void setLog(Logs log) {
        this.log = log;
    }
}
