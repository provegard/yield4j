package org.yield4j.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Vertex<TVertex extends Vertex<TVertex, TEdge, TData>, TEdge extends Edge<TEdge, TVertex, TData>, TData> {

    List<TEdge> outgoing = new ArrayList<TEdge>();
    List<TEdge> incoming = new ArrayList<TEdge>();

    @SuppressWarnings("unchecked")
    public TEdge addHead(TVertex head, TData data) {
        TEdge edge = createEdge((TVertex) this, head, data);
        outgoing.add(edge);
        head.incoming.add(edge);
        return edge;
    }

    protected abstract TEdge createEdge(TVertex tail, TVertex head, TData data);

    public Iterable<TEdge> outgoingEdges() {
        return new EdgeIterator(outgoing, false);
    }

    public Iterable<TEdge> incomingEdges() {
        return new EdgeIterator(incoming, true);
    }

    private class EdgeIterator implements Iterator<TEdge>, Iterable<TEdge> {
        private final Iterator<TEdge> edgeIterator;
        private TEdge currentEdge;
        private boolean isIteratingOverIncoming;

        EdgeIterator(List<TEdge> list, boolean incoming) {
            this.edgeIterator = list.iterator();
            isIteratingOverIncoming = incoming;
        }

        @Override
        public boolean hasNext() {
            return edgeIterator.hasNext();
        }

        @Override
        public TEdge next() {
            currentEdge = edgeIterator.next();
            return currentEdge;
        }

        @Override
        public void remove() {
            edgeIterator.remove();
            if (isIteratingOverIncoming) {
                // Remove from outgoing of the edge tail
                boolean removed = currentEdge.tail.outgoing.remove(currentEdge);
                assert removed;
            } else {
                // Remove from incoming off the edge head
                boolean removed = currentEdge.head.incoming.remove(currentEdge);
                assert removed;
            }
        }

        @Override
        public Iterator<TEdge> iterator() {
            return this;
        }
    }
}
