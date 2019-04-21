package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.List;
import java.util.Map;

public class DisjointSets<T> {

  private Map<T,T> parents;

  public DisjointSets(List<T> elements) {
    for (T e : elements) {
      parents.put(e,e);
    }
  }

  public void add(T element) {
    parents.put(element, element);
  }

  public T find(T element) {
    if (parents.containsKey(element)) {
      T parent = parents.get(element);
      if (parents.get(element).equals(element)) {
        return element;
      } else {
        return find(parent);
      }
    } else {
      return null;
    }
  }

  public boolean union(T a, T b) {
    if (parents.containsKey(b) && parents.containsKey(b)) {
      parents.put(b, a);
      return true;
    } else {
      return false;
    }
  }

}
