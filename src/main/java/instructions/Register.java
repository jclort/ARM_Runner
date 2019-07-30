package instructions;

import driver.SystemState;

/**
 * Register object is a hook into the SystemState. A way of looking into the
 * system state. It does not represent an actual register in the system state.
 * That is done via an array of longs in system state
 *
 * @author Mark
 */
public class Register implements DataSource {

	private int index;

	public Register(int index) {
		this.index = index;
	}

	@Override
	public long getValue(SystemState systemState) {
		return systemState.getValueAtRegister(index);
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Register other = (Register) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}
}
