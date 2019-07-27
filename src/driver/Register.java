package driver;

import main.Main;

public class Register {
	
	private int index;
	private long value = 0;
	
	public Register(int index, long value) {
		this.index = index;
		this.value = value;
	}
	
	public Register(int index) {
		this.index = index;
	}
	
	public void setValue(long val) {
		value = val;
	}
	
	public long getValue() {
		return value;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		String output = "";
		
		output += String.format("%02d", index);
		if (index == 29) {
			output += " (FP)";
		}
		else if (index == 30) {
			output += " (LR)";
		}
		else if (index == 31) {
			output += " (SP)";
		}
		else if (index == -1) {
			output = "   (PC)";
		}
		else {
			output += "     ";
		}
		
		if (Main.decimalDisplay.get()) {
			output += String.format(": %d", value);
		}
		else if (Main.hexDisplay.get()) {
			output += String.format(": 0x%016X", value);
		}
		return output;
	}
}
