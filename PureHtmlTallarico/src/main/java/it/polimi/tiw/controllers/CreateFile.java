package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * @file CreateFile.java
 * @brief This servlet handles the creation of new files and directories.
 * @class CreateFile
 * 
 * This class extends HttpServlet and processes GET and POST requests to create
 * new files or directories in the user's storage based on the provided parameters.
 */
public class CreateFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    public CreateFile() {
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
	 * @brief Handles POST requests to create a new file or directory.
	 * @param request the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-related error occurs.
	 * @throws IOException if an I/O error occurs during the processing of the request.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/Index.html");
			return;
		}
		User user = (User) session.getAttribute("user");
		String nome;
        int dirPadre;
        String tipo1;
        String tipo2;
        String descrizione;
		try {
	        // Get idDirPadre by POST
			nome = request.getParameter("nome");
	        dirPadre = Integer.parseInt(request.getParameter("dirPadre"));
	        tipo1 = request.getParameter("tipo1");
	        tipo2 = request.getParameter("tipo2").trim();
	        descrizione = request.getParameter("description").trim();

	    } catch (Exception e) {
	        // if it's empty send a bad_request message
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing value");
	        return;
	    }
		
        String error= "";
        
        if (nome == null || nome.isBlank()) {
            error += "Il campo nome e' vuoto. <br/>";
        }
        if (tipo1 == null || tipo1.isBlank()) {
            error += "Il selettore dir/file e' vuoto. <br/>";
        }
        if (dirPadre==0 && !tipo1.equals("dir")) {
            error += "Cartella destinazione invalida. <br/> ";
        }
        if(!tipo1.equals("dir") && !tipo1.equals("file")) {
        	error += "Il selettore dir/file e' invalido. <br/> ";
        }
        if(tipo1.equals("dir") && !tipo2.isBlank()) {
        	error += "Non è possibile applicare un estensione a una dir. <br/> ";
        }
        if(tipo1.equals("dir") && !descrizione.isBlank()) {
        	error += "Non è possibile applicare una descrizione a una dir. <br/> ";
        }
        if(!tipo1.equals("dir")) {
        	tipo1=tipo2;
        }
        if(!error.equals("")) {
        	getServletContext().setAttribute("errorCreateFile", error);
			response.sendRedirect(getServletContext().getContextPath() + "/creategestionecontenuti");
        }
        else {
        	if (descrizione == null || descrizione.isEmpty()) {
        	    descrizione = "";  
        	}
        	Date data = new Date();
            SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
            String dataS = formatoData.format(data);
        	FileDAO fileDao = new FileDAO(connection);
        	try {
				if(fileDao.newDocu(user.getId(), nome, dirPadre, tipo1, descrizione, dataS)) {
					response.sendRedirect(getServletContext().getContextPath() + "/createhome");
				}
				else {
					getServletContext().setAttribute("error", error);
					response.sendRedirect(getServletContext().getContextPath() + "/creategestionecontenuti");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
		
	}
	
	/**
	 * @brief Cleans up resources when the servlet is destroyed.
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
