package org.yield4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.yield4j.CompilationMessage.MessageType;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.GenericTypeWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.TypeWrapper;

public class GeneratorMethodContext {

    /**
     * The prefix for the inner class create for a generator method.
     */
    private static final String INNER_CLASS_NAME_PREFIX = "$__GeneratorIterator_";
    
    private static final String MAIN_SWITCH_LABEL = "$__mainswitch";
    private static final String MAIN_LOOP_LABEL = "$__loop";

    /**
     * The fixed name of the actual step method that contains the generator
     * state machine. This can be a fixed name as the method takes a parameter
     * that has the inner class as type, and that type will be unique for each
     * generator method (within a top-level class, at least).
     */
    private static final String GEN_METHOD_NAME = "$__generate";

    private final MethodWrapper method;
    private final int index;
    private final ExpressionWrapper type;

    private String iteratorClassName;
    private String contextParameterName;

    private List<DeclareVariableWrapper> variables;
    
    private int currentLevel = 0;
    private int nextFreeLevel = 0;

    private Set<String> usedNames;

    private List<CompilationMessage> messages = new ArrayList<CompilationMessage>();

    public GeneratorMethodContext(MethodWrapper method,
            int index) {
        this.method = method;
        this.index = index;
        this.type = getIterableType(method);
    }

    /**
     * Returns the original generator method.
     * 
     * @return a method
     */
    public MethodWrapper getMethod() {
        return method;
    }

    /**
     * Returns the class that encloses the generator method, and thus also the
     * to-be-created inner iterator class and the actual generator state machine
     * method.
     * 
     * @return a class
     */
    public ClassWrapper getEnclosingClass() {
        return (ClassWrapper) method.getParent();
    }

    /**
     * Returns the generic type of the {@code Iterable} that is returned by the
     * generator method. This will be {@code null} if the {@code Iterable} is
     * non-generic.
     * 
     * @return an expression representing the type, or {@code null}
     */
    public ExpressionWrapper getIterableType() {
        return type;
    }

    /**
     * Returns the non-qualified name of the inner iterator class to create. The
     * name is guaranteed to not conflict with an existing name in the enclosing
     * class (returned by {@link #getEnclosingClass()}.
     * 
     * @return a simple class name
     */
    public String getIteratorClassName() {
        if (iteratorClassName == null) {
            iteratorClassName = generateInnerClassName();
        }
        return iteratorClassName;
    }

    /**
     * Returns the name of the actual generator state machine method to create.
     * Since the method will take an instance of the iterator class as
     * parameter, and the name of the iterator class will be unique, the name
     * returned by this method does not need to be unique in itself.
     * 
     * @return a method name
     */
    // TODO: "new method name" - we could do better!!
    public String getNewMethodName() {
        return GEN_METHOD_NAME;
    }

    private String generateInnerClassName() {
        String name, suffix;
        ClassWrapper clazz = getEnclosingClass();
        int i = 0;
        do {
            suffix = i == 0 ? "" : "_" + i;
            name = INNER_CLASS_NAME_PREFIX + index + suffix;
            i++;
        } while (clazz.isNameOccupied(name));
        return name;
    }

    private ExpressionWrapper getIterableType(MethodWrapper method) {
        ExpressionWrapper e = method.getReturnType();
        ExpressionWrapper t = null;
        if (e instanceof GenericTypeWrapper) {
            t = ((GenericTypeWrapper) e).getTypeParameters().get(0);
        }
        
        if (t == null) {
            t = new TypeWrapper("Object");
        }
        return t;
    }

    public void setLocalVariables(List<DeclareVariableWrapper> variables) {
        assert this.variables == null : "Variables can only be set once.";
        this.variables = variables;
    }
    
    public List<DeclareVariableWrapper> getLocalVariables() {
        assert variables != null : "Variables not set yet!";
        return variables;
    }

    public String getMainSwitchLabel() {
        return MAIN_SWITCH_LABEL + currentLevel;
    }

    public String getMainLoopLabel() {
        return MAIN_LOOP_LABEL + currentLevel;
    }

    public String getContextParameterName() {
        if (contextParameterName == null) {
            contextParameterName = generateName("$context");
        }
        return contextParameterName;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
        nextFreeLevel = Math.max(nextFreeLevel, currentLevel + 1);
    }

    public int getNextFreeLevel() {
        return nextFreeLevel;
    }
    
    public void setUsedNames(Set<String> names) {
        assert usedNames == null : "Used names can only be set once.";
        usedNames = names;
    }
    
    public String generateName(String base) {
        assert usedNames != null : "Used names not set yet.";
        String name;
        int i = 0;
        do {
            name = i == 0 ? base : base + Integer.toString(i);
            i++;
        } while (usedNames.contains(name));
        return name;
    }

    public void addError(StatementWrapper s, String msg) {
        messages.add(new CompilationMessage(s, msg, MessageType.ERROR));
        throw new CancelException();
    }
    
    public void addWarning(StatementWrapper s, String msg) {
        messages.add(new CompilationMessage(s, msg, MessageType.WARNING));
    }

    public List<CompilationMessage> getMessages() {
        return messages;
    }
}
