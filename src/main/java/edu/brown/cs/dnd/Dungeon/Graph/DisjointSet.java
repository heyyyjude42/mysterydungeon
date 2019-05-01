package edu.brown.cs.dnd.Dungeon.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * Class representing a Disjoint Set.
 * @param <T>
 */
public class DisjointSet<T> {

  private Map<T,T> parents;

  /**
   * A Constructor for a Disjoint Set.
   * @param elements    A Collection that are initial elements inside the
   *                    disjoint set
   */
  public DisjointSet(Collection<T> elements) {
    this.parents = new HashMap<>();

    for (T e : elements) {
      parents.put(e, e);
    }
  }

  /**
   * Method adds an element to the parents map.
   * @param element   An element to add to the parents map
   */
  public void add(T element) {
    parents.put(element, element);
  }

  /**
   * Method finds an element in the disjoint set based on the components.
   * @param element   The element to find
   * @return    The found element, or the original element if it wasn't found
   */
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

  /**
   * Method unions two elements in the component.
   * @param a   The first element to union
   * @param b   The second element to union
   */
  public void union(T a, T b) {
    T rootA = find(a);
    T rootB = find(b);

    if (rootA.equals(rootB)) {return;}

    parents.put(rootA, rootB);
  }

  /**
   * Method finds if two elements are in the same set.
   * @param a   The first element to check with
   * @param b   The second element to check with
   * @return    A boolean that is true if the two elements are in the same set,
   * and false otherwise
   */
  public boolean inSameSet(T a, T b) {
    return find(a).equals(find(b));
  }

  /**
   * Method gets the parents map.
   * @return    A Map that is the parents map of the disjoint set
   */
  public Map<T, T> getParents() {
    return this.parents;
  }

}
