package instructions;

import driver.Driver;
import driver.SystemState;

public class RotateRight extends ArithmeticInstruction {

	public RotateRight(int destination, int source1, DataSource source2)
	{
		this.destination = new Register(destination);
		this.source1 = new Register(source1);
		this.source2 = source2;
	}

	@Override
	public void execute(Driver driver)
	{
		int destinationIndex = destination.getIndex();
		long value1 = source1.getValue(driver.systemState);
		long value2 = source2.getValue(driver.systemState);

		driver.systemState.setRegisterValue(destinationIndex, Long.rotateRight(value1, (int)value2));
	}

	public static RotateRight create(Object[] args) {
		if(args == null) {
			return null;
		}
		return new RotateRight((Integer)args[0], (Integer)args[1], (DataSource)args[2]);
	}
}
