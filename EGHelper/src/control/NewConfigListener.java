package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import view.NewConfigDialog;

public class NewConfigListener implements ActionListener {
	NewConfigDialog ncd;
	EGMessenger carrier;

	public NewConfigListener(NewConfigDialog ncd,EGMessenger carrier) {
		this.ncd = ncd;
		this.carrier = carrier;
	}
	
	public void saveConfig(){
		try {
			carrier.print("保存配置文件中。。。");
			File f = new File("EGHelper.inf");
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("[AppInfo]");
			bw.newLine();
			bw.write("app = "+ncd.getjTextFieldApp().getText());
			bw.newLine();
			bw.write("sdk = "+ncd.getjTextFieldSDK().getText());
			bw.newLine();
			bw.write("digest = "+ncd.getjTextFieldDigest().getText());
			bw.newLine();
			bw.write("App-Id-1 = "+ncd.getjTextFieldAID1().getText());
			bw.newLine();
			bw.write("App-Id-2 = "+ncd.getjTextFieldAID2().getText());
			bw.newLine();
			bw.write("App-Id-3 = "+ncd.getjTextFieldAID3().getText());
			bw.newLine();
			bw.write("UID = "+ncd.getjTextFieldUID().getText());
			bw.newLine();
			bw.write("[Config]");
			bw.newLine();
			bw.write("MAXLINES = 1000");
			bw.newLine();
			bw.write("ST_UP = 20");
			bw.newLine();
			bw.write("ST_DOWN = 20");
			bw.newLine();
			bw.write("BP_COMBO = 5");
			bw.newLine();
			bw.write("WAIT = 5");
			bw.newLine();
			bw.write("NORMALBATTLE = 0");
			bw.newLine();
			bw.write("PVP_PT_MAX = 1000");
			bw.newLine();
			bw.write("PVP_DEFENSE_MAX = 100000");
			bw.newLine();
			bw.write("PVE_PRIORITY = 0");
			bw.newLine();
			bw.write("PVE_NORMAL = 0");
			bw.newLine();
			bw.write("PVE_LARGE = 0");
			bw.newLine();
			bw.write("PVE_URGENT = 0");
			bw.newLine();
			bw.close();
			carrier.println("完成");
		} catch (IOException e) {
			carrier.showError(e);
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource()==ncd.getjButtonConfirm()){
			saveConfig();
			ncd.dispose();
		}
		if (arg0.getSource()==ncd.getjButtonCancel()){
			ncd.dispose();
		}
		ncd.frame.getjButtonLoadCfg().setEnabled(true);
		ncd.frame.getjButtonNewCfg().setEnabled(true);
	}

}
