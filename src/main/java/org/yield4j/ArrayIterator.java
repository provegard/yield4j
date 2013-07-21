package org.yield4j;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ArrayIterator<E> implements Iterator<E> {

    private int idx;

    protected ArrayIterator() {
        idx = -1;
    }

    public static <T> ArrayIterator<T> create(T[] array) {
        return new ReferenceArrayIterator<T>(array);
    }

    public static ArrayIterator<Integer> create(int[] array) {
        return new IntArrayIterator(array);
    }

    public static ArrayIterator<Long> create(long[] array) {
        return new LongArrayIterator(array);
    }

    public static ArrayIterator<Float> create(float[] array) {
        return new FloatArrayIterator(array);
    }

    public static ArrayIterator<Double> create(double[] array) {
        return new DoubleArrayIterator(array);
    }

    public static ArrayIterator<Character> create(char[] array) {
        return new CharArrayIterator(array);
    }

    public static ArrayIterator<Short> create(short[] array) {
        return new ShortArrayIterator(array);
    }

    public static ArrayIterator<Byte> create(byte[] array) {
        return new ByteArrayIterator(array);
    }

    public static ArrayIterator<Boolean> create(boolean[] array) {
        return new BooleanArrayIterator(array);
    }

    @Override
    public boolean hasNext() {
        return idx < getLength() - 1;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return getElement(++idx);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    protected abstract int getLength();

    protected abstract E getElement(int index);

    private static class ReferenceArrayIterator<T> extends ArrayIterator<T> {
        private T[] array;

        private ReferenceArrayIterator(T[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected T getElement(int index) {
            return array[index];
        }
    }

    private static class IntArrayIterator extends ArrayIterator<Integer> {
        private int[] array;

        private IntArrayIterator(int[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Integer getElement(int index) {
            return array[index];
        }
    }

    private static class LongArrayIterator extends ArrayIterator<Long> {
        private long[] array;

        private LongArrayIterator(long[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Long getElement(int index) {
            return array[index];
        }
    }

    private static class FloatArrayIterator extends ArrayIterator<Float> {
        private float[] array;

        private FloatArrayIterator(float[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Float getElement(int index) {
            return array[index];
        }
    }

    private static class DoubleArrayIterator extends ArrayIterator<Double> {
        private double[] array;

        private DoubleArrayIterator(double[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Double getElement(int index) {
            return array[index];
        }
    }

    private static class CharArrayIterator extends ArrayIterator<Character> {
        private char[] array;

        private CharArrayIterator(char[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Character getElement(int index) {
            return array[index];
        }
    }

    private static class ShortArrayIterator extends ArrayIterator<Short> {
        private short[] array;

        private ShortArrayIterator(short[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Short getElement(int index) {
            return array[index];
        }
    }

    private static class ByteArrayIterator extends ArrayIterator<Byte> {
        private byte[] array;

        private ByteArrayIterator(byte[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Byte getElement(int index) {
            return array[index];
        }
    }

    private static class BooleanArrayIterator extends ArrayIterator<Boolean> {
        private boolean[] array;

        private BooleanArrayIterator(boolean[] array) {
            this.array = array;
        }

        @Override
        protected int getLength() {
            return array.length;
        }

        @Override
        protected Boolean getElement(int index) {
            return array[index];
        }
    }
}
