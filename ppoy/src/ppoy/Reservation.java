package ppoy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Reservation extends JFrame implements ActionListener {
	String loginUser;
//DB연결//////////////////////////////////////////////////////

	Connection cn = null;

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://222.119.100.81:3382/ppoy";
	String user = "ppoy";
	String pwd = "ppoy";

	Statement stmt = null;
	ResultSet rs = null;
	String sql, tempmemo;

	public void dbConnect() {
		try {

			Class.forName(driver);

			cn = DriverManager.getConnection(url, user, pwd);

			stmt = cn.createStatement();

		} catch (Exception ex) {

			ex.printStackTrace();

		}
	}

	public void saveInfo() {
		try {
			dbConnect();

			String checkIn = checkInField.getText();
			String checkOut = checkOutField.getText();
			String strRoomNo = roomNum.getSelectedItem().toString();
			String roomNo = strRoomNo.equals("economy")?"1":"2";
			String teamNo = num2.getSelectedItem().toString();

			if (strRoomNo.equals("")) {
				JOptionPane.showMessageDialog(null, "내용이 없습니다.");
				return;
			} else {
				sql = "insert into reservation(reser_no,room_no, user_id, check_in, check_out, team_num) values (null,";
				sql += "" + roomNo + ",";
				sql += "'"+loginUser+"', ";
				sql += "'" + checkIn + "',";
				sql += "'" + checkOut + "',";
				sql += "" + teamNo + ");";
				stmt.executeUpdate(sql);
				
				JOptionPane.showMessageDialog(null, "예약이 완료되었습니다.");
				System.out.println("예약완료");
			}
			

			stmt.close();

			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////

	Font fnt = new Font("맑은 고딕", Font.BOLD, 15);

	JPanel changePane = new JPanel();

	JLabel title = new JLabel("예약 페이지");

	ImageIcon icon = new ImageIcon("img/calendar.png");
	JLabel startCalendar = new JLabel(icon);

	JLabel checkInLbl = new JLabel("체크인");
	static JTextField checkInField = new JTextField();
	JLabel checkOutLbl = new JLabel("체크아웃");
	static JTextField checkOutField = new JTextField();

	JLabel roomNumLbl = new JLabel("selected room");
	static String room[] = { "economy", "VVIP" };
	static DefaultComboBoxModel<String> roomModel = new DefaultComboBoxModel<String>(room);
	static JComboBox<String> roomNum = new JComboBox<String>(roomModel);
	static String cb = "economy";

	JLabel teamNumLbl = new JLabel("인원수");
	static Integer num[] = { 1, 2, 3, 4, 5 };
	static DefaultComboBoxModel<Integer> numModel = new DefaultComboBoxModel<Integer>(num);
	static JComboBox<Integer> num2 = new JComboBox<Integer>(numModel);

	JButton btnnext = new RoundButton("예약하기");

	int calendarWindowTest = 0;
	int clickCheck = 0;
	static int teamNum;

	public Reservation(String loginUSerId) {
		loginUser =loginUSerId;
		getContentPane().setLayout(new BorderLayout());

		setTitle("YangNolja");

		getContentPane().add(changePane, "Center");
		changePane.setLayout(null);
		changePane.setBackground(new Color(255, 255, 255));

		int x = 200;
		int x1 = 400;
		int x2 = 500;

		changePane.add(title).setBounds(x, 150, 200, 50);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 25));

		changePane.add(startCalendar).setBounds(750, 230, 50, 50);
		changePane.add(checkInLbl).setBounds(x, 230, 200, 50);
		changePane.add(checkInField).setBounds(x1, 230, 350, 50);
		checkInLbl.setFont(fnt);
		checkInField.setFont(fnt);

		changePane.add(checkOutLbl).setBounds(x, 280, 200, 50);
		changePane.add(checkOutField).setBounds(x1, 280, 400, 50);
		checkOutLbl.setFont(fnt);
		checkOutField.setFont(fnt);

		changePane.add(roomNumLbl).setBounds(x, 350, 300, 50);
		changePane.add(teamNumLbl).setBounds(x2, 350, 300, 50);
		changePane.add(roomNum).setBounds(x, 400, 300, 50);
		changePane.add(num2).setBounds(x2, 400, 300, 50);
		roomNumLbl.setFont(fnt);
		teamNumLbl.setFont(fnt);
		roomNum.setFont(fnt);
		num2.setFont(fnt);

		changePane.add(btnnext).setBounds(x1, 520, 200, 50);
		btnnext.setFont(fnt);
		btnnext.setBackground(new Color(100, 149, 237));
		btnnext.setForeground(new Color(224, 255, 255));

		setBackground(Color.white);
		setSize(1000, 800);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - getWidth() / 2;
		int leftTopY = centerPoint.y - getHeight() / 2;
		this.setLocation(leftTopX, leftTopY);

		setVisible(true);

		startCalendar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				me.getSource();
				if (calendarWindowTest == 0) {
					new CustomCalendar();
					calendarWindowTest = 1;
				} else if (calendarWindowTest == 1) {
					calendarWindowTest = 0;
					clickCheck = 0;
					checkInField.setText("");
					checkOutField.setText("");
				}
			}
		});

		btnnext.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();

		if (obj instanceof JButton) {
			String btn = ae.getActionCommand();

			if (btn.equals("예약하기")) {
				if (checkInField.getText().equals("") || checkOutField.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "날짜를 선택하세요.");
				} else if (checkInField.getText().equals(checkOutField.getText())) {
					JOptionPane.showMessageDialog(this, "동일한 날짜 선택은 불가능합니다.");
				} else if (errorCheck() == 1) {
					JOptionPane.showMessageDialog(this, "당일 이전일은 선택 불가능합니다.");
				} else if (dayMinusCheck() == 1) {
					JOptionPane.showMessageDialog(this, "체크인보다 체크아웃 날짜가 이전일 수 없습니다.");
				} else {
					saveInfo();
					this.setVisible(false);
// Reservation.reservationCheck.setVisible(true); 다음페이지 연결?
// Reservation.centerPane.add(Reservation.reservationCheck);
				}
			}
		} else if (obj instanceof JComboBox) {
			cb = (String) ae.getActionCommand();
		}
	}

