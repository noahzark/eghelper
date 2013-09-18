package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import model.Page;
import control.EGHelperGUIMain;
import control.EGMessenger;

public class EGHelperMain {
	
	public static void showTactics(EGMessenger carrier){
		carrier.print(
				"当前策略：\n"+
				"1.剩余校园PVP免费BP时继续参战；\n" +
				"2.剩余ST+EXP大于升级所需EXP时自动探索/战斗；\n" +
				"3.保持ST在"+carrier.infoMap.get("ST_DOWN")+"与最大ST-" +
				carrier.infoMap.get("ST_UP")+"间浮动；\n"+
				"4.刷新间隔60+0~60+0~"+carrier.infoMap.get("WAIT")+"s；\n"+
				"5.当BP大于等于"+carrier.infoMap.get("BP_COMBO")+"时继续参加PVP活动战斗；\n"+
				"6.PVP战斗只攻击校园点数低于+"+carrier.infoMap.get("PVP_PT_MAX")+"的敌人；\n"+
				"7.PVP活动只攻击防御点数低于+"+carrier.infoMap.get("PVP_DEFENSE_MAX")+"的敌人；\n"+
				"8.当BP大于等于"+carrier.infoMap.get("PVE_NORMAL")+"时，参加好友/自己普通PVE战斗；\n"+
				"9.当BP大于等于"+carrier.infoMap.get("PVE_LARGE")+"时，参加好友/自己特大PVE战斗；\n"+
				"10.当BP大于等于"+carrier.infoMap.get("PVE_URGENT")+"时，参加好友/自己紧急PVE战斗；\n"+
				"11.当BP大于等于4时，自动参加当前PVE任务列表第一的战斗；\n"+
				"12."
		);
		if (carrier.infoMap.get("NORMALBATTLE").equals("0"))
			carrier.println("不使用BP参加校园PVP。");
		else
			carrier.println("使用BP参加校园PVP。");
	}
	
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
	
	public static boolean checkVersion(){
		DefaultHttpClient hc = new DefaultHttpClient();
		HttpHost target = new HttpHost("eghelper.googlecode.com", 80, "http");
		HttpGet req = new HttpGet("/svn/trunk/EGHelper/EGINFO.dat");
		try {
			HttpResponse rsp = hc.execute(target, req);
			int status = rsp.getStatusLine().getStatusCode();
			HttpEntity entity = rsp.getEntity();
			if (status != 200) {
				System.out.println("Status:"+status);
				EntityUtils.consume(entity);
				return false;
            }
			if (entity != null) {
				InputStream instreams = entity.getContent();
				Scanner sc = new Scanner(instreams);
				TreeMap<String, String> map = new TreeMap<String, String>();
				String p,p2,t;
				while(sc.hasNext()){
					t = sc.next();
					if (!t.contains("Noahzark")){
						System.out.println("版本校验失败，请联系原作者。");
						System.out.println("Failed to check the version information, please contact the author.");
						return true;
					}
						
					while(sc.hasNext()){
						p = sc.next();
						t = sc.next();
						if (!t.equals("=")){
							System.out.println("版本校验失败，请联系原作者。");
							System.out.println("Failed to check the version information, please contact the author.");
							return true;
						}
						p2 = sc.next();
						map.put(p, p2);
					}
				}
				if (Integer.parseInt(map.get("reversion"))>EGMessenger.versionNumber) {
					System.out.println("EG助手已有新版本，请前往 "+map.get("download")+" 进行更新。");
					System.out.println("We've updated the EGHelper, please go to "+map.get("download")+" to download.");
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		if (EGHelperMain.checkVersion()){
			return;
		}
		
		if (args.length>0)
			System.out.println("Arguments:");
		for (String s : args){
			System.out.println(s);
		}
		switch(args.length){
			case 0:{
				EGHelperGUIMain another = new EGHelperGUIMain(false);
				another.run();
			}
				break;
			case 1:{
				if (args[0].equals("console")){
					EGHelperConsoleMain another = new EGHelperConsoleMain(false);
					if (another.carrier.infoMap!=null){
						EGHelperMain.showTactics(another.carrier);
						another.start();
					}
				} else if (args[0].equals("true")){
					EGHelperGUIMain another = new EGHelperGUIMain(true);
					another.run();
				} else {
					EGHelperGUIMain another = new EGHelperGUIMain(false);
					another.run();
				}
			}
				break;
			case 2:{
				if (args[1].equals("true")){
					EGHelperGUIMain another = new EGHelperGUIMain(true);
					another.run();
				} else {
					int port = Integer.parseInt(args[1]);
					EGHelperConsoleMain another = new EGHelperConsoleMain(false);
					another.carrier.infoMap.put("proxy", "127.0.0.1");
					another.carrier.infoMap.put("port", ""+port);
					if (another.carrier.infoMap!=null)
						another.start();
				}
			}
			default:{
				EGHelperConsoleMain another = new EGHelperConsoleMain(true);
				if (another.carrier.infoMap!=null)
					another.start();
			}
		}
	}

}
