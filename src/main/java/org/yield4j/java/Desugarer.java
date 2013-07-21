package org.yield4j.java;

import static org.yield4j.java.astwrapper.CallMethodWrapper.callMethod;
import static org.yield4j.java.astwrapper.GeneratedFlags.emptyFlags;
import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.AssignWrapper;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.CallMethodWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.FinallyWrapper;
import org.yield4j.java.astwrapper.ForEachWrapper;
import org.yield4j.java.astwrapper.IfWrapper;
import org.yield4j.java.astwrapper.IsNotNullWrapper;
import org.yield4j.java.astwrapper.NameWrapper;
import org.yield4j.java.astwrapper.SimpleWrapperVisitor;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.ThrowWrapper;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.TypeWrapper;
import org.yield4j.java.astwrapper.ValueWrapper;
import org.yield4j.java.astwrapper.WhileWrapper;
import org.yield4j.java.astwrapper.Wrapper;

public class Desugarer extends SimpleWrapperVisitor {

    private final GeneratorMethodContext context;
    private int tryLevel;

    public Desugarer(GeneratorMethodContext context) {
        this.context = context;
    }

    @Override
    public void visit(TryWrapper tryWrapper) {
        StatementWrapper repl = transformTry(tryWrapper);
        if (repl == tryWrapper) {
            super.visit(tryWrapper);
        } else {
            doReplacement(tryWrapper, repl);
        }
    }

    @Override
    public void visit(ForEachWrapper forEachWrapper) {
        StatementWrapper repl = transformForEach(forEachWrapper);
        if (repl == forEachWrapper) {
            super.visit(forEachWrapper);
        } else {
            doReplacement(forEachWrapper, repl);
        }
    }

    private void doReplacement(Wrapper original, Wrapper repl) {
        original.getParent().replace(original, repl);
        repl.accept(this);
    }

    private StatementWrapper transformForEach(ForEachWrapper w) {
        // for (T x : iterable) {
        // ... body ...
        // }
        // =>
        // {
        // java.util.Iterator<T> it_x = getIterator(iterable);
        // while (it_x.hasNext()) {
        // T x = it_x.next();
        // ... body ...
        // }
        // }

        DeclareVariableWrapper init = w.getVariable();
        ExpressionWrapper iterable = w.getExpression();
        StatementWrapper body = w.getStatement();

        ExpressionWrapper initType = init.getVariableType();
        ExpressionWrapper iteratorType = new TypeWrapper(Iterator.class)
                .makeGeneric(initType);

        // Statement 1: java.util.Iterator<T> it_x = getIterator(iterable));
        String iteratorName = context.generateName("$it_"
                + init.getName().toString());
        CallMethodWrapper iteratorFetch = callMethod(
                context.getContextParameterName(), "getIterator")
                .withArguments(iterable);

        DeclareVariableWrapper iteratorVarDecl = new DeclareVariableWrapper(
                iteratorName, iteratorType, iteratorFetch, emptyFlags());

        // Condition: (it_x.hasNext())
        ExpressionWrapper condition = callMethod(iteratorName, "hasNext")
                .withoutArguments();

        // Statement within body: T x = it_x.next();
        ExpressionWrapper moveNextInit = callMethod(iteratorName, "next")
                .withoutArguments();
        StatementWrapper moveNext = new DeclareVariableWrapper(init.getName(),
                initType, moveNextInit, emptyFlags());

        // Wrap in a while loop
        StatementWrapper whileLoop = new WhileWrapper(condition,
                new BlockWrapper(asList(moveNext, body)));

        // TODO: get source positions from original structure!!
        return new BlockWrapper(asList(iteratorVarDecl, whileLoop));
    }

    // http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html:
    /*
     * Note that the close methods of resources are called in the opposite order
     * of their creation.
     */
    /*
     * Note: A try-with-resources statement can have catch and finally blocks
     * just like an ordinary try statement. In a try-with-resources statement,
     * any catch or finally block is run after the resources declared have been
     * closed.
     */

    private StatementWrapper transformTry(TryWrapper w) {

        List<Wrapper> resources = w.getResources();
        FinallyWrapper finalizer = w.getFinalizer();
        List<CatchWrapper> catches = w.getCatchers();
        BlockWrapper block = w.getBlock();

        StatementWrapper ret;
        if (resources.size() == 0) {
            ret = w;
        } else if (finalizer != null || catches.size() > 0) {
            // Extended try-with-resources, move try-with-resources into a
            // try with the catch(ers)/finally. -- JLS 14.20.3.2
            StatementWrapper innerTry = new TryWrapper(resources, block, null,
                    null);
            BlockWrapper body = new BlockWrapper(asList(innerTry));

            ret = new TryWrapper(null, body, catches, finalizer);
        } else {
            // Basic try-with-resources. -- JLS 14.20.3.1
            List<Wrapper> resourcesTail = resources
                    .subList(1, resources.size());
            // TODO: Is this cast ok??
            DeclareVariableWrapper variableDecl = (DeclareVariableWrapper) resources
                    .get(0);

            int suffix = tryLevel++;
            String primaryExcName = context.generateName("primaryExc" + suffix);
            String catchExcName = context.generateName("t" + suffix);
            String suppExcName = context.generateName("suppressedExc" + suffix);

            // TODO: Make the mess below easier to read...

            TypeWrapper throwableType = new TypeWrapper("Throwable");

            // Throwable primaryExc = null
            StatementWrapper primaryExc = new DeclareVariableWrapper(
                    primaryExcName, throwableType, new ValueWrapper(null),
                    emptyFlags());
            // primaryExc = t
            StatementWrapper assignPrimaryExc = new AssignWrapper(
                    new NameWrapper(primaryExcName), new NameWrapper(
                            catchExcName)).asStatement();
            // throw t
            StatementWrapper throwT = new ThrowWrapper(new NameWrapper(
                    catchExcName));
            BlockWrapper catchBody = new BlockWrapper(asList(assignPrimaryExc,
                    throwT));
            // Variable in catch must be a parameter!
            CatchWrapper catch1 = new CatchWrapper(new DeclareVariableWrapper(
                    catchExcName, throwableType, emptyFlags().addParameter()),
                    catchBody);

            StatementWrapper closeResource = callMethod(variableDecl.getName(),
                    "close").withoutArguments().asStatement();

            StatementWrapper addSuppressed = callMethod(primaryExcName,
                    "addSuppressed")
                    .withArguments(new NameWrapper(suppExcName)).asStatement();

            // Variable in catch must be a parameter!
            CatchWrapper suppressCatch = new CatchWrapper(
                    new DeclareVariableWrapper(suppExcName, throwableType,
                            emptyFlags().addParameter()), new BlockWrapper(
                            asList(addSuppressed)));

            StatementWrapper closeResourceInTry = new TryWrapper(null,
                    new BlockWrapper(asList(closeResource)),
                    asList(suppressCatch), null);

            StatementWrapper closeIf = new IfWrapper(new IsNotNullWrapper(
                    new NameWrapper(primaryExcName)), closeResourceInTry,
                    closeResource);

            StatementWrapper finallyIf = new IfWrapper(new IsNotNullWrapper(
                    new NameWrapper(variableDecl.getName())), closeIf, null);
            FinallyWrapper finallyBlock = new FinallyWrapper(asList(finallyIf));

            TryWrapper newTry = new TryWrapper(resourcesTail, block,
                    asList(catch1), finallyBlock);

            ret = new BlockWrapper(asList(variableDecl, primaryExc, newTry));
        }

        return ret;
    }
}
