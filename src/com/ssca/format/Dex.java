package com.ssca.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dex {
	//for vpn recognize
	public int vpnCount=0;
	public Map<String,Integer> vpnMap = new HashMap<>();
	
	public String dexName;
	public DexHeader dexHeader = new DexHeader();
	public List<Integer> stringOffList = new ArrayList<Integer>();
	public List<String> stringList = new ArrayList<String>();
	public List<String> typeList = new ArrayList<String>();
	public List<String> protoList = new ArrayList<String>();
	public List<String> classList = new ArrayList<String>();
	public List<DexMethod> methodList = new ArrayList<DexMethod>();
	
	public Dex(String dexName){
		this.dexName = dexName;
	}
}
