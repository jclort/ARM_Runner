package conditions;

import driver.SystemState;

/**
 * Always executed
 *
 * @author Mark
 */
public class Always implements Condition {

	@Override
	public String code() {
		return "AL";
	}

	@Override
	public boolean isTrue(SystemState systemState) {
		return true;
	}

}
