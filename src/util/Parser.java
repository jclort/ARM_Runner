package util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import driver.LabelNode;
import driver.Node;
import instructions.Branch;
import instructions.BranchLink;
import instructions.Instruction;
import instructions.InstructionGenerator;
import instructions.NOP;

/**
 * Instances of Parser should correlate 1 to 1 with ARM code that needs to be
 * processed.
 *
 * @author Mark
 */

public class Parser {
	
	private boolean inDataSeg = false;
	
	public String error = "\n";
	
	private final Node BLANK_NODE = new Node(0, 0, null);
	
	//comment pattern, group 1 returns line without ;comment
	private final Pattern commentPattern = Pattern.compile("^(.*?)\\s*;.*$");
	//if match, line has label. Label is group 1, instruction(rest) is group 2
	private final Pattern labelPattern = Pattern.compile("^\\s*(\\w*)\\s*:\\s*?(.*)$");
	//opcode is group 1, args(rest) is group 2
	private final Pattern opCodePattern = Pattern.compile("^\\s*([A-Z]+|[a-z]+)\\b(?:\\s+(.*))?");
	//matches if follows format ^BXX label$, group 1 is condition (can be blank), group 2 is label
	private final Pattern branchPattern = Pattern
			.compile("^\\s*(?:B(?= )|b(?= )|B(?=[A-Z]+)|b(?=[a-z]+))([A-Z]+|[a-z]+|)\\b\\s+(\\w+)\\s*$");
	//arg1 is group1, arg2 is group2, arg3 is group3, comment is group4
	private final Pattern argPattern = Pattern
			.compile("^\\s*([xX]\\d+),(?: )?([xX]\\d+),(?: )?((?:[xX]\\d+)|(?:#?\\d+))\\s*(;.*)?$");
	// if in data segment, group 1 is type, group 2 is value
	private final Pattern dataPattern = Pattern.compile("^\\s*\\.(\\w*)\\s*(\\\".*\\\"|(?:0x|-)?\\d*)$");
	//just an empty line/line with only whitespace
	private final Pattern blankPattern = Pattern.compile("^\\s*$");
	
	private HashMap<String, LabelNode> labelMap = new HashMap<String, LabelNode>();
	
	private long currAddress = -4;
	
	public static final String INSTRUCTION_DELIMITER = "\n";
	
	//public static final HashMap<String, String> opMap = new HashMap<String, String>();
	
	// Some sort of data structure for tracking invalid lines
	// CreateProgramGraph should not fail-fast when encountering
	// invalid ARM, but instead should parse through every line,
	// keeping track of every bad line.
	
	public ArrayList<Integer> badLines = new ArrayList<Integer>();
	
	/**
	 * Used to parse the data segment of a program if it exists
	 * Visibility is set to package-private so that it can be tested
	 *
	 * @param type type of the data (ascii, asciz, string). Dot before typename should be filtered out before passing in
	 * @param str value of the data
	 * @return
	 */
	protected byte[] parseStringData(String type, String str) {
		byte[] ba = str.substring(1, str.length() - 1).getBytes();
		if (type.equalsIgnoreCase("asciz") || type.equalsIgnoreCase("string")) {
			ba = Arrays.copyOf(ba, ba.length + 1);
		}
		return ba;
	}
	
	protected Object parseWordData(String type, String val) {
		String numStr = val;
		int rad = 10;
		
		if(Pattern.compile("0x.*").matcher(val).matches()) {
			numStr = val.substring(2);
			rad = 16;
		}
		
		switch(type.toUpperCase()) {
		case "HALF":
		case "2BYTE":
			return Short.parseShort(numStr, rad);
		case "WORD":
		case "4BYTE":
			return Integer.parseInt(numStr, rad);
		case "QUAD":
		case "8BYTE":
			return Long.parseLong(numStr, rad);
		default:
			return null;
		}
		
	}
	
	protected Object parseData(String type, String val) {
		if(type.equalsIgnoreCase("ascii") || type.equalsIgnoreCase("string") || type.equalsIgnoreCase("asciz")) {
			return parseStringData(type, val);
		}else {
			return parseWordData(type, val);
		}
	}
	
	/**
	 * Does initial runthrough of program code and puts all labels into a map
	 *
	 * @param programString
	 */
	public ArrayList<String> dataSegTypes;
	public ArrayList<Object> dataSegValues;
	public HashMap<String, Object> dataLookup;
	private void constructLabelMap(String programString) {
		dataLookup = new HashMap<String, Object>();
		String[] lines = programString.split(INSTRUCTION_DELIMITER);
		int lineNumber = 1;
		for (String line : lines) {
			if (line.equalsIgnoreCase(".data")) {
				inDataSeg = true;
			}
			else if (line.equalsIgnoreCase(".text")) {
				inDataSeg = false;
			}
			else {
				String trimmedLine = line;
				Matcher commentMatcher = commentPattern.matcher(line);
				Matcher labelMatcher = labelPattern.matcher(trimmedLine);
				if(commentMatcher.matches()) {
					trimmedLine = commentMatcher.group(1);
				}
				if (inDataSeg) {
					Matcher dataMatcher;
					boolean hasLabel = labelMatcher.matches();
					if (hasLabel) { //TODO: label should be OPTIONAL
						//TODO: handle possible comments and blank lines too
						trimmedLine = labelMatcher.group(2);
					}
					if(!blankPattern.matcher(trimmedLine).matches()) {
						dataMatcher = dataPattern.matcher(trimmedLine);

						if (dataMatcher.matches()) {
							String dataType = dataMatcher.group(1);
							Object dataVal = parseData(dataType, dataMatcher.group(2));
							dataSegTypes.add(dataType);
							dataSegValues.add(dataVal);
							if(hasLabel) {
								dataLookup.put(labelMatcher.group(1), dataVal);
							}
						}
						else{
							error += "bad data \n";
							badLines.add(lineNumber);
						}
					}
				}
				else {
					if (labelMatcher.matches()) {
						labelMap.put(labelMatcher.group(1), new LabelNode(labelMatcher.group(1), lineNumber, null));
					}
				}
			}
			lineNumber++;
		}
	}

