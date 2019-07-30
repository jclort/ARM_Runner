package driver;

import java.util.ArrayList;

public class SystemState {

	public static final int FP_REGISTER = 29;
	public static final int LR_REGISTER = 30;
	public static final int SP_REGISTER = 31;

	public MemoryManager memoryManager = new MemoryManager();

	public Register PC = new Register(-1, 0);

	private Register[] registers = new Register[32];

	public SystemState() {
		for(int count = 0; count < registers.length; count++) {
			registers[count] = new Register(count);
		}
		registers[SP_REGISTER].setValue(memoryManager.addressMaximum-MemoryManager.WORD_SIZE*2);
	}
	
	public long getValueAtRegister(int index) {
		return registers[index].getValue();
	}

	public void setRegisterValue(int index, long value) {
		registers[index].setValue(value);
	}

	public long getValueAtFP() {
		return registers[FP_REGISTER].getValue();
	}

	public long getValueAtLR() {
		return registers[LR_REGISTER].getValue();
	}

	public long getValueAtSP() {
		return registers[SP_REGISTER].getValue();
	}

	public void setFPValue(long value) {
		registers[FP_REGISTER].setValue(value);
	}

	public void setLRValue(long value) {
		registers[LR_REGISTER].setValue(value);
	}

	public void setSPValue(long value) {
		registers[SP_REGISTER].setValue(value);
	}

	private boolean flagN; // Negative
	private boolean flagZ; // Zero
	private boolean flagC; // Carry (or unsigned overflow)
	private boolean flagV; // Overflow (signed)

	public boolean getN() {
		return flagN;
	}

	public boolean getZ() {
		return flagZ;
	}

	public boolean getC() {
		return flagC;
	}

	public boolean getV() {
		return flagV;
	}
	
	/**
	 * We only want to allow for modification of all the flags at once. They should
	 * never be set individually. There are no cases in arm where they can be
	 * individually set.
	 *
	 * @param N negative
	 * @param Z zero
	 * @param C carry
	 * @param V overflow
	 */
	public void setFlags(boolean N, boolean Z, boolean C, boolean V) {
		flagN = N;
		flagZ = Z;
		flagC = C;
		flagV = V;
	}

	/**
	 * Stores 2 words since a register is 8 bytes
	 *
	 * @param registerIndex
	 * @param address
	 */
	public void storeRegister(int registerIndex, long address) {
		long regValue = registers[registerIndex].getValue();

		memoryManager.storeDoubleWord(address, regValue);
	}

	public void loadRegister(int registerIndex, long address) {
		setRegisterValue(registerIndex, memoryManager.loadDoubleWord(address));
	}
	
	public Register[] getRegisterArray() {
		return registers;
	}
	
	public void storeData(ArrayList<String> types, ArrayList<Object> values) {
		long currAddress = 0;
		for(int i = 0; i < types.size(); i++) {
			switch(types.get(i).toUpperCase()) {
			case "HALF":
			case "2BYTE":
				memoryManager.storeHalfWord(currAddress, (short)values.get(i));
				currAddress += 2;
				break;
			case "WORD":
			case "4BYTE":
				memoryManager.storeWord(currAddress, (int)values.get(i));
				currAddress += 4;
				break;
			case "QUAD":
			case "8BYTE":
				memoryManager.storeDoubleWord(currAddress, (long)values.get(i));
				currAddress += 8;
				break;
			default: //ascii, asciz, or string
				
			}
		}
	}
}
