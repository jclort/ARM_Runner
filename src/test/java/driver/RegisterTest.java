package driver;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import instructions.InstructionTestSetup;
import main.Main;

public class RegisterTest extends InstructionTestSetup {
	
	@Test
	public void testGetIndex() throws Exception {
		Register r = new Register(REGISTER_ZERO);
		assertEquals(0, r.getIndex());
	}

	@Test
	public void testDecVsHexDisplay() throws Exception {
		sInst.setRegisterValue(REGISTER_ZERO, 10);
		Main.decimalDisplay.set(true);
		Main.hexDisplay.set(false);
		assertEquals("00     : 10", sInst.getRegisterArray()[REGISTER_ZERO].toString());

		Main.decimalDisplay.set(false);
		Main.hexDisplay.set(true);
		assertEquals("00     : 0x000000000000000A", sInst.getRegisterArray()[REGISTER_ZERO].toString());
	}

	@Test
	public void testSpecialRegisterDisplay() throws Exception {
		Main.decimalDisplay.set(true);
		Main.hexDisplay.set(false);
		assertEquals("29 (FP): 0", sInst.getRegisterArray()[sInst.FP_REGISTER].toString());
		assertEquals("30 (LR): 0", sInst.getRegisterArray()[sInst.LR_REGISTER].toString());
		assertEquals("31 (SP): 1048568", sInst.getRegisterArray()[sInst.SP_REGISTER].toString());
		assertEquals("   (PC): 0", sInst.PC.toString());

	}
}
