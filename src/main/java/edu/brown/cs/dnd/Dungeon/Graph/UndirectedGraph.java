package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.*;

public class UndirectedGraph<T> {

  private Set<Edge> edges;

  class Edge<T> implements Comparable<Edge<T>> {
    T v1;
    T v2;
    Double weight;

    Edge(T t1, T t2, double weight) {
      this.v1 = t1;
      this.v2 = t2;
      this.weight = weight;
    }

    @Override
    public int hashCode() {
      return 7 * v1.hashCode() + 7 * v2.hashCode() + 17 * weight.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof Edge)) {
        return false;
      }

      Edge e = (Edge) o;

      return ((this.v1.equals(e.v1) && this.v2.equals(e.v2))
              || (this.v1.equals(e.v2) && this.v2.equals(e.v1)))
              && this.weight.equals(e.weight);
    }

    @Override
    public int compareTo(Edge<T> e) {
      if (this.weight > e.weight) {
        return 1;
      } else if (e.weight > this.weight) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  public void addEdge(T src, T dest, double weight) {
    Edge<T> toAdd = new Edge(src, dest, weight);
    edges.add(toAdd);
  }

  public void addEdge(Edge<T> e) {
    edges.add(e);
  }

  /**
   * Method gets all the vertices of the graph.
   * @return    A Collection of the vertices in the graph
   */
  public Collection<T> getVertices() {
    Set<T> vertices = new HashSet<>();

    for (Edge<T> e : this.edges) {
      vertices.add(e.v1);
      vertices.add(e.v2);
    }

    return vertices;
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
    PriorityQueue<Edge<T>> pq = new PriorityQueue<Edge<T>>();

    for (Edge<T> e : this.edges) {
      pq.add(e);
    }

    UndirectedGraph ug = new UndirectedGraph();
    while (ug.edges.size() < verticesSize - 1) {
      Edge<T> e = pq.poll();
      if (!components.inSameSet(e.v1, e.v2)) {
        components.union(e.v1, e.v2);
        ug.addEdge(e);
      }
    }
    return ug;
  }
}
