package org.yield4j.java;

import org.yield4j.java.IdentifierScanner.IdentifierAction;
import org.yield4j.java.astwrapper.AnyExpressionWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.NameWrapper;
import org.yield4j.java.astwrapper.Scope;
import org.yield4j.java.astwrapper.SimpleWrapperVisitor;
import org.yield4j.java.astwrapper.ValueWrapper;
import org.yield4j.java.astwrapper.Wrapper;

public class LocalVariableMarker extends SimpleWrapperVisitor {

    private Scope stopScope;
    private IdentifierScanner identScanner;

    public LocalVariableMarker(IdentifierScanner scanner) {
        identScanner = scanner;
    }

    // TODO: When checking a name, make sure that it's the _first_, i.e.
    // name.x => ok but x.name => nok!

    @Override
    public void visit(MethodWrapper methodWrapper) {
        stopScope = methodWrapper.getBody().getScope();
        super.visit(methodWrapper);
    }

    @Override
    public void visit(ValueWrapper valueWrapper) {
        check(valueWrapper);
        super.visit(valueWrapper);
    }

    @Override
    public void visit(NameWrapper nameWrapper) {
        // Cannot check this via ident scanner, as we have created it ourselves.
        checkNames(nameWrapper, nameWrapper.getNameParts());
        super.visit(nameWrapper);
    }

    @Override
    public void visit(AnyExpressionWrapper anyExpressionWrapper) {
        check(anyExpressionWrapper);
        super.visit(anyExpressionWrapper);
    }

    private void checkNames(Wrapper w, String[] names) {
        for (String name : names) {
            if (w.getScope().isVisibleVariable(name, stopScope)) {
                w.setNeedsLocalNameResolution(true);
            }
        }
    }

    private void check(ExpressionWrapper w) {
        if (w.getTarget() != null) {

            final Scope scope = w.getScope();
            assert scope != null : "No scope for " + w;
            final boolean[] found = new boolean[1];
            identScanner.scan(w, new IdentifierAction() {
                @Override
                public void action(String identifier) {
                    if (scope.isVisibleVariable(identifier, stopScope)) {
                        found[0] = true;
                    }
                }
            });

            if (found[0]) {
                w.setNeedsLocalNameResolution(true);
            }
        }
    }
}