/// 출발 날짜가 도착 날짜보다 뒤로 설정해보는 엉뚱한 사람을 체크해라!
	public int dayMinusCheck() {
		int result = 0;
		int checkIn = Integer.valueOf(checkInField.getText().replace("/", ""));
		int checkOut = Integer.valueOf(checkOutField.getText().replace("/", ""));
		int minusCheck = checkOut - checkIn;
		if (minusCheck < 0) {
			result = 1;
		}
		return result;
	}

// 출발날짜 선택을 당일보다 전일로 설정 할 경우 걸러낸다
	public int errorCheck() {
		int result = 0;
		int checkIn = Integer.valueOf(checkInField.getText().replace("/", ""));
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		int fmt = Integer.valueOf(format.format(date));
		int dateCheck = fmt - checkIn;
		if (dateCheck > 0) {
			result = 1;
		}
		return result;
	}

	class CustomCalendar extends JFrame implements ActionListener, WindowListener {
// 상단 bar
		JPanel bar = new JPanel();
		JButton lastMonth = new RoundButton("◀");

		JComboBox<Integer> yearCombo = new JComboBox<Integer>();
		DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<Integer>();
		JLabel yLbl = new JLabel("년 ");

		JComboBox<Integer> monthCombo = new JComboBox<Integer>();
		DefaultComboBoxModel<Integer> monthModel = new DefaultComboBoxModel<Integer>();
		JLabel mLbl = new JLabel("월");
		JButton nextMonth = new RoundButton("▶");

// 중앙 날짜
		JPanel center = new JPanel(new BorderLayout());
// 중앙상단
		JPanel cntNorth = new JPanel(new GridLayout(0, 7));
// 정중앙
		JPanel cntCenter = new JPanel(new GridLayout(0, 7));
//요일 입력
		String dw[] = { "일", "월", "화", "수", "목", "금", "토" };

		Calendar now = Calendar.getInstance();
		int year, month, date;

		public CustomCalendar() {
			year = now.get(Calendar.YEAR);
			month = now.get(Calendar.MONTH) + 1; //
			date = now.get(Calendar.DATE);
			for (int i = year; i <= year + 50; i++) {
				yearModel.addElement(i);
			}
			for (int i = 1; i <= 12; i++) {
				monthModel.addElement(i);
			}

///프레임
// 상단
			add(bar, "North");
			bar.setLayout(new FlowLayout());
			bar.setSize(300, 400);
			bar.add(lastMonth);
///////////달력
			bar.add(yearCombo);
			yearCombo.setModel(yearModel);
			yearCombo.setSelectedItem(year);

			bar.add(yLbl);
			bar.add(monthCombo);
			monthCombo.setModel(monthModel);
			monthCombo.setSelectedItem(month);

			bar.add(mLbl);
			bar.add(nextMonth);
			bar.setBackground(new Color(100, 149, 237));
// 중앙지역
			add(center, "Center");
//중앙상단
			center.add(cntNorth, "North");
			for (int i = 0; i < dw.length; i++) {
				JLabel dayOfWeek = new JLabel(dw[i], JLabel.CENTER);
				if (i == 0)
					dayOfWeek.setForeground(Color.red);
				else if (i == 6)
					dayOfWeek.setForeground(Color.blue);
				cntNorth.add(dayOfWeek);
			}
// 정중앙
			center.add(cntCenter, "Center");
			dayPrint(year, month);

// 이벤트
			yearCombo.addActionListener(this);
			monthCombo.addActionListener(this);
			lastMonth.addActionListener(this);
			nextMonth.addActionListener(this);
			addWindowListener(this);

// frame 기본세팅
			setSize(400, 300);
			setVisible(true);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// setLocation(750,230);
		}

// 이벤트처리

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if (obj instanceof JButton) {
				JButton eventBtn = (JButton) obj;
				int yy = (Integer) yearCombo.getSelectedItem();
				int mm = (Integer) monthCombo.getSelectedItem();
				if (eventBtn.equals(lastMonth)) {
					if (mm == 1 && yy == year) {
					} else if (mm == 1) {
						yy--;
						mm = 12;
					} else {
						mm--;
					}
				} else if (eventBtn.equals(nextMonth)) {
					if (mm == 12) {
						yy++;
						mm = 1;
					} else {
						mm++;
					}
				}
				yearCombo.setSelectedItem(yy);
				monthCombo.setSelectedItem(mm);
			} else if (obj instanceof JComboBox) {
				createDayStart();
			}
		}

		private void createDayStart() {
			cntCenter.setVisible(false);
			cntCenter.removeAll();
			dayPrint((Integer) yearCombo.getSelectedItem(), (Integer) monthCombo.getSelectedItem());
			cntCenter.setVisible(true);
		}

