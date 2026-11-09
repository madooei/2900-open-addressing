package map;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A hash table implementation of the Map ADT, with open addressing and linear
 * probing.
 *
 * @param <K> the type of keys in this map.
 * @param <V> the type of values in this map.
 */
public class HashMap<K, V> implements Map<K, V> {

  private Entry<K, V>[] slots;  // one entry per slot, or null when the slot is empty
  private int size;             // how many live pairs the map holds
  private int used;             // how many slots are non-empty: live entries and tombstones
  private int primeIndex;       // the current capacity is PRIMES[primeIndex]

  private static final int[] PRIMES = {
      7, 17, 37, 79, 163, 331, 673, 1361, 2729, 5471,
      10949, 21911, 43853, 87719, 175447, 350899, 701819, 1403641
  };

  private static final double MAX_LOAD_FACTOR = 0.5;

  private static class Entry<K, V> implements Map.Entry<K, V> {
    K key;
    V value;
    boolean deleted;  // false for a live entry, true for a tombstone

    Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public K key() {
      return key;
    }

    @Override
    public V value() {
      return value;
    }
  }

  // slots only ever holds Entry<K, V>, so the cast is safe.
  @SuppressWarnings("unchecked")
  public HashMap() {
    slots = (Entry<K, V>[]) new Entry[PRIMES[0]];  // start with 7 slots
    size = 0;                                      // the map starts empty
    used = 0;                                      // no slots are taken yet
    primeIndex = 0;                                // capacity is PRIMES[0]
  }

  @Override
  public int size() {
    return size;
  }

  // Turns a key into the index of its home slot: hash, then compress to the array's range.
  private int indexFor(K key) {
    return Math.floorMod(key.hashCode(), slots.length);
  }

  // Returns the slot of the key's live entry if the key is present. Otherwise
  // returns the slot where a new key goes: the first tombstone on the probe path,
  // or the empty slot that ends it.
  private int probe(K key) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Returns true if slot i holds a live entry with the given key.
  private boolean holdsKey(int i, K key) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean put(K key, V value) {
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
  public V get(K key) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean containsKey(K key) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean remove(K key) {
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
  public Iterator<K> iterator() {
    return new KeyIterator();
  }

  @Override
  public Iterable<Map.Entry<K, V>> entries() {
    return new Entries();
  }

  // Returns the key of each entry, using an EntryIterator for the traversal.
  private class KeyIterator implements Iterator<K> {
    private Iterator<Map.Entry<K, V>> entries;  // the traversal this iterator takes its keys from

    KeyIterator() {
      entries = new EntryIterator();
    }

    @Override
    public boolean hasNext() {
      return entries.hasNext();
    }

    @Override
    public K next() {
      Map.Entry<K, V> entry = entries.next();
      return entry.key();
    }
  }

  // The Iterable that entries() returns. Each call to iterator() starts a new traversal.
  private class Entries implements Iterable<Map.Entry<K, V>> {
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
      return new EntryIterator();
    }
  }

  // Traverses the slots array, returning each live entry and skipping empty slots
  // and tombstones.
  private class EntryIterator implements Iterator<Map.Entry<K, V>> {

    EntryIterator() {
      // TODO: Implement me
      throw new UnsupportedOperationException("TODO: Implement me");
    }


    @Override
    public boolean hasNext() {
      // TODO: Implement me
      throw new UnsupportedOperationException("TODO: Implement me");
    }

    @Override
    public Map.Entry<K, V> next() {
      // TODO: Implement me
      throw new UnsupportedOperationException("TODO: Implement me");
    }
  }
}
