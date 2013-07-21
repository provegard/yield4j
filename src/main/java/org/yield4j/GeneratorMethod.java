package org.yield4j;

import static java.util.Arrays.asList;

import java.util.Set;

import org.yield4j.builder.IteratorClassBuilder;
import org.yield4j.builder.NewIterableMethodBodyBuilder;
import org.yield4j.builder.StepMethodBuilder;
import org.yield4j.java.Desugarer;
import org.yield4j.java.IdentifierScanner;
import org.yield4j.java.LocalVariableMarker;
import org.yield4j.java.NameFinder;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;

public class GeneratorMethod {

    private final MethodWrapper method;
    private final int methodIndex;
    private GeneratorMethodContext context;

    public GeneratorMethod(MethodWrapper method,
            int methodIndex) {
        this.method = method;
        this.methodIndex = methodIndex;
    }
    
    public ClassWrapper getKlass() {
        return (ClassWrapper) method.getParent();
    }
    
    public MethodWrapper getMethod() {
        return method;
    }

    public void transform(IdentifierScanner identScanner) {
        assert context != null : "Forgot to preprocess!";
        
        // Prepare for code generation by marking wrappers that refer to
        // names of locally declared variables.
        method.accept(new LocalVariableMarker(identScanner));

        MethodWrapper newMeth = buildStepMethod(context);
        ClassWrapper innerClass = buildIteratorClass(context);
        BlockWrapper body = buildMethodBody(context);

        // Finally - update class and method with replacements/additions.
        method.setBody(body);
        getKlass().addMembers(asList(newMeth, innerClass));
    }
    
    public void preprocess(NameFinder nameFinder) {
        context = createContext();
        
        // Find out which names the method uses. We need to do this to avoid a
        // name collision with the context parameter, i.e. we need to name the
        // context parameter in such a way that it doesn't conflict with a
        // non-local name.
        Set<String> names = nameFinder.findNames(method);
        context.setUsedNames(names);

        // Do some early desugaring. This is normally done by the compiler at
        // a later stage, but we need to do it already now in order to handle
        // certain statements.
        Desugarer desugar = new Desugarer(context);
        method.accept(desugar);        
    }
    
    private BlockWrapper buildMethodBody(GeneratorMethodContext context) {
        NewIterableMethodBodyBuilder b3 = new NewIterableMethodBodyBuilder();
        return b3.build(context);
    }

    private ClassWrapper buildIteratorClass(GeneratorMethodContext context) {
        IteratorClassBuilder b2 = new IteratorClassBuilder();
        ClassWrapper innerClass = b2.build(context);
        return innerClass;
    }

    private MethodWrapper buildStepMethod(GeneratorMethodContext context) {
        StepMethodBuilder b = new StepMethodBuilder();
        MethodWrapper newMeth = b.build(context);

        context.setLocalVariables(b.getVariables());
        return newMeth;
    }

    private GeneratorMethodContext createContext() {
        return new GeneratorMethodContext(method, methodIndex);
    }

    public GeneratorMethodContext getContext() {
        return context;
    }
}
