package instructions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This class extends InstructionGenerator so that we can test its methods
 *
 * @author habib
 */
class InstructionGeneratorTest extends InstructionTestSetup {
	
	@Test
	void testGetArgs() {
		String armcode = "x0,x0,x0";
		String[] argList = { "x0", "x0", "x0" };
		assertArrayEquals(argList, InstructionGenerator.parseArgs(armcode, 3));
	}

	@Test
	void testGetDataSource() {
		String register = "x0";
		assertEquals(new Register(0), InstructionGenerator.getDataSource(register));
	}
	
	@Test
	void testGetRegisterIndex() {
		String register = "r10";
		assertEquals(null, InstructionGenerator.getRegisterIndex(register));

		register = "xa";
		assertEquals(null, InstructionGenerator.getRegisterIndex(register));
		
		register = "x1";
		assertEquals(1, InstructionGenerator.getRegisterIndex(register).intValue());
	}

	@Test
	void testGetRegister() {
		String register = "x0";
		assertEquals(new Register(0), InstructionGenerator.getRegister(register));
	}

	@Test
	void testGetImmediate() {
		String immediate = "#1";
		assertEquals(new Immediate(1), InstructionGenerator.getImmediate(immediate));
		
		immediate = "s";
		assertEquals(null, InstructionGenerator.getImmediate(immediate));

		immediate = "&s";
		assertEquals(null, InstructionGenerator.getImmediate(immediate));

		immediate = "&f";
		assertEquals(new Immediate(15), InstructionGenerator.getImmediate(immediate));
		
		immediate = "15";
		assertEquals(new Immediate(15), InstructionGenerator.getImmediate(immediate));
	}
	
	//	public InstructionGeneratorTest(String opCode, int argc) {
	//		super(opCode, argc);
	//	}
	//
	//	@Override
	//	public Instruction generate(String argString) {
	//		return null;
	//	}

}
