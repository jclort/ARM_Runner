package instructions;

import driver.Driver;

public class Return extends Instruction {

	public Return() {
		
	}
	
	//TODO: allow user to return to something other than LR

	@Override
	public void execute(Driver driver) {
		driver.systemState.PC.setValue(driver.systemState.getValueAtLR() - 4);
	}
}
