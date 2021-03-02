package FxPages;

import Graph.*;
import GraphUI.GraphView;
import Persistence.DaoFactory;
import WebScrapper.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Creates a page. This page is reachable when the user is trying to load a graph
 * @author Afonso Cunha and Marco Pereira
 */

public class LoadGraph extends BorderPane {
    private Label label;
    private TextField textField;
    private Button btn, btn1;
    private Stage stage;
    
    /**
     * Creates the page itself, setting up everythinmg necessary
     * @param stage The Stage is necessary for the interaction between pages
     */
    public LoadGraph(Stage stage) {
        label = new Label();
        textField = new TextField();
        btn = new Button("Ok");
        btn1 = new Button("Quit");
        setUpLabel();
        setUpTextField();
        setPositions();
        setUpButtons();
        this.stage = stage;
    }

    private void setUpButtons(){
        btn.setOnAction(e -> {
            try {
                if (!textField.getText().trim().equals("")){
                    DirectGraph<Webpage, Connection> graph = DaoFactory.createGraphViewDao("file").loadGraph(textField.getText());
                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                    errorAlert.setHeaderText("Loaded");
                    errorAlert.setContentText("Graph Loaded");
                    errorAlert.showAndWait();
                    Stage stage = (Stage) btn.getScene().getWindow();
                    startGraphView(stage, graph);
                }
            } catch (IOException | ClassNotFoundException e1) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("File name not valid");
                errorAlert.setContentText("Please insert a valid name");
                errorAlert.showAndWait();
            }
        });

        btn1.setOnAction(e -> {
            stage.setScene(new Scene(new Secondary(stage), 400, 300));
            stage.show();
        });
    }

    private void setUpLabel(){
        label.setText("Please insert the name of the file");
        label.setFont(Font.font("Advent Pro", FontWeight.NORMAL,20));
    }

    private void setUpTextField(){
        textField.setText("Name of the file.");
        textField.setPrefWidth(100);
    }

    private void setPositions(){
        VBox vbox = new VBox();

        GridPane gridPane = new GridPane();
        gridPane.add(btn, 0, 1);
        gridPane.add(btn1, 1, 1);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(250);

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, textField, gridPane);
        vbox.setSpacing(30);

        setCenter(vbox);
    }
    
    /**
     * starts the graphview with the saved graph
     * @param stage stage to open
     * @param g graph to open
     */
    private void startGraphView(Stage stage, DirectGraph<Webpage, Connection> g){
        GraphView graphView = new GraphView(new WebScrapperIterative(), g, stage);

        stage.setTitle("JavaFXGraph Visualization");
        Scene sceneAux = new Scene(graphView.getGraphView(), 1024, 768);
        stage.setScene(sceneAux);
        stage.show();

        graphView.getGraphView().init();
        graphView.getGraphView().update();
        graphView.startColors();
        //graphView.turnGraphRealTime(initialWebsite.getText());
    }


}
