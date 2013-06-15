package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StringScanner {
	public static String findString(String key,String fileName) {
		File f = new File(fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			while ((s = br.readLine())!=null){
				if (s.contains(key)){
					br.close();
					return s;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
