package instructions;

import driver.Driver;

/**
 * Represents a software interrupt (SWI), though now it's called supervisor call in arm64 (pg 65 of giant arm pdf in readme).
 * Can be used to print to console, read in a file, etc.
 * The original ARM Runner devs didn't have time to implement more than print to console. You will need to do the rest lol.
 * Actually, this instruction doesn't work currently. Needs some work.
 *
 * Note: Think about scenario where you point r1 to random point in stack. When does SWI know when to stop printing?
 * http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.dui0068b/BABFCEEG.html
 * Register 0 should have 1 in it to represent stdout. Register 1 should have adress to label to print
 * @author Habibullah Aslam
 */
public class SVC extends Instruction {

	public static final int PRINT_CODE = 69;
	
	int code; //represents the SWI code

	SVC(int code) {
		this.code = code;
	}

	@Override
	public void execute(Driver driver) {
		if (code == PRINT_CODE) {
			if (driver.systemState.getValueAtRegister(0) == 1) {
				driver.toPrint = "" + driver.systemState.SP_REGISTER;
				//print to console driver.systemState.memoryManager(address in reg1)
			}
		}
	}

	public static SVC create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new SVC((Integer) args[0]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SVC other = (SVC) obj;
		if (code != other.code) {
			return false;
		}
		return true;
	}

}
