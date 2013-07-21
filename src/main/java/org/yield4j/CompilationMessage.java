package org.yield4j;

import org.yield4j.java.astwrapper.Wrapper;

public class CompilationMessage {
    private final Wrapper wrapper;
    private final String message;
    private final MessageType type;
    
    public CompilationMessage(Wrapper wrapper, String message, MessageType type) {
        this.wrapper = wrapper;
        this.message = message;
        this.type = type;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public String getMessage() {
        return message;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public enum MessageType {
        WARNING,
        ERROR;
    }
}