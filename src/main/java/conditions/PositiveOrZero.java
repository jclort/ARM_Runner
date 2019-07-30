package conditions;

import driver.SystemState;

/**
 * Positive or zero. The mnemonic stands for "plus".
 *
 * @author Mark
 */
public class PositiveOrZero implements Condition {
	
	@Override
	public String code() {
		return "PL";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return !systemState.getN();
	}
	
}
