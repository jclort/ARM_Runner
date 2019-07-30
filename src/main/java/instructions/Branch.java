package instructions;

import conditions.Condition;
import driver.Driver;
import driver.LabelNode;

public class Branch extends Instruction {
	
	private Condition condition;
	private LabelNode destination;

	public Branch(Condition condition, LabelNode destination) {
		this.condition = condition;
		this.destination = destination;
	}

	@Override
	public void execute(Driver driver) {
		if (condition.isTrue(driver.systemState)) {
			driver.jumpToLabel(destination);
		}
	}

}
