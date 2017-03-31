package com.ssca.format;

public class DexMethod {
	public String classType = "";
	public String methodDeclare = "";
	public String name = "";

	@Override
	public String toString() {
		String rtn = "";
		// if(null != methodDeclare){
		// rtn = rtn + methodDeclare.charAt(0) + ":";
		// }
		rtn += classType + "->" + name + "(" + methodDeclare.substring(1).trim() + ")" + methodDeclare.charAt(0);
		return rtn;
	}
}
