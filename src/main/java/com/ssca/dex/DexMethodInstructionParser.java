package com.ssca.dex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.jf.baksmali.BaksmaliOptions;
import org.jf.baksmali.Adaptors.MethodDefinition.InvalidSwitchPayload;
import org.jf.baksmali.Adaptors.MethodItem;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.VerificationError;
import org.jf.dexlib2.dexbacked.DexBackedDexFile.InvalidItemIndex;
import org.jf.dexlib2.iface.instruction.DualReferenceInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.util.ExceptionWithContext;

import com.ssca.format.MethodDefinitionEx;

public class DexMethodInstructionParser {

	MethodDefinitionEx methodDef;

	public DexMethodInstructionParser(MethodDefinitionEx methodDef) {
		this.methodDef = methodDef;
	}

	public List<String> getInstruction(Instruction instruction, MethodItem methodItem) {
		Opcode opcode = instruction.getOpcode();
		String verificationErrorName = null;
		List<String> methodReferedList = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			public boolean add(String s) {
				if (s != null && !s.isEmpty() && s.startsWith("L")) {
					super.add(s);
					return true;
				} else {
					return false;
				}
			}
		};
		String referenceString = null;
		String referenceString2 = null;

		boolean commentOutInstruction = false;

		if (instruction instanceof Instruction20bc) {
			int verificationError = ((Instruction20bc) instruction).getVerificationError();
			verificationErrorName = VerificationError.getVerificationErrorName(verificationError);
			if (verificationErrorName == null) {
				// writer.write("#was invalid verification error type: ");
				// writer.printSignedIntAsDec(verificationError);
				// writer.write("\n");
				verificationErrorName = "generic-error";
			}
		}

