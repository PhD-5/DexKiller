package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;

public class DexHeaderParser {
	public static void getHeaderInfo(JarFile jarFile, String dexName, Dex dex) throws IOException {

		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if (dis != null) {
			// byte[] string_ids_size = new byte[4];
			// byte[] string_ids_off = new byte[4];
			// byte[] type_ids_size = new byte[4];
			// byte[] type_ids_off = new byte[4];
			// byte[] proto_ids_size= new byte[4];
			// byte[] proto_ids_off= new byte[4];
			// byte[] field_ids_size= new byte[4];
			// byte[] field_ids_off= new byte[4];
			// byte[] method_ids_size = new byte[4];
			// byte[] method_ids_off = new byte[4];
			// byte[] class_defs_size = new byte[4];
			// byte[] class_defs_off = new byte[4];

			// read header info
			dis.skipBytes(56);
			dis.read(dex.dexHeader.string_ids_size);
			dis.read(dex.dexHeader.string_ids_off);
			dis.read(dex.dexHeader.type_ids_size);
			dis.read(dex.dexHeader.type_ids_off);
			dis.read(dex.dexHeader.proto_ids_size);
			dis.read(dex.dexHeader.proto_ids_off);
			dis.read(dex.dexHeader.field_ids_size);
			dis.read(dex.dexHeader.field_ids_off);
			dis.read(dex.dexHeader.method_ids_size);
			dis.read(dex.dexHeader.method_ids_off);
			dis.read(dex.dexHeader.class_defs_size);
			dis.read(dex.dexHeader.class_defs_off);
			dis.read(dex.dexHeader.data_size);
			dis.read(dex.dexHeader.data_off);

			// store byte[] into instance
			// dexHeader.string_ids_size = string_ids_size;
			// dexHeader.string_ids_off =string_ids_off;
			// dexHeader.type_ids_size=type_ids_size;
			// dexHeader.type_ids_off=type_ids_off;
			// dexHeader.proto_ids_size=proto_ids_size;
			// dexHeader.proto_ids_off=proto_ids_off;
			// dexHeader.field_ids_size=field_ids_size;
			// dexHeader.field_ids_size=field_ids_size;
			// dexHeader.method_ids_size=method_ids_size;
			// dexHeader.method_ids_size=method_ids_size;
			// dexHeader.class_defs_off = class_defs_off;
			// dexHeader.class_defs_size = class_defs_size;
		} else {
			System.err.println("data input stream is null!");
		}
		// byte[] magic = new byte[8];
		// dis.read(magic);
		// String magicStr = new String(magic);
		// System.out.println(magicStr);
		// byte[]headersize= new byte[4];
		// dis.skipBytes(36);
		// dis.read(headersize);
		// System.out.println();

		// System.out.println(ByteUtils.byte2Int(class_defs_size));
		// System.out.println(ByteUtils.byte2Int(class_defs_off));
		// DexClassParser.getClassInfo(dis, ByteUtils.byte2Int(class_defs_off),
		// ByteUtils.byte2Int(class_defs_size));
	}

	// public static void main(String[] args) {
	// String s = "/Users/konghaohao/Desktop/test_result/APK/baidumap.apk";
	// }

}
