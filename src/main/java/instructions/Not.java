package instructions;

import driver.Driver;

public class Not extends BitwiseInstruction {
	
	Not(int destination, int source) {
		this.destination = new Register(destination);
		this.source = new Register(source);
	}

	@Override
	public void execute(Driver driver) {
		int destinationIndex = destination.getIndex();
		long value1 = ~(source.getValue(driver.systemState));
		
		driver.systemState.setRegisterValue(destinationIndex, value1);

	}

	public static Not create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new Not((Integer) args[0], (Integer) args[1]);
	}
	
}
