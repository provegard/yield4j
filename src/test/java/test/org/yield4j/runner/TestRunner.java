package test.org.yield4j.runner;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import test.util.AbstractAnnotationProcessorTest;

import org.yield4j.YieldProcessor;
import org.yield4j.debug.DebugUtil;

public class TestRunner extends AbstractAnnotationProcessorTest {

    private static final String EXPECTED_ERROR_PREFIX = "//!!";
    private static final String EXPECTED_RESULT_PREFIX = "//>>";
    private static final String JAVA_VERSION_PREFIX = "//@@";
    
    private String resource;
    private ExpectedErrorOrWarning expectedError;
    private String javaVersionRegex;
    private Map<String, ExpectedEnumeration> expectedEnums = new HashMap<String, ExpectedEnumeration>();
    private File outputPath;

    public TestRunner(String resource) throws IOException {
        if ("Default test name".equals(resource)) {
            resource = getFailedTestResource();
            if (resource == null)
                throw new IOException(
                        "Failed to find the test resource to use!");
            // Enable debug
            System.setProperty(DebugUtil.DEBUG_PROP, "c:\\temp");
        }

        this.resource = resource;
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        outputPath = new File(tempDir, UUID.randomUUID().toString());
        outputPath.mkdirs();
        readExpectations();
    }

    private String getFailedTestResource() {
        String failedTest = System.getProperty("failedTests");
        if (failedTest == null)
            return null;
        int i = failedTest.indexOf(":");
        if (i < 0)
            return null;
        return failedTest.substring(i + 1).trim();
    }

