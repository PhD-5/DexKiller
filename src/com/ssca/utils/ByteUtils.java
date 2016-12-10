package com.ssca.utils;

public class ByteUtils {
	public static int byte2Int(byte[] res){
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或   
				| ((res[2] << 24) >>> 8) | (res[3] << 24);   
		return targets;   
	}
	
	public static int twoBytes2Int(byte[] res){
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00);   
		return targets; 
	}
	
	public static String bytes2HexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
}
