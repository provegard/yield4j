package org.yield4j.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class IOUtil {

    public static void writeToFile(File f, String data, boolean append) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, append);
            fw.write(data);
        } catch (IOException ignore) {
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ignore2) {
                }
            }
        }
    }
}
