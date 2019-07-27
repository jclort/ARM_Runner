package driver;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MemoryManagerTest {

	private static final int REGISTER_ZERO = 0;
	static SystemState sInst;
	static MemoryManager memMgr;

	@BeforeAll
	public static void oneTimeSetUp() {
		sInst = new SystemState();
		memMgr = sInst.memoryManager;
	}

	@Test
	void testConstructorWithWordLimit() {
		final int numWords = 50;
		MemoryManager memMgr2 = new MemoryManager(numWords);
		assertEquals(numWords, memMgr2.getMemoryArray().length);
	}

	@Test
	void testStoreWord() {
		final int value = 50;
		memMgr.storeWord(MemoryManager.WORD_SIZE, value);
		assertEquals(value, memMgr.getMemoryArray()[1].getValue());
	}

	@Test
	void testLoadWordFromLower() {
		sInst.setRegisterValue(REGISTER_ZERO, 5L);
		sInst.storeRegister(REGISTER_ZERO, 0);
		assertEquals(5, memMgr.loadWord(0 + MemoryManager.WORD_SIZE));
	}

	@Test
	void testLoadWordFromHigher() {
		sInst.setRegisterValue(REGISTER_ZERO, 4294967296L); //2^32 ie 1 with 32 0's
		sInst.storeRegister(REGISTER_ZERO, 0);
		assertEquals(1, memMgr.loadWord(0));
	}

	@Test
	void testLoadDoubleWord() {
		sInst.setRegisterValue(REGISTER_ZERO, 4294967295L); //2^32-1 ie all 1's
		sInst.storeRegister(REGISTER_ZERO, 0);
		assertEquals(4294967295L, memMgr.loadDoubleWord(0));
	}
}
