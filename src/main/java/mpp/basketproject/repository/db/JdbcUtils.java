package mpp.basketproject.repository.db;

import mpp.basketproject.repository.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {

    private Properties properties;
    private static final Logger logger = LogManager.getLogger();
    private Connection connection = null;

    public JdbcUtils(Properties properties){
        this.properties = properties;
    }

    private Connection getNewConnection(){
        logger.traceEntry();

        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        logger.info("connecting to database {}", url);
        logger.info("user {}", user);
        logger.info("password {}", password);

        Connection connection = null;
        try {
            if (user != null && password != null){
                connection = DriverManager.getConnection(url, user, password);
            }
            else{
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public Connection getConnection() throws RepositoryException {
        logger.traceEntry();

        try {
            if (connection == null || connection.isClosed()) {
                connection = getNewConnection();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            throw new RepositoryException("Connection Error: " + ex.getMessage());
        }

        logger.traceExit();
        return connection;
    }

}
