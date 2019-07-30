package instructions;

import driver.Driver;

public class Load extends Instruction {

	private Register destination;
	private Register address;
	private DataSource offset;
	
	public Load(int destReg, int addressReg, DataSource offset) {
		destination = new Register(destReg);
		address = new Register(addressReg);
		this.offset = offset;
	}
	
	@Override
	public void execute(Driver driver) {
		int destIndex = destination.getIndex();
		long memAddress = address.getValue(driver.systemState);
		long addressOffset = offset.getValue(driver.systemState);
		
		driver.systemState.setRegisterValue(destIndex,
				driver.systemState.memoryManager.loadDoubleWord(memAddress + addressOffset));
	}
}
