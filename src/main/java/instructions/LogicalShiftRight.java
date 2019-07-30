package instructions;

import driver.Driver;

/**
 * LSR provides the unsigned value of a register divided by a variable power of two. It inserts zeros into the vacated bit positions.
 *
 * @author Mark
 */
public class LogicalShiftRight extends ArithmeticInstruction {
	
	public LogicalShiftRight(int destination, int source1, DataSource source2) {
		this.destination = new Register(destination);
		this.source1 = new Register(source1);
		this.source2 = source2;
	}
	
	@Override
	public void execute(Driver driver) {
		int destinationIndex = destination.getIndex();
		long value1 = source1.getValue(driver.systemState);
		long value2 = source2.getValue(driver.systemState);
		
		driver.systemState.setRegisterValue(destinationIndex, value1 >>> value2);
	}
	
	public static LogicalShiftRight create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new LogicalShiftRight((Integer) args[0], (Integer) args[1], (DataSource) args[2]);
	}
}