	public static HashMap<Long, Node> addressMap;
	
	public Node createProgramGraph(String programString) {
		addressMap = new HashMap<Long, Node>();
		dataSegTypes = new ArrayList<String>();
		dataSegValues = new ArrayList<Object>();
		constructLabelMap(programString);
		String[] lines = programString.split(INSTRUCTION_DELIMITER, -1);
		int index = 0;
		Node firstNode = null;
		Node currentNode = null;
		while (index < lines.length) {
			if (lines[index].equalsIgnoreCase(".data")) {
				while(!lines[index].equalsIgnoreCase(".text")) {
					index++;
					if(index == lines.length) {
						error += ".text section not found \n";
						return null;
					}
				}
				index++;
			}
			Node newNode = parseLine(lines[index], index + 1);
			if(firstNode == null && newNode != null) {
				firstNode = newNode;
				currentNode = newNode;
			}else {
				if (newNode == null) {
					// Error, add it to the set of errors
					badLines.add(index);
				}
				else if (!newNode.equals(BLANK_NODE)) {
					addressMap.put(newNode.getAddress(), newNode);
					if (firstNode.equals(BLANK_NODE)) {
						firstNode = newNode;
					}
					else {
						currentNode.setNext(newNode);
					}
					currentNode = newNode;
				}
			}
			index++;
		}
		
		// We don't want to return a valid node if there were any errors.
		if (badLines.size() > 0) {
			// This indicates that we didn't get a valid program. Caller should check badLines.
			return null;
		}
		return firstNode;
	}
	
	ArrayList<LabelNode> labelsToLink = new ArrayList<LabelNode>();
	
	//Magic happens here
	protected Node parseLine(String line, long lineNumber) {
		// TODO: research if labels appear on the same line as instructions or not
		// if they do, we'll need this method to silently create the label node
		// and point it at the next node. Otherwise, the CreateProgramGraph method
		// can handle it.
		
		//Handle comment parsing
		Matcher commentMatcher = commentPattern.matcher(line);
		if (commentMatcher.matches()) {
			line = commentMatcher.group(1);
		}
		
		//Handle label parsing
		String labelName = "";
		Matcher labelMatcher = labelPattern.matcher(line);
		boolean hasLabel = labelMatcher.matches();
		if (hasLabel) {
			labelName = labelMatcher.group(1);
			line = labelMatcher.group(2);
			labelsToLink.add(labelMap.get(labelName));
		}
		
		//Handle blank input parsing
		if (blankPattern.matcher(line).matches()) {
			return BLANK_NODE;
		}
		
		currAddress += 4;
		
		//Handle Branch instructions
		Matcher branchMatcher = branchPattern.matcher(line);
		if (branchMatcher.matches()) {
			String condition = branchMatcher.group(1);
			String branchLabel = branchMatcher.group(2);
			if (!labelMap.containsKey(branchLabel)) {
				System.out.println(branchLabel + " is not a real label");
				return null;
			}
			Node resultNode;
			if (condition.equalsIgnoreCase("L")) {
				resultNode = new Node(currAddress, lineNumber, new BranchLink(labelMap.get(branchLabel)));
			}
			else {
				if (!conditions.Conditions.isValidCondition(condition)) {
					error += "invalid branch condition";
					return null;
				}
				resultNode = new Node(currAddress, lineNumber,
						new Branch(conditions.Conditions.getConditionFromString(condition), labelMap.get(branchLabel)));
			}
			if (!labelsToLink.isEmpty()) {
				linkLabelsToNode(resultNode);
			}
			return resultNode;
		}
		
		// Handle other instructions
		String opCode;
		Matcher opCodeMatcher = opCodePattern.matcher(line);
		if (opCodeMatcher.matches()) {
			opCode = opCodeMatcher.group(1).toUpperCase();
			line = opCodeMatcher.group(2);
		}
		else {
			System.out.println("no opcode");
			return null;
		}
		
		if (InstructionGenerator.INSTRUCTION_GENERATORS.containsKey(opCode)) {
			/*
			Matcher argMatcher = argPattern.matcher(line);
			if (!argMatcher.matches()) {
				System.out.println("bad arguments!");
				return null;
			}
			*/
			InstructionGenerator ig = InstructionGenerator.INSTRUCTION_GENERATORS.get(opCode);
			Instruction instr = ig.generate(line);
			if (instr == null) {
				return null;
			}
			Node resultNode = new Node(currAddress, lineNumber, instr);
			
			if (!labelsToLink.isEmpty()) {
				linkLabelsToNode(resultNode);
			}
			return resultNode;
		}
		else {
			error += "invalid opcode! \n";
			return null;
		}
		
		// TODO: figure out what to do here (note that we'll need to track addresses here maybe)
		// TODO: tell them how they messed up
		
	}
	
	private void linkLabelsToNode(Node node) {
		ListIterator<LabelNode> iter = labelsToLink.listIterator();
		while (iter.hasNext()) {
			LabelNode ln = iter.next();
			ln.setNext(node);
			iter.remove();
		}
	}
}
