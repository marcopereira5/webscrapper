package Persistence;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

/**
 * /**
 * Defines the logs of a Webpage
 * @author Afonso Cunha and Marco Pereira
 */

public class Logs implements Serializable {
    private String title, link, origin, log;
    private Timestamp time;
    private Date date;
    private int numberOfLinks;
    
    /**
     * Create the log itself
     * @param title title of the page
     * @param link link of page
     * @param origin title of the origin webpage
     * @param numberOfLinks number of hyperlinks
     */
    public Logs(String title, String link, String origin, int numberOfLinks){
        setUpTime();
        this.title = title;
        this.link = link;
        this.origin = origin;
        this.numberOfLinks = numberOfLinks;
        getEntry();
    }
    
    /**
     * Sets up the link
     * @param link new link
     */
    public void setLink(String link) {
        this.link = link;
    }
    
    /**
     * Sets Up the time
     */
    public void setUpTime(){
        date = new Date();
        long aux = date.getTime();
        time = new Timestamp(aux);
    }

    /**
     * A auxilliar method used to get the log
     * @return the log of a webpage
     */
    public String getLog() {
        return log;
    }
    
    /**
     * Sets up the full log in the form of a string
     */
    public void getEntry(){
        String entry = " ";
        entry = "<" + time + "> | <"+ title + "> | <"+ link +"> | <"+origin+ "> | <"+ numberOfLinks + ">";
        this.log = entry;

    }
}