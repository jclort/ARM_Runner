package util;

public class Util {

	public static final long UPPER_WORD_BIT_MASK = 0xffffffff00000000L;
	public static final long LOWER_WORD_BIT_MASK = 0x00000000ffffffffL;
	
	public static final int UPPER_HALF_WORD_BIT_MASK = 0xffff0000;
	public static final int LOWER_HALF_WORD_BIT_MASK = 0x0000ffff;
	
	/**
	 * Split a 64 bit value into two 32 bit values, the first with
	 * the upper 32 bits, the second with the lower 32 bits.
	 * @param value - The 64 bit value to split
	 * @return an array of 32 bit ints with length 2, where the 0th
	 * element is the upper 32 bits of <bold>value</bold> and the
	 * 1st element is the lower 32 bits.
	 */
	public static int[] splitLongIntoInts(long value)
	{
		int[] result = new int[2];
		
		// To get upper word, perform unsigned right shift by 32, then make into int
		long shifted = value >>> 32;
		result[0] = (int)shifted;
		
		// To get lower word, mask long with lower word bit mask, then make into int
		long masked = value & LOWER_WORD_BIT_MASK;
		result[1] = (int)masked;
		
		return result;
	}
	
	public static short[] splitIntIntoShorts(int value)
	{
		short[] result = new short[2];
		
		// To get upper word, perform unsigned right shift by 32, then make into int
		int shifted = value >>> 16;
		result[0] = (short)shifted;
		
		// To get lower word, mask long with lower word bit mask, then make into int
		int masked = value & LOWER_HALF_WORD_BIT_MASK;
		result[1] = (short)masked;
		
		return result;
	}
	
	/**
	 * Split a 64 bit value into 8 8 bit values, the first with
	 * the upper 32 bits, the second with the lower 32 bits.
	 * @param value - The 64 bit value to split
	 * @return an array of 32 bit ints with length 2, where the 0th
	 * element is the upper 32 bits of <bold>value</bold> and the
	 * 1st element is the lower 32 bits.
	 */
	public static byte[] splitLongIntoBytes(long value)
	{
		byte[] result = new byte[8];
		
		//Each word will be shifted 8 less than the last
		result[0] = (byte)(value >>> 56);
		result[1] = (byte)(value >>> 48);
		result[2] = (byte)(value >>> 40);
		result[3] = (byte)(value >>> 32);
		result[4] = (byte)(value >>> 24);
		result[5] = (byte)(value >>> 16);
		result[6] = (byte)(value >>> 8);
		result[7] = (byte)(value >>> 0);
		
		return result;
	}
	
	/**
	 * Split a 32 bit value into 4 8 bit values, the first with
	 * the upper 32 bits, the second with the lower 32 bits.
	 * @param value - The 64 bit value to split
	 * @return an array of 32 bit ints with length 2, where the 0th
	 * element is the upper 32 bits of <bold>value</bold> and the
	 * 1st element is the lower 32 bits.
	 */
	public static byte[] splitIntIntoBytes(int value)
	{
		byte[] result = new byte[4];
		
		//Each word will be shifted 8 less than the last
		result[0] = (byte)(value >>> 24);
		result[1] = (byte)(value >>> 16);
		result[2] = (byte)(value >>> 8);
		result[3] = (byte)(value >>> 0);
		
		return result;
	}
	
	/**
	 * Split a 16 bit value into 2 8 bit values, the first with
	 * the upper 32 bits, the second with the lower 32 bits.
	 * @param value - The 64 bit value to split
	 * @return an array of 32 bit ints with length 2, where the 0th
	 * element is the upper 32 bits of <bold>value</bold> and the
	 * 1st element is the lower 32 bits.
	 */
	public static byte[] splitShortIntoBytes(short value)
	{
		byte[] result = new byte[2];
		
		//Each word will be shifted 8 less than the last
		result[0] = (byte)(value >>> 8);
		result[1] = (byte)(value >>> 0);
		
		return result;
	}
	
