package com.ssca.main;

import java.io.File;
import java.util.List;

import com.ssca.dex.DexParser;
import com.ssca.format.Dex;

public class Main {

	public static void main(String[] args) {
//		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/vpn APk/yijian_VPN__1_VPN_V1.6.2.apk");
//		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/vpn APk/");
		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/text");
//		File dir = new File("/Users/konghaohao/Desktop/test_result/apks0729/");
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			for(File file:files){
				if(!file.getName().contains("apk"))
					continue;
				System.out.println("file:"+file.getAbsolutePath());
				long start = System.currentTimeMillis();
				List<Dex> dexList = DexParser.parseEachDexFile(file.getAbsolutePath());
				long end   = System.currentTimeMillis();
				for(Dex dex:dexList){
					System.out.println("dex:"+dex.dexName+" vpn:"+dex.vpnCount);
				}
			}
		}else{
			List<Dex> dexList = DexParser.parseEachDexFile(dir.getAbsolutePath());
			//			System.out.println(dexList.get(0).stringList);
			//			System.out.println(dexList.get(0).stringList.size());
		}



	}

}
