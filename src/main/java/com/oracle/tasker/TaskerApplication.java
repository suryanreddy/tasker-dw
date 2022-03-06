package com.oracle.tasker;

import com.oracle.tasker.api.Task;
import com.oracle.tasker.db.TaskDAO;
import com.oracle.tasker.health.TaskerHealthCheck;
import com.oracle.tasker.resources.TaskResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class TaskerApplication extends Application<TaskerConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(TaskerApplication.class);

    public static void main(final String[] args) throws Exception {
        new TaskerApplication().run(args);
    }

    private final HibernateBundle<TaskerConfiguration> hibernateBundle =
            new HibernateBundle<TaskerConfiguration>(Task.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(TaskerConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "tasker";
    }

    @Override
    public void initialize(final Bootstrap<TaskerConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MigrationsBundle<TaskerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TaskerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(hibernateBundle);
        logger.info("TaskerApplication initialized.");
    }

    @Override
    public void run(final TaskerConfiguration configuration,
                    final Environment environment) {

        final TaskDAO taskDAO = new TaskDAO(hibernateBundle.getSessionFactory());
        environment.jersey().register(new TaskResource(taskDAO));

        final TaskerHealthCheck healthCheck =
                new TaskerHealthCheck(configuration.getTemplate());

        environment.healthChecks().register("template", healthCheck);

        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        logger.info("TaskerApplication classes, entities and health checks were registered.");
    }

}
