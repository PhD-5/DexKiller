package com.ssca.dex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuleUtils {
	public static List<String> rules = new ArrayList<String>();
	
	public static void readRuleTxt(){
		File ruleTxt = new File("apiRules.txt");
		FileReader fr;
		try {
			fr = new FileReader(ruleTxt);
			BufferedReader br = new BufferedReader(fr);
			String lineStr;
			lineStr = br.readLine();
			while(lineStr!=null){
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
		
	}

	public static int matchStr(String s){
		int count = 0;
		for(String rule:rules){
			if(s.toLowerCase().contains(rule))
				count++;
		}
		return count;
	}
}
