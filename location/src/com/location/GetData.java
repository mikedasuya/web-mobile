package com.location;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.LangLat;
import com.database.DbConnection;

import java.io.*;
import java.util.List;

/**
 * Servlet implementation class Main
 */
@WebServlet("/GetData")
public class GetData extends HttpServlet {
	private static final long serialVersionUID = 2L;

    /**
     * Default constructor. 
     */
    public GetData() {
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
			       /* response.setContentType("text/html");
			        PrintWriter out = response.getWriter();
			        out.println("<html>");
			        out.println("<head>");
			        out.println("<title>Hello World!</title>");
			        out.println("</head>");
			        out.println("<body>");
			        out.println("<h1>Hello World!</h1>");
			        out.println("</body>");
			        out.println("</html>");*/
			        String emailid = request.getParameter("email");
			        String day = request.getParameter("day");
			        String month = request.getParameter("month");
			        String year = request.getParameter("year");
			        System.out.println(emailid+ day+ month+ year);
			        DbConnection db = new DbConnection();
			        List<LangLat> list = db.getData(emailid, day, month, year);
			        int i = 0;
			        JSONArray  array = new JSONArray();
			        JSONObject mainObj = new JSONObject();
			        
			        while(i < list.size()) {
			        	LangLat obj = list.get(i);
			        	JSONObject langlat = new JSONObject();
			        	try {
			        		langlat.put("lat", obj.lat);
							langlat.put("longi", obj.longi);
							array.put(langlat);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	i++;
			        }
			        response.setContentType("application/json");
			        try {
						mainObj.put("LatLong", array);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        System.out.println(mainObj.toString());
			        response.getWriter().write(mainObj.toString());
			        
			    }
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}

}
