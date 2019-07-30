package conditions;

import driver.SystemState;

/**
 * Unsigned higher or same (or carry set)
 *
 * @author Mark
 */
public class CarrySet implements Condition {

	@Override
	public String code() {
		return "CS";
	}

	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getC();
	}

}
