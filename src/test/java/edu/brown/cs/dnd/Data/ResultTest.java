package edu.brown.cs.dnd.Data;

import edu.brown.cs.dnd.Data.Monster;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ResultTest {

  @Test
  public void testConstruct() {
    Monster m = new Monster("a", "b", "c", "d",
            5, 5, "e", "f", 5,
            5, 5, 5, 5, 5, 5.0,
            new HashMap<>(), new HashMap<>(), new HashMap<>());
    List<Monster> result = new ArrayList<>();
    result.add(m);

    Result r = new Result(ReturnType.MONSTER, result);
    assertNotNull(r);
  }

  @Test
  public void testGetters() {
    Monster m = new Monster("a", "b", "c", "d",
            5, 5, "e", "f", 5,
            5, 5, 5, 5, 5, 5.0,
            new HashMap<>(), new HashMap<>(), new HashMap<>());
    List<Monster> result = new ArrayList<>();
    result.add(m);

    Result r = new Result(ReturnType.MONSTER, result);

    assertEquals(r.getType(), ReturnType.MONSTER);
    assertEquals(r.getResults(), result);
  }
}
