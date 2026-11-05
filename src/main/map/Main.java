package map;

/**
 * A small demo of the HashMap, using the keys from the chapter's trace: cat,
 * bee, pig, and hen. bee, pig, and hen share a home slot, so pig and hen probe
 * forward to open slots. The client works only through the Map interface and
 * never sees the slots.
 */
public final class Main {

  private Main() {
    // This class should not be instantiated!
  }

  public static void main(String[] args) {
    Map<String, Integer> counts = new HashMap<>();
    System.out.println("put cat: " + counts.put("cat", 10)); // true: the key was new
    System.out.println("put bee: " + counts.put("bee", 40)); // true: lands in its home slot
    System.out.println("put pig: " + counts.put("pig", 50)); // true: probes past bee
    System.out.println("put hen: " + counts.put("hen", 60)); // true: probes past bee and pig
    System.out.println("pairs stored: " + counts.size());    // 4

    // A key that had to probe past taken slots is still found.
    System.out.println("count for hen: " + counts.get("hen")); // 60

    // put on an existing key overwrites its value and returns false.
    System.out.println("put bee again: " + counts.put("bee", 99)); // false: the key existed
    System.out.println("count for bee now: " + counts.get("bee")); // 99
    System.out.println("pairs stored: " + counts.size());          // 4

    // Removing pig leaves a tombstone, so the keys around it stay reachable.
    System.out.println("remove pig: " + counts.remove("pig")); // true
    System.out.println("pairs stored: " + counts.size());      // 3
    System.out.println("count for hen: " + counts.get("hen")); // 60

    // A key that was never put is not in the map.
    System.out.println("has fox? " + counts.containsKey("fox")); // false

    // The map is Iterable<K>, so the enhanced for loop traverses the keys.
    System.out.println("keys:");
    for (String key : counts) {
      System.out.println("  " + key);
    }

    // entries() traverses the pairs.
    System.out.println("key -> count:");
    for (Map.Entry<String, Integer> entry : counts.entries()) {
      System.out.println("  " + entry.key() + " -> " + entry.value());
    }
  }
}
