package com.ssca.main;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ssca.dex.DexParser;
import com.ssca.format.DexMethod;

public class MainTest {

	Logger logger = LogManager.getLogger();

	@Test
	public void testMain() {
		//File file = new File("G:\\OK健康.apk");
		File file = new File("F:\\Desktop\\app-debug.apk");
//		List<Dex> dexList = DexParser.parseEachDexFile(file.getAbsolutePath());
//		logger.info("" + dexList.size());
		List<DexMethod> dex = DexParser.getReferedListFromApk(file.getAbsolutePath());
		for (DexMethod dexMethod : dex) {
			logger.info("" + dexMethod.classType + " -> " + dexMethod.name + " " + dexMethod.methodDeclare);
		}
	}

}
