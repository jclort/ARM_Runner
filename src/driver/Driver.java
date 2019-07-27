package driver;

import util.Parser;
import viewmodel.ViewModel;

public class Driver {
	
	private Node startNode;
	private Node currentNode;
	private long currentLineNumber;
	
	public String toPrint;
	public SystemState systemState;
	public ViewModel viewModel;
	
	private boolean finished = false;
	
	public Driver(Node startNode) {
		this.startNode = startNode;
		currentNode = startNode;
	}
	
	public void start() {
		systemState = new SystemState();
		viewModel = new ViewModel(this);
		currentNode = startNode;
		currentLineNumber = currentNode.getLineNumber();
	}
		
	public void step() {
		systemState.memoryManager.step();
		if (!finished) {
			currentNode.executeInstruction(this);
			
			//currentNode = currentNode.getNext();
			systemState.PC.setValue(systemState.PC.getValue() + 4);
			currentNode = Parser.addressMap.get(systemState.PC.getValue());
			
			if (currentNode == null) {
				finished = true;
			}
			else {
				//systemState.PC.setValue(currentNode.getAddress());
				currentLineNumber = currentNode.getLineNumber();
			}
			
			viewModel.updateFromState(this);
		}
		
	}
	
	public void jumpToLabel(LabelNode destination) {
		//currentNode = destination;
		if(destination.getNext() == null) {
			finished = true; //TODO: set PC??!??
		}else {
			systemState.PC.setValue(destination.getNext().getAddress() - 4);
		}
	}
	
	public boolean isFinished() {
		return finished;
	}

	public long getCurrentLineNumber() {
		return currentLineNumber;
	}

}
