package ppoy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

class ImageWindow extends JFrame {
	public ImageWindow(ImageIcon imageIcon) {
		// 새창 생성
		JFrame frame = new JFrame("상세 화면");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
		// 이미지 표시
		JLabel label = new JLabel(imageIcon);
		frame.add(label);

		// 창크기 조절 및 가운데 정렬
		frame.pack();
		frame.setLocationRelativeTo(null);

		// 창 표시
		frame.setVisible(true);

		// 창크기 고정
		frame.setResizable(false);
	}
}

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
	// Date date;

	JButton btnBefore, btnAfter;
	JButton btnAdd, btnDel;
	JButton btnRoom1, btnRoom2, btnRoom3;
	JButton myPage, btnLogout;
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

	String checkIn = "";
	String checkOut = "";

	public HomePage(String id) {
		setResizable(false);// 창크기 고정
		today = Calendar.getInstance(); // 디폴트의 타임 존 및 로케일을 사용해 달력을 가져옵니다.
		cal = new GregorianCalendar(); // Calendar 구상 서브 클래스

		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH) + 1;// 1월의 값이 0

		// panNorth : 이전 월, 연 월 표시, 다음 월
		panNorth = new JPanel();

		// test
		JPanel blankPan = new JPanel();
		blankPan.setPreferredSize(new Dimension(160, 50));
		blankPan.setBackground(Color.white);
		panNorth.add(blankPan);

		panNorth.add(btnBefore = new JButton("  ◀  "));
		panNorth.add(txtYear = new JTextField(year + "년 "));
		panNorth.add(txtMonth = new JTextField(month + "월"));
		panNorth.add(btnAfter = new JButton("  ▶  "));
		panNorth.setBackground(Color.WHITE);
		txtYear.setEnabled(false); // 이벤트 막음
		txtYear.setBorder(null);
		txtMonth.setEnabled(false); // 이벤트 막음
		txtMonth.setBorder(null);

		// 위치 조정
		JPanel blankPan2 = new JPanel();
		blankPan2.setPreferredSize(new Dimension(175, 50));
		blankPan2.setBackground(Color.white);
		panNorth.add(blankPan2);

		panNorth.add(myPage = new JButton(" INFO  "));
		// 이미지 크기 조절
		ImageIcon icon = new ImageIcon(getClass().getResource("/img/user.png"));
		Image img = icon.getImage();
		Image changeImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		myPage.setIcon(changeIcon);
		
		myPage.setHorizontalTextPosition(JLabel.LEFT);
		panNorth.add(btnLogout = new JButton(" LOGOUT  "));

		// 이미지 크기 조절
		ImageIcon icon2 = new ImageIcon(getClass().getResource("/img/logout.png"));
		Image img2 = icon2.getImage();
		Image changeImg2 = img2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon changeIcon2 = new ImageIcon(changeImg2);

		btnLogout.setIcon(changeIcon2);
		btnLogout.setHorizontalTextPosition(JLabel.LEFT);

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

		btnLogout.setForeground(new Color(67, 117, 218));
		btnLogout.setBackground(new Color(255, 255, 255));
		btnLogout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginProject loginPage = new LoginProject();

				loginPage.setFrame(loginPage);
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

		myPage.setFont(f2);
		btnLogout.setFont(f2);

		getContentPane().add(panNorth, "North");
		setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());


		// panCenter : 날짜, 요일 버튼
		// 달력에 날에 해당하는 부분
		panCenter = new JPanel(new GridLayout(7, 7));// 격자나,눈금형태의 배치관리자
		gridInit(); // 49개의 버튼 생성하는 메소드
		calSet(); // 달력 버튼 표시
		hideInit(); // 버튼이 없는 곳은 비활성화
		getContentPane().add(panCenter, "Center");
		panCenter.setBackground(Color.WHITE);
		panCenter.setBorder(new EmptyBorder(10, 10, 10, 10));

		// panSouth : 메모 만들기
		panSouth = new JPanel();
		panSouth.setBorder(new EmptyBorder(0, 0, 5, 0));
		panSouth.add(time = new JLabel("CHECK▶"));
		time.setBounds(100, 50, 70, 60);
		panSouth.add(txtWrite = new JTextArea());
		txtWrite.setPreferredSize(new Dimension(900, 100));
		txtWrite.setEditable(false);
		
		// 메모를 입력받을 텍스트 박스를 가로 200 세로 50에 생성
		txtWrite.setFont(f2);
		getContentPane().add(panSouth, BorderLayout.SOUTH);
		txtWrite.setBackground(new Color(180, 201, 239));
		panSouth.setBackground(Color.WHITE);

		// panEast : 방 정보
		panEast = new JPanel();
		JLabel jLabel1 = new JLabel();
		JLabel jLabel2 = new JLabel();

		jLabel1.setIcon(new ImageIcon(getClass().getResource("/img/room1.jpg")));
		jLabel2.setIcon(new ImageIcon(getClass().getResource("/img/room2.jpg")));

		// 이미지 새창띄우기
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/room1_big.jpg"));

		// 라벨 생성
		JLabel label = jLabel1;

		// 라벨에 리스너 등록
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 이미지 클릭 시 새창 띄우기
				ImageWindow imageWindow = new ImageWindow(imageIcon);
			}
		});

		ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/img/room2_big.jpg"));

		// 라벨 생성
		JLabel label2 = jLabel2;

		// 라벨에 리스너 등록
		label2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 이미지 클릭 시 새창 띄우기
				ImageWindow imageWindow = new ImageWindow(imageIcon2);
			}
		});
		panEast.setLayout(new GridLayout(5, 1, 0, 0));

		panEast.add(jLabel1);
		panEast.add(new JLabel("<html>" + "<p style = 'font-family:맑은 고딕', 'font-size:10px'>"
				+ "<span style = 'font-size:11px; font-weight:bold;'>" + "<span style = 'color:red; '>[★연박불가★]</span>"
				+ " economy(Room 1)</span>" + "<br> 누우면 잠이 솔솔 오는 아늑한 방.</p> " + "</html>"));
		panEast.add(jLabel2);
		panEast.add(new JLabel("<html>" + "<p style = 'font-family:맑은 고딕', 'font-size:10px'>"
				+ "<span style = 'font-size:11px; font-weight:bold;'>" + "<span style = 'color:red;'>[★연박불가★]</span>"
				+ " VVIP(Room 2)</span>" + "<br> 럭셔리하고 세련된 풀옵션 오션 뷰 룸.</p>" + "</html>"));
		panEast.add(btnRoom1 = new RoundButton("예약 하기"));

		btnRoom1.setForeground(Color.white);
		btnRoom1.setBackground(new Color(100, 149, 237));
		btnRoom1.setFont(f3);
		panEast.setBorder(new EmptyBorder(10, 0, 10, 15));

		btnRoom1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Reservation reserPage = new Reservation(id);
				reserPage.setVisible(true);

			}
		});


		getContentPane().add(panEast, BorderLayout.EAST);
		panEast.setBackground(Color.WHITE);

		btnBefore.addActionListener(this);
		btnAfter.addActionListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("예약 현황");

		setSize(1000, 600);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public void calSet() {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, (month - 1));
		cal.set(Calendar.DATE, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 요일 숫자로 반환 (일요일 : 0)


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

		// 해당 달의 첫 시작 요일 맞춤(빈칸 채움)
		hopping = j;
		for (int kk = 0; kk < hopping; kk++) {
			calBtn[kk + 7].setText("");
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
			// 남은 빈칸 채우기
			calBtn[i + 6 + hopping].setText((i) + "");

		}

	}

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


		else if (Integer.parseInt(ae.getActionCommand()) >= 1 && Integer.parseInt(ae.getActionCommand()) <= 31) {
			day = Integer.parseInt(ae.getActionCommand());


			searchmemo();
			calSet();
		}

	}

	public void hideInit() {
		for (int i = 0; i < calBtn.length; i++) {
			if ((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false);//일이 찍히지 않은 나머지 버튼을 비활성화 시킨다.

		}
	}

	public void gridInit() {
		// 표 요일 출력 (버튼으로 출력됨)
		for (int i = 0; i < days.length; i++) {
			JButton th = new JButton(days[i]);
			th.setBackground(new Color(180, 201, 239)); // 버튼 배경색
			panCenter.add(calBtn[i] = th);
		}

		for (int i = days.length; i < 49; i++) {
			panCenter.add(calBtn[i] = new JButton(""));
			calBtn[i].addActionListener(this);
		}

	}

	public void panelInit() {
		GridLayout gridLayout1 = new GridLayout(7, 7);
		panCenter.setLayout(gridLayout1);
	}

	public void calInput(int gap) {
		month += (gap);
		if (month <= 0) {
			month = 12;
			year = year - 1;

		} else if (month >= 13) {
			month = 1;
			year = year + 1;
		}

	}

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

			sql = "update reservation " + "set check_in =?, " + "check_out = ?," + "where id = reser_no";
			stmt.executeUpdate(sql);

			stmt.close();
			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void del() {
		try {
			dbConnect();
			String clickDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
			sql = "delete from reservation where check_in='";
			sql += clickDate + "'";

			stmt.executeUpdate(sql);
			stmt.close();
			cn.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void searchmemo() { // 사용자가 선택한 날짜 정보
		try {
			dbConnect();


			String clickDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
			sql = "select * from reservation where check_in='";
			sql += clickDate + "'";

			rs = stmt.executeQuery(sql);
			String gettemp = "\n";

			while (rs.next()) {
				gettemp += " - " + rs.getString("memo") + "\n";
			}
			txtWrite.setText(gettemp);

			stmt.close();
			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkday() { // 이번달 전체 정보
		dbConnect();

		String clickDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(todays);
		sql = "select * from reservation where check_in='";
		sql += clickDate + "'";

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

	}

	public void dbConnect() {
		try {
			Class.forName(driver);
			cn = DriverManager.getConnection(url, user, pwd);
			stmt = cn.createStatement();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}