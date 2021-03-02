package GraphUI;

import FxPages.Save;
import FxPages.Statistics;
import Graph.DirectGraph;
import Graph.Vertex;
import Persistence.DaoFactory;
import WebScrapper.Connection;
import WebScrapper.Webpage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

/**
 * Creates the top bar where the user can save and see the statistics
 * @author Afonso Cunha and Marco Pereira
 */

class TopBar extends VBox{
    private MenuBar menuBar;
    private Stage stage;
    private Statistics statistics;
    private DirectGraph<Webpage, Connection> g;
    private Vertex<Webpage> firstVertex;
    
    /**
     * Creates the top bar
     * @param stage The Stage is necessary for the interaction between pages
     * @param g The graph that will be saved or from who the statistics will be calculated
     * @param graphView 
     */
    TopBar(Stage stage, DirectGraph<Webpage, Connection> g, GraphView graphView) {
        this.stage = stage;
        this.g = g;
        menuBar();
        createStatistics();
    }
    
    /**
     * Sets the first vertex
     * @param firstVertex the first vertex
     */
    void setFirstVertex(Vertex<Webpage> firstVertex) {
        this.firstVertex = firstVertex;
    }

    private void menuBar(){
        Menu menu = new Menu("Options");
        menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        MenuItem menuItem1 = new MenuItem("Save");
        MenuItem menuItem2 = new MenuItem("Statistics");

        menuItem2.setOnAction(e -> {
            Stage stageAux = new Stage();
            stageAux.setScene(new Scene(createStatistics(),1000,800));
            stageAux.initModality(Modality.APPLICATION_MODAL);
            stageAux.show();
        });

        menuItem1.setOnAction(e -> {
            Stage stageAux2 = new Stage();
            stageAux2.setScene(new Scene(new Save(g),500,400));
            stageAux2.initModality(Modality.APPLICATION_MODAL);
            stageAux2.show();
        });

        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);

        getChildren().add(menuBar);
    }

    private Statistics createStatistics(){
        statistics = new Statistics(g);
        statistics.resetLabels();
        statistics.setVisitedPages(g.numVertices());
        statistics.setNotFoundWebSites(countNotFound());
        statistics.setPositions();
        setMostVisitedPage();
        return statistics;
    }

    private void setMostVisitedPage(){
        StringBuilder list = new StringBuilder();
        if (g.vertices().size() != 0){
            for (Vertex<Webpage> v : g.mostReferencedVertex()){
                list.append(v.element().getTitle()).append(" || ");
            }

            statistics.setMostReferencedPage(list.toString().substring(0, list.length() - 3));
        }
    }



    private int countNotFound(){
        int count = 0;

        if (firstVertex != null){
            for (Map.Entry<Vertex<Webpage>, List<Webpage>> entrys : g.BFS(firstVertex).entrySet()){
                for (int i = 0; i < entrys.getValue().size(); i++){
                    if (entrys.getValue().get(i).isNotFound()){
                        count++;
                    }
                }
            }
        }

        return count;
    }
}
