package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import conditions.Conditions;
import driver.Driver;
import driver.LabelNode;
import driver.Node;
import util.Parser;

/**
 * I did not test branch with no argument, because the parser
 * automatically turns no-arg branch into an always condition (so that's a test for the parser)
 *
 * @author Habibullah Aslam
 */
class BranchTest extends InstructionTestSetup {
	
	private static Node addNode;
	
	void setupDriverAndSetStartNode(Node startNode) {
		this.startNode = startNode;
		dInst = new Driver(startNode);
		dInst.start();
		dInst.systemState = sInst;
	}
	
	@Test
	void testBranchTrue() {
		LabelNode lNode = new LabelNode("label1", null);
		sInst.setRegisterValue(5, 2);
		addNode = new Node(0L, new Add(REGISTER_ZERO, REGISTER_ZERO, new Register(5)));
		setupDriverAndSetStartNode(addNode);
		lNode.setNext(addNode);
		Node branchNode = new Node(4L, new Branch(Conditions.getConditionFromString("AL"), lNode));
		addNode.setNext(branchNode);
		Parser.addressMap = new HashMap<Long, Node>();
		Parser.addressMap.put(0L, addNode);
		Parser.addressMap.put(4L, branchNode);
		dInst.step(); //step through addNode
		assertEquals(4L, sInst.PC.getValue());
		dInst.step(); //step through branch and go up to label
		assertEquals(0L, sInst.PC.getValue());
	}
	
	@Test
	void testBranchFalse() {
		addNode = new Node(0L, new Add(REGISTER_ZERO, REGISTER_ZERO, new Register(5)));
		setupDriverAndSetStartNode(addNode);
		LabelNode lnode = new LabelNode("label1", null);
		sInst.setRegisterValue(5, 2);
		lnode.setNext(addNode);
		Node branchNode = new Node(4L, new Branch(Conditions.getConditionFromString("LT"), lnode));
		addNode.setNext(branchNode);
		Node postBranchNode = new Node(8L, new Add(REGISTER_ZERO, REGISTER_ZERO, new Immediate(5)));
		branchNode.setNext(postBranchNode);
		Parser.addressMap = new HashMap<Long, Node>();
		Parser.addressMap.put(0L, addNode);
		Parser.addressMap.put(4L, branchNode);
		Parser.addressMap.put(8L, postBranchNode);

		dInst.step(); //step through addNode
		assertEquals(4L, sInst.PC.getValue());
		dInst.step(); //step through branch and skip over to next Add
		assertEquals(8L, sInst.PC.getValue());
		dInst.step();
		assertEquals(7, sInst.getValueAtRegister(REGISTER_ZERO));
	}
}
