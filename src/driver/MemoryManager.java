package driver;

import util.Util;

public class MemoryManager {

	public static final int WORD_SIZE = 4;

	// 1048572 bytes = 1 Mb - 4 bytes
	// This makes the valid memory range exactly 1 Mb (multiples of 4 within 0 - 1048572)
	public static final long DEFAULT_ADDRESS_MAXIMUM = 1024*1024;
	public static final long DEFAULT_WORDS = DEFAULT_ADDRESS_MAXIMUM / WORD_SIZE;

	public final long addressMaximum;
	public final long words;

	private Word[] memory; // TODO: See if we can do anything about making larger arrays

	public MemoryManager()
	{
		memory = new Word[(int) DEFAULT_WORDS];
		for(int count = 0; count < DEFAULT_WORDS; count++)
		{
			memory[count] = new Word(count*WORD_SIZE);
		}
		words = DEFAULT_WORDS;
		addressMaximum = DEFAULT_ADDRESS_MAXIMUM;
	}

	public MemoryManager(long wordCount)
	{
		memory = new Word[(int) wordCount];
		words = wordCount;
		addressMaximum = (words - 1) * WORD_SIZE;
	}

	public byte loadByte(long address) {
		return Util.splitIntIntoBytes(memory[(int) (address / WORD_SIZE)].getValue())[(int) (address % WORD_SIZE)];
	}
	
	public short loadHalfWord(long address) {
		int whichHalf = (int)(address % WORD_SIZE)/2;
		byte byte1 = Util.splitIntIntoBytes(memory[(int) (address / WORD_SIZE)].getValue())[whichHalf];
		byte byte2 = Util.splitIntIntoBytes(memory[(int) (address / WORD_SIZE)].getValue())[whichHalf + 1];
		return Util.joinBytesIntoShort(new byte[] {byte1, byte2});
	}
	
	public int loadWord(long address)
	{
		return memory[(int) (address / WORD_SIZE)].getValue();
	}

	public long loadDoubleWord(long address)
	{
		int highBits = memory[(int) (address / WORD_SIZE)].getValue();
		int lowBits = memory[(int) ((address + WORD_SIZE) / WORD_SIZE)].getValue();

		return Util.joinIntsIntoLong(highBits, lowBits);
	}
	
	public void storeByte(long address, byte value)
	{
		
		int whichOne = (int)(address % WORD_SIZE);
		byte[] bytes = Util.splitIntIntoBytes(memory[(int) (address / WORD_SIZE)].getValue());
		
		bytes[whichOne] = value;
		
		memory[(int) (address / WORD_SIZE)].setValue(Util.joinBytesIntoInt(bytes));
	}
	
	public void storeHalfWord(long address, short value)
	{
		int whichHalf = (int)(address % WORD_SIZE)/2;
		short[] shorts = Util.splitIntIntoShorts(memory[(int) (address / WORD_SIZE)].getValue());
		
		shorts[whichHalf] = value;
		
		memory[(int) (address / WORD_SIZE)].setValue(Util.joinShortsIntoInt(shorts));
	}

	public void storeWord(long address, int value)
	{
		memory[(int) (address / WORD_SIZE)].setValue(value);
	}

	public void storeDoubleWord(long address, long value)
	{
		int[] ints = Util.splitLongIntoInts(value);
		memory[(int) (address / WORD_SIZE)].setValue(ints[0]);
		memory[(int) ((address + WORD_SIZE) / WORD_SIZE)].setValue(ints[1]);
	}
	
	public Word[] getMemoryArray() {
		return memory;
	}
	
	public void step() {
		for(Word w : memory) {
			w.step();
		}
	}
}
