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
 * @file CreateHome.java
 * @brief This servlet handles the home page for content management, displaying the user's directories.
 * @class CreateHome
 *
 * This class extends HttpServlet and processes GET and POST requests to display 
 * the user's home page with their directories, handling session management and errors.
 */
public class CreateHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;


	public CreateHome() {
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
	 * @brief Handles GET requests to display the user's home page.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		User user = (User) session.getAttribute("user");

		ArrayList<File> lista = new ArrayList<File>();
		FileDAO fileDao = new FileDAO(connection);
		try {
			lista = fileDao.getDir(user.getId());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore !");
			return;
		}

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		if (lista.isEmpty()) {
			ctx.setVariable("homeError", "Nessuna Cartella Trovata!");
			templateEngine.process("/WEB-INF/Home.html", ctx, response.getWriter());
			return; // Return after displaying success message
		} else {
			File root = TreeOrder.getOrder(lista);
			ctx.setVariable("lista", root.getFigli());
			templateEngine.process("/WEB-INF/Home.html", ctx, response.getWriter());
		}

	}

	/**
	 * @brief Handles POST requests by redirecting to the doGet method.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
