package org.yield4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class YieldIterator<T> implements Iterator<T>, Iterable<T> {

    private Map<Integer, Integer> indices = new HashMap<Integer, Integer>();
    private T state;

    private boolean result;
    private boolean needsMove;
    private boolean iteratorHasBeenUsed;
    private final Iterable<T> iteratorFactory;

    /**
     * Flag that is set to true if the step method exits with an exception. In
     * this case, the generator is considered done, and there will be no more
     * values generated.
     */
    private boolean isDoneByException;

    /**
     * Flag that indicates if finally statements should be run "on the way out".
     * This is set to true before each call to the step method. If the step
     * method sets the state as part of yield return, the flag is temporarily
     * set to false, so that finally statements are skipped.
     */
    private boolean shouldRunFinally;

    protected YieldIterator(Iterable<T> factory) {
        iteratorFactory = factory;
        needsMove = true;
    }

    private void moveForward() {
        shouldRunFinally = true;
        try {
            result = step();
        } catch (Exception ex) {
            isDoneByException = true;
            throwHidden(ex);
        }
    }

    public void setState(T state) {
        // Prepare for a return that will skip all finally statements.
        shouldRunFinally = false;
        this.state = state;
    }

    public boolean shouldRunFinally() {
        return shouldRunFinally;
    }

    public void setIndex(int level, int index) {
        indices.put(level, index);
    }

    public int getIndex(int level) {
        if (indices.containsKey(level)) {
            return indices.get(level);
        }
        indices.put(level, 0);
        return 0;
    }

    @Override
    public boolean hasNext() {
        if (isDoneByException)
            return false;
        maybeMove();
        return result;
    }

    private void maybeMove() {
        if (needsMove) {
            moveForward();
            needsMove = false;
        }
    }

    @Override
    public T next() {
        if (isDoneByException)
            throw new NoSuchElementException();
        maybeMove();
        if (!result)
            throw new NoSuchElementException();
        T ret = state;
        needsMove = true;
        return ret;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Iterator<T> iterator() {
        if (!iteratorHasBeenUsed) {
            iteratorHasBeenUsed = true;
            return this;
        } else {
            return iteratorFactory.iterator();
        }
    }

    protected abstract boolean step();

    public <E> Iterator<E> getIterator(Iterable<E> iterable) {
        return iterable.iterator();
    }

    public Iterator<Integer> getIterator(int[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Short> getIterator(short[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Long> getIterator(long[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Float> getIterator(float[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Double> getIterator(double[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Character> getIterator(char[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Byte> getIterator(byte[] array) {
        return ArrayIterator.create(array);
    }

    public Iterator<Boolean> getIterator(boolean[] array) {
        return ArrayIterator.create(array);
    }

    public <E> Iterator<E> getIterator(E[] array) {
        return ArrayIterator.create(array);
    }

    protected RuntimeException throwHidden(Throwable t) {
        this.<RuntimeException> throwGeneric(t);
        return null;
    }

    @SuppressWarnings("unchecked")
    private <TEx extends Throwable> void throwGeneric(Throwable t)
            throws TEx {
        throw (TEx) t;
    }
}
