package Persistence;

import Graph.DirectGraph;
import Graph.Edge;
import Graph.Vertex;
import WebScrapper.Connection;
import WebScrapper.Webpage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
/**
 * This class implements the json type of serialization and deserialization of a file
 * @author Afonso Cunha and Marco Pereira
 */
public class GraphDaoJson implements GraphDao{
     /**
     * Save's the graph
     * @param directGraph the graph to be saved
     * @param filename the filename
     * @throws IOException in case of name malfunction
     */
    @Override
    public void saveGraph(DirectGraph<Webpage, Connection> directGraph, String filename) throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String s1 = gson.toJson(directGraph.getVertices());
        String s2 = gson.toJson(directGraph.getEdges());
        String s3 = gson.toJson(directGraph.getFirstVertex());
        System.out.println(s2);
        new File(".\\SavedGraphs\\" + filename).mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(".\\SavedGraphs\\" + filename + "\\vertex.json"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(".\\SavedGraphs\\" + filename + "\\edges.json"));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(".\\SavedGraphs\\" + filename + "\\firstVertex.json"));
        writer.write(s1);
        writer1.write(s2);
        writer2.write(s3);

        writer.close();
        writer1.close();
        writer2.close();
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

        InputStream is = new FileInputStream(".\\SavedGraphs\\" + filename + "\\edges.json");
        InputStream is1 = new FileInputStream(".\\SavedGraphs\\" + filename + "\\vertex.json");
        InputStream is2 = new FileInputStream(".\\SavedGraphs\\" + filename + "\\firstVertex.json");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        BufferedReader buf1 = new BufferedReader(new InputStreamReader(is1));
        BufferedReader buf2 = new BufferedReader(new InputStreamReader(is2));

        String line = buf.readLine();
        String line1 = buf1.readLine();
        String line2 = buf2.readLine();

        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        line = buf.readLine();
        sb.append(line);

        line1 = buf1.readLine();
        sb1.append(line1);

        line2 = buf2.readLine();
        sb2.append(line2);

        Gson gson = new Gson();

        Type type = new TypeToken<Map<Webpage, Vertex<Webpage>>>(){}.getType();
        Map<Webpage, Vertex<Webpage>> vertexHashMap = gson.fromJson(sb.toString(), type);

        Type type1 = new TypeToken<Map<Connection, Vertex<Connection>>>(){}.getType();
        Map<Connection, Edge<Connection, Webpage>> edgesToHashMap = gson.fromJson(sb1.toString(), type1);

        Type type2 = new TypeToken<Vertex<Webpage>>(){}.getType();
        Vertex<Webpage> vertex = gson.fromJson(sb2.toString(), type2);

        DirectGraph<Webpage, Connection> directGraph = new DirectGraph<Webpage, Connection>(vertexHashMap, edgesToHashMap);

        return directGraph;
    }

}
