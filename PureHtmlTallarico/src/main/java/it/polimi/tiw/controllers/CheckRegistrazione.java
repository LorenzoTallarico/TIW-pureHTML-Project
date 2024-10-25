/**
 * @file CheckRegistrazione.java
 * @brief Servlet that handles the user registration process by validating input and adding new users to the database.
 * 
 * This servlet manages both GET requests (to display the registration page) and POST requests (to handle the registration process). 
 * It checks the validity of input fields such as name, email, and password, ensuring that they are non-empty and valid. 
 * If the input is correct, the servlet adds the user to the database using the `UserDAO` class.
 * 
 * @note It uses the Thymeleaf template engine to render HTML pages.
 * @note This servlet also manages errors such as existing usernames and emails.
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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ConnectionHandler;

/**
 * @class CheckRegistrazione
 * @brief Servlet that handles the user registration process.
 */
public class CheckRegistrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

    public CheckRegistrazione() {
        super();
    }

    /**
     * @brief Initializes the servlet, setting up the database connection and configuring the Thymeleaf template engine.
     * 
     * @throws ServletException if an error occurs during servlet initialization
     */
    @Override
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
     * @brief Handles GET requests by displaying the registration page.
     * 
     * This method processes the GET request and renders the registration page using Thymeleaf.
     * 
     * @param request  the HttpServletRequest object containing the client request
     * @param response the HttpServletResponse object used to respond to the client
     * @throws ServletException if an error occurs during the request
     * @throws IOException      if an I/O error occurs
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process("/index.html", ctx, response.getWriter());
	}

	/**
	 * @brief Handles POST requests for user registration.
	 * 
	 * This method validates the input fields, checks if the user already exists, and adds the new user to the database.
	 * If there are validation errors, it displays them on the registration page. 
	 * If registration is successful, it notifies the user.
	 * 
	 * @param request  the HttpServletRequest object containing the client request
	 * @param response the HttpServletResponse object used to respond to the client
	 * @throws ServletException if an error occurs during the request
	 * @throws IOException      if an I/O error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String nome = null;
	    String mail = null;
	    String password = null;
	    String password2 = null;
	    String errorMessage = ""; // Variable to store error messages
	    
	    try {
	        // Get nome, mail, password, and password2 from the form
	        nome = request.getParameter("nome");
	        mail = request.getParameter("mail");
	        password = request.getParameter("psw1");
	        password2 = request.getParameter("psw2");
	        boolean mailCheck=false;
	        // Check if fields are not empty
	        if (nome == null || nome.isBlank()) {
	            errorMessage += "Il campo nome e' vuoto.<br/>";
	        }
	        if (mail == null || mail.isBlank()) {
	            errorMessage += "Il campo mail e' vuoto.<br/>";
	        }
	        
	        if(!(mail.charAt(0)=='.') && !(mail.charAt(0)=='@') && mail.indexOf('@')< mail.indexOf('.', mail.indexOf('@')) && !(mail.charAt(mail.length()-1)=='.') && !(mail.charAt(mail.length()-1)=='@') && mail.indexOf('@', mail.indexOf('@')+1)==-1) {
	        	mailCheck=true;
	        }
	        	
	        if(!mailCheck) {
	        	errorMessage += "La mail non e' valida.<br/>";
	        }
	        if (password == null || password.isBlank()) {
	            errorMessage += "Il campo password e' vuoto.<br/>";
	        }
	        if (password2 == null || password2.isBlank()) {
	            errorMessage += "Il campo conferma password e' vuoto.<br/>";
	        }
	        if (!password.equals(password2)) {
	            errorMessage += "Le password non corrispondono.<br/>";
	        }
	        // If there are any errors, show them on the registration page
	        if (!errorMessage.equals("")) {
	            ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("registrazioneError", errorMessage);
	            templateEngine.process("/Registrazione.html", ctx, response.getWriter());
	            return; 
	        }
	        
	        // Add user to the database after validation
	        UserDAO userDao = new UserDAO(connection);
	        int risDB=userDao.addUser(nome, mail, password);
	        // Handle the result from the database
	        if (risDB==0) {
	        	// User successfully added
	            ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("loginError", "Registrato con successo!");
	            templateEngine.process("/Index.html", ctx, response.getWriter());
	            return;
	        } 
	        else if(risDB==1){
	        	// Username already registered
	            ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("registrazioneError", "Username gia' registrato");
	            templateEngine.process("/Registrazione.html", ctx, response.getWriter());
	            return; // Return after handling the error
	        }
	        else if(risDB==2) {
	        	// Email already registered
	            ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("registrazioneError", "Mail gia' registrata");
	            templateEngine.process("/Registrazione.html", ctx, response.getWriter());
	            return; // Return after handling the error
	        }
	        else {
	        	 // Both username and email already registered
	            ServletContext servletContext = getServletContext();
	            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	            ctx.setVariable("registrazioneError", "Username e Mail gia' registrati");
	            templateEngine.process("/Registrazione.html", ctx, response.getWriter());
	            return;
	        }

	    } catch (Exception e) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad_Request");
	        return;
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
