package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Positive or zero. The mnemonic stands for "plus"
 *
 * @author Habibullah Aslam
 */
public class PositiveOrZeroTest extends ConditionTestSetup {
	
	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertFalse(new PositiveOrZero().isTrue(sInst));
	}

	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertTrue(new PositiveOrZero().isTrue(sInst));
	}

	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertTrue(new PositiveOrZero().isTrue(sInst));
	}
	
	@Test
	void testWithNeg1stArg() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertFalse(new PositiveOrZero().isTrue(sInst));
	}
	
	@Test
	void testWithNeg2ndArg() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertTrue(new PositiveOrZero().isTrue(sInst));
	}
	
	@Test
	void testWithMin1stArgAndMax2ndArg() {
		setReg0And1ValsAndExecuteCompare((long) (-Math.pow(2, 63)), (long) (Math.pow(2, 63) - 1)); //result is 1
		assertTrue(new PositiveOrZero().isTrue(sInst));
	}
	
	@Test
	public void testCode() throws Exception {
		assertEquals("PL", new PositiveOrZero().code());
	}
}