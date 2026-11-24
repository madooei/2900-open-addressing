package demo;

import map.HashMap;
import map.Map;

/** A demo of HashMap on the chapter's trace, where pig and hen probe past bee. */
public final class HashMapDemo {

  private HashMapDemo() {
    // This class should not be instantiated!
  }

  public static void main(String[] args) {
    Map<String, Integer> counts = new HashMap<>();

    // Each key is new, so expect true four times and 4 pairs stored.
    // bee, pig, and hen share a home slot, so pig and hen probe forward.
    System.out.println("put cat: " + counts.put("cat", 10));
    System.out.println("put bee: " + counts.put("bee", 40));
    System.out.println("put pig: " + counts.put("pig", 50));
    System.out.println("put hen: " + counts.put("hen", 60));
    System.out.println("pairs stored: " + counts.size());

    // hen had to probe past taken slots, and it is still found. Expect 60.
    System.out.println("count for hen: " + counts.get("hen"));

    // bee is already there, so expect false, the new value 99, and still 4 pairs.
    System.out.println("put bee again: " + counts.put("bee", 99));
    System.out.println("count for bee now: " + counts.get("bee"));
    System.out.println("pairs stored: " + counts.size());

    // Removing pig leaves a tombstone, so hen stays reachable.
    // Expect true, 3 pairs, and 60 for hen.
    System.out.println("remove pig: " + counts.remove("pig"));
    System.out.println("pairs stored: " + counts.size());
    System.out.println("count for hen: " + counts.get("hen"));

    // fox was never put. Expect false.
    System.out.println("has fox? " + counts.containsKey("fox"));

    // Expect the three keys left in slot order: bee, cat, hen.
    System.out.println("keys:");
    for (String key : counts) {
      System.out.println("  " + key);
    }

    // Expect the same keys with their counts.
    System.out.println("key -> count:");
    for (Map.Entry<String, Integer> entry : counts.entries()) {
      System.out.println("  " + entry.key() + " -> " + entry.value());
    }
  }
}
