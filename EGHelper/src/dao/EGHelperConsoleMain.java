package dao;

import model.App;
import model.AppInfo;
import control.EGMessenger;

public class EGHelperConsoleMain extends Thread{
	
	EGMessenger carrier;
	Thread t;

	public EGHelperConsoleMain(boolean b){
		carrier = new EGMessenger(1000,1);
		carrier.println(EGMessenger.title+" V"+EGMessenger.version);
		carrier.setQuiteMode(b);
		if (!EGHelperMain.loadConfig(carrier))
			return;
	}
	
	public EGHelperConsoleMain(EGMessenger carrier2){
		carrier = carrier2;
	}
	
	public boolean startPlay(){
		AppInfo appinfo = new AppInfo(
				carrier.infoMap.get("sdk"),
				carrier.infoMap.get("digest")
		);
		App app = new App(
				carrier.infoMap.get("App-Id-1"),
				carrier.infoMap.get("App-Id-2"),
				carrier.infoMap.get("App-Id-3"),
				carrier.infoMap.get("app")
		);
		carrier.println("Connecting......");
		
		EGConnection egc = new EGConnection(appinfo, app, carrier);
		
		try {
			String s = carrier.infoMap.get("port");
			if (s!=null){
				int port = Integer.parseInt(s);
				egc.setUseProxy(true);
				egc.setProxy(carrier.infoMap.get("proxy"), port);
			}
		} catch (NumberFormatException e) {
			carrier.showError(e);
		}
		
		boolean succ = egc.connect();
		if (succ){
			carrier.println("Connected");
			carrier.enableBars();
			Core peg = new Core(
					egc.getHc(),
					app,
					carrier.infoMap.get("UID"),
					carrier
			);
			
			carrier.setMaxLines(Integer.parseInt(carrier.infoMap.get("MAXLINES")));
			peg.setPVEN(Integer.parseInt(carrier.infoMap.get("PVE_NORMAL")));
			peg.setPVEL(Integer.parseInt(carrier.infoMap.get("PVE_LARGE")));
			peg.setPVEU(Integer.parseInt(carrier.infoMap.get("PVE_URGENT")));
			peg.setWaitTime(Integer.parseInt(carrier.infoMap.get("WAIT")));
			peg.setSt_up(Integer.parseInt(carrier.infoMap.get("ST_UP")));
			peg.setSt_down(Integer.parseInt(carrier.infoMap.get("ST_DOWN")));
			peg.setPt_min(Integer.parseInt(carrier.infoMap.get("PVP_PT_MAX")));
			peg.setDefenseMax(Long.parseLong(carrier.infoMap.get("PVP_DEFENSE_MAX")));
			peg.setBp_combo(Integer.parseInt(carrier.infoMap.get("BP_COMBO")));
			peg.setUseBPMode(carrier.infoMap.get("NORMALBATTLE").equals("1"));
			peg.showRank = (carrier.infoMap.get("SHOWRANK").equals("1"));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			t = new Thread(peg);
			t.start();
			carrier.setGameThread(t);
		} else {
			carrier.showError("连接失败");
		}
		return true;
	}
	
	public void run(){
		if (carrier.checkMulti())
			return;
		carrier.listenPort();
		boolean flag = true;
		while (flag){
			flag = startPlay();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			if (t==null)
				continue;
			while (t.isAlive()){
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
			carrier.println("连接断开，等待10s后重试。。。");
		}
		
	}
	
	public static void main(String[] args) {
		EGHelperConsoleMain another = new EGHelperConsoleMain(false);
		if (another.carrier.infoMap!=null){
			EGHelperMain.showTactics(another.carrier);
			another.start();
		}
	}

}
