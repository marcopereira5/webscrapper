package Persistence;

import Graph.DirectGraph;
import WebScrapper.Connection;
import WebScrapper.Webpage;

import java.io.*;
/**
 * This class implements the normal type of serialization and deserialization of a file
 * @author Afonso Cunha And Marco Pereira
 */

public class GraphDaoFile implements GraphDao {
    /**
     * Save's the graph
     * @param graphView the graph to be saved
     * @param filename the filename
     * @throws IOException in case of name malfunction
     */
    @Override
    public void saveGraph(DirectGraph<Webpage, Connection> graphView, String filename) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(".\\SavedGraphs\\" + filename + ".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(graphView);
        out.close();
        fileOut.close();
    }
    
    /**
     * Load's the graph
     * @param filename name of the file
     * @return DirectGraph
     * @throws IOException in the of the filename not found
     * @throws ClassNotFoundException in case of the class not being serialized
     */
    @Override
    public DirectGraph<Webpage, Connection> loadGraph(String filename) throws IOException, ClassNotFoundException {
        DirectGraph<Webpage, Connection> e = null;

        FileInputStream fileIn = new FileInputStream(".\\SavedGraphs\\" + filename + ".ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        e = (DirectGraph<Webpage, Connection>) in.readObject();
        in.close();
        fileIn.close();

        return e;
    }
}
