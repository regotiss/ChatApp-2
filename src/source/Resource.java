package source;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Resource {
	
	private static Statement s;
	static String months[]=(new DateFormatSymbols()).getShortMonths();;
	
	public static String getDate(Date d){
		
	
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
	
		
		String date=day+"-"+months[month]+"-"+year;
		
		return date;
		
	}
	public static boolean signup(String username,String pass){
		
		boolean ret=false;
		s=ConnectToDatabase.getS();
		
		try {
			ResultSet rs=s.executeQuery("select max(userid) from users");
			int id=0;
			while(rs.next())
				id=rs.getInt(1);
			id=id+1;
		
			s.execute("insert into users(userid,username,password) values("+id+",'"+username+
					"','"+pass+"')");
			
			ret=true;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void storeMessage(String msg,int senderid,int receiverid){
		
		try {
			
			s=ConnectToDatabase.getS();
			s.execute("insert into message values("+senderid+","+receiverid+",sysdate,'"+msg+"')");
			System.out.println(msg+" is stored");
		} catch (Exception e) {
				System.out.println("problem in storing message:"+e);
		}
		
	}
	public static String getName(String id)
	{
		String str="";
		try {
			
			s=ConnectToDatabase.getS();
			ResultSet rs=s.executeQuery("select name from users where userid="+id);
			while(rs.next())
			str=rs.getString(1);
			
		} catch (Exception e) {
				e.printStackTrace();
		}
		return str;
	}
	public  static int getId(String name) {
		
		int reqid=0;
		try
		{
			s=ConnectToDatabase.getS();
			if(s==null){
				System.out.println("Connect To database");
				return -1;
			}
			String sql="select userid from users where username='"+name+"'";
		
			ResultSet rs=s.executeQuery(sql);
		
			while(rs.next())
			reqid=rs.getInt(1);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return reqid;
	}
	public static boolean authenticate(String username,String pass) {
		
		boolean flg=false;
		try
		{
			s=ConnectToDatabase.getS();
			String sql="select password from users where username='"+username+"'";
			ResultSet rs=s.executeQuery(sql);
			while(rs.next()){
				
				if(rs.getString(1).equals(pass))
					flg=true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return flg;
	}
	public static boolean saveUser(UsersDet d,int id){
		
		boolean b=false;
		try
		{
			System.out.println("save user");
		
			s=ConnectToDatabase.getS();
			String sql="update users set name='"+d.getName()+"' where userid='"+id+"'";
			s.execute(sql);
			
			String date=getDate(d.getDob());
			System.out.println(date);
			sql="update users set dob='"+date+"' where userid='"+id+"'";
			s.execute(sql);
			
			sql="update users set address='"+d.getAddr()+"' where userid="+id;
			s.execute(sql);
			
			sql="update users set country='"+d.getCountry()+"' where userid="+id;
			s.execute(sql);

			sql="update users set district='"+d.getDistrict()+"' where userid="+id;
			s.execute(sql);

			sql="update users set city='"+d.getCity()+"' where userid="+id;
			s.execute(sql);


			sql="update users set address='"+d.getAddr()+"' where userid="+id;
			s.execute(sql);
			
			
			PreparedStatement ps=ConnectToDatabase.getCon().prepareStatement("update users set picture=? where userid="+id);   
			  
			ByteArrayInputStream fin=new ByteArrayInputStream(d.getPic());  
			ps.setBinaryStream(1,fin,fin.available());  
			int i=ps.executeUpdate();  
			System.out.println(i+" records affected");  
			
			b=true;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
	public static Packet getUserData(String name,String to){
		
		Packet pkt=null;
		try
		{
			String sql="select * from users where username='"+name+"'";
			System.out.println(sql);
			ResultSet rs=s.executeQuery(sql);
			while(rs.next()){
				
				Blob b=rs.getBlob("Picture"); 
				//byte barr[]=b.getBytes(1,(int)b.length());
				byte barr[];
				if(b!=null){
					barr=b.getBytes(1,(int)b.length());
					
				}
				else{
						File file=new File(MyProfile.class.getResource("/images/default.png").toString().substring(6));
						barr=Files.readAllBytes(file.toPath());
				}
				UsersDet det=new UsersDet(rs.getString("name"),rs.getDate("dob"),rs.getString("address"),
						rs.getString("country"),rs.getString("district"),rs.getString("city"),barr); 
				pkt=new Packet(Packet.PROFILE,det,to);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return pkt;

	}
	public static void getFriendReq(int id,Vector<String> v,Vector<byte[]> b){

		try {
			ResultSet rs=s.executeQuery("select username,picture from users NATURAL join friendrequest where senderid=userid and receiverid="+id+" and accepted='no'");
			
		
			while(rs.next()){
				v.add(rs.getString(1));
				Blob b1=rs.getBlob("Picture"); 
				if(b1!=null){
					byte barr[]=b1.getBytes(1,(int)b1.length());
					b.add(barr);
					}
					else {
						File file=new File(MyProfile.class.getResource("/images/default.png").toString().substring(6));
						byte barr[]=Files.readAllBytes(file.toPath());
						b.add(barr);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public static byte[] getPicture(int id){
	
		byte barr[]=null;
		try {
			ResultSet rs=s.executeQuery("select picture from users where userid="+id);
		
			while(rs.next()){
				
				Blob b1=rs.getBlob("Picture"); 
				if(b1!=null){
					barr=b1.getBytes(1,(int)b1.length());
					
				}
				else 
				{
						File file=new File(MyProfile.class.getResource("/images/default.png").toString().substring(6));
						barr=Files.readAllBytes(file.toPath());
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return barr;
	}
	public static byte[] getPicture(String username){
		
		byte barr[]=null;
		int sid=getId(username);
		System.out.println("resource "+sid);
		if(sid>0){
			barr=getPicture(sid);
		}
		return barr;
	}
	public static boolean sendFrdReq(int id,String username){
		boolean b=false;
		try {
			
			int sid=getId(username);
			if(sid>0){
				s.execute("insert into friendrequest values("+id+","+sid+", sysdate,'no')");
				b=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	public static boolean addFriend(int id,String username){
		boolean b=false;
		try {
			
			int sid=getId(username);
			if(sid>0){
				s.execute("update friendrequest set accepted = 'yes' where senderid="+sid+" and receiverid="+id);
				b=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
		
	}
	public static void getAddFrdList(int id,Vector<String> v,Vector<byte[]> b){

		try {
			//ResultSet rs=s.executeQuery("select username,picture from users WHERE userid <> "+id+" and userid not in (select senderid from friendrequest where receiverid="+id+")");
			ResultSet rs=s.executeQuery("select username,picture from users where userid <> "+id
					+" and userid not in ( select senderid from friendrequest where receiverid="+
					id+" UNION select receiverid from friendrequest where senderid="+id+")");
			while(rs.next()){
				v.add(rs.getString(1));
				Blob b1=rs.getBlob("Picture"); 
				if(b1!=null){
				byte barr[]=b1.getBytes(1,(int)b1.length());
				b.add(barr);
				}
				else {
					File file=new File(MyProfile.class.getResource("/images/default.png").toString().substring(6));
					byte barr[]=Files.readAllBytes(file.toPath());
					b.add(barr);
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void getFrdsList(int id,Vector<String> v,Vector<byte[]> b){

		try {
			//ResultSet rs=s.executeQuery("select username,picture from users WHERE userid <> "+id+" and userid not in (select senderid from friendrequest where receiverid="+id+")");
			ResultSet rs=s.executeQuery("select username,picture from users where userid <> "+id
					+" and userid in ( select senderid from friendrequest where receiverid="+
					id+" and accepted='yes' UNION select receiverid from friendrequest where senderid="+id+" and accepted='yes')");
			while(rs.next()){
				v.add(rs.getString(1));
				Blob b1=rs.getBlob("Picture"); 
				if(b1!=null){
					
					System.out.println("picture found "+rs.getString(1));
					byte barr[]=b1.getBytes(1,(int)b1.length());
					b.add(barr);
			
				}
				else {
					File file=new File(MyProfile.class.getResource("/images/default.png").toString().substring(6));
					byte barr[]=Files.readAllBytes(file.toPath());
					b.add(barr);
					System.out.println("picture not found");
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Vector<String> getFriendReq(int id){
		Vector<String> v=new Vector<>();
		try {
			ResultSet rs=s.executeQuery("select username,picture from users where userid <> "+id
					+" and userid in ( select senderid from friendrequest where receiverid="+
					id+" UNION select receiverid from friendrequest where senderid="+id+")");
			
			
		
			while(rs.next()){
				v.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return v;
	}
	public static Vector<Boolean> getMessageList(int senderid,int receiverid,Vector<String> msg){
		
		Vector<Boolean> b=new Vector<Boolean>();
		try {
			ResultSet rs=s.executeQuery("select senderid, message,dateofmessage from message where senderid="+senderid+" and receiverid="+
								receiverid+" union select senderid, message,dateofmessage from message where senderid="+
								receiverid+" and receiverid="+senderid+" order by dateofmessage");
			
			
			while(rs.next()){
				
				b.add(rs.getInt("senderid")==senderid);
				msg.add(rs.getString("message"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return b;

	}
	
}
