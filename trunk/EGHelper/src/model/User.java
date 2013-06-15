package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import dao.StringScanner;

public class User {
	private String uid;
	private String name;
	private String level;
	private String friend;
	private String rank;
	private String point;
	private String defense="";
	private String winPercent="";
	private boolean special = false;
		
	public boolean isSpecial() {
		return special;
	}
	public void setSpecial(boolean special) {
		this.special = special;
	}
	public long getDefense() {
		return Long.parseLong(this.defense);
	}
	public void setDefense(String defense) {
		this.defense = defense;
	}
	public String getWinPercent() {
		return winPercent;
	}
	public void setWinPercent(String winPercent) {
		this.winPercent = winPercent;
	}
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
	
	public boolean getUserInfo(String src){
		if (StringScanner.findString(uid, src)==null)
			return false;
		String s;
		s = StringScanner.findString("Profile Page", src);
		s = StringScanner.sortString(s, "\">", '<');
		this.name = s;
		
		s = StringScanner.findString("Lv:", src);
		s = StringScanner.sortString(s, "</span> ", '<');
		this.level = s;
		
		s = StringScanner.findString("フレンド:", src);
		s = StringScanner.sortString(s, "</span> ", '/');
		this.friend = s;

		try {
			File f = new File(src);
			BufferedReader br = new BufferedReader(new FileReader(f));
			s = this.readLnUntil(br, "separator");
			s = br.readLine();
			s = StringScanner.sortString(s, "\">", '<');
			this.rank = s;
			
			s = this.readLnUntil(br, "separator");
			s = br.readLine();
			s = StringScanner.sortString(s, "\">", '<');
			this.point = s;
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String toString(){
		if (this.defense!=null)
			if (this.defense.length()>1)
				return "\n"+name+" Lv."+level+"\nFriend:"+friend+"\n"+rank+" - "+point+"pt\nDefense:"+this.defense+"\nWin:"+this.winPercent+"%\n两倍: "+this.special+"\n";
		return "\n"+name+" Lv."+level+"\nFriend:"+friend+"\n";
	}
}
