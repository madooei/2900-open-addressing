package map;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Runs the Map contract suite against HashMap. */
public class HashMapTest extends MapTest {

  @Override
  protected Map<String, Integer> createMap() {
    return new HashMap<>();
  }

  @Test
  public void newMapHasSevenSlots() {
    HashMap<String, Integer> table = new HashMap<>();
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void keyLandsInItsHomeSlot() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("cat", 10);
    assertEquals("cat", keyAt(table, 3));
  }

  @Test
  public void collidingKeyProbesToTheNextSlot() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    assertEquals("pig", keyAt(table, 6));
  }

  @Test
  public void probeWrapsPastTheLastSlot() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    assertEquals("hen", keyAt(table, 0));
  }

  @Test
  public void probeWrapsForIntegerKeys() {
    HashMap<Integer, String> table = new HashMap<>();
    table.put(6, "a");
    table.put(13, "b");
    table.put(20, "c");
    assertEquals(Integer.valueOf(20), keyAt(table, 1));
  }

  @Test
  public void putExistingKeyOverwritesInItsSlot() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("pig", 55);
    assertEquals(55, valueAt(table, 6));
  }

  @Test
  public void containsKeyFindsAKeyThatProbed() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    assertTrue(table.containsKey("pig"));
  }

  @Test
  public void removeLeavesATombstone() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    assertTrue(isTombstone(table, 5));
  }

  @Test
  public void removeKeepsTheSlotInUse() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    assertEquals(2, usedOf(table));
  }

  @Test
  public void getProbesPastATombstone() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    table.remove("bee");
    assertEquals(60, table.get("hen"));
  }

  @Test
  public void getDoesNotMatchATombstone() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    assertNull(table.get("bee"));
  }

  @Test
  public void removeDoesNotMatchATombstone() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    assertFalse(table.remove("bee"));
  }

  @Test
  public void putReusesTheFirstTombstone() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    table.remove("bee");
    table.put("rat", 70);
    assertEquals("rat", keyAt(table, 5));
  }

  @Test
  public void putReusesTheFirstOfTwoTombstones() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    table.remove("bee");
    table.remove("pig");
    table.put("rat", 70);
    assertEquals("rat", keyAt(table, 5));
  }

  @Test
  public void putIntoATombstoneLeavesTheEmptySlotEmpty() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    table.remove("bee");
    table.put("rat", 70);
    assertNull(slotsOf(table)[1]);
  }

  @Test
  public void putIntoATombstoneDoesNotGrowUsed() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    table.remove("bee");
    table.put("rat", 70);
    assertEquals(3, usedOf(table));
  }

  @Test
  public void putPastATombstoneUpdatesTheLaterKey() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    table.put("pig", 55);
    assertEquals(55, valueAt(table, 6));
  }

  @Test
  public void putPastATombstoneDoesNotStoreTheKeyTwice() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    table.put("pig", 55);
    assertTrue(isTombstone(table, 5));
  }

  @Test
  public void threePutsDoNotGrowTheTable() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void fourthPutGrowsToTheNextPrime() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("cat", 10);
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    assertEquals(17, slotsOf(table).length);
  }

  @Test
  public void growingMovesEveryKeyToItsNewHomeSlot() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("cat", 10);
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    assertEquals("bee", keyAt(table, 0));
    assertEquals("cat", keyAt(table, 2));
    assertEquals("hen", keyAt(table, 12));
    assertEquals("pig", keyAt(table, 14));
  }

  @Test
  public void growingSteps7To17To37() {
    HashMap<String, Integer> table = new HashMap<>();
    for (int i = 0; i < 9; i++) {
      table.put("k" + i, i);
    }
    assertEquals(37, slotsOf(table).length);
  }

  @Test
  public void tombstonesTriggerACleanAtTheSameSize() {
    HashMap<String, Integer> table = tableAfterTombstoneClean();
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void cleanDropsEveryTombstone() {
    HashMap<String, Integer> table = tableAfterTombstoneClean();
    assertEquals(1, usedOf(table));
  }

  @Test
  public void cleanPutsTheLiveKeyInItsHomeSlot() {
    HashMap<String, Integer> table = tableAfterTombstoneClean();
    assertEquals("cat", keyAt(table, 3));
  }

  @Test
  public void halfTheSlotsLiveCleansAtTheSameSize() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("cat", 10);
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("cat");
    table.put("hen", 60);
    assertEquals(7, slotsOf(table).length);
  }

  @Test
  public void manyPutsAndRemovesKeepEveryLiveKey() {
    HashMap<String, Integer> table = new HashMap<>();
    for (int i = 0; i < 60; i++) {
      table.put("k" + i, i);
    }
    for (int i = 0; i < 60; i += 2) {
      table.remove("k" + i);
    }
    for (int i = 1; i < 60; i += 2) {
      assertEquals(i, table.get("k" + i));
    }
  }

  @Test
  public void manyPutsAndRemovesKeepUsedAtMostHalfTheSlots() {
    HashMap<String, Integer> table = new HashMap<>();
    for (int round = 0; round < 30; round++) {
      for (int i = 0; i < 20; i++) {
        table.put("c" + i, i);
        table.remove("c" + i);
      }
    }
    assertTrue(usedOf(table) <= slotsOf(table).length / 2);
  }

  @Test
  public void iteratorSkipsTombstones() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.remove("bee");
    List<String> seen = new ArrayList<>();
    for (String key : table) {
      seen.add(key);
    }
    List<String> expected = new ArrayList<>();
    expected.add("pig");
    assertEquals(expected, seen);
  }

  // bee, pig, and hen are put and removed, leaving three tombstones in seven
  // slots. Putting cat fills a fourth slot, so the table cleans at the same size.
  private static HashMap<String, Integer> tableAfterTombstoneClean() {
    HashMap<String, Integer> table = new HashMap<>();
    table.put("bee", 40);
    table.put("pig", 50);
    table.put("hen", 60);
    table.remove("bee");
    table.remove("pig");
    table.remove("hen");
    table.put("cat", 10);
    return table;
  }

  private static Object[] slotsOf(HashMap<?, ?> table) {
    return (Object[]) readField(table, "slots");
  }

  private static int usedOf(HashMap<?, ?> table) {
    return (Integer) readField(table, "used");
  }

  private static Object keyAt(HashMap<?, ?> table, int slot) {
    return readField(slotsOf(table)[slot], "key");
  }

  private static Object valueAt(HashMap<?, ?> table, int slot) {
    return readField(slotsOf(table)[slot], "value");
  }

  private static boolean isTombstone(HashMap<?, ?> table, int slot) {
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
