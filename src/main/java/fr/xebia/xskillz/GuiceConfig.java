package fr.xebia.xskillz;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.guice.JerseyServletModule;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class GuiceConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(XebianRepository.class).in(Singleton.class);
                bind(AuthRepository.class).in(Singleton.class);
                bind(SkillRepository.class).in(Singleton.class);

                Map<String, String> params = new HashMap<String, String>();
                params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

                serve("/app/api/*").with(GuiceContainer.class, params);
            }
        });
    }
}
