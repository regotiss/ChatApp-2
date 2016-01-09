package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.toedter.calendar.JDateChooser;

public class Details extends JPanel{
	
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

	public Details(){
		
		setLayout(new BorderLayout());
		JPanel pnlMain = new JPanel();
		pnlMain.setBorder(UIManager.getBorder("TitledBorder.border"));
		pnlMain.setOpaque(false);
		pnlMain.setBackground(Color.WHITE);
		pnlMain.setBounds(26, 65, 707, 440);
		add(pnlMain);
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
		
		
		JPanel pnlProfile = new JPanel();
		pnlProfile.setBounds(24, 22, 210, 246);
		pnlMain.add(pnlProfile);
		pnlProfile.setBorder(UIManager.getBorder("TitledBorder.border"));
		pnlProfile.setBackground(Color.WHITE);
		pnlProfile.setLayout(new BorderLayout(0, 0));
				
		
		JPanel imagePnl = new JPanel();
		
		imagePnl.setBackground(Color.WHITE);
		pnlProfile.add(imagePnl, BorderLayout.CENTER);
		imagePnl.setLayout(new BorderLayout(0, 0));
		
		lblProPic = new JLabel(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
		imagePnl.add(lblProPic, BorderLayout.CENTER);

		add(pnlMain);
	}
	public static void main(String args[]){
		WindowUtil.setNimbusLook();
		create();
	}
	public static void create() {
		f=new JFrame();
		f.setContentPane(new Details());
		WindowUtil.setToCenter(f, 800, 600);
		f.setVisible(true);
		
	}
	public void clear(){
		
		tfName.setText("");
		tfDOB.setDate(new Date());
		taddr.setText("");
		tfCity.setText("");
		tfDistrict.setText("");
		tfCountry.setText("");
	
		lblProPic.setIcon(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
	}
	public void setDetails(UsersDet data){
		tfName.setText(data.getName());
		tfDOB.setDate(data.getDob());
		taddr.setText(data.getAddr());
		tfCity.setText(data.getCity());
		tfDistrict.setText(data.getDistrict());
		tfCountry.setText(data.getCountry());
		ImageIcon img=WindowUtil.getImage(data.getPic(),200,200);
		if(img!=null)
		//lblProPic.setIcon(WindowUtil.getScaledImage(img.getImage(),200,200));
		lblProPic.setIcon(img);
		else
		lblProPic.setIcon(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
		
	}
}
