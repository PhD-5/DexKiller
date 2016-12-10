package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

import com.ssca.format.Dex;
import com.ssca.utils.ApkUnZip;
import com.ssca.utils.ByteUtils;
import com.ssca.utils.Uleb128;

public class DexStringParser {
	public static void getStringInfo(JarFile jarFile, String dexName, Dex dex) throws IOException{
		DataInputStream dis = ApkUnZip.getDexDataInputStreamWithBuffered(jarFile, dexName);
		if(dis!=null){
			int stringSize = ByteUtils.byte2Int(dex.dexHeader.string_ids_size);
			int stringOff  = ByteUtils.byte2Int(dex.dexHeader.string_ids_off);
			List<Integer> stringOffList = dex.stringOffList;
			// read string offset
			System.out.println("first skip :"+dis.skipBytes(stringOff));;
			byte[] eachStringOff = new byte[4];
			for(int i=0;i<stringSize;i++){
				dis.read(eachStringOff);
				stringOffList.add(i, ByteUtils.byte2Int(eachStringOff));
			}

			// read string data according to offset
			int nextSkip = stringOffList.get(0) - stringSize*4 - 0x70;  //0X70 is the size of header
			List<String> stringList = dex.stringList;
			System.out.println("need to skip "+ nextSkip);
			System.out.println("skip real: "+dis.skipBytes(nextSkip));
			for(int i=0;i<stringSize-1;i++){
				StringBuilder str = new StringBuilder();
				//get chars count which is uleb128
				int charCount = Uleb128.readLeb128(dis); //seem no use
				byte byteTmp = 0;
				while((byteTmp = dis.readByte())!=0){
					str.append((char)byteTmp);
				}
				//				for(int j=0;j<charCount;j++){
				//					byteTmp = dis.readByte();
				//					str.append((char)byteTmp);
				//				}
				//read the end of string
				//				if(dis.readByte()==0){
				System.out.println("string read success" + str);
				stringList.add(i, str.toString());
				//				}else{
				//					System.err.println("sth is wrong");
				//				}

			}
		}else{
			System.err.println("dis is null");
		}
	}
}
