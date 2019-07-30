package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
* Unsigned lower (or carry clear).
*/
public class CarryClearTest extends ConditionTestSetup {
	
	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertTrue(new CarryClear().isTrue(sInst));
	}

	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertFalse(new CarryClear().isTrue(sInst));
	}

	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertFalse(new CarryClear().isTrue(sInst));
	}

	@Test
	void testSignedLowerButNotUnsignedLower() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertFalse(new CarryClear().isTrue(sInst));
	}
	
	@Test
	void testUnsignedLowerButNotSignedLower() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertTrue(new CarryClear().isTrue(sInst));
	}
	
	@Test
	public void testCode() throws Exception {
		assertEquals("CC", new CarryClear().code());
	}
}
