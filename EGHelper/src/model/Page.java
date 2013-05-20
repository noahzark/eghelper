package model;

import java.util.TreeMap;

public class Page {
	public String DRIVER = null;
	public String HOST = null;
	
	public String MYPAGE = null;
	public String QUEST = null;
	public String BATTLE = null;
	
	public String MISSION = null;
	public String EVENT_QUEST = null;
	public String EVENT_BATTLE = null;
	
	public String SHOW_USER = null;
	public String USER_DETAIL = null;

	public Page(TreeMap<String ,String> pageConfig) {
		this.DRIVER = pageConfig.get("DRIVER");
		this.HOST = pageConfig.get("HOST");
		this.MYPAGE = pageConfig.get("MYPAGE");
		this.QUEST = pageConfig.get("QUEST");
		this.BATTLE = pageConfig.get("BATTLE");
		this.MISSION = pageConfig.get("MISSION");
		this.EVENT_QUEST = pageConfig.get("EVENT_QUEST");
		this.EVENT_BATTLE = pageConfig.get("EVENT_BATTLE");
		this.SHOW_USER = pageConfig.get("SHOW_USER");
		this.USER_DETAIL = pageConfig.get("USER_DETAIL");
	}

}
