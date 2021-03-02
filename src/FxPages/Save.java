package FxPages;

import Graph.DirectGraph;
import Persistence.DaoFactory;
import WebScrapper.Connection;
import WebScrapper.Webpage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Sets up a page which is reachable when trying to save a graph 
 * @author Afonso Cunha and Marco Pereira
 */

public class Save extends BorderPane {
    private TextField filename;
    private Label label;
    private Button button;
    private DirectGraph<Webpage, Connection> directGraph;
    
    /**
     * Creates the page itself, setting up everything necessary
     * @param directGraph the graph that will be saved
     */
    public Save(DirectGraph<Webpage, Connection> directGraph) {
        filename = new TextField("Filename");
        label = new Label();
        button = new Button("Ok");
        this.directGraph = directGraph;
        setLabel();
        setFilename();
        setButton();
        setPositions();
    }

    private void setLabel(){
        label.setText("Please insert the filename");
        label.setFont(Font.font("Advent Pro", FontWeight.NORMAL,15));
    }

    private void setFilename(){
        filename.setPrefWidth(100);
    }

    private void setButton(){
        button.setOnAction(e -> {
            try {
                if (!filename.getText().trim().equals("")){
                    DaoFactory.createGraphViewDao("file").saveGraph(directGraph, filename.getText());
                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                    errorAlert.setHeaderText("SAVED");
                    errorAlert.setContentText("Your graph as been saved");
                    errorAlert.showAndWait();
                    Stage stage = (Stage) button.getScene().getWindow();
                    stage.close();
                }
            } catch (IOException i){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Name not valid");
                errorAlert.setContentText("Please insert a valid name");
                errorAlert.showAndWait();
            }
        });
    }

    private void setPositions(){
        VBox vbox = new VBox();

        vbox.setPadding(new Insets(10, 50, 10, 50));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, filename, button);
        vbox.setSpacing(50);
        setCenter(vbox);
    }
}
