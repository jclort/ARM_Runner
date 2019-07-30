package instructions;

import static java.util.Map.entry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The methods in here are package-private for the sake of testing
 * @author habib
 *
 */
public abstract class InstructionGenerator {
	
	private enum OpType{
		ARITHMETIC, LOADSTORE, LOGIC, COMPARE, JUMP;
	};
	
	private final static Pattern argPattern = Pattern.compile("(?:((?:(?:[xX]|#|&)?[\\dA-Fa-f]+)|SP)(?:\\s*,\\s*|\\s*$))|(\\[.*)(!)?");
	
	//Complex register patterns
	private final static Pattern compRegNoOffset = Pattern.compile("\\[\\s*(x\\d+|SP)\\s*\\]\\s*");
	private final static Pattern compRegImmOffset = Pattern.compile("\\[\\s*(x\\d+|SP)\\s*,\\s*(#?\\d+|\\&[\\da-f]{1,6})\\s*]\\s*$");
	private final static Pattern compRegImmOffsetPreWB = Pattern.compile("\\[\\s*(x\\d+|SP)\\s*,\\s*(#?\\d+|\\&[\\da-f]{1,6})\\s*]\\s*!\\s*$");
	private final static Pattern compRegImmOffsetPostWB = Pattern.compile("\\[\\s*(x\\d+|SP)\\s*\\]\\s*,\\s*(#?\\d+|\\&[\\da-f]{1,6})\\s*\\s*$");
	private final static Pattern compRegRegOffset = Pattern.compile("\\[\\s*(x\\d+|SP)\\s*,\\s*(x\\d+|SP)\\s*]\\s*$");
	private final static Pattern compRegRegOffsetShifted = Pattern.compile("\\[\\s*(x\\d+|SP)\\s*,\\s*(x\\d+|SP)\\s*,\\s*(ASR|LSR|LSL)\\s+(#?\\d+|\\&[\\da-f]{1,6})\\s*]\\s*$");
	
	public OpType opType;
	
	public abstract Instruction generate(String argString);
	
