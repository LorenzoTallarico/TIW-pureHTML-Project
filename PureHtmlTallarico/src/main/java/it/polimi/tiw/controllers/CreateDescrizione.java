package it.polimi.tiw.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.utils.ConnectionHandler;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.thymeleaf.context.WebContext;

import it.polimi.tiw.beans.File;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.FileDAO;

/**
 * @file CreateDescrizione.java
 * @brief This servlet handles the display of file descriptions.
 * @class CreateDescrizione
 * 
 * This class extends HttpServlet and processes GET and POST requests
 * to retrieve and display information about a specific file based on
 * the user's session. It ensures the user is logged in and retrieves
 * the file data from the database.
 */
public class CreateDescrizione extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    public CreateDescrizione() {
        super();
    }
    
	/**
	 * @brief Initializes the servlet and establishes a database connection.
	 * @throws ServletException if a servlet-related error occurs.
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
	 * @brief Handles POST requests to retrieve and display file information.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int idDocu;
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		User user = (User) session.getAttribute("user");
		try {
	        // Get idDirPadre by POST
	        idDocu = Integer.parseInt(request.getParameter("idDocu"));

	    } catch (Exception e) {
	        // if it's empty send a bad_request message
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing value");
	        return;
	    }
		File file;
		FileDAO fileDao = new FileDAO(connection);
		try {
			file = fileDao.getFile(user.getId(), idDocu);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore!");
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		if (file==null || file.getNome()==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File inesistente!");
			return;
		} else {
			ctx.setVariable("file", file);
			templateEngine.process("/WEB-INF/Descrizione.html", ctx, response.getWriter());
		}
	}
	
	/**
	 * @brief Cleans up resources when the servlet is destroyed.
	 */
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