		if (instruction instanceof ReferenceInstruction) {
			ReferenceInstruction referenceInstruction = (ReferenceInstruction) instruction;
			String classContext = null;
			if (methodDef.classDef.options.implicitReferences) {
				classContext = methodDef.method.getDefiningClass();
			}

			try {
				Reference reference = referenceInstruction.getReference();
				referenceString = ReferenceUtil.getReferenceString(reference, classContext);
				assert referenceString != null;
			} catch (InvalidItemIndex ex) {
				commentOutInstruction = true;
				try {
					referenceString = writeInvalidItemIndex(ex, referenceInstruction.getReferenceType());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (ReferenceType.InvalidReferenceTypeException ex) {
				// writer.write("#invalid reference type: ");
				// writer.printSignedIntAsDec(ex.getReferenceType());
				commentOutInstruction = true;
				referenceString = "invalid_reference";
			}

			if (instruction instanceof DualReferenceInstruction) {
				DualReferenceInstruction dualReferenceInstruction = (DualReferenceInstruction) instruction;
				try {
					Reference reference2 = dualReferenceInstruction.getReference2();
					referenceString2 = ReferenceUtil.getReferenceString(reference2, classContext);
				} catch (InvalidItemIndex ex) {
					commentOutInstruction = true;
					try {
						referenceString2 = writeInvalidItemIndex(ex, dualReferenceInstruction.getReferenceType2());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (ReferenceType.InvalidReferenceTypeException ex) {
					// writer.write("#invalid reference type: ");
					// writer.printSignedIntAsDec(ex.getReferenceType());
					commentOutInstruction = true;
					referenceString2 = "invalid_reference";
				}
			}
		}

		if (instruction instanceof Instruction31t) {
			boolean validPayload = true;

			switch (instruction.getOpcode()) {
			case PACKED_SWITCH:
				int baseAddress = methodDef.getPackedSwitchBaseAddress(
						methodItem.getCodeAddress() + ((Instruction31t) instruction).getCodeOffset());
				if (baseAddress == -1) {
					validPayload = false;
				}
				break;
			case SPARSE_SWITCH:
				baseAddress = methodDef.getSparseSwitchBaseAddress(
						methodItem.getCodeAddress() + ((Instruction31t) instruction).getCodeOffset());
				if (baseAddress == -1) {
					validPayload = false;
				}
				break;
			case FILL_ARRAY_DATA:
				try {
					methodDef.findPayloadOffset(
							methodItem.getCodeAddress() + ((Instruction31t) instruction).getCodeOffset(),
							Opcode.ARRAY_PAYLOAD);
				} catch (InvalidSwitchPayload ex) {
					validPayload = false;
				}
				break;
			default:
				throw new ExceptionWithContext("Invalid 31t opcode: %s", instruction.getOpcode());
			}

			if (!validPayload) {
				// writer.write("#invalid payload reference\n");
				commentOutInstruction = true;
			}
		}

		if (opcode.odexOnly()) {
			if (!isAllowedOdex(opcode)) {
				// writer.write("#disallowed odex opcode\n");
				commentOutInstruction = true;
			}
		}

		if (commentOutInstruction) {
			// writer.write("#");
		}

		switch (instruction.getOpcode().format) {
		case Format10t:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeTargetLabel(writer);
			break;
		case Format10x:
			// if (instruction instanceof UnknownInstruction) {
			// writer.write("#unknown opcode: 0x");
			// writer.printUnsignedLongAsHex(((UnknownInstruction)instruction).getOriginalOpcode());
			// writer.write('\n');
			// }
			// writeOpcode(writer);
			break;
		case Format11n:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeLiteral(writer);
			break;
		case Format11x:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			break;
		case Format12x:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			break;
		case Format20bc:
			// TODO reference
			// writeOpcode(writer);
			// writer.write(' ');
			// writer.write(verificationErrorName);
			// writer.write(", ");
			// writer.write(referenceString);
			methodReferedList.add(referenceString);
			break;
		case Format20t:
		case Format30t:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeTargetLabel(writer);
			break;
		case Format21c:
		case Format31c:
			// TODO reference
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writer.write(referenceString);
			methodReferedList.add(referenceString);
			break;
		case Format21ih:
		case Format21lh:
		case Format21s:
		case Format31i:
		case Format51l:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeLiteral(writer);
			// if (instruction.getOpcode().setsWideRegister()) {
			// writeCommentIfLikelyDouble(writer);
			// } else {
			// boolean isResourceId = writeCommentIfResourceId(writer);
			// if (!isResourceId)
			// writeCommentIfLikelyFloat(writer);
			// }
			break;
		case Format21t:
		case Format31t:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeTargetLabel(writer);
			break;
		case Format22b:
		case Format22s:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			// writer.write(", ");
			// writeLiteral(writer);
			break;
		case Format22c:
			// TODO reference
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			// writer.write(", ");
			// writer.write(referenceString);
			methodReferedList.add(referenceString);
			break;
		case Format22cs:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			// writer.write(", ");
			// writeFieldOffset(writer);
			break;
		case Format22t:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			// writer.write(", ");
			// writeTargetLabel(writer);
			break;
		case Format22x:
		case Format32x:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			break;
		case Format23x:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeFirstRegister(writer);
			// writer.write(", ");
			// writeSecondRegister(writer);
			// writer.write(", ");
			// writeThirdRegister(writer);
			break;
		case Format35c:
			// TODO reference
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRegisters(writer);
			// writer.write(", ");
			// writer.write(referenceString);
			methodReferedList.add(referenceString);
			break;
		case Format35mi:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRegisters(writer);
			// writer.write(", ");
			// writeInlineIndex(writer);
			break;
		case Format35ms:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRegisters(writer);
			// writer.write(", ");
			// writeVtableIndex(writer);
			break;
		case Format3rc:
			// TODO reference
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRangeRegisters(writer);
			// writer.write(", ");
			// writer.write(referenceString);
			methodReferedList.add(referenceString);
			break;
		case Format3rmi:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRangeRegisters(writer);
			// writer.write(", ");
			// writeInlineIndex(writer);
			break;
		case Format3rms:
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRangeRegisters(writer);
			// writer.write(", ");
			// writeVtableIndex(writer);
			break;
		case Format45cc:
			// TODO reference1, 2
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRegisters(writer);
			// writer.write(", ");
			// writer.write(referenceString);
			// writer.write(", ");
			// writer.write(referenceString2);
			methodReferedList.add(referenceString);
			methodReferedList.add(referenceString2);
			break;
		case Format4rcc:
			// TODO reference1, 2
			// writeOpcode(writer);
			// writer.write(' ');
			// writeInvokeRangeRegisters(writer);
			// writer.write(", ");
			// writer.write(referenceString);
			// writer.write(", ");
			// writer.write(referenceString2);
			methodReferedList.add(referenceString);
			methodReferedList.add(referenceString2);
			break;
		default:
			break;
		// assert false;
		}

		if (commentOutInstruction) {
			// writer.write("\nnop");
		}
		return methodReferedList;
	}

	private String writeInvalidItemIndex(InvalidItemIndex ex, int type) throws IOException {
		// writer.write("#");
		// writer.write(ex.getMessage());
		// writer.write("\n");
		return String.format("%s@%d", ReferenceType.toString(type), ex.getInvalidIndex());
	}

	private boolean isAllowedOdex(@Nonnull Opcode opcode) {
		BaksmaliOptions options = methodDef.classDef.options;
		if (options.allowOdex) {
			return true;
		}
		if (methodDef.classDef.options.apiLevel >= 14) {
			return false;
		}
		return opcode.isVolatileFieldAccessor() || opcode == Opcode.THROW_VERIFICATION_ERROR;
	}

}
