package WebScrapper;

import Graph.DirectGraph;
import Graph.Graph;
import Graph.InvalidVertexException;
import Graph.Vertex;
import Persistence.Logs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * This class is the web crawler that will be used to scrape a website populating our digraph with the webpages
 * and existing connections
 */

public abstract class WebScrapper extends Observable{
    Vertex<Webpage> v;
    Set<Vertex<Webpage>> visited;
    private IterationStrategy iterationStrategy;

    /**
     * Creates a webscrapper
     */
    public WebScrapper() {
        visited = new HashSet<>();
        iterationStrategy = new VisitedPagesCriteria();
    }

    public WebScrapper(Vertex<Webpage> v, Set<Vertex<Webpage>> visited) {
        this.v = v;
        this.visited = new HashSet<>(visited);
        iterationStrategy = new VisitedPagesCriteria();
    }

    /**
     * * Populates the graph with the websites and respective connections
     * @param urlAddress url address where the iteration starts
     * @param numberOfIterations number of iterations
     * @param graph graph where the vertex's and edge's will be added
     */
    public abstract void populateGraph(String urlAddress, int numberOfIterations, DirectGraph<Webpage, Connection> graph);

    /**
     * Print's the elements of the graph
     * @param pag1 webpage 1
     * @param pag2 webpage 2
     * @param con connection
     */
    void printElements(Webpage pag1, Webpage pag2, Connection con){
        System.out.println("FROM " + pag1.getTitle() + "\nTO " + pag2.getTitle() + "\nORIGIN LINK:  " + pag1.getUrl() + "\nDESTINATION URL:" + con.getUrl() + "\n---------------------------------");
    }
    
    /**
     * Returns the first vertex that was added to the graph
     * @return Vertex
     */
    public Vertex<Webpage> getV() {
        return v;
    }
    
    /**
     * Observer methods 
     */
    void observerMethods(){
        setChanged();
        notifyObservers();
    }
    
    /**
     * Returns the visited list
     * @return Set
     */
    public Set<Vertex<Webpage>> getVisited() {
        return visited;
    }
    
    /**
     * Set the first vertex
     * @param v vertex
     */
    public void setV(Vertex<Webpage> v) {
        this.v = v;
    }
    
    /**
     * Check's if a link is valid
     * @param urlAddress address
     * @return boolean
     */
    public static boolean checkLink(String urlAddress){
        try {
            Document doc = Jsoup.connect(urlAddress).userAgent("Mozilla").get();
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    /**
     * Creates a webpage
     * @param link link of the webpage
     * @param parent parent of the webpage
     * @return
     */
    public Webpage createWebPage(Element link, Vertex<Webpage> parent){
        Webpage newWebpage;
        try {
            Document aux = Jsoup.connect(link.attr("abs:href")).userAgent("Mozilla").get();
            newWebpage = new Webpage(link.attr("abs:href"), aux.title(), false);
            newWebpage.setLog(new Logs(aux.title(),newWebpage.getUrl(),parent.element().getTitle(), aux.select("a[href]").size()));
        } catch (UnknownHostException ex1) {
            System.out.println(link.attr("abs:href") + "Invalid link");
            newWebpage = new Webpage(link.attr("abs:href"), "404 not found", true);
            newWebpage.setLog(new Logs("404 not found", newWebpage.getUrl(), parent.element().getTitle(),0));
        } catch (IOException ex2) {
            newWebpage = new Webpage(link.attr("abs:href"), "404 not found", true);
            newWebpage.setLog(new Logs("404 not found", newWebpage.getUrl(), parent.element().getTitle(),0));
        }

        return newWebpage;
    }

    /**
     * Creates the link vertex
     * @param graph graph to check if wether the webpage exists
     * @param newWebpage page to be added to the graph
     * @return Vertex
     */
    public Vertex<Webpage> createLinkVertex(Graph<Webpage, Connection> graph, Webpage newWebpage){
        Vertex<Webpage> linkVertice = null;

        try {
            linkVertice = graph.insertVertex(newWebpage);
            observerMethods();
        } catch (InvalidVertexException ignored) {
            for (Vertex<Webpage> webpage : graph.vertices()){
                if (webpage.element().equals(newWebpage)){
                    linkVertice = webpage;
                }
            }
        }

        return linkVertice;
    }

    /**
     * Return the strategy implemented
     * @return Iteration Strategy
     */
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set's the specified criteria
     * @param iterationStrategy strategy to be applied
     */
    public void setIterationStrategy(IterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }
}
