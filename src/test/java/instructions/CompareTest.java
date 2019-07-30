package instructions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class CompareTest extends InstructionTestSetup {
	
	private void setRegs0and1To5And10() {
		sInst.setRegisterValue(0, 5); //Note: can't directly sub negative immediates
		sInst.setRegisterValue(1, 10);
	}

	@Test
	void testZandCSetForEqual() {
		setRegs0and1To5And10();
		Compare c = new Compare(REGISTER_ZERO, new Register(0));
		setupDriverAndSetStartNode(c);
		c.execute(dInst);
		assertFalse(sInst.getN());
		assertTrue(sInst.getZ());
		assertTrue(sInst.getC());
		assertFalse(sInst.getV());
	}

	@Test
	void testNSetForNegResult() {
		setRegs0and1To5And10();
		Compare c = new Compare(REGISTER_ZERO, new Register(1));
		setupDriverAndSetStartNode(c);
		c.execute(dInst);
		assertTrue(sInst.getN());
		assertFalse(sInst.getZ());
		assertFalse(sInst.getC());
		assertFalse(sInst.getV());
	}
	
	@Test
	void testCSetForSubWPosResult() {
		setRegs0and1To5And10();
		Compare c = new Compare(1, new Register(REGISTER_ZERO));
		setupDriverAndSetStartNode(c);
		c.execute(dInst);
		assertFalse(sInst.getN());
		assertFalse(sInst.getZ());
		assertTrue(sInst.getC());
		assertFalse(sInst.getV());
	}

	@Test
	void testVSetForNegMinusPosWPosResult() {
		sInst.setRegisterValue(0, (long) -Math.pow(2, 63));
		sInst.setRegisterValue(1, 1);
		Compare c = new Compare(REGISTER_ZERO, new Register(1));
		setupDriverAndSetStartNode(c);
		c.execute(dInst);
		assertFalse(sInst.getN());
		assertFalse(sInst.getZ());
		assertTrue(sInst.getC());
		assertTrue(sInst.getV());
	}

	@Test
	void testVSetForPosMinusNegWNegResult() {
		sInst.setRegisterValue(0, (long) (Math.pow(2, 63) - 1));
		sInst.setRegisterValue(1, -1);
		Compare c = new Compare(REGISTER_ZERO, new Register(1));
		setupDriverAndSetStartNode(c);
		c.execute(dInst);
		assertTrue(sInst.getN());
		assertFalse(sInst.getZ());
		assertFalse(sInst.getC());
		assertTrue(sInst.getV());
	}
}
