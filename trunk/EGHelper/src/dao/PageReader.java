package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

import control.EGMessenger;

public class PageReader {
	File src;
	EGMessenger carrier = null;

	public PageReader(String fileName, EGMessenger carrier) {
		this.src = new File(fileName);
		this.carrier = carrier;
	}
	
	private boolean check(TreeMap<String, String> infoMap) {
		String s[] ={
				"DRIVER",
				"HOST",
				"MYPAGE",
				"EVENT_BATTLE",
				"SHOW_USER",
				"USER_DETAIL"
		};
		if (infoMap.size()<s.length)
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
		carrier.print("读取页面地址配置文件中。。。");
		TreeMap<String, String> map = new TreeMap<String, String>();
		try {
			carrier.println(src.getAbsolutePath());
			Scanner sc = new Scanner(new FileInputStream(src));
			String t = sc.nextLine();
			if (!t.equals("[Pages]"))
				return null;
			String p,p2;
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
