package conditions;

import driver.SystemState;

/**
 * Unsigned lower or same
 *
 * @author Mark
 */
public class LowerOrSame implements Condition {
	
	@Override
	public String code() {
		return "LS";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return !systemState.getC() || systemState.getZ();
	}
	
}
