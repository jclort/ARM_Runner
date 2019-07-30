package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests AbsoluteValue instruction
 *
 * @author Habibullah Aslam
 */
class AbsoluteValueTest extends InstructionTestSetup {

	@Test
	void testAbsValWPosArg() {
		sInst.setRegisterValue(0, 1);
		AbsoluteValue av = new AbsoluteValue(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(av);
		av.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testAbsValWNegArg() {
		sInst.setRegisterValue(0, -1);
		AbsoluteValue av = new AbsoluteValue(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(av);
		av.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(1, -1);
		AbsoluteValue av = new AbsoluteValue(REGISTER_ZERO, REGISTER_ONE);
		setupDriverAndSetStartNode(av);
		av.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ONE));
	}
	
	@Test
	void testCreate() {
		AbsoluteValue av = AbsoluteValue.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO });
		assertEquals(new AbsoluteValue(REGISTER_ZERO, REGISTER_ZERO), av);
	}
	
	@Test
	void testCreateWNull() {
		AbsoluteValue av = AbsoluteValue.create(null);
		assertEquals(null, av);
	}
}
