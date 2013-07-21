package org.yield4j.builder;

import static org.yield4j.java.astwrapper.CallMethodWrapper.callMethod;
import static org.yield4j.java.astwrapper.ClassWrapper.classNamed;
import static org.yield4j.java.astwrapper.GeneratedFlags.emptyFlags;
import static org.yield4j.java.astwrapper.MethodWrapper.methodNamed;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.YieldIterator;
import org.yield4j.java.astwrapper.AssignWrapper;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.CallMethodWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.NameWrapper;
import org.yield4j.java.astwrapper.ReturnWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.ThrowWrapper;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.TypeWrapper;

public class IteratorClassBuilder {

    public ClassWrapper build(GeneratorMethodContext context) {
        ExpressionWrapper generatorType = context.getIterableType();

        ExpressionWrapper extending = new TypeWrapper(YieldIterator.class)
                .makeGeneric(generatorType);

        MethodWrapper stepMethod = createStepMethod(context);
        MethodWrapper constructor = createConstructor(context);

        /*
         * Note: I used synthetic first as modifier, both on this class and on
         * the actual generator method. It didn't work on the generator method
         * (not possible to call it), so to be consistent I won't mark the class
         * as synthetic either.
         */
        ClassWrapper newClass = classNamed(context.getIteratorClassName())
                .extending(extending).create();

        // Add methods and variables
        newClass.addMembers(asList(constructor, stepMethod));
        newClass.addMembers(context.getLocalVariables());

        return newClass;
    }

    private MethodWrapper createConstructor(GeneratorMethodContext context) {
        List<DeclareVariableWrapper> originalParams = context.getMethod()
                .getParameters();
        DeclareVariableWrapper[] params = new DeclareVariableWrapper[originalParams
                .size() + 1];
        for (int i = 0; i < originalParams.size(); i++) {
            DeclareVariableWrapper org = originalParams.get(i);
            params[i] = new DeclareVariableWrapper(org.getName(),
                    org.getVariableType(), emptyFlags().addParameter());
        }

        ExpressionWrapper iterableParamType = new TypeWrapper(Iterable.class)
                .makeGeneric(context.getIterableType());
        String iterableParamName = context.generateName("$iterable");
        params[params.length - 1] = new DeclareVariableWrapper(iterableParamName,
                iterableParamType, emptyFlags().addParameter());

        List<StatementWrapper> statements = new ArrayList<StatementWrapper>();
        statements.add(callMethod("super").withArguments(new NameWrapper(iterableParamName)).asStatement());
        for (int i = 0; i < params.length - 1; i++) {
            statements.add(new AssignWrapper(new NameWrapper("this", params[i].getName()), new NameWrapper(params[i].getName())).asStatement());
        }

        return methodNamed("<init>")
                .withParameters(params)
                .withBody(new BlockWrapper(statements))
                .withFlags(emptyFlags()).create();
    }

    private MethodWrapper createStepMethod(GeneratorMethodContext context) {

        CallMethodWrapper call = callMethod(context.getNewMethodName())
                .withArguments(new NameWrapper("this"));
        StatementWrapper ret = new ReturnWrapper(call);

        DeclareVariableWrapper catchParam = new DeclareVariableWrapper("$e",
                new TypeWrapper(Throwable.class), emptyFlags().addParameter());

        ExpressionWrapper throwHidden = callMethod("throwHidden")
                .withArguments(new NameWrapper("$e"));
        StatementWrapper throwStatement = new ThrowWrapper(throwHidden);

        CatchWrapper catcher = new CatchWrapper(catchParam, new BlockWrapper(
                asList(throwStatement)));
        StatementWrapper wrap = new TryWrapper(null, new BlockWrapper(
                asList(ret)), asList(catcher), null);

        BlockWrapper body = new BlockWrapper(asList(wrap));

        return methodNamed("step").withReturnType(boolean.class).withBody(body)
                .withFlags(emptyFlags().addPublic()).overrides().create();
    }
}
