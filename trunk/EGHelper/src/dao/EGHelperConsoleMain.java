package dao;

import java.util.TreeMap;

import model.App;
import model.AppInfo;
import model.Page;
import control.EGMessenger;

public class EGHelperConsoleMain extends Thread{
	private boolean debug = true;
	
	EGMessenger carrier;
	Thread t;
	
	public static boolean loadConfig(EGMessenger carrier) {
		boolean flag = true;
		flag &= ((carrier.infoMap = (new ConfigReader("EGHelper.inf", carrier)).load())!=null);
		if (!flag){
			carrier.println("应用配置文件读取失败，请重新建立。");
			return false;
		}
		TreeMap<String, String> pages;
		flag &= ((pages = (new PageReader("PageConfig.inf", carrier)).load())!=null);
		if (flag){
			carrier.pages = new Page(pages);
		} else {
			carrier.println("页面地址配置文件读取失败，请重新建立。");
			return false;
		}
		return true;
	}

	public EGHelperConsoleMain(boolean b){
		carrier = new EGMessenger(1000,1);
		carrier.println(EGMessenger.title+" V"+EGMessenger.version);
		carrier.setDebugMode(debug);
		carrier.setQuiteMode(b);
		if (!EGHelperConsoleMain.loadConfig(carrier))
			return;
	}
	
	public EGHelperConsoleMain(EGMessenger carrier2){
		carrier = carrier2;
	}
	
	public void startPlay(){
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
			peg.setBp_combo(Integer.parseInt(carrier.infoMap.get("BP_COMBO")));
			peg.setUseBPMode(carrier.infoMap.get("NORMALBATTLE").equals("1"));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			t = new Thread(peg);
			t.start();
			carrier.setGameThread(t);
		} else {
			carrier.println("失败\n"+"请稍后重试或联系作者");
		}
	}
	
	public void run(){
		while (true){
			startPlay();
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
		}
		
	}
	
	public static void showTactics(EGMessenger carrier){
		carrier.print(
				"当前策略：\n"+
				"1.剩余校园PVP免费BP时继续参战；\n" +
				"2.剩余ST+EXP大于升级所需EXP时自动探索/战斗；\n" +
				"3.保持ST在"+carrier.infoMap.get("ST_DOWN")+"与最大ST-" +
				carrier.infoMap.get("ST_UP")+"间浮动；\n"+
				"4.刷新间隔10+0~10+0~"+carrier.infoMap.get("WAIT")+"s；\n"+
				"5.当BP大于等于"+carrier.infoMap.get("BP_COMBO")+"时继续参加PVP活动战斗；\n"+
				"6.PVP战斗只攻击校园点数低于+"+carrier.infoMap.get("PVP_PT_MAX")+"的敌人；\n"+
				"7.当BP大于等于"+carrier.infoMap.get("PVE_NORMAL")+"时，参加好友普通PVE战斗；\n"+
				"8.当BP大于等于"+carrier.infoMap.get("PVE_LARGE")+"时，参加好友特大PVE战斗；\n"+
				"9.当BP大于等于"+carrier.infoMap.get("PVE_URGENT")+"时，参加好友紧急PVE战斗；\n"+
				"10.当BP大于等于4时，自动参加当前PVE任务列表第一的战斗；\n"+
				"11."
		);
		if (carrier.infoMap.get("NORMALBATTLE").equals("0"))
			carrier.println("不使用BP参加校园PVP。");
		else
			carrier.println("使用BP参加校园PVP。");
	}
	
	public static void main(String[] args) {
		EGHelperConsoleMain another = new EGHelperConsoleMain(false);
		if (another.carrier.infoMap!=null){
			EGHelperConsoleMain.showTactics(another.carrier);
			another.start();
		}
	}

}
