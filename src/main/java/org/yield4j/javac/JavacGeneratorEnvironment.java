package org.yield4j.javac;

import org.yield4j.java.AstBuilder;
import org.yield4j.java.CodeGenerator;
import org.yield4j.java.GeneratorEnvironment;
import org.yield4j.java.IdentifierScanner;
import org.yield4j.java.Logger;
import org.yield4j.java.NameFinder;
import org.yield4j.javac.util.TreeBuilder;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;

public class JavacGeneratorEnvironment implements GeneratorEnvironment {

    private final JavacProcessingEnvironment env;
    private final TreeBuilder builder;

    public JavacGeneratorEnvironment(JavacProcessingEnvironment processingEnv) {
        env = processingEnv;
        builder = TreeBuilder.create(processingEnv);
    }

    @Override
    public AstBuilder getAstBuilder() {
        return new JavaTreeBuilderNew(env, builder);
    }

    @Override
    public Logger getLogger() {
        return new JavacLogger(env);
    }

    @Override
    public CodeGenerator getCodeGenerator() {
        return new JavacCodeGenerator(builder);
    }

    @Override
    public NameFinder getNameFinder() {
        return new JavacNameFinder();
    }

    @Override
    public IdentifierScanner getIdentifierScanner() {
        return new JavacIdentScanner();
    }

}
