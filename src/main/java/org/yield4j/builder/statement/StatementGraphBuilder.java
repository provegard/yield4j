package org.yield4j.builder.statement;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.LabeledWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.WrapperType;

public class StatementGraphBuilder {
    private final List<StatementWrapper> statements;

    public StatementGraphBuilder(StatementWrapper statement) {
        this(asList(statement));
    }

    public StatementGraphBuilder(List<StatementWrapper> statements) {
        this.statements = statements;
    }

    public Graph build() {
        List<Statement> tails = new ArrayList<Statement>();
        Statement head = build(new LinkedList<StatementWrapper>(statements), tails);
        if (head == null) {
            head = Statement.create(null).describeAs("placeholder");
            tails.add(head);
        }
        return new Graph(head, tails);
    }
    
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        assert tails != null && tails.isEmpty();

        Statement head = null;
        List<Statement> newTails = new ArrayList<Statement>();
        while (!statements.isEmpty()) {
            newTails.clear();
            Statement s = createStatement(statements, newTails);

            if (head == null)
                head = s;

            // Point to s from each of our current tails.
            Iterator<Statement> tailIterator = tails.iterator();
            while (tailIterator.hasNext()) {
                Statement tail = tailIterator.next();
                if (!tail.isBreak() && !tail.isContinue()) {
                    tail.addHead(s, null);
                    tailIterator.remove();
                }
            }

            if (newTails.isEmpty()) {
                // Make s our new tail.
                tails.add(s);
            } else {
                tails.addAll(newTails);
                newTails.clear();
            }
        }

        return head;
    }

    private Statement createStatement(Queue<StatementWrapper> statements, List<Statement> newTails) {

        String label = null;
        StatementWrapper s = statements.poll();
        if (s.getType() == WrapperType.LABELLED) {
            LabeledWrapper ls = (LabeledWrapper) s;
            label = ls.getLabel();
            s = ls.getStatement();
        }

        WrapperType jst = s.getType();
        StatementGraphBuilder sg = null;
        switch (jst) {
        case IF:
            sg = new IfGraphBuilder(s);
            break;
        case FOR:
            sg = new ForGraphBuilder(s, label);
            break;
        case WHILE:
            sg = new WhileGraphBuilder(s, label);
            break;
        case DO:
            sg = new DoWhileGraphBuilder(s, label);
            break;
        case SWITCH:
            sg = new SwitchGraph(s, label);
            break;
        case TRY:
            TryStatement ts = new TryStatement(s);
            newTails.add(ts);
            return ts;
        case BLOCK:
            sg = new BlockGraphBuilder(s);
            break;
        case VARIABLE:
            DeclareVariableWrapper var = (DeclareVariableWrapper) s;
            if (var.getFlags().isFinal()) {
                List<StatementWrapper> varPlusSiblings = new ArrayList<StatementWrapper>();
                varPlusSiblings.add(var);
                for (StatementWrapper next : var.after()) {
                    StatementWrapper alsoNext = statements.poll();
                    assert alsoNext == next;
                    varPlusSiblings.add(alsoNext);
                }
                LocalFinalVariableStatement fs = new LocalFinalVariableStatement(varPlusSiblings);
                newTails.add(fs);
                return fs;
            }
            break;
        }

        if (sg != null) {
            Graph g = sg.build();
            newTails.addAll(g.tails);
            return g.head;
        }
        return Statement.create(s);
    }
    
    public class Graph {
        public final Statement head;
        public final List<Statement> tails;
        
        public Graph(Statement head, List<Statement> tails) { 
            this.head = head;
            this.tails = unmodifiableList(tails);
        }
        
        public void verify(GeneratorMethodContext context) {
            verifyTails(context);
        }

        private void verifyTails(GeneratorMethodContext context) {
            for (Statement tail : tails) {
                if (tail instanceof TargetingStatement) {
                    String label = ((TargetingStatement) tail).getLabel();
                    assert label != null : "Encountered lingering break/continue statement without a label.";
                    String msg = "undefined label: " + label.toString();
                    context.addError(tail.statement, msg);
                }
            }
        }
    }
}
