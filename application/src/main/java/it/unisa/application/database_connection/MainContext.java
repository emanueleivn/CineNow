package it.unisa.application.database_connection;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

@WebListener
public class MainContext implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataSource ds = DataSourceSingleton.getInstance();
        ServletContext context = sce.getServletContext();
        context.setAttribute("DataSource", ds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
