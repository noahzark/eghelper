package control;

import javax.swing.SwingUtilities;

import view.InfoFrame;
import view.StartMenuFrame;

public class EGHelperGUIMain {
	private boolean debug = true;
	private boolean quiteMode = false;
	
	public EGHelperGUIMain(boolean b) {
		quiteMode = b;
	}

	public void run(){
		final EGMessenger carrier = new EGMessenger(1000,1);
		carrier.setDebugMode(debug);
		carrier.setQuiteMode(quiteMode);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				InfoFrame inst = new InfoFrame("EG助手 V"+carrier.version,carrier);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StartMenuFrame inst = new StartMenuFrame("主菜单",carrier);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		EGHelperGUIMain another = new EGHelperGUIMain(false);
		another.run();
	}

}
