package model;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;

/**
 * @author LongFangzhou
 * @version 1.0
 * 核心类应该是一个进程以提高稳定性与健壮性。
 */	

public interface CoreThreadInterface extends Runnable {
	
	/**
	 * 使用Get方法获取网页内容
	 * @param addr 目标网页地址
	 * @param fileName 获取后的网页存储位置
	 * @return 如果页面载入成功，返回true，否则返回false
	 */	
	abstract boolean loadPage(String addr,String fileName);
	
	/**
	 * 使用Post方法获取网页内容
	 * @param addr 目标网页地址
	 * @param fileName 获取后的网页存储位置
	 * @param httpEntity 用来存放请求参数内容
	 * @return 如果页面载入成功，返回true，否则返回false
	 */	
	abstract boolean postPage(String addr, String fileName, HttpEntity httpEntity) throws ClientProtocolException, IOException;
	
	/**
	 * 清理缓存文件
	 * @param dirPath 工作目录路径
	 * @return 无
	 * @throws 无
	 */	
	abstract void clearFiles(String dirPath);
	
	/**
	 * 核心类主方法体
	 * @param 空
	 * @return 无
	 * @throws 无
	 */	
	abstract void run();
}
