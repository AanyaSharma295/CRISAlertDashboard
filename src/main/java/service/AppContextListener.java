package service;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import java.util.concurrent.*;

@WebListener
public class AppContextListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    // Runs when Tomcat starts
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("App started — starting alert scheduler...");

        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Run AlertScheduler every 30 minutes
        scheduler.scheduleAtFixedRate(
            new AlertScheduler(),
            0,        // start immediately
            30,       // then every 30 minutes
            TimeUnit.MINUTES
        );

        System.out.println("Scheduler started successfully!");
    }

    // Runs when Tomcat stops
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdown();
            System.out.println("Scheduler stopped.");
        }
    }
}