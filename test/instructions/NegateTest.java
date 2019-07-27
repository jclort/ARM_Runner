package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests Negate instruction
 *
 * @author Habibullah Aslam
 */
class NegateTest extends InstructionTestSetup {
	
	@Test
	void testWPosArg() {
		sInst.setRegisterValue(0, 1);
		Negate n = new Negate(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testWNegArg() {
		sInst.setRegisterValue(0, -1);
		Negate n = new Negate(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testWZero() {
		sInst.setRegisterValue(0, 0);
		Negate n = new Negate(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(0, sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 1);
		Negate n = new Negate(REGISTER_ONE, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ONE));
	}

	@Test
	void testCreate() {
		Negate n = Negate.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO });
		assertEquals(new Negate(REGISTER_ZERO, REGISTER_ZERO), n);
	}

	@Test
	void testCreateWNull() {
		Negate n = Negate.create(null);
		assertEquals(null, n);
	}
}
