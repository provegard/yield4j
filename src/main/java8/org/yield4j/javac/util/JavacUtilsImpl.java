package org.yield4j.javac.util;

import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

public class JavacUtilsImpl extends Ge7JavacUtils {

    private final TreeMaker maker;

    public JavacUtilsImpl(Context context) {
        super(context);
        maker = TreeMaker.instance(context);
    }
    
    @Override
    public JCExpression equalsExpression(JCExpression left, JCExpression right) {
        return maker.Binary(JCTree.Tag.EQ, left, right);
    }

    @Override
    public JCExpression isNotNullExpression(JCExpression e) {
        return maker.Binary(JCTree.Tag.NE, e, maker.Literal(getCtcInt(TypeTags.class, "BOT"), null));
    }
}
