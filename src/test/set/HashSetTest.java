package set;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Runs the Set contract suite against HashSet, then checks how it uses its slots. */
public class HashSetTest extends SetTest {

  @Override
  protected Set<Integer> createSet() {
    return new HashSet<>();
  }

  @Test
  public void newSetHasSevenSlots() {
    HashSet<String> table = new HashSet<>();
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void elementLandsInItsHomeSlot() {
    HashSet<String> table = new HashSet<>();
    table.add("cat");
    assertEquals("cat", itemAt(table, 3));
  }

  @Test
  public void negativeHashCodeLandsInSlotRange() {
    HashSet<String> table = new HashSet<>();
    table.add("elephant");  // hashCode -5491951, and floorMod(-5491951, 7) is 4
    assertEquals("elephant", itemAt(table, 4));
  }

  @Test
  public void collidingElementProbesToTheNextSlot() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    assertEquals("pig", itemAt(table, 6));
  }

  @Test
  public void probeWrapsPastTheLastSlot() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    assertEquals("hen", itemAt(table, 0));
  }

  @Test
  public void probeWrapsForIntegerElements() {
    HashSet<Integer> table = new HashSet<>();
    table.add(6);
    table.add(13);
    table.add(20);
    assertEquals(Integer.valueOf(20), itemAt(table, 1));
  }

  @Test
  public void addExistingElementTakesNoNewSlot() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("pig");
    assertEquals(2, usedOf(table));
  }

  @Test
  public void containsFindsAnElementThatProbed() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    assertTrue(table.contains("pig"));
  }

  @Test
  public void removeLeavesATombstone() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    assertTrue(isTombstone(table, 5));
  }

  @Test
  public void removeKeepsTheSlotInUse() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    assertEquals(2, usedOf(table));
  }

  @Test
  public void containsProbesPastATombstone() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    table.remove("bee");
    assertTrue(table.contains("hen"));
  }

  @Test
  public void containsDoesNotMatchATombstone() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    assertFalse(table.contains("bee"));
  }

  @Test
  public void removeDoesNotMatchATombstone() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    assertFalse(table.remove("bee"));
  }

  @Test
  public void addReusesTheFirstTombstone() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    table.remove("bee");
    table.add("rat");
    assertEquals("rat", itemAt(table, 5));
  }

  @Test
  public void addReusesTheFirstOfTwoTombstones() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    table.remove("bee");
    table.remove("pig");
    table.add("rat");
    assertEquals("rat", itemAt(table, 5));
  }

  @Test
  public void addIntoATombstoneLeavesTheEmptySlotEmpty() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    table.remove("bee");
    table.add("rat");
    assertNull(slotsOf(table)[1]);
  }

  @Test
  public void addIntoATombstoneDoesNotGrowUsed() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    table.remove("bee");
    table.add("rat");
    assertEquals(3, usedOf(table));
  }

  @Test
  public void addPastATombstoneFindsTheLaterElement() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    assertFalse(table.add("pig"));
  }

  @Test
  public void addPastATombstoneDoesNotStoreTheElementTwice() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    table.add("pig");
    assertTrue(isTombstone(table, 5));
  }

  @Test
  public void threeAddsDoNotGrowTheTable() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void fourthAddGrowsToTheNextPrime() {
    HashSet<String> table = new HashSet<>();
    table.add("cat");
    table.add("bee");
    table.add("pig");
    table.add("hen");
    assertEquals(17, slotsOf(table).length);
  }

  @Test
  public void growingMovesEveryElementToItsNewHomeSlot() {
    HashSet<String> table = new HashSet<>();
    table.add("cat");
    table.add("bee");
    table.add("pig");
    table.add("hen");
    assertEquals("bee", itemAt(table, 0));
    assertEquals("cat", itemAt(table, 2));
    assertEquals("hen", itemAt(table, 12));
    assertEquals("pig", itemAt(table, 14));
  }

  @Test
  public void growingSteps7To17To37() {
    HashSet<String> table = new HashSet<>();
    for (int i = 0; i < 9; i++) {
      table.add("k" + i);
    }
    assertEquals(37, slotsOf(table).length);
  }

  @Test
  public void tombstonesTriggerACleanAtTheSameSize() {
    HashSet<String> table = tableAfterTombstoneClean();
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void cleanDropsEveryTombstone() {
    HashSet<String> table = tableAfterTombstoneClean();
    assertEquals(1, usedOf(table));
  }

  @Test
  public void cleanPutsTheLiveElementInItsHomeSlot() {
    HashSet<String> table = tableAfterTombstoneClean();
    assertEquals("cat", itemAt(table, 3));
  }

  @Test
  public void growingAfterACleanGoesToTheNextPrime() {
    HashSet<String> table = tableAfterTombstoneClean();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    assertEquals(17, slotsOf(table).length);
  }

  @Test
  public void growingPastTheLastPrimeDoublesAndAddsOne() {
    HashSet<String> table = new HashSet<>();
    for (int i = 1; i <= 701821; i++) {  // the first count above 0.5 * 1403641
      table.add("k" + i);
    }
    assertEquals(2 * 1403641 + 1, slotsOf(table).length);
  }

  @Test
  public void halfTheSlotsLiveCleansAtTheSameSize() {
    HashSet<String> table = new HashSet<>();
    table.add("cat");
    table.add("bee");
    table.add("pig");
    table.remove("cat");
    table.add("hen");
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void manyAddsAndRemovesKeepEveryLiveElement() {
    HashSet<String> table = new HashSet<>();
    for (int i = 0; i < 60; i++) {
      table.add("k" + i);
    }
    for (int i = 0; i < 60; i += 2) {
      table.remove("k" + i);
    }
    for (int i = 1; i < 60; i += 2) {
      assertTrue(table.contains("k" + i));
    }
  }

  @Test
  public void manyAddsAndRemovesKeepUsedAtMostHalfTheSlots() {
    HashSet<String> table = new HashSet<>();
    for (int round = 0; round < 30; round++) {
      for (int i = 0; i < 20; i++) {
        table.add("c" + i);
        table.remove("c" + i);
      }
    }
    assertTrue(usedOf(table) <= slotsOf(table).length / 2);
  }

  @Test
  public void iteratorSkipsTombstones() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.remove("bee");
    List<String> seen = new ArrayList<>();
    for (String item : table) {
      seen.add(item);
    }
    List<String> expected = new ArrayList<>();
    expected.add("pig");
    assertEquals(expected, seen);
  }

  // bee, pig, and hen are added and removed, leaving three tombstones in seven
  // slots. Adding cat fills a fourth slot, so the table cleans at the same size.
  private static HashSet<String> tableAfterTombstoneClean() {
    HashSet<String> table = new HashSet<>();
    table.add("bee");
    table.add("pig");
    table.add("hen");
    table.remove("bee");
    table.remove("pig");
    table.remove("hen");
    table.add("cat");
    return table;
  }

  private static Object[] slotsOf(HashSet<?> table) {
    return (Object[]) readField(table, "slots");
  }

  private static int usedOf(HashSet<?> table) {
    return (Integer) readField(table, "used");
  }

  private static Object itemAt(HashSet<?> table, int slot) {
    return readField(slotsOf(table)[slot], "item");
  }

  private static boolean isTombstone(HashSet<?> table, int slot) {
    return (Boolean) readField(slotsOf(table)[slot], "deleted");
  }

  // Reads a private field of an object by reflection.
  private static Object readField(Object object, String name) {
    try {
      Field field = object.getClass().getDeclaredField(name);
      field.setAccessible(true);
      return field.get(object);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}
