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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import model.App;
import model.CoreData;
import model.CoreThreadInterface;
import model.Mission;
import model.User;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import control.EGMessenger;

public class Core extends CoreData implements CoreThreadInterface {
	
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

	public boolean loadPage(String addr,String filename){
		if (referrer.equals("http://"+this.host+addr))
			referrer = referrer2;
		//重复刷新防止referrer改变
		
		HttpHost target = new HttpHost(host, 80, "http");
		HttpGet req = new HttpGet(addr);
		setHeaders(req);
		carrier.println("Now loading:"+addr);
		
		HttpEntity entity = null;
		try{
			HttpResponse rsp = hc.execute(target, req);
			
			entity = rsp.getEntity();
			if (entity != null) {
				Header[] headers = rsp.getHeaders("Content-Encoding");
				InputStream is = null;
				if (headers.length>0)
					is = new GZIPInputStream(entity.getContent());
				else
					is = entity.getContent();
				int b;
				FileOutputStream fos = new FileOutputStream(new File(filename));
				while((b = is.read())!=-1)
					fos.write(b);
				fos.close();
			}
		} catch(Exception e){
			carrier.showError(e);
			return false;
		} finally{
			try {
				if (entity!=null)
					EntityUtils.consume(entity);
			} catch (IOException e) {
			}
		}
		
		referrer2 = referrer;
		referrer = "http://"+this.host+addr;
		//页面前进刷新referrer
		return true;
	}
	
	public boolean postPage(String addr, String filename, HttpEntity en){
		if (referrer.equals("http://"+host+addr))
			referrer = referrer2;
		
		HttpHost target = new HttpHost(host, 80, "http");
		HttpPost req = new HttpPost(addr);
		setHeaders2(req);
		req.setEntity(en);
		carrier.println("Now loading:"+addr);

		HttpEntity entity = null;
		try{
			HttpResponse rsp = hc.execute(target, req);
			
			entity = rsp.getEntity();
			if (entity != null) {
				Header[] headers = rsp.getHeaders("Content-Encoding");
				InputStream is = null;
				if (headers.length>0)
					is = new GZIPInputStream(entity.getContent());
				else
					is = entity.getContent();
				int b;
				FileOutputStream fos = new FileOutputStream(new File(filename));
				while((b = is.read())!=-1)
					fos.write(b);
				fos.close();
			}
		} catch(Exception e){
			carrier.showError(e);
			return false;
		} finally{
			try {
				if (entity!=null)
					EntityUtils.consume(entity);
			} catch (IOException e) {
			}
		}

		referrer2 = referrer;
		referrer = "http://"+host+addr;
		return true;
	}

	private boolean getBonus(String s){
		carrier.println("有未领取奖励，自动领取。。。");
		String url = StringScanner.sortString(s, this.host, '\"');
		if (this.loadPage(url, this.fileBonus))
			return false;
		url = StringScanner.findString("ボーナスを受取る", this.fileBonus);
		url = StringScanner.sortString(url, host, '\"');
		if (this.loadPage(url, this.fileBonusResult))
			return false;
		return true;
	}
	
