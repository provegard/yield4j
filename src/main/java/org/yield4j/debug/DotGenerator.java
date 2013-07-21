package org.yield4j.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yield4j.builder.statement.Condition;
import org.yield4j.builder.statement.Statement;
import org.yield4j.builder.statement.StatementPointer;
import org.yield4j.builder.statement.StatementVisitor;

public final class DotGenerator {

    public static String createDot(Statement... statements) {
        StringBuilder sb = new StringBuilder();
        NodeAndEdgeCollector coll = new NodeAndEdgeCollector();
        for (Statement s : statements) {
            s.accept(coll);
        }
        sb.append("digraph yield4j {\n");
        for (Node n : coll.statementNodes.values()) {
            sb.append(String.format("%s [label=\"%s\"];\n", n.getId(),
                    n.getLabel()));
        }
        for (Edge e : coll.edges) {
            String lbl = e.getLabel();
            sb.append(String.format("%s -> %s", e.src.getId(), e.dst.getId()));
            if (!"".equals(lbl)) {
                sb.append(String.format(" [label=\"%s\"]", lbl));
            }
            sb.append(";\n");
        }
        int i = 0;
        for (Statement ep : statements) {
            // For each entry point statement, create a start node and an edge
            // to the statement node.
            Node n = coll.statementNodes.get(ep);
            sb.append(String.format("start%d [label=\"START\"];\n", i));
            sb.append(String.format("start%d -> %s;\n", i, n.getId()));
            i++;
        }
        sb.append("}\n");
        return sb.toString();
    }

    private static class NodeAndEdgeCollector extends StatementVisitor {
        Map<Statement, Node> statementNodes = new HashMap<Statement, Node>();
        List<Edge> edges = new ArrayList<Edge>();

        @Override
        protected void visitStatement(Statement s) {
            Node n = addOrGetNode(s);
            for (StatementPointer ptr : s.incomingEdges()) {
                Node src = addOrGetNode(ptr.tail);
                Edge e = new Edge(src, n, ptr);
                edges.add(e);
            }

            super.visitStatement(s);
        }

        private Node addOrGetNode(Statement s) {
            Node n = statementNodes.get(s);
            if (n == null) {
                n = new Node(s, statementNodes.size());
                statementNodes.put(s, n);
            }
            return n;
        }
    }

    private static class Node {
        private Statement statement;
        private int index;

        Node(Statement s, int idx) {
            statement = s;
            index = idx;
        }

        String getId() {
            return Integer.toString(index);
        }

        String getLabel() {
            return statement.toString().replace(lineSeparator(), "\\n");
        }
    }
    
    private static String lineSeparator() {
        // System.lineSeparator() is Java 7 API
        //TODO: Move to some util!
        return System.getProperty("line.separator");
    }

    private static class Edge {

        private Node src;
        private Node dst;
        private StatementPointer ptr;

        Edge(Node from, Node to, StatementPointer p) {
            src = from;
            dst = to;
            ptr = p;
        }

        String getLabel() {
            Condition c = ptr.data;
            return c != null ? c.toString().replace(lineSeparator(),
                    "\\n") : "";
        }
    }
}
