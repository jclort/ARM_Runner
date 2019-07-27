package instructions;

import driver.Driver;

/**
 * ASR provides the signed value of the contents of a register divided by a power of two. It copies the sign bit into vacated bit positions on the left.
 *
 * @author Mark
 */
public class ArithmeticShiftRight extends ArithmeticInstruction {

	public ArithmeticShiftRight(int destination, int source1, DataSource source2) {
		this.destination = new Register(destination);
		this.source1 = new Register(source1);
		this.source2 = source2;
	}

	@Override
	public void execute(Driver driver) {
		int destinationIndex = destination.getIndex();
		long value1 = source1.getValue(driver.systemState);
		long value2 = source2.getValue(driver.systemState);

		driver.systemState.setRegisterValue(destinationIndex, value1 >> value2);
	}

	public static ArithmeticShiftRight create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new ArithmeticShiftRight((Integer) args[0], (Integer) args[1], (DataSource) args[2]);
	}
}
