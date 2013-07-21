package test.org.yield4j.graph;

import org.yield4j.graph.Edge;

public class ExampleEdge extends Edge<ExampleEdge, ExampleVertex, String> {

    public ExampleEdge(ExampleVertex tail, ExampleVertex head, String data) {
        super(tail, head, data);
    }
}
