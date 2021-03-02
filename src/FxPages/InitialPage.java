package FxPages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The Setup For the initial page of our project
 * @author Afonso Cunha and Marco Pereira
 */

public class InitialPage extends BorderPane {
    private Button btn;
    private Label title;
    private Label names;
    private VBox midLane;
    private VBox botLane;
    private Stage stage;
    private ImageView imageView;
    
    /**
     * Creates the page itself, setting up everythinmg necessary
     * @param stage The Stage is necessary for the interaction between pages
     */
    public InitialPage(Stage stage){
        setUpButton();
        setUpLabels();
        setUpImages();
        setUpStruct();

        this.setCenter(midLane);
        this.setBottom(botLane);
        this.stage = stage;
    }

    private void setUpButton(){
        btn = new Button();
        btn.setText("Iniciar Crawler");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.setScene(new Scene(new SavesPage(stage),500,250));
                stage.show();
            }
        });
    }

    private  void setUpLabels(){
        title = new Label("Web Crawler");
        names = new Label("Autores: \n Marco Pereira nº180221019 \n Afonso Cunha nº180221017");
        title.setFont(Font.font("Advent Pro", FontWeight.BOLD,50));
        names.setFont(Font.font("Abel",FontWeight.BOLD,20));
        names.setTextAlignment(TextAlignment.CENTER);
    }

    private void setUpImages(){
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(".\\Images\\crawler.jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(inputstream);
        imageView = new ImageView(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(450);
    }

    private void setUpStruct(){
        midLane = new VBox();
        botLane = new VBox();

        botLane.setPadding(new Insets(10, 10, 100, 10));
        botLane.setSpacing(15);
        midLane.setPadding(new Insets(10));
        midLane.setSpacing(15);

        botLane.getChildren().add(btn);
        midLane.getChildren().add(title);
        midLane.getChildren().add(imageView);
        midLane.getChildren().add(names);


        title.setAlignment(Pos.CENTER);
        names.setAlignment(Pos.BOTTOM_CENTER);
        btn.setAlignment(Pos.TOP_CENTER);

        botLane.setAlignment(Pos.BOTTOM_CENTER);
        midLane.setAlignment(Pos.CENTER);
    }

}
