package com.ssca.main;

import java.io.File;
import java.util.List;

import com.ssca.dex.DexParser;
import com.ssca.format.Dex;

public class Main {

	public static void main(String[] args) {
//		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/vpn APk/yijian_VPN__1_VPN_V1.6.2.apk");
//		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/vpn特征不明显");
		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/vpn APk/");
//		File dir = new File("/Users/konghaohao/Documents/worklist/16-12-20-Android_VPN_DETECT/text");
//		File dir = new File("/Users/konghaohao/Desktop/test_result/apks0729/");
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			for(File file:files){
				if(!file.getName().contains("apk"))
					continue;
				long start = System.currentTimeMillis();
				List<Dex> dexList = DexParser.parseEachDexFile(file.getAbsolutePath());
				long end   = System.currentTimeMillis();
				int total = 0;
				for(Dex dex:dexList){
					total+=dex.vpnCount;
					System.out.println(dex.vpnMap);
//					System.out.println("["+(end-start)+"] file:"+file.getName()+" dex:"+dex.dexName+" vpn:"+dex.vpnCount +" map:"+dex.vpnMap);
//					System.out.println("["+(end-start)+"] file:"+file.getName()+" dex:"+dex.dexName+" vpn:"+dex.vpnCount );
				}
				
				if(total>=80)
					System.out.println("["+(end-start)+"] file:"+file.getName()+" YES" +"["+total+"]");
				else if(total >20)
					System.out.println("["+(end-start)+"] file:"+file.getName()+" MAY" +"["+total+"]");
				else
					System.out.println("["+(end-start)+"] file:"+file.getName()+" NO"  +"["+total+"]");
			}
		}else{
			List<Dex> dexList = DexParser.parseEachDexFile(dir.getAbsolutePath());
			//			System.out.println(dexList.get(0).stringList);
			//			System.out.println(dexList.get(0).stringList.size());
		}



	}

}
