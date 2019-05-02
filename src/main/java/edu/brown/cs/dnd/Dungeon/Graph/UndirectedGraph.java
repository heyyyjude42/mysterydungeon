package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.*;

/**
 * Class representing an Undirected Graph.
 * @param <T>   The parameterized type of the graph
 */
public class UndirectedGraph<T> {

  private Set<UndirectedEdge<T>> edges;

  /**
   * A Constructor for an UndirectedGraph.
   */
  public UndirectedGraph() {this.edges = new HashSet<>();}

  /**
   * Method constructs and adds an edge to the graph.
   * @param src   An element that is one of the vertices of the edge
   * @param dest    An element that is the other vertex of the edge
   * @param weight    A double that is the weight of the edge
   */
  public void addEdge(T src, T dest, double weight) {
    UndirectedEdge<T> toAdd = new UndirectedEdge<>(src, dest, weight);
    edges.add(toAdd);
  }

  /**
   * Method adds an edge to the graph.
   * @param e   An UndirectedEdge that is the edge to add
   */
  void addEdge(UndirectedEdge<T> e) {edges.add(e);}

  /**
   * Method gets all the vertices of the graph.
   * @return    A Collection of the vertices in the graph
   */
  Collection<T> getVertices() {
    Set<T> vertices = new HashSet<>();

    for (UndirectedEdge<T> e : this.edges) {
      vertices.add(e.getV1());
      vertices.add(e.getV2());
    }

    return vertices;
  }

  /**
   * Method gets all the edges of the graph.
   * @return    A Set of UndirectedEdges that are the edges of the graph
   */
  public Set<UndirectedEdge<T>> getEdges() {return edges;}

  /**
   * Method creates a minimum-spanning tree from the graph.
   * @return    An UndirectedGraph that is the minimum-spanning tree
   */
  public UndirectedGraph mst() {
    Collection<T> vertices = this.getVertices();
    int verticesSize = vertices.size();
    DisjointSet<T> components = new DisjointSet<>(vertices);
    PriorityQueue<UndirectedEdge<T>> pq = new PriorityQueue<UndirectedEdge<T>>(this.edges);

    UndirectedGraph ug = new UndirectedGraph();

    while (ug.edges.size() < verticesSize - 1) {
      UndirectedEdge<T> e = pq.poll();
      if (!components.inSameSet(e.getV1(), e.getV2())) {
        components.union(e.getV1(), e.getV2());
        ug.addEdge(e);
      }
    }
    return ug;
  }

  @Override
  public String toString() {
    return "UndirectedGraph{" +
            "edges=" + edges +
            '}';
  }
}
