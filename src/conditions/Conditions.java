package conditions;

import static java.util.Map.entry;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Map of all conditions
 *
 * @author Mark
 */
public class Conditions {
	
	private static final Condition equal = new Equal();
	private static final Condition notEqual = new NotEqual();
	private static final Condition carryClear = new CarryClear();
	private static final Condition carrySet = new CarrySet();
	private static final Condition negative = new Negative();
	private static final Condition positiveOrZero = new PositiveOrZero();
	private static final Condition signedOverflow = new SignedOverflow();
	private static final Condition noSignedOverflow = new NoSignedOverflow();
	private static final Condition higher = new Higher();
	private static final Condition lowerOrSame = new LowerOrSame();
	private static final Condition greaterThanOrEqual = new GreaterThanOrEqual();
	private static final Condition lessThan = new LessThan();
	private static final Condition greaterThan = new GreaterThan();
	private static final Condition lessThanOrEqual = new LessThanOrEqual();
	private static final Condition always = new Always();
	
	private static final Map<String, Condition> CONDITIONS = Map.ofEntries(
			(Entry<? extends String, ? extends Condition>) entry(equal.code(), equal), entry(notEqual.code(), notEqual),
			entry(carryClear.code(), carryClear), entry(carrySet.code(), carrySet), entry(negative.code(), negative),
			entry(positiveOrZero.code(), positiveOrZero), entry(signedOverflow.code(), signedOverflow),
			entry(noSignedOverflow.code(), noSignedOverflow), entry(higher.code(), higher),
			entry(lowerOrSame.code(), lowerOrSame), entry(greaterThanOrEqual.code(), greaterThanOrEqual),
			entry(lessThan.code(), lessThan), entry(greaterThan.code(), greaterThan),
			entry(lessThanOrEqual.code(), lessThanOrEqual), entry(always.code(), always), entry("", always),
			entry("HS", carrySet), entry("LO", carryClear));
	
	public static Condition getConditionFromString(String input) {
		return CONDITIONS.get(input.toUpperCase());
	}
	
	public static boolean isValidCondition(String input) {
		return CONDITIONS.containsKey(input.toUpperCase());
	}
}
