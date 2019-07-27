package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoadTest extends InstructionTestSetup {

	private static final int ADDRESS_16 = 16;

	@Test
	void testLoadWithNoOffset() {
		sInst.setRegisterValue(REGISTER_ZERO, 2);
		sInst.storeRegister(0, ADDRESS_16);
		sInst.setRegisterValue(REGISTER_ZERO, ADDRESS_16);
		Load a = new Load(1, REGISTER_ZERO, new Immediate(0));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(2, sInst.memoryManager.loadDoubleWord(ADDRESS_16));
	}

	@Test
	void testLoadWithOffset() {
		sInst.setRegisterValue(REGISTER_ZERO, 2);
		sInst.storeRegister(0, ADDRESS_16);
		sInst.setRegisterValue(REGISTER_ZERO, ADDRESS_16 - 4);
		Load a = new Load(1, REGISTER_ZERO, new Immediate(4));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(2, sInst.memoryManager.loadDoubleWord(ADDRESS_16));
	}
}
