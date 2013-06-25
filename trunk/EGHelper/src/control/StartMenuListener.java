package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.EGHelperConsoleMain;
import dao.EGHelperMain;

import view.NewConfigDialog;
import view.ProxyConfigDialog;
import view.StartMenuFrame;

public class StartMenuListener implements ActionListener {
	private StartMenuFrame frame;
	EGMessenger carrier;

	public StartMenuListener(StartMenuFrame frame, EGMessenger carrier) {
		this.frame = frame;
		this.carrier = carrier;
	}
	
	public void checkAllButtons(){
		frame.getjButtonLoadCfg().setEnabled(false);
		frame.getjButtonNewCfg().setEnabled(false);
		frame.getjButtonSetProxy().setEnabled(false);
		frame.getjButtonConfig().setEnabled(false);
		frame.getjButtonStart().setEnabled(false);
		
		frame.getjButtonLoadCfg().setEnabled(true);
		frame.getjButtonNewCfg().setEnabled(true);
		frame.getjButtonSetProxy().setEnabled(false);
		frame.getjButtonConfig().setEnabled(false);
		frame.getjButtonStart().setEnabled(false);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==frame.getjButtonLoadCfg()){
			if (EGHelperMain.loadConfig(carrier)){
				frame.getjButtonSetProxy().setEnabled(true);
			} else {
				frame.getjButtonLoadCfg().setEnabled(false);
			}
		}
		if (ae.getSource()==frame.getjButtonNewCfg()){
			checkAllButtons();
			frame.getjButtonLoadCfg().setEnabled(false);
			frame.getjButtonNewCfg().setEnabled(false);
			NewConfigDialog inst = new NewConfigDialog(frame, carrier);
			inst.setVisible(true);
		}
		if (ae.getSource()==frame.getjButtonSetProxy()){
			ProxyConfigDialog inst = new ProxyConfigDialog(frame, carrier);
			inst.setVisible(true);
		}
		if (ae.getSource()==frame.getjButtonConfig()){
			EGHelperMain.showTactics(carrier);
			frame.getjButtonStart().setEnabled(true);
		}
		if (ae.getSource()==frame.getjButtonStart()){
			checkAllButtons();
			EGHelperConsoleMain another = new EGHelperConsoleMain(carrier);
			another.start();
			frame.dispose();
		}
	}

}
