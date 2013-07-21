package test.org.yield4j;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.yield4j.ArrayIterator;

public class ArrayIteratorTest {

    @Test
    public void test_that_array_of_primitive_booleans_can_be_iterated_over() {
        boolean[] arr = new boolean[] { true, false };
        Iterator<Boolean> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList(true, false));
    }

    @Test
    public void test_that_array_of_primitive_ints_can_be_iterated_over() {
        int[] arr = new int[] { 1, 2, 3 };
        Iterator<Integer> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList(1, 2, 3));
    }

    @Test
    public void test_that_array_of_primitive_longs_can_be_iterated_over() {
        long[] arr = new long[] { 1l, 2l, 3l };
        Iterator<Long> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList(1l, 2l, 3l));
    }

    @Test
    public void test_that_array_of_primitive_floats_can_be_iterated_over() {
        float[] arr = new float[] { 1f, 2f, 3f };
        Iterator<Float> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList(1f, 2f, 3f));
    }

    @Test
    public void test_that_array_of_primitive_doubles_can_be_iterated_over() {
        double[] arr = new double[] { 1d, 2d, 3d };
        Iterator<Double> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList(1d, 2d, 3d));
    }

    @Test
    public void test_that_array_of_primitive_chars_can_be_iterated_over() {
        char[] arr = new char[] { 'a', 'b', 'c' };
        Iterator<Character> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList('a', 'b', 'c'));
    }

    @Test
    public void test_that_array_of_primitive_shorts_can_be_iterated_over() {
        short[] arr = new short[] { (short) 1, (short) 2, (short) 3 };
        Iterator<Short> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(
                asList((short) 1, (short) 2, (short) 3));
    }

    @Test
    public void test_that_array_of_primitive_bytes_can_be_iterated_over() {
        byte[] arr = new byte[] { (byte) 1, (byte) 2, (byte) 3 };
        Iterator<Byte> it = ArrayIterator.create(arr);
        assertThat(iterate(it)).isEqualTo(asList((byte) 1, (byte) 2, (byte) 3));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void test_that_next_throw_for_empty_array() {
        Iterator<String> it = ArrayIterator.create(new String[0]);
        it.next();
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void test_that_next_throw_after_last_next() {
        Iterator<String> it = ArrayIterator.create(new String[] { "x" });
        assertThat(it.next()).isEqualTo("x");
        it.next(); // should throw
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void test_that_removal_is_not_supported() {
        Iterator<String> it = ArrayIterator.create(new String[] { "x" });
        it.next();
        it.remove(); // should throw
    }

    @DataProvider
    public Iterator<Object[]> arrayData() {
        List<Object[]> rows = new ArrayList<Object[]>();
        rows.add(new Object[] { new String[0] });
        rows.add(new Object[] { new String[] { "x" } });
        rows.add(new Object[] { new String[] { "a", "b", "c", "d" } });
        return rows.iterator();
    }

    @Test(dataProvider = "arrayData")
    public void test_that_iterating_with_array_iterator_yields_array_elements(
            Object[] array) {
        Iterator<Object> it = ArrayIterator.create(array);
        assertThat(iterate(it)).isEqualTo(asList(array));
    }

    private <E> Collection<E> iterate(Iterator<E> it) {
        Collection<E> coll = new ArrayList<E>();
        while (it.hasNext()) {
            coll.add(it.next());
        }
        return coll;
    }
}
