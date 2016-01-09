package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ViewFrd extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame f;
	private JPanel pnlDet;
	private JPanel bottomPnl;
	private ClientNetwork client;
	private DefaultListModel<JLabel> model;
	JList<JLabel> list;
	private Details detPnl;
	/**
	 * Create the panel.
	 */
	public DefaultListModel<JLabel> getModel() {
		return model;
	}

	public void setModel(DefaultListModel<JLabel> model) {
		this.model = model;
	}

	public ViewFrd(ClientNetwork c) {

		/*setSize(800,600);
		setBackground(Color.BLACK);
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		//JPanel panel = new JPanel();
		JLabel panel=new JLabel(new ImageIcon(MyProfile.class.getResource("/images/profileback.png")));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JList<JLabel> list = new JList<JLabel>();
		
		
		JScrollPane listSP=new JScrollPane(list);
		listSP.setBorder(new TitledBorder(null, "Requests", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
		listSP.setBounds(6, 84, 268, 458);
		panel.add(listSP);
		
		pnlDet = new JPanel();
		pnlDet.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlDet.setBackground(Color.WHITE);
		pnlDet.setBounds(286, 84, 477, 395);
		panel.add(pnlDet);
		
		lblTitle = new JLabel("All Friends");
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Segoe Print", Font.BOLD, 24));
		lblTitle.setBounds(322, 6, 200, 50);
		panel.add(lblTitle);
		
		bottomPnl = new JPanel();
		bottomPnl.setBackground(Color.WHITE);
		bottomPnl.setBorder(UIManager.getBorder("TitledBorder.border"));
		bottomPnl.setBounds(286, 481, 477, 50);
		panel.add(bottomPnl);
		bottomPnl.setLayout(null);
		
		lblSearch = new JLabel("Search  By Name :");
		lblSearch.setFont(new Font("Segoe Print", Font.BOLD, 16));
		lblSearch.setBounds(19, 10, 164, 34);
		bottomPnl.add(lblSearch);
		
		textField = new JTextField();
		textField.setBounds(195, 14, 256, 28);
		bottomPnl.add(textField);
		textField.setColumns(10);*/
		
		setSize(1097,675);
		
		this.client=c;
		setBackground(Color.BLACK);
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		//JPanel panel = new JPanel();
		JLabel panel=new JLabel(new ImageIcon(MyProfile.class.getResource("/images/profileback.png")));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		model=new DefaultListModel<>();
		list = new JList<JLabel>(model);
		list.setFont(new Font("Segoe Print", Font.PLAIN, 15));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new TitledBorder(null, "Friends", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe Print", Font.BOLD, 16), Color.RED));
		list.setCellRenderer(new MyRenderer());
		
		list.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
				
					if(list.getSelectedIndex()>=0){
						String user=list.getSelectedValue().getText();
						System.out.println("In Friend Req"+user);
						client.sendMessage(Packet.PROFILE, user,"view");
					}
					
				
				}
		});
	
		
		JScrollPane listSP=new JScrollPane(list);
		
		listSP.setBounds(6,6, 320, 630);
		panel.add(listSP);
		
		pnlDet = new JPanel(new BorderLayout());
		pnlDet.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP,new Font("Segoe Print", Font.BOLD, 16), Color.red));
		pnlDet.setBackground(Color.WHITE);
		pnlDet.setBounds(346, 6, 735, 589);
		panel.add(pnlDet);
		
		detPnl=new Details();
		pnlDet.add(detPnl);
		
		bottomPnl = new JPanel();
		bottomPnl.setOpaque(false);
		bottomPnl.setBackground(Color.WHITE);
		bottomPnl.setBorder(UIManager.getBorder("TitledBorder.border"));
		bottomPnl.setBounds(338, 598, 743, 50);
		panel.add(bottomPnl);
		bottomPnl.setLayout(null);
		
		
	}
	public static void main(String args[]){
		WindowUtil.setNimbusLook();
		create();
	}
	public static void create() {
		f=new JFrame();
		f.setContentPane(new ViewFrd(null));
		WindowUtil.setToCenter(f, 800, 600);
		f.setVisible(true);
		
	}
	public void setDetails(UsersDet data){
		detPnl.setDetails(data);
	}

}
