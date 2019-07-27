package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Not equal
 *
 * @author Habibullah Aslam
 */
public class NotEqualTest extends ConditionTestSetup {

	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertTrue(new NotEqual().isTrue(sInst));
	}
	
	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertFalse(new NotEqual().isTrue(sInst));
	}
	
	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertTrue(new NotEqual().isTrue(sInst));
	}

	@Test
	void testWithNegative1stArg() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertTrue(new NotEqual().isTrue(sInst));
	}

	@Test
	void testWithNegative2ndArg() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertTrue(new NotEqual().isTrue(sInst));
	}
	
	@Test
	public void testCode() throws Exception {
		assertEquals("NE", new NotEqual().code());
	}
}