package ppoy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
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

import javax.swing.BorderFactory;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Reservation extends JFrame implements ActionListener {
	String loginUser;// 로그인 정보
	JFrame setCheckInOutDate;

	// DB연결
	ConnectionDB connDB;
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
			String roomNo = strRoomNo.equals("economy") ? "1" : "2";
			String teamNo = selectedNum.getSelectedItem().toString();

			if (strRoomNo.equals("")) {
				JOptionPane.showMessageDialog(null, "내용이 없습니다.");
				return;
			} else {
				sql = "insert into reservation(reser_no,room_no, user_id, check_in, check_out, team_num,memo) values (null,";
				sql += "" + roomNo + ",";
				sql += "'" + loginUser + "', ";
				sql += "'" + checkIn + "',";
				sql += "'" + checkOut + "',";
				sql += "" + teamNo + ",";
				sql += "'" + checkIn + " ~ " + checkOut + " : Room " + roomNo + "이 예약된 상태입니다.\n');";
				stmt.executeUpdate(sql);

				JOptionPane.showMessageDialog(null, "예약이 완료되었습니다.");
				HomePage hp = new HomePage(loginUser);
			}

			stmt.close();

			cn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Font fnt = new Font("맑은 고딕", Font.BOLD, 15);

	JPanel changePane = new JPanel();

	JLabel title = new JLabel("예약 페이지");

	// 이미지 크기 조절
	ImageIcon icon = new ImageIcon(getClass().getResource("/img/calendar.png"));
	Image img = icon.getImage();
	Image changeImg = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
	ImageIcon changeIcon = new ImageIcon(changeImg);

	JLabel startCalendar = new JLabel(changeIcon);

	JLabel checkInLbl = new JLabel("체크인");
	static JTextField checkInField = new JTextField();

	JLabel checkOutLbl = new JLabel("체크아웃");
	static JTextField checkOutField = new JTextField();

	JLabel roomNumLbl = new JLabel("방 선택");
	static String room[] = { "economy", "VVIP" };
	static DefaultComboBoxModel<String> roomModel = new DefaultComboBoxModel<String>(room);
	static JComboBox<String> roomNum = new JComboBox<String>(roomModel);
	static String cb = "economy";

	JLabel teamNumLbl = new JLabel("인원 수");
	static Integer num[] = { 1, 2, 3, 4, 5 };
	static DefaultComboBoxModel<Integer> numModel = new DefaultComboBoxModel<Integer>(num);
	static JComboBox<Integer> selectedNum = new JComboBox<Integer>(numModel);

	JButton btnNext = new RoundButton("예약하기");
	JButton btnBack = new RoundButton("취소");

	int calendarWindowTest = 0;
	int clickCheck = 0;
	static int teamNum;

	public Reservation(String loginUSerId) {
		checkInField.setText("");
		checkOutField.setText("");
		selectedNum.setSelectedIndex(0);
		roomNum.setSelectedIndex(0);

		connDB = new ConnectionDB();
		loginUser = loginUSerId;
		getContentPane().setLayout(new BorderLayout());
		checkInField.setEditable(false);
		checkOutField.setEditable(false);
		checkInField.setBackground(Color.white);
		checkOutField.setBackground(Color.white);

		setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
		setTitle("YangNolja");
		setResizable(false); // 창크기 고정

		getContentPane().add(changePane, "Center");
		changePane.setLayout(null);
		changePane.setBackground(new Color(255, 255, 255));

		int x = 200;
		int x1 = 400;
		int x2 = 500;
		int y = 80;
		int y1 = 155;
		int y2 = 280;

		changePane.add(title).setBounds(x, y, 200, 50);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 25));

		changePane.add(startCalendar).setBounds(750, y1 - 50, 50, 50);
		changePane.add(checkInLbl).setBounds(x, y1, 200, 49);
		changePane.add(checkInField).setBounds(x1, y1, 400, 49);
		checkInLbl.setFont(fnt);
		checkInField.setFont(fnt);
		checkInLbl.setOpaque(true);
		checkInLbl.setBackground(new Color(100, 149, 237));
		checkInLbl.setForeground(new Color(255, 255, 255));
		checkInLbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		checkInField.setBorder(new LineBorder(new Color(100, 149, 237)));

		changePane.add(checkOutLbl).setBounds(x, y1 + 55, 200, 49);
		changePane.add(checkOutField).setBounds(x1, y1 + 55, 400, 49);
		checkOutLbl.setFont(fnt);
		checkOutField.setFont(fnt);
		checkOutLbl.setOpaque(true);
		checkOutLbl.setBackground(new Color(100, 149, 237));
		checkOutLbl.setForeground(new Color(255, 255, 255));
		checkOutLbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		checkOutField.setBorder(new LineBorder(new Color(100, 149, 237)));

		changePane.add(roomNumLbl).setBounds(x, y2, 300, 50);
		changePane.add(teamNumLbl).setBounds(x2, y2, 300, 50);
		changePane.add(roomNum).setBounds(x, y2 + 50, 300, 50);
		changePane.add(selectedNum).setBounds(x2, y2 + 50, 300, 50);
		roomNumLbl.setFont(fnt);
		teamNumLbl.setFont(fnt);
		roomNum.setFont(fnt);
		selectedNum.setFont(fnt);
		roomNum.setForeground(new Color(130, 151, 189));
		roomNum.setBackground(new Color(255, 255, 255));
		selectedNum.setForeground(new Color(130, 151, 189));
		selectedNum.setBackground(new Color(255, 255, 255));

		changePane.add(btnNext).setBounds(x2 + 100, y + 350, 200, 50);
		btnNext.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btnNext.setBackground(new Color(100, 149, 237));
		btnNext.setForeground(new Color(255, 255, 255));

		changePane.add(btnBack).setBounds(x, y + 350, 200, 50);
		btnBack.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btnBack.setBackground(new Color(100, 149, 237));
		btnBack.setForeground(new Color(255, 255, 255));

		setBackground(Color.white);
		setSize(1000, 600);

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
					setCheckInOutDate = new CustomCalendar();
					calendarWindowTest = 1;
				} else if (calendarWindowTest == 1) {
					calendarWindowTest = 0;
					clickCheck = 0;
					checkInField.setText("");
					checkOutField.setText("");
				}
			}
		});

		btnNext.addActionListener(this);
		btnBack.addActionListener(this);
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
				} else if (dayOverCheck() == 1) {
					JOptionPane.showMessageDialog(this, "연박 불가능합니다. 다시 예약해주세요.");
				} else if (doubleCheck(checkInField.getText(), roomNum.getSelectedIndex() + 1) == 1) {
					JOptionPane.showMessageDialog(this, "이미 예약된 날짜입니다. 다시 예약해주세요.");
				} else {
					saveInfo();
					this.setVisible(false);
					setCheckInOutDate.setVisible(false);

				}
			} else if (btn.equals("취소")) {
				this.setVisible(false);
				if (setCheckInOutDate != null) {
					setCheckInOutDate.setVisible(false);
				}

			}
		} else if (obj instanceof JComboBox) {
			cb = (String) ae.getActionCommand();
		}
	}

	public int dayMinusCheck() { // 체크인 날짜가 체크아웃날짜 이후의 날짜인지 확인
		int result = 0;
		int checkIn = Integer.valueOf(checkInField.getText().replace("/", ""));
		int checkOut = Integer.valueOf(checkOutField.getText().replace("/", ""));
		int minusCheck = checkOut - checkIn;
		if (minusCheck < 0) {
			result = 1;
		}
		return result;
	}

	public int dayOverCheck() {
		int result = 0;
		int checkIn = Integer.valueOf(checkInField.getText().replace("/", ""));
		int checkOut = Integer.valueOf(checkOutField.getText().replace("/", ""));
		int minusCheck = checkOut - checkIn;
		if (minusCheck > 1) {
			result = 1;
		}
		return result;
	}

	public int errorCheck() { // 체크인 날짜가 오늘 이전의 날짜인지 확인
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

	public int doubleCheck(String checkinDate, int roomNumber) { // 중복 예약인지 확인
		int result = 0;
		String ci = checkinDate.replace("/", "-");

		String dbCI = "";
		int dbRN = 0;
		for (int i = 0; i < connDB.reserTblListAll.size(); i++) {
			dbCI = connDB.reserTblListAll.get(i).getCheckIn().toString();
			dbRN = connDB.reserTblListAll.get(i).getRoomNo();

			if (dbCI.equals(ci) && roomNumber == dbRN) {
				result = 1;
				break;
			}
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

		// 요일 입력
		String dw[] = { "일", "월", "화", "수", "목", "금", "토" };

		Calendar now = Calendar.getInstance();
		int year, month, date;

		public CustomCalendar() {
			setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
			setTitle("날짜 지정");
			cntCenter.setOpaque(true);
			cntCenter.setBackground(new Color(255, 255, 255));

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Point centerPoint = ge.getCenterPoint();
			int leftTopX = (centerPoint.x - getWidth() / 2) + 300;
			int leftTopY = (centerPoint.y - getHeight() / 2) - 150;
			this.setLocation(leftTopX, leftTopY);

			year = now.get(Calendar.YEAR);
			month = now.get(Calendar.MONTH) + 1; //
			date = now.get(Calendar.DATE);
			for (int i = year; i <= year + 50; i++) {
				yearModel.addElement(i);
			}
			for (int i = 1; i <= 12; i++) {
				monthModel.addElement(i);
			}

			bar.setFont(new Font("맑은 고딕", Font.BOLD, 25));
			yearCombo.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			monthCombo.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			yearCombo.setOpaque(true);
			yearCombo.setBackground(new Color(255, 255, 255));
			monthCombo.setOpaque(true);
			monthCombo.setBackground(new Color(255, 255, 255));

			add(bar, "North");
			bar.setLayout(new FlowLayout());
			bar.setSize(300, 400);
			bar.add(lastMonth);

			// 달력
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

			// 중앙상단
			center.add(cntNorth, "North");
			for (int i = 0; i < dw.length; i++) {
				JLabel dayOfWeek = new JLabel(dw[i], JLabel.CENTER);
				if (i == 0)
					dayOfWeek.setForeground(Color.red);
				else if (i == 6)
					dayOfWeek.setForeground(Color.blue);
				cntNorth.add(dayOfWeek);
				dayOfWeek.setFont(new Font("맑은 고딕", Font.BOLD, 15));
				dayOfWeek.setOpaque(true);
				dayOfWeek.setBackground(new Color(255, 255, 255));
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

		// 날짜출력
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

		public void windowClosed(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

	}

}
