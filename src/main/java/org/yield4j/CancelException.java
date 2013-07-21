package org.yield4j;

public class CancelException extends RuntimeException {
    private static final long serialVersionUID = 2792345567674318783L;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
