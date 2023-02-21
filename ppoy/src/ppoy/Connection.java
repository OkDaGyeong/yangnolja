package ppoy;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connection {
	Connection conn = null;
	Statement stmt = null;
	ArrayList<Users> userTblList = new ArrayList<>();
	ArrayList<ReservationTbl> rserTblList = new ArrayList<>();
	
	
	public Connection() {
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
			String sqlUsers = "select * from users";
			//PreparedStatement 얻기 및 값 지정
			PreparedStatement pstmtUser = conn.prepareStatement(sqlUsers);			
			//SQL실행 후 ResultSet을 통해 데이터 읽기
			ResultSet rs = pstmtUser.executeQuery();
			System.out.println("[user 테이블 데이터]");
			while(rs.next()) {
				Users user = new Users();
				user.setUserId(rs.getString("user_Id"));
				user.setUserName(rs.getString("user_name"));
				user.setUserPw(rs.getString("user_pw"));
				user.setUserTel(rs.getString("user_tel"));
				userTblList.add(user);
				System.out.println(user);
			}
			//System.out.println("88"+userTblList.get(1)); // 인덱스 가져오기
			
			String sqlReser = "select * from reservation";			
			PreparedStatement pstmtReser = conn.prepareStatement(sqlReser);			
			ResultSet rs2 = pstmtReser.executeQuery();
			System.out.println("[reservation 테이블 데이터]");
			while(rs2.next()) {
				ReservationTbl reser = new ReservationTbl();
				reser.setReserNo(rs2.getInt("reser_no"));
				reser.setRoomNo(rs2.getInt("room_no"));
				reser.setUserId(rs2.getString("user_id"));
				reser.setCheckIn(rs2.getDate("check_in"));
				reser.setCheckOut(rs2.getDate("check_out"));
				reser.setTeamNum(rs2.getInt("team_num"));
				rserTblList.add(reser);
				
				System.out.println(reser);
			}
			//System.out.println("88"+rserTblList.get(0)); // 인덱스 가져오기
			
			rs.close();
			rs2.close();
			
			pstmtUser.close();
			pstmtReser.close();
			
			
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

	public static void main(String[] args) {
		Connection cn = new Connection();
	}

}
