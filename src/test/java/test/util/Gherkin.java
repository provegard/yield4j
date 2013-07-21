package test.util;

import org.testng.annotations.BeforeClass;

public class Gherkin {

    @BeforeClass
    public final void setup() {
        given();
        when();
    }

    protected void given() {
    }

    protected void when() {
    }
}
