package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
public static Connection conn = null;
public static Connection getDBConnection(){
	try{
		//Load and register the JDBC driver.
		Class.forName("com.mysql.jdbc.Driver");
		//Open a connection to the database.
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbs?useSSL=false", "root", "");
	}
	catch (Exception e) {
		e.printStackTrace();//this is used  to get system generated error.
	}
	return conn;
}
}
