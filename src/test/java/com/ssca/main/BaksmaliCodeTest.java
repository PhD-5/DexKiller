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
		Set<String> methodReferedSet = DexParser.getMethodReferedListFromApkByClass("E:\\Desktop\\app-debug.apk",
				"Lu/can/i/up/helloworld/MainActivity");
		// Set<String> methodReferedSet =
		// DexParser.getMethodReferedListFromApk("E:\\Desktop\\app-debug.apk");
		logger.info("" + methodReferedSet.size());
	}

}
