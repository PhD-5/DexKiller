package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;

public class DexParser {

	public static void parseEachDexFile(String apkPath){
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
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//			}else
			//				System.err.println("dex data input stream is null");
		}
	}

	public static void main(String[] args) {
		String s  = "/Users/konghaohao/Desktop/test_result/APK/baidumap.apk";
		DexParser.parseEachDexFile(s);
	}

}
