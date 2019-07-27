package instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import driver.Driver;
import driver.LabelNode;
import driver.Node;
import util.Parser;

public class ReturnTest extends InstructionTestSetup {

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
		Node addNode2 = new Node(4L, new Add(REGISTER_ZERO, REGISTER_ZERO, new Immediate(10)));
		addNode.setNext(addNode2);
		Node returnNode = new Node(8L, new Return());
		addNode2.setNext(returnNode);
		
		sInst.setLRValue(4L);
		
		Parser.addressMap = new HashMap<Long, Node>();
		Parser.addressMap.put(0L, addNode);
		Parser.addressMap.put(4L, addNode2);
		Parser.addressMap.put(8L, returnNode);
		dInst.step(); //step through addNode
		assertEquals(4L, sInst.PC.getValue());
		dInst.step(); //step through addNode2
		assertEquals(8L, sInst.PC.getValue());
		dInst.step(); //step through branch and go up to add2
		assertEquals(4L, sInst.PC.getValue());
		assertEquals(12, sInst.getValueAtRegister(REGISTER_ZERO));
		dInst.step(); //step through branch and go up to add2
		assertEquals(8L, sInst.PC.getValue());
		assertEquals(22, sInst.getValueAtRegister(REGISTER_ZERO));
	}
}
