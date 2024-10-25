package it.polimi.tiw.beans;

import java.util.ArrayList;

/**
 * @file File.java
 * @class File
 * 
 * @brief Represents a file or directory in the document management system.
 * 
 * This class encapsulates the properties of a file or directory, 
 * including its ID, parent directory ID, owner, name, creation date, 
 * type, description, and child files.
 */
public class File {
	
	/// This attribute represents the unique identifier for the document.
	private int idDocu;
	
	/// This attribute represents the ID of the parent directory.
	private int idDirPadre;
	
	/// This attribute represents the owner of the document.
	private int proprietario;
	
	/// This attribute represents the name of the document or directory.
	private String nome;
	
	/// This attribute represents the creation date of the document.
	private String creazione;
	
	/// This attribute represents the type of the document (e.g., file type).
	private String tipo;
	
	/// This attribute represents the description of the document.
	private String descrizione;
	
	/// This attribute represents the name of the parent directory.
	private String nomePadre;
	
	/// This attribute holds a list of child files or directories.
	private ArrayList<File> figli= new ArrayList<>();
	
	//GETTER
	/**
     * @brief Gets the document ID.
     * 
     * @return The document ID.
     */
    public int getIdDocu() {
        return idDocu;
    }

    /**
     * @brief Gets the parent directory ID.
     * 
     * @return The parent directory ID.
     */
    public int getIdDirPadre() {
        return idDirPadre;
    }

    /**
     * @brief Gets the owner ID.
     * 
     * @return The owner ID.
     */
    public int getProprietario() {
        return proprietario;
    }

    /**
     * @brief Gets the name of the file or directory.
     * 
     * @return The name of the file or directory.
     */
    public String getNome() {
        return nome;
    }

    /**
     * @brief Gets the creation date of the file or directory.
     * 
     * @return The creation date as a string.
     */
    public String getCreazione() {
        return creazione;
    }

    /**
     * @brief Gets the type of the file.
     * 
     * @return The type of the file.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @brief Gets the description of the file or directory.
     * 
     * @return The description.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @brief Gets the list of child files or directories.
     * 
     * @return An ArrayList of child File objects.
     */
    public ArrayList<File> getFigli() {
        return figli;
    }

    /**
     * @brief Sets the document ID.
     * 
     * @param idDocu The document ID to set.
     */
    public void setIdDocu(int idDocu) {
        this.idDocu = idDocu;
    }

    /**
     * @brief Sets the parent directory ID.
     * 
     * @param idDirPadre The parent directory ID to set.
     */
    public void setIdDirPadre(int idDirPadre) {
        this.idDirPadre = idDirPadre;
    }

    /**
     * @brief Sets the owner ID.
     * 
     * @param proprietario The owner ID to set.
     */
    public void setProprietario(int proprietario) {
        this.proprietario = proprietario;
    }

    /**
     * @brief Sets the name of the file or directory.
     * 
     * @param nome The name to set.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @brief Sets the creation date of the file or directory.
     * 
     * @param creazione The creation date to set.
     */
    public void setCreazione(String creazione) {
        this.creazione = creazione;
    }

    /**
     * @brief Sets the type of the file.
     * 
     * @param tipo The type to set.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @brief Sets the description of the file or directory.
     * 
     * @param descrizione The description to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @brief Sets the list of child files or directories.
     * 
     * @param figli The list of child files to set.
     */
    public void setFigli(ArrayList<File> figli) {
        this.figli = figli;
    }

    /**
     * @brief Adds a child file or directory.
     * 
     * @param figlio The child file to add.
     */
    public void addFiglio(File figlio) {
        figli.add(figlio);
    }

    /**
     * @brief Gets the name of the parent directory.
     * 
     * @return The name of the parent directory.
     */
    public String getNomePadre() {
        return nomePadre;
    }

    /**
     * @brief Sets the name of the parent directory.
     * 
     * @param nomePadre The name of the parent directory to set.
     */
    public void setNomePadre(String nomePadre) {
        this.nomePadre = nomePadre;
    }
	
}
