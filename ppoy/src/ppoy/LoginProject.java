package ppoy;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LoginProject {

   JPanel cardPanel;
   LoginProject lp;
   CardLayout card;
   
   JFrame jf;

   public void setFrame(LoginProject lpro) {

      jf = new JFrame();
      
      LoginPanel lp = new LoginPanel(lpro, jf);
      signupPanel sp = new signupPanel(lpro);

      card = new CardLayout();

      cardPanel = new JPanel(card);
      cardPanel.add(lp.mainPanel, "Login");
      cardPanel.add(sp.mainPanel, "Register");

      jf.getContentPane().add(cardPanel);
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.setSize(1000, 600);
     
      jf.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
      jf.setTitle("YangNOLJA");
      jf.setVisible(true);
      jf.setResizable(false); // 창 조절 불가

      jf.setLocationRelativeTo(null);


   }

   /**
    * @wbp.parser.entryPoint
    */
   public Connection getConnection() throws SQLException {
      Connection conn = null;

      conn = DriverManager.getConnection("jdbc:mysql://222.119.100.81:3382/ppoy", "ppoy", "ppoy");

      return conn;
   }

}

// ***** class LoginPanel *****

class LoginPanel extends JPanel implements ActionListener {

   JPanel mainPanel;
   JTextField idTextField;
   JPasswordField passTextField;

   String userMode = "일반";
   LoginProject lp;
   Font font = new Font("회원가입", Font.BOLD, 40);
   String admin = "admin";
   JFrame jf;

   public LoginPanel(JFrame jf) {
   }

   public LoginPanel(LoginProject lp, JFrame jf) {
      this.lp = lp;
      this.jf = jf;
      Font f1;
      
      mainPanel = new JPanel();
      mainPanel.setLayout(new GridLayout(3,1,0,0));
      mainPanel.setBackground(new Color(255, 255, 255));

      JPanel centerPanel = new JPanel();
//      JLabel loginLabel = new JLabel();
//      loginLabel.setFont(font);
//      centerPanel.add(loginLabel);
      centerPanel.setBackground(new Color(255, 255, 255));

      f1 = new Font("맑은 고딕", Font.BOLD, 17);
      
      JPanel userPanel = new JPanel() {
    	  
         Image background=new ImageIcon(getClass().getResource("/img/logo.png")).getImage();
        
        public void paint(Graphics g) {//그리는 함수
           g.drawImage(background, 425, 75,120,120, null);//background를 그려줌      
        }
      };
      

      JPanel gridBagidInfo = new JPanel(new GridBagLayout());
//      gridBagidInfo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
      GridBagConstraints c = new GridBagConstraints();
      gridBagidInfo.setBackground(new Color(255, 255, 255));

      JLabel idLabel = new JLabel(" 아이디   ");
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 0;
      gridBagidInfo.add(idLabel, c);
      idLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));

      idTextField = new JTextField(10);
      idTextField.setBorder(new LineBorder(new Color(228, 228, 228),2, true));
      c.insets = new Insets(0, 10, 0, 0);
      c.gridx = 1;
      c.gridy = 0;
      gridBagidInfo.add(idTextField, c);
      idTextField.setFont(new Font("맑은 고딕", Font.BOLD, 22));

      JLabel passLabel = new JLabel(" 비밀번호   ");
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 1;
      c.insets = new Insets(20, 0, 0, 0);
      gridBagidInfo.add(passLabel, c);
      passLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));

      passTextField = new JPasswordField(10);
      passTextField.setBorder(new LineBorder(new Color(228, 228, 228),2, true));
      c.insets = new Insets(20, 10, 0, 0);
      c.gridx = 1;
      c.gridy = 1;
      gridBagidInfo.add(passTextField, c);
      passTextField.setFont(new Font("맑은 고딕", Font.BOLD, 22));
      
      JPanel loginPanel = new JPanel();
     // JPanel signupPanel = new JPanel();
      JButton signupButton = new RoundButton("회원가입");
     // signupPanel.setBackground(new Color(255, 255, 255));
      signupButton.setForeground(new Color(255, 255, 255));
      signupButton.setBackground(new Color(100, 149, 237));
      loginPanel.add(signupButton);
      signupButton.setFont(f1);
      
      JButton loginButton = new RoundButton("로그인");
      loginButton.setForeground(new Color(255, 255, 255));
      loginButton.setBackground(new Color(100, 149, 237));
      loginPanel.add(loginButton);
      loginPanel.setBackground(new Color(255, 255, 255));
      loginButton.setFont(f1);
      
      //mainPanel.add(centerPanel);
      mainPanel.add(userPanel);//로고
      mainPanel.add(gridBagidInfo);
      mainPanel.add(loginPanel);
     // mainPanel.add(signupPanel);
      loginButton.addActionListener(this);

      signupButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            lp.card.next(lp.cardPanel);
         }
      });

   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JButton jb = (JButton) e.getSource();

         String id = idTextField.getText();
         String pass = passTextField.getText();

         try {

            String sql_query = String.format("SELECT user_pw FROM users WHERE user_id = '%s' AND user_pw ='%s'", id,
                  pass);

            Connection conn = lp.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rset = stmt.executeQuery(sql_query);
            rset.next();

            if (pass.equals(rset.getString(1))) {
               JOptionPane.showMessageDialog(this, "메인 페이지로 이동합니다.", "로그인 성공", 1);
               HomePage hp = new HomePage(id);
               jf.dispose();

            } else
               JOptionPane.showMessageDialog(this, "로그인에 실패하였습니다.", "로그인 실패", 1);

         } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "로그인에 실패하였습니다", "로그인 실패", 1);
            System.out.println("SQLException" + ex);
         }

   }
}

