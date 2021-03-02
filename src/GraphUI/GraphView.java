package GraphUI;

import FxPages.SideList;
import FxPages.TreeView;
import Graph.DirectGraph;
import Graph.Vertex;
import GraphViewPackage.SmartCircularSortedPlacementStrategy;
import GraphViewPackage.SmartGraphPanel;
import GraphViewPackage.SmartPlacementStrategy;
import UndoFeature.GraphViewCaretaker;
import UndoFeature.Memento;
import WebScrapper.*;
import com.google.gson.internal.$Gson$Preconditions;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

public class GraphView implements Observer {
    private WebScrapper webScrapper;
    private SmartGraphPanel<Webpage, Connection> graphView;
    private DirectGraph<Webpage, Connection> g;
    private final SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
    private ListView<String> listView;
    private GraphViewCaretaker graphViewCaretaker;
    private Stage stage;
    private TopBar topBar;
    private boolean isPathFinding = false;
    private int stopCriteria;
    
    /**
     * Creates the graphview
     * @param webScrapper The webscrapper necessary to handle the graph 
     * @param stage The Stage is necessary for the interaction between pages
     */
    public GraphView(WebScrapper webScrapper, Stage stage, int stopCriteria) {
        this.stage = stage;
        graphViewCaretaker = new GraphViewCaretaker();
        this.webScrapper = webScrapper;
        this.webScrapper.addObserver(this);
        this.stopCriteria = stopCriteria;
        createGraphView();
        initUI();
    }
    
    /**
     * creates the graphview for loading purposes
     * @param webScrapper The webscrapper necessary to handle the graph 
     * @param g The graph
     * @param stage The Stage is necessary for the interaction between pages
     */
    public GraphView(WebScrapper webScrapper, DirectGraph<Webpage, Connection> g, Stage stage) {
        graphViewCaretaker = new GraphViewCaretaker();
        this.stage = stage;
        this.webScrapper = webScrapper;
        this.webScrapper.addObserver(this);
        this.g = g;
        graphView = new SmartGraphPanel<Webpage, Connection>(this.g, strategy);
        graphView.setAutomaticLayout(true);
        initUI();
        createPopulateEvent();
        topBar.setFirstVertex(g.getFirstVertex());
    }

    private void createGraphView(){
        g = new DirectGraph<Webpage, Connection>();
        graphView = new SmartGraphPanel<Webpage, Connection>(g, strategy);
        graphView.setAutomaticLayout(true);
    }
    
    /**
     * Set's the color of the special vertex's (initial and error)
     */
    public void startColors(){
        graphView.getStylableVertex(g.getFirstVertex()).setStyle("-fx-fill: #FFFF00; -fx-stroke: black");
        turnVertexNotFoundRed();
        graphView.update();
    }

    private void initUI(){
        SideList sideList = new SideList();
        sideList.setPositions(createUndoButton(), createPathButton());
        graphView.getChildren().add(sideList);
        listView = sideList.getListView();
        createUndoButton();
        createTopBar();
    }
    
    /**
     * Starts the graph in real time
     * @param urlAddress url address of the first website
     */
    public void turnGraphRealTime(String urlAddress){
        new Thread(() -> {
            webScrapper.populateGraph(urlAddress, stopCriteria, g);
            graphView.getStylableVertex(webScrapper.getV()).setStyle("-fx-fill: #FFFF00; -fx-stroke: black");
            turnVertexNotFoundRed();
            graphView.update();
            createPopulateEvent();
            topBar.setFirstVertex(webScrapper.getV());
        }).start();
    }
    
    private void turnVertexNotFoundRed(){
        for (Vertex<Webpage> vertex: g.vertices()){
            if (vertex.element().isNotFound()){
                graphView.getStylableVertex(vertex).setStyle("-fx-fill: #ff0000; -fx-stroke: black");
            }
        }
        graphView.update();
    }

