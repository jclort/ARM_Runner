package conditions;

import driver.SystemState;

/**
 * Signed less than
 *
 * @author Mark
 */
public class LessThan implements Condition {
	
	@Override
	public String code() {
		return "LT";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getN() != systemState.getV();
	}
	
}
