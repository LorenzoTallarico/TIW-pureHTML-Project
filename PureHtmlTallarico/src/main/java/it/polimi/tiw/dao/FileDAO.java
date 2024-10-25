package it.polimi.tiw.dao;

import it.polimi.tiw.beans.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

/**
 * @file FileDAO.java
 * @brief This class provides methods for interacting with the file database.
 * @class FileDAO
 *
 * This class allows for various operations on files and directories in the database,
 * such as retrieving, inserting, and moving files, as well as checking directory status.
 */
public class FileDAO {

	private Connection connection;

	/**
	 * @brief Constructs a FileDAO with the specified database connection.
	 * @param connection the database connection to be used by this DAO.
	 */
	public FileDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @brief Retrieves all directories owned by a user.
	 * @param id the ID of the user whose directories are to be retrieved.
	 * @return a list of File objects representing the user's directories.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method queries the database for directories owned by the specified user and 
	 * returns them in an ArrayList. The directories are sorted by their parent ID.
	 */
	public ArrayList<File> getDir(int id) throws SQLException {
		ArrayList<File> lista = new ArrayList<>();
		File file = null;
		String performedAction = " finds all folders of a user, by their id";
		String query = "SELECT * FROM docu WHERE proprietario = ? AND tipo = 'dir' ORDER BY id_dirPadre;";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				file = new File();
				file.setIdDocu(resultSet.getInt("id_docu"));
				file.setNome(resultSet.getString("nome"));

				if (resultSet.getInt("id_dirPadre") == 0) {
					file.setIdDirPadre(0);
				} else {
					file.setIdDirPadre(resultSet.getInt("id_dirPadre"));
				}

				file.setTipo(resultSet.getString("tipo"));
				lista.add(file);
			}
			if (lista.isEmpty()) {
				System.out.println("No folder found!");
			} else {
				System.out.println("Folders found!");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					
				}
			}
		}
		return lista;
	}

	/**
	 * @brief Retrieves all files and directories owned by a user.
	 * @param id the ID of the user whose files and directories are to be retrieved.
	 * @return a list of File objects representing the user's files and directories.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method queries the database for all files and directories owned by the specified user 
	 * and returns them in an ArrayList, sorted by their parent ID.
	 */
	public ArrayList<File> getAll(int id) throws SQLException {
		ArrayList<File> lista = new ArrayList<>();
		File file = null;
		String performedAction = " finds all folders of a user, by their id";
		String query = "SELECT * FROM docu WHERE proprietario = ? ORDER BY id_dirPadre;";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				file = new File();
				file.setIdDocu(resultSet.getInt("id_docu"));
				file.setNome(resultSet.getString("nome"));

				if (resultSet.getInt("id_dirPadre") == 0) {
					file.setIdDirPadre(0);
				} else {
					file.setIdDirPadre(resultSet.getInt("id_dirPadre"));
				}

				file.setTipo(resultSet.getString("tipo"));
				lista.add(file);
			}
			if (lista.isEmpty()) {
				System.out.println("No folder found!");
			} else {
				System.out.println("Folders found!");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				
				}
			}
		}
		return lista;
	}

	/**
	 * @brief Retrieves all files in a specific directory owned by a user.
	 * @param id the ID of the user whose files are to be retrieved.
	 * @param idDirPadre the ID of the parent directory.
	 * @return a list of File objects representing the files in the specified directory.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method queries the database for all files that belong to the specified user 
	 * and are located in the specified directory.
	 */
	public ArrayList<File> getFileFrom(int id, int idDirPadre) throws SQLException {
		ArrayList<File> lista = new ArrayList<>();
		File file = null;
		String performedAction = "finds all files of a user, by their id and idDirPadre";
		String query = "SELECT D1.id_docu as id_docu, D1.nome as nome, D1.id_dirPadre as id_dirPadre, D1.nome as nome, D1.tipo as tipo, D1.descrizione as descrizione, D1.creazione as creazione, D2.nome as nomePadre FROM docu as D1, docu as D2 WHERE D1.id_dirPadre=D2.id_docu and D1.proprietario = ? AND D1.tipo <> 'dir' AND D1.id_dirPadre = ? && D2.tipo = 'dir';";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, idDirPadre);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				file = new File();
				file.setNomePadre(resultSet.getString("nomePadre"));
				file.setIdDirPadre(resultSet.getInt("id_dirPadre"));
				file.setIdDocu(resultSet.getInt("id_docu"));
				file.setNome(resultSet.getString("nome"));
				file.setTipo(resultSet.getString("tipo"));
				file.setDescrizione(resultSet.getString("descrizione"));
				file.setCreazione(resultSet.getString("creazione"));
				lista.add(file);
			}
			if (lista.isEmpty()) {
				System.out.println("No folder or file found!");
			} else {
				System.out.println("Folders or files found!");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					
				}
			}
		}
		return lista;
	}

	/**
	 * @brief Retrieves the details of a specific file.
	 * @param id the ID of the user who owns the file.
	 * @param idDocu the ID of the file to be retrieved.
	 * @return a File object representing the specified file, or null if not found.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method queries the database for a specific file's details based on the user ID 
	 * and file ID.
	 */
	public File getFile(int id, int idDocu) throws SQLException {
		File file = null;
		String performedAction = "finds file description, by their id and idUser";
		String query = "SELECT D1.id_docu as id_docu, D1.nome as nome, D1.id_dirPadre as id_dirPadre, D1.nome as nome, D1.tipo as tipo, D1.descrizione as descrizione, D1.creazione as creazione, D2.nome as nomePadre FROM docu as D1, docu as D2 WHERE D1.id_dirPadre=D2.id_docu and D1.proprietario = ? AND D1.tipo <> 'dir' AND D1.id_docu = ?;";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, idDocu);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				file = new File();
				file.setIdDocu(resultSet.getInt("id_docu"));
				file.setIdDirPadre(resultSet.getInt("id_dirPadre"));
				file.setNome(resultSet.getString("nome"));
				file.setTipo(resultSet.getString("tipo"));
				file.setDescrizione(resultSet.getString("descrizione"));
				file.setCreazione(resultSet.getString("creazione"));
				file.setNomePadre(resultSet.getString("nomePadre"));
			}
			if (file == null) {
				System.out.println("No file found!");
			} else {
				System.out.println("file found!");
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
			
				}
			}
		}
		return file;
	}
	
	/**
	 * @brief Checks if a specific ID corresponds to a directory owned by a user.
	 * @param id the ID of the user.
	 * @param idDirDest the ID of the directory to check.
	 * @return true if the ID corresponds to a directory, false otherwise.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method queries the database to determine if the specified ID corresponds 
	 * to a directory owned by the user.
	 */
	public boolean isDir(int id, int idDirDest) throws SQLException {
		String performedAction = "Is dir?";
		String query = "SELECT * FROM docu WHERE proprietario = ? AND id_docu = ? AND tipo = 'dir';";
		boolean ris = false;
		ResultSet resultSet = null;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, idDirDest);

			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				ris= true;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new SQLException("Error accessing the DB when " + performedAction, e);
		}

		return ris;
	}
	
	/**
	 * @brief Moves a file to a new directory.
	 * @param id the ID of the user who owns the file.
	 * @param idPartenza the ID of the file to be moved.
	 * @param idDirDest the ID of the destination directory.
	 * @return true if the file was successfully moved, false otherwise.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method updates the database to move the specified file to the specified 
	 * destination directory, ensuring that the destination is a valid directory.
	 */
	public boolean moveFile(int id, int idPartenza, int idDirDest) throws SQLException {
		if(!isDir(id, idDirDest)) {
			return false;
		}
		String performedAction = "move File in the database";
		String query = "UPDATE docu SET id_dirPadre = ? WHERE id_docu = ? AND proprietario = ? AND tipo <> 'dir';";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean ris = false;

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, idDirDest);
			preparedStatement.setInt(2, idPartenza);
			preparedStatement.setInt(3, id);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Dir cambiata");
				ris = true;
			} else {
				System.out.println("ERRORE! Dir non Cambiata");
				ris = false;
			}
		} catch (SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				
				}
			}
		}
		return ris;
	}

	/**
	 * @brief Inserts a new file or directory into the database.
	 * @param id the ID of the user creating the file or directory.
	 * @param nome the name of the file or directory.
	 * @param idDirPadre the ID of the parent directory, or 0 if it is a root element.
	 * @param tipo the type of the file or directory.
	 * @param descrizione a description of the file or directory.
	 * @param data the creation date of the file or directory.
	 * @return true if the file or directory was successfully created, false otherwise.
	 * @throws SQLException if there is an error accessing the database.
	 *
	 * This method inserts a new file or directory into the database with the specified attributes,
	 * checking if the parent directory exists if it is not a root element.
	 */
	public boolean newDocu(int id, String nome, int idDirPadre, String tipo, String descrizione, String data)
			throws SQLException {
		if(idDirPadre!=0 && !isDir(id, idDirPadre)){
			return false;
		}
		String performedAction = "Insert new file or dir";
		String query = "INSERT INTO docu (nome, creazione, proprietario, tipo, id_dirPadre, descrizione) VALUES (?, ?, ?, ?, ?, ?);";
		boolean ris = false;
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, nome);
			preparedStatement.setString(2, data);
			preparedStatement.setInt(3, id);
			preparedStatement.setString(4, tipo);
			if(idDirPadre==0)
				preparedStatement.setNull(5, java.sql.Types.INTEGER);
			else
				preparedStatement.setInt(5, idDirPadre);
			preparedStatement.setString(6, descrizione);

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Docu creato");
				ris = true;
			} else {
				System.out.println("ERRORE! Docu non creato");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error accessing the DB when " + performedAction, e);
		}

		return ris;
	}
}
