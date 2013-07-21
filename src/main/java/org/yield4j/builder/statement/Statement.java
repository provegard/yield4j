package org.yield4j.builder.statement;

import static org.yield4j.java.astwrapper.CallMethodWrapper.callMethod;

import java.util.Iterator;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.graph.Vertex;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.ContinueWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.WrapperType;

public abstract class Statement extends
        Vertex<Statement, StatementPointer, Condition> {
    protected StatementWrapper statement;

    private int index = -1;

    private String description;

    protected Statement(StatementWrapper statement) {
        this.statement = statement;
    }

    public Statement describeAs(String s) {
        description = s;
        return this;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int findIndex() {
        if (index >= 0)
            return index;
        Iterator<StatementPointer> tails = incomingEdges().iterator();
        assert tails.hasNext() : "No tails!";
        return tails.next().tail.findIndex();
    }

    public static Statement create(StatementWrapper statement) {
        if (statement == null) {
            return new NoopStatement();
        }
        WrapperType t = statement.getType();
        switch (t) {
        case YIELDRETURN:
            return new YieldReturn(statement);
        case YIELDBREAK:
            return new YieldBreak(statement);
        case CONTINUE:
            return new ContinueStatement((ContinueWrapper) statement);
        case BREAK:
            return new BreakStatement((BreakWrapper) statement);
        }
        return new GenericStatement(statement);
    }

    public final void emit(List<StatementWrapper> statements,
            GeneratorMethodContext context) {
        emitThis(statements, context);
        new StatementPointers(outgoingEdges()).emit(statements, context);
    }

    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {
        if (statement != null) {
            statements.add(statement);
        }
    }

    /**
     * Finds a pointer to the given statement head. If the head is among the
     * heads of this statement, the pointer to the head is returned. Otherwise,
     * {@code null} is returned.
     * 
     * @param head
     *            the head to look for
     * @return a statement pointer or {@code null}
     */
    public StatementPointer findPointerToHead(Statement head) {
        for (StatementPointer p : outgoingEdges()) {
            if (p.head == head) {
                return p;
            }
        }
        return null;
    }

    // TODO: better name!
    public abstract void transform(GeneratorMethodContext context,
            StatementPointer ptr);

    public void detachFromTails() {
        Iterator<StatementPointer> tailIt = incomingEdges().iterator();
        while (tailIt.hasNext()) {
            tailIt.next();
            tailIt.remove();
        }
    }

    public boolean isContinue() {
        return false;
    }

    public boolean isBreak() {
        return false;
    }

    public void accept(StatementVisitor v) {
        v.visit(this);
    }

    protected StatementWrapper createStateAssignment(
            GeneratorMethodContext context, Wrapper value) {
        return callMethod(context.getContextParameterName().toString(),
                "setState").withArguments(value).asStatement();
    }

    protected StatementWrapper createIndexAssignment(
            GeneratorMethodContext context, int index) {
        return callMethod(context.getContextParameterName(), "setIndex")
                .withArguments(context.getCurrentLevel(), index).asStatement();
    }

    @Override
    public String toString() {
        if (statement == null && description == null) {
            throw new IllegalStateException(
                    "Description is needed when statement is null.");
        }
        StringBuilder sb = new StringBuilder();
        if (index >= 0) {
            sb.append("[").append(index).append("] ");
        }
        sb.append(statement != null ? statement.toString() : description);
        return sb.toString();
    }

    @Override
    protected StatementPointer createEdge(Statement tail, Statement head,
            Condition c) {
        return new StatementPointer(tail, head, c);
    }
}