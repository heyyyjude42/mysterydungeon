package edu.brown.cs.dnd.Data;

import org.junit.Test;
import static org.junit.Assert.*;

public class SearchOperatorTest {

  @Test
  public void testConstruct() {
    SearchOperator so = new SearchOperator(Comparator.GREATER_THAN,
            "a", "b");
    assertNotNull(so);
  }

  @Test
  public void testGetters() {
    SearchOperator so = new SearchOperator(Comparator.GREATER_THAN,
            "a", "b");
    assertEquals(so.getTerm(), "b");
  }
}
