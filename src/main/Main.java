package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import driver.Driver;
import driver.MemoryManager;
import driver.Node;
import driver.Register;
import driver.Word;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Parser;

public class Main extends Application {
	
	private static PrintWriter out;
	private File file;
	
	private Driver driver;
	
	private ListView<Register> registerListView;
	private ListView<Register> pcListView;
	private ListView<Word> memoryListView;
	
	private SimpleBooleanProperty validated = new SimpleBooleanProperty(false);
	private boolean hasBeenRun = false;
	private SimpleBooleanProperty keepRunning = new SimpleBooleanProperty(false);
	Task<Void> programStepper;
	Thread stepperThread;
	private double stepDelay = 250;
	private static final double STEP_DELAY_MIN = 0; //ms
	private static final double STEP_DELAY_MAX = 1500; //ms
	private static final double STEP_DELAY_HYPER_SPEED = 50;
	
	private static final double SPLASH_SCREEN_DELAY = 2.5; //s
	
	public static SimpleBooleanProperty decimalDisplay = new SimpleBooleanProperty(false);
	public static SimpleBooleanProperty hexDisplay = new SimpleBooleanProperty(true);
	
	TextArea program = new TextArea();
	private String programTextCache = "";
	
	private SimpleDoubleProperty maxProgramLines = new SimpleDoubleProperty(3);
	private int updateLines = (int) maxProgramLines.get() + 1;
	private SimpleDoubleProperty textHeight = new SimpleDoubleProperty();
	private SimpleDoubleProperty lineHeight = new SimpleDoubleProperty();
	VBox markers = new VBox();
	TextField[] lineNumberList = new TextField[5000];
	private TextField lastLine = null;
	double nextScroll = 0;
	
	TextArea console = new TextArea();
	ScrollPane scroll = new ScrollPane();
	
	Button run = new Button("Run");
	Button step = new Button("Step");
	
	VBox registers = new VBox();
	VBox memory = new VBox();
	
	public void showSplashScreen(Stage primaryStage, boolean temporary) {
		VBox splashRoot = new VBox();
		
		Stage splashStage = new Stage();
		splashStage.setTitle("ARM Runner - ARMx64");
		splashStage.getIcons().add(new Image("images/beefy_computer.png"));
		
		HBox row1 = new HBox();
		
		Image logo = new Image("images/beefy_computer.png");
		ImageView logoView = new ImageView(logo);
		
		logoView.setFitHeight(350);
		logoView.setFitWidth(350);
		
		Text creditsText = new Text(
				"University of Delaware\nCreated by:\n\nMark Redman\nMichael Gonzalez\nHabibulah Aslam\nJeffrey Lort\n\nwith Dr. Gregory Silber");
		
		row1.getChildren().addAll(logoView, creditsText);
		splashRoot.getChildren().add(row1);
		
		splashStage.setScene(new Scene(splashRoot, 650, 450));
		splashStage.show();
		
		if (temporary) {
			PauseTransition delay = new PauseTransition(Duration.seconds(SPLASH_SCREEN_DELAY));
			delay.setOnFinished(event -> {
				splashStage.close();
				showMainScreen(primaryStage);
			});
			delay.play();
		}
	}
	
	public void popUpSplashScreen() {
		Stage tempStage = new Stage();
		
		showSplashScreen(tempStage, false);
	}
	
