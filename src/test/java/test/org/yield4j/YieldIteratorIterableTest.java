package test.org.yield4j;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Iterator;

import org.testng.annotations.Test;

public class YieldIteratorIterableTest {

    @Test
    public void testThatIteratorClassIsAlsoIterable() {
        Object iterator = new SingleIntegerIterator();
        assertThat(iterator).isInstanceOf(Iterable.class);
    }
    
    @Test
    public void testThatTwoIteratorsRetrievedFromIterableAreDifferent() {
        Iterable<Integer> iterable = (Iterable<Integer>) new SingleIntegerIterator();
        Iterator<Integer> iterator1 = iterable.iterator();
        Iterator<Integer> iterator2 = iterable.iterator();
        assertThat(iterator2).isNotSameAs(iterator1);
    }
}
