package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests ArithmeticShiftRight instruction
 *
 * @author Habibullah Aslam
 */
class ArithmeticShiftRightTest extends InstructionTestSetup {
	
	@Test
	void testASRWPosArg() {
		sInst.setRegisterValue(0, 8);
		ArithmeticShiftRight asr = new ArithmeticShiftRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(asr);
		asr.execute(dInst);
		assertEquals(2, sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testASRWNegArg() {
		sInst.setRegisterValue(0, -1);
		ArithmeticShiftRight asr = new ArithmeticShiftRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(asr);
		asr.execute(dInst);
		assertEquals(-1, sInst.getValueAtRegister(REGISTER_ZERO));
	}

	@Test
	void testDiffSrcAndDest() {
		sInst.setRegisterValue(0, 4);
		ArithmeticShiftRight asr = new ArithmeticShiftRight(REGISTER_ONE, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(asr);
		asr.execute(dInst);
		assertEquals(4, sInst.getValueAtRegister(REGISTER_ZERO));
		assertEquals(1, sInst.getValueAtRegister(REGISTER_ONE));
	}

	@Test
	void testCreate() {
		ArithmeticShiftRight asr = ArithmeticShiftRight
				.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Immediate(2) });
		assertEquals(new ArithmeticShiftRight(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2)), asr);
	}

	@Test
	void testCreateWNull() {
		ArithmeticShiftRight asr = ArithmeticShiftRight.create(null);
		assertEquals(null, asr);
	}
}