	private static final InstructionGenerator ADD_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		
		@Override
		public Instruction generate(String argString) {
			return Add.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator SUB_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		
		@Override
		public Instruction generate(String argString) {
			return Subtract.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator MUL_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		@Override
		public Instruction generate(String argString) {
			return Multiply.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator DIV_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		@Override
		public Instruction generate(String argString) {
			return Divide.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator ABS_GEN = new InstructionGenerator(OpType.LOGIC) {
		@Override
		public Instruction generate(String argString) {
			return AbsoluteValue.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator LSL_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		@Override
		public Instruction generate(String argString) {
			return LogicalShiftLeft.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator LSR_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		@Override
		public Instruction generate(String argString) {
			return LogicalShiftRight.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator ASR_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		@Override
		public Instruction generate(String argString) {
			return ArithmeticShiftRight.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator ROR_GEN = new InstructionGenerator(OpType.ARITHMETIC) {
		@Override
		public Instruction generate(String argString) {
			return RotateRight.create(getArgObjs(argString, opType));
		}
	};
	private static final InstructionGenerator CMP_GEN = new InstructionGenerator(OpType.COMPARE) {
		
		@Override
		public Instruction generate(String argString) {
			String[] argList = parseArgs(argString, 2);
			if (argList == null) {
				return null;
			}
			Integer src1 = getRegisterIndex(argList[0]);
			DataSource src2 = getDataSource(argList[1]);
			if (src1 == null || src2 == null) {
				return null;
			}
			return new Compare(src1, src2);
		}
	};
	private static final InstructionGenerator STR_GEN = new InstructionGenerator(OpType.LOADSTORE) {
		
		@Override
		public Instruction generate(String argString) {
			Object[] args = getArgObjs(argString, opType);
			if (args == null) {
				return null;
			}
			return new Store((Integer)args[0], (Integer)args[1], (DataSource)args[2]);
			
		}
	};
	private static final InstructionGenerator LDR_GEN = new InstructionGenerator(OpType.LOADSTORE) {
		
		@Override
		public Instruction generate(String argString) {
			Object[] args = getArgObjs(argString, opType);
			if (args == null) {
				return null;
			}
			return new Load((Integer)args[0], (Integer)args[1], (DataSource)args[2]);
			
		}
	};
	private static final InstructionGenerator NOT_GEN = new InstructionGenerator(OpType.LOGIC) {
		
		@Override
		public Instruction generate(String argString) {
			Object[] args = getArgObjs(argString, opType);
			if (args == null) {
				return null;
			}
			return new Not((Integer)args[0], (Integer)args[1]);
		}
	};
	private static final InstructionGenerator NEG_GEN = new InstructionGenerator(OpType.LOGIC) {
		
		@Override
		public Instruction generate(String argString) {
			Object[] args = getArgObjs(argString, opType);
			if (args == null) {
				return null;
			}
			return new Negate((Integer)args[0], (Integer)args[1]);
		}
	};
	private static final InstructionGenerator RET_GEN = new InstructionGenerator(OpType.JUMP) {
		
		@Override
		public Instruction generate(String argString) {
			Object[] args = getArgObjs(argString, opType);
			/*
			if(args == null) {
				return null;
			}
			*/
			return new Return();
		}
	};
	private static final InstructionGenerator MOV_GEN = new InstructionGenerator(OpType.LOADSTORE) {
		@Override
		public Instruction generate(String argString) {
			Object[] args = getArgObjs(argString, opType);
			return Move.create(args);
		}
	};
	
	public static final Map<String, InstructionGenerator> INSTRUCTION_GENERATORS = Map.ofEntries(
			(Entry<? extends String, ? extends InstructionGenerator>) 
			entry("ADD", ADD_GEN), 
			entry("SUB", SUB_GEN),
			entry("CMP", CMP_GEN), 
			entry("STR", STR_GEN), 
			entry("NOT", NOT_GEN), 
			entry("MVN", NOT_GEN),
			entry("MUL", MUL_GEN),
			entry("DIV", DIV_GEN),
			entry("ABS", ABS_GEN),
			entry("LSL", LSL_GEN),
			entry("LSR", LSR_GEN),
			entry("ASR", ASR_GEN),
			entry("ROR", ROR_GEN),
			entry("NEG", NEG_GEN),
			entry("RET", RET_GEN),
			entry("MOV", MOV_GEN),
			entry("LDR", LDR_GEN));
	
	public InstructionGenerator(OpType opType) {
		this.opType = opType;
	}

	private static Object[] getArithmeticArgs(String argString) {
		String[] argList = parseArgs(argString, 3);
		if (argList == null) {
			return null;
		}
		Integer dest = getRegisterIndex(argList[0]);
		Integer src1 = getRegisterIndex(argList[1]);
		DataSource src2 = getDataSource(argList[2]);
		if (dest == null || src1 == null || src2 == null) {
			return null;
		}
		return new Object[]{dest, src1, src2};
	}
	
	private static Object[] getLoadStoreArgs(String argString) {
		String[] argList = parseArgs(argString, 2);//TODO: handle offset
		if(argList == null) {
			return null;
		}
		Integer src = getRegisterIndex(argList[0]);
		Integer addy = getRegisterIndex(argList[1]);
		DataSource offset = new Immediate(0);
		if (src == null || addy == null || offset == null){
			return null;
		}
		return new Object[]{src, addy, offset};
		
	}
	
	private static Object[] getLogicArgs(String argString) {
		String[] argList = parseArgs(argString, 2);
		if(argList == null) {
			return null;
		}
		Integer src = getRegisterIndex(argList[0]);
		Integer dest = getRegisterIndex(argList[1]);
		if(src == null || dest == null) {
			return null;
		}
		return new Object[] {src, dest};
	}
	
	private static Object[] getArgObjs(String argString, OpType ot) {
		switch(ot) {
		case ARITHMETIC:
			return getArithmeticArgs(argString);
		case LOADSTORE:
			return getLoadStoreArgs(argString);
		case LOGIC:
			return getLogicArgs(argString);
		case COMPARE:
			return null;
		case JUMP:
			return null;
		default:
			return null; //will never reach this
		}
	}
	
	/**
	 * By putting no visibility identifier, we make the method package-private.
	 * We need to do this for testing
	 *
	 * @param argString
	 * @param count
	 * @return
	 */
	static String[] parseArgs(String argString, int count) {
		int c = 0;
		String[] result = new String[count];
		Matcher argMatcher = argPattern.matcher(argString);
		while (argMatcher.find()) {
			if (c >= count) {
				return null;
			}
			if(argMatcher.group(1) != null) {
				result[c] = argMatcher.group(1);
			}else {
				result[c] = argMatcher.group(2);
			}
			c++;
		}
		if (c < count) {
			return null;
		}
		return result;
	}
	
	static DataSource getDataSource(String token) {
		DataSource result = getRegister(token);
		if (result != null) {
			return result;
		}
		else {
			return getImmediate(token);
		}
	}
	
	static Integer getRegisterIndex(String token) {
		if (token.substring(0, 1).equalsIgnoreCase("x")) {
			try {
				int i = Integer.parseInt(token.substring(1));
				if (i >= 0 && i < 32) {
					return i;
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}
	
	static Register getRegister(String token) {
		Integer i = getRegisterIndex(token);
		if (i == null) {
			return null;
		}
		return new Register(i);
	}
	/*
	 * private static boolean isRegisterIndex(String token) { return
	 * getRegisterIndex(token) != null; }
	 */
	
	static Immediate getImmediate(String token) {
		if (token.substring(0, 1).equals("#")) {
			try {
				int i = Integer.parseInt(token.substring(1));
				return new Immediate(i);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		else if (token.substring(0, 1).equals("&")) {
			try {
				int i = Integer.parseInt(token.substring(1), 16);
				return new Immediate(i);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		else {
			try {
				int i = Integer.parseInt(token);
				return new Immediate(i);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
	/*
	 * private static boolean isImmediateValue(String token) { return
	 * getImmediateValue(token) != null; }
	 */
	
}
