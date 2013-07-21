package test.org.yield4j.graph;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Iterator;

import org.testng.annotations.Test;

public class WhenAVertexIsRemovedFromAnotherVertex extends GivenTwoVertices {

    @Override
    protected void given() {
        super.given();
        vertexA.addHead(vertexB, "data");
    }

    @Override
    protected void when() {
        Iterator<ExampleEdge> it = vertexB.incomingEdges().iterator();
        it.next();
        it.remove();
    }

    @Test
    public void thenTheEdgeHasBeenRemovedAsOutgoingEdge() {
        assertThat(vertexA.outgoingEdges().iterator().hasNext()).isFalse();        
    }

    @Test
    public void thenTheSecondVertexHasNoTails() {
        assertThat(vertexB.incomingEdges().iterator().hasNext()).isFalse();        
    }

}
