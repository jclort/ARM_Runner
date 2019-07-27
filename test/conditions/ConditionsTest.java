package conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests Conditions class
 *
 * @author Habibullah Aslam
 */
public class ConditionsTest {

	@Test
	public void testConditionsMap() {
		Conditions c = new Conditions(); //need this to make conditions class test 100% covered
		assertEquals("CC", Conditions.getConditionFromString("CC").code());
	}
	
	@Test
	public void testGetConditionFromString() {
		assertEquals("CC", Conditions.getConditionFromString("CC").code());
	}
	
	@Test
	public void testIsValidCondition() {
		assertTrue(Conditions.isValidCondition("CS"));
		assertFalse(Conditions.isValidCondition("XX"));
	}
}
