package org.yield4j;

public final class YieldSupport {
    
    private static final String CALL_ERROR = "This method should never be called. Is annotation processing turned off?";
    
    private YieldSupport() {
    }
    
    public static void yield_break() {
        throw new AssertionError(CALL_ERROR);
    }
    
    public static <T> void yield_return(T value) {
        throw new AssertionError(CALL_ERROR);
    }

}
