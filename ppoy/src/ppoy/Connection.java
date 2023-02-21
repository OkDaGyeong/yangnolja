package ppoy;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

	public static void main(String[] args) {
		java.sql.Connection conn = null;
		try {
			//JDBC Driver 등록
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//연결하기
			conn = DriverManager.getConnection(
				"jdbc:mysql://222.119.100.81:3382/ppoy", 
				"ppoy", 
				"ppoy"
			);	
			
			System.out.println("연결 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try { 
					//연결 끊기
					conn.close(); 
					System.out.println("연결 끊기");
				} catch (SQLException e) {}
			}
		}
	}

}
