package test.org.yield4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;

import test.org.yield4j.runner.TestRunner;
import test.util.ResourceFinder;

public class TestRunnerFactory {

    @Factory
    public Object[] factory() throws IOException {
        ResourceFinder finder = new ResourceFinder(
                "org/yield4j/.*/.*\\.java");
        List<TestRunner> list = new ArrayList<TestRunner>();
        for (String resource : finder) {
            list.add(new TestRunner(resource));
        }
        return list.toArray();
    }
}
