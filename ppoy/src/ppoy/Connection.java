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
			
			//SQL 작성
			String sql = "select * from users";
			//PreparedStatement 얻기 및 값 지정
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			//SQL실행 후 ResultSet을 통해 데이터 읽기
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Users user = new Users();
				user.setUserId(rs.getString("user_Id"));
				user.setUserName(rs.getString("user_name"));
				user.setUserPw(rs.getString("user_pw"));
				user.setUserTel(rs.getString("user_tel"));
				
				System.out.println(user);
			}
			rs.close();
			pstmt.close();
			
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
