package FxPages;

import Graph.DirectGraph;
import Graph.InvalidVertexException;
import Graph.Vertex;
import GraphViewPackage.SmartCircularSortedPlacementStrategy;
import GraphViewPackage.SmartGraphPanel;
import GraphViewPackage.SmartPlacementStrategy;
import WebScrapper.Connection;
import WebScrapper.Webpage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

/**
 * Creates the page where the path from the origin to a vertex will be visualized
 * @author Afonso Cunha and Marco Pereira
 */

public class TreeView extends VBox {
    private SmartGraphPanel<Webpage, Connection> graphView;
    private DirectGraph<Webpage, Connection> g;
    private final SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
    
    /**
     * Cretaes the page itself, setting up everything necessary
     * @param g The graph that will be analyzed that has the information necessary 
     * of the remaining vertexes and edges
     * @param vertex final vertex
     */
    public TreeView(DirectGraph<Webpage, Connection> g, Vertex<Webpage> vertex) {
        this.g = g;
        setNodes(vertex);
    }

    private void setNodes(Vertex<Webpage> vertex){
        List<Vertex<Webpage>> list = g.BFSearch(vertex);
        DirectGraph<Webpage, Connection> graph = new DirectGraph<Webpage, Connection>();

        if (list != null){
            for (int i = 0; i < list.size() - 1; i++){
                Vertex<Webpage> vertex1 = list.get(i);
                Vertex<Webpage> vertex2 = list.get(i+1);
                try {
                    graph.insertVertex(vertex2.element());
                    graph.insertVertex(vertex1.element());
                } catch (InvalidVertexException ignored){

                }
                graph.insertEdge(vertex1.element(), vertex2.element(), new Connection(null, null));
            }

            graphView = new SmartGraphPanel<Webpage, Connection>(graph, strategy);
            graphView.setAutomaticLayout(true);

            Stage stageAux = new Stage();
            stageAux.setScene(new Scene(graphView,1000,800));
            stageAux.initModality(Modality.APPLICATION_MODAL);
            stageAux.show();

            graphView.init();
        }
    }
}
