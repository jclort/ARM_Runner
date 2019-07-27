package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests RotateRight instruction
 *
 * @author Habibullah Aslam
 */
class RotateRightTest extends InstructionTestSetup {

	@Test
	void testWPosArgWhichShouldBecomeNeg() {
		sInst.setRegisterValue(0, 1);
		RotateRight rr = new RotateRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(1));
		setupDriverAndSetStartNode(rr);
		rr.execute(dInst);
		assertEquals(-Math.pow(2, 63), sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testWPosArgWhichShouldStayPos() {
		sInst.setRegisterValue(0, 1);
		RotateRight rr = new RotateRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(rr);
		rr.execute(dInst);
		assertEquals(Math.pow(2, 62), sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testWNegArgWhichShouldBecomePos() {
		sInst.setRegisterValue(0, (long) -Math.pow(2, 63));
		RotateRight rr = new RotateRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(1));
		setupDriverAndSetStartNode(rr);
		rr.execute(dInst);
		assertEquals(Math.pow(2, 62), sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testWNegArgWhichShouldStayNeg() {
		sInst.setRegisterValue(0, -1);
		RotateRight rr = new RotateRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(rr);
		rr.execute(dInst);
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 4);
		RotateRight rr = new RotateRight(REGISTER_ONE, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(rr);
		rr.execute(dInst);
		assertEquals(4, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ONE));
	}
	
	@Test
	void testCreate() {
		RotateRight rr = RotateRight.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Immediate(2) });
		assertEquals(new RotateRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2)), rr);
	}
	
	@Test
	void testCreateWNull() {
		RotateRight rr = RotateRight.create(null);
		assertEquals(null, rr);
	}
}
