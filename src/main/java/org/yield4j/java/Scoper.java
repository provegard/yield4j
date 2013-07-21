package org.yield4j.java;

import java.util.Stack;

import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.FinallyWrapper;
import org.yield4j.java.astwrapper.ForEachWrapper;
import org.yield4j.java.astwrapper.ForWrapper;
import org.yield4j.java.astwrapper.IrrelevantWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.Scope;
import org.yield4j.java.astwrapper.SimpleWrapperVisitor;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.WrapperType;

public class Scoper extends SimpleWrapperVisitor {

    private Stack<Scope> scopes = new Stack<Scope>();

    public Scoper() {
        scopes.push(new Scope(null));
    }
    
    public void enter(Wrapper w) {
        accept(w);
    }

    @Override
    protected void accept(Wrapper w) {
        w.setScope(scopes.peek());
        super.accept(w);
    }

    private Scope newChildScope() {
        return scopes.peek().newChild();
    }

    @Override
    public void visit(ForWrapper forWrapper) {
        // JLS 6.3: The scope of a local variable declared in the ForInit part
        // of a basic for statement (§14.14.1) includes all of the following:
        // * Its own initializer
        // * Any further declarators to the right in the ForInit part of the for
        // statement
        // * The Expression and ForUpdate parts of the for statement
        // * The contained Statement
        scopes.push(newChildScope());
        super.visit(forWrapper);
        scopes.pop();
    }

    @Override
    public void visit(DeclareVariableWrapper var) {
        // JLS 6.3: The scope of a local variable declaration in a block (§14.4)
        // is the rest of the block in which the declaration appears, starting
        // with its own initializer and including any further declarators to the
        // right in the local variable declaration statement.
        if (var.getParent().getType() == WrapperType.CLASS) {
            scopes.peek().insert(var, var.getName());
        } else {
            scopes.peek().addLocalVariable(var);
        }
        super.visit(var);
    }

    @Override
    public void visit(BlockWrapper blockWrapper) {
        scopes.push(newChildScope());
        super.visit(blockWrapper);
        scopes.pop();
    }

    @Override
    public void visit(FinallyWrapper finallyWrapper) {
        scopes.push(newChildScope());
        super.visit(finallyWrapper);
        scopes.pop();
    }

    @Override
    public void visit(MethodWrapper methodWrapper) {
        scopes.peek().insert(methodWrapper, methodWrapper.getName());
        // JLS 6.3: The scope of a formal parameter of a method (§8.4.1) or
        // constructor (§8.8.1) is the entire body of the method or constructor.
        scopes.push(newChildScope());
        super.visit(methodWrapper);
        scopes.pop();
    }

    @Override
    public void visit(ClassWrapper classWrapper) {
        scopes.peek().insert(classWrapper, classWrapper.getName());
        scopes.push(newChildScope());
        super.visit(classWrapper);
        scopes.pop();
    }

    @Override
    public void visit(CatchWrapper catchWrapper) {
        // JLS 6.3: The scope of a parameter of an exception handler that is
        // declared in a catch clause of a try statement (§14.20) is the entire
        // block associated with the catch.
        scopes.push(newChildScope());
        super.visit(catchWrapper);
        scopes.pop();
    }

    @Override
    public void visit(ForEachWrapper forEachWrapper) {
        throw new AssertionError(
                "Enhanced for loop should've been desugarized by now.");
    }

    @Override
    public void visit(TryWrapper tryWrapper) {
        if (tryWrapper.getResources().size() > 0) {
            throw new AssertionError(
                    "Try-with-resources should've been desugarized by now.");
        }
        super.visit(tryWrapper);
    }

    @Override
    public void visit(IrrelevantWrapper irrelevantWrapper) {
        String name = irrelevantWrapper.getName();
        if (name != null) {
            scopes.peek().insert(irrelevantWrapper, name);
        }
        super.visit(irrelevantWrapper);
    }

    // JLS 6.3: The scope of a local variable declared in the FormalParameter
    // part of an enhanced for statement (§14.14.2) is the contained Statement.

    // JLS 6.3: The scope of a variable declared in the ResourceSpecification of
    // a try-with-resources statement (§14.20.3) is from the declaration
    // rightward over the remainder of the ResourceSpecification and the entire
    // try block associated with the try-with-resources statement.
}
