package conditions;

import org.junit.jupiter.api.BeforeEach;

import driver.Driver;
import driver.Node;
import driver.SystemState;
import instructions.Compare;
import instructions.Instruction;
import instructions.Register;

public abstract class ConditionTestSetup {

	protected static final int REG_ZERO = 0;
	protected static final Register REG_ONE = new Register(1);
	protected Driver dInst;
	protected SystemState sInst;
	protected Node startNode;
	
	@BeforeEach
	public void resetSystemState() {
		sInst = new SystemState();
	}

	public void setupDriverAndSetStartNode(Instruction startInstr) {
		startNode = new Node(0, startInstr);
		dInst = new Driver(startNode);
		dInst.systemState = sInst;
	}
	
	/**
	 * Just execute this in a test and then check the system state for any flags you want to check
	 * @param regZeroVal
	 * @param regOneVal
	 */
	public void setReg0And1ValsAndExecuteCompare(long regZeroVal, long regOneVal) {
		sInst.setRegisterValue(0, regZeroVal);
		sInst.setRegisterValue(1, regOneVal);
		Compare c = new Compare(REG_ZERO, REG_ONE);
		setupDriverAndSetStartNode(c);
		c.execute(dInst);
	}
}
