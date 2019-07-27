package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.ShiftDirection;

/**
 * Doesn't test writeback yet
 *
 * @author Habibullah Aslam
 */
class ComplexRegisterTest extends InstructionTestSetup {
	
	@Test
	void testGetValueWithBasicConstructor() {
		sInst.setRegisterValue(0, 1);
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO);
		assertEquals(1, cr.getValue(sInst));
	}
	
	@Test
	void testGetValueWithNoShift() {
		sInst.setRegisterValue(0, 1);
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO, ShiftDirection.NONE, 0);
		assertEquals(1, cr.getValue(sInst));
	}

	@Test
	void testGetValueWithShiftLeft() {
		sInst.setRegisterValue(0, 1);
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO, ShiftDirection.LEFT, 1);
		assertEquals(2, cr.getValue(sInst));
	}

	@Test
	void testGetValueWithPosNumberAndShiftRight() {
		sInst.setRegisterValue(0, 4);
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO, ShiftDirection.UNSIGNED_RIGHT, 1);
		assertEquals(2, cr.getValue(sInst));
	}

	@Test
	void testGetValueWithNegNumberAndShiftRightUnsigned() {
		sInst.setRegisterValue(0, -4); //1111111111111100 in 32-bit
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO, ShiftDirection.UNSIGNED_RIGHT, 62);
		assertEquals(3, cr.getValue(sInst));
	}
	
	@Test
	void testGetValueWithNegNumberAndShiftRightSigned() {
		sInst.setRegisterValue(0, -4); //1111111111111100 in 32-bit
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO, ShiftDirection.SIGNED_RIGHT, 1);
		assertEquals(-2, cr.getValue(sInst));
	}

	@Test
	void testGetValueWithWriteback() {
		sInst.setRegisterValue(0, 1); //1111111111111100 in 32-bit
		ComplexRegister cr = new ComplexRegister(REGISTER_ZERO, ShiftDirection.LEFT, 1, true);
		assertEquals(2, cr.getValue(sInst));
		assertEquals(2, sInst.getValueAtRegister(REGISTER_ZERO));
	}
	
	@Test
	public void testGetIndex() throws Exception {
		ComplexRegister cr = new ComplexRegister(REGISTER_ONE, ShiftDirection.SIGNED_RIGHT, 1);
		assertEquals(1, cr.getIndex());
	}
}
