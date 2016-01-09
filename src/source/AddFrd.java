package source;

import javax.swing.JFrame;


public class AddFrd extends FriendReq {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public AddFrd(ClientNetwork c) {
		super(c);
		btnAccept.setText("Add");
		listBorder.setTitle("Add Friend");
	}
	public static void main(String args[]){
		WindowUtil.setNimbusLook();
		create();
	}
	public static void create() {
		f=new JFrame();
		f.setContentPane(new AddFrd(null));
		WindowUtil.setToCenter(f, 800, 600);
		f.setVisible(true);
		
	}

}
