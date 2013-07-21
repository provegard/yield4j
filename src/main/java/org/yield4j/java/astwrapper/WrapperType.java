package org.yield4j.java.astwrapper;


public enum WrapperType {
    BLOCK, BREAK, CALLMETHOD, CASE, CATCH, CONTINUE, DO, FINALLY, FOR, IF, LABELLED, RETURN, SWITCH, THROW, TRY, WHILE, OTHER, YIELDRETURN, YIELDBREAK, METHOD, VARIABLE, CLASS;

    /*static WrapperType fromKind(Kind k) {
        switch (k) {
        case BLOCK:
            return WrapperType.BLOCK;
        case BREAK:
            return WrapperType.BREAK;
        case CASE:
            return WrapperType.CASE;
        case CATCH:
            return WrapperType.CATCH;
        case CONTINUE:
            return WrapperType.CONTINUE;
        case DO_WHILE_LOOP:
            return WrapperType.DO;
        case FOR_LOOP:
            return WrapperType.FOR;
        case IF:
            return WrapperType.IF;
        case LABELED_STATEMENT:
            return WrapperType.LABELLED;
        case RETURN:
            return WrapperType.RETURN;
        case SWITCH:
            return WrapperType.SWITCH;
        case THROW:
            return WrapperType.THROW;
        case TRY:
            return WrapperType.TRY;
        case METHOD:
            return WrapperType.METHOD;
        case VARIABLE:
            return WrapperType.VARIABLE;
        default:
            return OTHER;
        }
    }*/
}
