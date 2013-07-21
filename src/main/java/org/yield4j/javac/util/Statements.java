package org.yield4j.javac.util;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.StatementTree;

public class Statements {
    private static final String YIELD_BREAK = "yield_break";
    private static final String YIELD_RETURN = "yield_return";

    public static boolean isYieldMethodCall(MethodInvocationTree mi) {
        return isYieldReturn(mi) || isYieldBreak(mi);
    }

    public static boolean isYieldReturn(MethodInvocationTree mi) {
        return YIELD_RETURN.equals(getMethodName(mi));
    }

    public static boolean isYieldBreak(MethodInvocationTree mi) {
        return YIELD_BREAK.equals(getMethodName(mi));
    }

    private static String getMethodName(MethodInvocationTree mi) {
        return mi != null ? mi.getMethodSelect().toString() : null;
    }

    public static MethodInvocationTree getMethodInvocation(StatementTree s) {
        if (s instanceof ExpressionStatementTree == false)
            return null;
        ExpressionStatementTree es = (ExpressionStatementTree) s;
        ExpressionTree ex = es.getExpression();
        if (ex instanceof MethodInvocationTree == false)
            return null;
        return (MethodInvocationTree) ex;
    }
}
