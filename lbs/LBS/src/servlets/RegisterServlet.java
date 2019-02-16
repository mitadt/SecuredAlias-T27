package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Database;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String mobile = request.getParameter("mobile");			
			String password = request.getParameter("password");
			String city = request.getParameter("city");
			String gender = request.getParameter("gender");
			String age = request.getParameter("age");
			String usertype = request.getParameter("usertype");
		
			PrintWriter out = response.getWriter();
			Connection conn = Database.getDBConnection();
			String selectQuery = "SELECT * FROM users WHERE mobileno=? AND UserType=?";
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, mobile);
			ps.setString(2, usertype);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()){
				String insertQuery = "INSERT INTO users(Name,Email,MobileNo,Password,City,Gender,Age,UserType) VALUES (?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(insertQuery);
				ps.setString(1, name);
				ps.setString(2, email);
				ps.setString(3, mobile);
				ps.setString(4, password);
				ps.setString(5, city);
				ps.setString(6, gender);
				ps.setString(7, age);
				ps.setString(8, usertype);	
				ps.executeUpdate();
				out.print("Successfully registered!");
			}
			else{
				out.print("MobileNo already exist!");
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
