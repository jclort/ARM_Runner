package conditions;

import driver.SystemState;

/**
 * Signed greater than
 *
 * @author Mark
 */
public class GreaterThan implements Condition {
	
	@Override
	public String code() {
		return "GT";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return !systemState.getZ() && (systemState.getN() == systemState.getV());
	}
	
}
