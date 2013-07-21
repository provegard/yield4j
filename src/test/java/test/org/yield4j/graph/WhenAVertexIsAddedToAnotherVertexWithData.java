package test.org.yield4j.graph;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

public class WhenAVertexIsAddedToAnotherVertexWithData extends GivenTwoVertices {

    private ExampleEdge edge;

    @Override
    protected void when() {
        edge = vertexA.addHead(vertexB, "data");
    }
    
    @Test
    public void thenTheDataAreExpoedViaTheEdge() {
        assertThat(edge.data).isEqualTo("data");
    }
}
