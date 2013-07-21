package org.yield4j.javac.util;

import javax.annotation.processing.ProcessingEnvironment;

import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public class TreeBuilder {
    private final TreeMaker maker;
    private final JavacElements elements;
    private final JavacUtils jcutils;
    private final ClassReader reader;
    private final Symtab syms;

    public TreeBuilder(Context context) {
        maker = TreeMaker.instance(context);
        elements = JavacElements.instance(context);
        jcutils = JavacUtils.get(context);
        reader = ClassReader.instance(context);
        syms = Symtab.instance(context);
    }
    
    public JavacUtils getJavacUtils() {
        return jcutils;
    }

    public TreeMaker getTreeMaker() {
        return maker;
    }

    public Name getName(String name) {
        if (name == null)
            return null;
        return elements.getName(name);
    }

    public JCMethodInvocation callMethod(JCExpression method,
            JCExpression... args) {
        return maker.Apply(List.<JCExpression> nil(), method, List.from(args));
    }

    public JCExpression genericType(Class<?> clazz, JCExpression... types) {
        return maker.TypeApply(nameToExpression(clazz.getName()),
                List.from(types));
    }

    public JCExpression nameToExpression(String name) {
        assert name != null;
        Object[] elems = name.split("\\.");
        return nameToExpression(elems);
    }

    public JCExpression nameToExpression(Object... elems) {
        JCExpression e = maker.Ident(toName(elems[0]));
        for (int i = 1; i < elems.length; i++) {
            e = maker.Select(e, toName(elems[i]));
        }

        return e;
    }

    private Name toName(Object object) {
        if (object instanceof Name)
            return (Name) object;
        return getName(object.toString());
    }

    public JCExpression boxPrimitiveType(JCPrimitiveTypeTree primitiveType) {
        Name n = syms.boxedName[primitiveType.typetag];
        return maker.Ident(reader.enterClass(n));
    }

    public static TreeBuilder create(ProcessingEnvironment processingEnv) {
        Context context = ((JavacProcessingEnvironment) processingEnv)
                .getContext();
        return new TreeBuilder(context);
    }
}
