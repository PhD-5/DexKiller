package com.ssca.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ApkUnZip {

	public static int getDexCount(JarFile jarFile){
		int dexCount = 0;
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			final JarEntry je = entries.nextElement();
			if (je.getName().contains("classes") && je.getName().endsWith("dex")) {
				dexCount++;
			}
		}
		return dexCount;
	}

	@SuppressWarnings("resource")
	public static byte[] getDex(JarFile jarFile) {
		try {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				final JarEntry je = entries.nextElement();
				if (je.getName().contains("classes") && je.getName().endsWith("dex")) {
					int fileSize = (int) je.getSize();
					byte data[] = new byte[fileSize];
					InputStream is = new BufferedInputStream(jarFile.getInputStream(je));
					is.read(data, 0, fileSize);
					return data;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@SuppressWarnings("resource")
	public static List<DataInputStream> getDexDataInputStream(String filePathString) {
		List<DataInputStream> disList = new ArrayList<DataInputStream>();
		JarFile jarFile;
		try {
			jarFile = new JarFile(filePathString);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				final JarEntry je = entries.nextElement();
				if (je.getName().contains("classes")&&je.getName().endsWith("dex")) {
					System.out.println("file name:"+je.getName());
					System.out.println("file size:"+je.getSize());
					DataInputStream dis = new DataInputStream(jarFile.getInputStream(je));
					disList.add(dis);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return disList;
	}

	public static DataInputStream getDexDataInputStreamWithBuffered(JarFile jarFile, String dexName) {
		try {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				final JarEntry je = entries.nextElement();
				if (je.getName().equals(dexName)) {
					DataInputStream dis = new DataInputStream(new BufferedInputStream(jarFile.getInputStream(je)));
					return dis;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String s  = "/Users/konghaohao/Desktop/test_result/APK/baidumap.apk";
		ApkUnZip.getDexDataInputStream(s);
	}
}
