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
 * Servlet implementation class AdminViewUploadedFile
 */
@WebServlet("/UserViewFile")
public class UserViewFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserViewFile() {
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
			System.out.println(UserId);
			String selectQuery="SELECT uf.*,u.Name FROM uploaded_file uf JOIN users u ON uf.AdminId=u.UserId WHERE uf.UserId=? ORDER BY Id DESC;";
			Connection conn = Database.getDBConnection();
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, UserId);
			ResultSet rs =  ps .executeQuery();
			rs.last();
			int row = rs.getRow();
			if(row>0){
				rs.beforeFirst();
				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
			while(rs.next()){				
				LinkedHashMap<String,String> lm = new LinkedHashMap<String,String>();
				lm.put("FileId", rs.getString("Id"));
				lm.put("AdminId", rs.getString("AdminId"));
				lm.put("AdminName", rs.getString("Name"));
				lm.put("FileName", rs.getString("FileName"));	
				lm.put("Password", rs.getString("Password"));
				lm.put("FromTime", rs.getString("FromTime"));
				lm.put("ToTime", rs.getString("ToTime"));
				lm.put("Latitude", rs.getString("Latitude"));
				lm.put("Longitude", rs.getString("Longitude"));
				jsonArray.put(lm);				
			}
			jsonObject.put("ViewFile", jsonArray);
			out.print(jsonObject);
			}
			else{
				out.print("No Data Found!!!!");
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
