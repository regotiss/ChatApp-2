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
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class MessagePnl extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame f;
	private JButton btnSend;
	private JTextPane tchat;
	private StyledDocument doc;
	JList<JLabel> list;
	public DefaultListModel<JLabel> model;
	private String username;
	private ClientNetwork client;
	public DefaultListModel<JLabel> getModel() {
		return model;
	}

	public void setModel(DefaultListModel<JLabel> model) {
		this.model = model;
	}
	/**
	 * Create the panel.
	 */
	public MessagePnl(String username,ClientNetwork c) {
		this.username=username;
		this.client=c;
		createView();
	}
	public void createView() {
		setSize(1300,800);
		setBackground(Color.BLACK);
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		//JPanel panel = new JPanel();
		JLabel panel=new JLabel(new ImageIcon(MyProfile.class.getResource("/images/profileback.png")));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		model=new DefaultListModel<>();
		
		JPanel backPnl = new JPanel();
		backPnl.setOpaque(false);
		backPnl.setBackground(new Color(255, 255, 255));
		backPnl.setBounds(10, 17, 1268, 706);
		panel.add(backPnl);
		backPnl.setLayout(null);
		list = new JList<JLabel>(model);
		
		
		JScrollPane listSP=new JScrollPane(list);
		listSP.setBounds(810, 5, 284, 638);
		backPnl.add(listSP);
		listSP.setBorder(new TitledBorder(null, "Online", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe Print", Font.BOLD, 16), new Color(255, 255, 255)));
		list.setCellRenderer(new MyRenderer());
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 792, 542);
		backPnl.add(scrollPane);
		
		tchat = new JTextPane();
		tchat.setFont( new Font("Segoe Print", Font.BOLD, 16));
		scrollPane.setViewportView(tchat);
		scrollPane.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe Print", Font.BOLD, 16), new Color(255, 255, 255)));
		tchat.setEditable(false);
		
		JPanel pnlBottom = new JPanel();
		pnlBottom.setBounds(6, 560, 792, 83);
		backPnl.add(pnlBottom);
		pnlBottom.setOpaque(false);
		pnlBottom.setBorder(UIManager.getBorder("TitledBorder.border"));
		pnlBottom.setLayout(null);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(6, 31, 79, 30);
		pnlBottom.add(lblMessage);
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setFont(new Font("Segoe Print", Font.BOLD, 16));
		
		JTextArea tMsg = new JTextArea();
		tMsg.setFont( new Font("Segoe Print", Font.BOLD, 16));
		tMsg.setBounds(81, 6, 618, 67);
		pnlBottom.add(tMsg);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(client!=null){
					
					if(list.getSelectedIndex()>=0){
						
						client.sendMessage(Packet.MSG,username,list.getSelectedValue().getText(),tMsg.getText());
						
					}
					else
					JOptionPane.showMessageDialog(MessagePnl.this, "please select friend with \nwhom you want to chat");
				}
			}
		});
		btnSend.setBounds(711, 6, 62, 58);
		pnlBottom.add(btnSend);
		doc = tchat.getStyledDocument();
		((DefaultCaret)tchat.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
			
				if(list.getSelectedIndex()>=0){
					btnSend.setEnabled(true);
				}
				tchat.setText("");
				JLabel l=list.getSelectedValue();
				if(l!=null)
				client.sendMessage(Packet.MSG1, username, list.getSelectedValue().getText());
				
			
			}
		});
		addStylesToDocument(doc);
		
	}

	public static void main(String args[]){
		WindowUtil.setNimbusLook();
		create("sujata",null);
	}
	public static MessagePnl create(String username,ClientNetwork client) {
		f=new JFrame();
		
		MessagePnl msg=new MessagePnl(username,client);
		f.setContentPane(msg);
		WindowUtil.setToCenter(f, 800, 620);
		f.setVisible(true);
		
		return msg;
	}
	public void addStylesToDocument(StyledDocument doc) {
		
		 Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
		 doc.addStyle("regular", def);
		 StyleConstants.setFontFamily(def, "Segoe Print");
		 
		 Style regular = doc.addStyle("regular", def);
	     
		 

		 Style s = doc.addStyle("italic", regular);
	     StyleConstants.setItalic(s, true);

	     s = doc.addStyle("bold", regular);
	     StyleConstants.setBold(s, true);

	     s = doc.addStyle("small", regular);
	     StyleConstants.setFontSize(s, 10);

	     s = doc.addStyle("large", regular);
	     StyleConstants.setFontSize(s, 16);
		 //new Font("Segoe Print", Font.BOLD, 16);
	}
	public void append(String msg) {
		//tchat.append(msg+"\n");
		try {
			doc.insertString(doc.getLength(), msg+"\n",
			        doc.getStyle("large"));
		} catch (BadLocationException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void clearChatPanel(){
		tchat.setText("");
	}
}
