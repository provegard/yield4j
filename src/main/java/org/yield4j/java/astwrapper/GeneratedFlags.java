package org.yield4j.java.astwrapper;

public class GeneratedFlags implements Flags {

    private boolean isPublic;
    private boolean isFinal;
    private boolean isParameter;
    private boolean isSynchronized;
    
    public static GeneratedFlags emptyFlags() {
        return new GeneratedFlags();
    }
    
    @Override
    public long getFlags() {
        return 0;
    }

    @Override
    public boolean hasFlags() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public boolean isParameter() {
        return isParameter;
    }

    @Override
    public boolean isSynchronized() {
        return isSynchronized;
    }
    
    public GeneratedFlags addPublic() {
        isPublic = true;
        return this;
    }

    public GeneratedFlags addFinal() {
        isFinal = true;
        return this;
    }

    public GeneratedFlags addParameter() {
        isParameter = true;
        return this;
    }

    public GeneratedFlags addSynchronized() {
        isSynchronized = true;
        return this;
    }
}
