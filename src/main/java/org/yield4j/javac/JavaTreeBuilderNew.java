package org.yield4j.javac;

import static org.yield4j.javac.util.Statements.getMethodInvocation;
import static org.yield4j.javac.util.Statements.isYieldBreak;
import static org.yield4j.javac.util.Statements.isYieldReturn;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import org.yield4j.Generator;
import org.yield4j.java.AstBuilder;
import org.yield4j.java.astwrapper.AnyExpressionWrapper;
import org.yield4j.java.astwrapper.AnyStatementWrapper;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.CallMethodWrapper;
import org.yield4j.java.astwrapper.CaseWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.ClassWrapper;
import org.yield4j.java.astwrapper.ContinueWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.DoWrapper;
import org.yield4j.java.astwrapper.ExpressionStatementWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.FinallyWrapper;
import org.yield4j.java.astwrapper.ForEachWrapper;
import org.yield4j.java.astwrapper.ForWrapper;
import org.yield4j.java.astwrapper.GenericTypeWrapper;
import org.yield4j.java.astwrapper.IfWrapper;
import org.yield4j.java.astwrapper.IrrelevantWrapper;
import org.yield4j.java.astwrapper.LabeledWrapper;
import org.yield4j.java.astwrapper.MethodWrapper;
import org.yield4j.java.astwrapper.NewInstanceWrapper;
import org.yield4j.java.astwrapper.ReturnWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.SwitchWrapper;
import org.yield4j.java.astwrapper.SynchronizedWrapper;
import org.yield4j.java.astwrapper.ThrowWrapper;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.WhileWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.WrapperType;
import org.yield4j.java.astwrapper.YieldBreakWrapper;
import org.yield4j.java.astwrapper.YieldReturnWrapper;
import org.yield4j.javac.util.TreeBuilder;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class JavaTreeBuilderNew implements AstBuilder {

    private final TreeBuilder builder;
    private final ProcessingEnvironment env;

    public JavaTreeBuilderNew(ProcessingEnvironment env, TreeBuilder builder) {
        this.builder = builder;
        this.env = env;
    }

    @Override
    public List<ClassWrapper> generateAstWithClassRoots(Element startingPoint) {
        Trees trees = Trees.instance(env);
        Tree t = trees.getPath(startingPoint).getCompilationUnit();
        ActualBuilder w = new ActualBuilder();
        t.accept(w, null);
        return w.foundClasses;
    }

    private class ActualBuilder extends SimpleTreeVisitor<Wrapper, Void> {

        private boolean blockIsFinally;
        private boolean foundYieldStatement;
        List<ClassWrapper> foundClasses = new ArrayList<ClassWrapper>();

        private <T extends Wrapper> List<T> visitList(
                Iterable<? extends Tree> trees, Class<T> itemClass) {
            List<T> result = new ArrayList<T>();
            for (Tree t : trees) {
                result.add(visit(t, itemClass));
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        private <T extends Wrapper> T visit(Tree tree, Class<T> itemClass) {
            T ret = (T) visit(tree, (Void) null);
            return ret;
        }

        @Override
        public Wrapper visitCompilationUnit(CompilationUnitTree arg0, Void arg1) {
            visitList(arg0.getImports(), Wrapper.class);
            visitList(arg0.getTypeDecls(), Wrapper.class);
            return null;
        }

        @Override
        public Wrapper visitImport(ImportTree arg0, Void arg1) {
            return super.visitImport(arg0, arg1);
        }

        @Override
        public Wrapper visitIf(IfTree arg0, Void arg1) {
            return new IfWrapper(arg0, visit(arg0.getCondition(),
                    ExpressionWrapper.class), visit(arg0.getThenStatement(),
                    StatementWrapper.class), visit(arg0.getElseStatement(),
                    StatementWrapper.class));
        }

        @Override
        public Wrapper visitWhileLoop(WhileLoopTree arg0, Void arg1) {
            return new WhileWrapper(arg0, visit(arg0.getCondition(),
                    ExpressionWrapper.class), visit(arg0.getStatement(),
                    StatementWrapper.class));
        }

        @Override
        public Wrapper visitDoWhileLoop(DoWhileLoopTree arg0, Void arg1) {
            return new DoWrapper(arg0, visit(arg0.getCondition(),
                    ExpressionWrapper.class), visit(arg0.getStatement(),
                    StatementWrapper.class));
        }

        @Override
        public Wrapper visitForLoop(ForLoopTree arg0, Void arg1) {
            return new ForWrapper(arg0, //
                    visitList(arg0.getInitializer(), StatementWrapper.class), //
                    visit(arg0.getCondition(), ExpressionWrapper.class), //
                    visitList(arg0.getUpdate(), StatementWrapper.class), //
                    visit(arg0.getStatement(), StatementWrapper.class));
        }

        @Override
        public Wrapper visitEnhancedForLoop(EnhancedForLoopTree arg0, Void arg1) {
            return new ForEachWrapper(arg0, //
                    visit(arg0.getVariable(), DeclareVariableWrapper.class), //
                    visit(arg0.getExpression(), ExpressionWrapper.class), //
                    visit(arg0.getStatement(), StatementWrapper.class));
        }

        @Override
        public Wrapper visitSwitch(SwitchTree arg0, Void arg1) {
            return new SwitchWrapper(arg0, //
                    visit(arg0.getExpression(), ExpressionWrapper.class), //
                    visitList(arg0.getCases(), CaseWrapper.class));
        }

        @Override
        public Wrapper visitCase(CaseTree arg0, Void arg1) {
            return new CaseWrapper(arg0, //
                    visit(arg0.getExpression(), ExpressionWrapper.class), //
                    visitList(arg0.getStatements(), StatementWrapper.class));
        }

        @Override
        public Wrapper visitTry(TryTree arg0, Void arg1) {
            // Signal that the next BlockTree is a finally block.
            blockIsFinally = true;
            FinallyWrapper finallyWrapper;
            try {
                finallyWrapper = visit(arg0.getFinallyBlock(),
                        FinallyWrapper.class);
            } finally {
                blockIsFinally = false;
            }

            return new TryWrapper(arg0, //
                    visitList(builder.getJavacUtils().getResources(arg0),
                            Wrapper.class), visit(arg0.getBlock(),
                            BlockWrapper.class), //
                    visitList(arg0.getCatches(), CatchWrapper.class), //
                    finallyWrapper);
        }

        @Override
        public Wrapper visitCatch(CatchTree arg0, Void arg1) {
            DeclareVariableWrapper param = visit(arg0.getParameter(),
                    DeclareVariableWrapper.class);

            return new CatchWrapper(arg0, param, visit(arg0.getBlock(),
                    BlockWrapper.class));
        }

        @Override
        public Wrapper visitBlock(BlockTree arg0, Void arg1) {
            boolean oldIsFinally = blockIsFinally;
            blockIsFinally = false;
            List<StatementWrapper> statements = visitList(arg0.getStatements(),
                    StatementWrapper.class);
            blockIsFinally = oldIsFinally;

            Wrapper w;
            if (blockIsFinally) {
                w = new FinallyWrapper(arg0, statements);
            } else {
                w = new BlockWrapper(arg0, statements);
            }
            return w;
        }

        @Override
        public Wrapper visitBreak(BreakTree arg0, Void arg1) {
            Name label = arg0.getLabel();
            return new BreakWrapper(arg0, label != null ? label.toString()
                    : null);
        }

        @Override
        public Wrapper visitContinue(ContinueTree arg0, Void arg1) {
            Name label = arg0.getLabel();
            return new ContinueWrapper(arg0, label != null ? label.toString()
                    : null);
        }

        @Override
        public Wrapper visitThrow(ThrowTree arg0, Void arg1) {
            return new ThrowWrapper(arg0, visit(arg0.getExpression(),
                    ExpressionWrapper.class));
        }

        @Override
        public Wrapper visitLabeledStatement(LabeledStatementTree arg0,
                Void arg1) {
            return new LabeledWrapper(arg0, visit(arg0.getStatement(),
                    StatementWrapper.class), arg0.getLabel().toString());
        }

        @Override
        public Wrapper visitVariable(VariableTree arg0, Void arg1) {
            String varName = arg0.getName().toString();
            JavacFlags flags = new JavacFlags(
                    ((JCVariableDecl) arg0).getModifiers().flags);
            DeclareVariableWrapper v = new DeclareVariableWrapper(arg0, visit(
                    arg0.getType(), ExpressionWrapper.class), visit(
                    arg0.getInitializer(), ExpressionWrapper.class), flags,
                    varName);

            return v;
        }

        @Override
        public Wrapper visitMethodInvocation(MethodInvocationTree arg0,
                Void arg1) {
            List<ExpressionWrapper> args = visitList(arg0.getArguments(),
                    ExpressionWrapper.class);
            return new CallMethodWrapper(arg0, visit(arg0.getMethodSelect(),
                    ExpressionWrapper.class), args);
        }

        @Override
        protected Wrapper defaultAction(Tree arg0, Void arg1) {
            Wrapper w = null; // TODO: does null cause problems?
            if (arg0 instanceof ExpressionTree) {
                w = new AnyExpressionWrapper(arg0);
            } else if (arg0 instanceof StatementTree) {
                w = new AnyStatementWrapper(arg0);
            }
            return w;
        }

        @Override
        public Wrapper visitClass(ClassTree arg0, Void arg1) {
            List<Wrapper> memberWrappers = visitList(arg0.getMembers(),
                    Wrapper.class);
            boolean hasGeneratorMethod = false;
            for (Wrapper w : memberWrappers) {
                // Non-generator methods are wrapped as irrelevant entities
                // and will have a different type.
                if (w.getType() == WrapperType.METHOD) {
                    hasGeneratorMethod = true;
                    break;
                }
            }
            Wrapper w = null;
            if (hasGeneratorMethod) {
                ClassWrapper cw = new ClassWrapper(arg0, arg0.getSimpleName()
                        .toString(), memberWrappers);
                foundClasses.add(cw);
                w = cw;
            } else {
                // Don't care about this class, but we must wrap it.
                w = new IrrelevantWrapper(arg0, arg0.getSimpleName().toString());
            }
            return w;
        }

        @Override
        public Wrapper visitMethod(MethodTree arg0, Void arg1) {
            List<DeclareVariableWrapper> params = visitList(
                    arg0.getParameters(), DeclareVariableWrapper.class);

            boolean oldFoundFlag = foundYieldStatement;
            foundYieldStatement = false;
            try {
                BlockWrapper body = null;
                if (hasGeneratorAnnotation(arg0)) {
                    body = visit(arg0.getBody(), BlockWrapper.class);
                }
                Wrapper w = null;
                if (foundYieldStatement) {
                    // This method is relevant.
                    // Note: Annotations and flags will be preserved due to the
                    // fact that we later on (in the code generation phase)
                    // simply replace the method body. Had we not done that, we
                    // would have to copy all annotations here!
                    JavacFlags flags = new JavacFlags(
                            ((JCModifiers) arg0.getModifiers()).flags);
                    boolean overrides = false;
                    w = new MethodWrapper(
                            arg0,
                            arg0.getName().toString(),
                            body,
                            visit(arg0.getReturnType(), ExpressionWrapper.class),
                            params,
                            visitList(arg0.getThrows(), ExpressionWrapper.class),
                            flags, overrides);
                } else {
                    // Don't care about this method, but we must wrap it.
                    w = new IrrelevantWrapper(arg0, arg0.getName().toString());
                }
                return w;
            } finally {
                // Reset the flag as this method might be in an anonymous inner
                // class, and we don't want the enclosing method to be marked as
                // generator method by accident.
                foundYieldStatement = oldFoundFlag;
            }
        }

        private boolean hasGeneratorAnnotation(MethodTree arg0) {
            List<? extends AnnotationTree> anns = arg0.getModifiers().getAnnotations();
            for (AnnotationTree ann : anns) {
                Tree at = ann.getAnnotationType();
                if (at.toString().equals(Generator.class.getName()))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Wrapper visitExpressionStatement(ExpressionStatementTree arg0,
                Void arg1) {
            ExpressionTree t = arg0.getExpression();
            Wrapper w = null;
            MethodInvocationTree mi = getMethodInvocation(arg0);
            if (mi != null) {
                if (isYieldBreak(mi)) {
                    w = new YieldBreakWrapper(arg0, visit(t,
                            CallMethodWrapper.class));
                } else if (isYieldReturn(mi)) {
                    w = new YieldReturnWrapper(arg0, visit(t,
                            CallMethodWrapper.class));
                }
            }
            if (w == null) {
                // This is something else, don't care what!
                w = new ExpressionStatementWrapper(arg0, visit(
                        arg0.getExpression(), ExpressionWrapper.class));
            } else {
                // Found a yield, set the flag!
                foundYieldStatement = true;
            }
            return w;
        }

        @Override
        public Wrapper visitReturn(ReturnTree arg0, Void arg1) {
            return new ReturnWrapper(arg0, visit(arg0.getExpression(),
                    ExpressionWrapper.class));
        }

        @Override
        public Wrapper visitParameterizedType(ParameterizedTypeTree arg0,
                Void arg1) {
            return new GenericTypeWrapper(arg0, visit(arg0.getType(),
                    ExpressionWrapper.class), visitList(
                    arg0.getTypeArguments(), ExpressionWrapper.class));
        }

        @Override
        public Wrapper visitNewClass(NewClassTree arg0, Void arg1) {
            if (arg0.getEnclosingExpression() != null) {
                // expr.new C< ... > ( ... )
                throw new AssertionError("TODO: Handle enclosing expression.");
            }
            return new NewInstanceWrapper(arg0, visit(arg0.getIdentifier(),
                    ExpressionWrapper.class), visitList(
                    arg0.getTypeArguments(), ExpressionWrapper.class),
                    visitList(arg0.getArguments(), ExpressionWrapper.class),
                    visit(arg0.getClassBody(), Wrapper.class));
        }

        @Override
        public Wrapper visitSynchronized(SynchronizedTree arg0, Void arg1) {
            return new SynchronizedWrapper(arg0, visit(arg0.getExpression(),
                    ExpressionWrapper.class), visit(arg0.getBlock(),
                    BlockWrapper.class));
        }
    }
}
