package instructions;

public abstract class BitwiseInstruction extends Instruction {

	protected Register destination;
	protected Register source;

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
		BitwiseInstruction other = (BitwiseInstruction) obj;
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
