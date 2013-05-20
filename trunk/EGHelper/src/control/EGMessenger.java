package control;

import java.util.Date;
import java.util.TreeMap;

import model.Page;

import dao.Core;



import view.InfoFrame;

public class EGMessenger {
	public String version = "3.0";
	
	public TreeMap<String, String> infoMap = null;
	
	private boolean useProxy = false;
	public boolean isUseProxy() {
		return useProxy;
	}
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	private boolean debugMode = true;
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
	
	private Thread gameThread;

	public Page pages = null;
	
	public void setGameThread(Thread gameThread) {
		this.gameThread = gameThread;
	}

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
	
	public void showError(Exception e){
		if (this.isDebugMode()){
			println(new Date()+"");
			e.printStackTrace();
		}
		println("错误: "+e.toString()+". 请联系作者。");
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
}
