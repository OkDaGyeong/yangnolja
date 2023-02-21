package ppoy;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;


public class MyPage extends JFrame {
	Connection cn = new Connection();
	private JTable reserTbl;
	
	JButton btnDelete = new JButton("예약 취소");
	JButton btnUpdate = new JButton("예약 변경");
	JButton btnHome = new JButton("메인페이지");
	
	
	private Font tHead = new Font("맑은 고딕", Font.BOLD, 23);
	private Font tpt = new Font("맑은 고딕", Font.PLAIN, 18);
	private Font pt = new Font("맑은 고딕", Font.PLAIN, 23); 
	
	public MyPage() {
		getContentPane().setBackground(new Color(255, 255, 255));
		this.setTitle("MyPage");
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel PageName = new JLabel("My Page");
		PageName.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		PageName.setBounds(37, 22, 154, 56);
		getContentPane().add(PageName);
	
		
		//btnDelete.setBackground(new Color(224, 255, 255));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDelete.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		btnDelete.setBounds(101, 479, 201, 56);
		getContentPane().add(btnDelete);
		
		
		btnUpdate.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		btnUpdate.setBounds(645, 479, 201, 56);
		getContentPane().add(btnUpdate);
		
		
		btnHome.setBounds(797, 32, 140, 23);
		getContentPane().add(btnHome);
		
		
		//--- 테이블
		JScrollPane scrollPane = new JScrollPane(getReserTbl());
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setBounds(37, 103, 900, 350);
		getContentPane().add(scrollPane);
		
		//scrollPane.setRowHeaderView(getReserTbl());
		
		
	}
	
	public JTable getReserTbl() {
		if(reserTbl == null) {
			//-- 출력할 정보
			String[] header = {"예약번호","방번호", "체크인 날짜","체크아웃 날짜","인원 수"};
			String[][] contents=null;
			DefaultTableModel reserTM = new DefaultTableModel(contents, header){
				public boolean isCellEditable(int rowIndex, int mCollndex) {
					return false; //셀 내용 수정 불가
				}
			};
			
			//표 생성
			reserTbl = new JTable(reserTM);
			//reserTbl.getTableHeader().setBackground(new Color(214,201,239)); // th 색상
			//reserTbl.setBackground(new Color(233,228,245));
			reserTbl.getTableHeader().setFont(tHead);
			reserTbl.getTableHeader().setReorderingAllowed(false); 
			reserTbl.getTableHeader().setResizingAllowed(false);
			reserTbl.getColumn("예약번호").setPreferredWidth(10);
			reserTbl.getColumn("방번호").setPreferredWidth(10);
			reserTbl.getColumn("인원 수").setPreferredWidth(10);
			
			//DB 데이터 표에 삽입
			for(int i=0; i<cn.rserTblList.size(); i++) {
				reserTM.addRow(new Object[] {
						cn.rserTblList.get(i).getReserNo(),
						cn.rserTblList.get(i).getReserNo(),
						cn.rserTblList.get(i).getCheckIn(),
						cn.rserTblList.get(i).getCheckOut(),
						cn.rserTblList.get(i).getTeamNum()
								});
			}
			
		}
		return reserTbl;
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