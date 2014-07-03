package fr.xebia.xskillz;

import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Provider;
import java.util.function.Function;

public class Transaction<T> {

    private final Function<GraphDatabaseService, T> work;

    public Transaction(Function<GraphDatabaseService, T> work) {
        this.work = work;
    }

    public T run(GraphDatabaseService db) {
        org.neo4j.graphdb.Transaction tx = db.beginTx();

        T result = work.apply(db);

        tx.success();
        tx.close();

        return result;

    }

    public T run(Provider<GraphDatabaseService> provider) {
        return run(provider.get());
    }

    public <U> Transaction<U> map(Function<T, U> f) {
        return new Transaction<>(work.andThen(f));
    }

    public <U> Transaction<U> flatMap(Function<GraphDatabaseService, Function<T, U>> nextWork) {
        return new Transaction<>(new Function<GraphDatabaseService, U>() {

            @Override
            public U apply(GraphDatabaseService db) {
                T t = work.apply(db);
                return nextWork.apply(db).apply(t);
            }
        });
    }

    public static <T> Transaction<T> start(Function<GraphDatabaseService, T> work) {
        return new Transaction<>(work);
    }

}
