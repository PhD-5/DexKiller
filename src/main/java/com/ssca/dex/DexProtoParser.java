package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;
import com.ssca.utils.ByteUtils;

public class DexProtoParser {
	public static void getProtoInfo(JarFile jarFile, String dexName, Dex dex) throws IOException{
		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if(dis!=null){
			int protoSize = ByteUtils.byte2Int(dex.dexHeader.proto_ids_size);
			int protoOff  = ByteUtils.byte2Int(dex.dexHeader.proto_ids_off);
			dis.skipBytes(protoOff);
			List<String> stringList = dex.stringList;
			byte[]shortyIdx = new byte[4];
			for(int i=0; i<protoSize;i++){
				dis.read(shortyIdx);
				dis.skipBytes(8);// skip over returnType and parameters
				dex.protoList.add(i, stringList.get(ByteUtils.byte2Int(shortyIdx)));
			}
			
		}else{
			System.err.println("dis is null");
		}
	}
}
