package com.ssca.dex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

import org.jf.baksmali.BaksmaliOptions;
import org.jf.baksmali.Adaptors.ClassDefinition;
import org.jf.baksmali.Adaptors.MethodItem;
import org.jf.baksmali.Adaptors.Format.InstructionMethodItem;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;

import com.google.common.collect.Ordering;
import com.ssca.format.Dex;
import com.ssca.format.DexMethod;
import com.ssca.format.MethodDefinitionEx;
import com.ssca.utils.ApkUnZip;

public class DexParser {

	// public static List<String> allClassList = new ArrayList<>();

	public static List<Dex> parseEachDexFile(String apkPath) {
		List<Dex> res = new ArrayList<Dex>();

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(apkPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int dexCount = ApkUnZip.getDexCount(jarFile);
		for (int i = 1; i <= dexCount; i++) {
			String dexName;
			if (i == 1)
				dexName = "classes.dex";
			else
				dexName = "classes" + i + ".dex";
			try {
				System.out.println("start parse " + dexName);
				Dex thisDex = new Dex(dexName);
				DexHeaderParser.getHeaderInfo(jarFile, dexName, thisDex);
				DexStringParser.getStringInfo(jarFile, dexName, thisDex);
				DexTypeParser.getTypeInfo(jarFile, dexName, thisDex);
				DexClassParser.getClassInfo(jarFile, dexName, thisDex);
				DexProtoParser.getProtoInfo(jarFile, dexName, thisDex);
				DexMethodParser.getMethodInfo(jarFile, dexName, thisDex);

				res.add(thisDex);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// System.out.println("all classes count: "+allClassList.size());
		return res;
	}

	// public static void main(String[] args) {
	// String s = "/Users/konghaohao/Desktop/test_result/APK/baidumap.apk";
	// long starTime = System.currentTimeMillis();
	// List<Dex> dexResult = DexParser.parseEachDexFile(s);
	// long endTime = System.currentTimeMillis();
	// System.out.println("耗时：" + (endTime - starTime) + " ms");
	// }

	/**
	 * @param apkPath
	 *            - apk路径.
	 * @return 返回系统调用方法(非声明方法)列表.
	 */
	public static List<DexMethod> getSystemMethodReferedListFromApk(String apkPath) {
		List<DexMethod> methodDefinedList = new ArrayList<DexMethod>();
		List<DexMethod> systemMethodReferedList = new ArrayList<DexMethod>();
		Set<DexMethod> methodSet = new HashSet<DexMethod>();
		Set<String> classSet = new HashSet<String>();
		List<Dex> dexList = parseEachDexFile(new File(apkPath).getAbsolutePath());
		for (Dex dex : dexList) {
			methodSet.addAll(dex.methodList);
			classSet.addAll(dex.classList);
		}
		updateMethodInfo(methodSet, classSet, methodDefinedList, systemMethodReferedList);
		return systemMethodReferedList;
	}

	/**
	 * @param apkPath
	 *            - apk路径.
	 * @return 返回类列表.
	 */
	public static List<String> getClassListFromApk(String apkPath) {
		Set<String> classSet = new HashSet<String>();
		List<String> classList = new ArrayList<String>();
		List<Dex> dexList = parseEachDexFile(new File(apkPath).getAbsolutePath());
		for (Dex dex : dexList) {
			classSet.addAll(dex.classList);
		}
		classList.addAll(classSet);
		return classList;
	}

	private static void updateMethodInfo(Set<DexMethod> methodSet, Set<String> classSet,
			List<DexMethod> methodDefinedList, List<DexMethod> systemMethodReferedList) {
		if (!methodSet.isEmpty()) {
			for (DexMethod dexMethod : methodSet) {
				if (classSet.contains(dexMethod.classType)) {
					methodDefinedList.add(dexMethod);
				} else {
					systemMethodReferedList.add(dexMethod);
				}
			}
		}
	}

	/**
	 * @author lczgywzyy
	 * 
	 * @param apkPath
	 *            - apk路径.
	 * @return apk中调用的方法.
	 */
	public static Set<String> getMethodReferedListFromApk(String apkPath) {
		Set<String> methodReferedSet = new HashSet<String>();
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(apkPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int dexCount = ApkUnZip.getDexCount(jarFile);
		for (int i = 1; i <= dexCount; i++) {
			String dexName;
			if (i == 1) {
				dexName = "classes.dex";
			} else {
				dexName = "classes" + i + ".dex";
			}
			File mApkFile = new File(apkPath);
			DexBackedDexFile dexFile = null;
			try {
				dexFile = DexFileFactory.loadDexEntry(mApkFile, dexName, true, Opcodes.forApi(0));
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<? extends ClassDef> classDefs = Ordering.natural().sortedCopy(dexFile.getClasses());
			for (final ClassDef classDef : classDefs) {
				// class name.
				String classDescriptor = classDef.getType();
				// logger.info("[Class]" + classDescriptor);
				if (classDescriptor.charAt(0) != 'L' || classDescriptor.charAt(classDescriptor.length() - 1) != ';') {
					continue;
				}
				methodReferedSet.addAll(disassembleClass(classDef));
			}
		}
		return methodReferedSet;
	}

	/**
	 * @author lczgywzyy
	 * 
	 * @param apkPath
	 *            - apk路径.
	 * @param classname
	 *            - 待分析的类.
	 * @return apk指定类中调用的方法.
	 */
	public static Set<String> getMethodReferedListFromApkByClass(String apkPath, String classname) {
		Set<String> methodReferedSet = new HashSet<String>();
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(apkPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int dexCount = ApkUnZip.getDexCount(jarFile);
		for (int i = 1; i <= dexCount; i++) {
			String dexName;
			if (i == 1) {
				dexName = "classes.dex";
			} else {
				dexName = "classes" + i + ".dex";
			}
			File mApkFile = new File(apkPath);
			DexBackedDexFile dexFile = null;
			try {
				dexFile = DexFileFactory.loadDexEntry(mApkFile, dexName, true, Opcodes.forApi(0));
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<? extends ClassDef> classDefs = Ordering.natural().sortedCopy(dexFile.getClasses());
			for (final ClassDef classDef : classDefs) {
				// class name.
				String classDescriptor = classDef.getType();
				// logger.info("[Class]" + classDescriptor);
				if (classDescriptor.charAt(0) != 'L' || classDescriptor.charAt(classDescriptor.length() - 1) != ';') {
					continue;
				}
				if (classDescriptor.equals(classname + ";")) {
					methodReferedSet = disassembleClass(classDef);
					break;
				}
			}
		}
		return methodReferedSet;
	}

	@SuppressWarnings("rawtypes")
	private static Set<String> disassembleClass(ClassDef classDef) {
		Set<String> methodReferedSet = new HashSet<String>();
		ClassDefinition classDefinition = new ClassDefinition(new BaksmaliOptions(), classDef);
		Iterable<? extends Method> directMethods, virtualMethods;
		if (classDef instanceof DexBackedClassDef) {
			directMethods = ((DexBackedClassDef) classDef).getDirectMethods(false);
			virtualMethods = ((DexBackedClassDef) classDef).getVirtualMethods(false);
		} else {
			directMethods = classDef.getDirectMethods();
			virtualMethods = classDef.getVirtualMethods();
		}
		Set<Method> methodSet = new HashSet<Method>();
		Iterator di = directMethods.iterator();
		while (di.hasNext()) {
			methodSet.add((Method) di.next());
		}
		Iterator vi = virtualMethods.iterator();
		while (vi.hasNext()) {
			methodSet.add((Method) vi.next());
		}
		for (Method method : methodSet) {
			MethodImplementation methodImpl = method.getImplementation();
			if (methodImpl == null) {
				// TODO
			} else {
				MethodDefinitionEx methodDefinitionex = new MethodDefinitionEx(classDefinition, method, methodImpl);
				methodReferedSet.addAll(getMethodReferedList(methodDefinitionex, method));
			}
		}
		return methodReferedSet;
	}

	@SuppressWarnings({ "rawtypes" })
	private static Set<String> getMethodReferedList(MethodDefinitionEx methodDefinitionex, Method method) {
		Set<String> methodReferedSet = new HashSet<String>();
		List<MethodItem> methodItems = methodDefinitionex.getInstructionList();
		// logger.info("[Method]" + method.toString());
		DexMethodInstructionParser dmio = new DexMethodInstructionParser(methodDefinitionex);
		for (MethodItem methodItem : methodItems) {
			if (methodItem instanceof InstructionMethodItem) {
				Instruction instruction = methodDefinitionex.getInstructionEx((InstructionMethodItem) methodItem);
				methodReferedSet.addAll(dmio.getInstruction(instruction, methodItem));
			}
		}
		return methodReferedSet;
	}
}
