package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UtilTest {
	
	/**
	 * This is a dummy test to help us get 100% code coverage for Util class
	 * (so I don't have to keep wondering why the Util class isn't fully covered when I run eclEmma code coverage)
	 */
	@Test
	void testConstructor() {
		Util u = new Util();
	}
	
	@Test
	void testLongToInts() {
		long input = Long.MAX_VALUE;
		int[] ints = Util.splitLongIntoInts(input);
		
		assertEquals(Integer.MAX_VALUE, ints[0]);
		assertEquals(-1, ints[1]);
	}
	
	@Test
	void testIntsToLong() {
		// Every bit should be 1 in both of these
		int inputHigh = -1;
		int inputLow = -1;
		
		long result = Util.joinIntsIntoLong(inputHigh, inputLow);
		
		assertEquals(-1, result);
	}

	@Test
	void testLongToIntsToLong() {
		for (long count = 1; count < Long.MAX_VALUE / 3; count = count * 3) {
			long tempIn = count;
			int[] tempInts = Util.splitLongIntoInts(tempIn);
			long tempOut = Util.joinIntsIntoLong(tempInts);
			
			assertEquals(tempIn, tempOut);
		}
	}
	
	@Test
	void testIntsToLongToInts() {
		for (int highInt = 1; highInt < Integer.MAX_VALUE / 3; highInt = highInt * 3) {
			int lowInt = highInt - 1;
			long tempLong = Util.joinIntsIntoLong(highInt, lowInt);
			int[] tempOut = Util.splitLongIntoInts(tempLong);
			
			assertEquals(highInt, tempOut[0]);
			assertEquals(lowInt, tempOut[1]);
		}
	}
	
	@Test
	void testLessThanUnsigned() {
		long n1 = 1;
		long n2 = -1;

		assertTrue(Util.lessThanUnsigned(n1, n2));
		assertFalse(Util.lessThanUnsigned(n2, n1));

		n1 = 1;
		n2 = 2;
		assertTrue(Util.lessThanUnsigned(n1, n2));
		assertFalse(Util.lessThanUnsigned(n2, n1));

		n1 = 0;
		n2 = 1;
		assertTrue(Util.lessThanUnsigned(n1, n2));
		assertFalse(Util.lessThanUnsigned(n2, n1));
	}
}
