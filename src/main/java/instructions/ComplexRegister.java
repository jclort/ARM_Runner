package instructions;

import driver.SystemState;
import util.ShiftDirection;

public class ComplexRegister implements DataSource {

	private int regIndex;
	private int shiftAmount = 0;
	int size;
	private ShiftDirection shiftDirection = ShiftDirection.NONE;
	private boolean writeback = false; //will write back to the register you are shifting

	
	public ComplexRegister(int regIndex) {
		this.regIndex = regIndex;
	}

	public ComplexRegister(int regIndex, ShiftDirection shiftDirection, int shiftAmount) {
		this.regIndex = regIndex;
		this.shiftDirection = shiftDirection;
		this.shiftAmount = shiftAmount;
	}

	public ComplexRegister(int regIndex, ShiftDirection shiftDirection, int shiftAmount, boolean writeback) {
		this.regIndex = regIndex;
		this.shiftDirection = shiftDirection;
		this.shiftAmount = shiftAmount;
		this.writeback = writeback;
	}
	
	public long getValueAtAddress(SystemState systemState, long addr) {
		switch(size) {
		case 2:
			return systemState.memoryManager.loadHalfWord(addr);
		case 4:
			return systemState.memoryManager.loadWord(addr);
		case 8:
			return systemState.memoryManager.loadDoubleWord(addr);
		default:
			return systemState.memoryManager.loadByte(addr);
			//TODO: actually load strings
		}
	}

	@Override
	public long getValue(SystemState systemState) {

		long out = systemState.getValueAtRegister(regIndex);
		switch (shiftDirection) {
			case NONE:
				break;
			case LEFT:
				out = out << shiftAmount;
				break;
			case UNSIGNED_RIGHT: //pads 0s to left side of long
				out = out >>> shiftAmount;
				break;
			case SIGNED_RIGHT: //pads 0s or 1s to left side of long based on most significant bit
				out = out >> shiftAmount;
				break;
			default:
				throw new RuntimeException("Unexpected shift direction. Should NOT happen");
		}

		if (writeback) {
			//TODO: handle pre/post-indexing
			systemState.setRegisterValue(regIndex, out);
		}

		return out;
	}

	public int getIndex() {
		return regIndex;
	}
}
