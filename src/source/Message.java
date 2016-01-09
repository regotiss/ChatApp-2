package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class Message extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame f;
	private JPanel contentPane;
	private ClientNetwork client;
	private JTextPane tchat;
	public JList<JLabel> list;
	public JList<JLabel> getList() {
		return list;
	}

	public void setList(JList<JLabel> list) {
		this.list = list;
	}
	private DefaultListModel<JLabel> model;
	private JButton btnSend;
	private int selected;
	private String username;
	private StyledDocument doc;
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public DefaultListModel<JLabel> getModel() {
		return model;
	}

	public void setModel(DefaultListModel<JLabel> model) {
		this.model = model;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					new Message("sujata",null);
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Message(String username,ClientNetwork c) {
	
		this.username=username;
		this.client=c;
		createView();
	}

	public void createView() {
		
		setLayout(new BorderLayout());
		//JLabel back=new JLabel(new ImageIcon(MyProfile.class.getResource("/images/profileback.png")));
		///add(back);
		//back.setLayout(null);
		
		contentPane = new JPanel();
		//contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBounds(0,0,800,600);
		//back.add(contentPane);
		add(contentPane);
		JPanel bottomPnl = new JPanel();
		bottomPnl.setBackground(Color.LIGHT_GRAY);
		contentPane.add(bottomPnl, BorderLayout.SOUTH);
		bottomPnl.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrMessage = new JTextArea();
		txtrMessage.setText("Message");
		bottomPnl.add(new JScrollPane(txtrMessage));
		
		btnSend = new JButton("send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(client!=null){
					
					if(selected>=0){
						
						client.sendMessage(Packet.MSG,username,list.getSelectedValue().getText(),txtrMessage.getText());
						
					}
					else
					JOptionPane.showMessageDialog(Message.this, "please select friend with \nwhom you want to chat");
				}
			}
		});
		bottomPnl.add(btnSend,BorderLayout.EAST);
		
		JPanel centerPnl = new JPanel();
		contentPane.add(centerPnl, BorderLayout.CENTER);
		centerPnl.setLayout(new BorderLayout(0, 0));
		
		tchat = new JTextPane();
        doc = tchat.getStyledDocument();
        addStylesToDocument(doc);
		tchat.setEditable(false);
		((DefaultCaret)tchat.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		centerPnl.add(new JScrollPane(tchat), BorderLayout.CENTER);

		JPanel panel = new JPanel();
		//panel.setPreferredSize(new );
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		model=new DefaultListModel<>();
		list = new JList<>(model);
		list.setCellRenderer(new MyRenderer());
		selected=list.getSelectedIndex();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
			
				selected=list.getSelectedIndex();
				if(selected>=0){
					btnSend.setEnabled(true);
				}
				
			
			}
		});
		
		JScrollPane listSP=new JScrollPane(list);
		listSP.setPreferredSize(new Dimension(getWidth()/4,0));
		panel.add(listSP);
		
		
		
	}

	public void addStylesToDocument(StyledDocument doc) {
		
		 Style def = StyleContext.getDefaultStyleContext().
                 getStyle(StyleContext.DEFAULT_STYLE);
		 doc.addStyle("regular", def);
	}

	public void append(String msg) {
		//tchat.append(msg+"\n");
		try {
			doc.insertString(doc.getLength(), msg+"\n",
			        doc.getStyle("regular"));
		} catch (BadLocationException e) {
			
			e.printStackTrace();
		}
		
	}
	public class MyRenderer extends DefaultListCellRenderer
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index,boolean isSelected, boolean cellHasFocus) 
	    {
	        //JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        if(value instanceof JLabel)
	        {
	        	 
	             if (isSelected) {
	                 setBackground(Color.black);
	                 setForeground(Color.blue);
	             } else {
	                 setBackground(Color.gray);
	                 setForeground(Color.green);
	             }

	            this.setText(((JLabel)value).getText());
	            //this.setIcon(((JLabel)value).getIcon());
	        }
	        return this;
	    }
	}
	public static void create(String username,ClientNetwork client) {
		f=new JFrame();
		f.setContentPane(new Message(username,client));
		WindowUtil.setToCenter(f, 800, 600);
		f.setVisible(true);
		
	}
}
