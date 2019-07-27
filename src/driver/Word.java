package driver;

import main.Main;

public class Word {
	private long address;
	private int value = 0;
	private boolean touched = false;
	
	public Word(long address) {
		this.address = address;
	}
	
	public void step() {
		touched = false;
	}
	
	public void setValue(int newValue) {
		touched = true;
		value = newValue;
	}
	
	public int getValue() {
		return value;
	}
	
	public long getAddress() {
		return address;
	}
	
	public String toString() {
		if(Main.decimalDisplay.get()) {
			return String.format("0x%016X: %d", address, value);
		}
		
		if(Main.hexDisplay.get()) {
			return String.format("0x%016X: 0x%08X", address, value);
		}
		return String.format("0x%016X: ", address);
	}
}