//날짜출력
		public void dayPrint(int y, int m) {
			Calendar cal = Calendar.getInstance();
			cal.set(y, m - 1, 1);
			int week = cal.get(Calendar.DAY_OF_WEEK); // 1일에 대한 요일
			int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 1월에 대한 마지막 요일
			for (int i = 1; i < week; i++) {
				cntCenter.add(new JLabel(""));
			}
			for (int i = 0; i <= lastDate - 1; i++) {
				JLabel day = new JLabel();
				day.setHorizontalAlignment(JLabel.CENTER);
				if ((week + i) % 7 == 0) {
					cntCenter.add(day).setForeground(Color.blue);
					day.setText(1 + i + "");
				} else if ((week + i) % 7 == 1) {
					cntCenter.add(day).setForeground(Color.red);
					day.setText(1 + i + "");
				} else {
					cntCenter.add(day);
					day.setText(1 + i + "");
				}

				day.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						JLabel mouseClick = (JLabel) me.getSource();
						String str = mouseClick.getText();
						String y = "" + yearCombo.getSelectedItem();
						String m = "" + monthCombo.getSelectedItem();

// 받은 "요일"이 1자리면 앞에 0을 붙여 출력
						if (str.equals(""))
							;
						else if (str.length() == 1)
							str = "0" + str;

// 받은 월 :"
						if (m.length() == 1)
							m = "0" + m;

						if (clickCheck == 0) {
							checkInField.setText(y + "/" + m + "/" + str);
							checkInField.setEnabled(false);
							clickCheck++;
						} else if (clickCheck == 1) {
							checkOutField.setText(y + "/" + m + "/" + str);
							checkOutField.setEnabled(false);
							clickCheck--;
						}
					}
				});
			}
		}

		public void windowOpened(WindowEvent e) {
			calendarWindowTest = 1;
		}

		public void windowClosing(WindowEvent e) {
			calendarWindowTest = 1;
		}

		public void windowClosed(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}

	}

	/*public static void main(String[] args) {
		String id = "test";
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Reservation jFrame = new Reservation(id);
				jFrame.setVisible(true);
			}
		});

	}*/

}
