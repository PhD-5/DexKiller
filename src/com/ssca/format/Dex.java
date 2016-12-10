package com.ssca.format;

import java.util.ArrayList;
import java.util.List;

public class Dex {
	public String dexName;
	public DexHeader dexHeader = new DexHeader();
	public List<Integer> stringOffList = new ArrayList<Integer>();
	public List<String> stringList = new ArrayList<String>();
	
	public Dex(String dexName){
		this.dexName = dexName;
	}
}
