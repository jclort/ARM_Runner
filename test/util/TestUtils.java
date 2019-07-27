package util;

import driver.SystemState;

public class TestUtils {

	public static void setRegisters(SystemState s, long[] regs)
	{
		for (int i = 0; i < regs.length; i++)
		{
			s.setRegisterValue(i, regs[i]);
		}
	}
}