    private void createPopulateEvent(){
        if (webScrapper instanceof WebScrapperIterative){
            setVertexAction();
        } else {
            graphView.setVertexDoubleClickAction(graphVertex -> {
                if(!isPathFinding) {
                    System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
                } else {
                    TreeView treeView = new TreeView(g, graphVertex.getUnderlyingVertex());
                }
            });
        }

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        });
    }

    private void setVertexAction(){
        graphView.setVertexDoubleClickAction(graphVertex -> {
            if (!isPathFinding){
                if (!webScrapper.getVisited().isEmpty()){
                    graphViewCaretaker.saveState(this);
                    new Thread(() -> {
                        webScrapper.populateGraph(graphVertex.getUnderlyingVertex().element().getUrl(), 100, g);
                        turnVertexNotFoundRed();
                    }).start();
                }
            } else {
                TreeView treeView = new TreeView(g, graphVertex.getUnderlyingVertex());
            }
        });

        listView.setOnMouseClicked(e -> {
            graphViewCaretaker.saveState(this);
            new Thread(() -> {
                try {
                    webScrapper.populateGraph(listView.getSelectionModel().getSelectedItem(), 100, g);
                } catch (IllegalArgumentException ignored){

                }
                turnVertexNotFoundRed();
            }).start();
        });
    }

    /**
     * Updates the graph when a vertex or an edge is inserted in the graph
     * @param o instance
     * @param arg object
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WebScrapperIterative){
            WebScrapperIterative e = (WebScrapperIterative) o;
            Platform.runLater(() -> listView.getItems().clear());
            Platform.runLater(() -> listView.getItems().addAll(e.getList()));
        }
        graphView.update();
    }
    
    /**
     * Returns the graphview
     * @return graphview
     */
    public SmartGraphPanel<Webpage, Connection> getGraphView() {
        return graphView;
    }
    
    /**
     * creates a memento
     * @return GraphViewMemento
     */
    public GraphViewMemento createMemento(){
        return new GraphViewMemento(g, webScrapper);
    }
    
    /**
     * Set's a memento
     * @param memento memento to apply
     */
    public void setMemento(Memento memento){
        g.setVertices(((GraphViewMemento) memento).getGraph().getVertices());
        g.setEdges(((GraphViewMemento) memento).getGraph().getEdges());
        webScrapper = ((GraphViewMemento) memento).getWebScrapper();
        webScrapper.addObserver(this);
        graphView.update();
        if (webScrapper instanceof WebScrapperIterative){
            listView.getItems().clear();
            listView.getItems().addAll(((WebScrapperIterative) webScrapper).getList());
        }
    }

    private class GraphViewMemento implements Memento{
        /**
         * Class for implementing the memento for UNDO purposes
         */
        private DirectGraph<Webpage, Connection> graph;
        private WebScrapper webScrapper;
        
        /**
         * Creates a GraphViewMemento
         * @param graph to save
         * @param webScrapper to save
         */
        GraphViewMemento(DirectGraph<Webpage, Connection> graph, WebScrapper webScrapper) {
            this.graph = new DirectGraph<Webpage, Connection>(graph.getVertices(), graph.getEdges());
            if (webScrapper instanceof  WebScrapperIterative){
                this.webScrapper = new WebScrapperIterative(webScrapper.getV(), webScrapper.getVisited(), ((WebScrapperIterative) webScrapper).getList());
            } else {
                this.webScrapper = new WebScrapperAutomatic(webScrapper.getV(), webScrapper.getVisited());
            }
        }
        
        /**
         * Returns the memento graph
         * @return DirectGraph<Webpage, Connection>
         */
        public DirectGraph<Webpage, Connection> getGraph() {
            return this.graph;
        }
           
        /**
         * Return the webscrapper of the memento
         * @return WebScrapper
         */
        WebScrapper getWebScrapper() {
            return webScrapper;
        }
    }

    private Button createUndoButton(){
        Button button = new Button("UNDO");
        button.setOnAction(e -> graphViewCaretaker.restoreState(this));
        return button;
    }

    private Button createPathButton(){
        Button button = new Button("SHORTEST PATH");
        button.setOnAction(e -> {
            if (!isPathFinding){
                isPathFinding = true;
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("ON");
                errorAlert.setContentText("Please select the vertex you want to find the shortest path");
                errorAlert.showAndWait();
            } else {
                isPathFinding = false;
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("OFF");
                errorAlert.setContentText("Now you can resume");
                errorAlert.showAndWait();
            }
        });
        return button;
    }

    private void createTopBar(){
        topBar = new TopBar(stage, g, this);
        graphView.getChildren().add(topBar);
    }

}
