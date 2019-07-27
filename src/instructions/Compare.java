package instructions;

import driver.Driver;
import util.Util;

public class Compare extends Instruction {
	
	private Register source1;
	private DataSource source2;

	public Compare(int source1, DataSource source2) {
		this.source1 = new Register(source1);
		this.source2 = source2;
	}

	/**
	 * N - Set to 1 when the result of the operation was negative
	 * Z - Set to 1 when the result of the operation was zero
	 * 		"In practice, N is set to the two's complement sign bit of the result (bit 31)"
	 * 		https://community.arm.com/processors/b/blog/posts/condition-codes-1-condition-flags-and-codes
	 * C - set if following occurs:
		if the result of an addition is greater than or equal to 2^64 //not relev to CMP
		if the result of a subtraction is positive or zero 			  //must consider args as 64 bit and output as 65 bit
		as the result of an inline barrel shifter operation in a move or logical instruction.
	
	   V -
		Overflow occurs when the sign of the result, in bit[31], does not match the sign of the result had the operation been performed at infinite precision, for example:
		if adding two negative values results in a positive value //not relev to CMP
		if adding two positive values results in a negative value //not relev to CMP
		if subtracting a positive value from a negative value generates a positive value
		if subtracting a negative value from a positive value generates a negative value.

		The Compare operations are identical to subtracting
		http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.dui0552a/BABEHFEF.html
		This question contradicts the above on carry flag explanation
		https://stackoverflow.com/questions/8031625/why-does-cmp-compare-sometimes-sets-a-carry-flag-in-8086-assembly?rq=1
	 */
	@Override
	public void execute(Driver driver) {
		long v1 = source1.getValue(driver.systemState);
		long v2 = source2.getValue(driver.systemState);
		long result = v1 - v2;
		//		System.out.println("V1:" + v1 + " V2:" + v2 + " Result:" + result);

		boolean v1Positive = v1 > 0;
		boolean v2Positive = v2 > 0;

		boolean n = result < 0;
		boolean z = result == 0;
		//		boolean c = result > 0 || z; //this is weird, I have to do comparisons unsigned for carries
		boolean c = !Util.lessThanUnsigned(v1, v2);
		//		System.out.println(c);
		//		boolean c = Util.lessThanUnsigned(v1, result);
		boolean v = (v2Positive && !v1Positive && (result > 0)) || (!v2Positive && v1Positive && (result < 0));
		/* ie the same as checking whether result would be less than 2^-63 or greater than 2^63-1
		 * if we had stored result in a variable that could store more than 64 bits
		 * (we're currently using a long tho, so we stuck at 64 bits)
		 */

		//		boolean v = (positiveV2 && (v1 < result)) || (!positiveV2 && (result < v1));
		driver.systemState.setFlags(n, z, c, v);
	}
	
}
