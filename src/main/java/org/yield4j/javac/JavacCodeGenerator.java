package org.yield4j.javac;

import java.util.HashSet;
import java.util.Set;

import org.yield4j.java.CodeGenerator;
import org.yield4j.java.astwrapper.AnyExpressionWrapper;
import org.yield4j.java.astwrapper.AnyStatementWrapper;
import org.yield4j.java.astwrapper.AssignWrapper;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.CallMethodWrapper;
import org.yield4j.java.astwrapper.CaseWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.ContinueWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.DoWrapper;
import org.yield4j.java.astwrapper.EqualsWrapper;
import org.yield4j.java.astwrapper.ExpressionStatementWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.FinallyWrapper;
import org.yield4j.java.astwrapper.ForEachWrapper;
import org.yield4j.java.astwrapper.ForWrapper;
import org.yield4j.java.astwrapper.GenericTypeWrapper;
import org.yield4j.java.astwrapper.IfWrapper;
import org.yield4j.java.astwrapper.IrrelevantWrapper;
import org.yield4j.java.astwrapper.IsNotNullWrapper;
import org.yield4j.java.astwrapper.LabeledWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.NameWrapper;
import org.yield4j.java.astwrapper.NewInstanceWrapper;
import org.yield4j.java.astwrapper.NoopWrapper;
import org.yield4j.java.astwrapper.ReturnWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.SwitchWrapper;
import org.yield4j.java.astwrapper.SynchronizedWrapper;
import org.yield4j.java.astwrapper.ThrowWrapper;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.TypeWrapper;
import org.yield4j.java.astwrapper.ValueWrapper;
import org.yield4j.java.astwrapper.WhileWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.WrapperVisitor;
import org.yield4j.java.astwrapper.YieldBreakWrapper;
import org.yield4j.java.astwrapper.YieldReturnWrapper;
import org.yield4j.javac.util.TreeBuilder;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class JavacCodeGenerator implements CodeGenerator {

    private static final String OVERRIDE_CLASS_NAME = "Override";

    private final TreeBuilder builder;
    private Object result;

    public JavacCodeGenerator(TreeBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void generateInPlace(Wrapper root) {
        ActualGenerator gen = new ActualGenerator();
        root.accept(gen);
        result = gen.result;
    }

    @Override
    public Object getResult() {
        return result;
    }

    private class ActualGenerator implements WrapperVisitor {
        private LocalNameResolver resolver = new LocalNameResolver();
        private JCTree result;
        private MethodWrapper currentMethod;

        public <T extends JCTree> T generate(Wrapper w, Class<T> clazz) {
            T ret = generate0(w, clazz);
            if (ret == null) {
                return null;
            }
            if (w.shouldResolveLocalName()) {
                ret = resolveLocalName(ret);
            }
            w.setTarget(ret);
            return ret;
        }

        public <T extends JCTree> T generate0(Wrapper w, Class<T> clazz) {
            if (w == null) {
                return null;
            }

            w.accept(this);
            assert result != null : "No result generated for wrapper of type "
                    + w.getType();
            JCTree ret = result;
            result = null;
            return (T) ret;
        }

        public JCStatement generate(StatementWrapper w) {
            return generate(w, JCStatement.class);
        }

        public JCExpression generate(ExpressionWrapper w) {
            return generate(w, JCExpression.class);
        }

        public <T extends JCTree> List<T> generate(
                java.util.List<? extends Wrapper> wrappers, Class<T> clazz) {
            ListBuffer<T> list = new ListBuffer<T>();
            for (Wrapper sw : wrappers) {
                list.append((T) generate(sw, clazz));
            }
            return list.toList();
        }

        @Override
        public void visit(YieldReturnWrapper yieldReturnWrapper) {
            throw new AssertionError("Yield statement should not be generated.");
        }

        @Override
        public void visit(YieldBreakWrapper yieldBreakWrapper) {
            throw new AssertionError("Yield statement should not be generated.");
        }

        @Override
        public void visit(WhileWrapper w) {
            result = builder.getTreeMaker().WhileLoop(
                    generate(w.getCondition()), generate(w.getStatement()));
        }

        @Override
        public void visit(TryWrapper tryWrapper) {
            TreeMaker maker = builder.getTreeMaker();

            JCBlock block = (JCBlock) generate(tryWrapper.getBlock());
            JCBlock finalizer = (JCBlock) generate(tryWrapper.getFinalizer());
            ListBuffer<JCCatch> catches = new ListBuffer<JCCatch>();
            for (CatchWrapper cw : tryWrapper.getCatchers()) {
                catches.append(generate(cw, JCCatch.class));
            }

            result = maker.Try(block, catches.toList(), finalizer);
        }

        @Override
        public void visit(ThrowWrapper w) {
            result = builder.getTreeMaker().Throw(generate(w.getExpression()));
        }

        @Override
        public void visit(SwitchWrapper switchWrapper) {
            TreeMaker maker = builder.getTreeMaker();

            ListBuffer<JCCase> cases = new ListBuffer<JCCase>();
            for (CaseWrapper cw : switchWrapper.getCases()) {
                cases.append(generate(cw, JCCase.class));
            }

            result = maker.Switch(generate(switchWrapper.getExpression()),
                    cases.toList());
        }

        @Override
        public void visit(ReturnWrapper w) {
            result = builder.getTreeMaker().Return(generate(w.getExpression()));
        }

        @Override
        public void visit(LabeledWrapper w) {
            result = builder.getTreeMaker().Labelled(nameOrNull(w.getLabel()),
                    generate(w.getStatement()));
        }

        @Override
        public void visit(IfWrapper ifWrapper) {
            result = builder.getTreeMaker().If(
                    generate(ifWrapper.getCondition()),
                    generate(ifWrapper.getThenStatement()),
                    generate(ifWrapper.getElseStatement()));
        }

        @Override
        public void visit(ForWrapper w) {
            result = builder.getTreeMaker().ForLoop(
                    generate(w.getInitStatements(), JCStatement.class),
                    generate(w.getCondition()),
                    generate(w.getUpdateStatements(),
                            JCExpressionStatement.class),
                    generate(w.getStatement()));
        }

        @Override
        public void visit(DoWrapper w) {
            result = builder.getTreeMaker().DoLoop(generate(w.getStatement()),
                    generate(w.getCondition()));
        }

        @Override
        public void visit(DeclareVariableWrapper w) {
            TreeMaker maker = builder.getTreeMaker();

            JCVariableDecl var = (JCVariableDecl) w.getTarget();
            if (var != null && !w.isLocalVariable()) {
                // Just use the original field, with annotations and all.
                result = var;
                return;
            }

            long numericFlags = toNumericFlags(w.getFlags());
            result = maker.VarDef(maker.Modifiers(numericFlags),
                    nameOrNull(w.getName()), generate(w.getVariableType()),
                    generate(w.getInit()));
        }

        private long toNumericFlags(
                org.yield4j.java.astwrapper.Flags flags) {
            long numericFlags;
            if (flags.hasFlags()) {
                numericFlags = flags.getFlags();
            } else {
                numericFlags = 0l;
                numericFlags |= flags.isFinal() ? Flags.FINAL : 0l;
                numericFlags |= flags.isParameter() ? Flags.PARAMETER : 0l;
                numericFlags |= flags.isPublic() ? Flags.PUBLIC : 0l;
                numericFlags |= flags.isSynchronized() ? Flags.SYNCHRONIZED : 0l;
            }
            return numericFlags;
        }

        @Override
        public void visit(ContinueWrapper w) {
            result = builder.getTreeMaker().Continue(nameOrNull(w.getLabel()));
        }

        @Override
        public void visit(BreakWrapper w) {
            result = builder.getTreeMaker().Break(nameOrNull(w.getLabel()));
        }

        @Override
        public void visit(BlockWrapper w) {
            result = builder.getTreeMaker().Block(0,
                    generate(w.getStatements(), JCStatement.class));
        }

        @Override
        public void visit(ExpressionStatementWrapper w) {
            result = builder.getTreeMaker().Exec(generate(w.getExpression()));
        }

        @Override
        public void visit(AnyStatementWrapper w) {
            result = (JCTree) w.getTarget();
        }

        private Name nameOrNull(String s) {
            return s != null ? builder.getName(s) : null;
        }

        @Override
        public void visit(ValueWrapper valueWrapper) {
            result = builder.getJavacUtils().createLiteral(
                    valueWrapper.getValue());
        }

        @Override
        public void visit(TypeWrapper w) {
            String typeName = w.getTypeName();
            Class<?> typeClass = w.getTypeClass();

            if (typeName != null) {
                result = builder.nameToExpression(typeName);
            } else {
                assert typeClass != null : "Both typeName and typeClass are null!";
                if (typeClass.isPrimitive()) {
                    if (typeClass == boolean.class) {
                        result = builder.getTreeMaker().TypeIdent(
                                TypeTags.BOOLEAN);
                    } else {
                        throw new AssertionError("Unhandled primitive class: "
                                + typeClass.getName());
                    }
                } else {
                    result = builder.nameToExpression(typeClass.getName());
                }
            }
        }

        @Override
        public void visit(NewInstanceWrapper w) {
            JCExpression eclass = generate(w.getClazz());
            List<JCExpression> targs = generate(w.getTypeArguments(),
                    JCExpression.class);
            List<JCExpression> eargs = generate(w.getArguments(),
                    JCExpression.class);
            Wrapper impl = w.getImplementation();
            JCClassDecl cd = impl != null ? generate(impl, JCClassDecl.class)
                    : null;
            result = builder.getTreeMaker().NewClass(null, targs, eclass,
                    eargs, cd);
        }

        @Override
        public void visit(NameWrapper w) {
            // TODO: Object[] vs String[] - problem?
            result = builder.nameToExpression((Object[]) w.getNameParts());
        }

        @Override
        public void visit(EqualsWrapper w) {
            JCExpression lhs = generate(w.getLeft());
            JCExpression rhs = generate(w.getRight());
            result = builder.getJavacUtils().equalsExpression(lhs, rhs);
        }

        @Override
        public void visit(CallMethodWrapper w) {
            JCExpression methodSelect = generate(w.getMethodSelect());
            ListBuffer<JCExpression> argList = new ListBuffer<JCExpression>();
            for (ExpressionWrapper e : w.getArguments()) {
                argList.append(generate(e));
            }
            result = builder.getTreeMaker().Apply(null, methodSelect,
                    argList.toList());
        }

        @Override
        public void visit(AssignWrapper w) {
            result = builder.getTreeMaker().Assign(generate(w.getLeft()),
                    generate(w.getRight()));
        }

        @Override
        public void visit(GenericTypeWrapper w) {
            JCExpression eouter = generate(w.getOuter());
            List<JCExpression> params = generate(w.getTypeParameters(),
                    JCExpression.class);

            ListBuffer<JCExpression> newList = new ListBuffer<JCExpression>();
            for (JCExpression param : params) {
                JCExpression p2 = param;
                if (param.getKind() == Kind.PRIMITIVE_TYPE) {
                    p2 = builder.boxPrimitiveType((JCPrimitiveTypeTree) param);
                }
                newList.append(p2);
            }

            result = builder.getTreeMaker().TypeApply(eouter, newList.toList());
        }

        @Override
        public void visit(FinallyWrapper w) {
            result = builder.getTreeMaker().Block(0,
                    generate(w.getStatements(), JCStatement.class));
        }

        @Override
        public void visit(CaseWrapper cw) {
            result = builder.getTreeMaker().Case(generate(cw.getExpression()),
                    generate(cw.getStatements(), JCStatement.class));
        }

        @Override
        public void visit(MethodWrapper w) {
            MethodWrapper oldCurrentMethod = currentMethod;
            currentMethod = w;
            try {
                TreeMaker maker = builder.getTreeMaker();
    
                JCBlock body = generate(w.getBody(), JCBlock.class);
                JCMethodDecl meth = (JCMethodDecl) w.getTarget();
                if (meth != null) {
                    // Shortcut, just replace the body
                    meth.body = body;
                    result = meth;
                    return;
                }
    
                ListBuffer<JCVariableDecl> paramList = new ListBuffer<JCVariableDecl>();
                for (DeclareVariableWrapper p : w.getParameters()) {
                    paramList.append(generate(p, JCVariableDecl.class));
                }
    
                ListBuffer<JCAnnotation> anns = new ListBuffer<JCAnnotation>();
                if (w.isOverride()) {
                    anns.append(maker.Annotation(
                            builder.nameToExpression(OVERRIDE_CLASS_NAME),
                            List.<JCExpression> nil()));
                }
                long numericFlags = toNumericFlags(w.getFlags());
                JCModifiers mods = maker.Modifiers(numericFlags, anns.toList());
    
                List<JCExpression> throwDecls = generate(w.getThrows(),
                        JCExpression.class);
    
                Name methodName = builder.getName(w.getName());
                result = maker.MethodDef( //
                        mods, //
                        methodName, //
                        generate(w.getReturnType()), //
                        List.<JCTypeParameter> nil(), // no type parameters
                        paramList.toList(), // parameters
                        throwDecls, //
                        body, // method body
                        null // no default value
                        );
            } finally {
                currentMethod = oldCurrentMethod;
            }
        }

        @Override
        public void visit(ClassWrapper classWrapper) {
            ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
            for (Wrapper w : classWrapper.getMembers()) {
                defs.add(generate(w, JCTree.class));
            }

            JCClassDecl target = (JCClassDecl) classWrapper.getTarget();
            if (target != null) {
                // We're at the root class, so just replace all members.
                target.defs = defs.toList();
                result = target;
                return;
            }

            TreeMaker maker = builder.getTreeMaker();

            ExpressionWrapper baseType = classWrapper.getBaseType();
            JCExpression bexp = baseType != null ? generate(baseType) : null;
            result = maker.ClassDef( //
                    maker.Modifiers(0), // no modifiers
                    builder.getName(classWrapper.getName()), // class name
                    List.<JCTypeParameter> nil(), // no type parameters
                    bexp, // base class
                    List.<JCExpression> nil(), // no interfaces
                    defs.toList() // variables and methods
                    );
        }

        @Override
        public void visit(CatchWrapper cw) {
            result = builder.getTreeMaker().Catch(
                    (JCVariableDecl) generate(cw.getParameter()),
                    (JCBlock) generate(cw.getBlock()));
        }

        @Override
        public void visit(AnyExpressionWrapper anyExpressionWrapper) {
            result = (JCTree) anyExpressionWrapper.getTarget();
        }

        @Override
        public void visit(IsNotNullWrapper w) {
            result = builder.getJavacUtils().isNotNullExpression(
                    generate(w.getExpression()));
        }

        @Override
        public void visit(ForEachWrapper forEachWrapper) {
            throw new AssertionError(
                    "For each loop should not remain until code generation.");
        }

        @Override
        public void visit(NoopWrapper noopWrapper) {
            // "assert true" is essentially a no-op statement.
            JCExpression trueValue = builder.getJavacUtils()
                    .createLiteral(true);
            result = builder.getTreeMaker().Assert(trueValue, null);
        }

        @Override
        public void visit(IrrelevantWrapper irrelevantWrapper) {
            result = (JCTree) irrelevantWrapper.getTarget();
        }

        private <T extends JCTree> T resolveLocalName(T tree) {
            if (currentMethod.getLocalVariableSelector() == null) {
                return tree;
            }
            return resolver.translate(tree);
        }

        private class LocalNameResolver extends TreeTranslator {
            private Set<JCTree> resolved = new HashSet<JCTree>();

            @Override
            public void visitSelect(JCFieldAccess arg0) {
                if (resolved.contains(arg0)) {
                    // This is something we have resolved here. This can happen
                    // if
                    // we resolve something that comes from
                    // AnyExpressionWrapper,
                    // since that is one of two cases where we simply use the
                    // target
                    // directly from the wrapper rather than generating
                    // something.
                    result = arg0;
                } else {
                    super.visitSelect(arg0);
                }
            }

            @Override
            public void visitIdent(JCIdent arg0) {
                String name = arg0.name.toString();
                boolean isLocalName = currentMethod.getLocalVariableNames()
                        .contains(name);
                if (isLocalName) {
                    result = builder
                            .nameToExpression(
                                    currentMethod.getLocalVariableSelector(),
                                    arg0.name);
                    resolved.add(result);
                } else {
                    result = arg0;
                }
            }

        }

        @Override
        public void visit(SynchronizedWrapper synchronizedWrapper) {
            throw new AssertionError(
                    "Synchronized statement should not remain until code generation.");            
        }
    }
}
