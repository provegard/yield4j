/*
 * @(#)AbstractAnnotationProcessorTest.java     5 Jun 2009
 */
package test.util;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * A base test class for {@link Processor annotation processor} testing that
 * attempts to compile source test cases that can be found on the classpath.
 * 
 * @author aphillips
 * @since 5 Jun 2009
 * 
 */
// TODO: Go through this file, clean up, adjust comments.
public abstract class AbstractAnnotationProcessorTest {
    protected final String SOURCE_FILE_SUFFIX = ".java";
    private final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    /**
     * @return the processor instances that should be tested
     */
    protected abstract Collection<? extends Processor> getProcessors();

    /**
     * Attempts to compile the given compilation units using the Java Compiler
     * API.
     * <p>
     * The compilation units and all their dependencies are expected to be on
     * the classpath.
     * 
     * @param compilationUnits
     *            the classes to compile
     * @return the {@link Diagnostic diagnostics} returned by the compilation,
     *         as demonstrated in the documentation for {@link JavaCompiler}
     * @see #compileTestCase(String...)
     */
    protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(
            Class<?>... compilationUnits) {
        assert (compilationUnits != null);

        String[] compilationUnitPaths = new String[compilationUnits.length];

        for (int i = 0; i < compilationUnitPaths.length; i++) {
            assert (compilationUnits[i] != null);
            compilationUnitPaths[i] = toResourcePath(compilationUnits[i]);
        }

        return compileTestCase(compilationUnitPaths);
    }

    private String toResourcePath(Class<?> clazz) {
        return clazz.getName().replace('.', '/') + SOURCE_FILE_SUFFIX;
    }

    /**
     * Attempts to compile the given compilation units using the Java Compiler
     * API.
     * <p>
     * The compilation units and all their dependencies are expected to be on
     * the classpath.
     * 
     * @param compilationUnitPaths
     *            the paths of the source files to compile, as would be expected
     *            by {@link ClassLoader#getResource(String)}
     * @return the {@link Diagnostic diagnostics} returned by the compilation,
     *         as demonstrated in the documentation for {@link JavaCompiler}
     * @see #compileTestCase(Class...)
     * 
     */
    protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(
            String... compilationUnitPaths) {
        final Collection<File> compilationUnits = toCompilationUnits(compilationUnitPaths);
        final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        final StandardJavaFileManager fileManager = COMPILER
                .getStandardFileManager(diagnosticCollector, null,
                        Charset.forName("UTF-8"));
        try {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                    asList(getClassOutputPath()));
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }

        /*
         * Call the compiler with the "-proc:only" option. The "class names"
         * option (which could, in principle, be used instead of compilation
         * units for annotation processing) isn't useful in this case because
         * only annotations on the classes being compiled are accessible.
         * 
         * Information about the classes being compiled (such as what they are
         * annotated with) is *not* available via the RoundEnvironment. However,
         * if these classes are annotations, they certainly need to be
         * validated.
         */
        CompilationTask task = COMPILER.getTask(new PrintWriter(System.out),
                fileManager, diagnosticCollector,
                Collections.<String> emptyList(), null,
                fileManager.getJavaFileObjectsFromFiles(compilationUnits));
        task.setProcessors(getProcessors());
        task.call();

        try {
            fileManager.close();
        } catch (IOException exception) {
        }

        return diagnosticCollector.getDiagnostics();
    }

    protected abstract File getClassOutputPath();

    protected Class<?> loadCompiledClass(String name)
            throws MalformedURLException, ClassNotFoundException {
        URL u = getClassOutputPath().toURI().toURL();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        cl = new URLClassLoader(new URL[] { u }, cl);
        return cl.loadClass(name);
    }

    protected Collection<File> toCompilationUnits(
            String... compilationUnitPaths) {
        assert (compilationUnitPaths != null);

        Collection<File> compilationUnits;

        try {
            compilationUnits = findClasspathFiles(compilationUnitPaths);
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "Unable to resolve compilation units "
                            + Arrays.toString(compilationUnitPaths)
                            + " due to: " + exception.getMessage(), exception);
        }
        return compilationUnits;
    }

    protected List<File> getSourceOutputPaths() {
        return Collections.singletonList(new File("").getAbsoluteFile()
                .getParentFile());
    }

    private Collection<File> findClasspathFiles(String[] filenames)
            throws IOException {
        final Collection<File> classpathFiles = new ArrayList<File>(
                filenames.length);
        for (final String filename : filenames) {
            final File file = getFile(filename);
            assertTrue(file.exists() && file.canRead() && file.isFile(),
                    file.getAbsolutePath());
            classpathFiles.add(file);
        }

        return classpathFiles;
    }

    protected File getFile(String filename) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            return new File(cl.getResource(filename).toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    /**
     * Asserts that the compilation produced no errors, i.e. no diagnostics of
     * type {@link Kind#ERROR}.
     * 
     * @param diagnostics
     *            the result of the compilation
     * @see #assertCompilationReturned(Kind, long, List)
     * @see #assertCompilationReturned(Kind[], long[], List)
     */
    protected void assertCompilationSuccessful(
            List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        assert (diagnostics != null);
        final Diagnostics failures = new Diagnostics();

        for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            if (diagnostic.getKind().equals(Kind.ERROR)) {
                failures.add(diagnostic);
            }
        }
        if (!failures.isEmpty()) {
            fail(failures.getMessages(Locale.getDefault()));
        }
    }

    protected void assertCompilationReturned(Kind expectedDiagnosticKind,
            long expectedLineNumber, String message,
            List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        assert ((expectedDiagnosticKind != null) && (diagnostics != null));
        boolean expectedDiagnosticFound = false;

        StringBuilder sb = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {

            sb.append(
                    String.format("%s (%d): %s", diagnostic.getKind(),
                            diagnostic.getLineNumber(),
                            diagnostic.getMessage(null))).append("\n");

            String actualMessage = diagnostic.getMessage(null);
            // At least in Java 6: Remove the file and line number info from
            // the message.
            String delim = ":" + diagnostic.getLineNumber() + ":";
            int idx = actualMessage.indexOf(delim);
            if (idx >= 0) {
                actualMessage = actualMessage.substring(idx + delim.length())
                        .trim();
            }

            if (diagnostic.getKind().equals(expectedDiagnosticKind)
                    && (diagnostic.getLineNumber() == expectedLineNumber && actualMessage
                            .equals(message))) {
                expectedDiagnosticFound = true;
            }
        }

        assertTrue(expectedDiagnosticFound, "Expected a result of kind "
                + expectedDiagnosticKind + " at line " + expectedLineNumber
                + " with message " + message + ", but got:\n\n" + sb.toString());
    }
}
