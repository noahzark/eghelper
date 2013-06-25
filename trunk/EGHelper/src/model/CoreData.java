package model;

import java.util.Random;
import java.util.TreeMap;

import org.apache.http.impl.client.DefaultHttpClient;

import control.EGMessenger;

public class CoreData {
	/**
	 * 登录后的连接客户端
	 */
	protected DefaultHttpClient hc;
	/**
	 * App基础信息的类
	 */
	protected App app;
	/**
	 * 随机数生成器
	 */
	protected Random radomer;
	/**
	 * 用户UID
	 */
	protected String UID;
	/**
	 * 信使类
	 */
	protected EGMessenger carrier = null;
	/**
	 * 服务器地址
	 */
	protected String host;
	/**
	 * 来源地址1
	 */
	protected String referrer = "http://"+host+"/";
	/**
	 * 来源地址2
	 */
	protected String referrer2;
	
	protected String fileMainPage = "nowMain.html";
	protected String fileStatusPage = "nowStatus.html";
	protected String filemissionSet = "nowMissions.html";
	protected String fileMissionPage = "MissionNow.html";
	protected String fileQuestPage = "nowQuest.html";
	protected String fileMissionResult = "MissionResult.json";
	protected String fileQuestResult = "QuestResult.json";
	protected String fileBattleList = "nowBattles.html";
	protected String fileBattlePage = "BattleNow.html";
	protected String fileBattleResult = "BattleResult.json";
	protected String fileUserPage = "nowUser.html";
	protected String fileWinList = "WinList.inf";
	protected String fileLostList = "LostList.inf";
	protected String fileBonus = "Bonus.html";
	protected String fileBonusResult = "BonusResult.json";
	/**
	 * 是否显示分析信息
	 */
	protected boolean showAnalyze = true;
	/**
	 * 用于标记体力已够升级所需经验
	 */
	protected boolean isUpgrade = false;
	
	protected boolean useBPMode = false;
	public void setUseBPMode(boolean useBPMode) {
		this.useBPMode = useBPMode;
	}
	
	protected int fightMode = 0; //0 - 全部 -1 - 自己优先  1 - 好友优先
	public void setFightMode(int fightMode) {
		if (fightMode<0)
			fightMode = -1;
		if (fightMode>0)
			fightMode = 1;
		this.fightMode = fightMode;
	}

	public int st,max_st,bp,max_bp,exp=0,max_exp=9999;
	protected int freebp = 5;
	public String t1,t2;	
	protected int st_up=20,st_down=20,pt_min=100,bp_combo=5;
	
	public TreeMap<Integer, Mission> missions = null;
	public TreeMap<Integer, User> users = null;
	public void setSt_up(int st_up) {
		this.st_up = st_up;
	}
	public void setSt_down(int st_down) {
		this.st_down = st_down;
	}
	public void setPt_min(int pt_min) {
		this.pt_min = pt_min;
	}
	public void setBp_combo(int bp_combo) {
		this.bp_combo = bp_combo;
	}
	
	protected int waitTime = 5;
	public void setWaitTime(int wait){
		this.waitTime = wait;
	}
	protected int PVEN = 0, PVEL = 0, PVEU = 0;
	public void setPVEN(int pVE) {
		PVEN = pVE;
	}
	public void setPVEL(int pVE) {
		PVEL = pVE;
	}
	public void setPVEU(int pVE) {
		PVEU = pVE;
	}
	
	protected long hpmin = 0;
	protected long defenseMax;	
	public void setDefenseMax(long defenseMax) {
		this.defenseMax = defenseMax;
	}
	
	protected int pvpNoFresh = 0;
	/**
	 * 两次刷新间隔的最短时长
	 */
	public int randomLeast = 60;
	/**
	 * 两次刷新间隔时长的随机参数1
	 */
	public int randomOther = 60;
}
