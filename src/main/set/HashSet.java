package set;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A hash table implementation of the Set ADT, with open addressing and linear
 * probing.
 *
 * @param <T> the type of elements in this set.
 */
public class HashSet<T> implements Set<T> {

  private Entry<T>[] slots;  // one entry per slot, or null when the slot is empty
  private int size;          // how many live elements the set holds
  private int used;          // how many slots are non-empty: live entries and tombstones
  private int primeIndex;    // the current capacity is PRIMES[primeIndex]

  private static final int[] PRIMES = {
      7, 17, 37, 79, 163, 331, 673, 1361, 2729, 5471,
      10949, 21911, 43853, 87719, 175447, 350899, 701819, 1403641
  };

  private static final double MAX_LOAD_FACTOR = 0.5;

  private static class Entry<T> {
    T item;
    boolean deleted;  // false for a live entry, true for a tombstone

    Entry(T item) {
      this.item = item;
    }
  }

  // slots only ever holds Entry<T>, so the cast is safe.
  @SuppressWarnings("unchecked")
  public HashSet() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public int size() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Turns an element into the index of its home slot: hash, then compress to the array's range.
  private int indexFor(T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Returns the slot of the element's live entry if the element is present.
  // Otherwise returns the slot where a new element goes: the first tombstone on
  // the probe path, or the empty slot that ends it.
  private int probe(T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Returns true if slot i holds a live entry with the given element.
  private boolean holdsItem(int i, T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean add(T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Rehashes once more than half the slots are in use: grows when the live
  // entries passed half, and cleans at the same size when the tombstones did.
  private void rehashIfNeeded() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean contains(T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean remove(T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Rebuilds the table into a fresh array of the given capacity, moving every
  // live entry and leaving the tombstones behind. primeIndex is not changed
  // here: rehashIfNeeded sets it before a growing rehash, and a same-size clean keeps it.
  @SuppressWarnings("unchecked")
  private void rehash(int capacity) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public Iterator<T> iterator() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Traverses the slots array, returning each live element and skipping empty
  // slots and tombstones.
  private class SetIterator implements Iterator<T> {

    SetIterator() {
      // TODO: Implement me
      throw new UnsupportedOperationException("TODO: Implement me");
    }


    @Override
    public boolean hasNext() {
      // TODO: Implement me
      throw new UnsupportedOperationException("TODO: Implement me");
    }

    @Override
    public T next() {
      // TODO: Implement me
      throw new UnsupportedOperationException("TODO: Implement me");
    }
  }
}
