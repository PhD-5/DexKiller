package com.ssca.format;

public class DexMethod {
	public String classType = "";
	public String methodDeclare = "";
	public String name = "";
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return classType+"->"+name+"("+methodDeclare+")";
	}
}
