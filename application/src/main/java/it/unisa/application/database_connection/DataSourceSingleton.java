package it.unisa.application.database_connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceSingleton {
    private static volatile DataSource instance;

    private DataSourceSingleton() {}

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSourceSingleton.class) {
                if (instance == null) {
                    try {
                        Context initCtx = new InitialContext();
                        Context envCtx = (Context) initCtx.lookup("java:comp/env");
                        instance = (DataSource) envCtx.lookup("jdbc/CineNowDataSource");
                    } catch (NamingException e) {
                        throw new RuntimeException("Error initializing DataSource", e);
                    }
                }
            }
        }
        return instance;
    }
}
