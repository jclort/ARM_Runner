package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StoreTest extends InstructionTestSetup {
	
	private static final int ADDRESS_16 = 16;
	
	@Test
	void testStoreWithNoOffset() {
		sInst.setRegisterValue(REGISTER_ZERO, ADDRESS_16);
		sInst.setRegisterValue(5, 2);
		Store a = new Store(5, REGISTER_ZERO, new Immediate(0));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(2, sInst.memoryManager.loadDoubleWord(ADDRESS_16));
	}
	
	@Test
	void testStoreWithOffset() {
		sInst.setRegisterValue(REGISTER_ZERO, 12);
		sInst.setRegisterValue(5, 2);
		Store a = new Store(5, REGISTER_ZERO, new Immediate(4));
		setupDriverAndSetStartNode(a);
		a.execute(dInst);
		assertEquals(2, sInst.memoryManager.loadDoubleWord(ADDRESS_16));
	}
}
