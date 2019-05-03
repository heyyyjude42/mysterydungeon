package edu.brown.cs.dnd.Dungeon.Graph;

/**
 * Class representing an endirected edge.
 * @param <T>   The parameterized type of the undirected edge
 */
public class UndirectedEdge<T> implements Comparable<UndirectedEdge<T>> {
  private T v1;
  private T v2;
  private Double weight;

  /**
   * A Constructor for an UndirectedEdge.
   * @param t1    An element that is the first vertex of the edge
   * @param t2    An element that is the second vertex of the edge
   * @param weight    A double that is the weight of the edge
   */
  public UndirectedEdge(T t1, T t2, double weight) {
    this.v1 = t1;
    this.v2 = t2;
    this.weight = weight;
  }

  @Override
  public int hashCode() {
    final int p1 = 7;
    final int p2 = 17;
    return p1 * v1.hashCode() + p1 * v2.hashCode() + p2 * weight.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof UndirectedEdge)) {
      return false;
    }

    UndirectedEdge e = (UndirectedEdge) o;

    return ((this.v1.equals(e.v1) && this.v2.equals(e.v2))
            || (this.v1.equals(e.v2) && this.v2.equals(e.v1)))
            && this.weight.equals(e.weight);
  }

  @Override
  public int compareTo(UndirectedEdge<T> e) {
    if (this.weight > e.weight) {
      return 1;
    } else if (e.weight > this.weight) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "Undirected Edge {"
            + "v1 = "
            + v1
            + ", v2 = " + v2
            + ", weight = " + weight
            + "}";
  }

  /**
   * Method gets the first vertex of the edge.
   * @return    An element that is the first vertex of the edge
   */
  public T getV1() {
    return v1;
  }

  /**
   * Method gets the second vertex of the edge.
   * @return    An element that is the second vertex of the edge
   */
  public T getV2() {
    return v2;
  }
}
