package test.org.yield4j;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Iterator;

import org.testng.annotations.Test;

import org.yield4j.YieldIterator;

public class YieldIteratorInvariantsTest {

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void test_that_remove_is_not_supported() {
        Iterator<Integer> iterator = new SingleIntegerIterator();
        iterator.next();
        iterator.remove();
    }
    
    @Test
    public void test_that_index_can_be_retrieved() {
        YieldIterator<Integer> iterator = new SingleIntegerIterator();
        assertThat(iterator.getIndex(0)).isEqualTo(0);
    }
    
    @Test
    public void test_that_index_can_be_retrieved_for_new_level() {
        YieldIterator<Integer> iterator = new SingleIntegerIterator();
        assertThat(iterator.getIndex(1)).isEqualTo(0);
    }
    
    @Test
    public void test_that_index_can_be_updated() {
        YieldIterator<Integer> iterator = new SingleIntegerIterator();
        iterator.setIndex(0, 1);
        assertThat(iterator.getIndex(0)).isEqualTo(1);
    }

    @Test
    public void test_that_index_can_be_updated_for_new_level() {
        YieldIterator<Integer> iterator = new SingleIntegerIterator();
        iterator.setIndex(1, 1);
        assertThat(iterator.getIndex(1)).isEqualTo(1);
    }

    @Test
    public void test_that_index_can_be_updated_for_arbitrary_level() {
        YieldIterator<Integer> iterator = new SingleIntegerIterator();
        iterator.setIndex(100, 1);
        assertThat(iterator.getIndex(100)).isEqualTo(1);
    }

}
