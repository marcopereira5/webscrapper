import GraphUI.GraphView;
import FxPages.InitialPage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainApplication extends Application{
    private volatile GraphView graphView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        Stage stage = new Stage(StageStyle.DECORATED);
        BorderPane primary = new InitialPage(stage);
        Scene scene = new Scene(primary, 1024, 768);
        stage.setTitle("JavaFXGraph Visualization");
        stage.setScene(scene);
        stage.show();
    }

}
