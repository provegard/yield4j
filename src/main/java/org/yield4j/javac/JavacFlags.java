package org.yield4j.javac;

import org.yield4j.java.astwrapper.Flags;

public class JavacFlags implements Flags {

    private final long flags;

    public JavacFlags(long flags) {
        this.flags = flags;
    }
    
    @Override
    public long getFlags() {
        return flags;
    }

    @Override
    public boolean hasFlags() {
        return true;
    }

    @Override
    public boolean isPublic() {
        return (flags & com.sun.tools.javac.code.Flags.PUBLIC) != 0;
    }

    @Override
    public boolean isFinal() {
        return (flags & com.sun.tools.javac.code.Flags.FINAL) != 0;
    }

    @Override
    public boolean isParameter() {
        return (flags & com.sun.tools.javac.code.Flags.PARAMETER) != 0;
    }

    @Override
    public boolean isSynchronized() {
        return (flags & com.sun.tools.javac.code.Flags.SYNCHRONIZED) != 0;
    }

}
