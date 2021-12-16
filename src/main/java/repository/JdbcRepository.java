package repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public abstract class JdbcRepository<T> implements ICrudRepository<T> {

    /**
     * takes url, user, password from the .properties file and opens the connection with the database
     *
     * @return the connection with the database
     * @throws ClassNotFoundException (forName) if the class cannot be located
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     */
    public Connection openConnection() throws ClassNotFoundException, SQLException, IOException {
        InputStream input = new FileInputStream("./src/main/resources/config.properties");
        Properties prop = new Properties();
        prop.load(input);

        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, user, password);

        input.close();
        return connection;
    }

    /**
     * closes the connection with the database. The connection is given as a parameter
     *
     * @param connection - the connection to be closed
     * @throws SQLException if a database access error occurs
     */
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * reads data from database and returns a list with the objects from the database
     *
     * @return a list with the objects from the database
     */


    @Override
    public int size() throws SQLException, ClassNotFoundException, IOException {
        Connection connection = openConnection();
        int listSize = readDataFromDatabase(connection).size();
        closeConnection(connection);
        return listSize;
    }

    @Override
    public List<T> findAll() throws SQLException, ClassNotFoundException, IOException {
        Connection connection = openConnection();
        List<T> objects = readDataFromDatabase(connection);
        closeConnection(connection);
        return objects;
    }
}
