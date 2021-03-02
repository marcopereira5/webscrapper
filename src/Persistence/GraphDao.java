package Persistence;

import Graph.DirectGraph;
import GraphUI.GraphView;
import WebScrapper.Connection;
import WebScrapper.Webpage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface of the DAO pattern
 * @author Afonso Cunha and Marco Pereira
 */

public interface GraphDao {

    void saveGraph(DirectGraph<Webpage, Connection> directGraph, String filename) throws IOException;
    DirectGraph<Webpage, Connection> loadGraph(String filename) throws IOException, ClassNotFoundException;

}
