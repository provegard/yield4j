package org.yield4j.java.astwrapper;

import java.util.List;


public class SimpleWrapperVisitor implements WrapperVisitor {
    private void acceptAll(List<? extends Wrapper> list) {
        for (Wrapper w : list) {
            accept(w);
        }
    }
    
    protected void accept(Wrapper w) {
        w.accept(this);
    }

    public void visit(YieldReturnWrapper yieldReturnWrapper) {
        acceptAll(yieldReturnWrapper.getArguments());
    }

    public void visit(YieldBreakWrapper yieldBreakWrapper) {
        acceptAll(yieldBreakWrapper.getArguments());
    }

    public void visit(WhileWrapper whileWrapper) {
        accept(whileWrapper.getCondition());
        accept(whileWrapper.getStatement());
    }

    public void visit(TryWrapper tryWrapper) {
        acceptAll(tryWrapper.getResources());
        accept(tryWrapper.getBlock());
        acceptAll(tryWrapper.getCatchers());
        FinallyWrapper finalizer = tryWrapper.getFinalizer();
        if (finalizer != null) {
            accept(finalizer);
        }
    }

    public void visit(ThrowWrapper throwWrapper) {
        accept(throwWrapper.getExpression());
    }

    public void visit(SwitchWrapper switchWrapper) {
        accept(switchWrapper.getExpression());
        for (CaseWrapper cw : switchWrapper.getCases()) {
            acceptAll(cw.getStatements());
        }
    }

    public void visit(ReturnWrapper returnWrapper) {
        accept(returnWrapper.getExpression());
    }

    public void visit(LabeledWrapper labeledWrapper) {
        accept(labeledWrapper.getStatement());
    }

    public void visit(IfWrapper ifWrapper) {
        accept(ifWrapper.getCondition());
        accept(ifWrapper.getThenStatement());
        StatementWrapper elseStmt = ifWrapper.getElseStatement();
        if (elseStmt != null) {
            accept(elseStmt);
        }
    }

    public void visit(ForWrapper forWrapper) {
        accept(forWrapper.getCondition());
        acceptAll(forWrapper.getInitStatements());
        accept(forWrapper.getStatement());
        acceptAll(forWrapper.getUpdateStatements());
    }

    public void visit(DoWrapper doWrapper) {
        accept(doWrapper.getCondition());
        accept(doWrapper.getStatement());
    }

    public void visit(DeclareVariableWrapper declareVariableWrapper) {
        accept(declareVariableWrapper.getVariableType());
        ExpressionWrapper init = declareVariableWrapper.getInit();
        if (init != null) {
            accept(init);
        }
    }

    public void visit(ContinueWrapper continueWrapper) {
    }

    public void visit(BreakWrapper breakWrapper) {
    }

    public void visit(BlockWrapper blockWrapper) {
        acceptAll(blockWrapper.getStatements());
    }

    public void visit(ExpressionStatementWrapper expressionStatementWrapper) {
        accept(expressionStatementWrapper.getExpression());
    }

    public void visit(AnyStatementWrapper anyStatementWrapper) {
    }

    @Override
    public void visit(ValueWrapper valueWrapper) {
    }

    @Override
    public void visit(TypeWrapper typeWrapper) {
    }

    @Override
    public void visit(GenericTypeWrapper typeWrapper) {
        accept(typeWrapper.getOuter());
        acceptAll(typeWrapper.getTypeParameters());
    }

    @Override
    public void visit(NewInstanceWrapper newInstanceWrapper) {
        acceptAll(newInstanceWrapper.getTypeArguments());
        acceptAll(newInstanceWrapper.getArguments());
    }

    @Override
    public void visit(NameWrapper nameWrapper) {
    }

    @Override
    public void visit(EqualsWrapper equalsWrapper) {
        accept(equalsWrapper.getLeft());
        accept(equalsWrapper.getRight());
    }

    @Override
    public void visit(CallMethodWrapper callMethodWrapper) {
        accept(callMethodWrapper.getMethodSelect());
        acceptAll(callMethodWrapper.getArguments());
    }

    @Override
    public void visit(AssignWrapper assignWrapper) {
        accept(assignWrapper.getLeft());
        accept(assignWrapper.getRight());
    }

    @Override
    public void visit(CaseWrapper caseWrapper) {
        accept(caseWrapper.getExpression());
        acceptAll(caseWrapper.getStatements());
    }

    @Override
    public void visit(MethodWrapper methodWrapper) {
        accept(methodWrapper.getReturnType());
        acceptAll(methodWrapper.getParameters());
        accept(methodWrapper.getBody());
    }

    @Override
    public void visit(ClassWrapper classWrapper) {
        acceptAll(classWrapper.getMembers());
    }

    @Override
    public void visit(CatchWrapper catchWrapper) {
        accept(catchWrapper.getParameter());
        accept(catchWrapper.getBlock());
    }

    @Override
    public void visit(FinallyWrapper finallyWrapper) {
        acceptAll(finallyWrapper.getStatements());
    }

    @Override
    public void visit(AnyExpressionWrapper anyExpressionWrapper) {
    }

    @Override
    public void visit(IsNotNullWrapper isNotNullWrapper) {
        accept(isNotNullWrapper.getExpression());
    }

    @Override
    public void visit(ForEachWrapper forEachWrapper) {
        accept(forEachWrapper.getVariable());
        accept(forEachWrapper.getExpression());
        accept(forEachWrapper.getStatement());
    }

    @Override
    public void visit(NoopWrapper noopWrapper) {
    }

    @Override
    public void visit(IrrelevantWrapper irrelevantWrapper) {
    }

    @Override
    public void visit(SynchronizedWrapper synchronizedWrapper) {
        accept(synchronizedWrapper.getExpression());
        accept(synchronizedWrapper.getBlock());
    }
}
