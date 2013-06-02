package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import model.App;
import model.Mission;
import model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import control.EGMessenger;

public class Core implements Runnable {
	private final int randomLeast = 10;
	private final int randomOther = 10;
	
	private DefaultHttpClient hc;
	private App app;
	private Random radomer;
	private String UID;
	private EGMessenger carrier = null;

	private String host;
	private String referrer = "http://"+host+"/";
	private String referrer2;
	
	private String fileMainPage = "nowMain.html";
	private String fileStatusPage = "nowStatus.html";
	private String filemissionSet = "nowMissions.html";
	private String fileMissionPage = "MissionNow.html";
	private String fileQuestPage = "nowQuest.html";
	private String fileMissionResult = "MissionResult.json";
	private String fileQuestResult = "QuestResult.json";
	private String fileBattleList = "nowBattles.html";
	private String fileBattlePage = "BattleNow.html";
	private String fileBattleResult = "BattleResult.json";
	private String fileUserPage = "nowUser.html";
	private String fileWinList = "WinList.inf";
	private String fileLostList = "LostList.inf";
	private String fileBonus = "Bonus.html";
	private String fileBonusResult = "BonusResult.json";
	
	private boolean showAnalyze = true;
	private boolean isUpgrade = false;
	
	private boolean useBPMode = false;
	public boolean isCanDoBattle() throws IOException {
		String s = Core.findString("title=\"Battle\">", fileBattleList);
		if (s.contains("校内定期戦"))
			return useBPMode;
		else
			return (bp>0);
	}
	public void setUseBPMode(boolean useBPMode) {
		this.useBPMode = useBPMode;
	}

	public int st,max_st,bp,max_bp,exp=0,max_exp=9999;
	private int freebp = 5;
	public String t1,t2;	
	private int st_up=20,st_down=20,pt_min=100,bp_combo=5;
	
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
	
	private int waitTime = 5;
	public void setWaitTime(int wait){
		this.waitTime = wait;
	}
	private int PVEN = 0, PVEL = 0, PVEU = 0;
	public void setPVEN(int pVE) {
		PVEN = pVE;
	}
	public void setPVEL(int pVE) {
		PVEL = pVE;
	}
	public void setPVEU(int pVE) {
		PVEU = pVE;
	}
	
	public Core(DefaultHttpClient dhc,App app,String uID, EGMessenger carrier) {
		this.host = carrier.pages.HOST;
		hc = dhc;
		radomer = new Random();
		this.app = app;
		UID = uID;
		this.carrier = carrier;
		carrier.setGame(this);
	}
	
	private void setHeaders(HttpGet req) {
		req.setHeader("Host",this.host);
		req.setHeader("Accept-Language", "zh-cn");
		req.setHeader("Accept-Encoding", "gzip, deflate");
		req.setHeader("App-Id-2", this.app.id2);
		req.setHeader("App-Version", this.app.version);
		req.setHeader("App-Id-3", this.app.id3);
		req.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		req.setHeader("referrer", referrer);
		req.setHeader("Content-Type", "application/x-www-form-urlencoded");
		req.setHeader("App-Id-1", this.app.id1);
	}
	
	private void setHeaders2(HttpPost req) {
		req.setHeader("Host",host);
		req.setHeader("Accept-Language", "zh-cn");
		req.setHeader("X-Requested-With", "XMLHttpRequest");
		req.setHeader("Accept", "application/json");
		req.setHeader("referrer", referrer);
		req.setHeader("Origin", "http://"+host);
	}

	private void loadPage(String addr,String filename) throws ClientProtocolException, IOException {
		if (referrer.equals("http://"+this.host+addr))
			referrer = referrer2;
		//重复刷新防止referrer改变
		
		HttpHost target = new HttpHost(host, 80, "http");
		HttpGet req = new HttpGet(addr);
		setHeaders(req);
		carrier.println("Now loading:"+addr);
		HttpResponse rsp = hc.execute(target, req);
		HttpEntity entity = rsp.getEntity();

		if (entity != null) {
			GZIPInputStream gis = new GZIPInputStream(entity.getContent());
			int b;
			FileOutputStream fos = new FileOutputStream(new File(filename));
			while((b = gis.read())!=-1)
				fos.write(b);
			fos.close();
		}
		
		EntityUtils.consume(entity);

		referrer2 = referrer;
		referrer = "http://"+this.host+addr;
		//页面前进刷新referrer
	}
	
