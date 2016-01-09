package source;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ClientNetwork client;
	private String username;
	private int port;
	private MessagePnl messagePnl;
	private JMenuBar menubar;
	private JPanel friends;
	private JTabbedPane frdtab;
	private MyProfile profile;
	private FriendReq frdreq;
	public FriendReq getFrdreq() {
		return frdreq;
	}

	public void setFrdreq(FriendReq frdreq) {
		this.frdreq = frdreq;
	}

	private AddFrd addfrd;
	public AddFrd getAddfrd() {
		return addfrd;
	}

	public void setAddfrd(AddFrd addfrd) {
		this.addfrd = addfrd;
	}

	private ViewFrd view;
	public ViewFrd getView() {
		return view;
	}

	public void setView(ViewFrd view) {
		this.view = view;
	}

	public MyProfile getProfile() {
		return profile;
	}

	public void setProfile(MyProfile profile) {
		this.profile = profile;
	}

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowUtil.setNimbusLook();
					//String username=JOptionPane.showInputDialog("Enter username");
					new Client("sujata");
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client(String username,ClientNetwork c){
		
		this.username=username;
		this.client=c;
		//createView();
	}
	public Client(String username) {
		
		this.username=username;
		String sport=JOptionPane.showInputDialog("Enter Port");
		port=0;
		try {
			port=Integer.parseInt(sport.trim());
			
		} catch (Exception e) {
			
			System.exit(0);
			return;
		}
		
		client=new ClientNetwork("localhost",port,username,this);
		createView();
		client.startS();

		client.sendMessage(Packet.PROFILE, username,"myprofile");
		client.sendMessage(Packet.REQUEST, username);
		client.sendMessage(Packet.FRDLIST,username);
		client.sendMessage(Packet.LIST,username);
	}

	public void createView() {
		
		setTitle(username);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel back=new JPanel();
		//JLabel back=new JLabel(new ImageIcon(Client.class.getResource("/images/back.png")));
		contentPane.add(back);
		back.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.RIGHT);
		
		tabPane.setOpaque(false);
		tabPane.setFont(new Font("Segoe Print", Font.BOLD, 18));
		back.add(tabPane, BorderLayout.CENTER);
		
		profile=new MyProfile(username,client);
	
		tabPane.addTab("",WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/tprofile.png")).getImage(),150,150),profile);
		
		friends=new JPanel();
		addTabs();
		tabPane.addTab("",WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/tfrd.png")).getImage(),150,150),friends);
		
		messagePnl=new MessagePnl(username,client);
		tabPane.addTab("",WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/tchat.png")).getImage(),150,150),messagePnl);
		
		
		//messagePnl=new Message(username,client);
		//add(messagePnl);
		
		menubar=new JMenuBar();
		setJMenuBar(menubar);
		
		addMenus();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				
				if(client!=null)
				client.stop();
			}
		});

		setVisible(true);
		
		WindowUtil.setToCenter(this, WindowUtil.getScreenWidth(), WindowUtil.getScreenHeight());
	}

	public void addTabs() {
		
		friends.setLayout(new BorderLayout());
		frdreq=new FriendReq(client);
		frdtab=new JTabbedPane();
		
		
		//frdtab.addTab("Friend Requests",WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/frdreq.png")).getImage(),50,50),frdreq);
		frdtab.addTab("Friend Requests",frdreq);
		friends.add(frdtab);
		
		addfrd=new AddFrd(client);
		//frdtab.addTab("Add Friend", WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/searchfrd.png")).getImage(),50,50),addfrd);
		frdtab.addTab("Add Friend",addfrd);
		
		view=new ViewFrd(client);
		//frdtab.addTab("View Friends",WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/viewf.png")).getImage(),50,50),view);
		frdtab.addTab("View Friends",view);
	}

	public void addMenus() {
		JMenu profile=new JMenu("Profile");
		JMenu friends=new JMenu("Friends");
		JMenu chat=new JMenu("Chat");
		
		menubar.add(profile);
		menubar.add(friends);
		menubar.add(chat);
		
		JMenuItem myprofile=new JMenuItem("My Profile");
		myprofile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyProfile.create(username);
				
			}
		});
		profile.add(myprofile);
		
		JMenuItem frdreq=new JMenuItem("Friend Request");
		frdreq.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FriendReq.create();
				
			}
		});
		JMenuItem addfrd=new JMenuItem("Add Friend");
		addfrd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddFrd.create();
				
			}
		});

		JMenuItem viewfrd=new JMenuItem("View Friends");
		viewfrd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewFrd.create();
				
			}
		});
		friends.add(frdreq);
		friends.add(addfrd);
		friends.add(viewfrd);
		
		JMenuItem online=new JMenuItem("Online");
		online.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				messagePnl=MessagePnl.create(username,client);
				System.out.println(messagePnl);
			}
		});
		chat.add(online);
		
		
	}

	public MessagePnl getMessagePnl() {
		return messagePnl;
	}

	public void setMessagePnl(MessagePnl messagePnl) {
		this.messagePnl = messagePnl;
	}

	
}
