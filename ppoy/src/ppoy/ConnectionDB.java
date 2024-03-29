package ppoy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectionDB {
	
	Connection conn = null;
	Statement stmt = null;
	String driver = "com.mysql.cj.jdbc.Driver";
	
	String url = "jdbc:mysql://222.119.100.81:3382/ppoy";
	String user = "ppoy";
	String pwd = "ppoy";
	
	ArrayList<Users> userTblList = new ArrayList<>(); //로그인 유저 정보
	ArrayList<ReservationTbl> reserTblList = new ArrayList<>(); //로그인한 유저의 예약정보
	ArrayList<ReservationTbl> reserTblListAll = new ArrayList<>(); // 모든 유저의 예약정보
	
	String loginUserName="";
	public void dbconnect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
			stmt = conn.createStatement();
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public ConnectionDB() {
		try {
			//JDBC Driver 등록
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//연결하기
			conn = DriverManager.getConnection(
				"jdbc:mysql://222.119.100.81:3382/ppoy", 
				"ppoy", 
				"ppoy"
			);
			
			//reserTblListAll
			String sqlReserAll = "select room_no, check_in, reser_no from reservation;";	
		
			PreparedStatement pstmtReserAll = conn.prepareStatement(sqlReserAll);			
			ResultSet rs3 = pstmtReserAll.executeQuery();
			//System.out.println("[reservation 테이블 데이터]");
			while(rs3.next()) {
				ReservationTbl reserAll = new ReservationTbl();
				reserAll.setRoomNo(rs3.getInt("room_no"));
				reserAll.setCheckIn(rs3.getDate("check_in"));
				reserAll.setReserNo(rs3.getInt("reser_no"));
				reserTblListAll.add(reserAll);
				
				//System.out.println("all:"+reserAll);
			}
			
			rs3.close();
			pstmtReserAll.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ConnectionDB(String loginId) {
		try {
			//JDBC Driver 등록
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//연결하기
			conn = DriverManager.getConnection(
				"jdbc:mysql://222.119.100.81:3382/ppoy", 
				"ppoy", 
				"ppoy"
			);	
			
			///System.out.println("연결 성공");
			
			//SQL 작성
			String sqlUsers = "select * from users where user_id='"+loginId+"'";
			//PreparedStatement 얻기 및 값 지정
			PreparedStatement pstmtUser = conn.prepareStatement(sqlUsers);			
			//SQL실행 후 ResultSet을 통해 데이터 읽기
			ResultSet rs = pstmtUser.executeQuery();
			//System.out.println("[user 테이블 데이터]");
			while(rs.next()) {
				Users user = new Users();
				user.setUserId(rs.getString("user_Id"));
				user.setUserName(rs.getString("user_name"));
				user.setUserPw(rs.getString("user_pw"));
				user.setUserTel(rs.getString("user_tel"));
				userTblList.add(user);
				loginUserName=rs.getString("user_name");
				//System.out.println(user);
			}
			
			
			String sqlReser = "select * from reservation where user_id = '"+loginId+"' order by check_in;";	
			//System.out.println("login : " +loginId);
			PreparedStatement pstmtReser = conn.prepareStatement(sqlReser);			
			ResultSet rs2 = pstmtReser.executeQuery();
			//System.out.println("[reservation 테이블 데이터]");
			while(rs2.next()) {
				ReservationTbl reser = new ReservationTbl();
				reser.setReserNo(rs2.getInt("reser_no"));
				reser.setRoomNo(rs2.getInt("room_no"));
				reser.setUserId(rs2.getString("user_id"));
				reser.setCheckIn(rs2.getDate("check_in"));
				reser.setCheckOut(rs2.getDate("check_out"));
				reser.setTeamNum(rs2.getInt("team_num"));
				reserTblList.add(reser);
				
				//System.out.println(reser);
			}
			
			
			
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
					
					//System.out.println("연결 끊기");
				} catch (SQLException e) {}
			}
		}
	}


}
