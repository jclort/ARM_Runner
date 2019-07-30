package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests LogicalShiftLeft instruction
 *
 * @author Habibullah Aslam
 */
class LogicalShiftLeftTest extends InstructionTestSetup {

	@Test
	void testLSLWPosArg() {
		sInst.setRegisterValue(0, 1);
		LogicalShiftLeft lsl = new LogicalShiftLeft(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(lsl);
		lsl.execute(dInst);
		assertEquals(4, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testLSLWNegArg() {
		sInst.setRegisterValue(0, -1);
		LogicalShiftLeft lsl = new LogicalShiftLeft(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(lsl);
		lsl.execute(dInst);
		assertEquals(-4, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 1);
		LogicalShiftLeft lsl = new LogicalShiftLeft(REGISTER_ONE, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(lsl);
		lsl.execute(dInst);
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(4, sInst.getValueAtRegister(REGISTER_ONE));
	}
	
	@Test
	void testCreate() {
		LogicalShiftLeft lsl = LogicalShiftLeft.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Immediate(2) });
		assertEquals(new LogicalShiftLeft(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2)), lsl);
	}
	
	@Test
	void testCreateWNull() {
		LogicalShiftLeft lsl = LogicalShiftLeft.create(null);
		assertEquals(null, lsl);
	}
}
