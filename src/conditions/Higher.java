package conditions;

import driver.SystemState;

/**
 * Unsigned higher
 *
 * @author Mark
 */
public class Higher implements Condition {
	
	@Override
	public String code() {
		return "HI";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getC() && !systemState.getZ();
	}
	
}
