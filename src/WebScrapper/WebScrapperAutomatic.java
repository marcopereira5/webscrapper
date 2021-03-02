package WebScrapper;

import Graph.DirectGraph;
import Graph.InvalidEdgeException;
import Graph.Vertex;
import Persistence.Logs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * This class defines the automatic part of the webscrapper
 * @author Afonso Cunha and Marco Pereira
*/

public class WebScrapperAutomatic extends WebScrapper{

    public WebScrapperAutomatic() {
        super();
    }

    public WebScrapperAutomatic(Vertex<Webpage> v, Set<Vertex<Webpage>> visited) {
        super(v, visited);
    }
    
    /**
     * Run's the scrapping all the way until it reaches the count
     * @param urlAddress address to be scrapped
     * @param numberOfIterations number of times the scrapping will occur
     * @param graph the graph where the vertex's and edges will be added
     */
    @Override
    public void populateGraph(String urlAddress, int numberOfIterations, DirectGraph<Webpage, Connection> graph){
        Queue<Vertex<Webpage>> queue = new LinkedList<>();

        int count = 0;

        Document doc = null;

        try {
            doc = Jsoup.connect(urlAddress).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Webpage firstSite = new Webpage(urlAddress, doc.title(), false);
        firstSite.setLog(new Logs(doc.title(), urlAddress, doc.title(),doc.select("a[href]").size()));

        v = graph.insertVertex(firstSite);
        observerMethods();

        queue.add(v);
        Vertex<Webpage> v2;
        count += getIterationStrategy().visitedPagesCount();
        Elements links;

        while(!queue.isEmpty() && count < numberOfIterations){
            v2 = queue.poll();
            visited.add(v2);
            if (!v2.equals(v)){
                count += getIterationStrategy().depthCount();
            }

            try {
                doc = Jsoup.connect(v2.element().getUrl()).userAgent("Mozilla").get();
                links = doc.select("a[href]");
            } catch (IOException e) {
                links = null;
            }

            if (links != null){
                if (getIterationStrategy().checkLinks(links, numberOfIterations)){
                    for (Map.Entry<Vertex<Webpage>, Connection> entrys : pagesToVertex(links, v2, numberOfIterations, count, graph).entrySet()) {
                        try {
                            graph.insertEdge(v2.element(), entrys.getKey().element(), entrys.getValue());
                            observerMethods();
                        } catch (InvalidEdgeException | NullPointerException ignored){

                        }
                        if (!visited.contains(entrys.getKey())){
                            queue.offer(entrys.getKey());
                            count += getIterationStrategy().visitedPagesCount();
                        }

                        if (count == numberOfIterations) break;
                    }
                }
            }
        }
    }

    private Map<Vertex<Webpage>, Connection> pagesToVertex(Elements links, Vertex<Webpage> parent, int numberOfIterations, int count, DirectGraph<Webpage, Connection> graph){
        Map<Vertex<Webpage>, Connection> map = new HashMap<>();

        if (links != null){
            for (Element link : links) {
                if (count < numberOfIterations){

                    Webpage newWebpage = createWebPage(link, parent);

                    Vertex<Webpage> linkVertice = createLinkVertex(graph, newWebpage);

                    Connection connection = new Connection(link.attr("abs:href"), link.text());

                    if (linkVertice != null) {
                        map.put(linkVertice, connection);
                        count += getIterationStrategy().visitedPagesCount();
                    }
                }
            }
        }
        return map;
    }


}
