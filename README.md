# Open Addressing

A hash table that resolves collisions by open addressing: `HashMap` keeps every entry in one array of slots, probes forward one slot at a time from a key's home slot, leaves a tombstone when it removes a key, and rehashes when more than half the slots are in use, growing to the next prime or cleaning at the same size. A JUnit suite checks the `Map` contract, then reads the slots through reflection to check the slots the notes draw.

## Prerequisites

- JDK 17+
- The JUnit jar is already vendored in `lib/`; there is nothing to download.

## Repository layout

```plaintext
code/
  README.md
  .gitignore
  lib/
    junit-platform-console-standalone-6.1.0.jar
  src/
    main/
      map/
        Map.java                        # the Map ADT
        HashMap.java                    # open addressing with linear probing and tombstones
      demo/                             # one demo per class you implement
        HashMapDemo.java
    test/
      map/
        MapTest.java                    # the Map contract suite
        HashMapTest.java                # the contract, plus the slots by reflection
  scripts/
    run.sh                              # compile and run one demo
    test.sh                             # compile and run the JUnit tests
```

## How to compile and run

- `scripts/test.sh` — compiles everything and runs the full JUnit suite.
- `scripts/test.sh map.HashMapTest` — compiles everything and runs only that test class.
- `scripts/run.sh demo.HashMapDemo` — compiles everything and runs that one demo. Every class you implement has its own demo, `<Class>Demo`.

## What's here

- `map.Map<K, V>` — the Map ADT, copied unchanged from the earlier chapters.
- `map.HashMap<K, V>` — the open-addressed hash table: `put`, `get`, `containsKey`, `remove`, `size`, and iteration over the keys and the entries.
- `map.MapTest` — the Map contract suite from the map chapter.
- `map.HashMapTest` — runs the contract suite against `HashMap`, then reads the slots through reflection to check the probe, the tombstones, and the rehash.
- `demo.HashMapDemo` — puts, looks up, overwrites, and removes the keys from the chapter's trace, then iterates.
