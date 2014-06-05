package fr.xebia.xskillz;

import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.SEVERE;

public class GuiceConfig extends GuiceServletContextListener {

    private Injector injector;

    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(XebianRepository.class).in(Singleton.class);
                bind(AuthRepository.class).in(Singleton.class);
                bind(SkillRepository.class).in(Singleton.class);
                bind(DatabaseService.class).in(Singleton.class);
                bind(GraphDatabaseService.class).toProvider(DatabaseService.class);


                Map<String, String> params = new HashMap<>();
                params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

                serve("/app/api/*").with(GuiceContainer.class, params);
            }
        });
        return injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        final DatabaseService databaseService = injector.getInstance(DatabaseService.class);
        try {
            databaseService.startAsync().awaitRunning(30, SECONDS);
        } catch (TimeoutException e) {
            throw Throwables.propagate(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    databaseService.stopAsync().awaitTerminated(30, SECONDS);
                } catch (TimeoutException e) {
                    Logger.getGlobal().log(SEVERE, "Error while terminating database", e);
                }
            }
        }));
    }
}
