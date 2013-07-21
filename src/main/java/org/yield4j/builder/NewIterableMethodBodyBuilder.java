package org.yield4j.builder;

import static org.yield4j.java.astwrapper.ClassWrapper.anonymousClass;
import static org.yield4j.java.astwrapper.GeneratedFlags.emptyFlags;
import static org.yield4j.java.astwrapper.MethodWrapper.methodNamed;
import static org.yield4j.java.astwrapper.NewInstanceWrapper.newInstanceOfClass;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.NameWrapper;
import org.yield4j.java.astwrapper.NewInstanceWrapper;
import org.yield4j.java.astwrapper.ReturnWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.TypeWrapper;
import org.yield4j.java.astwrapper.ValueWrapper;

public class NewIterableMethodBodyBuilder {

    private static final String ITERATOR_METHOD_NAME = "iterator";

    public BlockWrapper build(GeneratorMethodContext context) {
        ExpressionWrapper iterableType = context.getIterableType();
        TypeWrapper classType = new TypeWrapper(context.getIteratorClassName());
        List<StatementWrapper> bodyStatements = new ArrayList<StatementWrapper>();

        // 1. Add final version of parameters
        List<DeclareVariableWrapper> methodParams = context.getMethod()
                .getParameters();
        for (DeclareVariableWrapper param : methodParams) {
            // it.x = final_x;
            String finalVarName = "final_" + param.getName();

            ExpressionWrapper parameterType = param.getVariableType();
            ExpressionWrapper init = new NameWrapper(param.getName());
            DeclareVariableWrapper varDecl = new DeclareVariableWrapper(
                    finalVarName, parameterType, init, emptyFlags().addFinal());
            bodyStatements.add(varDecl);
        }

        // 2. Create the iterator factory
        ExpressionWrapper returnType = new TypeWrapper(Iterator.class)
                .makeGeneric(iterableType);
        BlockWrapper body = new BlockWrapper(asList(returnNewIterator(classType, methodParams, null)));
        MethodWrapper iteratorMethod = methodNamed(ITERATOR_METHOD_NAME)
                .withReturnType(returnType).withBody(body)
                .withFlags(emptyFlags().addPublic()).overrides().create();
        ClassWrapper anonClass = anonymousClass().create();
        anonClass.addMembers(asList(iteratorMethod));
        ExpressionWrapper iteratorFactory = newInstanceOfClass(
                new TypeWrapper(Iterable.class).makeGeneric(iterableType))
                .withImplementation(anonClass).create();

        // 3. Create new iterator, passing the final args + the iterator factory
        bodyStatements.add(returnNewIterator(classType, methodParams, iteratorFactory));

        return new BlockWrapper(bodyStatements);
    }

    private StatementWrapper returnNewIterator(TypeWrapper iteratorType, List<DeclareVariableWrapper> methodParams, ExpressionWrapper lastArg) {
        ExpressionWrapper[] allArgs = new ExpressionWrapper[methodParams.size() + 1];
        for (int i = 0; i < allArgs.length - 1; i++) {
            allArgs[i] = new NameWrapper("final_"
                    + methodParams.get(i).getName());
        }
        allArgs[allArgs.length - 1] = lastArg != null ? lastArg : new ValueWrapper(null);
        NewInstanceWrapper newIterator = newInstanceOfClass(iteratorType)
                .withArguments(allArgs).create();

        return new ReturnWrapper(newIterator);
    }
}
