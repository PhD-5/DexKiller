package com.ssca.format;

public class DexClassDef {
	public byte[] classIdx = new byte[4];
	public byte[] accessFlags = new byte[4];
	public byte[] superclassIdx = new byte[4];
	public byte[] interfacesOff = new byte[4];
	public byte[] sourceFileIdx = new byte[4];
	public byte[] annotationsOff = new byte[4];
	public byte[] classDataOff = new byte[4];
	public byte[] staticValuesOff = new byte[4];
}
