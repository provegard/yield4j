package test.org.yield4j;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.yield4j.YieldIterator;

public class YieldIteratorTest {

    public class When_step_method_returns_false {
        private Iterator<Integer> iterator;

        @BeforeMethod
        public void becauseOf() {
            iterator = new YieldIterator<Integer>(null) {
                @Override
                protected boolean step() {
                    return false;
                }
            };
        }

        @Test
        public void then_there_is_no_next_element() {
            assertThat(iterator.hasNext()).isFalse();
        }

        @Test(expectedExceptions = NoSuchElementException.class)
        public void then_moving_to_next_throws() {
            iterator.next();
        }
    }

    public class When_step_method_returns_true_then_false {
        private Iterator<Integer> iterator;

        @BeforeMethod
        public void becauseOf() {
            iterator = new SingleIntegerIterator();
        }

        @Test
        public void then_there_is_a_next_element() {
            assertThat(iterator.hasNext()).isTrue();
        }

        @Test
        public void then_moving_to_next_retrieves_the_state() {
            assertThat(iterator.next()).isEqualTo(42);
        }

        @Test
        public void then_there_is_no_next_element_after_moving() {
            iterator.next();
            assertThat(iterator.hasNext()).isFalse();
        }
    }
}
