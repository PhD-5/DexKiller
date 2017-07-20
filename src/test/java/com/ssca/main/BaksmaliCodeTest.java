package com.ssca.main;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ssca.dex.DexParser;

public class BaksmaliCodeTest {

	static Logger logger = LogManager.getLogger();

	@Test
	public void test() {
		// Set<String> methodReferedSet =
		// DexParser.getMethodReferedListFromApkByClass("E:\\Desktop\\app-debug.apk",
		// "Lu/can/i/up/helloworld/MainActivity");
		Set<String> methodReferedSet = DexParser.getMethodReferedListFromApkByClass(
				"E:\\chapter6apk\\e411370d3a3717a571be3708ffe0aa81.apk",
				"Lcom/dianxinos/optimizer/module/space/SpaceClearActivity");
		// Set<String> methodReferedSet =
		// DexParser.getMethodReferedListFromApk("E:\\Desktop\\app-debug.apk");
		logger.info("" + methodReferedSet.size());
		for(String s: methodReferedSet){
			logger.info("" + s);
		}
	}

}
