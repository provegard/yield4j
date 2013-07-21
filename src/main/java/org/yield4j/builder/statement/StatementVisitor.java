package org.yield4j.builder.statement;

import java.util.ArrayList;
import java.util.List;

public class StatementVisitor {

    private List<Statement> seen = new ArrayList<Statement>();

    public final void visit(Statement s) {
        if (seen.contains(s))
            return;
        seen.add(s);

        visitStatement(s);
        for (StatementPointer sp : s.outgoingEdges()) {
            sp.head.accept(this);
        }
    }

    protected void visitStatement(Statement s) {
    }
}