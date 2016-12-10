package com.ssca.dex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;

public class DexParser {
	
	public static List<String> allClassList = new ArrayList<>();

	public static List<String> parseEachDexFile(String apkPath){
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
			//			DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
			//			if(dis!=null){
			try {
				System.out.println("start parse "+dexName);
				Dex thisDex = new Dex(dexName);
				DexHeaderParser.getHeaderInfo(jarFile, dexName, thisDex);
				DexStringParser.getStringInfo(jarFile, dexName, thisDex);
				DexTypeParser.getTypeInfo(jarFile, dexName, thisDex);
//				DexClassParser.getClassInfo(jarFile, dexName, thisDex);
				DexProtoParser.getProtoInfo(jarFile, dexName, thisDex);
				DexMethodParser.getMethodInfo(jarFile, dexName, thisDex);
//				allClassList.addAll(thisDex.classList);
				System.out.println(thisDex.methodList.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return allClassList;
	}

	public static void main(String[] args) {
		String s  = "/Users/konghaohao/Desktop/test_result/APK/baidumap.apk";
		long starTime=System.currentTimeMillis();
		DexParser.parseEachDexFile(s);
		long endTime=System.currentTimeMillis();
		System.out.println("耗时："+(endTime-starTime)+" ms");
	}

}
