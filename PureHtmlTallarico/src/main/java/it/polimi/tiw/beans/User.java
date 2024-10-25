package it.polimi.tiw.beans;

/**
 * @file User.java
 * @class User
 * 
 * @brief Represents a user in the system.
 * 
 * This class encapsulates the properties of a user, including
 * their ID, email address, and name.
 */
public class User {
	
	/** @brief Unique identifier for the user. */
	private int id;
	
	/** @brief Email address of the user. */
	private String mail;
	
	/** @brief Email address of the user. */
	private String nome;
	
	
	//GETTER
	
	/**
     * @brief Gets the unique identifier for the user.
     * @return The unique identifier for the user.
     */
	public int getId() {
		return id;
	}
	
	/**
     * @brief Gets the email address of the user.
     * @return The email address of the user.
     */
	public String getMail() {
		return mail;
	}

	/**
     * @brief Gets the name of the user.
     * @return The name of the user.
     */
	public String getNome() {
		return nome;
	}
	
	
	//SETTER
	
	/**
     * @brief Sets the unique identifier for the user.
     * @param id The new unique identifier for the user.
     */
	public void setId(int id) {
		this.id = id;
	}

	/**
     * @brief Sets the email address of the user.
     * @param mail The new email address for the user.
     */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
     * @brief Sets the name of the user.
     * @param nome The new name for the user.
     */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
