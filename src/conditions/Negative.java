package conditions;

import driver.SystemState;

/**
 * Negative. The mnemonic stands for "minus"
 * (Obviously we look at result signed. Ignore overflow; look only at 31st bit)
 *
 * @author Mark
 */
public class Negative implements Condition {
	
	@Override
	public String code() {
		return "MI";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getN();
	}
	
}
