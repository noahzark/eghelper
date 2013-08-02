package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RankInfoAnalyzer {
	String file;

	public RankInfoAnalyzer(String fileRankPage) {
		this.file = fileRankPage;
	}
	
	private boolean readUntil(FileInputStream fis, String string) throws IOException{
		int c;
		StringBuffer sb = new StringBuffer();
		while ((c = fis.read())!=-1){
			sb.append((char)c);
			if (!string.startsWith(sb.toString()))
				sb.setLength(0);
			if (string.equals(sb.toString()))
				return true;
		};
		return false;
	}
	
	private String getUntil(FileInputStream fis, char c) throws IOException{
		int temp;
		StringBuffer sb = new StringBuffer();
		while ((temp = fis.read())!=-1){
			if ((char)temp==c)
				return sb.toString();
			if ((char)temp==',')
				continue;
			sb.append((char)temp);
			if (sb.toString().endsWith("\\"+"u")){
				sb.setLength(sb.length()-2);
				fis.read();
				fis.read();
				fis.read();
				fis.read();
			}
		};
		return null;
	}

	public String analyze(int i) {
		try {
			File f = new File(file);
			FileInputStream fis = new FileInputStream(f);
			if (readUntil(fis, i+"\\"+"u4f4d:"))
				if (readUntil(fis, "colorRL\\\">"))
					return getUntil(fis, '<');
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
