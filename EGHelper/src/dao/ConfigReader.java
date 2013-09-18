package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

import control.EGMessenger;

public class ConfigReader {
	File src;
	EGMessenger carrier = null;

	public ConfigReader(String fileName, EGMessenger carrier) {
		this.src = new File(fileName);
		this.carrier = carrier;
	}

	private boolean check(TreeMap<String, String> infoMap) {
		String s[] ={
				"App-Id-1",
				"App-Id-2",
				"App-Id-3",
				"UID",
				"MAXLINES",
				"ST_UP",
				"ST_DOWN",
				"BP_COMBO",
				"WAIT",
				"NORMALBATTLE",
				"PVP_PT_MAX",
				"PVP_DEFENSE_MAX",
				"PVE_PRIORITY",
				"PVE_NORMAL",
				"PVE_LARGE",
				"PVE_URGENT",
				"SHOWRANK"
		};
		if (infoMap.size()!=s.length)
			return false;
		String t;
		for (int i=0;i<s.length;i++){
			t = infoMap.get(s[i]);
			if (t==null)
				return false;
		}
		return true;
	}

	public TreeMap<String, String> load() {
		carrier.print("读取应用配置文件中。。。");
		TreeMap<String, String> map = new TreeMap<String, String>();
		try {
			carrier.println(src.getAbsolutePath());
			Scanner sc = new Scanner(new FileInputStream(src));
			String t = sc.nextLine();
			if (!t.equals("[AppInfo]"))
				return null;
			String p,p2;
			while(sc.hasNext()){
				p = sc.next();
				t = sc.next();
				if (!t.equals("="))
					return null;
				p2 = sc.next();
				map.put(p, p2);
				if (p.equals("UID"))
					break;
			}
			
			t = sc.next();
			if (!t.equals("[Config]"))
				return null;
			while(sc.hasNext()){
				p = sc.next();
				t = sc.next();
				if (!t.equals("="))
					return null;
				p2 = sc.next();
				map.put(p, p2);
			}
		} catch (FileNotFoundException e) {
			carrier.showError(e);
			return null;
		}
		if (!check(map))
			return null;
		carrier.println("完成");
		return map;
	}
}
