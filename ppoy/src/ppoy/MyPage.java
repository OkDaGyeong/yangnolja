package ppoy;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class MyPage extends JFrame {
	String loginId;
	ConnectionDB cn;
	private JTable reserTbl;
	
	JButton btnDelete = new RoundButton("예약 취소");
	JButton btnHome = new JButton("");
	
	
	private Font tHead = new Font("맑은 고딕", Font.BOLD, 23);
	private Font tpt = new Font("맑은 고딕", Font.PLAIN, 18);
	
	int delNum = 0 ; // 취소할 예약번호
	int reserNum=0;
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
	
	
	
	public MyPage(String id) {
		loginId=id;
		cn = new ConnectionDB(loginId);
		
		getContentPane().setBackground(new Color(230, 230, 245));
		this.setTitle("INFO");
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
		this.setResizable(false); // 창크기 고정
		getContentPane().setLayout(null);

		String loginUserName=cn.loginUserName;
		JLabel PageName = new JLabel(" "+loginUserName+"("+id+") "+"님의 예약정보"); //이모티콘 추가
		PageName.setIcon(new ImageIcon(MyPage.class.getResource("/img/user.png")));
		PageName.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		PageName.setBounds(37, 25, 776, 56);
		getContentPane().add(PageName);

		btnDelete.setForeground(new Color(255, 255, 255));
		btnDelete.setBackground(new Color(100, 149, 237));
	
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(reserTbl.getSelectedRow() == -1) //선택안했을 때
				{
					JOptionPane.showMessageDialog(null, "취소할 예약을 선택하십시오", "경고", JOptionPane.WARNING_MESSAGE);
					//return;
				}
				else
				{
					int result = JOptionPane.showConfirmDialog(null, "선택한 예약을 취소하시겠습니까?","예약 취소",JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						delNum = (int) reserTbl.getValueAt(reserTbl.getSelectedRow(),0);
						delReser(delNum);
						reserTM.removeRow(reserTbl.getSelectedRow());
					}else {
						//예약취소 안함
					}
				}

			}
		});
		
		btnDelete.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnDelete.setBounds(736, 479, 201, 56);
		getContentPane().add(btnDelete);
		

		ImageIcon changeIcon = new ImageIcon(getClass().getResource("/img/home.png"));
		
		RoundButton btnUpdate = new RoundButton("예약 수정");
		btnUpdate.setForeground(new Color(255, 255, 255));
		btnUpdate.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnUpdate.setBackground(new Color(100, 149, 237));
		btnUpdate.setBounds(37, 479, 201, 56);
		getContentPane().add(btnUpdate);
	    
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if(reserTbl.getSelectedRow() == -1) //선택안했을 때
				{
					JOptionPane.showMessageDialog(null, "변경할 예약을 선택하십시오", "경고", JOptionPane.WARNING_MESSAGE);
					//return;
				}
				else
				{
					int result = JOptionPane.showConfirmDialog(null, "해당 예약을 수정하시겠습니까?","예약 변경",JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						reserNum = (int) reserTbl.getValueAt(reserTbl.getSelectedRow(),0);
						
						ReReservation reReserPage = new ReReservation(id,reserNum);
						reReserPage.setVisible(true);
						setVisible(false);
					}else {
						//예약변경 안함
					}
					
				}

			}
		});

		
		btnHome.setIcon(changeIcon);	    
		btnHome.setBackground(new Color(240, 240, 240));
	    btnHome.setOpaque(false); //버튼 배경색 없애기
	    btnHome.setBorder(null); // 버튼 테두리 없애기
		btnHome.setBounds(885, 38, 52, 30);
		getContentPane().add(btnHome);
		btnHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HomePage h = new HomePage(id);
				h.setVisible(true);
				dispose();
				
			}
		});
		
		
		//--- 테이블
		JScrollPane scrollPane = new JScrollPane(getReserTbl());
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setBounds(37, 103, 900, 350);
		scrollPane.setBorder(new LineBorder(new Color(100, 149, 237), 3, true));
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
			reserTbl.setFont(tpt);
			reserTbl.getTableHeader().setBackground(new Color(180,201,239)); // th 색상
			
			reserTbl.setBackground(new Color(255, 255, 255));
			reserTbl.getTableHeader().setFont(tHead);
			reserTbl.getTableHeader().setReorderingAllowed(false); 
			reserTbl.getTableHeader().setResizingAllowed(false);
			reserTbl.getTableHeader().setBorder(null);
			reserTbl.setRowHeight(30);
			
			EmptyBorder b1 = new EmptyBorder(5,3,5,3);
			reserTbl.getTableHeader().setBorder(b1);
			
			
			//컬럼 너비
			reserTbl.getColumn("체크인 날짜").setPreferredWidth(180);	
			reserTbl.getColumn("체크아웃 날짜").setPreferredWidth(180);	
			
			//정렬
			reserTbl.getColumn("예약번호").setCellRenderer(celAlignCenter);
			reserTbl.getColumn("방번호").setCellRenderer(celAlignCenter);				
			reserTbl.getColumn("인원 수").setCellRenderer(celAlignCenter);
			reserTbl.getColumn("체크인 날짜").setCellRenderer(celAlignCenter);	
			reserTbl.getColumn("체크아웃 날짜").setCellRenderer(celAlignCenter);
			
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
					"Room "+cn.reserTblList.get(i).getRoomNo(),
					cn.reserTblList.get(i).getCheckIn(),
					cn.reserTblList.get(i).getCheckOut(),
					cn.reserTblList.get(i).getTeamNum()+" 명"
							});
		}
	}
	
	public void delReser (int num) {		
		try {
			dbconnect();
			sql = "delete from reservation where reser_no = "+num+";";
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

}