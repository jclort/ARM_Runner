package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NOPTest extends InstructionTestSetup {

	@Test
	void testNOP() {
		sInst.setRegisterValue(5, 2);
		NOP a = new NOP();
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(2, sInst.getValueAtRegister(5));
	}

}
