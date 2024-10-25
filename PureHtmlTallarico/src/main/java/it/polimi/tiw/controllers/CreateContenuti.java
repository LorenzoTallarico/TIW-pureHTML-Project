package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.File;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.FileDAO;
import it.polimi.tiw.utils.ConnectionHandler;


public class CreateContenuti extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	/**
	 * @file CreateContenuti.java
	 * @brief This servlet handles the creation and display of contents in a user's directory.
	 * @class CreateContenuti
	 * 
	 * This class extends HttpServlet and provides functionality for handling both
	 * GET and POST requests related to the contents in a specific directory.
	 * It verifies user sessions and retrieves files from the database.
	 */
	public CreateContenuti() {
		super();
	}

	/**
	 * @brief Initializes the servlet and establishes a database connection.
	 * 
	 * This method sets up the template engine and prepares the connection to the
	 * database when the servlet is initialized.
	 * 
	 * @throws ServletException if a servlet error occurs during initialization
	 */
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	/**
	 * @brief Handles GET requests for creating contents.
	 * 
	 * This method retrieves the contents from the specified directory and displays
	 * them on the contents page. It checks if the user is logged in and whether
	 * the directory ID is valid.
	 * 
	 * @param request the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet error occurs
	 * @throws IOException if an I/O error occurs during the process
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int idDirPadre;
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		User user = (User) session.getAttribute("user");
		try {
			String idDirS = request.getParameter("idDir");
			if (idDirS == null || idDirS.isEmpty()) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Value");
		        return;
		    }
			idDirPadre = Integer.parseInt(idDirS);
	    } catch (Exception e) {
	        // if it's empty send a bad_request message
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Value");
	        return;
	    }
		ArrayList<File> lista = new ArrayList<File>();
		FileDAO fileDao = new FileDAO(connection);
		try {
			lista = fileDao.getFileFrom(user.getId(), idDirPadre);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore!");
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		if (lista.isEmpty()) {
			ctx.setVariable("contenutiError", "Cartella vuota o non esistente!");
			templateEngine.process("/WEB-INF/Contenuti.html", ctx, response.getWriter());
			return; // Return after displaying success message
		} else {
			ctx.setVariable("lista", lista);
			ctx.setVariable("nomePadre", lista.get(0).getNomePadre());
			templateEngine.process("/WEB-INF/Contenuti.html", ctx, response.getWriter());
		}

	}

	/**
	 * @brief Handles POST requests for creating contents.
	 * 
	 * This method processes the request to create contents based on user input and
	 * displays the updated contents on the contents page. It checks if the user is
	 * logged in and whether the directory ID is valid.
	 * 
	 * @param request the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet error occurs
	 * @throws IOException if an I/O error occurs during the process
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		int idDirPadre;
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		User user = (User) session.getAttribute("user");
		try {
			String idDirS = request.getParameter("idDir");
			if (idDirS == null || idDirS.isEmpty()) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Value");
		        return;
		    }
			idDirPadre = Integer.parseInt(idDirS);
	    } catch (Exception e) {
	        // if it's empty send a bad_request message
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Value");
	        return;
	    }
		ArrayList<File> lista = new ArrayList<File>();
		FileDAO fileDao = new FileDAO(connection);
		try {
			lista = fileDao.getFileFrom(user.getId(), idDirPadre);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore!");
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		if (lista.isEmpty()) {
			ctx.setVariable("contenutiError", "Cartella vuota o non esistente!");
			templateEngine.process("/WEB-INF/Contenuti.html", ctx, response.getWriter());
			return; // Return after displaying success message
		} else {
			ctx.setVariable("lista", lista);
			ctx.setVariable("nomePadre", lista.get(0).getNomePadre());
			templateEngine.process("/WEB-INF/Contenuti.html", ctx, response.getWriter());
		}
	}
	
	/**
	 * @brief Cleans up resources when the servlet is destroyed.
	 * 
	 * This method closes the database connection to free up resources.
	 * It handles any SQL exceptions that may occur during the closing process.
	 * 
	 * @throws SQLException if a database access error occurs while closing the connection
	 */
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
