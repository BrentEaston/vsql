package CASL.GameBuilder;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Scrollable;

import CASL.Scenario.Game;
import CASL.Scenario.Scenario;
import CASL.Scenario.ScenarioGroup;
import CASL.Unit.Counter;
import CASL.Unit.Fortification;
import CASL.Unit.Infantry;
import CASL.Unit.SMC;
import CASL.Unit.SW;

/**
 * Title:        GameEditor.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class 	GameEditor
	extends 	JPanel
	implements  MouseListener,
				MouseMotionListener,
				Scrollable,
				KeyListener
	{

	// status variables
	private boolean scenarioOpen		= false;

	// counter variables
	private Counter	currentCounter;
	private CounterInfoFrame infoFrame;

	// file variables
	private File 	file;
	private String	fileName;
	private String	homeDirectory;

	// the scenario
	public Scenario		scenario	= null;

	// counters
	public static int MAX_MMC_UNITS	= 500;
	public static int MAX_SMC_UNITS	= 200;
	public static int MAX_SW		= 200;
	public static int MAX_FORTIFICATIONS = 100;

	Infantry	MMCUnitsList[]	= new Infantry[MAX_MMC_UNITS];
	Image		MMCImagesList[]	= new Image[MAX_MMC_UNITS];

	SMC		SMCUnitsList[]	= new SMC[MAX_SMC_UNITS];
	Image		SMCImagesList[]	= new Image[MAX_SMC_UNITS];

	SW		SWList[]		= new SW[MAX_SW];
	Image		SWImagesList[]	= new Image[MAX_SW];

	Fortification	fortificationList[]		= new Fortification[MAX_FORTIFICATIONS];
	Image			fortificationImagesList[]	= new Image[MAX_FORTIFICATIONS];

	// window stuff
	private GameEditFrame 	frame;
	private Dimension	   dim;

	// graphics stuff
	Image 	checkMark;
	boolean	showCounterInfo 	= false;
	int		SCREEN_WIDTH	= 800;

	GridBagLayout mainGridBag = new GridBagLayout();

	JScrollPane 	axisScrollPane = new JScrollPane();
	JPanel 			axisPanel = new JPanel();
	GridBagLayout 	axisGridBag = new GridBagLayout();

	JScrollPane 	alliedScrollPane = new JScrollPane();
	JPanel 			alliedPanel = new JPanel();
	GridBagLayout	alliedGridBag = new GridBagLayout();

	JScrollPane	preamblePanel = new JScrollPane();
	JTextArea 	preambleTextArea = new JTextArea(5, 115);

	JLabel 	VCLabel = new JLabel();
	JTextField 	VCTextField = new JTextField();
	JLabel 	balanceLabel = new JLabel();
	JTextField 	balanceTextField = new JTextField();

	JScrollPane 	aftermathPanel = new JScrollPane();
	JTextArea 	aftermathTextArea = new JTextArea(4, 50);
	JTextArea 	SSRTextArea = new JTextArea(4, 50);

	public GameEditor() {

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setFrame(GameEditFrame frame) {this.frame = frame;}
	public GameEditFrame getFrame() {return frame;}

	//Component initialization
	private void jbInit() throws Exception  {

		// set the home directory
		int loc = System.getProperty("java.class.path",".").indexOf(';');
		if (loc != -1){

			homeDirectory =
				System.getProperty("java.class.path",".").substring(0, loc) +
				System.getProperty("file.separator","\\") + "CASL" +
				System.getProperty("file.separator","\\") + "GameBuilder" ;
		}
		else {

			homeDirectory = System.getProperty("java.class.path",".") +
				System.getProperty("file.separator","\\") + "CASL" +
				System.getProperty("file.separator","\\") + "GameBuilder";
		}

		this.setMinimumSize(new Dimension(SCREEN_WIDTH, 1000));
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, 1000));
		this.setToolTipText("");
		this.setEnabled(true);
		adjustGameViewSize();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		// get other graphic files
		try  {
			checkMark = (new ImageIcon(Game.class.getResource("checkMark.GIF"))).getImage();
		}
		catch(Exception e) {
			System.out.println("Cannot find the image file: checkMark.GIF");
		}

		// setup the preamble panels/fields
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		fl.setHgap(2);
		fl.setVgap(2);
		JPanel VCPanel = new JPanel();
		VCPanel.setLayout(fl);
		VCPanel.setMinimumSize(new Dimension(SCREEN_WIDTH, 50));
		VCPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, 50));
		VCPanel.add(preambleTextArea, null);
		VCLabel.setText("Victoy conditions: ");
		balanceLabel.setText("Balance:           ");
		VCLabel.setPreferredSize(new Dimension(100, 25));
		VCTextField.setPreferredSize(new Dimension(695, 25));
		VCTextField.setMinimumSize(new Dimension(675, 25));
		VCTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				changeVC(e);
			}
		});
		balanceLabel.setPreferredSize(new Dimension(100, 25));
		balanceTextField.setPreferredSize(new Dimension(695, 25));
		balanceTextField.setMinimumSize(new Dimension(675, 25));
		balanceTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				changeBalance(e);
			}
		});

		preamblePanel.setMinimumSize(new Dimension(SCREEN_WIDTH, 75));
		preamblePanel.setPreferredSize(new Dimension(SCREEN_WIDTH, 100));
		preambleTextArea.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				changePreamble(e);
			}
		});

		VCPanel.add(VCLabel, null);
		VCPanel.add(VCTextField, null);
		VCPanel.add(balanceLabel, null);
		VCPanel.add(balanceTextField, null);
		preamblePanel.getViewport().add(preambleTextArea);

		// set up the aftermath panel
		aftermathPanel.setMinimumSize(new Dimension(SCREEN_WIDTH, 100));
		aftermathPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, 150));

		JPanel p = new JPanel();
		p.setMinimumSize(new Dimension(SCREEN_WIDTH, 200));
		p.setPreferredSize(new Dimension(SCREEN_WIDTH, 500));
		GridLayout  gl = new GridLayout(1, 2, 4, 2);
		p.setLayout(gl);

		aftermathTextArea.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				changeAftermath(e);
			}
		});
		SSRTextArea.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				changeSSR(e);
			}
		});

		// set the wrap policy
		preambleTextArea.setLineWrap(true);
		SSRTextArea.setLineWrap(true);
		aftermathTextArea.setLineWrap(true);
		preambleTextArea.setWrapStyleWord(true);
		SSRTextArea.setWrapStyleWord(true);
		aftermathTextArea.setWrapStyleWord(true);

		// set the font
		Font f = new Font(aftermathTextArea.getFont().getFontName(), aftermathTextArea.getFont().getStyle(), 11);
		preambleTextArea.setFont(f);
		aftermathTextArea.setFont(f);
		SSRTextArea.setFont(f);
		balanceTextField.setFont(f);
		VCTextField.setFont(f);

		// start with text areas disabled
		preambleTextArea.setEnabled(false);
		SSRTextArea.setEnabled(false);
		aftermathTextArea.setEnabled(false);
		VCTextField.setEnabled(false);
		balanceTextField.setEnabled(false);

		p.add(aftermathTextArea, null);
		p.add(SSRTextArea, null);
		aftermathPanel.getViewport().add(p, null);

		alliedScrollPane.setMinimumSize(new Dimension(SCREEN_WIDTH, 220));
		alliedScrollPane.setPreferredSize(new Dimension(SCREEN_WIDTH, 350));
		alliedScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		alliedScrollPane.getHorizontalScrollBar().setUnitIncrement(15);
		axisScrollPane.setMinimumSize(new Dimension(SCREEN_WIDTH, 220));
		axisScrollPane.setPreferredSize(new Dimension(SCREEN_WIDTH, 350));
		axisScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		axisScrollPane.getHorizontalScrollBar().setUnitIncrement(15);

		axisPanel.setLayout(axisGridBag);
		alliedPanel.setLayout(alliedGridBag);
		axisScrollPane.getViewport().add(axisPanel, null);
		alliedScrollPane.getViewport().add(alliedPanel, null);

		// set up the main grid bag
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 4;
		c.fill = GridBagConstraints.BOTH;

		setLayout(mainGridBag);
		mainGridBag.setConstraints(preamblePanel, c);
		add(preamblePanel, null);
		mainGridBag.setConstraints(VCPanel, c);
		add(VCPanel, null);
		mainGridBag.setConstraints(axisScrollPane, c);
		add(axisScrollPane, null);
		mainGridBag.setConstraints(alliedScrollPane, c);
		add(alliedScrollPane, null);
		mainGridBag.setConstraints(aftermathPanel, c);
		add(aftermathPanel, null);

	}

	public void 	setFileName(String newFileName)	{fileName = newFileName;}
	public String	getFileName()					{return fileName;}
	public boolean	isScenarioOpen()						{return scenarioOpen;}
	public Scenario	getScenario()					{return scenario;}
	public boolean	isScenarioChanged(){

		if (scenario != null) {

			return scenario.hasChanged();
		}
		else return false;
	}

	public void adjustGameViewSize() {

		// adjust window to game size
		if (scenario == null) {

			this.setPreferredSize(new Dimension(SCREEN_WIDTH, 700));
			this.revalidate();
		}
		else {
			this.setPreferredSize(new Dimension(SCREEN_WIDTH, 700));
			this.revalidate();
		}
	}


/*	public void paint(Graphics g) {

		super.paint(g);

		Graphics2D  screen2D 	= (Graphics2D) g;
		FontMetrics fm		= screen2D.getFontMetrics();

		// is the scenario open?
		if (scenarioOpen) {

		}

		// show the current counter image if selected
		if (showCounterInfo) {
			if (currentInfoCounter != null){

				// center on the screen
				Rectangle	area 		= currentInfoCounter.getPaintAllInformationArea();

				currentInfoCounter.paintAllInformation(
					screen2D,
					(int) (SCREEN_WIDTH - area.getWidth())/2 ,
					0,
					this,
					Color.white,
					Color.black,
					checkMark);
			}
		}

		// free resources
		screen2D.dispose();
	}
*/
	public void update(Graphics screen) {
		paint(screen);
	}

	/******************************
	Mouse methods
	******************************/
	public void mouseReleased(MouseEvent e) {

		// hide the counter info
		if (showCounterInfo) {

			showCounterInfo = false;
			repaint();
			return;
		}

		// is the scenario open?
		if (!scenarioOpen) return;

	}

	public void mousePressed(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public Dimension getPreferredScrollableViewportSize(){
		return dim;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (scenario == null) {
			return 100;
		}
		else {
			return 100;
		}
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction){
		if (scenario == null) {
			return 400;
		}
		else {
			return 400;
		}
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public	 boolean getScrollableTracksViewportHeight(){
		return false;
	}

	public void createNewScenario(String scenarioName){

		// create the scenario
		scenario  	 	= new Scenario();
		scenarioOpen	= true;
		frame.setStatusBarText("");
		adjustGameViewSize();

		// add the current counter master lists
		scenario.setMMCUnitsList(MMCUnitsList);
		scenario.setSMCUnitsList(SMCUnitsList);
		scenario.setSWList(SWList);
		scenario.setFortificationList(fortificationList);

		// initialize and add the allied groups
		populateGroups(scenario.getAlliedGroups(), alliedPanel, alliedGridBag);
		populateGroups(scenario.getAxisGroups(), axisPanel, axisGridBag);

		// turn on the text areas
		preambleTextArea.setEnabled(true);
		SSRTextArea.setEnabled(true);
		aftermathTextArea.setEnabled(true);
		VCTextField.setEnabled(true);
		balanceTextField.setEnabled(true);

		// set the initial variables
		scenario.setName(scenarioName);
		scenario.setChanged(false);
		repaint();
	}

	public void saveScenario(){

		frame.setStatusBarText("Saving the scenario...");
		frame.paintImmediately();
		scenario.writeScenario(fileName);
		frame.setStatusBarText("");

	}

	public void openScenario(){

		frame.setStatusBarText("Loading the scenario...");
		frame.paintImmediately();
		scenario = Scenario.readScenario(fileName);
		frame.setStatusBarText("");
		scenarioOpen = true;
		adjustGameViewSize();

		// needed to reset the painter after serialization
		scenario.resetCounterObjects(
			MMCUnitsList, 		MMCImagesList,
			SMCUnitsList, 		SMCImagesList,
			SWList, 			SWImagesList,
			fortificationList,	fortificationImagesList
		);

		// initialize and add the allied groups
		populateGroups(scenario.getAlliedGroups(), alliedPanel, alliedGridBag);
		populateGroups(scenario.getAxisGroups(), axisPanel, axisGridBag);

		// turn on and populate the text areas
		preambleTextArea.setEnabled(true);
		SSRTextArea.setEnabled(true);
		aftermathTextArea.setEnabled(true);
		VCTextField.setEnabled(true);
		balanceTextField.setEnabled(true);

		preambleTextArea.setText(scenario.getPreamble());
		SSRTextArea.setText(scenario.getSSR());
		aftermathTextArea.setText(scenario.getAftermath());
		VCTextField.setText(scenario.getVC());
		balanceTextField.setText(scenario.getBalance());

		repaint();
	}

	public void closeScenario(){

		// reset the scenario
		scenarioOpen 		= false;
		scenario 			= null;
		fileName		= "";
		System.gc();		// recover space
		frame.setStatusBarText("");

		// turn off the text areas
		preambleTextArea.setEnabled(false);
		SSRTextArea.setEnabled(false);
		aftermathTextArea.setEnabled(false);
		VCTextField.setEnabled(false);
		balanceTextField.setEnabled(false);
		preambleTextArea.setText("");
		SSRTextArea.setText("");
		aftermathTextArea.setText("");
		VCTextField.setText("");
		balanceTextField.setText("");

		// clear the swing components
		axisPanel.removeAll();
		alliedPanel.removeAll();
		adjustGameViewSize();

		repaint();

	}

	// load the scenario groups into the swing panel
	private void populateGroups(ScenarioGroup[] group, JPanel panel, GridBagLayout layout){

		// set up the grid bag
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = Scenario.MAX_GROUPS;
		c.fill = GridBagConstraints.BOTH;

		JTextField descriptionTextField;

		panel.setLayout(layout);

		for (int x = 0; x < Scenario.MAX_GROUPS; x++){
			if (group[x] != null){

				group[x].setMinimumSize(new Dimension(group[x].getBucketWidth(), group[x].getBucketHeight()));
				group[x].setPreferredSize(new Dimension(group[x].getBucketWidth() * group[x].getNumberOfBuckets(), group[x].getBucketHeight()));
//				group[x].setGameEditor(this);

				// set up the group desciption
				descriptionTextField = new JTextField();
				descriptionTextField.setPreferredSize(new Dimension(group[x].getBucketWidth() * group[x].getNumberOfBuckets(), 25));
				descriptionTextField.setMinimumSize(new Dimension(group[x].getBucketWidth(), 25));
				descriptionTextField.setText(group[x].getDescription());
				descriptionTextField.addKeyListener(new ScenGroupDescKeyAdapter(group[x]));

				// add the group components
				layout.setConstraints(descriptionTextField, c);
				panel.add(descriptionTextField, null);
				layout.setConstraints(group[x], c);
				panel.add(group[x], null);
			}
		}
	}

	public boolean isFocusTraversable() {return true;}

	/******************************
	Keyboard methods
	******************************/
	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		String modifiers = KeyEvent.getKeyModifiersText(e.getModifiers());

		// is the scenario open?
		if (!scenarioOpen) return;

	}

	public void counterPopupMenuEvent(String event){

		frame.setStatusBarText(event);
	}

	// load the MMC units
	public void loadMMCUnits(){

		String sbuf[] = new String[MAX_MMC_UNITS];
		String fileName = homeDirectory + System.getProperty("file.separator","\\") + "MMC.txt";

		String s;
		int current = 0;

		// open and read the file
		try  {

			BufferedReader r = new BufferedReader(new FileReader(fileName));

			// read in the text line for each unit
			while ((s = r.readLine( ) ) != null && current < MAX_MMC_UNITS) {

				sbuf[current++] = s;
			}
			r.close();
		}
		catch (IOException e)  {

			// handle error
			System.out.println("Error reading the MMC unit file " + fileName);
		}

		// create the units
		current = 0;
		Infantry i;
		while (sbuf[current] != null && current < MAX_MMC_UNITS) {

			i = createMMCFromText(sbuf[current], current);

			if (i != null) {
				MMCUnitsList[i.getTypeID()] = i;

				// get the image
				try{
					MMCImagesList[i.getTypeID()] = (new ImageIcon(GameBuilder.class.getResource(i.getImageName()))).getImage();
				}
				catch (Exception e){
					System.out.println("Cannot find the image file for MMC unit " + i.getName());
				}
			}
			current++;
		}
	}

	// load the SMC units
	public void loadSMCUnits(){

		String sbuf[] = new String[MAX_SMC_UNITS];
		String fileName = homeDirectory + System.getProperty("file.separator","\\") + "SMC.txt";

		String s;
		int current = 0;

		// open and read the file
		try  {

			BufferedReader r = new BufferedReader(new FileReader(fileName));

			// read in the text line for each unit
			while ((s = r.readLine( ) ) != null && current < MAX_SMC_UNITS) {

				sbuf[current++] = s;
			}
			r.close();
		}
		catch (IOException e)  {

			// handle error
			System.out.println("Error reading the SMC unit file " + fileName);
		}

		// create the units
		current = 0;
		SMC smc;
		while (sbuf[current] != null && current < MAX_SMC_UNITS) {

			smc = createSMCFromText(sbuf[current], current);

			if (smc != null) {
				SMCUnitsList[smc.getTypeID()] = smc;

				// get the image
				try{
					SMCImagesList[smc.getTypeID()] = (new ImageIcon(GameBuilder.class.getResource(smc.getImageName()))).getImage();
				}
				catch (Exception e){
					System.out.println("Cannot find the image file for SMC unit " + smc.getName());
				}
			}
			current++;
		}
	}

	// load the SW
	public void loadSW(){

		String sbuf[] = new String[MAX_SW];
		String fileName = homeDirectory + System.getProperty("file.separator","\\") + "SW.txt";

		String s;
		int current = 0;

		// open and read the file
		try  {

			BufferedReader r = new BufferedReader(new FileReader(fileName));

			// read in the text line for each unit
			while ((s = r.readLine( ) ) != null && current < MAX_SW) {

				sbuf[current++] = s;
			}
			r.close();
		}
		catch (IOException e)  {

			// handle error
			System.out.println("Error reading the support weapons file " + fileName);
		}

		// create the SW
		current = 0;
		SW sw;
		while (sbuf[current] != null && current < MAX_SW) {

			sw = createSWFromText(sbuf[current], current);

			if (sw != null) {
				SWList[sw.getTypeID()] = sw;

				// get the image
				try{
					SWImagesList[sw.getTypeID()] = (new ImageIcon(GameBuilder.class.getResource(sw.getImageName()))).getImage();
				}
				catch (Exception e){
					System.out.println("Cannot find the image file for support weapon " + sw.getName());
				}
			}
			current++;
		}
	}

	// load the Fortifications
	public void loadFortifications(){

		String sbuf[] = new String[MAX_FORTIFICATIONS];
		String fileName = homeDirectory + System.getProperty("file.separator","\\") + "Fortifications.txt";

		String s;
		int current = 0;

		// open and read the file
		try  {

			BufferedReader r = new BufferedReader(new FileReader(fileName));

			// read in the text line for each unit
			while ((s = r.readLine( ) ) != null && current < MAX_FORTIFICATIONS) {

				sbuf[current++] = s;
			}
			r.close();
		}
		catch (IOException e)  {

			// handle error
			System.out.println("Error reading the fortifications file " + fileName);
		}

		// create the fortifications
		current = 0;
		Fortification f;
		while (sbuf[current] != null && current < MAX_FORTIFICATIONS) {

			f = createFortificationFromText(sbuf[current], current);

			if (f != null) {
				fortificationList[f.getTypeID()] = f;

				// get the image
				try{
					fortificationImagesList[f.getTypeID()] = (new ImageIcon(GameBuilder.class.getResource(f.getImageName()))).getImage();
				}
				catch (Exception e){
					System.out.println("Cannot find the image file for fortification " + f.getName());
				}
			}
			current++;
		}
	}

	private Infantry createMMCFromText(String line, int lineNumber){

		char 		tempChar;

		// MMC.txt file variables
		int		typeID;
		String 	unitName;
		String 	imageName;
		int		nationality;
		int		unitType;
		int		classType;
		int		staticELR;
		int		BPV;
		int		FP;
		boolean	assaultFire;
		int		smokeExponent;
		int		range;
		boolean	sprayingFire;
		int		morale;
		int		brokenMorale;
		boolean	underlinedMorale;
		boolean	selfRally;
		boolean	combatEngineer;
		boolean	stealthy;
		boolean	lax;
		boolean	deploy;
		int 		ELRUnitTypeID;
		int 		ELRFromUnitTypeID;
		int 		reducedUnitTypeID;
		int 		parentSquadTypeID;

		// get the unit type ID number
		try {
			typeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ID ");
			return null;
		}

		// get the nationality
		try {
			nationality = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the nationality ");
			return null;
		}

		// get the unit name
		try {
			unitName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the unit name ");
			return null;
		}

		// get the image name
		try {
			imageName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the image name ");
			return null;
		}

		// get the type
		try {
			unitType = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the unit type");
			return null;
		}

		// get the class type
		try {
			classType = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the class type");
			return null;
		}

		// get the static ELR
		try {
			staticELR = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ELR");
			return null;
		}

		// get the BPV
		try {
			BPV = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the BPV");
			return null;
		}

		// get the FP
		try {
			FP = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the FP");
			return null;
		}

		// get the assault fire flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				assaultFire = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				assaultFire = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for assault fire flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the assault fire flag ");
			return null;
		}

		// get the smoke exponent
		try {
			smokeExponent = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the smoke exponent");
			return null;
		}

		// get the range
		try {
			range = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the range");
			return null;
		}

		// get the spraying fire flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				sprayingFire = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				sprayingFire = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for spraying fire flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the spraying fire flag ");
			return null;
		}

		// get the morale
		try {
			morale = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the morale");
			return null;
		}

		// get the broken morale
		try {
			brokenMorale = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the broken morale");
			return null;
		}

		// get the underlined morale flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				underlinedMorale = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				underlinedMorale = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for underlined morale flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the underlined morale flag ");
			return null;
		}

		// get the self rally flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				selfRally = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				selfRally = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for self rally flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the self rally flag ");
			return null;
		}

		// get the combat engineer flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				combatEngineer = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				combatEngineer = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for combat engineer flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the combat engineer flag ");
			return null;
		}

		// get the stealthy flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				stealthy = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				stealthy = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for stealthy flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the stealthy flag ");
			return null;
		}

		// get the lax flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				lax = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				lax = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for lax flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the lax flag ");
			return null;
		}

		// get the deploy flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				deploy = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				deploy = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for deploy flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the deploy flag ");
			return null;
		}

		// get the ELR to unit type ID
		try {
			ELRUnitTypeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ELR unit type ID");
			return null;
		}

		// get the ELR from unit type ID
		try {
			ELRFromUnitTypeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ELR from unit type ID");
			return null;
		}

		// get the reduced unit type ID
		try {
			reducedUnitTypeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the reduced unit type ID");
			return null;
		}

		// get the reduced unit type ID
		try {
			parentSquadTypeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the parent squad unit type ID");
			return null;
		}

		// create the unit
		return new Infantry(
			typeID,
		 	unitName,
			imageName,
			nationality,
			unitType,
			classType,
			staticELR,
			BPV,
			FP,
			assaultFire,
			smokeExponent,
			range,
			sprayingFire,
			morale,
			brokenMorale,
			underlinedMorale,
			selfRally,
			combatEngineer,
			stealthy,
			lax,
			deploy,
			ELRUnitTypeID,
			ELRFromUnitTypeID,
			reducedUnitTypeID,
			parentSquadTypeID
		);
	}

	private SMC createSMCFromText(String line, int lineNumber){

		char 		tempChar;

		// SMC.txt file variables
		int		typeID;
		int		nationality;
		String 	unitName;
		String 	imageName;
		int		unitType;
		int		BPV;
		int		FP;
		int		range;
		int		morale;
		int		leadershipModifier;
		boolean	commissar;
		boolean	selfRally;
		int 		ELRUnitTypeID;
		int 		ELRFromUnitTypeID;

		// get the unit type ID number
		try {
			typeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ID ");
			return null;
		}

		// get the nationality
		try {
			nationality = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the nationality ");
			return null;
		}

		// get the unit name
		try {
			unitName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the unit name ");
			return null;
		}

		// get the image name
		try {
			imageName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the image name ");
			return null;
		}

		// get the type
		try {
			unitType = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the unit type");
			return null;
		}

		// get the BPV
		try {
			BPV = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the BPV");
			return null;
		}

		// get the FP
		try {
			FP = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the FP");
			return null;
		}

		// get the range
		try {
			range = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the range");
			return null;
		}

		// get the morale
		try {
			morale = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the morale");
			return null;
		}

		// get the leadership modifier
		try {
			leadershipModifier = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the leadership modifier");
			return null;
		}

		// get the commissar flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				commissar = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				commissar = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for commaissar flag");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the commissar flag ");
			return null;
		}

		// get the self rally flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				selfRally = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				selfRally = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for self rally flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the self rally flag ");
			return null;
		}

		// get the ELR to unit type ID
		try {
			ELRUnitTypeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ELR unit type ID");
			return null;
		}

		// get the ELR from unit type ID
		try {
			ELRFromUnitTypeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ELR from unit type ID");
			return null;
		}

		// create the unit
		return new SMC(
			typeID,
			nationality,
			unitName,
			imageName,
			unitType,
			BPV,
			FP,
			range,
			morale,
			leadershipModifier,
			commissar,
			selfRally,
			ELRUnitTypeID,
			ELRFromUnitTypeID
		);
	}

	private SW createSWFromText(String line, int lineNumber){

		char 		tempChar;

		// SW.txt file variables
		int		typeID;
		int		nationality;
		String 	name;
		String 	imageName;
		int 		SWType;
		int		BPV;
		int		FP;
		int		DMFP;
		int		range;
		int		DMRange;
		int		minimumRange;
		int		ROF;
		int		DMROF;
		int		PP;
		int		DMPP;
		boolean	dismantled;
		boolean	sprayingFire;
		boolean	leadershipModifier;
		int		breakdownNumber;
		int		eliminationNumber;
		int		repairNumber;
		int		repairEliminationNumber;

		// get the type ID number
		try {
			typeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ID ");
			return null;
		}

		// get the nationality
		try {
			nationality = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the nationality ");
			return null;
		}

		// get the SW name
		try {
			name = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the unit name ");
			return null;
		}

		// get the image name
		try {
			imageName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the image name ");
			return null;
		}

		// get the type
		try {
			SWType = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the unit type");
			return null;
		}

		// get the BPV
		try {
			BPV = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the BPV");
			return null;
		}

		// get the FP
		try {
			FP = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the FP");
			return null;
		}

		// get the DMFP
		try {
			DMFP = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the DM FP");
			return null;
		}

		// get the range
		try {
			range = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the range");
			return null;
		}

		// get the DM range
		try {
			DMRange = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the DM range");
			return null;
		}

		// get the minimum range
		try {
			minimumRange = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the minimum range");
			return null;
		}

		// get the ROF
		try {
			ROF = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ROF");
			return null;
		}

		// get the DM ROF
		try {
			DMROF = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the DM ROF");
			return null;
		}

		// get the PP
		try {
			PP = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the PP");
			return null;
		}

		// get the DM PP
		try {
			DMPP = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the DM PP");
			return null;
		}

		// get the dismantled flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				dismantled = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				dismantled = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for dismantled flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the dismantled flag ");
			return null;
		}

		// get the spraying fire flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				sprayingFire = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				sprayingFire = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for spraying fire flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the spraying fire flag ");
			return null;
		}

		// get the leadership mod allowed flag
		try {
			tempChar = line.charAt(0);
			if (tempChar == 'Y' || tempChar == 'y'){

				leadershipModifier = true;
			}
			else if (tempChar == 'N' || tempChar == 'n'){

				leadershipModifier = false;
			}
			else {

				System.out.println("Line " + (lineNumber + 1) + ": Invalid value for leadership modifier flag ");
				return null;
			}

			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the leadership modifier flag ");
			return null;
		}

		// get the breakdown number
		try {
			breakdownNumber = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the breakdown number");
			return null;
		}

		// get the elimination number
		try {
			eliminationNumber = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the elimination number");
			return null;
		}

		// get the repair number
		try {
			repairNumber = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the repair number");
			return null;
		}

		// get the repair elimination number
		try {
			repairEliminationNumber = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the repair elimination number");
			return null;
		}

		// create the SW
		return new SW(
			typeID,
			nationality,
		 	name,
		 	imageName,
		 	SWType,
			BPV,
			FP,
			DMFP,
			range,
			DMRange,
			minimumRange,
			ROF,
			DMROF,
			PP,
			DMPP,
			dismantled,
			sprayingFire,
			leadershipModifier,
			breakdownNumber,
			eliminationNumber,
			repairNumber,
			repairEliminationNumber
		);
	}

	private Fortification createFortificationFromText(String line, int lineNumber){

		char 		tempChar;

		// SW.txt file variables
		int		typeID;
		String 	name;
		String 	imageName;
		int 		fortificationType;
		int		BPV;

		// get the type ID number
		try {
			typeID = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the ID ");
			return null;
		}

		// get the fortification name
		try {
			name = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the fortification name ");
			return null;
		}

		// get the image name
		try {
			imageName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the image name ");
			return null;
		}

		// get the type
		try {
			fortificationType = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the fortification type");
			return null;
		}

		// get the BPV
		try {
			BPV = Integer.parseInt(line.substring(0, line.indexOf('|')));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			System.out.println("Line " + (lineNumber + 1) + ": Cannot read the BPV");
			return null;
		}

		// create the fortification
		return new Fortification(
			typeID,
	 		name,
	 		imageName,
	 		fortificationType,
			BPV
		);

	}

	public ArrayList getCounterNameList(int nationality, String unitType){

		// get the list as a hashset
		ArrayList l = new ArrayList(25);

		if (unitType.equals("MMC")){

			for (int x = 0; x < MAX_MMC_UNITS; x++) {

				if (MMCUnitsList[x] != null && MMCUnitsList[x].getNationality() == nationality){

					l.add(MMCUnitsList[x].getName());
				}
			}
		}
		else if (unitType.equals("SMC")){

			for (int x = 0; x < MAX_SMC_UNITS; x++) {

				if (SMCUnitsList[x] != null && SMCUnitsList[x].getNationality() == nationality){

					l.add(SMCUnitsList[x].getName());
				}
			}
		}
		else if (unitType.equals("Support Weapons")){

			for (int x = 0; x < MAX_SW; x++) {

				if (SWList[x] != null && SWList[x].getNationality() == nationality){

					l.add(SWList[x].getName());
				}
			}
		}
		else if (unitType.equals("Fortifications")){

			for (int x = 0; x < MAX_FORTIFICATIONS; x++) {

				if (fortificationList[x] != null){
					l.add(fortificationList[x].getName());
				}
			}
		}


		return l;
	}

	public void initializeMMCUnits(){

		// set throught each unit and map the unit type ID to the corrent unit
		for (int x = 0; x < MAX_MMC_UNITS; x++) {

			if (MMCUnitsList[x] != null){

				// set the ELR unit type
				if (MMCUnitsList[x].getELRUnitTypeID() == Infantry.NO_UNIT_TYPE_ID) {

					MMCUnitsList[x].setELRUnitType(null);
				}
				else {
					MMCUnitsList[x].setELRUnitType(MMCUnitsList[MMCUnitsList[x].getELRUnitTypeID()]);
				}

				// set the ELR from unit type
				if (MMCUnitsList[x].getELRFromUnitTypeID() == Infantry.NO_UNIT_TYPE_ID) {

					MMCUnitsList[x].setELRFromUnitType(null);
				}
				else {
					MMCUnitsList[x].setELRFromUnitType(MMCUnitsList[MMCUnitsList[x].getELRFromUnitTypeID()]);
				}

				// set the reduction unit type
				if (MMCUnitsList[x].getReducedUnitTypeID() == Infantry.NO_UNIT_TYPE_ID) {

					MMCUnitsList[x].setReducedUnitType(null);
				}
				else {
					MMCUnitsList[x].setReducedUnitType(MMCUnitsList[MMCUnitsList[x].getReducedUnitTypeID()]);
				}

				// set the reduction unit type
				if (MMCUnitsList[x].getParentSquadTypeID() == Infantry.NO_UNIT_TYPE_ID) {

					MMCUnitsList[x].setParentSquadType(null);
				}
				else {
					MMCUnitsList[x].setParentSquadType(MMCUnitsList[MMCUnitsList[x].getParentSquadTypeID()]);
				}

				// set unit image

				MMCUnitsList[x].setImage(MMCImagesList[x]);
			}
		}
	}

	public void initializeSMCUnits(){

		// set throught each unit and map the unit type ID to the corrent unit
		for (int x = 0; x < MAX_SMC_UNITS; x++) {

			if (SMCUnitsList[x] != null){

				// set the ELR unit type
				if (SMCUnitsList[x].getELRUnitTypeID() == Infantry.NO_UNIT_TYPE_ID) {

					SMCUnitsList[x].setELRUnitType(null);
				}
				else {
					SMCUnitsList[x].setELRUnitType(SMCUnitsList[SMCUnitsList[x].getELRUnitTypeID()]);
				}

				// set the ELR from unit type
				if (SMCUnitsList[x].getELRFromUnitTypeID() == Infantry.NO_UNIT_TYPE_ID) {

					SMCUnitsList[x].setELRFromUnitType(null);
				}
				else {
					SMCUnitsList[x].setELRFromUnitType(SMCUnitsList[SMCUnitsList[x].getELRFromUnitTypeID()]);
				}

				// set unit image
				SMCUnitsList[x].setImage(SMCImagesList[x]);
			}
		}
	}

	public void initializeSW(){

		// set throught each SW
		for (int x = 0; x < MAX_SW; x++) {

			if (SWList[x] != null){

				// set SW image
				SWList[x].setImage(SWImagesList[x]);
			}
		}
	}

	public void initializeFortifications(){

		// set throught each fortification
		for (int x = 0; x < MAX_FORTIFICATIONS; x++) {

			if (fortificationList[x] != null){

				// set fortification image
				fortificationList[x].setImage(fortificationImagesList[x]);
			}
		}
	}

	public Infantry	getMMC(int id) {

		return MMCUnitsList[id];
	}

	public SMC	getSMC(int id) {

		return SMCUnitsList[id];
	}

	public SW	getSW(int id) {

		return SWList[id];
	}

	public Fortification getFortification(int id) {

		return fortificationList[id];
	}

	public Infantry	getMMC(int nationality, String unitName) {

		// step through all of the units, compare nationality and name
		for (int x = 0; x < MAX_MMC_UNITS; x++) {

			if (	MMCUnitsList[x] 				!= null &&
				MMCUnitsList[x].getNationality() 	== nationality &&
				MMCUnitsList[x].getName().equals(unitName)
			){
				return MMCUnitsList[x];
			}
		}

		return null;
	}

	public SMC	getSMC(int nationality, String unitName) {

		// step through all of the units, compare nationality and name
		for (int x = 0; x < MAX_SMC_UNITS; x++) {

			if (	SMCUnitsList[x] 				!= null &&
				SMCUnitsList[x].getNationality() 	== nationality &&
				SMCUnitsList[x].getName().equals(unitName)
			){
				return SMCUnitsList[x];
			}
		}

		return null;
	}

	public SW	getSW(int nationality, String unitName) {

		// step through all of the units, compare nationality and name
		for (int x = 0; x < MAX_SW; x++) {

			if (	SWList[x] 				!= null &&
				SWList[x].getNationality() 	== nationality &&
				SWList[x].getName().equals(unitName)
			){
				return SWList[x];
			}
		}

		return null;
	}

	public Fortification getFortification(String name) {

		// step through all of the fortifications, compare name
		for (int x = 0; x < MAX_FORTIFICATIONS; x++) {

			if (	fortificationList[x] 				!= null &&
				fortificationList[x].getName().equals(name)
			){
				return fortificationList[x];
			}
		}

		return null;
	}

	// current unit functions
	public Counter 	getCurrentCounter() 		{ return currentCounter;}
	public void 	setCurrentCounter(Counter c)	{ currentCounter = c;}

	public void showAllCounterInformation(Counter c){

		if (c != null){
			CounterInfoFrame infoFrame = new CounterInfoFrame(c, CounterInfoFrame.ALL_INFORMATION, getCheckMarkImage());
		}
	}

	public void addCurrentCounter(){

		if (scenarioOpen){

			scenario.addCounterToSelected(currentCounter);
		}
	}

	public void removeCurrentCounter(){

		if (scenarioOpen){

			scenario.removeCounterFromSelected();
		}
	}

	public Image getCheckMarkImage(){ return checkMark;}

	public void changePreamble(KeyEvent e){

		scenario.setPreamble(((JTextArea)e.getComponent()).getText());
	}

	public void changeAftermath(KeyEvent e){

		scenario.setAftermath(((JTextArea)e.getComponent()).getText());
	}

	public void changeSSR(KeyEvent e){

		scenario.setSSR(((JTextArea)e.getComponent()).getText());
	}

	public void changeVC(KeyEvent e){

		scenario.setVC(((JTextField)e.getComponent()).getText());
	}

	public void changeBalance(KeyEvent e){

		scenario.setBalance(((JTextField)e.getComponent()).getText());
	}
}

// key adapter class for scenario group description text items
class 	ScenGroupDescKeyAdapter
extends	KeyAdapter {

	ScenarioGroup group;

	ScenGroupDescKeyAdapter(ScenarioGroup g) {

		group = g;
	}

	public void keyReleased(KeyEvent e) {
		group.setDescription(((JTextField) e.getComponent()).getText());
	}
}

