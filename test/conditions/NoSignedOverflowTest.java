package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * No signed overflow. The mnemonic stands for "V clear"
 *
 * @author Habibullah Aslam
 */
public class NoSignedOverflowTest extends ConditionTestSetup {

	@Test
	void testWithNegCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 2);
		assertTrue(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testWithZeroCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 1);
		assertTrue(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testWithPosCmpResult() {
		setReg0And1ValsAndExecuteCompare(1, 0);
		assertTrue(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testSubPosFromNegWPosResult() {
		setReg0And1ValsAndExecuteCompare((long) (-Math.pow(2, 63)), 1);
		assertFalse(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testSubPosFromNegWNegResult() {
		setReg0And1ValsAndExecuteCompare(-1, 0);
		assertTrue(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testSubNegFromPosWNegResult() {
		setReg0And1ValsAndExecuteCompare((long) (Math.pow(2, 63) - 1), -1);
		assertFalse(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testSubNegFromPosWPosResult() {
		setReg0And1ValsAndExecuteCompare(0, -1);
		assertTrue(new NoSignedOverflow().isTrue(sInst));
	}
	
	@Test
	void testWithMin1stArgAndMax2ndArg() {
		setReg0And1ValsAndExecuteCompare((long) (-Math.pow(2, 63)), (long) (Math.pow(2, 63) - 1)); //result is 1
		assertFalse(new NoSignedOverflow().isTrue(sInst));
	}

	@Test
	public void testCode() throws Exception {
		assertEquals("VC", new NoSignedOverflow().code());
	}
}