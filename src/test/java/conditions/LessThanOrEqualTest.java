package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Signed less than or equal
 *
 * @author Habibullah Aslam
 */
public class LessThanOrEqualTest extends ConditionTestSetup {
	
	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertTrue(new LessThanOrEqual().isTrue(sInst));
	}

	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertTrue(new LessThanOrEqual().isTrue(sInst));
	}

	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertFalse(new LessThanOrEqual().isTrue(sInst));
	}

	@Test
	void testSignedLowerButNotUnsignedLower() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertTrue(new LessThanOrEqual().isTrue(sInst));
	}
	
	@Test
	void testUnsignedLowerButNotSignedLower() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertFalse(new LessThanOrEqual().isTrue(sInst));
	}
	
	@Test
	public void testCode() throws Exception {
		assertEquals("LE", new LessThanOrEqual().code());
	}
}
