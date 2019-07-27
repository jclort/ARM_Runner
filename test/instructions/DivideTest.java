package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.ShiftDirection;

class DivideTest extends InstructionTestSetup {
	
	@Test
	void testRegisterDividing() {
		sInst.setRegisterValue(0, 2);
		sInst.setRegisterValue(1, 6);
		Divide d = new Divide(REGISTER_ZERO, REGISTER_ZERO, new Register(1));
		setupDriverAndSetStartNode(d);
		d.execute(dInst);
		assertEquals(0, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testImmediateDividing() {
		sInst.setRegisterValue(0, 6);
		Divide d = new Divide(REGISTER_ZERO, REGISTER_ZERO, new Immediate(2));
		setupDriverAndSetStartNode(d);
		d.execute(dInst);
		assertEquals(3, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testShiftedDividing() {
		sInst.setRegisterValue(0, 8);
		sInst.setRegisterValue(1, 4);
		Divide a = new Divide(0, 0, new ComplexRegister(1, ShiftDirection.SIGNED_RIGHT, 1));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(4, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testCreate() {
		Divide a = Divide.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Register(5) });
		assertEquals(new Divide(REGISTER_ZERO, REGISTER_ZERO, new Register(5)), a);
	}

	@Test
	void testCreateWNull() {
		Divide a = Divide.create(null);
		assertEquals(null, a);
	}
	
}
