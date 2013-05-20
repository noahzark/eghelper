package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import dao.Core;

public class User {
	private String uid;
	private String name;
	private String level;
	private String friend;
	private String rank;
	private String point;	
	public String getUid() {
		return uid;
	}
	public String getName() {
		return name;
	}
	public String getLevel() {
		return level;
	}
	public String getFriend() {
		return friend;
	}
	public String getRank() {
		return rank;
	}
	public int getPoint() {
		return Integer.parseInt(point);
	}
	public User(String s) {
		this.uid = s;
	}
	
	private String readLnUntil(BufferedReader br, String string) throws IOException {
		String s;
		do{
			s = br.readLine();
		}while(!s.contains(string));
		return s;
	}
	
	public boolean getUserInfo(String src) throws IOException{
		if (Core.findString(uid, src)==null)
			return false;
		String s;
		s = Core.findString("Profile Page", src);
		s = Core.sortString(s, "\">", '<');
		this.name = s;
		
		s = Core.findString("Lv:", src);
		s = Core.sortString(s, "</span> ", '<');
		this.level = s;
		
		s = Core.findString("フレンド:", src);
		s = Core.sortString(s, "</span> ", '/');
		this.friend = s;

		File f = new File(src);
		BufferedReader br = new BufferedReader(new FileReader(f));
		s = this.readLnUntil(br, "separator");
		s = br.readLine();
		s = Core.sortString(s, "\">", '<');
		this.rank = s;
		
		s = this.readLnUntil(br, "separator");
		s = br.readLine();
		s = Core.sortString(s, "\">", 'p');
		this.point = s;
		br.close();
		
		return true;
	}
	
	public String toString(){
		return "\n"+name+" Lv."+level+"\nFriend:"+friend+"\n"+rank+" - "+point+"pt\n";
	}
}
