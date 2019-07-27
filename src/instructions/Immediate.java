package instructions;

import driver.SystemState;

/**
 * Note: No such thing as a negative immediate!
 *
 * @author habib
 */
public class Immediate implements DataSource {
	
	private long value;
	
	public Immediate(long value) {
		this.value = value;
	}
	
	@Override
	public long getValue(SystemState systemState) {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (value ^ (value >>> 32));
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
		Immediate other = (Immediate) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}
}
