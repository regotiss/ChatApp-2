package source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Server extends JFrame {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ServerNetwork1 server;
	private JTextArea tchat;
	private JList<String> list;
	private DefaultListModel<String> model;
	public DefaultListModel<String> getModel() {
		return model;
	}

	public void setModel(DefaultListModel<String> model) {
		this.model = model;
	}
	private static final String TITLE="SERVER CHAT";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			ConnectToDatabase.main(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectToDatabase.main(null);
					new Server();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		super(TITLE);
		createView();
		server=null;
		String sport=JOptionPane.showInputDialog("Enter port");
		int port=0;
		
		try {
			port=Integer.parseInt(sport.trim());
	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
			return;
		}
	
		server=new ServerNetwork1(port,this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				server.start();
				
			}
		}).start();
		
	}

	public void createView() {
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel bottomPnl = new JPanel();
		bottomPnl.setBackground(Color.LIGHT_GRAY);
		contentPane.add(bottomPnl, BorderLayout.SOUTH);
		bottomPnl.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrMessage = new JTextArea();
		txtrMessage.setText("Message");
		bottomPnl.add(new JScrollPane(txtrMessage));
		
		JButton btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(server!=null)
				server.broadCast("server :"+txtrMessage.getText());
		
			}
		});
		bottomPnl.add(btnSend,BorderLayout.EAST);
		
		JPanel centerPnl = new JPanel();
		contentPane.add(centerPnl, BorderLayout.CENTER);
		centerPnl.setLayout(new BorderLayout(0, 0));
		
		tchat = new JTextArea();
		tchat.setEditable(false);
		((DefaultCaret)tchat.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		centerPnl.add(new JScrollPane(tchat), BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		//panel.setPreferredSize(new );
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		model=new DefaultListModel<>();
		list = new JList<>(model);
		
		JScrollPane listSP=new JScrollPane(list);
		listSP.setPreferredSize(new Dimension(getWidth()/4,0));
		panel.add(listSP);
		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if(server!=null)
				server.stop();
				System.exit(0);
			}
		});
		setVisible(true);
	}
	public void append(String msg){
		tchat.append(msg+"\n");
	}

}
