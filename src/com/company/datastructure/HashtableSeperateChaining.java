package com.company.datastructure;

import java.util.HashMap;
import java.util.LinkedList;

public class HashtableSeperateChaining<K, V> {

    private static int DEFAULT_CAPACITY = 10;
    private static double DEFAULT_LOAD_FACTOR = 0.75f;
    private LinkedList<Entry<K, V>>[] table;

    private int capacity;
    private double loadFactor;
    private int threashold;
    private int size = 0;

    public HashtableSeperateChaining() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashtableSeperateChaining(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashtableSeperateChaining(int capacity, double loadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }
        if (loadFactor <= 0 || loadFactor > 1) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and less than or equal to 1.");
        }

        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.threashold = (int) (capacity * loadFactor);
        table = new LinkedList[this.capacity];
    }

    public int normalizeHash(int hash) {
        return hash % this.capacity;
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        Entry<K, V> entry = new Entry<>(key, value);
        int index = entry.hash;
        return insert(index, entry);
    }

    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        int index = normalizeHash(key.hashCode());
        LinkedList<Entry<K, V>> bucket = table[index];
        return findEntry(bucket, key) != null;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

        // get the bucket
        // get the entry
        // if the entry is not null, return the value

        int index = normalizeHash(key.hashCode());
        LinkedList<Entry<K, V>> bucket = table[index];
        Entry<K, V> entry = findEntry(bucket, key);
        return entry == null ? null : entry.value;
    }

    public V remove(K key) {
        if (key == null)
            return null;
        int index = normalizeHash(key.hashCode());
        LinkedList<Entry<K, V>> bucket = table[index];
        return removeEntryFromTable(bucket, key);
    }


    private V removeEntryFromTable(LinkedList<Entry<K, V>> bucket, K key) {

        Entry<K, V> entry = findEntry(bucket, key);

        if (entry == null)
            return null;

        bucket.remove(entry);
        size--;
        return entry.value;

    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 
     * @param index
     * @param entry
     * @return If an existing key is passed then the previous value gets returned.
     *         If a new pair is passed, then NULL is returned.
     */
    private V insert(int index, Entry<K, V> entry) {

        LinkedList<Entry<K, V>> bucket = table[index];
        if (bucket == null) {
            table[index] = new LinkedList<>();
            bucket = table[index];
        }

        Entry<K, V> oldEntry = findEntry(bucket, entry.key);
        if (oldEntry == null) {
            bucket.add(entry);
            size++;
            if (size > threashold) {
                resize(2 * capacity);
            }

            return null;

        } else {
            oldEntry.value = entry.value;
            return oldEntry.value;
        }
    }

    private void resize(int newCapacity) {

        threashold = (int) (newCapacity * loadFactor);

        LinkedList<Entry<K, V>>[] newTable = new LinkedList[newCapacity];

        for (int i = 0; i < table.length; i++) {

            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    int index = entry.hash;

                    LinkedList<Entry<K, V>> bucket = newTable[index];

                    if (bucket == null) {
                        newTable[index] = new LinkedList<>();
                        bucket = newTable[index];
                    }
                    bucket.add(entry);
                }

                table[i].clear();
                table[i] = null;

            }
        }

        table = newTable;

    }

    private Entry<K, V> findEntry(LinkedList<Entry<K, V>> bucket, K key) {

        if (bucket == null)
            return null;

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

}
