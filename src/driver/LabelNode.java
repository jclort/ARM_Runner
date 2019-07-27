package driver;

public class LabelNode extends Node {
	
	protected String name;

	public LabelNode(String name, long lineNumber, Node node) {
		// The instruction field and address field are not really used for labels
		super(0, lineNumber, null, node);
		this.name = name;
	}
	
	public LabelNode(String name, Node node) {
		// The instruction field and address field are not really used for labels
		super(0, 0, null, node);
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LabelNode other = (LabelNode) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
