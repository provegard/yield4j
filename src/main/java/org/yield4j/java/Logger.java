package org.yield4j.java;

import javax.lang.model.element.Element;

import org.yield4j.CompilationMessage;
import org.yield4j.java.astwrapper.Wrapper;

public interface Logger {
    void printMessage(Element e, CompilationMessage msg);

    void printNote(Element e, Wrapper wrapper, String note);

    void printNote(String note);
}
