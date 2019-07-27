package conditions;

import driver.SystemState;

/**
 * No signed overflow. The mnemonic stands for "V clear"
 *
 * @author Mark
 */
public class NoSignedOverflow implements Condition {
	
	@Override
	public String code() {
		return "VC";
	}
	
	@Override
	public boolean isTrue(SystemState systemState) {
		return !systemState.getV();
	}
	
}
