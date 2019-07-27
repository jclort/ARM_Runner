package instructions;

import org.junit.jupiter.api.BeforeEach;

import driver.Driver;
import driver.Node;
import driver.SystemState;

/**
 *
 *
 * @author Habibullah Aslam
 */
public abstract class InstructionTestSetup {
	
	protected static final int REGISTER_ZERO = 0;
	protected static final int REGISTER_ONE = 1;
	protected static final Register REG_ONE = new Register(1);
	protected Driver dInst;
	protected SystemState sInst;
	protected Node startNode;
	
	@BeforeEach
	public void oneTimeSetUp() {
		sInst = new SystemState();
	}
	
	void setupDriverAndSetStartNode(Instruction startInstr) {
		startNode = new Node(0, startInstr);
		dInst = new Driver(startNode);
		dInst.systemState = sInst;
	}
}
