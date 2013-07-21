package org.yield4j.java.astwrapper;


public interface WrapperVisitor {
    void visit(YieldReturnWrapper yieldReturnWrapper);

    void visit(YieldBreakWrapper yieldBreakWrapper);

    void visit(WhileWrapper whileWrapper);

    void visit(TryWrapper tryWrapper);

    void visit(ThrowWrapper throwWrapper);

    void visit(SwitchWrapper switchWrapper);

    void visit(ReturnWrapper returnWrapper);

    void visit(LabeledWrapper labeledWrapper);

    void visit(IfWrapper ifWrapper);

    void visit(ForWrapper forWrapper);

    void visit(DoWrapper doWrapper);

    void visit(DeclareVariableWrapper declareVariableWrapper);

    void visit(ContinueWrapper continueWrapper);

    void visit(BreakWrapper breakWrapper);

    void visit(BlockWrapper blockWrapper);

    void visit(FinallyWrapper finallyWrapper);

    void visit(ExpressionStatementWrapper expressionStatementWrapper);

    void visit(AnyStatementWrapper anyStatementWrapper);
    
    void visit(ValueWrapper valueWrapper);

    void visit(TypeWrapper typeWrapper);

    void visit(GenericTypeWrapper typeWrapper);

    void visit(NewInstanceWrapper newInstanceWrapper);

    void visit(NameWrapper nameWrapper);

    void visit(EqualsWrapper equalsWrapper);

    void visit(CallMethodWrapper callMethodWrapper);

    void visit(AssignWrapper assignWrapper);

    void visit(CaseWrapper caseWrapper);

    void visit(MethodWrapper methodWrapper);

    void visit(ClassWrapper classWrapper);

    void visit(CatchWrapper catchWrapper);

    void visit(AnyExpressionWrapper anyExpressionWrapper);

    void visit(IsNotNullWrapper isNotNullWrapper);

    void visit(ForEachWrapper forEachWrapper);

    void visit(NoopWrapper noopWrapper);

    void visit(IrrelevantWrapper irrelevantWrapper);

    void visit(SynchronizedWrapper synchronizedWrapper);
}
