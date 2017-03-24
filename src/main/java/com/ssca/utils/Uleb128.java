package com.ssca.utils;
import java.io.DataInputStream;
import java.io.IOException;


public class Uleb128 {
	public static int readLeb128(DataInputStream input) throws IOException{
		int result=0; int cur=0;
		result=input.read();
		if(result>0x7f){
			cur=input.read();
			result=(result& 0x7f)|((cur&0x7f)<<7);
			if(cur>0x7f){
				cur=input.read();
				result|=(cur&0x7f)<<14;
			//	result=result|((cur&0x7f)<<14);
				if(cur>0x7f){
					cur=input.read();
					result|=(cur&0x7f)<<21;
					if(cur>0x7f){
						cur=input.read();
						result|=cur<<28;
					}
				}
			}
		}
		return result;
	}
}
