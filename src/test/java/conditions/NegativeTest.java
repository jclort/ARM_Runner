package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Negative. The mnemonic stands for "minus"
 *
 * @author Habibullah Aslam
 */
public class NegativeTest extends ConditionTestSetup {

	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertTrue(new Negative().isTrue(sInst));
	}
	
	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertFalse(new Negative().isTrue(sInst));
	}
	
	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertFalse(new Negative().isTrue(sInst));
	}

	@Test
	void testWithNegative1stArg() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertTrue(new Negative().isTrue(sInst));
	}

	@Test
	void testWithNegative2ndArg() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertFalse(new Negative().isTrue(sInst));
	}

	@Test
	void testWithMin1stArgAndMax2ndArg() {
		setReg0And1ValsAndExecuteCompare((long) (-Math.pow(2, 63)), (long) (Math.pow(2, 63) - 1)); //result is 1
		assertFalse(new Negative().isTrue(sInst));
	}

	@Test
	public void testCode() throws Exception {
		assertEquals("MI", new Negative().code());
	}
}