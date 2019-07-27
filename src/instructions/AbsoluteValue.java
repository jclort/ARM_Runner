package instructions;

import driver.Driver;
import driver.SystemState;

public class AbsoluteValue extends BitwiseInstruction {

	public AbsoluteValue(int destination, int source)
	{
		this.destination = new Register(destination);
		this.source = new Register(source);
	}

	@Override
	public void execute(Driver driver)
	{
		int destinationIndex = destination.getIndex();
		long value = source.getValue(driver.systemState);

		driver.systemState.setRegisterValue(destinationIndex, Math.abs(value));
	}

	public static AbsoluteValue create(Object[] args) {
		if(args == null) {
			return null;
		}
		return new AbsoluteValue((Integer)args[0], (Integer)args[1]);
	}
}
