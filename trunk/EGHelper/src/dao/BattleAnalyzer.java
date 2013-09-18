package dao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	public TreeMap<Integer, User> analyze(){
		if (StringScanner.findString("人気投票", src)!=null)
			isNormal = false;
		else
			isNormal = true;
		TreeMap<Integer, User> users = new TreeMap<Integer, User>();
		
		try {
			File f = new File(src);
			BufferedReader br = new BufferedReader(new FileReader(f));
			int no=1;
			if (isNormal){
				String s;
				while ((s = br.readLine())!=null){
					if (s.contains("選択")){
						s = StringScanner.sortString(s, "user_id=", '\"');
						users.put(no++, new User(s));
					}
				}
			} else {
				int i=0;
				StringBuffer s = new StringBuffer();
				int level = 1;
				String defense="",winPercent="";
				boolean special = false;
				while ((i = br.read())!=-1){
					s.append((char)i);
					switch(level){
					case 1:
						{
							if (s.toString().contains("勝つとイベントpt2倍!")){
								special = true;
								s.setLength(0);
							}
							if (s.toString().contains("勝率")){
								String s2 = StringScanner.sortString(s.toString(), "pt:約", ')');
								defense=s2;
								s.setLength(0);
								level++;
							}
						}
						break;
					case 2:
						{
							if (s.toString().contains("を応援中")){
								String s2 = StringScanner.sortString(s.toString(), "：", '%');
								winPercent=s2;
								s.setLength(0);
								level++;
							}
						}
						break;
					case 3:
						{
							if (s.toString().contains("accentBtnS")){
								String s2 = StringScanner.sortString(s.toString(), "user_id=", '\"');
								User u = new User(s2);
								u.setDefense(defense);
								u.setWinPercent(winPercent);
								u.setSpecial(special);
								users.put(no++, u);
								s.setLength(0);
								level = 1;
								special = false;
							}
						}
						break;
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return users;
	}
}
