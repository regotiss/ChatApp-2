package source;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ClientNetwork {

	private Client client;
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	private String serverAddr;
	private int port;
	private Socket s;
	private ListenThread t;
	private String username;
	private Login l;
	private String type;


	public ClientNetwork(String string, int i, String username, Client client2) {
		this.client=client2;
		this.serverAddr=string;
		this.port=i;
		this.username=username;
	
	}
	public ClientNetwork(String type){
		this.type=type;
	}
	public void startServer(String username,String password,String addr,int port, Login login){
		try {
			this.serverAddr=addr;
			this.port=port;
			this.username=username;
			s=new Socket(serverAddr,port);
			this.l=login;
			t=new ListenThread(s,password);
			
			if(t.out!=null){
				
				t.start();
				System.out.println("client thread 1");
				sendMessage(Packet.PROFILE, username,"myprofile");
				sendMessage(Packet.REQUEST, username);
				sendMessage(Packet.FRDLIST,username);
				sendMessage(Packet.LIST,username);
	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server not found");
			return ;
		}

	}
	public void startS() {
		
		try {
			
			s=new Socket(serverAddr,port);
			
			//System.out.println("client net connected");
			t=new ListenThread(s);
		} catch (Exception e) {
			//System.out.println(e);
			e.printStackTrace();
			return ;
		}
	}

	public void stop() {
		
		try {
			if(s!=null)
			s.close();
			if(t!=null){
				t.in.close();
				t.out.close();
			}
		} catch (IOException e) {
	
			//e.printStackTrace();
			System.out.println(e);
		}
	}
	class ListenThread extends Thread
	{

		private Socket s;
		private ObjectInputStream in;
		private ObjectOutputStream out;

		public ListenThread(Socket s) {
			this.s=s;
			try {
				System.out.println("s1");
				in=new ObjectInputStream(s.getInputStream());
				System.out.println("s2");
				out=new ObjectOutputStream(s.getOutputStream());
				out.flush();
				out.writeObject(username);
			} catch (IOException e) {
		
				e.printStackTrace();
			}
			
			start();
		}
		public ListenThread(Socket s,String pass) {
			this.s=s;
			try {
				System.out.println("s1");
				in=new ObjectInputStream(s.getInputStream());
				System.out.println("s2");
				out=new ObjectOutputStream(s.getOutputStream());
				//out.flush();
				
				Packet pkt=new Packet(Packet.AUTH,username,pass);
				if(type.equals("signup"))
					pkt=new Packet(Packet.SIGNUP,username,pass);
				out.writeObject(pkt);
				
				//boolean b=in.readBoolean();
				try {
					pkt=(Packet) in.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String str=pkt.getMsg();
				System.out.println("client net bool "+str);
				if(str.equals("user")){
					
					l.setVisible(false);
					if(type.equals("login"))
					JOptionPane.showMessageDialog(null, "Login Successfully");
					else
					JOptionPane.showMessageDialog(null, "Sign Up successfully");
					client=new Client(username,ClientNetwork.this);
					client.createView();
					System.out.println("client thread");
				}
				else
					JOptionPane.showMessageDialog(null,"username/password is wrong");
					
			} catch (IOException e) {
		
				e.printStackTrace();
			}
			
			
		}
		public void run(){
			
		
			while(s.isConnected()){
		
					try {
						
						Packet pkt=(Packet) in.readObject();
						if(pkt.getType()==Packet.MSG){
								
							for(int i=client.getMessagePnl().getModel().size()-1;i>=0;i--){
								JLabel l=client.getMessagePnl().getModel().get(i);
								if(l.getText().trim().equals(pkt.getFrom())){
									client.getMessagePnl().list.setSelectedIndex(i);
									System.out.println("message from "+pkt.getFrom());
									break;
								}
								
							}
							client.getMessagePnl().append(pkt.getFrom()+" : "+pkt.getMsg());
						}
						else if(pkt.getType()==Packet.ONLINE){
							
							JLabel lbl=new JLabel(pkt.getMsg());
							ImageIcon img=WindowUtil.getImage(pkt.getB(),50,50);
							lbl.setIcon(img);
							DefaultListModel<JLabel> m = client.getMessagePnl().model;
							if(m==null){
								System.out.println("model is null");
							}
							client.getMessagePnl().model.addElement(lbl);
							//message.getModel().addElement(s.getMsg());
						}
						else if(pkt.getType()==Packet.OFFLINE){
							//System.out.println();
							//client.getMessagePnl().getModel().remove(client.getMessagePnl().getModel().indexOf(s.getMsg()));
							for(int i=client.getMessagePnl().getModel().size()-1;i>=0;i--){
								JLabel l=client.getMessagePnl().getModel().get(i);
								if(l.getText().trim().equals(pkt.getMsg())){
									client.getMessagePnl().getModel().remove(i);
									break;
								}
								
							}
						}
						
						else if(pkt.getType()==Packet.PROFILE){
							UsersDet user=pkt.getProf();
							if(user==null)
								System.out.println("data is not received");
							else{
								
								if(client.getProfile()==null){
									System.out.println("Profile not found");
								}
								else if(pkt.getTo().equals("myprofile"))
								client.getProfile().setDetails(user);
								else if(pkt.getTo().equals("accept")){
									client.getFrdreq().setDetails(user);
								}
								else if(pkt.getTo().equals("add"))
								client.getAddfrd().setDetails(user);
								else if(pkt.getTo().equals("view"))
								client.getView().setDetails(user);	
									
							}
						}
						else if(pkt.getType()==Packet.REQUEST){
							
							JLabel l=new JLabel(pkt.getMsg());
							ImageIcon img=WindowUtil.getImage(pkt.getB(),50,50);
							l.setIcon(img);
							client.getFrdreq().getModel().addElement(l);
							for(int i=client.getAddfrd().getModel().size()-1;i>=0;i--){
								JLabel l1=client.getAddfrd().getModel().get(i);
								if(l1.getText().trim().equals(pkt.getMsg())){
									client.getAddfrd().getModel().remove(i);
									break;
								}
								
							}
						}
						else if(pkt.getType()==Packet.ACCEPT){
							
							byte b[]=pkt.getB();
							if(b==null){
							JLabel l=client.getFrdreq().list.getSelectedValue();
							client.getFrdreq().getModel().removeElement(l);
							client.getView().getModel().addElement(l);
							JOptionPane.showMessageDialog(null, "Request Accepted");
							}
							else{
								
								JLabel l=new JLabel(pkt.getMsg());
								l.setIcon(WindowUtil.getImage(b, 50, 50));
								client.getView().getModel().addElement(l);

							}
						}
						else if(pkt.getType()==Packet.FRDLIST){
							JLabel l=new JLabel(pkt.getMsg());
							ImageIcon img=WindowUtil.getImage(pkt.getB(),50,50);
							l.setIcon(img);
							client.getAddfrd().getModel().addElement(l);
						}
						else if(pkt.getType()==Packet.LIST){
							/*JLabel l=new JLabel(pkt.getMsg());
							
							ImageIcon img=WindowUtil.getImage(pkt.getB());
							l.setIcon(WindowUtil.getScaledImage(img.getImage(), 50, 50));*/
							/*JFrame f=new JFrame();
							f.add(l);
							f.pack();
							f.setVisible(true);*/
							JLabel l=new JLabel(pkt.getMsg());
							ImageIcon img=WindowUtil.getImage(pkt.getB(),50,50);
							if(img!=null)
							//lblProPic.setIcon(WindowUtil.getScaledImage(img.getImage(),200,200));
							l.setIcon(img);
							else
							l.setIcon(WindowUtil.getScaledImage(new ImageIcon(Client.class.getResource("/images/default.png")).getImage(),200,200));
							client.getView().getModel().addElement(l);
						}
						else if(pkt.getType()==Packet.MSG1){
							
							String from=pkt.getFrom();
							//String to=pkt.getTo();
							String msg=pkt.getMsg();
							String append="";
							if(from.equals(username))
								append="You : "+msg;
							else
								append=from+" : "+msg;
						
							client.getMessagePnl().append(append);
						}
						else if(pkt.getType()==Packet.MSG2){
							for(int i=client.getMessagePnl().getModel().size()-1;i>=0;i--){
								JLabel l=client.getMessagePnl().getModel().get(i);
								if(l.getText().trim().equals(pkt.getMsg())){
									client.getMessagePnl().list.setSelectedIndex(i);
	
									break;
								}
								
							}
						}
					
					} 
					catch(EOFException e){
						
					}
					catch (IOException e) {
					
							//e.printStackTrace();
							System.out.println("eof "+e);
							break;
					}
					catch(NullPointerException e){
						e.printStackTrace();
						System.out.println("null "+e);
						break;
					}
					catch(ClassNotFoundException e){
						
						System.out.println("error "+e);
						break;
					}
					
			}
			
			try {
				
				client.getMessagePnl().append("cant connect to server");
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		
		}
		
	}
	public void sendMessage(String to, String msg) {
		try {
			if(to!=null){
				Packet pkt=new Packet(Packet.MSG,to,msg);
				t.out.writeObject(pkt);
				//t.out.flush();
				client.getMessagePnl().append("You-"+msg);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(int type, String msg) {
		try {
			
			Packet pkt=new Packet(type,msg);
			t.out.writeObject(pkt);			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void sendMessage(int type,String from, String to, String msg) {
	
		try {
			if(to!=null){
				Packet pkt=new Packet(type,from,to,msg);
				t.out.writeObject(pkt);
				client.getMessagePnl().append("You-"+msg);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}


	public void sendMessage(int update, UsersDet det) {
		try {
			if(det!=null){
				Packet pkt=new Packet(Packet.UPDATE,det,"myprofile");
				t.out.writeObject(pkt);
				JOptionPane.showMessageDialog(null, "Saved Successfully");
			}
			else
				System.out.println("In clientnet userdet null");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}


	public void sendMessage(int pkttype, String username, String type) {
		try {
		
				if(pkttype==Packet.MSG1){
					
				}
				Packet pkt=new Packet(pkttype,username,type);
				t.out.writeObject(pkt);
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		
	}
	
}
