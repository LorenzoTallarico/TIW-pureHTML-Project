package it.polimi.tiw.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;

/**
 * @file ConnectionHandler.java
 * @brief This class handles the management of database connections.
 * @class ConnectionHandler
 *
 * This utility class provides methods to establish and close database connections
 * using connection parameters defined in the servlet context.
 */
public class ConnectionHandler {
	/**
	 * @brief Establishes a database connection using parameters from the servlet context.
	 * @param context the ServletContext containing database connection parameters.
	 * @return a Connection object to interact with the database.
	 * @throws UnavailableException if the database driver cannot be loaded or if the connection cannot be established.
	 */
	public static Connection getConnection(ServletContext context) throws UnavailableException {
		Connection connection = null;
		try {
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't get db connection");
		}
		return connection;
	}

	/**
	 * @brief Closes the given database connection.
	 * @param connection the Connection object to close.
	 * @throws SQLException if an error occurs while closing the connection.
	 */
	public static void closeConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

}
