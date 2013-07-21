package org.yield4j;

import static org.yield4j.debug.DebugUtil.logMessage;
import static org.yield4j.debug.DebugUtil.shouldWriteDebugOutput;
import static org.yield4j.debug.DebugUtil.writeDebug;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.yield4j.CompilationMessage.MessageType;
import org.yield4j.java.CodeGenerator;
import org.yield4j.java.GeneratorEnvironment;
import org.yield4j.java.IdentifierScanner;
import org.yield4j.java.Logger;
import org.yield4j.java.NameFinder;
import org.yield4j.java.Scoper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.javac.JavacGeneratorEnvironment;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;

@SupportedAnnotationTypes("org.yield4j.Generator")
public class YieldProcessor extends AbstractProcessor {

    private GeneratorEnvironment generatorEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        if (processingEnv instanceof JavacProcessingEnvironment) {
            generatorEnv = new JavacGeneratorEnvironment(
                    (JavacProcessingEnvironment) processingEnv);
        } else {
            throw new UnsupportedOperationException(
                    "Unknown processing environment: "
                            + processingEnv.getClass().getName());
        }

        if (shouldWriteDebugOutput()) {
            logMessage("---------------------------------------------------");
            logMessage("yield4j initialization complete!");
        }

        super.init(processingEnv);
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver())
            return false;

        Set<? extends Element> rootElements = roundEnv.getRootElements();
        
        long beforeAll = System.currentTimeMillis();
        int count = 0;
        Logger logger = generatorEnv.getLogger();
        IdentifierScanner identScanner = generatorEnv.getIdentifierScanner();
        for (Element e : rootElements) {
            if (e.getKind() != ElementKind.CLASS) {
                continue;
            }

            List<ClassWrapper> classes = generatorEnv.getAstBuilder()
                    .generateAstWithClassRoots(e);

            if (shouldWriteDebugOutput()) {
                logMessage("Processing class '" + e.getSimpleName() + "' with "
                        + classes.size() + " root classes.");
            }

            for (int k = 0; k < classes.size(); k++) {
                ClassWrapper cw = classes.get(k);
                if (shouldWriteDebugOutput()) {
                    String name = cw.getName();
                    logMessage("-> Root class: "
                            + ("".equals(name) ? "(anonymous)" : name));
                }

                List<GeneratorMethod> methods = createGeneratorMethods(cw
                        .getGeneratorMethods());
                int errors = 0;

                // 1. Preprocess all methods
                NameFinder nameFinder = generatorEnv.getNameFinder();
                for (GeneratorMethod gm : methods) {

                    if (shouldWriteDebugOutput()) {
                        logMessage("--> Generator method: "
                                + gm.getMethod().getName());
                    }

                    gm.preprocess(nameFinder);
                }

                // 2. Fix scoping for the entire class
                Scoper scoper = new Scoper();
                scoper.enter(cw);

                // 3. Transform all methods
                for (GeneratorMethod gm : methods) {
                    try {
                        long before = System.currentTimeMillis();
                        gm.transform(identScanner);
                        long elapsed = System.currentTimeMillis() - before;
                        // TODO: getSignature would be better than name!
                        logger.printNote(
                                e,
                                gm.getMethod(),
                                String.format(
                                        "Transformed generator method '%s' in %d milliseconds.",
                                        gm.getMethod().getName(), elapsed));
                        count++;
                    } catch (CancelException ignored) {
                    }

                    for (CompilationMessage msg : gm.getContext().getMessages()) {
                        logger.printMessage(e, msg);
                        if (msg.getType() == MessageType.ERROR) {
                            errors++;
                        }
                    }
                }

                if (errors == 0 || shouldWriteDebugOutput()) {
                    // Generate code for the entire class once the methods have
                    // been transformed.
                    CodeGenerator gen = generatorEnv.getCodeGenerator();
                    gen.generateInPlace(cw);
                    if (shouldWriteDebugOutput()) {
                        writeDebug("gensource-" + k + ".java", gen.getResult()
                                .toString());
                    }
                }
            }
        }
        long elapsedAll = System.currentTimeMillis() - beforeAll;
        logger.printNote(String.format(
                "Transformed %d generator method(s) in %d milliseconds.",
                count, elapsedAll));

        return false;
    }

    private List<GeneratorMethod> createGeneratorMethods(
            List<MethodWrapper> generatorMethods) {
        List<GeneratorMethod> gms = new ArrayList<GeneratorMethod>();
        for (int i = 0; i < generatorMethods.size(); i++) {
            gms.add(new GeneratorMethod(generatorMethods.get(i), i));
        }
        return gms;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
