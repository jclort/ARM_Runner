package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests Move instruction
 *
 * @author Habibullah Aslam
 */
class MoveTest extends InstructionTestSetup {

	@Test
	void testExecute() {
		sInst.setRegisterValue(0, 1);
		Move asr = new Move(REGISTER_ZERO, REGISTER_ZERO);
		setupDriverAndSetStartNode(asr);
		asr.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 1);
		Move asr = new Move(REGISTER_ONE, REGISTER_ZERO);
		setupDriverAndSetStartNode(asr);
		asr.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ONE));
	}
	
	@Test
	void testCreate() {
		Move asr = Move.create(new Object[] { REGISTER_ONE, REGISTER_ZERO });
		assertEquals(new Move(REGISTER_ONE, REGISTER_ZERO), asr);
	}
	
	@Test
	void testCreateWNull() {
		Move asr = Move.create(null);
		assertEquals(null, asr);
	}
}
