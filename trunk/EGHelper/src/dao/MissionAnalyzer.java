package dao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import model.Mission;


public class MissionAnalyzer {
	private String host;
	private String src;	
	public boolean isResult;

	public MissionAnalyzer(String host, String string){
		this.host = host;
		src = string;
	}

	public TreeMap<Integer, Mission> analyze() throws IOException {
		if (StringScanner.findString("escapeTime", src)==null)
			isResult = true;
		else
			isResult = false;
		TreeMap<Integer, Mission> missions = new TreeMap<Integer, Mission>();
		File f = new File(src);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		int i=1;
		while ((s = br.readLine())!=null){
			if (s.contains("missionTitle")){
				s = br.readLine();
				String title = StringScanner.sortString(s, "\">", '<');
				String level = StringScanner.sortString(s, "Lv.", '<');

				s = readLnUntil(br,"separator");
				s = br.readLine();
				String uid = StringScanner.sortString(s, "user_id=", '\"');
				String user = StringScanner.sortString(s, "\">", '<');
				
				s = readLnUntil(br,"separator");
				s = br.readLine();
				String startTime =  StringScanner.sortString(s, "<td>", '<');
				if (!isResult){
					s = this.readLnUntil(br, "mission_id\">");
					String mid = StringScanner.sortString(s, "\">", '<');
					s = br.readLine();
					String escapeTime = StringScanner.sortString(s, "\">", '<');
					s = this.readLnUntil(br, "activity");
					String status = StringScanner.sortString(s, "activity\">", '<');
					Mission mission = new Mission(title,level,user,uid,startTime,escapeTime,status,mid);
					missions.put(i, mission);
				} else {
					s = this.readLnUntil(br, "separator");
					s = br.readLine();
					s = br.readLine();
					String status = StringScanner.sortString(s, ">", '<');
					s = this.readLnUntil(br, host);
					String mid = StringScanner.sortString(s, host, '\"');
					Mission mission = new Mission(title,level,user,uid,startTime,null,status,mid);
					missions.put(i, mission);
				}
				i++;
			}
		}
		return missions;
	}
	
	public String[] analyzeDetail(String fileName) throws IOException{
		String[] s = new String[2];
		s[0] = StringScanner.findString("残り：", fileName);
		s[0] = StringScanner.sortString(s[0], "残り：", '.');
		s[1] = StringScanner.findString("solidGauge", fileName);
		s[1] = StringScanner.sortString(s[1], "title=\"", '%');
		return s;
	}

	private String readLnUntil(BufferedReader br, String string) throws IOException {
		String s;
		do{
			s = br.readLine();
		}while(!s.contains(string));
		return s;
	}

}
