/**
 * This class is the implementation of a direct graph
 */

package Graph;

import java.io.Serializable;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class DirectGraph<V, E> implements Digraph<V, E>, Serializable {
    private Map<V, Vertex<V>> vertices;
    private Map<E, Edge<E,V>> edges;
    private Vertex<V> firstVertex;

    public DirectGraph(){
        vertices = new HashMap<V, Vertex<V>>();
        edges = new HashMap<E, Edge<E, V>>();
    }

    public DirectGraph(Map<V, Vertex<V>> vertices, Map<E, Edge<E,V>> edges){
        this.vertices = new HashMap<>(vertices);
        this.edges = new HashMap<>(edges);
    }

    public Map<V, Vertex<V>> getVertices() {
        return vertices;
    }

    public Map<E, Edge<E, V>> getEdges() {
        return edges;
    }

    public void setVertices(Map<V, Vertex<V>> vertices) {
        this.vertices = vertices;
    }

    public void setEdges(Map<E, Edge<E, V>> edges) {
        this.edges = edges;
    }

    private MyVertex vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.element().equals(vElement)) {
                return (MyVertex) v;
            }
        }
        return null;
    }

    private Collection<Vertex<V>> getAdjacent(Vertex<V> v) throws InvalidVertexException{
        List<Vertex<V>> list = new ArrayList<>();
        if (!this.vertices.containsValue(v)){
            throw new InvalidVertexException("This vertex does not exist");
        }

        for (Edge<E, V> edge : outboundEdges(v)){
            list.add(edge.vertices()[1]);
        }

        return list;
    }

    /**
     * Returns all the incident edges from a vertex. An incident edge is defined by the direction of an edge,
     * if the edge is pointing to the vertex, then it's an incident edge, if it starts on the provided vertex,
     * then its an outbound edge
     * @param inbound     vertex for which to obtain the incident edges
     *
     * @return Collection with all incident edges
     */
    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound) throws InvalidVertexException {
        if (!this.vertices.containsValue(inbound)){
            throw new InvalidVertexException("This vertex does not exist");
        }

        HashSet<Edge<E, V>> set = new HashSet<>();

        for (Edge<E, V> edge : edges.values()){
            if (edge.vertices()[1] == inbound) set.add(edge);
        }

        return set;
    }

    /**
     * Return all outbound edges from a vertex
     * @param outbound     vertex for which to obtain the outbound edges
     *
     * @return Collection with the outbound edges
     * @throws InvalidVertexException invalid vertex
     */
    @Override
    public Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) throws InvalidVertexException {
        if (!this.vertices.containsValue(outbound)){
            throw new InvalidVertexException("This vertex does not exist");
        }

        HashSet<Edge<E, V>> set = new HashSet<>();

        for (Edge<E, V> edge : edges.values()){
            if (edge.vertices()[0] == outbound) set.add(edge);
        }

        return set;
    }

    /**
     * return a boolean telling whether two vertex's are adjacent
     * @param outbound    outbound vertex
     * @param inbound     inbound vertex
     *
     * @return boolean
     * @throws InvalidVertexException invalid vertex
     */
    @Override
    public boolean areAdjacent(Vertex<V> outbound, Vertex<V> inbound) throws InvalidVertexException {
        for(Edge edge: edges.values()){
            if(edge.vertices()[0] == outbound && edge.vertices()[1] == inbound) return true;
        }
        return false;
    }

    /**
     * Insert an edge
     * @param outbound      outbound vertex
     * @param inbound       inbound vertex
     * @param edgeElement   the element to store in the new edge
     *
     * @return the inserted edge
     * @throws InvalidVertexException invalid vertex
     * @throws InvalidEdgeException invalid vertex
     */
    @Override
    public Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) throw new InvalidEdgeException("There's already an edge with this element.");
        if (!vertices.containsValue(outbound) || !vertices.containsValue(inbound)) throw new InvalidVertexException("One of those vertex do not exist");

        MyVertex outBoundVertex = checkVertex(outbound);
        MyVertex inBoundVertex = checkVertex(inbound);

        MyEdge newEdge = new MyEdge(edgeElement, outBoundVertex, inBoundVertex);

        edges.put(edgeElement, newEdge);

        return newEdge;
    }

    /**
     * Does a breadth-first search returning a map where the key is a parent vertex and the values a list with all children vertex's
     * @param v vertex to start with
     * @return Map
     */
    public Map<Vertex<V>, List<V>> BFS(Vertex<V> v){
        List<V> path;
        Map<Vertex<V>, List<V>> map = new HashMap<>();
        Set<Vertex<V>> visited = new HashSet<>();
        Queue<Vertex<V>> queue = new LinkedList<>();

        visited.add(v);
        queue.add(v);

        while(!queue.isEmpty()){
            Vertex<V> vLook = queue.remove();
            path = new ArrayList<>();

            for (Vertex<V> adj : getAdjacent(vLook)){
                if (!visited.contains(adj)){
                    visited.add(adj);
                    path.add(adj.element());
                    queue.add(adj);
                }
            }
            map.put(vLook, path);
        }

        return map;
    }

    public List<Vertex<V>> BFSearch(Vertex<V> vertex){
        Map<Vertex<V>, Vertex<V>> parents = new HashMap<>();
        Queue<Vertex<V>> queue = new LinkedList<>();

        queue.offer(firstVertex);
        parents.put(firstVertex, null);

        while(!queue.isEmpty()){
            Vertex<V> vertexAux = queue.poll();
            List<Vertex<V>> neighbors = (List) getAdjacent(vertexAux);

            for (int i = 0; i < neighbors.size(); i++){
                Vertex<V> neighbor = neighbors.get(i);

                boolean visited = parents.containsKey(neighbor);

                if(visited){
                    continue;
                } else {
                    queue.offer(neighbor);

                    parents.put(neighbor, vertexAux);

                    if(neighbor.equals(vertex)){
                        return getPath(parents, vertex);
                    }
                }
            }
        }
        return null;
    }

    private List<Vertex<V>> getPath(Map<Vertex<V>, Vertex<V>> parents, Vertex<V> vertex) {
        List<Vertex<V>> path = new ArrayList<Vertex<V>>();
        Vertex<V> vertex1 = vertex;

        while (vertex1 != null){
            path.add(0, vertex1);
            Vertex<V> vertex2 = parents.get(vertex1);
            vertex1 = vertex2;
        }

        return path;
    }

    /**
     * Returns the most referenced Vertex. Can return more then one because 2 vertex's can have the same number of incident edges
     * @return Iterable with the vertex's
     */
    public Iterable<Vertex<V>> mostReferencedVertex(){
        List<Vertex<V>> vertexList = new ArrayList<>();
        Vertex<V> v = null;
        int max = 0;

        for (Vertex<V> vertex : vertices.values()){
            if (incidentEdges(vertex).size() > max){
                max = incidentEdges(vertex).size();
                v = vertex;
            }
        }

        vertexList.add(v);

        for (Vertex<V> vertex : vertices.values()){
            if (incidentEdges(vertex).size() == max && vertex != v){
                vertexList.add(vertex);
            }
        }

        return vertexList;
    }

    /**
     * return the number of vertex's
     * @return int
     */
    @Override
    public int numVertices() {
        return vertices.size();
    }

    /**
     * return the number of edges
     * @return int
     */
    @Override
    public int numEdges() {
        return edges.size();
    }

    /**
     * return the vertex's
     * @return Collection with the vertex's
     */
    @Override
    public Collection<Vertex<V>> vertices() {
        return vertices.values();
    }

    /**
     * return the edges
     * @return Collection with the edges
     */
    @Override
    public Collection<Edge<E, V>> edges() {
        return edges.values();
    }

    /**
     * insert a vertex
     * @param vElement      the element to store at the vertex
     *
     * @return the inserted vertex
     * @throws InvalidVertexException invalid vertex
     */
    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        MyVertex newVertex = new MyVertex(vElement);

        vertices.put(vElement, newVertex);

        this.firstVertex = this.firstVertex == null ? newVertex : this.firstVertex;

        return newVertex;
    }

    public Vertex<V> getFirstVertex() {
        return firstVertex;
    }

    /**
     * return whether a vertex exists with the provided element
     * @param vElement
     * @return boolean
     */
    private boolean existsVertexWith(V vElement) {
        if(vElement == null) return false;
        return vertices.containsKey(vElement);
    }

    /**
     * return the opposite vertex, given a vertex and an edge
     * @param v         vertex on one end of <code>e</code>
     * @param e         edge connected to <code>v</code>
     * @return the opposite Vertex
     * @throws InvalidVertexException invalid vertex
     * @throws InvalidEdgeException invalid edge
     */
    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(v);
        MyEdge edge = checkEdge(e);

        if( !edge.contains(v) ) return null; /* this edge does not connect vertex v */

        if(edge.vertices()[0] == v) return edge.vertices()[1];
        else return edge.vertices()[0];
    }

    /**
     * inserts an edge
     * @param outboundElement  outbound vertex's stored element
     * @param inboundElement   inbound vertex's stored element
     * @param edgeElement      element to store in the new edge
     *
     * @return the inserted edge
     * @throws InvalidVertexException invalid vertex
     * @throws InvalidEdgeException invalid edge
     */
    @Override
    public Edge<E, V> insertEdge(V outboundElement, V inboundElement, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }
        if (!existsVertexWith(outboundElement)) {
            throw new InvalidEdgeException("No vertex contains " + outboundElement);
        }
        if (!existsVertexWith(inboundElement)) {
            throw new InvalidEdgeException("No vertex contains " + inboundElement);
        }

        MyVertex outVertex = vertexOf(outboundElement);
        MyVertex inVertex = vertexOf(inboundElement);

        MyEdge newEdge = new MyEdge(edgeElement, outVertex, inVertex);

        edges.put(edgeElement, newEdge);

        return newEdge;
    }

    /**
     * returns whether a edge exists with the given element
     * @param edgeElement
     * @return boolean
     */
    private boolean existsEdgeWith(E edgeElement) {
        return  edges.containsKey(edgeElement);
    }

    /**
     * replace the element of a given edge
     * @param e             edge to replace its element
     * @param newElement    new element to store in <code>e</code>
     *
     * @return old element
     * @throws InvalidEdgeException invalid edge
     */
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        for (Edge edge: edges.values()) {
            if ((edge.vertices()[0] == e.vertices()[0]) && (edge.vertices()[1] == e.vertices()[1]) && edge.element() == newElement){
                throw new IllegalArgumentException("This edge already exists with this element.");
            }
        }
        MyEdge edge = checkEdge(e);
        E oldElement = edge.element;
        edge.element = newElement;

        return oldElement;
    }

    /**
     * replaces the element of a given vertex
     * @param v             vertex to replace its element
     * @param newElement    new element to store in <code>v</code>
     *
     * @return old element
     * @throws InvalidVertexException invalid vertex
     */
    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if(existsVertexWith(newElement)){
            throw  new InvalidVertexException("There's already a vertex with this element");
        }
        MyVertex vertex = checkVertex(v);
        V oldElement = vertex.element;
        vertex.element = newElement;

        return oldElement;
    }

    /**
     * removes an edge
     * @param e     edge to remove
     *
     * @return edge's element
     * @throws InvalidEdgeException invalid edge
     */
    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        checkEdge(e);

        E element = e.element();
        edges.remove(e.element());

        return element;
    }

    public E removeEdge(E element){
        if (edges.containsKey(element)){
            edges.remove(element);
            return element;
        }

        return null;
    }

    /**
     * remove a vertex
     * @param v     vertex to remove
     *
     * @return the element of the edge
     * @throws InvalidVertexException invalid edge
     */
    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        if (!vertices.containsValue(v)){
            throw new InvalidVertexException("The vertex does not exist");
        }
        for (Edge<E, V> e : incidentEdges(v)){
            this.edges.remove(e.element());
        }

        this.vertices.remove(v.element());
        return v.element();
    }

    public LinkedHashMap<Vertex<V>, Integer> returnTop5(){
        HashMap<Vertex<V>, Integer> hashMap = new HashMap<>();

        for (Vertex<V> v : vertices()){
            hashMap.put(v, incidentEdges(v).size());
        }

        return hashMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }


    /*
     *  Code based of the code implemented during PA classes(LAB3)
     * */

    class MyVertex implements Vertex<V>, Serializable{

        V element;

        MyVertex(V element) {
            this.element = element;
        }

        @Override
        public V element() {
            return this.element;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}';
        }
    }

    /*
     *  Code based of the code implemented during PA classes(LAB3)
     * */
    class MyEdge implements Edge<E, V>, Serializable {

        E element;
        Vertex<V> origin;
        Vertex<V> destination;

        MyEdge(E element, Vertex<V> origin, Vertex<V> destination) {
            this.element = element;
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        public E element() {
            return this.element;
        }

        boolean contains(Vertex<V> v) {
            return (origin == v || destination == v);
        }

        @Override
        public Vertex<V>[] vertices() {
            Vertex[] vertex = new Vertex[2];
            vertex[0] =origin;
            vertex[1] = destination;
            return vertex;
        }

        @Override
        public String toString() {
            return "Edge{{" + element + "}, vertexOutbound=" + origin.toString()
                    + ", vertexInbound=" + destination.toString() + '}' + "\n";
        }
    }


    /*
     *  Code based of the code implemented during PA classes(LAB3)
     * */

    /**
     * Checks whether a given vertex is valid and belongs to this graph
     *
     * @param v
     * @return
     * @throws InvalidVertexException
     */
    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {

        MyVertex vertex;

        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsKey(vertex.element)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    /*
     *  Code based of the code implemented during PA classes(LAB3)
     * */
    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {

        MyEdge edge;

        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an adge.");
        }

        if (!edges.containsKey(edge.element)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }


}
