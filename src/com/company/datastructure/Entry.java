package com.company.datastructure;

public class Entry<K, V> {

    int hash;
    K key;
    V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
        this.hash = getHash(key);
    }

    // https://stackoverflow.com/questions/46625819/what-does-0x7fffffff-mean-in-inttime-time1000-0-0x7fffffff
    // handling the negative numbers
    private int getHash(K key) {
        return key.hashCode() & 0x7FFFFFFF;
    }

    @Override
    public String toString() {
        return key + " : " + value;
    }

}
