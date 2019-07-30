package instructions;

import driver.Driver;

public class Store extends Instruction {
	
	private Register source;
	private Register address;
	private DataSource offset;
	
	public Store(int sourceReg, int addressReg, DataSource offset) {
		source = new Register(sourceReg);
		address = new Register(addressReg);
		this.offset = offset;
	}

	@Override
	public void execute(Driver driver) {
		int sourceIndex = source.getIndex();
		long memAddress = address.getValue(driver.systemState);
		long addressOffset = offset.getValue(driver.systemState);

		driver.systemState.memoryManager.storeDoubleWord(memAddress + addressOffset,
				driver.systemState.getValueAtRegister(sourceIndex));
	}
	
}
