package CASL.GameBuilder;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;

import CASL.MapBuilder.MapEditFrame;
import CASL.MapBuilder.MapFileFilter;
import CASL.MapBuilder.MessageFrame;
import CASL.Scenario.Game;
import CASL.Scenario.Scenario;
import CASL.Unit.Counter;

/**
 * Title:        GameEditFrame.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class GameEditFrame extends JFrame  {

	// message frame
	MessageFrame messageFrame;

	// directories
	private String	homeDirectory;
	private String	scenarioDirectory;
	private String	mapDirectory;

	// combo boxes
	JComboBox nationalityComboBox = new JComboBox(Counter.nationalityNames);
	String[]  counterTypeList = {"MMC", "SMC", "Support Weapons", "Ordnance", "OBA", "Vehicles", "Fortifications", "Other"};
	JComboBox counterTypeComboBox = new JComboBox(counterTypeList);
	JComboBox counterComboBox = new JComboBox();

	// define the menu
	JMenuBar menuBar1 = new JMenuBar();
	JMenu menuFile = new JMenu();
	JMenuItem menuFileExit = new JMenuItem();
	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpAbout = new JMenuItem();
	JMenuItem menuFileClose = new JMenuItem();
	JMenuItem menuFileOpen = new JMenuItem();
	JMenuItem menuFileNew = new JMenuItem();
	JMenuItem menuFileSave = new JMenuItem();
	JMenuItem menuFileSaveAs = new JMenuItem();
	JMenuItem menuFilePrint = new JMenuItem();

	JToolBar toolBar = new JToolBar();
	JButton openButton = new JButton();
	JButton newButton = new JButton();
	JButton saveButton = new JButton();

	JLabel statusBar = new JLabel();
	BorderLayout borderLayout1 = new BorderLayout();
	JScrollPane jScrollPane1 = new JScrollPane();
	GameEditor gameEditor = new GameEditor();

	JPanel buttonBar = new JPanel();
	JPanel counterSelector = new JPanel();
	FlowLayout flowLayout3 = new FlowLayout();
	JLabel spacer = new JLabel();

	JButton scenarioSetupButton = new JButton();
	JLabel mapLabel = new JLabel();
	JButton mapButton = new JButton();
	JButton mapLaunchButton = new JButton();

	JButton counterInfoButton = new JButton();
	JButton addButton = new JButton();
	JButton removeButton = new JButton();

	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	FlowLayout flowLayout1 = new FlowLayout();

	//Construct the frame
  	public GameEditFrame() {

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Component initialization
  private void jbInit() throws Exception  {

		this.getContentPane().setLayout(borderLayout1);
		this.setEnabled(true);
		this.setTitle("GameBuilder");
		statusBar.setBorder(BorderFactory.createEtchedBorder());
		statusBar.setText(" ");

		// set up the menu
		menuFile.setMargin(new Insets(0, 0, 0, 0));
		menuFile.setText("File");
		menuFile.setFont(new java.awt.Font("Dialog", 0, 11));
		menuFileExit.setPreferredSize(new Dimension(100, 20));
		menuFileExit.setMnemonic('0');
		menuFileExit.setText("Exit");
		menuFileExit.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				fileExit(e);
			}
		});
		menuHelp.setMargin(new Insets(0, 0, 0, 0));
		menuHelp.setText("Help");
		menuHelp.setFont(new java.awt.Font("Dialog", 0, 11));
		menuHelpAbout.setText("About");
		menuHelpAbout.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				helpAbout_actionPerformed(e);
			}
		});
		openButton.setIcon(new ImageIcon(Game.class.getResource("openFile.gif")));
		openButton.setMargin(new Insets(1, 1, 1, 1));
		openButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openScenario();
			}
		});
		openButton.setMaximumSize(new Dimension(30, 30));
		openButton.setMinimumSize(new Dimension(30, 30));
		openButton.setPreferredSize(new Dimension(30, 30));
		openButton.setRequestFocusEnabled(false);
		openButton.setToolTipText("Open scenario");

		newButton.setMargin(new Insets(2, 2, 2, 2));
		newButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showNewScenarioDialog();
			}
		});
		newButton.setMaximumSize(new Dimension(30, 30));
		newButton.setMinimumSize(new Dimension(30, 30));
		newButton.setPreferredSize(new Dimension(30, 30));
		newButton.setRequestFocusEnabled(false);
		newButton.setToolTipText("New scenario");
		newButton.setIcon(new ImageIcon(Game.class.getResource("newFile.GIF")));

		toolBar.setAlignmentY((float) 0.5);
		toolBar.setFloatable(false);

		jScrollPane1.setMinimumSize(new Dimension(100, 100));
		jScrollPane1.setPreferredSize(new Dimension(200, 200));
		saveButton.setToolTipText("Save scenario");
		saveButton.setMinimumSize(new Dimension(30, 30));
		saveButton.setPreferredSize(new Dimension(30, 30));
		saveButton.setRequestFocusEnabled(false);
		saveButton.setEnabled(false);
		saveButton.setMaximumSize(new Dimension(30, 30));
		saveButton.setIcon(new ImageIcon(Game.class.getResource("saveFile.GIF")));
		saveButton.setMargin(new Insets(2, 2, 2, 2));
		saveButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveScenario();
			}
		});
		menuFileClose.setPreferredSize(new Dimension(100, 20));
		menuFileClose.setEnabled(false);
		menuFileClose.setMnemonic('1');
		menuFileClose.setText("Close");
		menuFileClose.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				closeScenario();
			}
		});
		menuFileOpen.setPreferredSize(new Dimension(100, 20));
		menuFileOpen.setMnemonic('1');
		menuFileOpen.setText("Open...");
		menuFileOpen.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openScenario();
			}
		});
		menuFileNew.setPreferredSize(new Dimension(100, 20));
		menuFileNew.setMnemonic('1');
		menuFileNew.setText("New...");
		menuFileNew.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showNewScenarioDialog();
			}
		});
		menuFileSave.setPreferredSize(new Dimension(100, 20));
		menuFileSave.setEnabled(false);
		menuFileSave.setMnemonic('0');
		menuFileSave.setText("Save");
		menuFileSave.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveScenario();
			}
		});
		menuFileSaveAs.setPreferredSize(new Dimension(100, 20));
		menuFileSaveAs.setEnabled(false);
		menuFileSaveAs.setMnemonic('0');
		menuFileSaveAs.setText("Save as...");
		menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveScenarioAs();
			}
		});
		menuFilePrint.setPreferredSize(new Dimension(100, 20));
		menuFilePrint.setEnabled(false);
		menuFilePrint.setMnemonic('0');
		menuFilePrint.setText("Print");
		buttonBar.setLayout(flowLayout1);

		counterSelector.setBorder(BorderFactory.createEtchedBorder());
		counterSelector.setMaximumSize(new Dimension(1000, 1000));
		counterSelector.setMinimumSize(new Dimension(100, 36));
		counterSelector.setPreferredSize(new Dimension(800, 36));
		counterSelector.setLayout(flowLayout3);

		nationalityComboBox.setPreferredSize(new Dimension(120, 20));
		nationalityComboBox.setMaximumRowCount(5);
		nationalityComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				nationalityComboBox(e);
			}
		});
		nationalityComboBox.setMinimumSize(new Dimension(120, 20));
		nationalityComboBox.setEnabled(true);
		nationalityComboBox.setMaximumSize(new Dimension(120, 20));

		counterTypeComboBox.setEnabled(true);
		counterTypeComboBox.setMaximumSize(new Dimension(150, 20));
		counterTypeComboBox.setMinimumSize(new Dimension(150, 20));
		counterTypeComboBox.setPreferredSize(new Dimension(150, 20));
		counterTypeComboBox.setMaximumRowCount(5);
		counterTypeComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				counterTypeComboBox(e);
			}
		});
		counterComboBox.setEnabled(true);
		counterComboBox.setMaximumSize(new Dimension(200, 20));
		counterComboBox.setMinimumSize(new Dimension(200, 20));
		counterComboBox.setPreferredSize(new Dimension(200, 20));
		counterComboBox.setMaximumRowCount(5);
		counterComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				counterComboBox(e);
			}
		});
		counterComboBox.addItem("Some counter");

		// initialize the game editor
		gameEditor.setMinimumSize(new Dimension(100, 100));
		gameEditor.setFrame(this);

		// load the counters
		System.out.println("Loading the MMCs...");
		gameEditor.loadMMCUnits();
		gameEditor.initializeMMCUnits();

		System.out.println("Loading the SMCs...");
		gameEditor.loadSMCUnits();
		gameEditor.initializeSMCUnits();

		System.out.println("Loading the SWs...");
		gameEditor.loadSW();
		gameEditor.initializeSW();

		System.out.println("Loading the fortifications...");
		gameEditor.loadFortifications();
		gameEditor.initializeFortifications();

		loadCounterComboBox();

		buttonBar.setMaximumSize(new Dimension(1000, 1000));
		buttonBar.setMinimumSize(new Dimension(100, 70));
		buttonBar.setPreferredSize(new Dimension(700, 70));
		flowLayout3.setAlignment(FlowLayout.LEFT);
		flowLayout3.setHgap(2);
		flowLayout3.setVgap(2);

		spacer.setAlignmentX((float) 0.5);
		spacer.setAlignmentY((float) 0.0);
		spacer.setOpaque(true);
		spacer.setRequestFocusEnabled(false);
		spacer.setIconTextGap(0);
		spacer.setText("  ");

		counterInfoButton.setMinimumSize(new Dimension(25, 25));
		counterInfoButton.setFocusPainted(false);
		counterInfoButton.setIcon(new ImageIcon(Game.class.getResource("info.GIF")));
		counterInfoButton.setMaximumSize(new Dimension(25, 25));
		counterInfoButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				counterInfoButton(e);
			}
		});
		counterInfoButton.setPreferredSize(new Dimension(30, 30));
		counterInfoButton.setRequestFocusEnabled(false);

		addButton.setMinimumSize(new Dimension(30, 30));
		addButton.setFocusPainted(false);
		addButton.setIcon(new ImageIcon(Game.class.getResource("add.GIF")));
		addButton.setMaximumSize(new Dimension(30, 30));
		addButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addButton();
			}
		});
		addButton.setPreferredSize(new Dimension(30, 30));
		addButton.setRequestFocusEnabled(false);
		addButton.setEnabled(false);

		removeButton.setMinimumSize(new Dimension(30, 30));
		removeButton.setFocusPainted(false);
		removeButton.setIcon(new ImageIcon(Game.class.getResource("remove.GIF")));
		removeButton.setMaximumSize(new Dimension(30, 30));
		removeButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeButton();
			}
		});
		removeButton.setPreferredSize(new Dimension(30, 30));
		removeButton.setRequestFocusEnabled(false);
		removeButton.setEnabled(false);

		scenarioSetupButton.setPreferredSize(new Dimension(30, 30));
		scenarioSetupButton.setIcon(new ImageIcon(Game.class.getResource("setup.GIF")));
		scenarioSetupButton.setToolTipText("Scenario setup dialog");
		scenarioSetupButton.setRequestFocusEnabled(false);
		scenarioSetupButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showNewScenarioDialog();
			}
		});
		scenarioSetupButton.setEnabled(false);
		scenarioSetupButton.setMaximumSize(new Dimension(30, 30));
		scenarioSetupButton.setMinimumSize(new Dimension(30, 30));

		mapLabel.setOpaque(true);
		mapLabel.setRequestFocusEnabled(false);
		mapLabel.setIconTextGap(0);
		mapLabel.setText("  <None>");
		mapLabel.setPreferredSize(new Dimension(200, 25));

		mapButton.setPreferredSize(new Dimension(30, 30));
		mapButton.setToolTipText("Select map");
		mapButton.setRequestFocusEnabled(false);
		mapButton.setIcon(new ImageIcon(Game.class.getResource("map.GIF")));
		mapButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openMap();
			}
		});
		mapButton.setEnabled(false);
		mapButton.setMaximumSize(new Dimension(30, 30));
		mapButton.setMinimumSize(new Dimension(30, 30));

		mapLaunchButton.setPreferredSize(new Dimension(30, 30));
		mapLaunchButton.setToolTipText("Start MapBuilder");
		mapLaunchButton.setRequestFocusEnabled(false);
		mapLaunchButton.setEnabled(false);
		mapLaunchButton.setIcon(new ImageIcon(Game.class.getResource("editMap.GIF")));
		mapLaunchButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				launchMapBuilder();
			}
		});
		mapLaunchButton.setEnabled(false);
		mapLaunchButton.setMaximumSize(new Dimension(30, 30));
		mapLaunchButton.setMinimumSize(new Dimension(30, 30));

		jLabel1.setText("Nationality:");
		jLabel2.setText("   Counter Type: ");
		jLabel3.setText("   Counter: ");
		flowLayout1.setAlignment(FlowLayout.LEFT);
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);

		menuFile.add(menuFileNew);
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileClose);
		menuFile.addSeparator();
		menuFile.add(menuFileSave);
		menuFile.add(menuFileSaveAs);
		menuFile.addSeparator();
		menuFile.add(menuFilePrint);
		menuFile.addSeparator();
		menuFile.add(menuFileExit);
		menuHelp.add(menuHelpAbout);
		menuBar1.add(menuFile);
		menuBar1.add(menuHelp);

		setJMenuBar(menuBar1);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		getContentPane().add(buttonBar, BorderLayout.NORTH);

		buttonBar.add(toolBar, null);
		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveButton, null);
		toolBar.add(spacer, null);

		buttonBar.add(spacer, null);
		buttonBar.add(scenarioSetupButton, null);

		buttonBar.add(mapButton, null);
		buttonBar.add(mapLabel, null);
		buttonBar.add(mapLaunchButton, null);
		buttonBar.add(counterSelector, null);

		counterSelector.add(jLabel1, null);
		counterSelector.add(nationalityComboBox, null);
		counterSelector.add(jLabel2, null);
		counterSelector.add(counterTypeComboBox, null);
		counterSelector.add(jLabel3, null);
		counterSelector.add(counterComboBox, null);
		counterSelector.add(counterInfoButton, null);
		counterSelector.add(addButton, null);
		counterSelector.add(removeButton, null);
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(gameEditor, null);

		// set the directories
		int loc = System.getProperty("java.class.path",".").indexOf(';');
		if (loc != -1){

			homeDirectory = System.getProperty("java.class.path",".").substring(0, loc) + System.getProperty("file.separator","\\") + "GameBuilder" ;
			scenarioDirectory = 	System.getProperty("java.class.path",".").substring(0, loc) +
							System.getProperty("file.separator","\\") + "CASL" +
							System.getProperty("file.separator","\\") + "Scenarios" ;
			mapDirectory = 		System.getProperty("java.class.path",".").substring(0, loc) +
							System.getProperty("file.separator","\\") + "CASL" +
							System.getProperty("file.separator","\\") + "Maps" ;
		}
		else {

			homeDirectory = System.getProperty("java.class.path",".") + System.getProperty("file.separator","\\") + "GameBuilder" ;
			scenarioDirectory = 	System.getProperty("java.class.path",".") +
							System.getProperty("file.separator","\\") + "CASL" +
							System.getProperty("file.separator","\\") + "Scenarios" ;
			mapDirectory = 		System.getProperty("java.class.path",".") +
							System.getProperty("file.separator","\\") + "CASL" +
							System.getProperty("file.separator","\\") + "Maps" ;
		}

		// set the status bar
		setStatusBarText("  ");
	}

	//Help | About action performed
	public void helpAbout_actionPerformed(ActionEvent e) {
		GameEditFrame_AboutBox dlg = new GameEditFrame_AboutBox(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.show();
	}

	//Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);

		if(e.getID() == WindowEvent.WINDOW_CLOSING) {
			fileExit(null);
		}
	}

	//File | Exit action performed
	public void fileExit(ActionEvent e) {
		// Scenario changed?
		if (gameEditor.isScenarioChanged()) {

			int response = this.AskYesNo("Save changes?");
			if (response == JOptionPane.YES_OPTION){

				saveScenario();
				closeScenarioVariables();
				System.exit(0);
			}
			else if (response == JOptionPane.NO_OPTION){

				System.exit(0);
			}
			else if (response == JOptionPane.CANCEL_OPTION){

				System.exit(0);
			}
		}
		System.exit(0);
	}

	public void setMessageFrame(MessageFrame frame){

		messageFrame = frame;
	}

	public void addMessage(String message){

		messageFrame.addMessage(message);

	}

 	public void showScenarioDialog(String function){

   		ScenarioDialog dialog = null;

		// set the function
		if (function.equals("Open")){
			dialog = new ScenarioDialog(this, "Open scenario", true);
		}
		else if (function.equals("Save as")){
			dialog = new ScenarioDialog(this, "Save scenario as...", true);
		}
		else {
			return;
		}

		//Center the dialog box
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		dialog.show();

	}

 	public void showNewScenarioDialog(){

		NewScenarioDialog dialog;

		Scenario s = gameEditor.getScenario();

		if (s == null) {
	   		dialog = new NewScenarioDialog(this, "Create a new scenario", true);
		}
		else {

	   		dialog = new NewScenarioDialog(
				this,
				"Create a new scenario",
				true,
				s.getName(),
				s.getTheater(),
				s.getEC(),
				s.getDate(),
				s.getTurns(),
				s.hasHalfTurn(),
				s.getAxisDefaultELR(),
				s.getAlliedDefaultELR(),
				s.getAxisSAN(),
				s.getAlliedSAN()
			);
		}

		//Center the dialog box
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		if (frameSize.height > screenSize.height)	frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)  	frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		dialog.show();

	}

	public void newScenario(
		String 	scenarioName,
		int 		theater,
		int 		EC,
		Date 		date,
		int		turns,
		boolean	halfTurn,
		int		axisELR,
		int		alliedELR,
		int		axisSAN,
		int		alliedSAN) {

		// abort if user chooses not to close the scenario
		if (!closeScenario()) return;

		// create and initialize the new scenario
		gameEditor.createNewScenario(scenarioName);
		initializeScenario(
			scenarioName,
			theater,
			EC,
			date,
			turns,
			halfTurn,
			axisELR,
			alliedELR,
			axisSAN,
			alliedSAN
		);

		// enable menus/buttons
		saveButton.setEnabled(true);
		menuFileClose.setEnabled(true);
		menuFileSave.setEnabled(true);
		menuFileSaveAs.setEnabled(true);
		addButton.setEnabled(true);
		removeButton.setEnabled(true);
		scenarioSetupButton.setEnabled(true);
		mapButton.setEnabled(true);
		mapLaunchButton.setEnabled(true);
		validate();

		// make the scenario the active component
		gameEditor.requestFocus();

		// update the window title
		setTitle("GameBuilder - " + scenarioName);
	}

	public void initializeScenario(
		String 	scenarioName,
		int 		theater,
		int 		EC,
		Date 		date,
		int		turns,
		boolean	halfTurn,
		int		axisELR,
		int		alliedELR,
		int		axisSAN,
		int		alliedSAN) {


		// set the initial variables
		Scenario s = gameEditor.getScenario();
		s.setName(scenarioName);
		s.setTheater(theater);
		s.setEC(EC);
		s.setDate(date);
		s.setTurns(turns);
		s.setHalfTurn(halfTurn);
		s.setAxisDefaultELR(axisELR);
		s.setAlliedDefaultELR(alliedELR);
		s.setAxisSAN(axisSAN);
		s.setAlliedSAN(alliedSAN);

	}

	public void saveScenarioAs() {

		// create the file filter
		ScenarioFileFilter filter = new ScenarioFileFilter("scn", "Scenario files");
		filter.addExtension("scn");
		filter.setDescription("Scenario files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(scenarioDirectory);
		fileChooser.setFileFilter(filter);
		int selected = fileChooser.showSaveDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			//if the filename doesn't contains a '.', add '.scn' to it
			if (fileChooser.getSelectedFile().getName().indexOf('.') == -1) {

				gameEditor.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName() + ".scn");
			}
			else{
				gameEditor.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());
			}

			gameEditor.saveScenario();
		}
	}

	public void saveScenario() {

		if (!gameEditor.isScenarioOpen()) return;

		if(!(gameEditor.getFileName() == null || gameEditor.getFileName().equals(""))){
			gameEditor.saveScenario();
		}
		else {

			// create the file filter
			ScenarioFileFilter filter = new ScenarioFileFilter("scn", "Scenario files");
			filter.addExtension("scn");
			filter.setDescription("Scenario files");

			// show the file chooser
			JFileChooser fileChooser = new JFileChooser(scenarioDirectory);
			fileChooser.setFileFilter(filter);
			int selected = fileChooser.showSaveDialog(this);

			if(selected == JFileChooser.APPROVE_OPTION){

				//if the filename doesn't contains a '.', add '.scn' to it
				if (fileChooser.getSelectedFile().getName().indexOf('.') == -1) {

					gameEditor.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName() + ".scn");
				}
				else{
					gameEditor.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());
				}

				gameEditor.saveScenario();
			}
		}
	}

	public void openScenario() {

		// abort if user chooses not to close the scenario
		if (!closeScenario()) return;

		// create the file filter
		ScenarioFileFilter filter = new ScenarioFileFilter("scn", "Scenario files");
		filter.addExtension("scn");
		filter.setDescription("Scenario files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(scenarioDirectory);
		fileChooser.setFileFilter(filter);
   		int selected = fileChooser.showOpenDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			gameEditor.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());

			// trap errors
			try{
				setStatusBarText("Opening scenario...");
				gameEditor.openScenario();
			} catch (Exception e){

				System.out.println("Cannot open the scenario: " + gameEditor.getFileName());
				gameEditor.setFileName(null);
				return;
			}

			// enable menus/buttons
			if (gameEditor.isScenarioOpen()){
				saveButton.setEnabled(true);
				scenarioSetupButton.setEnabled(true);
				mapButton.setEnabled(true);
				mapLaunchButton.setEnabled(true);
				menuFileClose.setEnabled(true);
				menuFileSave.setEnabled(true);
				menuFileSaveAs.setEnabled(true);
				addButton.setEnabled(true);
				removeButton.setEnabled(true);
				mapLabel.setText("  " + gameEditor.getScenario().getMapName());

				// make the scenario the active component
				gameEditor.requestFocus();

				// update the window title
				setTitle("GameBuilder - " + gameEditor.getScenario().getName());
			}
		}
	}

	public boolean closeScenario() {

		// Scenario changed?
		if (gameEditor.isScenarioChanged()) {

			int response = this.AskYesNo("Save changes?");
			if (response == JOptionPane.YES_OPTION){

				saveScenario();
				closeScenarioVariables();
				return true;
			}
			else if (response == JOptionPane.NO_OPTION){

				closeScenarioVariables();
				return true;
			}
			else if (response == JOptionPane.CANCEL_OPTION){

				return false;
			}
		}
		else {
			closeScenarioVariables();
			return true;
		}
		return false;
	}

	private void closeScenarioVariables(){

		// disable menus/buttons
		saveButton.setEnabled(false);
		scenarioSetupButton.setEnabled(false);
		mapButton.setEnabled(false);
		mapLaunchButton.setEnabled(false);
		mapLabel.setText("  <None>");
		menuFileClose.setEnabled(false);
		menuFileSave.setEnabled(false);
		menuFileSaveAs.setEnabled(false);
		addButton.setEnabled(false);
		removeButton.setEnabled(false);

		// reset the scenario
		gameEditor.closeScenario();

		// reset the title
		setTitle("GameBuilder");
	}

	public void openMap() {

		// create the file filter
		MapFileFilter filter = new MapFileFilter("map", "Map files");
		filter.addExtension("map");
		filter.setDescription("Map files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory + System.getProperty("file.separator","\\") + "Maps");
		fileChooser.setFileFilter(filter);
   		int selected = fileChooser.showOpenDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			gameEditor.getScenario().setMapName(fileChooser.getSelectedFile().getName());
			mapLabel.setText("  " + gameEditor.getScenario().getMapName());
		}
	}

	void setStatusBarText(String s){

		statusBar.setText(s);
	}

	void zoomComboBox(ItemEvent e) {


	}

	void brushshapeButton(ChangeEvent e) {

	}

	void pointerButton(ChangeEvent e) {

	}

	void createNewScenario(int width, int height){

		// enable menus/buttons
		saveButton.setEnabled(true);
		scenarioSetupButton.setEnabled(true);
		mapButton.setEnabled(true);
		mapLaunchButton.setEnabled(true);
		menuFileClose.setEnabled(true);
		menuFileSave.setEnabled(true);
		menuFileSaveAs.setEnabled(true);
		validate();

		// make the scenario the active component
		gameEditor.requestFocus();


	}

	void counterInfoButton(ActionEvent e) {

		ShowCurrentCounterInformation();
	}

	public void ShowCurrentCounterInformation(){

		CounterInfoFrame infoFrame = new CounterInfoFrame(gameEditor.getCurrentCounter(), CounterInfoFrame.ALL_INFORMATION, gameEditor.getCheckMarkImage());
	}

	void addButton() {

		if (gameEditor != null) gameEditor.addCurrentCounter();
	}

	void removeButton() {

		if (gameEditor != null) gameEditor.removeCurrentCounter();
	}

	void undoButton() {
	}

 	public int AskYesNo(String question){


		// get reponse
		return JOptionPane.showConfirmDialog(this, question);
	}


	public void paintImmediately(){

		statusBar.paintImmediately(0,0,statusBar.getWidth(), statusBar.getHeight());

	}

	void rotationTextField_keyReleased(KeyEvent e) {

	}

	void nationalityComboBox(ItemEvent e) {

		loadCounterComboBox();
	}

	void counterTypeComboBox(ItemEvent e) {

		loadCounterComboBox();
	}

	void loadCounterComboBox() {

		// clear what there
		counterComboBox.removeAllItems();
/*
		// fortifications
		if (counterTypeComboBox.getSelectedItem().equals("Fortifications")){


		}
		else {
*/
			// get new list and add to combo box
			ArrayList names = gameEditor.getCounterNameList(
				nationalityComboBox.getSelectedIndex(),
				(String) counterTypeComboBox.getSelectedItem());

			if (names.size() == 0) {
				counterComboBox.setEnabled(false);
				counterInfoButton.setEnabled(false);
				gameEditor.setCurrentCounter(null);
			}
			else {
				counterComboBox.setEnabled(true);
				counterInfoButton.setEnabled(true);

				Iterator iter = names.iterator();
				while (iter.hasNext()){

					counterComboBox.addItem((String) iter.next());
				}
			}
	}

	void counterComboBox(ItemEvent e) {

		Counter c = null;

		if (counterTypeComboBox.getSelectedItem().equals("MMC")){

			c = gameEditor.getMMC(nationalityComboBox.getSelectedIndex(), (String) counterComboBox.getSelectedItem());
		}
		else if (counterTypeComboBox.getSelectedItem().equals("SMC")){

			c = gameEditor.getSMC(nationalityComboBox.getSelectedIndex(), (String) counterComboBox.getSelectedItem());
		}
		else if (counterTypeComboBox.getSelectedItem().equals("Support Weapons")){

			c = gameEditor.getSW(nationalityComboBox.getSelectedIndex(), (String) counterComboBox.getSelectedItem());
		}
		else if (counterTypeComboBox.getSelectedItem().equals("Fortifications")){

			c = gameEditor.getFortification((String) counterComboBox.getSelectedItem());
		}
		else if (counterTypeComboBox.getSelectedItem().equals("Other")){

		}

		gameEditor.setCurrentCounter(c);
		gameEditor.repaint();
	}


	public void launchMapBuilder(){

		String mapName = gameEditor.getScenario().getMapName();
		if (mapName != null && !mapName.equals("")){

			MapEditFrame mp = new MapEditFrame(mapName);
			mp.setIsApplication(false);
		}
	}
}

