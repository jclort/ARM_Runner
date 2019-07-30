package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.ShiftDirection;

class SubtractTest extends InstructionTestSetup {
	
	@Test
	void testRegisterSubtracting() {
		sInst.setRegisterValue(5, 2);
		Subtract s = new Subtract(REGISTER_ZERO, REGISTER_ZERO, new Register(5));
		setupDriverAndSetStartNode(s);
		s.execute(dInst);
		assertEquals(-2, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testImmediateSubtracting() {
		Subtract s = new Subtract(REGISTER_ZERO, REGISTER_ZERO, new Immediate(5));
		setupDriverAndSetStartNode(s);
		s.execute(dInst);
		assertEquals(-5, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testShiftedSubtracting() {
		sInst.setRegisterValue(REGISTER_ZERO, 0);
		sInst.setRegisterValue(1, 1);
		Subtract s = new Subtract(0, 0, new ComplexRegister(1, ShiftDirection.LEFT, 2));
		setupDriverAndSetStartNode(s);
		s.execute(dInst);
		assertEquals(-4, sInst.getValueAtRegister(0));
	}

	@Test
	void testCreate() {
		Subtract s = Subtract.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Register(5) });
		assertEquals(new Subtract(REGISTER_ZERO, REGISTER_ZERO, new Register(5)), s);
	}
	
	@Test
	void testCreateWNull() {
		Subtract s = Subtract.create(null);
		assertEquals(null, s);
	}
	
}
