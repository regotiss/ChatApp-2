package source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;



public class ServerNetwork1 {

	
	private int port;
	private Server server;
	private ServerSocket serverSocket;
	private Socket s;
	private List<ClientThread> t;
	private boolean connected;

	public ServerNetwork1(int port, Server server) {
		this.port=port;
		this.server=server;
		t=new ArrayList<>();
	}

	public void start() {
		try {
			connected=true;
			serverSocket=new ServerSocket(port);

			server.append("server is started at port "+serverSocket.getLocalPort());
			while(connected){
				s=serverSocket.accept();
				t.add(new ClientThread(s));
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	public void stop() {
		try {
			connected=false;
			if(serverSocket!=null)
			serverSocket.close();
			for(int i = 0; i < t.size(); ++i) 
			{
				t.get(i).close();
			}
			

		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	class ClientThread extends Thread
	{
		
		private Socket s;
		public  ObjectInputStream in;
		public ObjectOutputStream out;
		private String username;
		private Set<Integer> frdids;
		private int id;
		

		ClientThread(Socket s){
			this.s=s;
			try {
				
				out=new ObjectOutputStream(s.getOutputStream());
				out.flush();
				in=new ObjectInputStream(s.getInputStream());
				
				Packet pkt=(Packet) in.readObject();
				
				boolean res=false;
				username=pkt.getTo();
				String password=pkt.getMsg();
				if(pkt.getType()==Packet.AUTH){
					
					res=Resource.authenticate(username, password);
				}
				else 
				{
					res=Resource.signup(username, password);
				}
				
			
				if(res)
				{
					out.writeObject(new Packet(Packet.AUTH, "user"));
					System.out.println(""+username);
					id=Resource.getId(username);
					getFrdList();
					server.append(username+" is just connected");
					
					
					server.getModel().addElement(username);
					System.out.println(""+username);
					for(int i=t.size()-1;i>=0;i--){
						
						System.out.println(""+username);
						if(frdids.contains(t.get(i).id)){
							
							byte b[]=Resource.getPicture(t.get(i).id);
							byte b1[]=Resource.getPicture(id);
							out.writeObject(new Packet(Packet.ONLINE,t.get(i).username,b));
							t.get(i).out.writeObject(new Packet(Packet.ONLINE,username,b1));
						}
				
					}
					start();
				}
				else
					out.writeObject(new Packet(Packet.AUTH, "not user"));
					
				
			} catch (Exception e) {
		
				System.out.println("problem in reading username "+e);
			}
			
		}
		public void getFrdList(){
			frdids=new HashSet<>();
			Statement s=ConnectToDatabase.getS();
			String sql="select receiverid from friendrequest where senderid="+id+" and accepted='yes' "+ 
					"UNION select senderid from friendrequest where receiverid="+id+" and accepted='yes'";
			ResultSet rs;
			try {
				rs = s.executeQuery(sql);
				while(rs.next()){
					frdids.add(rs.getInt(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		public void run(){
			
		
			while(s.isConnected()){
				
				try {
						
						Packet pkt=(Packet) in.readObject();
						
						if(pkt.getType()==Packet.MSG)
						{
							Packet p=new Packet(Packet.MSG2,username);
							for(int i=0;i<t.size();i++){
								
								if(pkt.getTo().equals(t.get(i).username)){
								
									
									t.get(i).writeMsg(p);
									t.get(i).writeMsg(pkt);
									
									Resource.storeMessage(pkt.getMsg(),id, t.get(i).id);
									break;
								}
							}
						
		
						}
						else if(pkt.getType()==Packet.UPDATE){
							
							if(Resource.saveUser(pkt.getProf(), id))
								server.append(username+" profile is updated");
							else
								server.append(username+" profile is not updated");
						}
						else if(pkt.getType()==Packet.PROFILE){
							
							System.out.println("profile ");
							System.out.println(pkt.getTo());
							Packet p=Resource.getUserData(pkt.getTo(),pkt.getMsg());
							if(p==null)
								server.append("cant retrive details of "+pkt.getMsg());
							else{
								writeMsg(p);
							}
						}
						else if(pkt.getType()==Packet.REQUEST){
							
							Vector<String> v=new Vector<String>();
							Vector<byte[]> b=new Vector<byte[]>();
							Resource.getFriendReq(id,v,b);
						
							for(int i=0;i<v.size();i++){
									writeMsg(new Packet(Packet.REQUEST,v.get(i),b.get(i)));
							}
							
						}
						else if(pkt.getType()==Packet.ACCEPT){
							if(!Resource.addFriend(id, pkt.getMsg()))
								server.append("cant add friend "+pkt.getMsg()+" requested to  "+username);
							else{
								Packet p=new Packet(Packet.ACCEPT,username,pkt.getMsg());
								for(int i=t.size()-1;i>=0;i--){
									
								
									if(t.get(i).username.equals(pkt.getMsg())){
										
										byte b[]=Resource.getPicture(t.get(i).id);
										byte b1[]=Resource.getPicture(id);
										out.writeObject(new Packet(Packet.ONLINE,t.get(i).username,b));
										t.get(i).out.writeObject(new Packet(Packet.ONLINE,username,b1));
										t.get(i).out.writeObject(new Packet(Packet.ACCEPT,username,b1));
										break;
									}
							
								}
								writeMsg(p);
							}
						}
						else if(pkt.getType()==Packet.FRDLIST){
							Vector<String> v=new Vector<String>();
							Vector<byte[]> b=new Vector<byte[]>();
							Resource.getAddFrdList(id,v,b);
						
							for(int i=0;i<v.size();i++){
									writeMsg(new Packet(Packet.FRDLIST,v.get(i),b.get(i)));
							}
		
						}
						else if(pkt.getType()==Packet.ADD){
							if(Resource.sendFrdReq(id, pkt.getMsg())){
								
								byte b[]=Resource.getPicture(id);
								if(b!=null){
									for(int i=0;i<t.size();i++){
										
										if(pkt.getMsg().equals(t.get(i).username)){
										
											
											t.get(i).writeMsg(new Packet(Packet.REQUEST,username,b));
											
											break;
										}
									}
								
								}
								else
									server.append("cant send picture to "+pkt.getMsg()+" by "+username);
							}
							else
								server.append("cant send request to "+pkt.getMsg()+" by "+username);
								
						}
						else if(pkt.getType()==Packet.LIST){
							Vector<String> v=new Vector<String>();
							Vector<byte[]> b=new Vector<byte[]>();
							Resource.getFrdsList(id, v, b);
						
							for(int i=0;i<v.size();i++){
									writeMsg(new Packet(Packet.LIST,v.get(i),b.get(i)));
									
							}
						}
						else if(pkt.getType()==Packet.MSG1){
							int receriverid=Resource.getId(pkt.getMsg());
							if(receriverid>0)
							{
								Vector<String> msg=new Vector<>();
								Vector<Boolean> b;
								b=Resource.getMessageList(id, receriverid, msg);
								if(b!=null){
									for(int i=0;i<b.size();i++){
										Packet p;
										if(b.get(i).equals(true)){
											p=new Packet(Packet.MSG1,username,pkt.getMsg(),msg.get(i));
										}
										else{
											p=new Packet(Packet.MSG1,pkt.getMsg(),username,msg.get(i));
										}
										
										writeMsg(p);
										
									/*	p=new Packet(Packet.MSG2,username);
										for(int j=0;j<t.size();j++){
											
											if(pkt.getMsg().equals(t.get(j).username)){
																					
												t.get(j).writeMsg(p);
												break;
											}
										}
									*/
									}
								}
								
			
							}	
							else
								server.append("cant find receiver "+pkt.getMsg());
						}
				
						
					} 
					catch (IOException |NullPointerException |ClassNotFoundException e) {
						//e.printStackTrace();
						server.append("client "+username+" is disconnected");
						server.getModel().remove(server.getModel().indexOf(username));
						/*for(int i=t.size()-1;i>=0;i--){
							
							try {
								if(t.get(i).s.isConnected())
								t.get(i).out.writeObject(new Packet(Packet.OFFLINE,username));
							} catch (IOException e1) {
								System.out.println(e);
							}
						}*/
						for(int i=t.size()-1;i>=0;i--){
							
							if(frdids.contains(t.get(i).id)){
								
								if(t.get(i).s.isConnected())
									try {
										t.get(i).out.writeObject(new Packet(Packet.OFFLINE,username));
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
							}
					
						}
						try {
							server.append(username+" is disconnected");
							s.close();
						} catch (IOException e1) {
							System.out.println("Exception while closing "+username+" connection");
						}
						t.remove(this);
						break;

					}
					
					System.out.println(username+" connected "+s.isConnected());
			}
		
		}
		public boolean writeMsg(Packet text){
			
			if(!s.isConnected())
			{
				server.append(username+ " socket is closed");
				close();
				t.remove(this);
				return false;
			}
			try {
				
				out.writeObject(text);
				out.flush();
				
			} catch (IOException e) {
				
				//e.printStackTrace();
				server.append("error while sending to "+username+" -"+e);
			}
			return true;
		}
		private void close() {
			try {
				if(out != null) out.close();
			
				if(in != null) in.close();
			
				if(s != null) s.close();
			}
			catch (Exception e) {}
			
		}
		
	}
	public void broadCast(String text)
	{
		
		server.append(text);
		Packet pkt=new Packet(Packet.MSG,text);
		for(int i=t.size()-1;i>=0;i--){
			if(!t.get(i).writeMsg(pkt)){
				
				server.append(t.get(i).username+" is disconnected");
				t.remove(i);
				
			}
		}
	}
	public void sendMessage(String text) {
		
		
	}

}
