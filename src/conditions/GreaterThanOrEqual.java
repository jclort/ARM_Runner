package conditions;

import driver.SystemState;

/**
 * Signed greater than or equal
 *
 * @author Mark
 */
public class GreaterThanOrEqual implements Condition {

	@Override
	public String code() {
		return "GE";
	}

	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getN() == systemState.getV();
	}

}
