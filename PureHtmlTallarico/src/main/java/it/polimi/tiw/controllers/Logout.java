package it.polimi.tiw.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @file Logout.java
 * @brief This servlet handles user logout functionality by invalidating the session.
 * @class Logout
 *
 * This class extends HttpServlet and processes GET and POST requests to log out a user,
 * ensuring that the session is invalidated and the user is redirected to the home page.
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Logout() {
		super();
	}

	/**
	 * @brief Handles GET requests to log out the user by invalidating the session.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
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
	
}