	private void postPage(String addr, String filename, HttpEntity en) throws ClientProtocolException, IOException {
		if (referrer.equals("http://"+host+addr))
			referrer = referrer2;
		
		HttpHost target = new HttpHost(host, 80, "http");
		HttpPost req = new HttpPost(addr);
		setHeaders2(req);
		req.setEntity(en);
		carrier.println("Now loading:"+addr);
		HttpResponse rsp = hc.execute(target, req);
		HttpEntity entity = rsp.getEntity();

		if (entity != null) {
			InputStream gis = entity.getContent();
			int b;
			FileOutputStream fos = new FileOutputStream(new File(filename));
			while((b = gis.read())!=-1)
				fos.write(b);
			fos.close();
		}
		
		EntityUtils.consume(entity);

		referrer2 = referrer;
		referrer = "http://"+host+addr;
	}

	public static String findString(String key,String fileName) throws IOException {
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		while ((s = br.readLine())!=null){
			if (s.contains(key)){
				br.close();
				return s;
			}
		}
		br.close();
		return null;
	}
	
	public static String sortString(String s,String front,char back){
		StringBuffer sb = new StringBuffer();
		int i;
		if (s == null)
			return null;
		for (i=0;i<s.length();i++){
			sb.append(s.charAt(i));
			if (sb.toString().contains(front))
				break;
		}
		sb = new StringBuffer();
		for (i++;i<s.length();i++){
			if (s.charAt(i)==back)
				break;
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}

	private void getBonus(String s) throws ClientProtocolException, IOException {
		carrier.println("有未领取奖励，自动领取。。。");
		String url = Core.sortString(s, this.host, '\"');
		this.loadPage(url, this.fileBonus);
		url = Core.findString("ボーナスを受取る", this.fileBonus);
		url = Core.sortString(url, host, '\"');
		this.loadPage(url, this.fileBonusResult);
	}
	private boolean attendMission(String mid,String resultFile, String hint) throws FileNotFoundException, ClientProtocolException, IOException{
		carrier.println(new Date() + ": "+ hint);
		if (bp==0){
			carrier.println("BP为零，无法继续作战");
			return true;
		}
		this.loadPage(carrier.pages.MISSION+"/show/"+mid, resultFile);
		if (Core.findString("カード枚数", resultFile)!=null){
			carrier.println("卡片数达到上限，无法继续作战，请先到新闻部整理卡牌。");
			return true;
		}
		String s;
		s = findString("id=\"executeBtn\"",resultFile);
		s = sortString(s, host, '\"');
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();   
		formParams.add(new BasicNameValuePair("healItem_2", "0"));   
		formParams.add(new BasicNameValuePair("healItem_4", "0"));        
		formParams.add(new BasicNameValuePair("bp", "1"));     
		HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");   
		postPage(s, this.fileMissionResult, entity);
		return true;
	}

	private boolean playMission(String missionUrl) throws ClientProtocolException, IOException {
		if (missionUrl==null){
			carrier.println("PVE活动暂时不开放，已关闭自动参加PVE活动功能。");
			return false;
		}
		
		loadPage(missionUrl,filemissionSet);
		String s = Core.findString("こちら", this.filemissionSet);
		if (s!=null)
			getBonus(s);
		if (Core.findString("ミッションはありません", this.filemissionSet)!=null){
			carrier.println("PVE活动暂时不开放，已关闭自动参加PVE活动功能。");
			return false;
		}
		
		MissionAnalyzer missionAn = new MissionAnalyzer(host,filemissionSet);
		missions = missionAn.analyze();
		if (missions==null){
			carrier.println("PVE任务解析失败，退出。");
			return false;
		}
		
		if (missionAn.isResult){
			carrier.println("当前无PVE任务，继续探索。");
			carrier.showMissionList();
			return true;
		}
		
		for (int i=1;i<=missions.size();i++){
			Mission m = missions.get(i);
			this.loadPage(carrier.pages.MISSION+"/show/"+m.getMid(), fileMissionPage);
			m.setHp(missionAn.analyzeDetail(fileMissionPage));
		}
		
		carrier.showMissionList();
		
		if (this.isUpgrade&&this.bp>0){
			carrier.println("即将升级，自动攻击。");
			bp = 5;
		}
		
		for (int i=1;i<=missions.size();i++){
			Mission m = missions.get(i);
			String str = "好友 - ";
			if (m.getStatus().contains("未参加")||m.getHp()[1].equals("100")){
				if (!m.getStatus().contains("未参加"))
					str = "自己 - ";
				if (m.getTitle().contains("緊急")){
					if (bp>=PVEU)
						this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 紧急，自动战斗。");
				} else if (m.getTitle().contains("特大")){
					if (bp>=PVEL)
						this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 特大，自动战斗。");
				} else if (bp>=PVEN)
					this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 普通，自动战斗。");
			} else if (bp>=4){
				this.attendMission(m.getMid(), fileMissionPage, "BP快满，自动战斗。");
			}
		}		
		return true;
	}
	
	private boolean attendQuest(String key,String fileName,String hint) throws FileNotFoundException, ClientProtocolException, IOException{
		carrier.println(new Date() + ": " + hint);
		String url,authenticity_token,app_token;
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		while ((s = br.readLine())!=null){
			if (s.contains(key))
				break;
		}
		url = sortString(s,host,'"');
		authenticity_token = sortString(s,"value=\"",'"');
		s = br.readLine();
		app_token = sortString(s,"value=\"",'"');
		br.close();
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();   
		formParams.add(new BasicNameValuePair("authenticity_token", authenticity_token));   
		formParams.add(new BasicNameValuePair("app_token", app_token));  
		HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");   
		postPage(url,this.fileQuestResult,entity);
		return true;
	}
	
	private boolean playQuest(String questUrl) throws ClientProtocolException, IOException {
		analyzeStatus(true,questUrl);
		int t = (max_st-(st_up+st_down))/5;
		if (st>(max_st-bp*t-st_up)){
			this.attendQuest("quest_execute_form", fileQuestPage, "ST过多，继续探索。");
		} else if (this.isUpgrade) {
			this.attendQuest("quest_execute_form", fileQuestPage, "即将升级，继续探索。");
		}
		return true;
	}
	private boolean analyzeBattleResult(String fileName) throws IOException{
		String s = Core.findString("\\u52dd\\u5229", fileName);//勝利
		return (s!=null);
	}
	
	private boolean attendBattle(String battleUrl,String key) throws ClientProtocolException, IOException {
		this.loadPage(battleUrl, fileBattlePage);
		String url;
		String s = Core.findString(key, fileBattlePage);
		url = sortString(s,host,'"');
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();   
		formParams.add(new BasicNameValuePair("healItem_2", "0"));   
		formParams.add(new BasicNameValuePair("healItem_4", "0"));        
		formParams.add(new BasicNameValuePair("bp", "1"));     
		HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");   
		postPage(url, this.fileBattleResult, entity);
		return this.analyzeBattleResult(this.fileBattleResult);
	}

	private boolean playBattle(String battleUrl, String attendUrl) throws ClientProtocolException, IOException {
		this.loadPage(battleUrl, fileBattleList);
		if (Core.findString("ご利用いただけません", fileBattleList)!=null){
			carrier.println("PVP校园战暂时不开放，稍后重试。");
			freebp = 0;
			return false;
		} else {
			String s = findString("活動力の消費なしで", fileBattleList);
			if (s==null){
				freebp = 0;
				if (!this.isCanDoBattle()){
					carrier.println("当前免费BP为0，且不使用BP参加校园战，不自动进行PVP校园战。");
					return false;
				}
			} else {
				s = sortString(s, "\">", '<');
				freebp = Integer.parseInt(s);
			}
		}
		if (freebp>0)
			carrier.println("免费BP剩余，继续战斗。");
		else
			carrier.println("BP剩余，继续战斗");
		BattleAnalyzer battleAn = new BattleAnalyzer(fileBattleList);
		users = battleAn.analyze();
		int no = 0;
		int ptmin = pt_min;
		User now;
		for (int i=1;i<=users.size();i++){
			now = users.get(i);
			this.loadPage(carrier.pages.USER_DETAIL+now.getUid(), fileUserPage);
			now.getUserInfo(fileUserPage);
			if (now.getPoint()<ptmin){
				no = i;
				ptmin = now.getPoint();
			}
		}
		carrier.showUserList();
		if (no!=0){
			now = users.get(no);
			carrier.println("找到对战点数为:"+now.getPoint()+"的对手,进入战斗！");
			if (attendBattle(attendUrl+now.getUid(),"executeBtn")){
				carrier.println("胜利！");
				File f = new File(this.fileWinList);
				FileWriter writer = new FileWriter(f, true); 
		        writer.write(now.getUid()+"\r\n");
		        writer.close();
			} else
				carrier.println("失败！");
				File f = new File(this.fileLostList);
				FileWriter writer = new FileWriter(f, true); 
				writer.write(now.getUid()+"\r\n");
				writer.close();
		}
		return true;		
	}

	private void analyzeStatus(boolean reload,String url) throws IOException{
		if (reload)
			loadPage(url,fileQuestPage);
		String s;
		s = findString("<span id=\"st\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"st\">", "");
		st = Integer.parseInt(s);
		s = findString("<span id=\"max_st\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"max_st\">", "");
		max_st = Integer.parseInt(s);
		s = findString("<span id=\"bp\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"bp\">", "");
		bp = Integer.parseInt(s);
		s = findString("<span id=\"max_bp\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"max_bp\">", "");
		max_bp = Integer.parseInt(s);
		
		if (st+exp>max_exp){
			this.attendQuest("quest_execute_form", fileQuestPage, "即将升级，继续探索。");
			this.isUpgrade = true;
		} else {
			this.isUpgrade = false;
		}
	}

	private boolean analyzeTime(String url1,String url2,String UID) throws ClientProtocolException, IOException {
		loadPage(url1,fileMainPage);
		try {
			loadPage(url2+UID,fileStatusPage);
		} catch (java.util.zip.ZipException e) {
			carrier.println("UID设置错误，无法获取个人页面，请查看。");
			return false;
		}
		
		File f = new File(fileStatusPage);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		
		while ((s = br.readLine())!=null){
			if (s.contains("経験値:"))
				break;
		}
		s = sortString(s,":</span> ",'<');
		exp = Integer.parseInt(s.substring(0, s.indexOf('/')));
		max_exp = Integer.parseInt(s.substring(s.indexOf('/')+1,s.length()));
		
		while ((s = br.readLine())!=null){
			if (s.contains("全回復まで"))
				break;
		}
		if (s==null){
			t1="--";
			t2="--";
			return true;
		}
		t1 = sortString(s,"全回復まで",'<');
		
		while ((s = br.readLine())!=null){
			if (s.contains("全回復まで"))
				break;
		}
		if (s==null){
			t2="--";
			return true;
		}
		t2 = sortString(s,"全回復まで",'<');
		br.close();
		return true;
	}
	
	private void analyzeLinks(String fileMainPage) throws IOException {
		String s = Core.findString(">quest<", fileMainPage);
		s = Core.sortString(s, this.host, '\"');
		carrier.pages.QUEST = s;
		s = Core.findString(">ミッション<", fileMainPage);
		s = Core.sortString(s, this.host, '\"');
		carrier.pages.MISSION = s;
		s = Core.findString(">校内定期戦<", fileMainPage);
		s = Core.sortString(s, this.host, '\"');
		carrier.pages.BATTLE = s;
	}
	
	private long getRandomTime(int i, int j){
		int time = this.randomLeast;
		time += radomer.nextInt(i)+radomer.nextInt(j);
		return time;
	}
	
	/**
	 * @author LongFangzhou
	 * @param fileName 加载后的页面文件名
	 * @return 如果当前页面可战斗，返回true，否则返回false
	 */	
	private boolean checkActivity(String fileName) throws IOException{
		return (Core.findString("利用いただけません", fileName)==null);
	}

	private void clearFiles() {
		File f = new File(".");
		FilenameFilter ff = new FilenameFilter(){
			public boolean accept(File arg0, String filename) {
				if (filename.toLowerCase().endsWith(".html"))
					return true;
				else
					return false;        
			}
		};
		File[] files = f.listFiles(ff);
		for (int i=0;i<files.length;i++)
			files[i].delete();
		ff = new FilenameFilter(){
			public boolean accept(File arg0, String filename) {
				if (filename.toLowerCase().endsWith(".json"))
					return true;
				else
					return false;        
			}
		};
		files = f.listFiles(ff);
		for (int i=0;i<files.length;i++)
			files[i].delete();
	}
	public void run() {
		try {
			boolean exitflag = true;
			boolean comboFightMode = false;
			boolean isBattleActivity = true;
			boolean isMissionActivity = true;
			
			carrier.println("开始解析链接。。。。。。");
			exitflag &= analyzeTime(
					carrier.pages.MYPAGE,
					carrier.pages.USER_DETAIL,
					this.UID
			);
			this.analyzeLinks(this.fileMainPage);
			carrier.println("解析完毕");
			carrier.println("Start: "+new Date());
						
			while(exitflag){
				try{
					analyzeStatus(true,carrier.pages.QUEST);
					
					if (isBattleActivity){
						loadPage(carrier.pages.EVENT_BATTLE,this.fileBattleList);
						isBattleActivity = this.checkActivity(this.fileBattleList);
						if (isBattleActivity){
							String s;
							s = Core.findString("3倍", this.fileBattleList);
							if (s!=null)
								carrier.println("踩中三倍！剩余："+Core.sortString(s, "\">", '<')+"秒。");
							s = Core.findString("バトルガチャチケット", this.fileBattleList);
							if (s!=null){
								carrier.println("连胜中，剩余："+Core.sortString(s, "class=\"colorR\">", '<'));
								comboFightMode = true;
							}
							if (comboFightMode)
								playBattle(carrier.pages.EVENT_BATTLE,carrier.pages.EVENT_BATTLE+carrier.pages.SHOW_USER);
						} else {
							carrier.println("PVP活动暂时不开放，已关闭自动参加PVP活动战斗功能。");
						}
					}
					
					if (isMissionActivity){
						isMissionActivity = this.playMission(carrier.pages.MISSION);
					}
					
					if (freebp>0||this.useBPMode)
						playBattle(carrier.pages.BATTLE, carrier.pages.BATTLE+carrier.pages.SHOW_USER);
					
					exitflag &= this.playQuest(carrier.pages.QUEST);
					
					analyzeStatus(false,carrier.pages.QUEST);
					
					exitflag &= analyzeTime(
							carrier.pages.MYPAGE,
							carrier.pages.USER_DETAIL,
							this.UID
					);
					
					if (carrier.isModeGUI())
						carrier.refreshBar();
					if (this.showAnalyze){
						if (!carrier.isModeGUI())
							carrier.println("ST: "+st+" / "+max_st+"\tBP: "+bp+" / "+max_bp+"\tEP: "+exp+" / "+max_exp);
						int i = max_exp-exp-st;
						if (i<0)
							i=0;
						String t3;
						t3 = (i/60<10)?"0":"";
						t3 = t3 + i/60 + ":";
						String t = (i%60<10)?"0":"";
						t3 = t3 + t + i%60;
						carrier.println("STTime:"+t1+"\tBPTime:"+t2+"\tUpgrade:"+t3);
					}
				} catch (Exception e){
					carrier.showError(e);
				}
				
				if (isBattleActivity){
					if (bp>=this.bp_combo)
						comboFightMode = true;
					if (bp==0)
						comboFightMode = false;
				}
				
				{
					long t = getRandomTime(this.randomOther, this.waitTime);
					carrier.println("等待: "+t+"s");
					this.clearFiles();
					carrier.setFight(true);
					try{
						Thread.sleep((long)t*1000L);
					} catch(InterruptedException e){
					} finally {
						carrier.println("\n已唤醒："+new Date());
					}
					carrier.setFight(false);
				}
			}
			carrier.println("Stop: "+new Date());
		} catch (IOException e) {
			carrier.showError(e);
		}
	}
}
