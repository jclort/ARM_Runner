package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NotTest extends InstructionTestSetup {
	
	@Test
	void testNotWithPosVal() {
		sInst.setRegisterValue(0, 0);
		Not n = new Not(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testNotWithNegValue() {
		sInst.setRegisterValue(0, -2);
		Not n = new Not(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 0);
		Not n = new Not(REGISTER_ONE, REGISTER_ZERO);
		setupDriverAndSetStartNode(n);
		n.execute(dInst);
		assertEquals(0, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ONE));
	}
	
	@Test
	void testCreate() {
		Not n = Not.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO });
		assertEquals(new Not(REGISTER_ZERO, REGISTER_ZERO), n);
	}
	
	@Test
	void testCreateWNull() {
		Not n = Not.create(null);
		assertEquals(null, n);
	}
}
