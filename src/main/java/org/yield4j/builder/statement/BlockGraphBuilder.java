package org.yield4j.builder.statement;

import java.util.List;
import java.util.Queue;

import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class BlockGraphBuilder extends StatementGraphBuilder {

    public BlockGraphBuilder(StatementWrapper block) {
        super(block);
    }

    @Override
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        BlockWrapper block = (BlockWrapper) statements.poll();
        Graph g = new StatementGraphBuilder(block.getStatements()).build();
        tails.addAll(g.tails);
        return g.head;
    }
}
