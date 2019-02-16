package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import database.Database;

/**
 * Servlet implementation class GetUsers
 */
@WebServlet("/GetUsers")
public class GetUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUsers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		try{
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String UserId = request.getParameter("UserId");
			String selectQuery="SELECT * FROM users WHERE UserType<>'Admin';";
			Connection conn = Database.getDBConnection();
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ResultSet rs =  ps .executeQuery();
			rs.last();
			int row = rs.getRow();
			if(row>0){
				rs.beforeFirst();
				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
			while(rs.next()){				
				LinkedHashMap<String,String> lm = new LinkedHashMap<String,String>();
				lm.put("UserId", rs.getString("UserId"));
				lm.put("UserName", rs.getString("Name"));			
				jsonArray.put(lm);				
			}
			jsonObject.put("GetUsername", jsonArray);
			out.print(jsonObject);
			}
			else{
				out.print("No Users Found!!!!");
			}
			rs.close();
			ps.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