	/**
	 * Join two 32 bit values into a single 64 bit value, with the first
	 * parameter being the upper 32 bits of the result, and the second
	 * parameter being the lower 32 bits of the result.
	 * @param highBits - Desired upper 32 bits of the result
	 * @param lowBits - Desired lower 32 bits of the result
	 * @return A 64 bit value with bits determined by the two parameters.
	 */
	public static long joinIntsIntoLong(int highBits, int lowBits)
	{
		long highBitsLong = Integer.toUnsignedLong(highBits) << 32;
		long lowBitsLong = Integer.toUnsignedLong(lowBits);
		return highBitsLong | lowBitsLong;
	}
	
	public static long joinIntsIntoLong(int[] ints)
	{
		long l1 = (long)(Integer.toUnsignedLong(ints[0]) << 32);
		long l2 = (long)(Integer.toUnsignedLong(ints[1]) << 0);
		
		return (l1|l2);
	}
	
	/**
	 * Join two 8 bit values into a single 16 bit value.
	 * @param intPair - Array of bytes with length eight.
	 * @return A 64 bit value with bits determined by the values in the parameter.
	 */
	public static short joinBytesIntoShort(byte[] bytes)
	{
		short l1 = (short)(Byte.toUnsignedLong(bytes[0]) << 8);
		short l2 = (short)(Byte.toUnsignedLong(bytes[0]) << 0);
		
		return (short) (l1|l2);
	}
	
	/**
	 * Join eight 8 bit values into a single 64 bit value.
	 * @param intPair - Array of bytes with length eight.
	 * @return A 64 bit value with bits determined by the values in the parameter.
	 */
	public static int joinBytesIntoInt(byte[] bytes)
	{
		int l1 = Byte.toUnsignedInt(bytes[0]) << 24;
		int l2 = Byte.toUnsignedInt(bytes[0]) << 16;
		int l3 = Byte.toUnsignedInt(bytes[0]) << 8;
		int l4 = Byte.toUnsignedInt(bytes[0]) << 0;
		
		return l1|l2|l3|l4;
	}
	
	public static int joinShortsIntoInt(short[] shorts)
	{
		int l1 = Short.toUnsignedInt(shorts[0]) << 8;
		int l2 = Short.toUnsignedInt(shorts[0]) << 0;
		
		return l1|l2;
	}
	
	/**
	 * Join eight 8 bit values into a single 64 bit value.
	 * @param intPair - Array of bytes with length eight.
	 * @return A 64 bit value with bits determined by the values in the parameter.
	 */
	public static long joinBytesIntoLong(byte[] bytes)
	{
		long l1 = Byte.toUnsignedLong(bytes[0]) << 56;
		long l2 = Byte.toUnsignedLong(bytes[0]) << 48;
		long l3 = Byte.toUnsignedLong(bytes[0]) << 40;
		long l4 = Byte.toUnsignedLong(bytes[0]) << 32;
		long l5 = Byte.toUnsignedLong(bytes[0]) << 24;
		long l6 = Byte.toUnsignedLong(bytes[0]) << 16;
		long l7 = Byte.toUnsignedLong(bytes[0]) << 8;
		long l8 = Byte.toUnsignedLong(bytes[0]) << 0;
		
		return l1|l2|l3|l4|l5|l6|l7|l8;
	}
	
	/**
	 * Compare two longs in an unsigned fashion. Pulled straight from
	 * the internet.
	 * https://www.javamex.com/java_equivalents/unsigned_arithmetic.shtml
	 * @param n1
	 * @param n2
	 * @return true if n1 is less than n2 when treating both as unsigned values
	 */
	public static boolean lessThanUnsigned(long n1, long n2)
	{
		  return (n1 < n2) ^ ((n1 < 0) != (n2 < 0));
	}
}
