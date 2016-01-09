package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class Login extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private static ClientNetwork client;
	private JPanel contentPane;
	private JTextField tfUsername;
	private JPasswordField tfPass;
	protected JButton btnLogin;
	final Color COLOR_NORMAL    = Color.BLUE;
  	final Color COLOR_HOVER     = Color.RED;
	protected JPanel pnlSignup;
	protected JPanel mainPnl;
	protected TitledBorder t;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		WindowUtil.setNimbusLook();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				
					ConnectToDatabase.main(null);
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 252);
		contentPane = new JPanel();
		contentPane.setBorder(UIManager.getBorder("TitledBorder.border"));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel imagePnl = new JPanel();
		panel.add(imagePnl, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/images/main.png")));
		imagePnl.add(lblNewLabel);
		
		mainPnl = new JPanel();
		t=new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 15), Color.BLUE);
		mainPnl.setBorder(t);
		mainPnl.setBackground(Color.WHITE);
		panel.add(mainPnl, BorderLayout.CENTER);
		mainPnl.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelUn = new JPanel();
		panelUn.setBackground(Color.WHITE);
		mainPnl.add(panelUn);
		
		JLabel lblUsername = new JLabel("Username :");
		panelUn.add(lblUsername);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		tfUsername = new JTextField();
		panelUn.add(tfUsername);
		tfUsername.setColumns(15);
		
		JPanel pnlPass = new JPanel();
		pnlPass.setBackground(Color.WHITE);
		mainPnl.add(pnlPass);
		
		JLabel lblPassword = new JLabel("Password  :");
		pnlPass.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		tfPass = new JPasswordField();
		pnlPass.add(tfPass);
		tfPass.setColumns(15);
		
		JPanel pnlLogin = new JPanel();
		pnlLogin.setBackground(Color.WHITE);
		mainPnl.add(pnlLogin);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(btnLogin.getText().equals("Login")){
				/*if(Resource.authenticate(tfUsername.getText().trim(),new String(tfPass.getPassword()).trim())){
					JOptionPane.showMessageDialog(Login.this, "Login Successfully");
					setVisible(false);
					new Client(tfUsername.getText().trim());
					dispose();
				}
				else
					JOptionPane.showMessageDialog(Login.this, "Username or password is incorrect");*/
					
					String host=JOptionPane.showInputDialog("Enter Server Address ");
					String sport=JOptionPane.showInputDialog("Enter Port");
					int port=0;
					try {
						port=Integer.parseInt(sport.trim());
						
					} catch (Exception e1) {
						
						System.exit(0);
						return;
					}
					client=new ClientNetwork("login");
					String pass=new String(tfPass.getPassword());
					//System.out.println("In login");
					client.startServer(tfUsername.getText().trim(),pass.trim(),host.trim(), port,Login.this);
					//client=new ClientNetwork("localhost", port,tfUsername.getText().trim(),new String(tfPass.getPassword()).trim(),Login.this);
					//client.start();					
				}
				else{
					signup();
				}
			}
		});
		btnLogin.setHorizontalAlignment(AbstractButton.RIGHT);
		
		getRootPane().setDefaultButton(btnLogin);
		pnlLogin.add(btnLogin);
		
		pnlSignup = new JPanel();
		mainPnl.add(pnlSignup);
		pnlSignup.setBackground(Color.WHITE);
		
		JLabel lblDontHaveAccount = new JLabel("dont have account ?");
		lblDontHaveAccount.setForeground(COLOR_NORMAL);
		lblDontHaveAccount.addMouseListener(new MouseAdapter(){
			 
			 public void mouseEntered(MouseEvent me) 
			 {
				 lblDontHaveAccount.setForeground(COLOR_HOVER);
			     setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
	 
			public void mouseClicked(java.awt.event.MouseEvent me)
			{

				if(me.getSource() instanceof JLabel)
				{
					lblDontHaveAccount.setForeground(COLOR_NORMAL);
					setVisible(false);
					dispose();
					new Signup();
				}
			}
			public void mouseExited(MouseEvent me)
		    {
				lblDontHaveAccount.setForeground(COLOR_NORMAL);  
		        setCursor(Cursor.getDefaultCursor());
		        //System.out.println("Mouse Exited");
		    }
		   
		});
		
		lblDontHaveAccount.setHorizontalAlignment(SwingConstants.LEFT);
		lblDontHaveAccount.setFont(new Font("SansSerif", Font.PLAIN, 14));
		/*lblDontHaveAccount.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e){
				lblDontHaveAccount.setForeground(COLOR_HOVER);
				System.out.println("motion");
			}
		});*/
		pnlSignup.add(lblDontHaveAccount);
	}

	/*public void signup() {
		Statement s=ConnectToDatabase.getS();
		
		try {
			ResultSet rs=s.executeQuery("select max(userid) from users");
			int id=0;
			while(rs.next())
				id=rs.getInt(1);
			id=id+1;
		
			s.execute("insert into users(userid,username,password) values("+id+",'"+tfUsername.getText().trim()+
					"','"+new String(tfPass.getPassword())+"')");
			
			JOptionPane.showMessageDialog(this, "Account Successfully Created");
			setVisible(false);
			new Client(tfUsername.getText().trim());
			dispose();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
	}
*/
	public void signup(){
		String host=JOptionPane.showInputDialog("Enter Server Address ");
		String sport=JOptionPane.showInputDialog("Enter Port");
		int port=0;
		try {
			port=Integer.parseInt(sport.trim());
			
		} catch (Exception e1) {
			
			System.exit(0);
			return;
		}
		client=new ClientNetwork("signup");
		String pass=new String(tfPass.getPassword());
		//System.out.println("In login");
		client.startServer(tfUsername.getText().trim(),pass.trim(),host.trim(), port,Login.this);
	}
	
}
