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

import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.File;
import it.polimi.tiw.dao.FileDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.TreeOrder;

/**
 * @file CreateSposta.java
 * @brief This servlet handles the file moving process, allowing users to move files between directories.
 * @class CreateSposta
 *
 * This class extends HttpServlet and processes GET and POST requests to manage file movement,
 * handling session management and error responses.
 */
public class CreateSposta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
	
    public CreateSposta() {
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
	 * @brief Handles POST requests to move a file to a specified directory.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int idFile;
		int idDirPadre;
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		User user = (User) session.getAttribute("user");
		
		try {
	        // Get idDirPadre by POST
	        idFile = Integer.parseInt(request.getParameter("idDocu"));
	        idDirPadre = Integer.parseInt(request.getParameter("idDirPadre"));

	    } catch (Exception e) {
	        // if it's empty send a bad_request message
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing value");
	        return;
	    }

		ArrayList<File> lista = new ArrayList<File>();
		FileDAO fileDao = new FileDAO(connection);
		try {
			lista = fileDao.getAll(user.getId());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore !");
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		if (lista.isEmpty()) {
			ctx.setVariable("SpostaError", "Nessuna Cartella Trovata!");
			templateEngine.process("/WEB-INF/Sposta.html", ctx, response.getWriter());
			return; // Return after displaying success message
		} else {
			File root = TreeOrder.getOrder(lista);
			System.out.println(idFile);
			System.out.println(idDirPadre);
			ctx.setVariable("lista", root.getFigli());
			ctx.setVariable("idPartenza", idFile);
			ctx.setVariable("dirPadre", idDirPadre);
			templateEngine.process("/WEB-INF/Sposta.html", ctx, response.getWriter());
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
