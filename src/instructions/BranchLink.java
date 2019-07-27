package instructions;

import conditions.Condition;
import conditions.Conditions;
import driver.Driver;
import driver.LabelNode;

public class BranchLink extends Instruction {

	private Condition condition;
	private LabelNode destination;
	
	public BranchLink(LabelNode destination) {
		condition = Conditions.getConditionFromString("AL");
		this.destination = destination;
	}
	
	@Override
	public void execute(Driver driver) {
		if (condition.isTrue(driver.systemState)) { 							//an unnecessary thing to check since we set to "Always", but it's there for clarity I guess. The compiler will probably ignore this anyways
			driver.systemState.setLRValue(driver.systemState.PC.getValue());
			driver.jumpToLabel(destination);
		}
	}
	
}
