package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unsigned higher
 *
 * @author Habibullah Aslam
 */
public class HigherTest extends ConditionTestSetup {

	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertFalse(new Higher().isTrue(sInst));
	}
	
	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertFalse(new Higher().isTrue(sInst));
	}
	
	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertTrue(new Higher().isTrue(sInst));
	}
	
	@Test
	void testUnsignedHigherButNotSignedHigher() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertTrue(new Higher().isTrue(sInst));
	}
	
	@Test
	void testSignedHigherButNotUnsignedHigher() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertFalse(new Higher().isTrue(sInst));
	}

	@Test
	public void testCode() throws Exception {
		assertEquals("HI", new Higher().code());
	}
}