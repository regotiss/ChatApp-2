package source;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class Signup extends Login{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Signup(){
		mainPnl.remove(pnlSignup);
		t.setTitle("Sign Up");
		btnLogin.setText("Sign Up");
		setVisible(true);
	}
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
					Signup frame = new Signup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
