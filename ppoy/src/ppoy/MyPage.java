package ppoy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class MyPage extends JFrame {
	ConnectionDB cn = new ConnectionDB();
	private JTable reserTbl;
	
	JButton btnDelete = new RoundButton("예약 취소");
	//JButton btnUpdate = new RoundButton("예약 변경");
	JButton btnHome = new JButton("");
	
	
	private Font tHead = new Font("맑은 고딕", Font.BOLD, 23);
	private Font tpt = new Font("맑은 고딕", Font.PLAIN, 18);
	private Font pt = new Font("맑은 고딕", Font.PLAIN, 23); 
	
	int delNum = 0 ; // 삭제할 예약번호
	String sql =""; // sql문 
	
	Connection conn = null;
	Statement stmt = null;
	String driver = "com.mysql.cj.jdbc.Driver";	
	String url = "jdbc:mysql://222.119.100.81:3382/ppoy";
	String user = "ppoy";
	String pwd = "ppoy";
	
	//-- 출력할 정보
	String[] header = {"예약번호","방번호", "체크인 날짜","체크아웃 날짜","인원 수"};
	String[][] contents=null;
	DefaultTableModel reserTM = new DefaultTableModel(contents, header){
		public boolean isCellEditable(int rowIndex, int mCollndex) {
			return false; //셀 내용 수정 불가
		}
	};
	
	
	public MyPage() {
		getContentPane().setBackground(new Color(173, 216, 230));
		this.setTitle("MyPage");
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel PageName = new JLabel("My Page");
		PageName.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		PageName.setBounds(37, 25, 154, 56);
		getContentPane().add(PageName);

		btnDelete.setForeground(new Color(224, 255, 255));
		btnDelete.setBackground(new Color(100, 149, 237));
	
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(reserTbl.getSelectedRow() == -1) //선택안했을 때
				{
					return;
				}
				else
				{
					//JOptionPane.showConfirmDialog("선택한 예약을 삭제하시겠습니까?", e);
					delNum = (int) reserTbl.getValueAt(reserTbl.getSelectedRow(),0);
					delReser(delNum);
					reserTM.removeRow(reserTbl.getSelectedRow());
					
				}
				//delNum = Integer.parseInt(JOptionPane.showInputDialog("삭제할 예약번호를 입력하시오")); // 다이얼로그 띄우기

			}
		});
		
		btnDelete.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnDelete.setBounds(736, 479, 201, 56);
		getContentPane().add(btnDelete);
	
		
		//이미지 크기 조절
	    ImageIcon icon = new ImageIcon(getClass().getResource("/img/home.png"));			
	    Image img = icon.getImage();
	    Image changeImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	    ImageIcon changeIcon = new ImageIcon(changeImg);
		btnHome.setIcon(changeIcon);	    
		btnHome.setBackground(new Color(240, 240, 240));
	    btnHome.setOpaque(false); //버튼 배경색 없애기
	    btnHome.setBorderPainted(false); // 버튼 테두리 없애기
		btnHome.setBounds(885, 38, 52, 30);
		getContentPane().add(btnHome);
		
		
		//--- 테이블
		JScrollPane scrollPane = new JScrollPane(getReserTbl());
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setBounds(37, 103, 900, 350);
		getContentPane().add(scrollPane);

	}
	
	public JTable getReserTbl() {
		if(reserTbl == null) {
			
			
			//셀 간격 조정
			DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
			celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
			DefaultTableCellRenderer celAlignRight = new DefaultTableCellRenderer();
			celAlignRight.setHorizontalAlignment(JLabel.RIGHT);
			
			
			//표 생성
			reserTbl = new JTable(reserTM);
			reserTbl.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
			reserTbl.getTableHeader().setBackground(new Color(180,201,239)); // th 색상
			reserTbl.setBackground(Color.white);
			reserTbl.getTableHeader().setFont(tHead);
			reserTbl.getTableHeader().setReorderingAllowed(false); 
			reserTbl.getTableHeader().setResizingAllowed(false);
			reserTbl.setRowHeight(30);
			
			
			//컬럼 너비
			reserTbl.getColumn("체크인 날짜").setPreferredWidth(180);	
			reserTbl.getColumn("체크아웃 날짜").setPreferredWidth(180);	
			
			//정렬
			reserTbl.getColumn("예약번호").setCellRenderer(celAlignCenter);
			reserTbl.getColumn("방번호").setCellRenderer(celAlignCenter);				
			reserTbl.getColumn("인원 수").setCellRenderer(celAlignCenter);
			reserTbl.getColumn("체크인 날짜").setCellRenderer(celAlignRight);	
			reserTbl.getColumn("체크아웃 날짜").setCellRenderer(celAlignRight);
			
			//테이블 데이터 추가
			readReserTbl();
			
		}
		return reserTbl;
	}

	public void readReserTbl() {
		//DB 데이터 표에 삽입		
	
		for(int i=0; i<cn.reserTblList.size(); i++) {
			reserTM.addRow(new Object[] {
					cn.reserTblList.get(i).getReserNo(),
					cn.reserTblList.get(i).getRoomNo(),
					cn.reserTblList.get(i).getCheckIn(),
					cn.reserTblList.get(i).getCheckOut(),
					cn.reserTblList.get(i).getTeamNum()
							});
		}
	}
	
	public void delReser (int num) {		
		try {
			dbconnect();
			sql = "delete from reservation where reser_no = "+num+";";
			System.out.println("[delReser] : "+sql);
			stmt.executeUpdate(sql);
			
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dbconnect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
			stmt = conn.createStatement();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	MyPage jFrame = new MyPage();
	        	jFrame.setVisible(true);
	        }
	    });

	}
}