package source;

import java.io.Serializable;

public class Packet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MSG=1,ONLINE=2,OFFLINE=3,AUTH=0,SIGNUP=13,UPDATE=4,PROFILE=5,REQUEST=6,ACCEPT=7,ADD=8,FRDLIST=9,LIST=10,MSG1=11,MSG2=12;
	private String msg;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int type;
	private String to;
	private String from;
	private UsersDet prof;
	private byte[] b;
	
	public byte[] getB() {
		return b;
	}

	public void setB(byte[] b) {
		this.b = b;
	}

	public UsersDet getProf() {
		return prof;
	}

	public void setProf(UsersDet prof) {
		this.prof = prof;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Packet(int type,String msg){
		this.msg=msg;
		this.type=type;
	}
	public Packet(int type,String to,String msg){
		this.msg=msg;
		this.type=type;
		this.to=to;
	}
	
	public Packet(int type,String from,String to,String msg){
		this.msg=msg;
		this.type=type;
		this.to=to;
		this.from=from;
	}
	public Packet(int type,UsersDet prof,String to){
		this.type=type;
		this.prof=prof;
		this.to=to;
	}
	public Packet(int type,String msg,byte[] b){
		this.msg=msg;
		this.type=type;
		this.b=b;
	}

	/*public Packet(int type, String from, String to, String msg) {
		
	}*/

}
