package com.ssca.dex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.format.DexMethod;
import com.ssca.utils.ApkUnZip;

public class DexParser {

	// public static List<String> allClassList = new ArrayList<>();

	public static List<Dex> parseEachDexFile(String apkPath) {
		List<Dex> res = new ArrayList<Dex>();

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(apkPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int dexCount = ApkUnZip.getDexCount(jarFile);
		for (int i = 1; i <= dexCount; i++) {
			String dexName;
			if (i == 1)
				dexName = "classes.dex";
			else
				dexName = "classes" + i + ".dex";
			try {
				System.out.println("start parse " + dexName);
				Dex thisDex = new Dex(dexName);
				DexHeaderParser.getHeaderInfo(jarFile, dexName, thisDex);
				DexStringParser.getStringInfo(jarFile, dexName, thisDex);
				DexTypeParser.getTypeInfo(jarFile, dexName, thisDex);
				DexClassParser.getClassInfo(jarFile, dexName, thisDex);
				DexProtoParser.getProtoInfo(jarFile, dexName, thisDex);
				DexMethodParser.getMethodInfo(jarFile, dexName, thisDex);
				DexMethodParser.updateMethodInfo(thisDex);
				res.add(thisDex);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// System.out.println("all classes count: "+allClassList.size());
		return res;
	}

	// public static void main(String[] args) {
	// String s = "/Users/konghaohao/Desktop/test_result/APK/baidumap.apk";
	// long starTime = System.currentTimeMillis();
	// List<Dex> dexResult = DexParser.parseEachDexFile(s);
	// long endTime = System.currentTimeMillis();
	// System.out.println("耗时：" + (endTime - starTime) + " ms");
	// }

	public static List<DexMethod> getReferedListFromApk(String apkPath) {
		List<DexMethod> returnList = new ArrayList<DexMethod>();
		List<Dex> dexList = parseEachDexFile(new File(apkPath).getAbsolutePath());
		for (Dex dex : dexList) {
			returnList.addAll(dex.methodReferedList);
		}
		return returnList;
	}

}
