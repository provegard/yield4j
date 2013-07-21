package test.org.yield4j;

import java.util.Iterator;

import org.yield4j.YieldIterator;

class SingleIntegerIterator extends YieldIterator<Integer> {
    SingleIntegerIterator() {
        super(new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new SingleIntegerIterator();
            }
        });
    }

    @Override
    protected boolean step() {
        if (getIndex(0) == 0) {
            setState(42);
            setIndex(0, -1);
            return true;
        }
        return false;
    }        
}