	private boolean attendMission(String mid,String resultFile, String hint){
		carrier.println(new Date() + ": "+ hint);
		if (bp==0){
			carrier.println("BP为零，无法继续作战");
			return true;
		}
		this.loadPage(carrier.pages.MISSION+"/show/"+mid, resultFile);
		if (StringScanner.findString("カード枚数", resultFile)!=null){
			carrier.println("卡片数达到上限，无法继续作战，请先到新闻部整理卡牌。");
			return true;
		}
		String s;
		s = StringScanner.findString("id=\"executeBtn\"",resultFile);
		s = StringScanner.sortString(s, host, '\"');
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();   
		formParams.add(new BasicNameValuePair("healItem_2", "0"));   
		formParams.add(new BasicNameValuePair("healItem_4", "0"));        
		formParams.add(new BasicNameValuePair("bp", "1"));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
			postPage(s, this.fileMissionResult, entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean playMission(String missionUrl){
		if (missionUrl==null){
			carrier.println("PVE活动暂时不开放，已关闭自动参加PVE活动功能。");
			return false;
		}
		
		loadPage(missionUrl,filemissionSet);
		String s = StringScanner.findString("こちら", this.filemissionSet);
		if (s!=null)
			if (getBonus(s))
				carrier.println("领取成功！");
			else
				carrier.println("领取失败！");
		if (StringScanner.findString("ミッションはありません", this.filemissionSet)!=null){
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
			if (m.getUid().equals(UID)&&(this.fightMode==1||this.fightMode==0)){
				str = "自己 - ";
				if (m.getTitle().contains("緊急")){
					if (bp>=PVEU)
						this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 紧急，自动战斗。");
				} else if (m.getTitle().contains("特大")){
					if (bp>=PVEL)
						this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 特大，自动战斗。");
				} else if (bp>=PVEN)
					this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 普通，自动战斗。");
			} else if (m.getStatus().contains("未参加")&&(this.fightMode==2||this.fightMode==0)){
				if (m.getHpNow()>=this.hpmin){
					if (m.getTitle().contains("緊急")){
						if (bp>=PVEU)
							this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 紧急，自动战斗。");
					} else if (m.getTitle().contains("特大")){
						if (bp>=PVEL)
							this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 特大，自动战斗。");
					} else if (bp>=PVEN)
						this.attendMission(m.getMid(), fileMissionPage, "发现"+str+m.getUser()+" 普通，自动战斗。");
				} else {
					carrier.println("好友"+m.getUser()+"的任务剩余血量"+m.getHpNow()+"低于"+this.hpmin+"，不参战。");
				}
			} else if (bp>=4){
				this.attendMission(m.getMid(), fileMissionPage, "BP快满，自动战斗。");
			}
		}		
		return true;
	}
	
	private boolean attendQuest(String key,String fileName,String hint) throws IOException{
		carrier.println(new Date() + ": " + hint);
		String url,authenticity_token,app_token;
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		while ((s = br.readLine())!=null){
			if (s.contains(key))
				break;
		}
		url = StringScanner.sortString(s,host,'"');
		authenticity_token = StringScanner.sortString(s,"value=\"",'"');
		s = br.readLine();
		app_token = StringScanner.sortString(s,"value=\"",'"');
		br.close();
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();   
		formParams.add(new BasicNameValuePair("authenticity_token", authenticity_token));   
		formParams.add(new BasicNameValuePair("app_token", app_token));  
		HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");   
		postPage(url,this.fileQuestResult,entity);
		return true;
	}
	
	private boolean playQuest(String questUrl){
		analyzeStatus(true,questUrl);
		int t = (max_st-(st_up+st_down))/5;
		try {
			if (st>(max_st-bp*t-st_up)){
				this.attendQuest("quest_execute_form", fileQuestPage, "ST过多，继续探索。");
			} else if (this.isUpgrade) {
				this.attendQuest("quest_execute_form", fileQuestPage, "即将升级，继续探索。");
			}
		} catch (IOException e) {
			carrier.showError(e);
			return false;
		}
		return true;
	}
	
	private boolean analyzeBattleResult(String fileName){
		String s = StringScanner.findString("\\u52dd\\u5229", fileName);//勝利
		return (s!=null);
	}
	
	private Boolean attendBattle(String battleUrl,String key){
		this.loadPage(battleUrl, fileBattlePage);
		String url;
		if (StringScanner.findString("カード枚数", fileBattlePage)!=null){
			carrier.println("卡片数达到上限，无法继续作战，请先到新闻部整理卡牌。");
			return null;
		}
		String s = StringScanner.findString(key, fileBattlePage);
		url = StringScanner.sortString(s,host,'"');
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();   
		formParams.add(new BasicNameValuePair("healItem_2", "0"));   
		formParams.add(new BasicNameValuePair("healItem_4", "0"));        
		formParams.add(new BasicNameValuePair("bp", "1"));     
		try {
			HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
			postPage(url, this.fileBattleResult, entity);
			return this.analyzeBattleResult(this.fileBattleResult);
		} catch (UnsupportedEncodingException e) {
			carrier.showError(e);
		}
		return null;
	}
	
	public boolean isCanDoBattle(){
		String s = StringScanner.findString("title=\"Battle\">", fileBattleList);
		if (s.contains("校内定期戦"))
			return useBPMode;
		else
			return (bp>0);
	}

	private boolean playBattle(String battleUrl, String attendUrl){
		this.loadPage(battleUrl, fileBattleList);
		if (StringScanner.findString("ご利用いただけません", fileBattleList)!=null){
			carrier.println("PVP校园战暂时不开放，稍后重试。");
			freebp = 0;
			return false;
		} else {
			String s = StringScanner.findString("活動力の消費なしで", fileBattleList);
			if (s==null){
				freebp = 0;
				if (!this.isCanDoBattle()){
					carrier.println("当前免费BP为0，且不使用BP参加校园战，不自动进行PVP校园战。");
					return false;
				}
			} else {
				s = StringScanner.sortString(s, "\">", '<');
				freebp = Integer.parseInt(s);
			}
		}
		if (freebp>0)
			carrier.println("免费BP剩余，继续战斗。");
		else
			if (bp>0)
				carrier.println("BP剩余，继续战斗");
			else
				return true;
		BattleAnalyzer battleAn = new BattleAnalyzer(fileBattleList);
		users = battleAn.analyze();
		
		int no = 0;
		User now;
		if (StringScanner.findString("人気投票", fileBattleList)!=null&&(pvpNoFresh<=20)){
			for (int i=1;i<=users.size();i++){
				now = users.get(i);
				this.loadPage(carrier.pages.USER_DETAIL+now.getUid(), fileUserPage);
				now.getUserInfo(fileUserPage);
				if (now.isSpecial()){
					if (now.getDefense()<=this.defenseMax)
						no = i;
				}				
			}
			this.pvpNoFresh++;
		} else {
			if (this.pvpNoFresh>10)
				carrier.println("PVP对战列表活动解析模式超过20次未成功，自动改为普通解析模式。");
			int ptmin = pt_min;
			for (int i=1;i<=users.size();i++){
				now = users.get(i);
				this.loadPage(carrier.pages.USER_DETAIL+now.getUid(), fileUserPage);
				now.getUserInfo(fileUserPage);
				if (now.getPoint()<ptmin){
					no = i;
					ptmin = now.getPoint();
				}
			}
		}
		carrier.showUserList();
		if (no!=0){
			now = users.get(no);
			carrier.println("找到");
			if (now.getWinPercent().length()>=1)
				carrier.print("防御点数为"+now.getDefense()+"，");
			carrier.println("对战点数为:"+now.getPoint()+"的对手，进入战斗！");
			
			try {
				Boolean result = attendBattle(attendUrl+now.getUid(),"executeBtn");
				
				if (result==null)
					return false;
				
				this.pvpNoFresh = 0;
				if (result){
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
			} catch (IOException e) {
				carrier.showError(e);
			}
		}
		return true;		
	}

	private boolean analyzeStatus(boolean reload,String url){
		if (reload)
			if (!loadPage(url,fileQuestPage))
				return false;
		String s;
		s = StringScanner.findString("<span id=\"st\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"st\">", "");
		st = Integer.parseInt(s);
		s = StringScanner.findString("<span id=\"max_st\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"max_st\">", "");
		max_st = Integer.parseInt(s);
		s = StringScanner.findString("<span id=\"bp\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"bp\">", "");
		bp = Integer.parseInt(s);
		s = StringScanner.findString("<span id=\"max_bp\">",fileQuestPage);
		s = s.replace("</span>", "");
		s = s.replace("<span id=\"max_bp\">", "");
		max_bp = Integer.parseInt(s);
		return true;
	}

	private boolean analyzeTime(String url1,String url2,String UID){
		loadPage(url1,fileMainPage);
		if (!loadPage(url2+UID,fileStatusPage)){
			carrier.println("UID设置错误，无法获取个人页面，请查看。");
			return false;
		}
		
		try {
			File f = new File(fileStatusPage);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			
			while ((s = br.readLine())!=null){
				if (s.contains("経験値:"))
					break;
			}
			s = StringScanner.sortString(s,":</span> ",'<');
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
			t1 = StringScanner.sortString(s,"全回復まで",'<');
			
			while ((s = br.readLine())!=null){
				if (s.contains("全回復まで"))
					break;
			}
			if (s==null){
				t2="--";
				return true;
			}
			t2 = StringScanner.sortString(s,"全回復まで",'<');
			br.close();
		} catch (NumberFormatException e) {
			carrier.showError(e);
			return false;
		} catch (FileNotFoundException e) {
			carrier.showError(e);
			return false;
		} catch (IOException e) {
			carrier.showError(e);
			return false;
		}
		return true;
	}
	
	private boolean analyzeLinks(String fileMainPage){
		String s = StringScanner.findString(">quest<", fileMainPage);
		s = StringScanner.sortString(s, this.host, '\"');
		if (s==null)
			return false;
		carrier.pages.QUEST = s;
		
		s = StringScanner.findString(">ミッション<", fileMainPage);
		s = StringScanner.sortString(s, this.host, '\"');
		if (s==null)
			return false;
		carrier.pages.MISSION = s;
		
		s = StringScanner.findString(">校内定期戦<", fileMainPage);
		s = StringScanner.sortString(s, this.host, '\"');
		if (s==null)
			return false;
		carrier.pages.BATTLE = s;
		return true;
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
	private boolean checkActivity(String fileName){
		return (StringScanner.findString("利用いただけません", fileName)==null);
	}

	public void clearFiles(String dirPath) {
		File f = new File(dirPath);
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
		if (!this.analyzeLinks(this.fileMainPage)){
			carrier.showError("解析失败");
			return;
		}
		carrier.println("解析完毕");
		carrier.println("Start: "+new Date());
					
		while(exitflag){
			if (!analyzeStatus(true,carrier.pages.QUEST))
				continue;
			
			if (isBattleActivity){
				loadPage(carrier.pages.EVENT_BATTLE,this.fileBattleList);
				isBattleActivity = this.checkActivity(this.fileBattleList);
				if (isBattleActivity){
					String s;
					s = StringScanner.findString("3倍", this.fileBattleList);
					if (s!=null)
						carrier.println("踩中三倍！剩余："+StringScanner.sortString(s, "\">", '<')+"秒。");
					s = StringScanner.findString("バトルガチャチケット", this.fileBattleList);
					if (s!=null){
						carrier.println("连胜中，剩余："+StringScanner.sortString(s, "class=\"colorR\">", '<'));
						comboFightMode = true;
					}
					if (comboFightMode)
						playBattle(carrier.pages.EVENT_BATTLE,carrier.pages.EVENT_BATTLE+carrier.pages.SHOW_USER);
					isMissionActivity = false;
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
			
			if (!analyzeStatus(false,carrier.pages.QUEST))
				continue;
			
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
			
			if (isBattleActivity){
				if (bp>=this.bp_combo)
					comboFightMode = true;
				if (bp==0)
					comboFightMode = false;
			}
			
			{
				long t = getRandomTime(this.randomOther, this.waitTime);
				carrier.println("等待: "+t+"s");
				this.clearFiles(".");
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
	}
}