	public void showMainScreen(Stage primaryStage) {
		int totalWidth = 1000;
		int registerWidth = 300;
		int memoryWidth = 270;
		int buttonSpaceWidth = 140;
		int buttonWidth = 80;
		int markerWidth = 50;
		int buttonPadWidth = (buttonSpaceWidth - buttonWidth) / 2;
		int programWidth = (totalWidth - buttonSpaceWidth) - (registerWidth + memoryWidth + markerWidth);
		int consoleHeight = (150);
		
		VBox root = new VBox();
		HBox top = new HBox();
		VBox controlPanel = new VBox();
		VBox buttons = new VBox();
		
		//program.setWrapText(true);
		
		lineNumberList[0] = new TextField();
		for (int i = 1; i <= maxProgramLines.get(); i++) {
			TextField t = new TextField(Integer.toString(i));
			t.setEditable(false);
			t.setAlignment(Pos.CENTER);
			t.prefHeightProperty().bind(lineHeight);
			t.setMinHeight(Region.USE_PREF_SIZE);
			t.setMaxHeight(Region.USE_PREF_SIZE);
			markers.getChildren().add(t);
			lineNumberList[i] = t;
			if (i < maxProgramLines.get()) {
				program.appendText("\n"); //prefills 3 lines in the textbox for aesthetic looks
			}
		}
		program.positionCaret(0); //returns carot to program start position
		
		FileChooser fileChooser = new FileChooser();
		
		Button load = new Button("Load");
		Button save = new Button("Save As");
		Button validate = new Button("Validate");
		Button stop = new Button("Stop");
		
		Text runSpeedLabel = new Text("Speed");
		Slider runSpeed = new Slider(STEP_DELAY_MIN, STEP_DELAY_MAX, 25);
		runSpeed.adjustValue(1250);
		
		runSpeed.valueProperty().addListener((ov, n, n1) -> {
			stepDelay = (STEP_DELAY_MAX - n1.doubleValue() + STEP_DELAY_MIN);
			if (stepDelay <= STEP_DELAY_HYPER_SPEED) {
				if (lastLine != null && keepRunning.get()) {
					lastLine.setStyle("-fx-background-color: cornsilk;");
				}
			}
		});
		
		RadioButton hexadecimal = new RadioButton("Hexadecimal");
		hexadecimal.selectedProperty().bindBidirectional(hexDisplay);
		hexadecimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Main.hexDisplay.set(true);
				Main.decimalDisplay.set(false);
				refreshViews();
			}
		});
		
		RadioButton decimal = new RadioButton("Decimal");
		decimal.selectedProperty().bindBidirectional(decimalDisplay);
		decimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Main.decimalDisplay.set(true);
				Main.hexDisplay.set(false);
				refreshViews();
			}
		});
		
		ToggleGroup mode = new ToggleGroup();
		hexadecimal.setToggleGroup(mode);
		decimal.setToggleGroup(mode);
		mode.selectToggle(hexadecimal);
		
		console.setId("ARM-console");
		program.setId("ARM-program");
		buttons.setId("ARM-buttons");
		top.setId("ARM-top");
		
		program.requestFocus();
		program.setOnKeyTyped(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent key) {
				if (hasBeenRun) {
					//Show a dialog asking for confirmation
					//If yes, invalidate
					
					//If no, don't invalidate, revert keyType event
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Edit Program Code");
					alert.setContentText("This will invalidate the current running code. Proceed?");
					
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						invalidateProgram();
						programTextCache = program.getText();
					}
					else {
						program.setText(programTextCache);
					}
				}
				else {
					invalidateProgram();
					programTextCache = program.getText();
				}
			}
		});
		
		console.setEditable(false);
		
		run.disableProperty().bind(validated.not().or(keepRunning));
		step.disableProperty().bind(validated.not());
		stop.disableProperty().bind(validated.not().or(keepRunning.not()));
		
		save.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if (file != null) {
					fileChooser.setInitialDirectory(file.getParentFile());
				}
				fileChooser.setTitle("Save Assembly Code");
				file = fileChooser.showSaveDialog(primaryStage);
				if (file != null) {
					try {
						
						if (!file.exists()) {
							file.createNewFile();
						}
						out = new PrintWriter(file);
						out.print(program.getText());
						out.close();
					} catch (FileNotFoundException e) {
						console.setText("file not found");
					} catch (IOException e) {
						console.setText("could not create file");
					}
				}
			}
		});
		
		load.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if (file != null) {
					fileChooser.setInitialDirectory(file.getParentFile());
				}
				fileChooser.setTitle("Open Assembly Code File");
				file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					try {
						program.setText(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));
						programTextCache = program.getText();
						fixLineNumbers();
						parse();
						
					} catch (FileNotFoundException e) {
						console.setText("file not found");
					} catch (IOException e) {
						console.setText("file not found");
					}
				}
				
			}
		});
		
		validate.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				parse();
			}
		});
		
		step.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if (driver != null) {
					driver.step();
					refreshViews();
				}
				hasBeenRun = true;
			}
		});
		
		run.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				parse();
				programStepper = new Task<Void>() {
					
					@Override
					protected Void call() throws Exception {
						while (!driver.isFinished() && keepRunning.get()) {
							if (stepDelay > 0) {
								try {
									Thread.sleep((long) stepDelay);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							// Re-evaluate keepRunning because it may have changed after the delay
							if (keepRunning.get()) {
								driver.step();
								if (driver.isFinished()) {
									program.setEditable(true);
									keepRunning.set(false);
								}
								refreshViews();
							}
						}
						return null;
					}
				};
				keepRunning.set(true);
				if (driver != null && (stepperThread == null || !stepperThread.isAlive())) {
					stepperThread = new Thread(programStepper);
					stepperThread.start();
				}
				if (stepDelay <= STEP_DELAY_HYPER_SPEED) {
					if (lastLine != null) {
						lastLine.setStyle("-fx-background-color: cornsilk;");
					}
				}
				program.setEditable(false);
				hasBeenRun = true;
			}
		});
		
		stop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				program.setEditable(true);
				keepRunning.set(false);
			}
		});
		
		HBox aboutButtonContainer = new HBox();
		
		Button aboutButton = new Button();
		aboutButton.setText("About");
		aboutButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				popUpSplashScreen();
			}
		});
		
		aboutButtonContainer.getChildren().addAll(aboutButton);
		
		controlPanel.getChildren().addAll(buttons, aboutButtonContainer);
		
		root.getChildren().addAll(top, console);
		scroll.setContent(markers);
		top.getChildren().addAll(controlPanel, scroll, program, registers, memory);
		buttons.getChildren().addAll(load, save, validate, step, run, stop, runSpeedLabel, runSpeed, hexadecimal,
				decimal);
		
		top.setPrefHeight(10000);
		console.setMinHeight(consoleHeight);
		console.setMaxHeight(consoleHeight);
		scroll.setPrefWidth(10000);
		
		markers.setMinWidth(markerWidth);
		markers.setMaxWidth(markerWidth);
		buttons.setPrefWidth(buttonSpaceWidth);
		buttons.setPrefHeight(Integer.MAX_VALUE);
		registers.setMinWidth(registerWidth);
		registers.setMaxWidth(registerWidth);
		memory.setMinWidth(memoryWidth);
		memory.setMaxWidth(memoryWidth);
		program.setMinWidth(programWidth);
		program.setPrefWidth(Integer.MAX_VALUE);
		buttons.setPadding(new Insets(buttonPadWidth));
		buttons.setSpacing(12);
		
		Scene scene = new Scene(root, totalWidth, 500);
		scene.getStylesheets().add(Main.class.getResource("main.css").toExternalForm());
		
		primaryStage.setTitle("ARM Runner - ARMx64");
		primaryStage.getIcons().add(new Image("images/beefy_computer.png"));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		textHeight.set(program.lookup(".text").getBoundsInLocal().getHeight());
		lineHeight.set(textHeight.get() / maxProgramLines.get());
		program.maxHeightProperty().bind(lineHeight.multiply(maxProgramLines.add(.5)));
		scroll.vmaxProperty().bind(textHeight.subtract(scroll.heightProperty()));
		scroll.vvalueProperty().bind(program.scrollTopProperty());
		maxProgramLines.bind(textHeight.divide(lineHeight));
		program.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent k) {
				fixLineNumbers();
			}
		});
		lineHeight.set(textHeight.get() / maxProgramLines.get());
		markers.setTranslateY(lineHeight.get() / 4);
		
		scroll.setMinWidth(ScrollBar.USE_PREF_SIZE);
		scroll.setMaxWidth(ScrollBar.USE_PREF_SIZE);
		scroll.setPrefWidth(markerWidth);
		scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		showSplashScreen(primaryStage, true);
	}
	
	private void fixLineNumbers() {
		textHeight.set(program.lookup(".text").getBoundsInLocal().getHeight());
		while (markers.getChildren().size() < maxProgramLines.get()) {
			addMarker();
			updateLines++;
		}
		while (markers.getChildren().size() > maxProgramLines.get()) {
			removeMarker();
			updateLines--;
		}
		if (program.getScrollTop() > scroll.getVmax()) {
			program.setScrollTop(scroll.getVmax());
		}
	}
	
	private void addMarker() {
		TextField t = new TextField("" + updateLines);
		t.setEditable(false);
		t.setAlignment(Pos.CENTER);
		t.prefHeightProperty().bind(lineHeight);
		t.setMaxHeight(TextField.USE_PREF_SIZE);
		t.setMinHeight(TextField.USE_PREF_SIZE);
		markers.getChildren().add(t);
		lineNumberList[updateLines] = t;
	}
	
	private void removeMarker() {
		markers.getChildren().remove(markers.getChildren().size() - 1);
	}
	
	private void setupRegistersPane(VBox registersPane) {
		Text header = new Text();
		
		registerListView = new ListView<Register>(driver.viewModel.registers);
		pcListView = new ListView<Register>(driver.viewModel.pc);
		HBox flagsContainer = new HBox();
		flagsContainer.setPadding(new Insets(0, 10, 0, 30));
		
		header.setText("Registers");
		header.minHeight(40);
		header.maxHeight(40);
		header.prefHeight(40);
		registerListView.setPrefHeight(900);
		pcListView.setPrefHeight(120);
		pcListView.setMinHeight(30);
		flagsContainer.setMinHeight(40);
		flagsContainer.setMaxHeight(40);
		flagsContainer.setPrefHeight(40);
		
		VBox nWrapper = new VBox();
		VBox zWrapper = new VBox();
		VBox cWrapper = new VBox();
		VBox vWrapper = new VBox();
		
		CheckBox N = new CheckBox("N");
		CheckBox Z = new CheckBox("Z");
		CheckBox C = new CheckBox("C");
		CheckBox V = new CheckBox("V");
		
		N.setPadding(new Insets(10));
		Z.setPadding(new Insets(10));
		C.setPadding(new Insets(10));
		V.setPadding(new Insets(10));
		
		Tooltip.install(nWrapper, new Tooltip("Negative:\nTrue if result was negative."));
		Tooltip.install(zWrapper, new Tooltip("Zero:\nTrue if result was zero."));
		Tooltip.install(cWrapper,
				new Tooltip("Carry (Unsigned overflow):\nTrue if result involved unsigned overflow."));
		Tooltip.install(vWrapper, new Tooltip("Overflow (signed):\nTrue if result involved signed overflow."));
		
		N.setDisable(true);
		Z.setDisable(true);
		C.setDisable(true);
		V.setDisable(true);
		N.setStyle("-fx-opacity: 1");
		Z.setStyle("-fx-opacity: 1");
		C.setStyle("-fx-opacity: 1");
		V.setStyle("-fx-opacity: 1");
		
		N.selectedProperty().bind(driver.viewModel.flagN);
		Z.selectedProperty().bind(driver.viewModel.flagZ);
		C.selectedProperty().bind(driver.viewModel.flagC);
		V.selectedProperty().bind(driver.viewModel.flagV);
		
		nWrapper.getChildren().add(N);
		zWrapper.getChildren().add(Z);
		cWrapper.getChildren().add(C);
		vWrapper.getChildren().add(V);
		flagsContainer.getChildren().addAll(nWrapper, zWrapper, cWrapper, vWrapper);
		
		registersPane.getChildren().clear();
		registersPane.getChildren().addAll(header, registerListView, pcListView, flagsContainer);
	}
	
	private void setupMemoryPane(VBox memoryPane) {
		VBox header = new VBox();
		memoryListView = new ListView<Word>(driver.viewModel.memory);
		memoryListView.setId("ARM-memory");
		
		Text headerLabel = new Text();
		
		headerLabel.setText("Memory");
		header.setMinHeight(74);
		header.setMaxHeight(74);
		header.setPrefHeight(40);
		
		TextField addressInput = new TextField();
		addressInput.setText("0x00000000");
		addressInput.setMaxWidth(150);
		
		VBox addressBox = new VBox();
		addressBox.setPadding(new Insets(0, 0, 0, 5));
		
		addressBox.getChildren().addAll(headerLabel, addressInput);
		
		HBox buttonContainer = new HBox();
		//buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(5);
		buttonContainer.setPadding(new Insets(5));
		Button goToAddress = new Button("Go to address");
		goToAddress.setPrefWidth(150);
		goToAddress.setMinWidth(150);
		goToAddress.setMaxWidth(150);
		goToAddress.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String addressText = addressInput.getText();
				long addr = 0;
				if (addressText.startsWith("x")) {
					// Register value mode
					int regIndex = Integer.parseInt(addressText.replaceAll("x", ""));
					if (regIndex >= 0 && regIndex < 32) {
						addr = driver.systemState.getValueAtRegister(regIndex);
					}
					
				}
				else if (addressText.startsWith("0x")) {
					// Hex mode
					addr = Long.decode(addressText);
				}
				else {
					// Decimal mode
					addr = Long.parseLong(addressText);
				}
				long wordNumber = addr / MemoryManager.WORD_SIZE;
				memoryListView.scrollTo((int) wordNumber);
				memoryListView.getSelectionModel().select((int) wordNumber);
			}
		});
		
		Button goToSP = new Button("Go to SP");
		goToSP.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				long addr = driver.systemState.getValueAtSP();
				long wordNumber = addr / MemoryManager.WORD_SIZE;
				memoryListView.scrollTo((int) wordNumber);
				memoryListView.getSelectionModel().select((int) wordNumber);
			}
		});
		
		HBox goToAddressContainer = new HBox();
		goToAddressContainer.setMinWidth(155);
		goToAddressContainer.setAlignment(Pos.CENTER_LEFT);
		goToAddressContainer.getChildren().add(goToAddress);
		
		HBox goToSPContainer = new HBox();
		goToSPContainer.setMinWidth(100);
		goToSPContainer.setAlignment(Pos.CENTER_RIGHT);
		goToSPContainer.getChildren().add(goToSP);
		
		buttonContainer.getChildren().addAll(goToAddressContainer, goToSPContainer);
		header.getChildren().addAll(addressBox, buttonContainer);
		
		memoryListView.setPrefHeight(1000);
		
		memoryPane.getChildren().clear();
		memoryPane.getChildren().addAll(header, memoryListView);
		memoryListView.scrollTo(memoryListView.getItems().size() - 1);
	}
	
	public void refreshViews() {
		if (registerListView != null) {
			registerListView.refresh();
		}
		if (memoryListView != null) {
			memoryListView.refresh();
		}
		if (pcListView != null) {
			pcListView.refresh();
		}
		
		// The UI starts to spaz out if we update this too quickly
		if (stepDelay > STEP_DELAY_HYPER_SPEED || !keepRunning.get()) {
			long currentLineNumber = driver.getCurrentLineNumber();
			if (lastLine != null) {
				lastLine.setStyle("-fx-background-color: cornsilk;");
			}
			lastLine = lineNumberList[(int) currentLineNumber];
			lineNumberList[(int) currentLineNumber]
					.setStyle("-fx-background-color: linear-gradient(to right, lawngreen, forestgreen);");
		}
	}
	
	private void parse() {
		//Halt any running code
		keepRunning.set(false);
		hasBeenRun = false;
		console.setText("parsing code...");
		
		Parser parser = new Parser();
		Node startNode = parser.createProgramGraph(program.getText());
		driver = new Driver(startNode);
		if (parser.badLines.isEmpty()) {
			//driver = new Driver(startNode);
			driver.start();
			setupRegistersPane(registers);
			setupMemoryPane(memory);
			
			console.setText("Program successfully parsed as valid ARM.");
			//refreshViews();
			validated.set(true);
		}
		else {
			// There were errors in the parsing, report them and don't allow running
			console.setText("There were errors on lines: " + parser.badLines.toString());
			step.setDefaultButton(true);
			//markBadLines(parser);
			validated.set(false);
		}
		console.appendText(parser.error);
		markBadLines(parser);
		refreshViews();
	}
	
	private void invalidateProgram() {
		driver = null;
		keepRunning.set(false);
		hasBeenRun = false;
		registers.getChildren().clear();
		memory.getChildren().clear();
		
		if (lastLine != null) {
			lastLine.setStyle("-fx-background-color: cornsilk;");
		}
		
		// Set this for edge cases
		program.setEditable(true);
		validated.set(false);
	}
	
	private void markBadLines(Parser parser) {
		for (int i = 0; i < maxProgramLines.get(); i++) {
			if (parser.badLines.contains(new Integer(i))) {
				lineNumberList[i + 1].setStyle("-fx-background-color: linear-gradient(to right, orange, firebrick);");
			}
			else {
				lineNumberList[i + 1].setStyle("-fx-background-color: cornsilk");
			}
		}
	}
	
	@Override
	public void stop() {
		//This makes sure that the auto-stepper thread will stop on its own
		// if we close the window
		keepRunning.set(false);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void startMe(String[] args) {
		launch(args);
	}
	
}
