package fr.xebia.xskillz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.GraphDatabaseService;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {

    @Mock
    org.neo4j.graphdb.Transaction tx;

    @Mock
    GraphDatabaseService db;

    @Before
    public void setUp() throws Exception {
        when(db.beginTx()).thenReturn(tx);
    }

    @Test
    public void transaction_should_chain_function_with_map_on_only_open_one_underlying_transaction_on_run_call() {
        Transaction<String> transaction = Transaction.start(db -> "step-1")
                .map(before -> before + "-2")
                .map(before -> before + "-3");

        verifyZeroInteractions(db);

        String result = transaction.run(db);

        assertThat(result).isEqualTo("step-1-2-3");

        verify(db).beginTx();
        verify(tx).success();
        verify(tx).close();
    }

    @Test
    public void transaction_should_chain_function_with_flatmap_on_only_open_one_underlying_transaction_on_run_call() {
        Transaction<String> transaction = Transaction.start(db -> "step-1")
                .flatMap(db -> before -> before + "-2")
                .flatMap(db -> before -> before + "-3");

        verifyZeroInteractions(db);

        String result = transaction.run(db);

        assertThat(result).isEqualTo("step-1-2-3");

        verify(db).beginTx();
        verify(tx).success();
        verify(tx).close();
    }

}