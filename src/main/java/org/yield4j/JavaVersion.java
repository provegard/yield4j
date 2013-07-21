package org.yield4j;

public final class JavaVersion {
    private JavaVersion() {
    }
    
    private static final int major;
    
    static {
        String ver = System.getProperty("java.version");
        String[] parts = ver.split("\\.");
        major = Integer.parseInt(parts[1]);
    }
    
    public static int majorVersion() {
        return major;
    }
}
