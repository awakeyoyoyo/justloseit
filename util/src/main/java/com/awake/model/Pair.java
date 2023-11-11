package com.awake.model;

import java.util.Objects;

public class Pair<T,U> {
    private final T key;
    private final U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return this.key;
    }

    public U getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "Pair [key=" + key + ", value=" + value + "]";
    }
}

