package com.ssca.dex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.ssca.format.Dex;

public class RuleUtils {
	public static Set<String> rules = new HashSet<String>();
	public static Map<String,Integer> ruleValue = new HashMap<>();
	public static void readRuleTxt(){
//		File ruleTxt = new File("apiRules.properties");
		try{
			//创建一个Properties
	        Properties properties = new Properties();
	        //建立输入字符流对象
	        FileReader fileReader = new FileReader("apiRules.properties");
	        //加载配置文件的数据到Properties是使用load方法。
	        properties.load(fileReader);
	        //遍历元素
	        Set<Entry<Object, Object>> set = properties.entrySet();
	        for(Entry<Object, Object> entry: set){
	        	String key = entry.getKey().toString().toLowerCase().trim();
	        	rules.add(key);
	        	ruleValue.put(key, Integer.parseInt(entry.getValue().toString()));
	        }

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		FileReader fr;
		try {
			fr = new FileReader(ruleTxt);
			BufferedReader br = new BufferedReader(fr);
			String lineStr;
			lineStr = br.readLine();
			while(lineStr!=null){
				System.out.println(lineStr);
				rules.add(lineStr.toLowerCase());
				lineStr = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/	
	}

	public static void matchStr(String s,Dex dex){
		if(s.length()>20)
			return;
		for(String rule:rules){
			if(s.toLowerCase().contains(rule)){
//				if(rule.equals("gae"))
//					System.out.println(s);
				if(dex.vpnMap.containsKey(rule))
					dex.vpnMap.put(rule, dex.vpnMap.get(rule)+1);
				else
					dex.vpnMap.put(rule, 1);
////				System.out.println(s+":"+rule);
//				if(rule.equals("vpn"))
//					count+=20;
//				else if(rule.equals("sslsocket"))
//					count+=10;
//				else
//					count++;
			}
		}
	}

	public static void countVPN(Dex dex){
		for(String key: dex.vpnMap.keySet()){
			dex.vpnCount+=(ruleValue.get(key)*dex.vpnMap.get(key));
		}
	}
}
