package FxPages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Creates a page that presents the user the option of importing or creating a 
 * graphvire
 * @author Afonso Cunha and Marco Pereira
 */

class SavesPage extends BorderPane {
    private Label question;
    private Button btn1, btn2;
    private Stage stage;
    
    /**
     * Creates the page itself, setting up everything necessary
     * @param stage The Stage is necessary for the interaction between pages
     */
    SavesPage(Stage stage) {
        question = new Label();
        btn1 = new Button("Import");
        btn2 = new Button("Create");
        setQuestion();
        setPositions();
        this.stage = stage;
        setButtonActions();
    }

    private void setQuestion() {
        question.setText("Do you want to import a graphview or to create a new one?");
        question.setFont(Font.font("Advent Pro", FontWeight.NORMAL,15));
    }

    private void setPositions(){
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.add(btn1, 0, 1);
        gridPane.add(btn2, 1, 1);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(250);

        vBox.getChildren().addAll(question, gridPane);
        vBox.setSpacing(50);
        vBox.setPadding(new Insets(50, 10, 10, 10));

        vBox.setAlignment(Pos.TOP_CENTER);
        question.setAlignment(Pos.CENTER);

        setCenter(vBox);
    }

    private void setButtonActions(){

        btn1.setOnAction(e -> {
            stage.setScene(new Scene(new LoadGraph(stage), 400, 300));
        });

        btn2.setOnAction(e -> {
            stage.setScene(new Scene(new Secondary(stage),400,300));
            stage.show();
        });
    }
}
