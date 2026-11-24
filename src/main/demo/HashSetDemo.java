package demo;

import set.HashSet;
import set.Set;

/** A demo of HashSet on the chapter's keys, where bee, pig, and hen share a home slot. */
public final class HashSetDemo {

  private HashSetDemo() {
    // This class should not be instantiated!
  }

  public static void main(String[] args) {
    Set<String> animals = new HashSet<>();

    // Each element is new, so expect true four times and a size of 4.
    // bee, pig, and hen share a home slot, so pig and hen probe forward.
    System.out.println("add cat: " + animals.add("cat"));
    System.out.println("add bee: " + animals.add("bee"));
    System.out.println("add pig: " + animals.add("pig"));
    System.out.println("add hen: " + animals.add("hen"));
    System.out.println("size: " + animals.size());

    // bee is already there, so expect false and still a size of 4.
    System.out.println("add bee again: " + animals.add("bee"));
    System.out.println("size: " + animals.size());

    // Removing pig leaves a tombstone, so hen stays reachable.
    // Expect true, a size of 3, and true for hen.
    System.out.println("remove pig: " + animals.remove("pig"));
    System.out.println("size: " + animals.size());
    System.out.println("contains hen? " + animals.contains("hen"));

    // Slot order, skipping empty slots and the tombstone.
    String text = "";
    for (String animal : animals) {
      text += animal + " ";
    }
    System.out.println("elements: " + text.trim());
  }
}
