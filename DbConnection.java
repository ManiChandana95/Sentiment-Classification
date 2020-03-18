package com.ieee;



import java.sql.*;


public class DbConnection {

	static Connection con;

	public static Connection getConnections() {

		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fuzzy","root","inetsolv");
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;

	}
}
