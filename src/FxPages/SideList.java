package FxPages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

/**
 * This class is an instace of a VBox where the listView will appear
 */

public class SideList extends VBox {
    ListView<String> listView;

    /**
     * Creates an instance of the side list
     */
    public SideList() {
        listView = new ListView<>();
    }

    /**
     * Set's the positions for the list and buttons
     * @param btn1 undo button
     * @param btn2 tree view button
     */
    public void setPositions(Button btn1, Button btn2){
        getChildren().add(listView);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setMaxHeight(300);
        listView.setMaxWidth(400);
        setPadding(new Insets(100, 25, 25, 25));
        setSpacing(25);
        setAlignment(Pos.TOP_LEFT);
        getChildren().addAll(btn1, btn2);
    }

    /**
     * returns the list view
     * @return ListView
     */
    public ListView<String> getListView() {
        return listView;
    }
}
