package conditions;

import driver.SystemState;

/**
 * Signed overflow. The mnemonic stands for "V set"
 *
 * @author Mark
 */
public class SignedOverflow implements Condition {
	
	@Override
	public String code() {
		return "VS";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return systemState.getV();
	}
	
}
