package conditions;

import driver.SystemState;

/**
 * Signed less than or equal
 *
 * @author Mark
 */
public class LessThanOrEqual implements Condition {
	
	@Override
	public String code() {
		return "LE";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getZ() || (systemState.getN() != systemState.getV());
	}
	
}
