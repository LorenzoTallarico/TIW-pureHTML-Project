package it.polimi.tiw.dao;

import it.polimi.tiw.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @file UserDAO.java
 * @class UserDAO
 * 
 * @brief Data Access Object for User entity.
 * 
 * This class provides methods for interacting with the user data 
 * stored in the database. It allows for operations like retrieving 
 * user details, checking existing email and username, and adding 
 * a new user.
 */
public class UserDAO {

	private Connection connection;

	/**
     * @brief Constructs a UserDAO with the given database connection.
     * 
     * @param connection The database connection to be used by the DAO.
     */
	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	/**
     * @brief Retrieves a user from the database using their email and password.
     * 
     * This method searches for a user with the specified email and password.
     * 
     * @param mail The email of the user.
     * @param psw The password of the user.
     * @return The User object if found, otherwise null.
     * @throws SQLException If an error occurs while accessing the database.
     */
	public User getUser(String mail, String psw) throws SQLException {
		User user = null;
		String performedAction = " finding a user by email and password";
		String query = "SELECT * FROM utente WHERE mail = ? AND psw = ?;";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			mail= mail.toLowerCase();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, mail);
			preparedStatement.setString(2, psw);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt("id_utente"));
				user.setNome(resultSet.getString("nome"));
				user.setMail(resultSet.getString("mail"));
			}
			if (user != null) {
				System.out.println("User found: " + user.getId());
			} else {
				System.out.println("User not found");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// Log or handle the exception if necessary
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// Log or handle the exception if necessary
				}
			}
		}
		return user;
	}

	/**
     * @brief Checks if a given email exists in the database.
     * 
     * This method verifies if the specified email is already associated 
     * with a user in the database.
     * 
     * @param mail The email to check.
     * @return True if the email exists, otherwise false.
     * @throws SQLException If an error occurs while accessing the database.
     */
	public boolean isExistingMail(String mail) throws SQLException {
		boolean result = false;
		String performedAction = " finding email";
		String query = "SELECT * FROM utente WHERE mail = ?;";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			mail= mail.toLowerCase();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, mail);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				result = true;
			}
			if (result) {
				System.out.println("mail already exists");
			} else {
				System.out.println("mail not found");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// Log or handle the exception if necessary
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// Log or handle the exception if necessary
				}
			}
		}
		return result;
	}
	
	/**
     * @brief Checks if a given username exists in the database.
     * 
     * This method verifies if the specified username is already associated 
     * with a user in the database.
     * 
     * @param nome The username to check.
     * @return True if the username exists, otherwise false.
     * @throws SQLException If an error occurs while accessing the database.
     */
	public boolean isExistingUsername(String nome) throws SQLException {
		boolean result = false;
		String performedAction = " finding nome";
		String query = "SELECT * FROM utente WHERE nome = ?;";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nome);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				result = true;
			}
			if (result) {
				System.out.println("username already exists");
			} else {
				System.out.println("username not found");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// Log or handle the exception if necessary
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// Log or handle the exception if necessary
				}
			}
		}
		return result;
	}

	 /**
     * @brief Adds a new user to the database.
     * 
     * This method registers a new user if the username and email are not 
     * already in use.
     * 
     * @param nome The username of the new user.
     * @param mail The email of the new user.
     * @param psw The password of the new user.
     * @return 0 if the user was added successfully, 
     *         1 if the email already exists, 
     *         2 if the username already exists.
     * @throws SQLException If an error occurs while accessing the database.
     */
	public int addUser(String nome, String mail, String psw) throws SQLException {
		String performedAction = "registering a new user in the database";
		String query = "INSERT INTO utente (nome, mail, psw) VALUES (?, ?, ?);";
		PreparedStatement preparedStatement = null;
		int ris=0;
		if (isExistingUsername(nome)) {
			ris += 2;
		} 
		if (isExistingMail(mail)) {
			ris += 1;
		} 
		
		if(ris==0){
			try {
				mail= mail.toLowerCase();
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, nome);
				preparedStatement.setString(2, mail);
				preparedStatement.setString(3, psw);
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Utente aggiunto");
					ris = 0;
				} else {
					System.out.println("Mail già registrata");
				}
			} catch (SQLException e) {
				throw new SQLException("Error accessing the DB when " + performedAction, e);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						// Log or handle the exception if necessary
					}
				}
				
			}
		}
		return ris;
	}

}
