package driver;

import instructions.Instruction;

/**
 * A Node is a piece of a program graph. It can contain an instruction to
 * execute, as well as a specific Node that follows logically after it.
 *
 * @author Mark
 */
public class Node {

	protected long address;
	protected long lineNumber;
	protected Instruction instruction;
	protected Node next;

	public Node(long address, long lineNumber, Instruction instruction, Node node)
	{
		this.address = address;
		this.lineNumber = lineNumber;
		this.instruction = instruction;
		next = node;
	}

	public Node(long address, long lineNumber, Instruction instruction)
	{
		this.address = address;
		this.lineNumber = lineNumber;
		this.instruction = instruction;
	}

	public Node(long address, long lineNumber)
	{
		this.address = address;
		this.lineNumber = lineNumber;
	}
	
	public Node(long address, Instruction instruction, Node node)
	{
		this.address = address;
		this.lineNumber = 0;
		this.instruction = instruction;
		next = node;
	}

	public Node(long address, Instruction instruction)
	{
		this.address = address;
		this.lineNumber = 0;
		this.instruction = instruction;
	}

	public Node(long address)
	{
		this.address = address;
		this.lineNumber = 0;
	}

	public void executeInstruction(Driver driver)
	{
		if (instruction != null)
		{
			instruction.execute(driver);
		}
	}

	public long getAddress()
	{
		return address;
	}
	
	public long getLineNumber() {
		return lineNumber;
	}

	public Node getNext()
	{
		return next;
	}

	public void setNext(Node newNext)
	{
		next = newNext;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (address ^ (address >>> 32));
		result = prime * result + ((instruction == null) ? 0 : instruction.hashCode());
		result = prime * result + ((next == null) ? 0 : next.hashCode());
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
		Node other = (Node) obj;
		if (address != other.address)
		{
			return false;
		}
		if (instruction == null)
		{
			if (other.instruction != null)
			{
				return false;
			}
		} else if (!instruction.equals(other.instruction))
		{
			return false;
		}
		if (next == null)
		{
			if (other.next != null)
			{
				return false;
			}
		} else if (!next.equals(other.next))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Node [address=" + address + ", instruction=" + instruction + ", next=" + next + "]";
	}
}
