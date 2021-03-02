package FxPages;

import Graph.DirectGraph;
import Graph.Vertex;
import WebScrapper.*;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Creates the page of the statistics where the user can get some information
 * about a graph
 * @author Afonso Cunha and Marco Pereira
 */

public class Statistics extends BorderPane {
    private Label visitedPages, title, notFoundWebSites, mostReferencedPage, top5;
    private BarChart<String, Number> chart;
    
    /**
     * Creates an instance of statistics 
     * @param g the graph where the statistics will apply 
     */
    public Statistics(DirectGraph<Webpage, Connection> g) {
        setTexts();
        visitedPages = new Label();
        notFoundWebSites = new Label();
        mostReferencedPage = new Label();
        top5 = new Label();
        chart = createBarChart(g);
    }
    
    private BarChart<String, Number> createBarChart(DirectGraph<Webpage, Connection> g) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of references");
        xAxis.setLabel("Website title");

        BarChart<String,Number> bc =
                new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        int i = 0;

        for (Map.Entry<Vertex<Webpage>, Integer> entry : g.returnTop5().entrySet()) {
            if (i != 5){
                i++;
                series1.getData().add(new XYChart.Data<>(entry.getKey().element().getTitle(), entry.getValue()));
            }
        }

        bc.getData().add(series1);

        return bc;
    }
    
    /**
     * Set the top text
     */
    public void setTexts(){
        title = new Label("Statistics");
        title.setFont(Font.font("Advent Pro", FontWeight.BOLD,50));
    }
    
    /**
     * set the text of visited pages to int that will be passed later
     * @param num number of visited pages
     */
    public void setVisitedPages(int num){
        visitedPages.setText("Number of visited pages: " + num);
        visitedPages.setFont(Font.font("Advent Pro", FontWeight.NORMAL,20));
        visitedPages.setAlignment(Pos.CENTER);
    }
    
    /**
     * Set the text of notfoundWebsites to the number of websites with errors
     * @param num number of websites with errors
     */
    public void setNotFoundWebSites(int num){
        notFoundWebSites.setText("Number of distint pages not found: " + num);
        notFoundWebSites.setFont(Font.font("Advent Pro", FontWeight.NORMAL,20));
    }
    
    /**
     * Set's the text of mostReferenced page with the most referenced page in the graph
     * @param name most referenced page in the graph
     */
    public void setMostReferencedPage(String name){
        mostReferencedPage.setText("Most referenced pages: " + name);
        mostReferencedPage.setFont(Font.font("Advent Pro", FontWeight.NORMAL,20));
    }
    
    /**
     * Set's the text of the top5 with the name of the top 5 most visited vertex's
     * @param name name of the 5 vertex
     */
    public void setTop5(String name){
        top5.setText("Top 5: " + name);
        top5.setFont(Font.font("Advent Pro", FontWeight.NORMAL,20));;
    }
    
    /**
     * Set's up the positions
     */
    public void setPositions(){
        VBox vbox = new VBox();

        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(15);

        vbox.getChildren().addAll(title, visitedPages, notFoundWebSites, mostReferencedPage, top5, chart);

        mostReferencedPage.setAlignment(Pos.CENTER);
        notFoundWebSites.setAlignment(Pos.CENTER);
        visitedPages.setAlignment(Pos.CENTER);
        title.setAlignment(Pos.CENTER);
        top5.setAlignment(Pos.CENTER);

        vbox.setPadding(new Insets(50, 10, 10, 10));

        setCenter(vbox);
    }
    
    /**
     * Reset's the label's
     */
    public void resetLabels(){
        visitedPages.setText("");
        mostReferencedPage.setText("");
        notFoundWebSites.setText("");
    }
}
