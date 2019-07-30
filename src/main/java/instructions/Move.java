package instructions;

import driver.Driver;

/**
 * Can move immediates (pg c2-141) in giant arm manual (see readme. https://www.element14.com/community/servlet/JiveServlet/previewBody/41836-102-1-229511/ARM.Reference_Manual.pdf)
 *
 * @author Habibullah Aslam
 */
public class Move extends Instruction {

	Register destination;
	Register source;
	
	Move(int destination, int source) {
		this.destination = new Register(destination);
		this.source = new Register(source);
	}
	
	@Override
	public void execute(Driver driver) {
		int destinationIndex = destination.getIndex();
		int sourceIndex = source.getIndex();
		
		driver.systemState.setRegisterValue(destinationIndex, driver.systemState.getValueAtRegister(sourceIndex));
	}
	
	public static Move create(Object[] args) {
		if (args == null) {
			return null;
		}
		return new Move((Integer) args[0], (Integer) args[1]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Move other = (Move) obj;
		if (destination == null) {
			if (other.destination != null) {
				return false;
			}
		}
		else if (!destination.equals(other.destination)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		}
		else if (!source.equals(other.source)) {
			return false;
		}
		return true;
	}
}
