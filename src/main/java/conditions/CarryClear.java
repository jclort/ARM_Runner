package conditions;

import driver.SystemState;

/**
 * Unsigned lower (or carry clear)
 *
 * @author Mark
 */
public class CarryClear implements Condition {
	
	/**
	 * We stick with CC tho LO technically counts. Parser will mush LO into CC through isValidCondition
	 */
	@Override
	public String code() {
		return "CC";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return !systemState.getC();
	}
	
}
