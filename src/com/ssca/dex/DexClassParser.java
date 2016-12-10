package com.ssca.dex;

import java.io.DataInputStream;
import java.io.IOException;

import com.ssca.utils.ByteUtils;

public class DexClassParser {
	
	public static void getClassInfo(DataInputStream dis,int offset, int size) throws IOException{
		dis.skipBytes(offset);
		byte b[] = new byte[4];
		dis.read(b);
		System.out.println(ByteUtils.bytes2HexString(b));
	}

	public static void main(String[] args) {

	}

}
