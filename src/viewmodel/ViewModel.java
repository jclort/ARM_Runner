package viewmodel;

import driver.Driver;
import driver.Register;
import driver.SystemState;
import driver.Word;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ViewModel {

	public BooleanProperty finished = new SimpleBooleanProperty();
	
	public LongProperty PC = new SimpleLongProperty();
	public LongProperty currentLine = new SimpleLongProperty();
	
	public ObservableList<Register> registers = FXCollections.observableArrayList();
	public ObservableList<Register> pc = FXCollections.observableArrayList(); //This is a list because I can't figure out a cleaner way to do it
	
	public ObservableList<Word> memory = FXCollections.observableArrayList();
	
	public BooleanProperty flagN = new SimpleBooleanProperty();
	public BooleanProperty flagZ = new SimpleBooleanProperty();
	public BooleanProperty flagC = new SimpleBooleanProperty();
	public BooleanProperty flagV = new SimpleBooleanProperty();
	
	public ViewModel(Driver driver) {
		Register[] regArr = driver.systemState.getRegisterArray();
		registers.addAll(regArr);
		pc.add(driver.systemState.PC);
		
		Word[] wordArr = driver.systemState.memoryManager.getMemoryArray();
		memory.addAll(wordArr);
	}
	
	public void updateFromState(Driver driver) {
		updateFinished(driver);
		updatePC(driver.systemState);
		updateLineNumber(driver);
		updateFlags(driver.systemState);
		
		//This is too slow, we may need to find a different way to handle memory "dirtiness"
		//updateMemory(driver.systemState);
	}
	
	public void updateFinished(Driver driver) {
		finished.setValue(driver.isFinished());
	}
	
	public void updatePC(SystemState state) {
		PC.setValue(state.PC.getValue());
	}
	
	public void updateLineNumber(Driver driver) {
		currentLine.setValue(driver.getCurrentLineNumber());
	}
	
	public void updateFlags(SystemState state) {
		flagN.setValue(state.getN());
		flagZ.setValue(state.getZ());
		flagC.setValue(state.getC());
		flagV.setValue(state.getV());
	}
}
