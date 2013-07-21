package org.yield4j.debug;

import java.io.File;

import org.yield4j.builder.statement.Statement;

public class DebugUtil {
    private static final String LOG_FILE = "yield4j.log";
    public static final String DEBUG_PROP = "yield4j.debug.dir";
    
    public static boolean shouldWriteDebugOutput() {
        return null != getDebugOutputDirectory();
    }

    public static void writeStatementGraph(String fn, Statement... statements) {
        assert shouldWriteDebugOutput();
        String dot = DotGenerator.createDot(statements);
        File f = new File(getDebugOutputDirectory(), fn);
        IOUtil.writeToFile(f, dot, false);
    }

    public static void writeDebug(String fn, String data) {
        writeDebug(fn, data, false);
    }

    private static void writeDebug(String fn, String data, boolean append) {
        assert shouldWriteDebugOutput();
        File f = new File(getDebugOutputDirectory(), fn);
        IOUtil.writeToFile(f, data, append);
    }

    public static void logMessage(String message) {
        writeDebug(LOG_FILE, message + System.getProperty("line.separator"), true);
    }
    
    public static String getDebugOutputDirectory() {
        return System.getProperty("yield4j.debug.dir");
    }
}
