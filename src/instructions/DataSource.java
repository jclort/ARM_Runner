package instructions;

import driver.SystemState;

/**
 * A DataSource is a generalized container for a specific
 * method of providing values to the ARM code. Values are
 * typically immediate values (constants) or are read from
 * registers.
 * @author Mark
 *
 */
public interface DataSource {
	public long getValue(SystemState systemState);
}
