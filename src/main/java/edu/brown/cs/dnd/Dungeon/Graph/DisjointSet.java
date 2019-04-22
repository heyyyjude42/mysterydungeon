package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

public class DisjointSet<T> {

  private Map<T,T> parents;

  public DisjointSet(Collection<T> elements) {
    this.parents = new HashMap<>();

    for (T e : elements) {
      parents.put(e, e);
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

  public boolean inSameSet(T a, T b) {
    if (parents.containsKey(b) && parents.containsKey(b)) {
      return find(a).equals(find(b));
    } else {
      return false;
    }
  }

}
