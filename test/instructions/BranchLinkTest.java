package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import driver.Driver;
import driver.LabelNode;
import driver.Node;
import driver.SystemState;
import util.Parser;

public class BranchLinkTest extends InstructionTestSetup {
	
	private static Node addNode;

	void setupDriverAndSetStartNode(Node startNode) {
		this.startNode = startNode;
		dInst = new Driver(startNode);
		dInst.start();
		dInst.systemState = sInst;
	}

	@Test
	void testExecute() {
		LabelNode lNode = new LabelNode("label1", null);
		sInst.setRegisterValue(5, 2);
		addNode = new Node(0L, new Add(REGISTER_ZERO, REGISTER_ZERO, new Register(5)));
		setupDriverAndSetStartNode(addNode);
		lNode.setNext(addNode);
		Node branchNode = new Node(4L, new BranchLink(lNode));
		addNode.setNext(branchNode);
		Parser.addressMap = new HashMap<Long, Node>();
		Parser.addressMap.put(0L, addNode);
		Parser.addressMap.put(4L, branchNode);
		dInst.step(); //step through addNode
		assertEquals(4L, sInst.PC.getValue());
		dInst.step(); //step through branch and go up to label
		assertEquals(0L, sInst.PC.getValue());
		assertEquals(4L, sInst.getValueAtRegister(SystemState.LR_REGISTER));
	}
}
