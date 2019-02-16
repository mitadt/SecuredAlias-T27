package servlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import aes.AES;
import common.Common;
import database.Database;
import emailutility.EmailUtility;

/**
 * Servlet implementation class InsertDocuments
 */
@WebServlet("/InsertFile")
@MultipartConfig
public class InsertFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		System.out.println("doPost");
		PrintWriter out=response.getWriter();
		Part UploadedFile=request.getPart("UploadedFile");
		InputStream inputUploadedFile=UploadedFile.getInputStream();
		String FileName = request.getParameter("FileName");
		String Password = request.getParameter("Password");
		//System.out.println("Password "+Password);
		String userid = request.getParameter("userid");
		String location = request.getParameter("location");
		String Date = request.getParameter("Date");
		String FromTime = request.getParameter("FromTime");
		String ToTime = request.getParameter("ToTime");
		String PersonId = request.getParameter("PersonId");
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");		
		try {
			byte[] result = IOUtils.toByteArray(inputUploadedFile);
			System.out.println(result.length+"\n"+UploadedFile.getSize()+" result "+result);
			ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
	        emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	Connection conn=Database.getDBConnection();
	                	String encrypted = AES.encrypt(result,Password);
	        			System.out.println(" encrypted ");//Arrays.toString(encrypted)
	        			//InputStream is = new ByteArrayInputStream(encrypted);
	        			//IOUtils.toByteArray(inputimageBlob);
	        			String InsertQuery = "INSERT INTO `uploaded_file`(`AdminId`, `FileName`, `UploadedFile`,`Password`,Location,Date,FromTime,ToTime,UserId,Latitude,Longitude) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	        			PreparedStatement ps=conn.prepareStatement(InsertQuery);
	        			ps.setString(1, userid);
	        			ps.setString(2, FileName);
	        			//ps.setBinaryStream(3, inputUploadedFile,UploadedFile.getSize());
	        			ps.setString(3, encrypted);
	        			ps.setString(4, Password);
	        			ps.setString(5, location);
	        			ps.setString(6, Date);
	        			ps.setString(7, FromTime);
	        			ps.setString(8, ToTime);
	        			ps.setString(9, PersonId);
	        			ps.setString(10, latitude);
	        			ps.setString(11, longitude);
	        			int rows = ps.executeUpdate();	
	        			if(rows > 0){
	        			System.out.print("File Stored Sucessfully");
	        			String selectQuery = "SELECT * FROM users WHERE UserId=?";
	        			ps=conn.prepareStatement(selectQuery);
	        			ps.setString(1, PersonId);
	        			ResultSet rs = ps.executeQuery();
	        			if(rs.next()){
	        				String EmailId = rs.getString("Email");
					        	   EmailUtility.sendEmailWithAttachment(Common.host,Common.port,Common.adminEmailId,Common.adminEmailPass,new String[]{EmailId},"Location Base Security","Password To Decrypt "+FileName+" File is "+Password,null);
	        			}
	        			}
	        			ps.close();
	        			conn.close();	   	
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        emailExecutor.shutdown(); // it is very important to shutdown your non-singleton 
	        out.print("File Stored Sucessfully");
	        out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("doPost");
	}

}
