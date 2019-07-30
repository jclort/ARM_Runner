package driver;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SystemStateTest {
	
	private static final int REGISTER_ZERO = 0;
	private static final int REGISTER_ONE = 1;
	static SystemState sInst;
	static MemoryManager memMgr;
	
	@BeforeAll
	public static void oneTimeSetUp() {
		sInst = new SystemState();
		memMgr = sInst.memoryManager;
	}

	@Test
	void testLoadRegister() {
		sInst.setRegisterValue(REGISTER_ZERO, 4294967295L); //2^32-1 ie all 1's
		sInst.storeRegister(REGISTER_ZERO, 0);
		sInst.loadRegister(REGISTER_ONE, 0);
		assertEquals(4294967295L, sInst.getValueAtRegister(REGISTER_ONE));
	}

}
