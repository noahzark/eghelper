package model;
import java.io.UnsupportedEncodingException;


public class Mission implements Comparable<Mission>{
	private String title;
	private String level;
	private String user;
	private String[] hp;
	private String uid;
	private String startTime;
	private String escapeTime;
	private String status;
	private String mid;
		
	public String getUser() {
		return user;
	}

	public String getTitle() {
		return title;
	}
	
	public String getUid() {
		return uid;
	}

	public String getStatus() {
		return status;
	}

	public String getMid() {
		return mid;
	}

	public String getEscapeTime() {
		return escapeTime;
	}
	
	public int getHpNow() {
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<this.hp[0].length();i++){
			char c = this.hp[0].charAt(i);
			if (c>='0'&&c<='9')
				sb.append(this.hp[0].charAt(i));
		}
		return Integer.parseInt(sb.toString());
	}

	public String[] getHp() {
		return hp;
	}

	public void setHp(String[] hp) {
		this.hp = hp;
	}

	public Mission(String title2, String level2, String user2, String uid2,
			String startTime2, String escapeTime2, String status2, String mid2) throws UnsupportedEncodingException {
		title = new String(title2.getBytes(),"UTF-8");
		level = level2;
		user = user2;
		uid = uid2;
		startTime = startTime2;
		escapeTime = escapeTime2;
		status = status2;
		mid = mid2;
	}
	
	public String toString(){
		if (hp==null)
			return "\n"+title+"\nLv."+level+"  "+user+"\n20"+startTime+"\n"+status+"\n";
		else
			return "\n"+title+"\nLv."+level+"  "+user+"\nHP:"+this.hp[0]+" - "+this.hp[1]+"%\n20"+startTime+"\n"+escapeTime+"\n"+status+"\n";
	}

	public int compareTo(Mission o) {
		return this.mid.compareTo(o.mid);
	}

}
