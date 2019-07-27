package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import driver.Node;
import instructions.Add;
import instructions.Branch;
import instructions.Immediate;

public class ParserTest {
	
	private static final Node BLANK_NODE = new Node(0, null);
	private static Parser parser;
	
	@BeforeEach
	void setupParser() {
		parser = new Parser();
	}

	@Test
	void testParseLineWithAddInstr() {
		String armcode = "ADD x0,x0,#2";
		Node result = parser.parseLine(armcode, 0);
		Node expected = new Node(0, Add.create(new Object[] { 0, 0, new Immediate(2) }));
		assertEquals(expected, result);
	}
	
	@Test
	void testParseLineWithTabbedAddInstr() {
		String armcode = "\tADD x0,x0,#2";
		Node result = parser.parseLine(armcode, 0);
		Node expected = new Node(0, Add.create(new Object[] { 0, 0, new Immediate(2) }));
		assertEquals(expected, result);
	}
	
	@Test
	void testParseLineWithPureComment() {
		String comment = "; hi this is comment";
		Node result = parser.parseLine(comment, 0);
		assertEquals(BLANK_NODE, result);

		String comment2 = ";hi this is comment";
		result = parser.parseLine(comment2, 0);
		assertEquals(BLANK_NODE, result);
	}
	
	// TODO: hacky test, but it does work
	@Test
	void testParseLineWithStackedLabels() {
		String program = "label1:\nlabel2:\nB label2";
		Node headNode = parser.createProgramGraph(program);

		String line1 = "label1:";
		Node result = parser.parseLine(line1, 1);
		Node expected = BLANK_NODE;
		assertEquals(expected, result);

		String line2 = "label2:";
		result = parser.parseLine(line2, 2);
		expected = BLANK_NODE;
		assertEquals(expected, result);

		String line3 = "B label1";
		result = parser.parseLine(line3, 3);
		expected = new Node(4, new Branch(conditions.Conditions.getConditionFromString("AL"), null));
		assertEquals(expected.getNext(), result.getNext());
		assertEquals(expected.getAddress(), result.getAddress());
	}
	
	@Test
	void testCreateProgramGraphWithNoInput() {
		String input = "";
		Node head = parser.createProgramGraph(input);
		assertEquals(BLANK_NODE, head);
	}
	
	@Test
	void testCreateProgramGraphWithGoodInstructionAndBadOpcode() {
		String input = "ADD x0,x0,2\n" + "XXX";
		Node head = parser.createProgramGraph(input);
		assertEquals(null, head);
	}
	
	@Test
	void testCreateProgramGraphWithBadOpcode() {
		String input = "XXX";
		Node head = parser.createProgramGraph(input);
		assertEquals(null, head);
	}

	@Test
	public void testParseStringData() throws Exception {
		byte[] actual = parser.parseStringData("ascii", "\"Hello\"");
		assertTrue(Arrays.equals(actual, "Hello".getBytes()));
		
		actual = parser.parseStringData("asciz", "\"Hello\"");
		assertTrue(Arrays.equals(actual, "Hello\0".getBytes()));

		actual = parser.parseStringData("string", "\"Hello\"");
		assertTrue(Arrays.equals(actual, "Hello\0".getBytes()));
	}
}
