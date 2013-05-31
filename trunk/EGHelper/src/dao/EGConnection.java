package dao;

import java.io.IOException;


import model.App;
import model.AppInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.*;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import control.EGMessenger;

public class EGConnection {
	private DefaultHttpClient hc;
	public DefaultHttpClient getHc() {
		return hc;
	}
	
	private boolean useProxy = false;
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
	
	private String proxyUrl;
	private int proxyPort;
	public void setProxy(String url,int port){
		this.proxyUrl = url;
		this.proxyPort = port;
	}
	
	private AppInfo appinfo;
	private App app;
	private EGMessenger carrier = null;
	
	private String userAgent = "EnsembleGirls/1.3 CFNetwork/609.1.4 Darwin/13.0.0";
	private String userAgent2 =  "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Mobile/10B142 EnsembleGirls/1.5";

	private String server1;
	private String server2;

	public EGConnection(AppInfo appinfo, App app, EGMessenger carrier) {
		this.appinfo = appinfo;
		this.app = app;
		this.carrier = carrier;
		this.server1 = carrier.pages.DRIVER;
		this.server2 = carrier.pages.HOST;
	}

	private void FirstCheck(String addr) throws ParseException, IOException {
		hc.getParams().setParameter(CoreProtocolPNames.USER_AGENT, this.userAgent);
		HttpHost target = new HttpHost(this.server1, 80, "http");
		HttpGet req = new HttpGet(addr+"?app="+this.app.version+"&sdk="+this.appinfo.sdk+"&digest="+this.appinfo.digest);
		carrier.println("Executing request to " + target);
        HttpResponse rsp = hc.execute(target, req);
        HttpEntity entity = rsp.getEntity();

        if (entity != null) {
            EntityUtils.consume(entity);
        }
	}

	private boolean SecondCheck(String addr) throws ClientProtocolException, IOException {
		hc.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent2);
		HttpHost target = new HttpHost(this.server2, 80, "http");
		HttpPost req = new HttpPost(addr);
		
		req.setHeader("App-Id-2", this.app.id2);
		req.setHeader("Accept-Encoding", "gzip, deflate");
		req.setHeader("App-Id-3", this.app.id3);
		req.setHeader("Accept","*/*");
		req.setHeader("Content-Type", "application/x-www-form-urlencoded");
		req.setHeader("App-Id-1", this.app.id1);
		carrier.println("Executing request to " + target);
        HttpResponse rsp = hc.execute(target, req);
        HttpEntity entity = rsp.getEntity();

        if (entity != null) {
        	String result = EntityUtils.toString(entity);
            if (result.equals("OK")){
            	return true;
            }
        }
        return false;
	}

	public boolean connect() {
			//hc = new DefaultHttpClient(new PoolingClientConnectionManager());
			hc = new DefaultHttpClient();
			if (this.useProxy){
				hc.getCredentialsProvider().setCredentials(
						new AuthScope(this.proxyUrl, this.proxyPort),
						new UsernamePasswordCredentials("", "")
				);
				HttpHost proxy = new HttpHost(this.proxyUrl, this.proxyPort);  
				hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
	        try {
				FirstCheck("/3.0.5835z");
				if (SecondCheck("/user_settings/update_device_token"))
					return true;
			} catch (ParseException e) {
				carrier.showError(e);
			} catch (ClientProtocolException e) {
				carrier.showError(e);
			} catch (IOException e) {
				carrier.showError(e);
			}
	        return false;
		}
}
