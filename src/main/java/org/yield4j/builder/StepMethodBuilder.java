package org.yield4j.builder;

import static java.util.Arrays.asList;
import static org.yield4j.java.astwrapper.GeneratedFlags.emptyFlags;
import static org.yield4j.java.astwrapper.MethodWrapper.methodNamed;
import static org.yield4j.java.astwrapper.ReturnWrapper.returnFalse;

import java.util.ArrayList;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.builder.statement.StatementGraphBuilder;
import org.yield4j.builder.statement.StatementGraphBuilder.Graph;
import org.yield4j.java.JavaTreeVerifier;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.MethodWrapper.Builder;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.TypeWrapper;

public class StepMethodBuilder {

    private List<DeclareVariableWrapper> variables;

    public List<DeclareVariableWrapper> getVariables() {
        return variables;
    }

    public MethodWrapper build(GeneratorMethodContext context) {
        // Temporary instance that allows us to do some stuff before we create
        // the actual tree.
        MethodWrapper method = context.getMethod();

        // Extract local variables.
        variables = method.extractLocalVariables();
        for (DeclareVariableWrapper w : method.getParameters()) {
            variables.add(new DeclareVariableWrapper(w.getName(), w
                    .getVariableType(), emptyFlags()));
        }

        BlockWrapper body = method.getBody();

        // First verification
        new JavaTreeVerifier(body).verify(context);

        StateMachine sm = new StateMachine();
        Graph g = new StatementGraphBuilder(body).build();

        // Second verification
        g.verify(context);

        StatementWrapper newBody = sm.build(context, g);

        MethodWrapper newMeth = createStepMethod(context, newBody);
        newMeth.setLocalVariableSelector(context.getContextParameterName());
        newMeth.setLocalVariableNames(toNames(variables));

        return newMeth;
    }

    private List<String> toNames(List<DeclareVariableWrapper> vars) {
        List<String> names = new ArrayList<String>();
        for (DeclareVariableWrapper w : vars) {
            names.add(w.getName());
        }
        return names;
    }

    private MethodWrapper createStepMethod(GeneratorMethodContext context,
            StatementWrapper body) {

        String paramName = context.getContextParameterName();
        String paramTypeName = context.getIteratorClassName();
        DeclareVariableWrapper param1 = new DeclareVariableWrapper(paramName,
                new TypeWrapper(paramTypeName), emptyFlags().addParameter());

        BlockWrapper bodyBlock = new BlockWrapper(asList(body, returnFalse()));

        /*
         * Note: I thought it would be nice to make the method synthetic, but
         * that means that we cannot call it from the iterator class (resolution
         * ignores synthetic methods, which is obvious when you think about it).
         * So no modifiers it is!
         */
        Builder builder = methodNamed(context.getNewMethodName())
                .withReturnType(boolean.class).withParameters(param1)
                .withBody(bodyBlock).withThrows(Throwable.class);
        if (context.getMethod().getFlags().isSynchronized()) {
            builder.withFlags(emptyFlags().addSynchronized());
        }
        return builder.create();
    }
}
