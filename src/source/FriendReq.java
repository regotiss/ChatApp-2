package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FriendReq extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static JFrame f;
	private JPanel pnlDet;
	protected JButton btnAccept;
	private JPanel bottomPnl;
	JList<JLabel> list;
	public DefaultListModel<JLabel> model;
	private ClientNetwork client;
	protected Details detPnl;
	TitledBorder listBorder;
	
	public DefaultListModel<JLabel> getModel() {
		return model;
	}

	public void setModel(DefaultListModel<JLabel> model) {
		this.model = model;
	}

	
	public FriendReq(ClientNetwork c) {
		
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
		listBorder=new TitledBorder(null, "Requests", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe Print", Font.BOLD, 16), Color.RED);
		list.setBorder(listBorder);
		list.setCellRenderer(new MyRenderer());
		
		if(this instanceof AddFrd)
			list.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
				
					if(list.getSelectedIndex()>=0){
						String user=list.getSelectedValue().getText();
						client.sendMessage(Packet.PROFILE, user,"add");
					}
					
				
				}
			});
		else
		{
			list.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
				
					if(list.getSelectedIndex()>=0){
						String user=list.getSelectedValue().getText();
						System.out.println("In Friend Req"+user);
						client.sendMessage(Packet.PROFILE, user,"accept");
					}
					
				
				}
			});
		}
		
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
		
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JButton btn=(JButton)e.getSource();
				if(btn.getText().equals("Accept")){
					client.sendMessage(Packet.ACCEPT, list.getSelectedValue().getText());
					
				}
				else{
					client.sendMessage(Packet.ADD, list.getSelectedValue().getText());
					JOptionPane.showMessageDialog(null, "Request is Sent");
					model.removeElement(list.getSelectedValue());
				}
				
				detPnl.clear();

			}
		});
		btnAccept.setBounds(345, 6, 90, 28);
		bottomPnl.add(btnAccept);
		btnAccept.setFont(new Font("Segoe Print", Font.BOLD, 14));

	}
	public static void main(String args[]){
		WindowUtil.setNimbusLook();
		create();
	}
	public static void create() {
		f=new JFrame();
		f.setContentPane(new FriendReq(null));
		WindowUtil.setToCenter(f, 800, 600);
		f.setVisible(true);
		
	}
	public void setDetails(UsersDet data){
		detPnl.setDetails(data);
	}
}
