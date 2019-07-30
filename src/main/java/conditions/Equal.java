package conditions;

import driver.SystemState;

/**
 * Equal
 *
 * @author Mark
 */
public class Equal implements Condition {
	
	@Override
	public String code() {
		return "EQ";
	}

	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getZ();
	}
	
}
