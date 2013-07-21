package test.org.yield4j.graph;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

public class WhenAVertexIsAddedToAnotherVertex extends GivenTwoVertices {
    private ExampleEdge edge;

    @Override
    protected void when() {
        edge = vertexA.addHead(vertexB, null);
    }

    @Test
    public void thenTheFirstVertexIsATailOfTheSecondOne() {
        assertThat(vertexB.incomingEdges()).contains(edge);
    }
    
    @Test
    public void thenAnEdgeIsCreated() {
        assertThat(edge).isNotNull();
    }
    
    @Test
    public void thenTheTailOfTheEdgeIsTheFirstVertex() {
        assertThat(edge.tail).isSameAs(vertexA);
    }
    
    @Test
    public void thenTheHeadOfTheEdgeIsTheSecondVertex() {
        assertThat(edge.head).isSameAs(vertexB);        
    }
    
    @Test
    public void thenTheOutgoingEdgesOfTheFirstVertexContainsTheEdge() {
        assertThat(vertexA.outgoingEdges()).contains(edge);        
    }
}
