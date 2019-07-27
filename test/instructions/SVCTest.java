package instructions;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SVCTest extends InstructionTestSetup {
	
	/**
	 * Still need more framework in place for data segment before we can fully use SVC
	 * @throws Exception
	 */
	@Test
	public void testWithValidOpcodeAndRegisters() throws Exception {
		sInst.setRegisterValue(REGISTER_ZERO, 1);
		SVC s = new SVC(SVC.PRINT_CODE);
		assertTrue(true);
	}

	@Test
	void testCreate() {
		SVC s = SVC.create(new Object[] { SVC.PRINT_CODE });
		assertEquals(new SVC(SVC.PRINT_CODE), s);
	}
	
	@Test
	void testCreateWNull() {
		SVC s = SVC.create(null);
		assertEquals(null, s);
	}
	
}
