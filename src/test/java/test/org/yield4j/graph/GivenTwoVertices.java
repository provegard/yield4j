package test.org.yield4j.graph;

import test.org.yield4j.Specification;

public class GivenTwoVertices extends Specification {
    protected ExampleVertex vertexA;
    protected ExampleVertex vertexB;

    @Override
    protected void given() {
        vertexA = new ExampleVertex("A");
        vertexB = new ExampleVertex("B");
    }
}