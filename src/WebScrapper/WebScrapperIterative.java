package WebScrapper;

import Graph.DirectGraph;
import Graph.InvalidEdgeException;
import Graph.InvalidVertexException;
import Graph.Vertex;
import Persistence.Logs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * This class defines the iterative part of the webscrapper
 * @author Afonso Cunha and Marco Pereira
 */

public class WebScrapperIterative extends WebScrapper{

    private List<String> list;
    
    /**
     * Creates an interactive webscrapper
     */
    public WebScrapperIterative() {
        super();
        list = new ArrayList<>();
    }
    
    /**
     * Second constructor for UNDO purposes (memento)
     * @param v vertex
     * @param visited visited list
     * @param list list
     */
    public WebScrapperIterative(Vertex<Webpage> v, Set<Vertex<Webpage>> visited, List<String> list) {
        super(v, visited);
        this.list = new ArrayList<>(list);
    }
    
    /**
     * Starts the iteration process one time, for one vertex
     * @param urlAddress url address where the iteration will run
     * @param count in this case the count parameter will be ignored
     * @param graph the graph to add the vertex's and edge's
     */
    @Override
    public void populateGraph(String urlAddress, int count, DirectGraph<Webpage, Connection> graph) {
        Document doc = null;

        try {
            doc = Jsoup.connect(urlAddress).userAgent("Mozilla").get();
        } catch (IOException ignored) {

        }

        Webpage firstSite = new Webpage(urlAddress, doc.title(), false);
        firstSite.setLog(new Logs(doc.title(), urlAddress, doc.title(),doc.select("a[href]").size()));

        try {
            v = graph.insertVertex(firstSite);
        } catch (InvalidVertexException e){
            v = findVertex(firstSite, graph);
        }
        Elements links;
        links = doc.select("a[href]");

        list = getAllLinks(links);
        observerMethods();

       if (!visited.contains(v)){
           for (Map.Entry<Vertex<Webpage>, Connection> entrys : pagesToVertex(links, v, graph).entrySet()) {
               try {
                   graph.insertEdge(v.element(), entrys.getKey().element(), entrys.getValue());
                   observerMethods();
               } catch (InvalidEdgeException | NullPointerException ignored){

               }
           }
           visited.add(v);
       }
    }


    private Map<Vertex<Webpage>, Connection> pagesToVertex(Elements links, Vertex<Webpage> parent, DirectGraph<Webpage, Connection> graph){
        Map<Vertex<Webpage>, Connection> map = new HashMap<>();
        Document aux = null;

        if (links != null){
            for (Element link : links) {
                Webpage newWebpage = createWebPage(link, parent);

                Vertex<Webpage> linkVertice = createLinkVertex(graph, newWebpage);

                Connection connection = new Connection(link.attr("abs:href"), link.text());

                if (linkVertice != null) {
                    map.put(linkVertice, connection);
                    printElements(parent.element(), linkVertice.element(), connection);
                }
            }
        }
        return map;
    }

    private Vertex<Webpage> findVertex(Webpage webpage, DirectGraph<Webpage, Connection> graph){
        for (Vertex<Webpage> v : graph.vertices()){
            if (v.element().equals(webpage)){
                return v;
            }
        }

        return null;
    }
    
    /**
     * Returns the list of links
     * @return List
     */
    public List<String> getList() {
        return list;
    }

    private List<String> getAllLinks(Elements elements){
        List<String> list = new ArrayList<>();

        for (Element element : elements) {
            list.add(element.attr("abs:href"));
        }

        return list;
    }
    
    /**
     * Set's the list of links
     * @param list list to be setted 
     */
    public void setList(List<String> list) {
        this.list = list;
    }
}
