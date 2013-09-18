package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.TreeMap;
import model.Page;

import dao.Core;

import view.InfoFrame;
import view.MyChartFrame;

public class EGMessenger {
	public static String title = "EG助手";
	public static String version = "5.0";
	public static int versionNumber = 14;
	
	private boolean betaMode = false;
	private boolean debugMode = true;
	private boolean rankOnlyMode = false;
	
	public TreeMap<String, String> infoMap = null;
	
	public boolean checkMulti(){
		if (this.isDebugMode())
			return false;
		this.println("初始化中。。。");
		try {
			Socket socket;
			socket = new Socket("127.0.0.1",9126);
			socket.close();
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		this.println("为了您的账号安全，请使用默认的启动器打开程序。");
		return true;
	}
	
	public void listenPort(){
		if (this.debugMode)
			return;
		try {
			ServerSocket serversocket=new ServerSocket(9126);
			serversocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean useProxy = false;
	public boolean isUseProxy() {
		return useProxy;
	}
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public boolean isDebugMode() {
		return debugMode;
	}
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	private boolean quiteMode = false;
	public boolean isQuiteMode() {
		return quiteMode;
	}
	public void setQuiteMode(boolean quiteMode) {
		this.quiteMode = quiteMode;
	}

	private int countLines,maxLines;
	public void setMaxLines(int maxLines) {
		this.maxLines = maxLines;
	}

	private InfoFrame inst;
	public void setInst(InfoFrame inst) {
		this.inst = inst;
	}
	
	private Core game;
	public void setGame(Core game) {
		this.game = game;
	}
	
	public Page pages = null;
	
	private Thread gameThread;
	public void setGameThread(Thread gameThread) {
		this.gameThread = gameThread;
	}
	
	private Thread monitor;
	private MyChartFrame cf;

	public EGMessenger(int i,int mode) {
		countLines = 0;
		maxLines = i;//行数超出后自动刷新文本框防卡
		if (i==0)
			inst = null;//mode 0-控制台界面  1-图形化界面
	}
	
	public boolean isModeGUI(){
		return (inst!=null);
	}

	public void print(String str){
		if (!this.isQuiteMode())
			if (!isModeGUI()){
				System.out.print(str);
			} else {
				inst.getTextArea().append(str);
				countLines++;
				if(countLines>=maxLines){
					countLines = 0;
					inst.getTextArea().setText(null);
				}
			}
	}
	
	public void println(String str){
		this.print(str+"\n");
	}
	
	public void setFight(boolean status){
		if (this.isModeGUI())
			inst.getjButtonManual().setEnabled(status);
	}
	
	public void fight() {
		gameThread.interrupt();
	}

	public int[] getNums(){
		int[] array = {
				game.st,
				game.bp,
				game.exp,
				game.max_st,
				game.max_bp,
				game.max_exp
		};
		return array;
	}
	
	public String[] getTimes(){
		String[] array = {
				game.t1,
				game.t2
		};
		return array;
	}
	
	public void showRankInfo(TreeMap<String, String> tempMap) {
		if (this.isQuiteMode())
			return;
		if (isModeGUI()){
			inst.getjTextAreaRankInfo().setText(tempMap.toString());
		}
	}
	
	public Integer getRankInfo(int i) {
		Integer rank = this.game.ranks.get(i);
		if (rank == null)
			return 0;
		return rank;
	}

	public void showUserList() {
		if (this.isQuiteMode())
			return;
		if (isModeGUI()){
			inst.getTextAreaList().setText(game.users.toString());
		}
	}

	public void showMissionList() {
		if (this.isQuiteMode())
			return;
		if (isModeGUI()){
			inst.getTextAreaList().setText(game.missions.toString());
		}
	}
	
	public void showError(String errorCause){
		println("错误: "+errorCause+"，请稍后重试或联系原作者。");
	}
	
	public void showError(Exception e){
		if (this.isDebugMode()){
			println(new Date()+"");
			e.printStackTrace();
		}
		this.showError(e.toString());
	}

	public void refreshBar() {
		if (this.isQuiteMode())
			return;
		inst.getJProgressBarST().setMaximum(game.max_st);
		inst.getJProgressBarBP().setMaximum(game.max_bp);
		inst.getJProgressBarEXP().setMaximum(game.max_exp);
		
		inst.getJProgressBarST().setValue(game.st);
		inst.getJProgressBarBP().setValue(game.bp);
		inst.getJProgressBarEXP().setValue(game.exp);	
		
		inst.getJProgressBarST().setString(game.st+"/"+game.max_st);
		inst.getJProgressBarBP().setString(game.bp+"/"+game.max_bp);
		inst.getJProgressBarEXP().setString(game.exp+"/"+game.max_exp);
	}

	public void enableBars() {
		if (!isModeGUI())
			return;
		if (this.isQuiteMode())
			return;
		inst.getJProgressBarST().setIndeterminate(false);
		inst.getJProgressBarBP().setIndeterminate(false);
		inst.getJProgressBarEXP().setIndeterminate(false);
		
		inst.getJProgressBarST().setStringPainted(true);
		inst.getJProgressBarBP().setStringPainted(true);
		inst.getJProgressBarEXP().setStringPainted(true);
	}

	public boolean isRankOnlyMode() {
		return rankOnlyMode;
	}

	public boolean isBetaMode() {
		return betaMode;
	}

	public void setRankOnlyMode(boolean rankOnlyMode) {
		this.rankOnlyMode = rankOnlyMode;
	}

	public void newChartFrame(int[] levels) {
		cf = new MyChartFrame(this,levels);
	}
	
	public void setMonitor(Thread monitor) {
		this.monitor = monitor;
	}

	public void disposeMonitor() {
		cf.dispose();
	}
	
	public void interruptMonitor() {
		this.monitor.interrupt();
	}
}
