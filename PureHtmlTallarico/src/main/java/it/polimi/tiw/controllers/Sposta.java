package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.FileDAO;
import it.polimi.tiw.utils.ConnectionHandler;

/**
 * @file Sposta.java
 * @brief This servlet handles the movement of files between directories.
 * @class Sposta
 *
 * This class extends HttpServlet and processes HTTP requests to move files from one directory to another
 * based on user input. It ensures that the user is authenticated and that necessary parameters are provided.
 */
public class Sposta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    public Sposta() {
        super();
    }
    
    /**
	 * @brief Initializes the servlet by establishing a database connection and setting up the template engine.
	 * @param servletContext the ServletContext for the application.
	 * @throws ServletException if a servlet-related error occurs.
	 */
    public void init() throws ServletException {
		this.connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
    
    /**
	 * @brief Handles GET requests by redirecting to the doPost method.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @brief Handles POST requests to move a file from one directory to another.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int idDirDest;
		int idPartenza;
		boolean ris;
		
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		
		User user = (User) session.getAttribute("user");
		
		try {
	        // Get idDirPadre by POST
	        idDirDest = Integer.parseInt(request.getParameter("idDirDest"));
	        idPartenza = Integer.parseInt(request.getParameter("idPartenza"));

	    } catch (Exception e) {
	        // if it's empty send a bad_request message
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing value");
	        return;
	    }
		
		FileDAO fileDao = new FileDAO(connection);
		try {
			ris = fileDao.moveFile(user.getId(), idPartenza, idDirDest);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore!");
			return;
		}
		if(ris) {
			response.sendRedirect(getServletContext().getContextPath() + "/createcontenuti?idDir=" + idDirDest);
		}
		else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore!");
			return;
		}
		
	}
	
	/**
	 * @brief Cleans up resources before the servlet is destroyed.
	 */
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
