package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.format.DexMethod;
import com.ssca.utils.ApkUnZip;
import com.ssca.utils.ByteUtils;

public class DexMethodParser {

	public static void getMethodInfo(JarFile jarFile, String dexName, Dex dex) throws IOException {
		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if (dis != null) {
			int methodSize = ByteUtils.byte2Int(dex.dexHeader.method_ids_size);
			int methodOff = ByteUtils.byte2Int(dex.dexHeader.method_ids_off);
			dis.skipBytes(methodOff);
			byte[] classIdx = new byte[2]; // index of type
			byte[] protoIdx = new byte[2]; // index of proto
			byte[] nameIdx = new byte[4]; // index of string
			for (int i = 0; i < methodSize; i++) {
				DexMethod dexMethod = new DexMethod();
				dis.read(classIdx);
				dis.read(protoIdx);
				dis.read(nameIdx);
				dexMethod.classType = dex.typeList.get(ByteUtils.twoBytes2Int(classIdx));
				dexMethod.methodDeclare = dex.protoList.get(ByteUtils.twoBytes2Int(protoIdx));
				dexMethod.name = dex.stringList.get(ByteUtils.byte2Int(nameIdx));
				dex.methodList.add(dexMethod);
			}
		} else {
			System.err.println("dis is null");
		}
	}
}
