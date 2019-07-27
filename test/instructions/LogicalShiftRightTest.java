package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests LogicalShiftRight instruction
 *
 * @author Habibullah Aslam
 */
class LogicalShiftRightTest extends InstructionTestSetup {

	@Test
	void testLSRWPosArg() {
		sInst.setRegisterValue(0, 8);
		LogicalShiftRight lsr = new LogicalShiftRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(lsr);
		lsr.execute(dInst);
		assertEquals(2, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testLSRWNegArg() {
		sInst.setRegisterValue(0, -1);
		LogicalShiftRight lsr = new LogicalShiftRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(1));
		setupDriverAndSetStartNode(lsr);
		lsr.execute(dInst);
		assertEquals((long) (Math.pow(2, 63) - 1), sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 4);
		LogicalShiftRight lsr = new LogicalShiftRight(REGISTER_ONE, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(lsr);
		lsr.execute(dInst);
		assertEquals(4, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ONE));
	}
	
	@Test
	void testCreate() {
		LogicalShiftRight lsr = LogicalShiftRight
				.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Immediate(2) });
		assertEquals(new LogicalShiftRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2)), lsr);
	}
	
	@Test
	void testCreateWNull() {
		LogicalShiftRight lsr = LogicalShiftRight.create(null);
		assertEquals(null, lsr);
	}
}
