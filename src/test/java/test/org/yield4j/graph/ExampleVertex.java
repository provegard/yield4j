package test.org.yield4j.graph;

import org.yield4j.graph.Vertex;

public class ExampleVertex extends Vertex<ExampleVertex, ExampleEdge, String> {

    private final String name;

    public ExampleVertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected ExampleEdge createEdge(ExampleVertex tail, ExampleVertex head, String data) {
        return new ExampleEdge(tail, head, data);
    }
}
