package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;
import com.ssca.utils.ByteUtils;
import com.ssca.utils.MUTF8;
import com.ssca.utils.Uleb128;

public class DexStringParser {
	@SuppressWarnings("unused")
	public static void getStringInfo(JarFile jarFile, String dexName, Dex dex) throws Exception{
		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if(dis!=null){
			int stringSize = ByteUtils.byte2Int(dex.dexHeader.string_ids_size);
			int stringOff  = ByteUtils.byte2Int(dex.dexHeader.string_ids_off);
			List<Integer> stringOffList = dex.stringOffList;
			// read string offset
			dis.skipBytes(stringOff);
			byte[] eachStringOff = new byte[4];
			for(int i=0;i<stringSize;i++){
				dis.read(eachStringOff);
				stringOffList.add(i, ByteUtils.byte2Int(eachStringOff));
			}

			// read string data according to offset
			int nextSkip = stringOffList.get(0) - stringSize*4 - 0x70;  //0X70 is the size of header
			List<String> stringList = dex.stringList;
			dis.skipBytes(nextSkip);
			for(int i=0;i<stringSize-1;i++){
				//get chars count from first uleb128
				int charCount = Uleb128.readLeb128(dis);
				//alloc char [] according to count
				char[] out = new char[charCount];
				//MUTF8 decode
				String s = MUTF8.decode(dis, out);
				//store s
				stringList.add(i, s);
			}
		}else{
			System.err.println("dis is null");
		}
	}
}
