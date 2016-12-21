package com.ssca.dex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;

public class DexParser {
	static{
		//read rule
				RuleUtils.readRuleTxt();
	}
	
	public static List<Dex> parseEachDexFile(String apkPath){
		
		
		List<Dex> res = new ArrayList<Dex>();
		
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(apkPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int dexCount = ApkUnZip.getDexCount(jarFile);
		for(int i=1; i<=dexCount; i++){
			String dexName;
			if(i==1)
				dexName = "classes.dex";
			else
				dexName = "classes"+i+".dex";
			try {
				Dex thisDex = new Dex(dexName);
				DexHeaderParser.getHeaderInfo(jarFile, dexName, thisDex);
				DexStringParser.getStringInfo(jarFile, dexName, thisDex);
//				DexTypeParser.getTypeInfo(jarFile, dexName, thisDex);
//				DexClassParser.getClassInfo(jarFile, dexName, thisDex);
//				DexProtoParser.getProtoInfo(jarFile, dexName, thisDex);
//				DexMethodParser.getMethodInfo(jarFile, dexName, thisDex);
				RuleUtils.countVPN(thisDex);
				res.add(thisDex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return res;
	}
}
