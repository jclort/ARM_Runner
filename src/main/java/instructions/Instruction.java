package instructions;

import driver.Driver;

/**
 * An Instruction is nothing more than a function that
 * implicitly operates on the SystemState.
 * @author Mark
 *
 */
public abstract class Instruction {
	public abstract void execute(Driver driver);
}
