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
    slots = (Entry<T>[]) new Entry[PRIMES[0]];  // start with 7 slots
    size = 0;                                   // the set starts empty
    used = 0;                                   // no slots are taken yet
    primeIndex = 0;                             // capacity is PRIMES[0]
  }

  @Override
  public int size() {
    return size;
  }

  // Turns an element into the index of its home slot: hash, then compress to the array's range.
  private int indexFor(T item) {
    return Math.floorMod(item.hashCode(), slots.length);
  }

  // Returns the slot of the element's live entry if the element is present.
  // Otherwise returns the slot where a new element goes: the first tombstone on
  // the probe path, or the empty slot that ends it.
  private int probe(T item) {
    int i = indexFor(item);
    int firstTombstone = -1;
    while (slots[i] != null) {
      if (slots[i].deleted) {
        if (firstTombstone == -1) {
          firstTombstone = i;  // remember the first tombstone on the path
        }
      } else if (slots[i].item.equals(item)) {
        return i;  // found the element
      }
      i = (i + 1) % slots.length;  // another element or a tombstone; keep probing
    }
    if (firstTombstone != -1) {
      return firstTombstone;  // the element is new; reuse the tombstone, not the empty slot
    }
    return i;
  }

  // Returns true if slot i holds a live entry with the given element.
  private boolean holdsItem(int i, T item) {
    return slots[i] != null && !slots[i].deleted && slots[i].item.equals(item);
  }

  @Override
  public boolean add(T item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    int i = probe(item);
    if (holdsItem(i, item)) {
      return false;  // already in the set; nothing changes
    }
    if (slots[i] == null) {
      used++;  // filling an empty slot; one more slot is now in use
    }
    slots[i] = new Entry<>(item);
    size++;
    rehashIfNeeded();
    return true;
  }

  // Rehashes once more than half the slots are in use: grows when the live
  // entries passed half, and cleans at the same size when the tombstones did.
  private void rehashIfNeeded() {
    if (used > MAX_LOAD_FACTOR * slots.length) {
      if (size > MAX_LOAD_FACTOR * slots.length) {
        int capacity;
        if (primeIndex + 1 < PRIMES.length) {
          capacity = PRIMES[++primeIndex];  // the next prime
        } else {
          capacity = 2 * slots.length + 1;  // past the array: double and add one
        }
        rehash(capacity);
      } else {
        rehash(slots.length);
      }
    }
  }

  @Override
  public boolean contains(T item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    return holdsItem(probe(item), item);
  }

  @Override
  public boolean remove(T item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    int i = probe(item);
    if (holdsItem(i, item)) {
      slots[i].deleted = true;  // leave a tombstone instead of emptying the slot
      size--;
      return true;
    }
    return false;
  }

  // Rebuilds the table into a fresh array of the given capacity, moving every
  // live entry and leaving the tombstones behind. primeIndex is not changed
  // here: rehashIfNeeded sets it before a growing rehash, and a same-size clean keeps it.
  @SuppressWarnings("unchecked")
  private void rehash(int capacity) {
    Entry<T>[] old = slots;
    slots = (Entry<T>[]) new Entry[capacity];
    used = 0;
    for (Entry<T> entry : old) {
      if (entry != null && !entry.deleted) {  // skip empty slots and tombstones
        slots[probe(entry.item)] = entry;     // the first empty slot on entry's path
        used++;
      }
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new SetIterator();
  }

  // Traverses the slots array, returning each live element and skipping empty
  // slots and tombstones.
  private class SetIterator implements Iterator<T> {
    private int slot = 0;  // the next slot to examine

    SetIterator() {
      advance();  // position slot on the first live entry, if there is one
    }

    private void advance() {
      while (slot < slots.length && (slots[slot] == null || slots[slot].deleted)) {
        slot++;  // skip empty slots and tombstones
      }
    }

    @Override
    public boolean hasNext() {
      return slot < slots.length;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      T item = slots[slot].item;
      slot++;
      advance();
      return item;
    }
  }
}
