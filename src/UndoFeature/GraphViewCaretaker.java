package UndoFeature;

import GraphUI.GraphView;

import java.util.Stack;

/**
 * Class used for the Memento patttern studied in PA classes
 * The Caretacker will take care of the mementos of the GraphView states like
 * snapshots of it
 * @author Afonso Cunha and Marco Pereira
 */

public class GraphViewCaretaker {
    Stack<Memento> mementos;
    
    /**
     * Creates the caretaker
     */
    public GraphViewCaretaker() {
        this.mementos = new Stack<>();
    }
    
    /**
     * Saves the state of a graphview
     * @param graphView The graph view in study
     */
    public void saveState(GraphView graphView){
        Memento objMemento = graphView.createMemento();
        mementos.push(objMemento);
    }
    
    /**
     * Restores the state of a graphview, just like an undo function
     * @param graphView The graph view in sudy
     */
    public void restoreState(GraphView graphView){
        if (mementos.isEmpty()) return;
        Memento memento = mementos.pop();
        graphView.setMemento(memento);
    }
}
