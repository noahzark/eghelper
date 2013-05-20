package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import view.ProxyConfigDialog;
import view.StartMenuFrame;

public class ProxyConfigListener implements ActionListener,ItemListener {
	ProxyConfigDialog dialog;
	EGMessenger carrier;
	StartMenuFrame frame;

	public ProxyConfigListener(ProxyConfigDialog proxyConfigDialog, StartMenuFrame frame, EGMessenger carrier) {
		this.carrier = carrier;
		dialog = proxyConfigDialog;
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource()==dialog.getjButtonConfirm()){
			carrier.infoMap.remove("proxy");
			carrier.infoMap.remove("port");
			if (dialog.getjCheckBoxUseProxy().isSelected()){
				carrier.infoMap.put("proxy", dialog.getJTextFieldAdress().getText());
				carrier.infoMap.put("port", dialog.getjTextFieldPort().getText());
			}
			carrier.println("代理设置完毕："+(carrier.infoMap.get("port") != null));
			frame.getjButtonConfig().setEnabled(true);
			dialog.dispose();
		}
	}

	public void itemStateChanged(ItemEvent arg0) {
		if (arg0.getSource()==dialog.getjCheckBoxUseProxy()){
			switch(arg0.getStateChange()){
				case 1:
					dialog.getJTextFieldAdress().setEnabled(true);
					dialog.getjTextFieldPort().setEnabled(true);
					break;
				case 2:
					dialog.getJTextFieldAdress().setEnabled(false);
					dialog.getjTextFieldPort().setEnabled(false);
					break;
			}
		}
	}

}
