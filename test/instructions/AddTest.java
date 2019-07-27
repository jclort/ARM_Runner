package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.ShiftDirection;

class AddTest extends InstructionTestSetup {
	
	@Test
	void testRegisterAdding() {
		sInst.setRegisterValue(5, 2);
		Add a = new Add(REGISTER_ZERO, REGISTER_ZERO, new Register(5));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(2, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testImmediateAdding() {
		Add a = new Add(REGISTER_ZERO, REGISTER_ZERO, new Immediate(5));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(5, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testShiftedAdding() {
		sInst.setRegisterValue(REGISTER_ZERO, 0);
		sInst.setRegisterValue(1, 1);
		Add a = new Add(0, 0, new ComplexRegister(1, ShiftDirection.LEFT, 2));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(4, sInst.getValueAtRegister(0));
	}
	
	@Test
	void testCreate() {
		Add a = Add.create(new Object[] { REGISTER_ZERO, REGISTER_ZERO, new Register(5) });
		assertEquals(new Add(REGISTER_ZERO, REGISTER_ZERO, new Register(5)), a);
	}
	
	@Test
	void testCreateWNull() {
		Add a = Add.create(null);
		assertEquals(null, a);
	}
}
