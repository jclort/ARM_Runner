package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.ShiftDirection;

class MultiplyTest extends InstructionTestSetup {

	@Test
	void testRegisterMultiplying() {
		sInst.setRegisterValue(0, 2);
		sInst.setRegisterValue(1, 6);
		Multiply m = new Multiply(REGISTER_ZERO, REGISTER_ZERO, new Register(1));
		setupDriverAndSetStartNode(m);
		m.execute(dInst);
		assertEquals(12, sInst.getValueAtRegister(0));
	}

	@Test
	void testImmediateMultiplying() {
		sInst.setRegisterValue(0, 6);
		Multiply m = new Multiply(REGISTER_ZERO, REGISTER_ZERO, new Immediate(3));
		setupDriverAndSetStartNode(m);
		m.execute(dInst);
		assertEquals(18, sInst.getValueAtRegister(0));
	}

	@Test
	void testShiftedMultiplying() {
		sInst.setRegisterValue(0, 6);
		sInst.setRegisterValue(1, 2);
		Multiply m = new Multiply(0, 0, new ComplexRegister(1, ShiftDirection.LEFT, 1));
		setupDriverAndSetStartNode(m);
		m.execute(dInst);
		assertEquals(24, sInst.getValueAtRegister(0));
	}

	@Test
	void testCreate() {
		Multiply m = Multiply.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Register(5) });
		assertEquals(new Multiply(REGISTER_ZERO, REGISTER_ZERO, new Register(5)), m);
	}
	
	@Test
	void testCreateWNull() {
		Multiply m = Multiply.create(null);
		assertEquals(null, m);
	}

}
