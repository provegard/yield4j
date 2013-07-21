package org.yield4j.builder.statement;

import org.yield4j.graph.Edge;

public class StatementPointer extends Edge<StatementPointer, Statement, Condition> {

    public StatementPointer(Statement tail, Statement head, Condition c) {
        super(tail, head, c);
    }
}