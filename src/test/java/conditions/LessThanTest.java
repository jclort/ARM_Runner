package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Signed lower
 *
 * @author Habibullah Aslam
 */
public class LessThanTest extends ConditionTestSetup {

	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertTrue(new LessThan().isTrue(sInst));
	}
	
	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertFalse(new LessThan().isTrue(sInst));
	}
	
	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertFalse(new LessThan().isTrue(sInst));
	}
	
	@Test
	void testSignedLowerButNotUnsignedLower() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertTrue(new LessThan().isTrue(sInst));
	}

	@Test
	void testUnsignedLowerButNotSignedLower() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertFalse(new LessThan().isTrue(sInst));
	}
	
	@Test
	public void testCode() throws Exception {
		assertEquals("LT", new LessThan().code());
	}
}
