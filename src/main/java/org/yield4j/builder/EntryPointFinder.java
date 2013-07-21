package org.yield4j.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yield4j.builder.statement.Statement;
import org.yield4j.builder.statement.StatementPointer;
import org.yield4j.builder.statement.StatementVisitor;
import org.yield4j.builder.statement.YieldReturn;

public class EntryPointFinder extends StatementVisitor {
    private List<Statement> entryPoints;
    
    public List<Statement> find(Statement head) {
        entryPoints = new ArrayList<Statement>();
        entryPoints.add(head);
        head.setIndex(0);
        head.accept(this);
        return entryPoints;
    }

    @Override
    protected void visitStatement(Statement s) {
        if (entryPoints.contains(s))
            return;

        boolean isEntryPoint = false;
        Iterator<StatementPointer> itTails = s.incomingEdges().iterator();
        if (itTails.hasNext()) {
            Statement tail = itTails.next().tail;
            if (itTails.hasNext()) {
                // This is a "join" point, e.g. a statement after an if
                // statement, where multiple execution paths come together.
                isEntryPoint = true;
            } else {
                // Single tail.
                if (tail instanceof YieldReturn) {
                    isEntryPoint = true;
                }
            }
        }
        if (isEntryPoint) {
            s.setIndex(entryPoints.size());
            entryPoints.add(s);
        }
    }
}