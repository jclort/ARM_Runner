package driver;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import instructions.InstructionTestSetup;

public class DriverTest extends InstructionTestSetup {
	
	private static Node addNode;

	void setupDriverAndSetStartNode(Node startNode) {
		this.startNode = startNode;
		dInst = new Driver(startNode);
		dInst.start();
		dInst.systemState = sInst;
	}
	
	@Test
	public void testJumpToLabelWithNullNext() throws Exception {
		LabelNode lNode = new LabelNode("label1", null);
		setupDriverAndSetStartNode(lNode);
		dInst.jumpToLabel(lNode);
		assertTrue(dInst.isFinished());
	}
	
}
