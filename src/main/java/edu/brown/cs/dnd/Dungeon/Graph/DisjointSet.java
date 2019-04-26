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
      if (parent.equals(element)) {
        return element;
      } else {
        return find(parent);
      }
    } else {
      return element;
    }
  }

  public void union(T a, T b) {
//    if (parents.containsKey(b) && parents.containsKey(b)) {
//      parents.put(b, a);
//      return true;
//    } else {
//      return false;
//    }

    T rootA = find(a);
    T rootB = find(b);

    if (rootA.equals(rootB)) {return;}

    parents.put(rootA, rootB);
  }

  public boolean inSameSet(T a, T b) {
//    if (parents.containsKey(b) && parents.containsKey(b)) {
//      return find(a).equals(find(b));
//    } else {
//      return false;
//    }
    return find(a).equals(find(b));
  }

}
