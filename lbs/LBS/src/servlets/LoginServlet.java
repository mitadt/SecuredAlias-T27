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
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
			String mobile = request.getParameter("mobile");
			String password = request.getParameter("password");
			String usertype = request.getParameter("usertype");
			String selectQuery="SELECT * FROM users WHERE mobileno=? and password=? and usertype=?";
			Connection conn = Database.getDBConnection();
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, mobile);
			ps.setString(2, password);
			ps.setString(3, usertype);
			ResultSet rs =  ps .executeQuery();
			if(rs.next()){
				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				LinkedHashMap<String,String> lm = new LinkedHashMap<String,String>();
				lm.put("UserId", rs.getString("UserId"));
				lm.put("Name", rs.getString("Name"));
				lm.put("Email", rs.getString("Email"));
				lm.put("MobileNo", rs.getString("MobileNo"));
				lm.put("Password", rs.getString("Password"));
				lm.put("City", rs.getString("City"));
				lm.put("Gender", rs.getString("Gender"));
				lm.put("Age", rs.getString("Age"));
				lm.put("UserType",rs.getString("UserType"));
				jsonArray.put(lm);
				jsonObject.put("loginData", jsonArray);
				out.print(jsonObject);
				out.print("Login successfully!");				
			}
			else{
				out.print("Invalid username or password!");
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
