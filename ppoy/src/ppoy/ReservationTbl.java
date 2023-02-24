package ppoy;

import java.util.Date;

public class ReservationTbl {
	
	private int reserNo; // 예약번호
	private int roomNo; // 방번호
	private String userId; //사용자 아이디(이메일)
	private Date checkIn; //체크인 날짜
	private Date checkOut; // 체크아웃 날짜
	private int teamNum; // 인원 수 
	
	//Getter
	public int getReserNo() {
		return reserNo;
	}
	public int getRoomNo() {
		return roomNo;
	}
	public String getUserId() {
		return userId;
	}
	public Date getCheckIn() {
		return checkIn;
	}
	public Date getCheckOut() {
		return checkOut;
	}
	public int getTeamNum() {
		return teamNum;
	}
	
	//Setter
	public void setReserNo(int reserNo) {
		this.reserNo = reserNo;
	}
	public void setRoomNo(int roomNo) {
		this.roomNo = roomNo;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}
	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}
	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}
	
	// toString
	@Override
	public String toString() {
		return "ReservationTbl [reserNo=" + reserNo + ", roomNo=" + roomNo + ", userId=" + userId + ", checkIn="
				+ checkIn + ", checkOut=" + checkOut + ", teamNum=" + teamNum + "]";
	}

	
	
	
}
