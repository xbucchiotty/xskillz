package fr.xebia.xskillz;

import org.junit.Test;

import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.api.Assertions.assertThat;

public class DatabaseServiceTest {

    @Test
    public void database_should_be_null_when_service_is_not_started() {
        DatabaseService databaseService = new DatabaseService();

        assertThat(databaseService.get()).isNull();
    }

    @Test
    public void database_should_be_notnull_when_service_started() throws TimeoutException {
        DatabaseService databaseService = new DatabaseService();

        databaseService.startAsync().awaitRunning(10, SECONDS);

        assertThat(databaseService.get()).isNotNull();
    }

    @Test
    public void database_should_be_null_when_service_is_terminated() throws TimeoutException {
        DatabaseService databaseService = new DatabaseService();

        databaseService.startAsync().awaitRunning(10, SECONDS);
        databaseService.stopAsync().awaitTerminated(10, SECONDS);


        assertThat(databaseService.get()).isNull();
    }
}
