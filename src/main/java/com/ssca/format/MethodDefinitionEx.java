package com.ssca.format;

import java.util.List;

import org.jf.baksmali.Adaptors.ClassDefinition;
import org.jf.baksmali.Adaptors.MethodDefinition;
import org.jf.baksmali.Adaptors.MethodItem;
import org.jf.baksmali.Adaptors.Format.InstructionMethodItem;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;

import com.ssca.utils.ReflectorEx;

public class MethodDefinitionEx extends MethodDefinition {

	public MethodDefinitionEx(ClassDefinition classDef, Method method, MethodImplementation methodImpl) {
		super(classDef, method, methodImpl);
	}

	@SuppressWarnings("unchecked")
	public List<MethodItem> getInstructionList() {
		java.lang.reflect.Method method;
		List<MethodItem> methodItems = null;
		try {
			method = ReflectorEx.getMethod(super.getClass(), "getMethodItems", null);
			method.setAccessible(true);
			methodItems = (List<MethodItem>) ReflectorEx.invoke(this, "getMethodItems");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return methodItems;
	}

	@SuppressWarnings("rawtypes")
	public Instruction getInstructionEx(InstructionMethodItem methodItem) {
		Instruction instruction = null;
		try {
			instruction = (Instruction) ReflectorEx.getFieldValue(methodItem, "instruction");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instruction;
	}

}