//회원가입 창 signupPanel

class signupPanel extends JPanel {

   JTextField idTf;
   JPasswordField passTf;
   JPasswordField passReTf;
   JTextField nameTf;
   JTextField phoneTf;
   JPanel mainPanel;
   JPanel subPanel;
   JButton joinBtn;
   Font f1, f2;
   String id = "", pass = "", passRe = "", name = "", phone = "";
   LoginProject lp;

   JButton backLogin = new RoundButton("로그인"); //회원가입 페이지에서 로그인페이지로 넘어가는 버튼
   public signupPanel(LoginProject lp) {

      this.lp = lp;
      subPanel = new JPanel();
      subPanel.setLayout(new GridBagLayout());
      subPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
      subPanel.setBackground(Color.white);
      
      f1 = new Font("맑은 고딕", Font.BOLD, 15);
      f2 = new Font("맑은 고딕", Font.PLAIN, 20);
      JLabel idLabel = new JLabel("아이디 ");
      idLabel.setFont(f1);
      JLabel passLabel = new JLabel("비밀번호 (영문 + 숫자 + 특수문자 : 8자이상)");
      passLabel.setFont(f1);
      JLabel passReLabel = new JLabel("비밀번호 재확인");
      passReLabel.setFont(f1);
      JLabel nameLabel = new JLabel("이름");
      nameLabel.setFont(f1);
      JLabel phoneLabel = new JLabel("핸드폰번호 (숫자만 입력)");
      phoneLabel.setFont(f1);

      idTf = new JTextField(15);
      idTf.setFont(f2);
      idTf.setBorder(new LineBorder(new Color(228, 228, 228),2, true));
      passTf = new JPasswordField(15);
      passTf.setFont(f2);
      passTf.setBorder(new LineBorder(new Color(228, 228, 228),2, true));
      passReTf = new JPasswordField(15);
      passReTf.setFont(f2);
      passReTf.setBorder(new LineBorder(new Color(228, 228, 228),2, true));
      nameTf = new JTextField(15);
      nameTf.setFont(f2);
      nameTf.setBorder(new LineBorder(new Color(228, 228, 228),2, true));
      phoneTf = new JTextField(11);
      phoneTf.setFont(f2);
      phoneTf.setBorder(new LineBorder(new Color(228, 228, 228),2, true));

      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.insets = new Insets(15, 20, 0, 0);

      c.gridx = 0;
      c.gridy = 0;
      subPanel.add(idLabel, c);

      c.gridx = 0;
      c.gridy = 1;
      subPanel.add(idTf, c); // 아이디

      c.gridx = 0;
      c.gridy = 2;
      subPanel.add(passLabel, c);

      c.gridx = 0;
      c.gridy = 3;
      subPanel.add(passTf, c); // pass

      c.gridx = 0;
      c.gridy = 4;
      subPanel.add(passReLabel, c);

      c.gridx = 0;
      c.gridy = 5;
      subPanel.add(passReTf, c); // password 재확인

      c.gridx = 0;
      c.gridy = 6;
      subPanel.add(nameLabel, c);

      c.gridx = 0;
      c.gridy = 7;
      subPanel.add(nameTf, c); // 이름

      c.gridx = 0;
      c.gridy = 8;
      subPanel.add(phoneLabel, c);

      c.gridx = 0;
      c.gridy = 9;
      subPanel.add(phoneTf, c);
      
      JPanel btnPan = new JPanel();
      btnPan.setBackground(Color.white);
      
      c.gridx = 0;
      c.gridy = 10;
      subPanel.add(btnPan, c);
      

      mainPanel = new JPanel();
      mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      JLabel signupLabel = new JLabel();
      signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      mainPanel.setBackground(Color.white);

      joinBtn = new RoundButton("회원가입");
      joinBtn.setFont(f1);
      joinBtn.setForeground(new Color(255, 255, 255));
      joinBtn.setBackground(new Color(100, 149, 237));
      joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

      mainPanel.add(signupLabel);
      mainPanel.add(subPanel);
      
      
// 로그인 화면으로 돌아가기*******************************************************************수정필요
      
      backLogin.setFont(f1);
      backLogin.setForeground(new Color(255, 255, 255));
      backLogin.setBackground(new Color(100, 149, 237));
      backLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
     
      btnPan.add(backLogin);// 회원가입 페이지의 로그인 버튼
      backLogin.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            lp.card.previous(lp.cardPanel); // 로그인 화면으로 돌아가기

         }
      });
      
      btnPan.add(joinBtn);//회원가입 페이지의 회원가입 버튼
      
      
      // 회원가입버튼
      joinBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            id = idTf.getText();
            pass = new String(passTf.getPassword());
            passRe = new String(passReTf.getPassword());
            name = nameTf.getText();
            phone = phoneTf.getText();

            String sql = "insert into users(user_id,user_tel, user_name, user_pw) values (?,?,?,?)";

            Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"); // 8자 영문+특문+숫자
            Matcher passMatcher = passPattern1.matcher(pass);

            if (!passMatcher.find()) {
               JOptionPane.showMessageDialog(null, "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상로 구성되어야 합니다", "비밀번호 오류", 1);
            } else if (!pass.equals(passRe)) {
               JOptionPane.showMessageDialog(null, "비밀번호가 서로 맞지 않습니다", "비밀번호 오류", 1);

            } else {
               try {
                  Connection conn = lp.getConnection();

                  PreparedStatement pstmt = conn.prepareStatement(sql);

                  pstmt.setString(1, idTf.getText()); // user_name
                  pstmt.setString(2, phoneTf.getText()); // user_tel
                  pstmt.setString(3, nameTf.getText()); // user_id
                  pstmt.setString(4, pass); // user_pw

                  int r = pstmt.executeUpdate();
                  System.out.println("변경된 row " + r);
                  JOptionPane.showMessageDialog(null, "회원 가입 완료!", "회원가입", 1);
                  lp.card.previous(lp.cardPanel); // 다 완료되면 로그인 화면으로
               } catch (SQLException e1) {
                  System.out.println("SQL error" + e1.getMessage());
                  if (e1.getMessage().contains("PRIMARY")) {
                     JOptionPane.showMessageDialog(null, "아이디 중복!", "아이디 중복 오류", 1);
                  } else
                     JOptionPane.showMessageDialog(null, "정보를 제대로 입력해주세요!", "오류", 1);
               } // try ,catch
            }
         }
      });

   }
}