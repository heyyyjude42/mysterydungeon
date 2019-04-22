package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.*;

public class UndirectedGraph<T> {

  private Set<UndirectedEdge<T>> edges;

  public UndirectedGraph() {
    this.edges = new HashSet<>();
  }


  public void addEdge(T src, T dest, double weight) {
    UndirectedEdge<T> toAdd = new UndirectedEdge<>(src, dest, weight);
    edges.add(toAdd);
  }

  public void addEdge(UndirectedEdge<T> e) {
    edges.add(e);
  }

  /**
   * Method gets all the vertices of the graph.
   * @return    A Collection of the vertices in the graph
   */
  public Collection<T> getVertices() {
    Set<T> vertices = new HashSet<>();

    for (UndirectedEdge<T> e : this.edges) {
      vertices.add(e.getV1());
      vertices.add(e.getV2());
    }

    return vertices;
  }

  public Set<UndirectedEdge<T>> getEdges() {
    return edges;
  }

  //  /**
//   * Method counts how many vertices there are in the graph.
//   */
//  public int getNumVertices() {
//    Set<T> vertices = new HashSet<>();
//
//    for (Edge<T> e : this.edges) {
//      vertices.add(e.v1);
//      vertices.add(e.v2);
//    }
//
//    return vertices.size();
//  }
//
//  private class VertexComparator implements Comparator<T> {
//
//    private Map<T, Double> priorities;
//
//    public VertexComparator(Map<T, Double> priorities) {
//      this.priorities = priorities;
//    }
//
//    public int compare(T t1, T t2) {
//      return Double.compare(priorities.get(t1), priorities.get(t2));
//    }
//  }
//
//  /**
//   * Method creates an minimum-spanning tree from the graph.
//   * @return    An UndirectedGraph that is the minimum-spanning tree
//   */
//  public UndirectedGraph mst() {
//    Set<T> visited = new HashSet<>();
//    Map<T, Double> priorities = new HashMap<>();
//    VertexComparator vc = new VertexComparator(priorities);
//    PriorityQueue<T> pq = new PriorityQueue<>(vc);
//
//    int numVertices = this.getNumVertices();
//
//    for (T t : priorities.keySet()) {
//      priorities.put(t, Double.POSITIVE_INFINITY);
//    }
//
//    while (visited.size() < numVertices) {
//      T vertex = pq.poll();
//      visited.add(vertex);
//    }
//  }


  /**
   * Method creates a minimum-spanning tree from the graph.
   * @return    An UndirectedGraph that is the minimum-spanning tree
   */
  public UndirectedGraph mst() {
    Collection<T> vertices = this.getVertices();
    int verticesSize = vertices.size();
    DisjointSet<T> components = new DisjointSet<>(vertices);
    PriorityQueue<UndirectedEdge<T>> pq = new PriorityQueue<UndirectedEdge<T>>();

    for (UndirectedEdge<T> e : this.edges) {
      pq.add(e);
    }

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
}
