package FxPages;

import GraphUI.GraphView;
import WebScrapper.WebScrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import WebScrapper.*;

/**
 * Creates the criteria page
 * @author Afonso Cunha and Marco Pereira
 */

public class CriteriaPage extends BorderPane {
    private RadioButton rBtn1, rBtn2, rBtn3;
    private Button button;
    private Label label;
    private TextField textField;
    private Stage stage;
    private WebScrapper webScrapper;
    private String initialWebpage;

    /**
     * Creates an instance of the criteria page
     * @param stage stage where the page will open
     * @param webScrapper webscrapper to alter the criteria
     * @param initialWebpage fisrt website to be visited
     */
    public CriteriaPage(Stage stage, WebScrapper webScrapper, String initialWebpage) {
        textField = new TextField("Please select a number for the criteria");
        label = new Label("Criteria:");
        rBtn1 = new RadioButton("Visited Pages");
        rBtn2 = new RadioButton("Depth");
        rBtn3 = new RadioButton("N Links");
        button = new Button("Start WebCrawler");
        this.stage = stage;
        this.webScrapper = webScrapper;
        this.initialWebpage = initialWebpage;
        setPositions();
        setButtonAction();
    }

    private void setPositions(){
        VBox vBox = new VBox();
        ToggleGroup group = new ToggleGroup();

        setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(0, 100, 0, 100));
        vBox.getChildren().add(textField);
        vBox.getChildren().add(label);
        vBox.getChildren().addAll(rBtn1, rBtn2, rBtn3, button);

    }

    private void setButtonAction(){
        button.setOnAction(e -> {
            GraphView graphView = null;
            if (rBtn1.isSelected()){
                createGraphView(graphView);
            } else if (rBtn2.isSelected()){
                webScrapper.setIterationStrategy(new DepthCriteria());
                createGraphView(graphView);
            } else if (rBtn3.isSelected()){
                webScrapper.setIterationStrategy(new LinksCriteria());
                createGraphView(graphView);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Wrong choice");
                errorAlert.setContentText("Please select a criteria");
                errorAlert.showAndWait();
            }
        });
    }

    private void createGraphView(GraphView graphView){
        graphView = new GraphView(webScrapper, stage, Integer.parseInt(textField.getText()));

        stage.setTitle("JavaFXGraph Visualization");
        Scene sceneAux = new Scene(graphView.getGraphView(), 1024, 768);
        stage.setScene(sceneAux);
        stage.show();

        graphView.getGraphView().init();
        graphView.turnGraphRealTime(initialWebpage);
    }
}
