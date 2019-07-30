package instructions;

public abstract class ArithmeticInstruction extends Instruction {

	protected Register destination;
	protected Register source1;
	protected DataSource source2;

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((source1 == null) ? 0 : source1.hashCode());
		result = prime * result + ((source2 == null) ? 0 : source2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ArithmeticInstruction other = (ArithmeticInstruction) obj;
		if (destination == null)
		{
			if (other.destination != null)
			{
				return false;
			}
		} else if (!destination.equals(other.destination))
		{
			return false;
		}
		if (source1 == null)
		{
			if (other.source1 != null)
			{
				return false;
			}
		} else if (!source1.equals(other.source1))
		{
			return false;
		}
		if (source2 == null)
		{
			if (other.source2 != null)
			{
				return false;
			}
		} else if (!source2.equals(other.source2))
		{
			return false;
		}
		return true;
	}
}
