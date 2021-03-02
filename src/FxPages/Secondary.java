package FxPages;

import GraphUI.GraphView;
import WebScrapper.WebScrapper;
import WebScrapper.WebScrapperAutomatic;
import WebScrapper.WebScrapperIterative;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates a page where the user gives the website as the origin of the graph that
 * will be presented. The user can also choose if he will view the graph in automatic ou interative
 * @author Afonso Cunha and Marco Pereira
 */

public class Secondary extends BorderPane {
    private boolean isInteraticve;
    private Stage stage;
    private VBox midLane;
    private VBox botLane;
    private TextField initialWebsite;
    private ToggleGroup group;
    private RadioButton auto;
    private RadioButton iteractive;
    private Button btn;

    /**
     * Creates the page itself, setting up everything necessary 
     * @param stage The Stage is necessary for the interaction between pages
     */
    Secondary(Stage stage){
        setUpVBoxs();
        setUpButton();
        setUpTextField();
        setUpToggles();
        this.stage = stage;
        this.setCenter(midLane);
        this.setBottom(botLane);
        setUpConnections();
    }

    private void setUpButton(){
        btn = new Button();
        btn.setText("Obter Grafo");
        btn.setOnAction(e -> setButtonActions());
    }

    private void setUpVBoxs(){
        midLane = new VBox();
        midLane.setPadding(new Insets(10, 100, 10, 100));
        midLane.setSpacing(15);
        botLane = new VBox();
        botLane.setPadding(new Insets(10, 10, 50, 10));
        botLane.setSpacing(15);
    }

    private void setUpToggles(){
        group = new ToggleGroup();

        auto = new RadioButton("Modo Automático");
        iteractive = new RadioButton("Modo Iterativo");

        auto.setToggleGroup(group);
        iteractive.setToggleGroup(group);

        auto.setOnAction(event -> {
            setInteractive(false);
        });
        iteractive.setOnAction(event -> {
            setInteractive(true);
        });
    }

    private void setInteractive(boolean interactive) {
        isInteraticve = interactive;
    }

    private void setUpConnections(){
        botLane.getChildren().add(btn);
        midLane.getChildren().addAll(initialWebsite,auto,iteractive);

        initialWebsite.setAlignment(Pos.TOP_CENTER);
        btn.setAlignment(Pos.TOP_CENTER);
        botLane.setAlignment(Pos.BOTTOM_CENTER);
        midLane.setAlignment(Pos.CENTER);
    }

    private void setUpTextField(){
        initialWebsite = new TextField("Insira o link inicial");
        initialWebsite.setPrefSize(200,20);
    }
    
    /**
     * Returns link
     * @return the link given by the user
     */
    public String getLink(){
        return initialWebsite.getText();
    }

    private void setButtonActions(){
        GraphView graphView;
        WebScrapper webScrapper;

        if (isInteraticve){
            webScrapper = new WebScrapperIterative();
            graphView = new GraphView(webScrapper, stage, 100);
            if (WebScrapper.checkLink(initialWebsite.getText())){
                stage.setTitle("JavaFXGraph Visualization");
                Scene sceneAux = new Scene(graphView.getGraphView(), 1024, 768);
                stage.setScene(sceneAux);
                stage.show();

                graphView.getGraphView().init();
                graphView.turnGraphRealTime(initialWebsite.getText());
            } else {
                throwErrorAlert();
            }
        } else {
            webScrapper = new WebScrapperAutomatic();
            if (WebScrapper.checkLink(initialWebsite.getText())){
                stage.setTitle("JavaFXGraph Visualization");
                Scene sceneAux = new Scene(new CriteriaPage(stage, webScrapper, initialWebsite.getText()), 1024, 768);
                stage.setScene(sceneAux);
                stage.show();
            } else {
                throwErrorAlert();
            }
        }
    }

    private void throwErrorAlert(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Link não válido");
        errorAlert.setContentText("Por favor insira um link válido");
        errorAlert.showAndWait();
    }
}
