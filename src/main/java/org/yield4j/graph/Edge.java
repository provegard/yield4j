package org.yield4j.graph;

public class Edge<TEdge extends Edge<TEdge, TVertex, TData>, TVertex extends Vertex<TVertex, TEdge, TData>, TData> {

    public final TVertex tail;
    public final TVertex head;
    public final TData data;

    public Edge(TVertex tail, TVertex head, TData data) {
        this.tail = tail;
        this.head = head;
        this.data = data;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(tail.toString());
        sb.append(" -> ").append(head);
        if (data != null) {
            sb.append(" [").append(data).append("]");
        }
        return sb.toString();
    }
}
