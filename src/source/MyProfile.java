package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.toedter.calendar.JDateChooser;

public class MyProfile extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame f;
	private JTextField tfName;
	private JDateChooser tfDOB;
	private JTextField tfCountry;
	private JTextField tfDistrict;
	private JTextField tfCity;
	private JTextArea taddr;
	private JLabel lblProPic;
	private JFileChooser fc;
	private ClientNetwork client;
	protected File file;

	/**
	 * Create the panel.
	 * @param username 
	 * @param client 
	 */
	public MyProfile(String username, ClientNetwork c) {
		
		
		setBackground(SystemColor.desktop);
		setSize(800,600);
		setLayout(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		
		this.client=c;
		
		//client.sendMessage(Packet.PROFILE, username);
		//JPanel panel = new JPanel();
		JLabel panel=new JLabel(new ImageIcon(MyProfile.class.getResource("/images/profileback.png")));
		panel.setBorder(UIManager.getBorder("TitledBorder.border"));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel label = new JLabel("User:"+username);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Cambria", Font.BOLD, 22));
		label.setBounds(595, 18, 142, 50);
		panel.add(label);
		
		JPanel pnlMain = new JPanel();
		
		pnlMain.setBorder(UIManager.getBorder("TitledBorder.border"));
		pnlMain.setOpaque(false);
		pnlMain.setBackground(Color.WHITE);
		pnlMain.setBounds(26, 65, 707, 440);
		panel.add(pnlMain);
		pnlMain.setLayout(null);
		
		JPanel pnlDet = new JPanel();
		//pnlDet.setOpaque(false);
		pnlDet.setBorder(UIManager.getBorder("TitledBorder.border"));
		pnlDet.setBackground(Color.WHITE);
		pnlDet.setBounds(290, 22, 396, 372);
		pnlMain.add(pnlDet);
		pnlDet.setLayout(null);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setBounds(13, 7, 80, 27);
		pnlDet.add(lblName);
		lblName.setFont(new Font("Segoe Print", Font.BOLD, 15));
		
		tfName = new JTextField();
		tfName.setBounds(95, 6, 280, 30);
		pnlDet.add(tfName);
		tfName.setColumns(10);
		
		JLabel lblDob = new JLabel("DOB :");
		lblDob.setBounds(30, 64, 63, 37);
		pnlDet.add(lblDob);
		lblDob.setFont(new Font("Segoe Print", Font.BOLD, 15));
		
		tfDOB = new JDateChooser();
		tfDOB.setBounds(95, 68, 280, 30);
		pnlDet.add(tfDOB);
		tfDOB.setDate(new Date());
		tfDOB.setDateFormatString("dd/MM/yyyy");
		System.out.println(tfDOB.getDate());
		

		
		JLabel lblAddress = new JLabel("Address :");
		lblAddress.setBounds(13, 129, 80, 37);
		pnlDet.add(lblAddress);
		lblAddress.setFont(new Font("Segoe Print", Font.BOLD, 15));
		
		JLabel lblCountry = new JLabel("Country :");
		lblCountry.setBounds(6, 208, 80, 37);
		pnlDet.add(lblCountry);
		lblCountry.setFont(new Font("Segoe Print", Font.BOLD, 15));
		
		tfCountry = new JTextField();
		tfCountry.setBounds(95, 215, 280, 30);
		pnlDet.add(tfCountry);
		tfCountry.setColumns(10);
		
		tfDistrict = new JTextField();
		tfDistrict.setBounds(95, 264, 280, 30);
		pnlDet.add(tfDistrict);
		tfDistrict.setColumns(10);
		
		JLabel lblDistrict = new JLabel("District :");
		lblDistrict.setBounds(13, 257, 80, 37);
		pnlDet.add(lblDistrict);
		lblDistrict.setFont(new Font("Segoe Print", Font.BOLD, 15));
		
		tfCity = new JTextField();
		tfCity.setBounds(95, 319, 280, 30);
		pnlDet.add(tfCity);
		tfCity.setColumns(10);
		
		JLabel lblCity = new JLabel("City :");
		lblCity.setBounds(40, 312, 53, 37);
		pnlDet.add(lblCity);
		lblCity.setFont(new Font("Segoe Print", Font.BOLD, 15));
		
		taddr = new JTextArea();
		taddr.setBounds(95, 134, 274, 49);
		pnlDet.add(taddr);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				UsersDet det=getUserDet();
				if(det!=null)
				client.sendMessage(Packet.UPDATE,det);
				else
					System.out.println("userdet is null");
			}
		});
		btnUpdate.setBounds(457, 400, 90, 28);
		pnlMain.add(btnUpdate);
		
		JPanel pnlProfile = new JPanel();
		pnlProfile.setBounds(24, 22, 210, 246);
		pnlMain.add(pnlProfile);
		pnlProfile.setBorder(UIManager.getBorder("TitledBorder.border"));
		pnlProfile.setBackground(Color.WHITE);
		pnlProfile.setLayout(new BorderLayout(0, 0));
		
		JPanel browseOpt = new JPanel();
		pnlProfile.add(browseOpt, BorderLayout.SOUTH);
		
		fc = new JFileChooser();
		file=new File(MyProfile.class.getResource("/images/default.png").toString().substring(6));
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(MyProfile.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                file = fc.getSelectedFile();
	                lblProPic.setIcon(WindowUtil.getScaledImage(new ImageIcon(file.getAbsolutePath()).getImage(),200,200));
				}
			}
		});
		browseOpt.add(btnBrowse);
		
		JPanel imagePnl = new JPanel();
		
		imagePnl.setBackground(Color.WHITE);
		pnlProfile.add(imagePnl, BorderLayout.CENTER);
		imagePnl.setLayout(new BorderLayout(0, 0));
		
		lblProPic = new JLabel(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
		imagePnl.add(lblProPic, BorderLayout.CENTER);
		
		/*JPanel pnlCenter = new JPanel();
		pnlCenter.setBackground(Color.WHITE);
		pnlCenter.setBounds(0, 0, panel.getWidth(), panel.getHeight());
		panel.add(pnlCenter);*/
		
	}
	public UsersDet getUserDet() {
		
		byte[] content;
		try {
			content = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cant save");
			return null;
		}
		
		String name=tfName.getText();
		Date date=tfDOB.getDate();
		//SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		
		String addr=taddr.getText();
		String country=tfCountry.getText();
		String city=tfCity.getText();
		String dist=tfDistrict.getText();
		return (new UsersDet(name,date,addr,country,dist,city,content));
	}
	public static void main(String args[]){
		WindowUtil.setNimbusLook();
		create("sujata");
	}
	public static void create(String username) {
		f=new JFrame();
		f.setContentPane(new MyProfile(username,null));
		WindowUtil.setToCenter(f, 800, 600);
		f.setVisible(true);
		
	}
	public void setDetails(UsersDet data){
		tfName.setText(data.getName());
		tfDOB.setDate(data.getDob());
		tfCity.setText(data.getCity());
		taddr.setText(data.getAddr());
		tfDistrict.setText(data.getDistrict());
		tfCountry.setText(data.getCountry());
		/*ImageIcon img=WindowUtil.getImage(data.getPic());
		if(img!=null)
		lblProPic.setIcon(WindowUtil.getScaledImage(img.getImage(),200,200));
		else
		lblProPic.setIcon(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
		*/
		ImageIcon img=WindowUtil.getImage(data.getPic(),200,200);
		if(img!=null)
		//lblProPic.setIcon(WindowUtil.getScaledImage(img.getImage(),200,200));
		lblProPic.setIcon(img);
		else
		lblProPic.setIcon(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
	}
	
}
