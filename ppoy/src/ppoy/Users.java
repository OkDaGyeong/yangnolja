package ppoy;

public class Users {

	private String userId;
	private String userName;
	private String userPw;
	private String userTel;
	
	//Getter
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserPw() {
		return userPw;
	}
	public String getUserTel() {
		return userTel;
	}
	
	//Setter
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	
	//toString
	@Override
	public String toString() {
		return "Users [userId=" + userId + ", userName=" + userName + ", userPw=" + userPw + ", userTel=" + userTel + "]";
	}
	

}
