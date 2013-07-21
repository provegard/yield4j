package org.yield4j.java;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.ContinueWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.FinallyWrapper;
import org.yield4j.java.astwrapper.ReturnWrapper;
import org.yield4j.java.astwrapper.SimpleWrapperVisitor;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.SynchronizedWrapper;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.WrapperType;
import org.yield4j.java.astwrapper.YieldBreakWrapper;
import org.yield4j.java.astwrapper.YieldReturnWrapper;

public class JavaTreeVerifier extends SimpleWrapperVisitor {

    private boolean isInTryWithFinally;
    private int finallyCount;
    private int catchCount;
    private int synchronizedCount;
    private final StatementWrapper wrapper;
    private GeneratorMethodContext context;
    
    public JavaTreeVerifier(StatementWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void verify(GeneratorMethodContext context) {
        this.context = context;
        finallyCount = 0;
        wrapper.accept(this);
    }

    @Override
    public void visit(BreakWrapper s) {
        checkStatementAfterBreak(s);
        super.visit(s);
    }
    
    @Override
    public void visit(ContinueWrapper s) {
        checkStatementAfterBreak(s);
        super.visit(s);
    }

    @Override
    public void visit(ReturnWrapper s) {
        String msg = "Cannot return a value from a generator. Use yield_return to return a value, or yield_break to end the generation.";
        context.addError(s, msg);
    }

    @Override
    public void visit(YieldBreakWrapper s) {
        if (s.getArguments().size() > 0) {
            String msg = "yield_break takes no arguments";
            context.addError(s, msg);
        }
        checkStatementAfterBreak(s);
        warnOnYieldInTryWithFinally(s);
        preventYieldInFinally(s, "yield_break");
        preventYieldInSynchronized(s, "yield_break");
        super.visit(s);
    }

    private void checkStatementAfterBreak(StatementWrapper s) {
        for (StatementWrapper next : s.after()) {
            String msg = "unreachable code";
            context.addError(next, msg);
            break;
        }
    }

    @Override
    public void visit(YieldReturnWrapper s) {        
        if (s.getArguments().size() != 1) {
            String msg = "yield_return takes exactly one argument";
            context.addError(s, msg);     
        }
        warnOnYieldInTryWithFinally(s);
        preventYieldInFinally(s, "yield_return");
        preventYieldInSynchronized(s, "yield_return");
        preventYieldReturnInCatch(s);
        super.visit(s);
    }
    
    @Override
    public void visit(SynchronizedWrapper synchronizedWrapper) {
        synchronizedCount++;
        super.visit(synchronizedWrapper);
        synchronizedCount--;
    }

    private void warnOnYieldInTryWithFinally(StatementWrapper s) {
        if (isInTryWithFinally) {
            String msg = "Yield in try with finalizer is dangerous; there is no guarantee that the finalizer will be executed.";
            context.addWarning(s, msg);
        }
    }

    private void preventYieldReturnInCatch(YieldReturnWrapper s) {
        if (catchCount > 0) {
            String msg = "yield_return cannot appear in a catch block";
            context.addError(s, msg);
        }        
    }

    private void preventYieldInFinally(StatementWrapper s, String type) {
        if (finallyCount > 0) {
            String msg = type + " cannot appear in a finally block";
            context.addError(s, msg);
        }        
    }
    
    private void preventYieldInSynchronized(StatementWrapper s, String type) {
        if (synchronizedCount > 0) {
            String msg = type + " cannot appear in a synchronized block";
            context.addError(s, msg);
        }   
    }

    @Override
    public void visit(BlockWrapper blockWrapper) {
        Wrapper parent = blockWrapper.getParent();
        boolean isBodyOfTryWithFinally = parent.getType() == WrapperType.TRY
                && ((TryWrapper) parent).getFinalizer() != null;
        boolean oldFlag = isInTryWithFinally;
        isInTryWithFinally |= isBodyOfTryWithFinally;
        try {
            super.visit(blockWrapper);
        } finally {
            isInTryWithFinally = oldFlag;
        }
    }

    @Override
    public void visit(CatchWrapper catchWrapper) {
        catchCount++;
        super.visit(catchWrapper);
        catchCount--;
    }

    @Override
    public void visit(FinallyWrapper finallyWrapper) {
        finallyCount++;
        super.visit(finallyWrapper);
        finallyCount--;
    }
    
    @Override
    public void visit(DeclareVariableWrapper var) {
        if (var.getFlags().isFinal() && var.getInit() == null) {
            String msg = "The final local variable " + var.getName() + " must be initialized at declaration time.";
            context.addError(var, msg);            
        }
        super.visit(var);
    }
}
