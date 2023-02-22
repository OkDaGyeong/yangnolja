package ppoy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class HomePage extends JFrame implements ActionListener

{
	Connection cn = null;

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://222.119.100.81:3382/ppoy";
	String user = "ppoy";
	String pwd = "ppoy";

	Statement stmt = null;
	ResultSet rs = null;
	String sql, tempmemo;

	String[] days = { "일", "월", "화", "수", "목", "금", "토" };
	int year, month, day, todays, memoday = 0;

	Font f;
	Font f2;
	Font f3;
	Color bc, fc;
	Calendar today;
	Calendar cal;

	JButton btnBefore, btnAfter;
	JButton btnAdd, btnDel;
	JButton btnRoom1, btnRoom2, btnRoom3;
	JButton myPage;
	JButton[] calBtn = new JButton[49];

	JLabel thing;
	JLabel time;
	JPanel panWest;
	JPanel panEast;
	JPanel panNorth;
	JPanel panSouth;
	JPanel panCenter;

	JTextField txtMonth, txtYear;
	JTextArea txtWrite;
	JTextField txtTime;

	public HomePage(String id) {

		today = Calendar.getInstance(); // 디폴트의 타임 존 및 로케일을 사용해 달력을 가져옵니다.
		cal = new GregorianCalendar();

		/*
		 * GregorianCalendar 는,Calendar 의 구상 서브 클래스이며, 세계의 대부분의 지역에서 사용되는 표준적인 달력 시스템을
		 * 제공합니다.
		 */

		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;// 1월의 값이 0

		// panNorth : 이전 월, 연 월 표시, 다음 월
		panNorth = new JPanel();
		panNorth.add(btnBefore = new JButton("  ◀  "));
		panNorth.add(txtYear = new JTextField(year + "년 "));
		panNorth.add(txtMonth = new JTextField(month + "월"));
		panNorth.add(btnAfter = new JButton("  ▶  "));
		panNorth.setBackground(Color.WHITE);
		txtYear.setEnabled(false); // 이벤트 막음
		txtYear.setBorder(null);
		txtMonth.setEnabled(false); // 이벤트 막음
		txtMonth.setBorder(null);
		panNorth.add(btnAdd = new JButton("예약 추가"));
		panNorth.add(btnDel = new JButton("예약 삭제"));
		panNorth.add(myPage = new JButton(id + "님 PAGE"));

		btnAdd.setForeground(new Color(224, 255, 255));
		btnAdd.setBackground(new Color(100, 149, 237));
		
		btnDel.setForeground(new Color(224, 255, 255));
		btnDel.setBackground(new Color(100, 149, 237));
		
		
		btnAfter.setForeground(new Color(67, 116, 217));
		btnAfter.setBackground(new Color(255, 255, 255));
		btnAfter.setBorder(null);
		
		btnBefore.setForeground(new Color(67, 116, 217));
		btnBefore.setBackground(new Color(255, 255, 255));
		btnBefore.setBorder(null);
		
		myPage.setForeground(new Color(67, 117, 218));
		myPage.setBackground(new Color(255, 255, 255));
		myPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MyPage myPage = new MyPage(id);
				myPage.setVisible(true);
				dispose();
			}
		});

		// Font 설정하기
		f = new Font("맑은 고딕", Font.BOLD, 40);
		f2 = new Font("맑은 고딕", Font.BOLD, 15);
		f3 = new Font("맑은 고딕", Font.BOLD, 25);
		txtYear.setFont(f);
		txtMonth.setFont(f);
		btnAfter.setFont(f3);
		btnBefore.setFont(f3);
		btnAdd.setFont(f2);
		btnDel.setFont(f2);
		myPage.setFont(f2);

		getContentPane().add(panNorth, "North");

		/*
		 * HomePage라는 큰놈 위에 레이아웃을 동,서,남,북으로 나눠서 패널을 하나 하나 올려 놓는 형식이다. 메인보드 위에 부품이 하나 하나
		 * 조립되듯.....
		 */

		// panCenter : 날짜, 요일 버튼
		// 달력에 날에 해당하는 부분
		panCenter = new JPanel(new GridLayout(7, 7));// 격자나,눈금형태의 배치관리자
		gridInit(); // 49개의 버튼 생성하는 메소드
		calSet(); // 달력 버튼 표시
		hideInit(); // 버튼이 없는 곳은 비활성화
		getContentPane().add(panCenter, "Center");
		panCenter.setBackground(Color.WHITE);

		// panSouth : 메모 만들기
		panSouth = new JPanel();

		panSouth.add(time = new JLabel("CHECK▶"));
		time.setBounds(100, 50, 70, 60);
		panSouth.add(txtWrite = new JTextArea());
		txtWrite.setPreferredSize(new Dimension(900, 100));
		// 메모를 입력받을 텍스트 박스를 가로 200 세로 50에 생성
		getContentPane().add(panSouth, BorderLayout.SOUTH);

		txtWrite.setBackground(new Color(100, 149, 237));
		panSouth.setBackground(Color.WHITE);

		// panEast : 방 만들기
		panEast = new JPanel();
		JLabel jLabel1 = new JLabel();
		JLabel jLabel2 = new JLabel();
		panEast.setLayout(new GridLayout(5, 1, 0, 0));

		jLabel1.setIcon(new ImageIcon(getClass().getResource("/img/room1.jpg")));
		jLabel2.setIcon(new ImageIcon(getClass().getResource("/img/room2.jpg")));
		
		panEast.add(jLabel1);
		panEast.add(new JLabel("economy"));
		panEast.add(jLabel2);
		panEast.add(new JLabel("VVIP"));
		
		panEast.add(btnRoom1 = new RoundButton("예약 하기"));		
		btnRoom1.setForeground(Color.white);
		btnRoom1.setBackground(new Color(100, 149, 237));
		btnRoom1.setFont(f2);
		btnRoom1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Reservation reserPage = new Reservation(id);
				reserPage.setVisible(true);
				
				
			}
		});
		
		/*
		panEast.add(btnRoom1 = new RoundButton("economy 예약"));
		btnRoom1.setForeground(new Color(224, 255, 255));
		btnRoom1.setBackground(new Color(100, 149, 237));
		
		panEast.add(btnRoom2 = new RoundButton("VVIP 예약"));
		btnRoom2.setForeground(new Color(224, 255, 255));
		btnRoom2.setBackground(new Color(100, 149, 237));

		btnRoom1.setFont(f2);
		btnRoom2.setFont(f2);*/

		getContentPane().add(panEast, BorderLayout.EAST);
		panEast.setBackground(Color.WHITE);

		btnBefore.addActionListener(this);
		btnAfter.addActionListener(this);
		btnAdd.addActionListener(this);
		btnDel.addActionListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// *************
		setTitle("예약 현황");// *********************
		// setBounds(500,300,600,500);
		setSize(1000, 600);
		setVisible(true);
		setLocationRelativeTo(null);
	}// end constuctor

	public void calSet() {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, (month - 1));
		cal.set(Calendar.DATE, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		/*
		 * get 및 set 를 위한 필드치로, 요일을 나타냅니다. 이 필드의 값은,SUNDAY,MONDAY,TUESDAY,WEDNESDAY
		 * ,THURSDAY,FRIDAY, 및 SATURDAY 가 됩니다. get()메소드의 의해 요일이 숫자로 반환
		 */

		int j = 0;
		int hopping = 0;
		calBtn[0].setForeground(new Color(255, 0, 0));// 일요일 "일" - 빨간색
		calBtn[6].setForeground(new Color(0, 0, 255));// 토요일 "토" - 파란색

		for (int i = 0; i < 7; i++) {
			calBtn[i].setFont(f2);
		}

		for (int i = cal.getFirstDayOfWeek(); i < dayOfWeek; i++) {
			j++;
		}
		/*
		 * 일요일부터 그달의 첫시작 요일까지 빈칸으로 셋팅하기 위해
		 */
		hopping = j;
		for (int kk = 0; kk < hopping; kk++) {
			calBtn[kk + 7].setText("");
			// calBtn[kk+7].setBackground(new Color(255, 255, 255));
		}

		for (int i = cal.getMinimum(Calendar.DAY_OF_MONTH); i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++) {
			cal.set(Calendar.DATE, i);
			calBtn[i + 6 + hopping].setFont(f2);

			if (cal.get(Calendar.MONTH) != month - 1) {
				break;
			}

			todays = i;
			checkday();
			if (memoday == 1) {
				calBtn[i + 6 + hopping].setForeground(new Color(123, 120, 202));
				calBtn[i + 6 + hopping].setBackground(new Color(230, 230, 245));
			}

			else {
				calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 0));
				calBtn[i + 6 + hopping].setBackground(new Color(255, 255, 255));

				if ((i + hopping - 1) % 7 == 0) {// 일요일
					calBtn[i + 6 + hopping].setForeground(new Color(255, 0, 0));
				}
				if ((i + hopping) % 7 == 0) {// 토요일
					calBtn[i + 6 + hopping].setForeground(new Color(0, 0, 255));
				}
			}
			/*
			 * 요일을 찍은 다음부터 계산해야 하니 요일을 찍은 버튼의 갯수를 더하고 인덱스가 0부터 시작이니 -1을 해준 값으로 연산을 해주고 버튼의
			 * 색깔을 변경해준다.
			 */

			calBtn[i + 6 + hopping].setText((i) + "");

		} // for

	}// end Calset()

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnBefore) {
			this.panCenter.removeAll();
			calInput(-1);
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year + "년");
			this.txtMonth.setText(month + "월");

		}

		else if (ae.getSource() == btnAfter) {
			this.panCenter.removeAll();
			calInput(1);
			gridInit();
			panelInit();
			calSet();
			hideInit();

			this.txtYear.setText(year + "년");
			this.txtMonth.setText(month + "월");
		}

		else if (ae.getSource() == btnAdd) {
			add();
			calSet();
			txtWrite.setText("");

		}

		else if (ae.getSource() == btnDel) {
			del();
			calSet();
			txtWrite.setText("");
		}

		else if (Integer.parseInt(ae.getActionCommand()) >= 1 && Integer.parseInt(ae.getActionCommand()) <= 31) {
			day = Integer.parseInt(ae.getActionCommand());

			// 버튼의 밸류 즉 1,2,3.... 문자를 정수형으로 변환하여 클릭한 날짜를 바꿔준다.

//                    System.out.println(day);
			searchmemo();
			calSet();
		}

	}// end actionperformed()

	public void hideInit() {
		for (int i = 0; i < calBtn.length; i++) {
			if ((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false);
//calBtn[i].setBackground(new Color(255, 255, 255));

//일이 찍히지 않은 나머지 버튼을 비활성화 시킨다.
		} // end for
	}// end hideInit()

//     public void separate(){

	public void gridInit() {
		// jPanel3에 버튼 붙이기
		// 표 요일 출력 (버튼으로 출력됨)
		for (int i = 0; i < days.length; i++) {
			JButton th = new JButton(days[i]);
			th.setBackground(new Color(180,201,239)); //버튼 배경색
			//th.setEnabled(false);
			panCenter.add(calBtn[i] = th );
		}
			

		for (int i = days.length; i < 49; i++) {
			panCenter.add(calBtn[i] = new JButton(""));
			calBtn[i].addActionListener(this);
		}

	}// end gridInit()

	public void panelInit() {
		GridLayout gridLayout1 = new GridLayout(7, 7);
		panCenter.setLayout(gridLayout1);
	}// end panelInit()

	public void calInput(int gap) {
		month += (gap);
		if (month <= 0) {
			month = 12;
			year = year - 1;

		} else if (month >= 13) {
			month = 1;
			year = year + 1;
		}

	}// end calInput()

	public void add() {
		try {
			String temp = txtWrite.getText();
			dbConnect();

			if (temp.equals("")) {
				JOptionPane.showMessageDialog(null, "내용이 없습니다.");
				return;
			} else {
				JOptionPane.showMessageDialog(null, "입력되었습니다.");
			}

			sql = "insert into memo (memo, year, month, day) values (";
			sql += "'" + temp + "',";
			sql += "" + year + ",";
			sql += "" + month + ",";
			sql += "" + day + ");";
			stmt.executeUpdate(sql);

			stmt.close();
			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end add()

	public void del() {
		try {
			dbConnect();

			sql = "delete from memo where year=";
			sql += year + " and month=";
			sql += month + " and day=";
			sql += +day + ";";

			stmt.executeUpdate(sql);
			stmt.close();
			cn.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}// end del();

	public void searchmemo() {
		try {
			dbConnect();

			sql = "select memo from memo where year=";
			sql += year + " and month=";
			sql += month + " and day=";
			sql += "" + day + ";";

			rs = stmt.executeQuery(sql);
			String gettemp = "";

			while (rs.next()) {
				gettemp += rs.getString("memo") + "  ";
			}
			txtWrite.setText(gettemp);

			stmt.close();
			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end searchmemo()

	public void checkday() {
		dbConnect();

		sql = "select * from memo where year=";
		sql += year + " and month=";
		sql += month + " and day=";
		sql += "" + todays + ";";

		try {
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				memoday = 1;
			} else
				memoday = 0;

			stmt.close();
			cn.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}// end checkday()

	public void dbConnect() {
		try {
			Class.forName(driver);
			cn = DriverManager.getConnection(url, user, pwd);
			stmt = cn.createStatement();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}// end class
