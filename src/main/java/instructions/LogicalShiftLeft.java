package instructions;

import driver.Driver;

/**
 * LSL provides the value of a register multiplied by a power of two. It inserts zeros into the vacated bit positions.
 *
 * @author Habibullah Aslam
 */
public class LogicalShiftLeft extends ArithmeticInstruction {

	public LogicalShiftLeft(int destination, int source1, DataSource source2) {
		this.destination = new Register(destination);
		this.source1 = new Register(source1);
		this.source2 = source2;
	}

	@Override
	public void execute(Driver driver) {
		int destinationIndex = destination.getIndex();
		long value1 = source1.getValue(driver.systemState);
		long value2 = source2.getValue(driver.systemState);

		driver.systemState.setRegisterValue(destinationIndex, value1 << value2);
	}

	public static LogicalShiftLeft create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new LogicalShiftLeft((Integer) args[0], (Integer) args[1], (DataSource) args[2]);
	}
}
