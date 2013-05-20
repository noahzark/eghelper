package dao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import model.User;

public class BattleAnalyzer {
	private String src;	
	private boolean isNormal;

	public BattleAnalyzer(String string){
		this.src = string;
	}
	
	public TreeMap<Integer, User> analyze() throws IOException{
		if (Core.findString("\"result\"", src)!=null)
			isNormal = false;
		else
			isNormal = true;
		TreeMap<Integer, User> users = new TreeMap<Integer, User>();
		File f = new File(src);
		BufferedReader br = new BufferedReader(new FileReader(f));
		int no=1;
		if (isNormal){
			String s;
			while ((s = br.readLine())!=null){
				if (s.contains("選択")){
					s = Core.sortString(s, "user_id=", '\"');
					users.put(no++, new User(s));
				}
			}
		} else {
			int i=0;
			StringBuffer s = new StringBuffer();
			while ((i = br.read())!=-1){
				s.append((char)i);
				if (s.toString().contains("accentBtnS")){
					String s2 = Core.sortString(s.toString(), "user_id=", '\\');
					users.put(no++, new User(s2));
					s.setLength(0);
				}
			}
		}
		return users;
	}
}
