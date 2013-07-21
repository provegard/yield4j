package org.yield4j.java;

import java.util.List;

import javax.lang.model.element.Element;

import org.yield4j.java.astwrapper.ClassWrapper;

public interface AstBuilder {

    List<ClassWrapper> generateAstWithClassRoots(Element startingPoint);
}
