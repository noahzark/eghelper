package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.InfoFrame;

public class InfoListener implements ActionListener {
	InfoFrame frame;
	EGMessenger carrier;

	public InfoListener(InfoFrame infoFrame, EGMessenger carrier) {
		frame = infoFrame;
		this.carrier = carrier;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == frame.getjButtonClear()){
			frame.getTextArea().setText(null);
			frame.getTextAreaList().setText(null);
		}
		if (ae.getSource() == frame.getjButtonClose()){
			System.exit(0);
		}
		if (ae.getSource() == frame.getjButtonManual()){
			carrier.fight();
		}
	}

}
