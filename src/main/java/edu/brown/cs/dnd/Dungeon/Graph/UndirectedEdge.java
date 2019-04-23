package edu.brown.cs.dnd.Dungeon.Graph;

public class UndirectedEdge<T> implements Comparable<UndirectedEdge<T>> {
  private T v1;
  private T v2;
  private Double weight;

  public UndirectedEdge(T t1, T t2, double weight) {
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
    return "UndirectedEdge{" +
            "v1=" + v1 +
            ", v2=" + v2 +
            ", weight=" + weight +
            '}';
  }

  public T getV1() {
    return v1;
  }

  public T getV2() {
    return v2;
  }
}
