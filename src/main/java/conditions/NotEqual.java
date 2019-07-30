package conditions;

import driver.SystemState;

/**
 * Not equal
 *
 * @author Mark
 */
public class NotEqual implements Condition {
	
	@Override
	public String code() {
		return "NE";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return !systemState.getZ();
	}
	
}
