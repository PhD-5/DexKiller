package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;
import com.ssca.utils.ByteUtils;

public class DexClassParser {

	static Logger logger = LogManager.getLogger();

	public static void getClassInfo(JarFile jarFile, String dexName, Dex dex) throws IOException {
		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if (dis != null) {
			int classSize = ByteUtils.byte2Int(dex.dexHeader.class_defs_size);
			int classOff = ByteUtils.byte2Int(dex.dexHeader.class_defs_off);
			List<String> typeList = dex.typeList;
			// byte[]eachClass = new byte[32];
			byte[] eachClassIdx = new byte[4];
			dis.skipBytes(classOff);
			for (int i = 0; i < classSize; i++) {
				dis.read(eachClassIdx);
				dis.skipBytes(28);
				// logger.info("get class : "
				// +typeList.get(ByteUtils.byte2Int(eachClassIdx)));
				dex.classList.add(typeList.get(ByteUtils.byte2Int(eachClassIdx)));
			}
		} else {
			logger.error("dis is null");
		}
	}

}
