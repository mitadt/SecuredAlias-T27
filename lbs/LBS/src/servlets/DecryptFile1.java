package servlets;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.File;
import org.json.JSONArray;
import org.json.JSONObject;

import aes.AES;
import common.Common;
import database.Database;
import emailutility.EmailUtility;

/**
 * Servlet implementation class DecryptFile
 */
@WebServlet("/DecryptFile1")
public class DecryptFile1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DecryptFile1() {
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
		/*try{
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String FileId = request.getParameter("FileId");
			String AdminId = request.getParameter("AdminId");
			String Password = request.getParameter("Password");
			String selectQuery="SELECT * FROM uploaded_file WHERE Id=? and AdminId=?";
			Connection conn = Database.getDBConnection();
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, FileId);
			ps.setString(2, AdminId);
			ResultSet rs =  ps .executeQuery();
			if(rs.next()){
				byte[] verified = AES.decrypt(rs.getString("UploadedFile"),Password);	
				System.out.println(verified.length+" verified "+verified);
	        	FileOutputStream  fw = new FileOutputStream ("E:/Java/1604/"+rs.getString("FileName"));	                        
	        	System.out.print("File Writting!!!!");
	        	fw.write(verified);
				fw.close();
				System.out.print("File Written!!!!");
			}
			else{
				out.print("No File Added!!!!");
			}
			rs.close();
			ps.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		try{
			response.setContentType("text/html;charset=UTF-8");
			//PrintWriter out = response.getWriter();
			String FileId = request.getParameter("FileId");
			String Password = request.getParameter("Password");
			Connection conn = Database.getDBConnection();
			String selectQuery = "SELECT UploadedFile,FileName FROM uploaded_file WHERE id=? AND Password=?";
			PreparedStatement ps = conn.prepareStatement(selectQuery);
			ps.setString(1, FileId);
			ps.setString(2, Password);
			ResultSet rs = ps.executeQuery();		
			if(rs.next()){
				ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	Connection conn=Database.getDBConnection();		        
		        			System.out.print("File Stored Sucessfully");
		        			String selectQuery = "SELECT u.Email,uf.FileName,uf.UserId FROM `users` u JOIN uploaded_file uf ON u.`UserId`=uf.AdminId AND uf.Id=?";
		        			PreparedStatement ps=conn.prepareStatement(selectQuery);
		        			ps.setString(1, FileId);
		        			ResultSet rs = ps.executeQuery();
		        			if(rs.next()){
		        				String EmailId = rs.getString("Email");
		        				String FileName = rs.getString("FileName");
		        				String UserId = rs.getString("UserId");
		        				String selectQuery1 = "SELECT * FROM `users` WHERE UserId=?";
			        			ps=conn.prepareStatement(selectQuery1);
			        			ps.setString(1, UserId);
			        			rs = ps.executeQuery();
			        			rs.beforeFirst();
			        			if(rs.next()){
			        				String Name = rs.getString("Name");
			        				String UserId1 = rs.getString("UserId");
						        	   EmailUtility.sendEmailWithAttachment(Common.host,Common.port,Common.adminEmailId,Common.adminEmailPass,new String[]{EmailId},"Location Base Security",Name+" with Id "+UserId1+" is decrypting "+FileName+" file",null);
			        			}
		        			}
		        			rs.close();
		        			ps.close();
		        			conn.close();	   	
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        });
		        emailExecutor.shutdown(); // it is very important to shutdown your non-singleton 
		        
				byte[] verified = AES.decrypt(rs.getString("UploadedFile"),Password);	
				//String bytetoString = new String(Base64.getEncoder().encodeToString(verified));
				//InputStream is = new ByteArrayInputStream(verified);
				//out.print(bytetoString);
				//System.out.println(bytetoString);
				response.setContentType("*/*");
				response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				response.addHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				//send the image using OutputStream
				response.setContentLength(verified.length);
				response.getOutputStream().write(verified);
			}
			else{
				//out.print("Incorrect Password!!!!");
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
