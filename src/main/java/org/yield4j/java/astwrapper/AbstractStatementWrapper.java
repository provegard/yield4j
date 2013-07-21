package org.yield4j.java.astwrapper;

import static java.util.Collections.emptyList;

import java.util.List;

public abstract class AbstractStatementWrapper extends AbstractWrapper implements StatementWrapper {

    protected AbstractStatementWrapper(Object target) {
        super(target);
    }
    
    @Override
    public Iterable<StatementWrapper> after() {
        Wrapper parent = getParent();
        // Instance check covers block as well as finally.
        if (parent instanceof BlockWrapper) {
            List<StatementWrapper> statements = ((BlockWrapper) parent).getStatements();
            int idx = statements.indexOf(this);
            if (idx >= 0) {
                return statements.subList(idx + 1, statements.size());
            }
        }
        return emptyList();
    }
}
