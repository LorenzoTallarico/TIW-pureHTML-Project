/**
 * @file CheckLogin.java
 * @brief Servlet that handles user login by checking credentials and managing session creation.
 * 
 * This servlet processes login requests, validates user credentials, manages session creation, 
 * and handles both GET and POST requests.
 */

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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ConnectionHandler;

/**
 * @class CheckLogin
 * @brief A servlet to manage user authentication and session creation.
 * 
 * The servlet retrieves user credentials via a form submission, validates them, 
 * and initiates a session for authenticated users. It uses Thymeleaf to display 
 * the login page and error messages if authentication fails.
 */
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	/**
     * @brief Constructor for CheckLogin servlet.
     * 
     * Initializes a new instance of the CheckLogin servlet.
     */
	public CheckLogin() {
		super();
	}

	/**
     * @brief Initializes the servlet and configures the template engine.
     * 
     * Establishes a database connection and configures Thymeleaf for HTML template rendering.
     * 
     * @throws ServletException if an error occurs during initialization
     */
	@Override
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
     * @brief Handles GET requests by rendering the login page.
     * 
     * Processes GET requests to display the login page using Thymeleaf templates.
     * 
     * @param request  the HttpServletRequest object containing the client request
     * @param response the HttpServletResponse object for sending a response back to the client
     * 
     * @throws ServletException if an error occurs during the request
     * @throws IOException      if an I/O error occurs during processing
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process("/index.html", ctx, response.getWriter());
	}

	/**
     * @brief Handles POST requests to authenticate a user.
     * 
     * Retrieves user credentials from the request, checks their validity, and creates a session 
     * for authenticated users. If the credentials are invalid, an error message is displayed.
     * 
     * @param request  the HttpServletRequest object containing the client request
     * @param response the HttpServletResponse object for sending a response back to the client
     * 
     * @throws ServletException if an error occurs during the request
     * @throws IOException      if an I/O error occurs during processing
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String mail = null;
	    String password = null;

	    try {
	    	// Retrieve mail and password from the form
	        mail = request.getParameter("mail");
	        password = request.getParameter("psw");
	        if (mail == null || password == null || mail.isBlank() || password.isBlank()) {
	        	ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("loginError", "Campi vuoti");
	            templateEngine.process("/Index.html", ctx, response.getWriter());
	            return; 
	        }
	    } catch (Exception e) {
	        
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "BadRequest");
	        return;
	    }

	    
	    UserDAO userDao = new UserDAO(connection);
	    User user = null;

	    try {
	        user = userDao.getUser(mail, password);
	        if (user == null) { // User not found
	            ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("loginError", "Mail o Password incorretti! Nessun utente trovato");
	            templateEngine.process("/Index.html", ctx, response.getWriter());
	            return; 
	        }

	    } catch (SQLException e) {
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
	                "Mail o Password incorretti (finale)! Nessun utente trovato");
	        return;
	    }

	    try {
	    	// Invalidate the current session if exists
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }

	        // Create a new session and store the User object
	        request.getSession().setAttribute("user", user);

	        // Redirect to CreateHome page
	        response.sendRedirect(getServletContext().getContextPath() + "/createhome");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	/**
     * @brief Closes the connection when the servlet is destroyed.
     * 
     * Ensures that the database connection is closed when the servlet is no longer in use.
     */
	@Override
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
