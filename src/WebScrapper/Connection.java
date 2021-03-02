package WebScrapper;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class determines what is a connection. We will be using this class as the edge element in our digraph
 * A connection is determined by an URL and a Link Name
 */
public class Connection implements Serializable {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String url;
    private String name;

    /**
     * Creates a connection
     * @param url url address of the connection
     * @param name name of the connection
     */
    public Connection(String url, String name) {
        this.url = url;
        this.name = name;
        id = count.incrementAndGet();
    }

    /**
     * returns the connection url
     * @return string
     */
    String getUrl() {
        return url;
    }

    /**
     * return the name of the link
     * @return string
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