    private void readExpectations() throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        try {
            ExpectedEnumeration lastExpectedEnum = null;
            String line;
            while ((line = reader.readLine()) != null) {
                String ltrim = line.trim();
                if (ltrim.startsWith(EXPECTED_ERROR_PREFIX)) {
                    assert expectedError == null : "At most one expected error per test case is allowed.";
                    expectedError = new ExpectedErrorOrWarning(ltrim);
                } else if (ltrim.startsWith(EXPECTED_RESULT_PREFIX)) {
                    lastExpectedEnum = new ExpectedEnumeration(ltrim);
                } else if (ltrim.startsWith(JAVA_VERSION_PREFIX)) {
                    javaVersionRegex = ltrim.substring(JAVA_VERSION_PREFIX.length()).trim();
                } else if (ltrim.matches(".*\\bIterable(<.*>)?\\b.*") && lastExpectedEnum != null) {
                    expectedEnums.put(getMethodName(ltrim), lastExpectedEnum);
                    lastExpectedEnum = null; // reset
                } else if (ltrim.matches(".*\\bclass\\b.*") && lastExpectedEnum != null) {                    
                    expectedEnums.put(".*", lastExpectedEnum);
                    lastExpectedEnum = null; // reset
                }
            }
        } finally {
            reader.close();
        }
    }

    private String getMethodName(String line) {
        StringTokenizer tok = new StringTokenizer(line);
        boolean pick = false;
        while (tok.hasMoreElements()) {
            String token = tok.nextToken();
            if (pick) {
                return token;
            }
            if (token.startsWith("Iterable")) {
                // Pick the token after this one!
                pick = true;
            }
        }
        throw new AssertionError("No method name found in: " + line);
    }

    @Test//(timeOut = 1000)
    public void test() throws Exception {
        if (javaVersionRegex != null) {
            Pattern pat = Pattern.compile(javaVersionRegex);
            String ver = System.getProperty("java.version");
            Matcher matcher = pat.matcher(ver);
            if (!matcher.lookingAt()) {
                throw new SkipException("This test does not apply to Java version " + ver);
            }
        }
        
        List<Diagnostic<? extends JavaFileObject>> diag = compileTestCase(resource);
        printCompilationOutput(diag);
        if (expectedError != null) {
            expectedError.verify(diag);
        } else {
            assertCompilationSuccessful(diag);
            assert !expectedEnums.isEmpty() : "Expectation(s) missing!";
            for (Map.Entry<String, ExpectedEnumeration> entry : expectedEnums.entrySet()) {
              Iterable<?> it = enumerateValues(entry.getKey());
              entry.getValue().verify(it, entry.getKey());                    
            }
        }
    }

    private void printCompilationOutput(
            List<Diagnostic<? extends JavaFileObject>> diag) {
        for (Diagnostic<? extends JavaFileObject> d : diag) {
            String msg = d.getMessage(null);
            long lineNo = d.getLineNumber();
            StringBuilder sb = new StringBuilder("Message");
            if (lineNo >= 0) {
                sb.append(" @ line ").append(lineNo);
            }
            sb.append(": ");
            sb.append(msg);
            Reporter.log(sb.toString());
        }
    }

    private Iterable<?> enumerateValues(String methodNameRegex) throws Exception {
        String className = resource.replace(SOURCE_FILE_SUFFIX, "").replace(
                '/', '.');
        Class<?> clazz = loadCompiledClass(className);

        Method[] methods = clazz.getDeclaredMethods();
        List<Method> candidates = new ArrayList<Method>();
        for (Method m : methods) {
            if (isPublic(m.getModifiers()) && m.getName().matches(methodNameRegex))
                candidates.add(m);
        }

        assert candidates.size() == 1 : "Expected single public method in class "
                + className + " matching name " + methodNameRegex;
        Method method = candidates.get(0);
        assert method.getReturnType() == Iterable.class : "Expected method to return an Iterable";
        assert method.getParameterTypes().length == 0 : "Expected method to take no parameters.";

        Object o = clazz.newInstance();
        Iterable<?> it = (Iterable<?>) method.invoke(o);
        return it;
    }

    @AfterMethod(alwaysRun = true)
    public void removeClassDirectory(Method m, ITestContext ctx) {
        deleteTree(getClassOutputPath());
    }

    private void deleteTree(File f) {
        if (!f.isFile()) {
            for (File sf : f.listFiles()) {
                deleteTree(sf);
            }
        }
        f.delete();
    }

    @Override
    public String toString() {
        return "Sample: " + resource;
    }

    @Override
    protected Collection<? extends Processor> getProcessors() {
        return asList(new YieldProcessor());
    }

    private class ExpectedErrorOrWarning {

        private final Kind expectedKind;
        private final int lineNo;
        private final String message;

        public ExpectedErrorOrWarning(String line) {
            int errIdx = line.indexOf("ERROR");
            int warnIdx = line.indexOf("WARNING");
            assert errIdx >= 0 || warnIdx >= 0 : "Missing ERROR token on expected error line: "
                    + line;
            expectedKind = errIdx >= 0 ? Kind.ERROR : Kind.WARNING;
            int lparen = line.indexOf('(', errIdx + 5);
            assert lparen >= 0 : "Missing line number info on expected error line: "
                    + line;
            int rparen = line.indexOf(')', lparen + 1);
            assert rparen >= 0 : "Missing line number info on expected error line: "
                    + line;
            lineNo = Integer.parseInt(line.substring(lparen + 1, rparen));
            message = line.substring(rparen + 1).trim();
        }

        void verify(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
            assertCompilationReturned(expectedKind, lineNo, message, diagnostics);
        }

    }

    private class ExpectedEnumeration {
        private String[] values;
        private int count;

        ExpectedEnumeration(String line) {
            int lb = line.indexOf('[');
            assert lb >= 0 : "Malformed enumeration expectation: " + line;
            int rb = line.indexOf(']', lb + 1);
            assert rb >= 0 : "Malformed enumeration expectation: " + line;
            String valueString = line.substring(lb + 1, rb).trim();
            if (valueString.isEmpty()) {
                values = new String[0];
            } else {
                values = valueString.split("\\s*,\\s*", 0);
            }
            if (line.charAt(lb - 1) == ':') {
                int space = line.lastIndexOf(' ', lb - 2);
                count = Integer.parseInt(line.substring(space + 1, lb - 1));
            } else {
                count = values.length;
            }
        }

        void verify(Iterable<?> i, String methodNameRegex) {
            assert i != null : "Iterator must not be null.";
            Collection<String> actual = new ArrayList<String>();
            int n = count;
            for (Object x : i) {
                actual.add(String.valueOf(x));
                if (--n <= 0) {
                    break;
                }
            }
            assertThat(actual).as("Enumeration failed for method matching: " + methodNameRegex).isEqualTo(asList(values));
        }
    }

    @Override
    protected File getClassOutputPath() {
        return outputPath;
    }
}
