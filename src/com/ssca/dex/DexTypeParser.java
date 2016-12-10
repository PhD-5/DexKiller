package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;
import com.ssca.utils.ByteUtils;

public class DexTypeParser {
	public static void getTypeInfo(JarFile jarFile, String dexName, Dex dex) throws IOException{
		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if(dis!=null){
			int typeSize = ByteUtils.byte2Int(dex.dexHeader.type_ids_size);
			int typeOff  = ByteUtils.byte2Int(dex.dexHeader.type_ids_off);
			List<String> typeList = dex.typeList;
			List<String> stringList = dex.stringList;
			dis.skipBytes(typeOff);
			byte[] eachType = new byte[4];
			for(int i=0;i<typeSize;i++){
				dis.read(eachType);
				typeList.add(i, stringList.get(ByteUtils.byte2Int(eachType)));
			}
			
		}else{
			System.err.println("dis is null");
		}
	}
}
