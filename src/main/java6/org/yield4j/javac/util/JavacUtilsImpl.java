package org.yield4j.javac.util;

import java.util.List;
import static java.util.Collections.emptyList;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;

public class JavacUtilsImpl extends JavacUtils {
    private final TreeMaker maker;
    private final Log log;

    public JavacUtilsImpl(Context context) {
        maker = TreeMaker.instance(context);
        log = Log.instance(context);
    }

    @Override
    public JCExpression equalsExpression(JCExpression left, JCExpression right) {
        return maker.Binary(getCtcInt(JCTree.class, "EQ"), left, right);
    }

    @Override
    public JCExpression isNotNullExpression(JCExpression e) {
        return maker.Binary(getCtcInt(JCTree.class, "NE"), e, createLiteral(null));
    }

    @Override
    public JCLiteral createLiteral(Object value) {
        if (value == null) {
            return maker.Literal(getCtcInt(TypeTags.class, "BOT"), null);
        }
        // Java 6 doesn't support boolean in the Literal(Object) method,
        // so we have to handle it manually!
        if (value instanceof Boolean) {
            // boolean = typetag 8 + Integer
            return maker.Literal(getCtcInt(TypeTags.class, "BOOLEAN"), ((Boolean) value) ? 1 : 0);
        }
        return maker.Literal(value);
    }
    
    @Override
    public List<? extends Tree> getResources(TryTree tree) {
        return emptyList();
    }

    // Copied from OpenJDK 7 code!
    @Override
    public void printMessage(Diagnostic.Kind kind, CharSequence msg, Tree t,
            CompilationUnitTree root) {
        JavaFileObject oldSource = null;
        JavaFileObject newSource = null;
        JCDiagnostic.DiagnosticPosition pos = null;

        newSource = root.getSourceFile();
        if (newSource != null) {
            oldSource = log.useSource(newSource);
            pos = ((JCTree) t).pos();
        }

        try {
            switch (kind) {
            case ERROR:
                boolean prev = log.multipleErrors;
                try {
                    log.error(pos, "proc.messager", msg.toString());
                } finally {
                    log.multipleErrors = prev;
                }
                break;

            case WARNING:
                log.warning(pos, "proc.messager", msg.toString());
                break;

            case MANDATORY_WARNING:
                log.mandatoryWarning(pos, "proc.messager", msg.toString());
                break;

            default:
                log.note(pos, "proc.messager", msg.toString());
            }
        } finally {
            if (oldSource != null)
                log.useSource(oldSource);
        }
    }
}
