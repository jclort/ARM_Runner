package conditions;

import driver.SystemState;

/**
 * A Condition is a container intended to provide first-class-object
 * status to the logic of ARM conditions. This way, it is easy for
 * instances of the logic to be contained elsewhere.
 * @author Mark
 *
 */
public interface Condition {
	
	public String code();
	
	public boolean isTrue(SystemState systemState);
}
