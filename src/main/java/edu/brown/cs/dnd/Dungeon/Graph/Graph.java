package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.List;

public class Graph<T> {

  class Edge<T> {
    T source;
    T destination;
    Double weight;

    Edge(T src, T dest, double weight) {
      this.source = src;
      this.destination = dest;
      this.weight = weight;
    }

    @Override
    public int hashCode() {
      return 7 * source.hashCode() + 13 * destination.hashCode() + 17 * weight.hashCode();
    }
  }

  private List<Edge> edges;

  public void addEdge(T src, T dest, double weight) {
    Edge<T> toAdd = new Edge(src, dest, weight);
    if (!edges.contains(toAdd)) {
      edges.add(toAdd);
    }
  }

}
