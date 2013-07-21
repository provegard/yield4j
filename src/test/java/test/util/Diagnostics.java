package test.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class Diagnostics implements
        Iterable<Diagnostic<? extends JavaFileObject>> {
    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public Diagnostics() {
        super();
        diagnostics = new ArrayList<Diagnostic<? extends JavaFileObject>>();
    }

    public String getMessages(Locale locale) {
        final StringBuilder s = new StringBuilder();
        s.append(diagnostics.size()).append(" messages").append("\r\n");
        for (final Diagnostic<? extends JavaFileObject> d : this) {
            String sourceName = d.getSource() != null ? d.getSource().getName() : "unknown";
            s.append(sourceName).append("#");
            s.append(d.getLineNumber()).append(": ");
            s.append(d.getMessage(locale)).append("\r\n");
        }
        return s.toString();
    }

    public void add(Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.add(diagnostic);
    }

    public boolean isEmpty() {
        return diagnostics.isEmpty();
    }

    @Override
    public Iterator<Diagnostic<? extends JavaFileObject>> iterator() {
        return diagnostics.iterator();
    }

}