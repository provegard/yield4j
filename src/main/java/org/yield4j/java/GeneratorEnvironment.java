package org.yield4j.java;

public interface GeneratorEnvironment {
    AstBuilder getAstBuilder();

    Logger getLogger();

    CodeGenerator getCodeGenerator();

    NameFinder getNameFinder();

    IdentifierScanner getIdentifierScanner();
}
