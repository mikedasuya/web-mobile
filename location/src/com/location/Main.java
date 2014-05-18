package com.location;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.DbConnection;

import java.io.*;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Main() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
		// TODO Auto-generated method stub
		public void doGet(HttpServletRequest request, HttpServletResponse response)
			    throws IOException, ServletException
			    {
			System.out.println("doGet");
			        response.setContentType("text/html");
			        PrintWriter out = response.getWriter();
			        out.println("<html>");
			        out.println("<head>");
			        out.println("<title>Hello World!</title>");
			        out.println("</head>");
			        out.println("<body>");
			        out.println("<h1>Hello World!</h1>");
			        out.println("</body>");
			        out.println("</html>");
			    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DbConnection db = new DbConnection();
		String emailid = request.getParameter("email");
		String lat = request.getParameter("lat");
		String longi = request.getParameter("long");
		String day = request.getParameter("day");
	    String month = request.getParameter("month");
	    String year = request.getParameter("year");
		System.out.println("---dopost");
		db.doJDBC(emailid, lat, longi, day, month, year);
		
	}

}
