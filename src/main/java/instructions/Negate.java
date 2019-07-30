package instructions;

import driver.Driver;

public class Negate extends BitwiseInstruction{

	Negate(int destination, int source) {
		
		this.destination = new Register(destination);
		this.source = new Register(source);
	}
	
	@Override
	public void execute(Driver driver) {
		int destinationIndex = destination.getIndex();
		long value1 = ~(source.getValue(driver.systemState)) + 1;

		driver.systemState.setRegisterValue(destinationIndex, value1);
		
	}
	
	public static Negate create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new Negate((Integer)args[0], (Integer)args[1]);
	}

}
