/*
 * Copyright (c) 2000-2003 by David Sullivan
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package CASL.MapBuilder;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;

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
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;

import CASL.Map.GameMap;
import CASL.Map.Terrain;
import CASL.MapBuilder.Utility.CASLProperties;

/**
 * Title:        MapEditFrame.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class MapEditFrame extends JFrame  {

	public static final int VASL_MAP_HEIGHT = 645;
	public static final int VASL_MAP_WIDTH = 1799;
	
	// message frame
    private MessageFrame messageFrame;

	// directories
	private String	homeDirectory;
	private String	mapDirectory;

	// is an application?
	private boolean isApplication = true;

	// combo boxes
	private String[] functionList = {
		"LOS", "Set ground level", "Add terrain", "Add hexside terrain", "Add bridge", "Add road", "Add objects"};
	private JComboBox functionComboBox = new JComboBox(functionList);
	private JComboBox terrainComboBox = new JComboBox();
	private JComboBox brushComboBox = new JComboBox();
	private JComboBox zoomComboBox = new JComboBox();

	// define the menu
	private JMenuBar menuBar1 = new JMenuBar();
	private JMenu menuFile = new JMenu();
	private JMenuItem menuFileExit = new JMenuItem();
	private JMenu menuHelp = new JMenu();
	private JMenuItem menuHelpAbout = new JMenuItem();
	private JMenuItem menuFileClose = new JMenuItem();
	private JMenuItem menuFileOpen = new JMenuItem();
	private JMenuItem menuFileNew = new JMenuItem();
	private JMenuItem menuFileSave = new JMenuItem();
	private JMenuItem menuFileSaveAs = new JMenuItem();
	private JMenuItem menuFilePrint = new JMenuItem();
	private JMenuItem menuFileImport = new JMenuItem();
	private JMenuItem menuFileImportAll = new JMenuItem();
	private JMenuItem menuFileExport = new JMenuItem();
	private JMenuItem menuFileVASLConvert = new JMenuItem();
	private JMenuItem menuFileImportTerrain = new JMenuItem();

	private JMenu menuEdit = new JMenu();
	private JMenuItem menuEditFlip = new JMenuItem();
	private JMenuItem menuEditInsert = new JMenuItem();

	private JToolBar toolBar = new JToolBar();
	private JButton openButton = new JButton();
	private JButton newButton = new JButton();
	private JButton saveButton = new JButton();
    private JButton saveAllButton = new JButton();
	private ImageIcon image1;
	private ImageIcon image2;
	private ImageIcon image3;
	private ImageIcon squareBrushIcon;
	private ImageIcon roundBrushIcon;
	private ImageIcon shadowsIcon;
	private ImageIcon contoursIcon;
	private ImageIcon pointerIcon;
	private ImageIcon pointerWithSquareIcon;
	private JLabel statusBar = new JLabel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private MapEditor MapEditor1 = new MapEditor();

	private JPanel buttonBar = new JPanel();
	private JPanel functionSelector = new JPanel();
	private FlowLayout flowLayout3 = new FlowLayout();
	private JLabel spacer = new JLabel();
	private JButton customBuildingButton = new JButton();
	private JButton undoFunctionButton = new JButton();
	private JButton updateMapButton = new JButton();
	private JToggleButton brushshapeButton = new JToggleButton();
	private JToggleButton shadowsButton = new JToggleButton();
	private JToggleButton contoursButton = new JToggleButton();
	private JLabel spacer3 = new JLabel();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel4 = new JLabel();
	private FlowLayout flowLayout1 = new FlowLayout();
	private JLabel spacer4 = new JLabel();
	private JToggleButton pointerButton = new JToggleButton();
	private JTextField rotationTextField = new JTextField();
	private JButton toArrowButton = new JButton();
	private JComboBox toTerrainComboBox = new JComboBox();
    private JLabel spacer5 = new JLabel();
    private JButton testButton = new JButton();

	//Construct the frame
  	public MapEditFrame() {

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

  	public MapEditFrame(String mapName) {

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		openMap(mapName);
	}

	//Component initialization
  private void jbInit() throws Exception  {

		// setup the window widgets
		image1 = new ImageIcon(MapEditor1.getImage("CASL/images/openFile.gif"));
		image2 = new ImageIcon(MapEditor1.getImage("CASL/images/closeFile.gif"));
		image3 = new ImageIcon(MapEditor1.getImage("CASL/images/help.gif"));


		this.getContentPane().setLayout(borderLayout1);
		this.setEnabled(true);
		this.setTitle("MapBuilder");
		statusBar.setBorder(BorderFactory.createEtchedBorder());
		statusBar.setText(" ");
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

		menuEdit.setMargin(new Insets(0, 0, 0, 0));
		menuEdit.setText("Edit");
		menuEdit.setFont(new java.awt.Font("Dialog", 0, 11));
		menuEdit.setEnabled(false);

		menuEditFlip.setPreferredSize(new Dimension(100, 20));
		menuEditFlip.setText("Flip");
		menuEditFlip.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				editFlip(e);
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
		openButton.setIcon(image1);
		openButton.setMargin(new Insets(1, 1, 1, 1));
		openButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openMap();
			}
		});
		openButton.setMaximumSize(new Dimension(25, 25));
		openButton.setMinimumSize(new Dimension(25, 25));
		openButton.setPreferredSize(new Dimension(25, 25));
		openButton.setRequestFocusEnabled(false);
		openButton.setToolTipText("Open map");
		newButton.setMargin(new Insets(2, 2, 2, 2));
		newButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				newMap();
			}
		});
		newButton.setMaximumSize(new Dimension(25, 25));
		newButton.setMinimumSize(new Dimension(25, 25));
		newButton.setPreferredSize(new Dimension(25, 25));
		newButton.setRequestFocusEnabled(false);
		newButton.setToolTipText("New map");
		newButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/newFile.gif")));
		toolBar.setAlignmentY((float) 0.5);
		toolBar.setFloatable(false);
		MapEditor1.setMinimumSize(new Dimension(100, 100));
		MapEditor1.setFrame(this);
		MapBuilder.writeError("Loading the terrain images... ");
		MapEditor1.loadTerrainGraphics();
		MapBuilder.writeError("Loading the prefab building images... ");
		MapEditor1.loadPrefabBuildings();
		jScrollPane1.setMinimumSize(new Dimension(100, 100));
		jScrollPane1.setPreferredSize(new Dimension(100, 100));
		saveButton.setToolTipText("Save map");
		saveButton.setMinimumSize(new Dimension(25, 25));
		saveButton.setPreferredSize(new Dimension(25, 25));
		saveButton.setRequestFocusEnabled(false);
		saveButton.setEnabled(false);
		saveButton.setMaximumSize(new Dimension(25, 25));
		saveButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/saveFile.gif")));
		saveButton.setMargin(new Insets(2, 2, 2, 2));
		saveButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveMap();
			}
		});

        saveAllButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveAllFiles();
			}
		});
        saveAllButton.setMargin(new Insets(2, 2, 2, 2));
        saveAllButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/saveAllFiles.gif")));
        saveAllButton.setMaximumSize(new Dimension(25, 25));
        saveAllButton.setEnabled(false);
        saveAllButton.setRequestFocusEnabled(false);
        saveAllButton.setPreferredSize(new Dimension(25, 25));
        saveAllButton.setMinimumSize(new Dimension(25, 25));
        saveAllButton.setToolTipText("Save all map files");

		menuFileClose.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				closeMap();
			}
		});
		menuFileClose.setPreferredSize(new Dimension(100, 20));
		menuFileClose.setEnabled(false);
		menuFileClose.setMnemonic('1');
		menuFileClose.setText("Close");
		menuFileClose.addActionListener(new ActionListener()  {

			public void actionPerformed(ActionEvent e) {
				closeMap();
			}
		});
		menuFileOpen.setPreferredSize(new Dimension(100, 20));
		menuFileOpen.setMnemonic('1');
		menuFileOpen.setText("Open...");
		menuFileOpen.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openMap();
			}
		});
		menuFileNew.setPreferredSize(new Dimension(100, 20));
		menuFileNew.setMnemonic('1');
		menuFileNew.setText("New...");
		menuFileNew.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				newMap();
			}
		});
		menuFileSave.setPreferredSize(new Dimension(100, 20));
		menuFileSave.setEnabled(false);
		menuFileSave.setMnemonic('0');
		menuFileSave.setText("Save");
		menuFileSave.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveMap();
			}
		});
		menuFileSaveAs.setPreferredSize(new Dimension(100, 20));
		menuFileSaveAs.setEnabled(false);
		menuFileSaveAs.setMnemonic('0');
		menuFileSaveAs.setText("Save as...");
		menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveMapAs();
			}
		});
		menuFilePrint.setPreferredSize(new Dimension(100, 20));
		menuFilePrint.setEnabled(false);
		menuFilePrint.setMnemonic('0');
		menuFilePrint.setText("Print");

		menuFileImport.setPreferredSize(new Dimension(100, 20));
		menuFileImport.setEnabled(true);
		menuFileImport.setMnemonic('0');
		menuFileImport.setText("Import...");
		menuFileImport.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				importMap();
			}
		});

		menuFileImportAll.setPreferredSize(new Dimension(100, 20));
		menuFileImportAll.setEnabled(true);
		menuFileImportAll.setMnemonic('0');
		menuFileImportAll.setText("Import all...");
		menuFileImportAll.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				importAll();
			}
		});

		menuFileExport.setPreferredSize(new Dimension(100, 20));
		menuFileExport.setEnabled(false);
		menuFileExport.setMnemonic('0');
		menuFileExport.setText("Export...");
		menuFileExport.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				exportMap();
			}
		});

		menuFileVASLConvert.setPreferredSize(new Dimension(150, 20));
		menuFileVASLConvert.setEnabled(false);
		menuFileVASLConvert.setMnemonic('0');
		menuFileVASLConvert.setText("Convert VASL image...");
		menuFileVASLConvert.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showConvertVASLMapDialog();
			}
		});

		menuFileImportTerrain.setPreferredSize(new Dimension(100, 20));
		menuFileImportTerrain.setEnabled(false);
		menuFileImportTerrain.setMnemonic('0');
		menuFileImportTerrain.setText("Import Terrain grid...");
		menuFileImportTerrain.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				importTerrain();
			}
		});
		
		menuEditInsert.setPreferredSize(new Dimension(100, 20));
		menuEditInsert.setMnemonic('1');
		menuEditInsert.setText("Insert...");
		menuEditInsert.setEnabled(true);
		menuEditInsert.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				insertMap();
			}
		});

		buttonBar.setLayout(flowLayout1);
		functionSelector.setBorder(BorderFactory.createEtchedBorder());
		functionSelector.setMaximumSize(new Dimension(11000, 1000));
		functionSelector.setMinimumSize(new Dimension(100, 30));
		functionSelector.setPreferredSize(new Dimension(850, 30));
		functionSelector.setLayout(flowLayout3);

		terrainComboBox.setEnabled(false);
		terrainComboBox.setMaximumSize(new Dimension(200, 20));
		terrainComboBox.setMinimumSize(new Dimension(200, 20));
		terrainComboBox.setPreferredSize(new Dimension(200, 20));
		terrainComboBox.setMaximumRowCount(5);
		terrainComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				terrainComboBox(e);
			}
		});
		functionComboBox.setPreferredSize(new Dimension(120, 20));
		functionComboBox.setMaximumRowCount(5);
		functionComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				functionComboBox(e);
			}
		});
		functionComboBox.setMinimumSize(new Dimension(120, 20));
		functionComboBox.setEnabled(false);
		functionComboBox.setMaximumSize(new Dimension(120, 20));
		brushComboBox.setEnabled(false);
		brushComboBox.setMaximumSize(new Dimension(120, 20));
		brushComboBox.setMinimumSize(new Dimension(120, 20));
		brushComboBox.setPreferredSize(new Dimension(120, 20));
		brushComboBox.setMaximumRowCount(5);
		brushComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				brushComboBox(e);
			}
		});
		brushComboBox.addItem("Hex");
		brushComboBox.addItem("64 Pixel");
		brushComboBox.addItem("32 Pixel");
		brushComboBox.addItem("16 Pixel");
		brushComboBox.addItem("8  Pixel");
		brushComboBox.addItem("4  Pixel");
		brushComboBox.addItem("2  Pixel");
		brushComboBox.addItem("1  Pixel");

		zoomComboBox.setEnabled(false);
		zoomComboBox.setMaximumSize(new Dimension(120, 20));
		zoomComboBox.setMinimumSize(new Dimension(120, 20));
		zoomComboBox.setPreferredSize(new Dimension(120, 20));
		zoomComboBox.setMaximumRowCount(5);
		zoomComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				zoomComboBox(e);
			}
		});
		zoomComboBox.addItem("200%");
		zoomComboBox.addItem("100%");
		zoomComboBox.addItem("50%");
		zoomComboBox.setSelectedIndex(1); // 100%

		buttonBar.setMaximumSize(new Dimension(11000, 1000));
		buttonBar.setMinimumSize(new Dimension(100, 60));
		buttonBar.setPreferredSize(new Dimension(700, 60));
		flowLayout3.setAlignment(FlowLayout.LEFT);
		flowLayout3.setHgap(2);
		flowLayout3.setVgap(2);

		spacer.setAlignmentX((float) 0.5);
		spacer.setAlignmentY((float) 0.0);
		spacer.setOpaque(true);
		spacer.setRequestFocusEnabled(false);
		spacer.setIconTextGap(0);
		spacer.setText("  ");

		updateMapButton.setMinimumSize(new Dimension(25, 25));
		updateMapButton.setFocusPainted(false);
		updateMapButton.setEnabled(false);
		updateMapButton.setMaximumSize(new Dimension(25, 25));
		updateMapButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/updateMap.gif")));
		updateMapButton.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				updateMapButton(e);
			}
		});
		updateMapButton.setPreferredSize(new Dimension(25, 25));
		updateMapButton.setRequestFocusEnabled(false);

		squareBrushIcon = new ImageIcon(MapEditor1.getImage("CASL/images/squareSelector.gif"));
		roundBrushIcon	= new ImageIcon(MapEditor1.getImage("CASL/images/roundSelector.gif"));
		brushshapeButton.setPreferredSize(new Dimension(25, 25));
		brushshapeButton.setIcon(squareBrushIcon);
		brushshapeButton.setMaximumSize(new Dimension(25, 25));
		brushshapeButton.setEnabled(false);
		brushshapeButton.setFocusPainted(false);
		brushshapeButton.setRequestFocusEnabled(false);
		brushshapeButton.addChangeListener(new javax.swing.event.ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				brushshapeButton(e);
			}
		});
		brushshapeButton.setMinimumSize(new Dimension(25, 25));

		pointerIcon = new ImageIcon(MapEditor1.getImage("CASL/images/pointer.gif"));
		pointerWithSquareIcon	= new ImageIcon(MapEditor1.getImage("CASL/images/pointerWithSquare.gif"));
		pointerButton.setPreferredSize(new Dimension(25, 25));
		pointerButton.setIcon(pointerIcon);
		pointerButton.setMaximumSize(new Dimension(25, 25));
		pointerButton.setEnabled(false);
		pointerButton.setFocusPainted(false);
		pointerButton.setRequestFocusEnabled(false);
		pointerButton.addChangeListener(new javax.swing.event.ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				pointerButton(e);
			}
		});
		pointerButton.setMinimumSize(new Dimension(25, 25));

		shadowsIcon = new ImageIcon(MapEditor1.getImage("CASL/images/shadows.gif"));
		shadowsButton.setPreferredSize(new Dimension(25, 25));
		shadowsButton.setIcon(shadowsIcon);
		shadowsButton.setMaximumSize(new Dimension(25, 25));
		shadowsButton.setEnabled(true);
		shadowsButton.setFocusPainted(false);
		shadowsButton.setRequestFocusEnabled(false);
		shadowsButton.addChangeListener(new javax.swing.event.ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				shadowsButton(e);
			}
		});
		shadowsButton.setMinimumSize(new Dimension(25, 25));

		contoursIcon = new ImageIcon(MapEditor1.getImage("CASL/images/hills.gif"));
		contoursButton.setPreferredSize(new Dimension(25, 25));
		contoursButton.setIcon(contoursIcon);
		contoursButton.setMaximumSize(new Dimension(25, 25));
		contoursButton.setEnabled(true);
		contoursButton.setFocusPainted(false);
		contoursButton.setRequestFocusEnabled(false);
		contoursButton.addChangeListener(new javax.swing.event.ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				contoursButton(e);
			}
		});
		contoursButton.setMinimumSize(new Dimension(25, 25));

		spacer3.setText("  ");
		spacer3.setIconTextGap(0);
		spacer3.setRequestFocusEnabled(false);
		spacer3.setOpaque(true);
		spacer3.setAlignmentY((float) 0.0);
		spacer3.setAlignmentX((float) 0.5);
		jLabel1.setText("Function:");

		undoFunctionButton.setPreferredSize(new Dimension(25, 25));
		undoFunctionButton.setRequestFocusEnabled(false);
		undoFunctionButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/undo.gif")));
		undoFunctionButton.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				undoButton();
			}
		});
		undoFunctionButton.setEnabled(false);
		undoFunctionButton.setMaximumSize(new Dimension(25, 25));
		undoFunctionButton.setMinimumSize(new Dimension(25, 25));
		customBuildingButton.setPreferredSize(new Dimension(25, 25));
		customBuildingButton.setRequestFocusEnabled(false);
		customBuildingButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/building.gif")));
		customBuildingButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				customBuildingAction();
			}
		});
		customBuildingButton.setEnabled(false);
		customBuildingButton.setMaximumSize(new Dimension(25, 25));
		customBuildingButton.setMinimumSize(new Dimension(25, 25));
		jLabel2.setText("   Terrain: ");
		jLabel3.setText("   Brush: ");
		jLabel4.setText("   Zoom: ");
		flowLayout1.setAlignment(FlowLayout.LEFT);
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);
		spacer4.setAlignmentX((float) 0.5);
		spacer4.setAlignmentY((float) 0.0);
		spacer4.setOpaque(true);
		spacer4.setRequestFocusEnabled(false);
		spacer4.setIconTextGap(0);
		spacer4.setText("  ");
		rotationTextField.setPreferredSize(new Dimension(25, 21));
		rotationTextField.setText("0");
		rotationTextField.setEnabled(false);
		rotationTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				rotationTextField_keyReleased(e);
			}
		});
		toArrowButton.setMinimumSize(new Dimension(25, 25));
		toArrowButton.setMaximumSize(new Dimension(25, 25));
		toArrowButton.setEnabled(false);
		toArrowButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				toArrowButton_actionPerformed();
			}
		});
		toArrowButton.setRequestFocusEnabled(false);
		toArrowButton.setActionCommand("");
		toArrowButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/toArrow.gif")));
		toArrowButton.setPreferredSize(new Dimension(40, 25));
		toTerrainComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				toTerrainComboBox(e);
			}
		});
		toTerrainComboBox.setMaximumRowCount(5);
		toTerrainComboBox.setPreferredSize(new Dimension(200, 20));
		toTerrainComboBox.setMinimumSize(new Dimension(200, 20));
		toTerrainComboBox.setMaximumSize(new Dimension(200, 20));
		toTerrainComboBox.setEnabled(false);
        spacer5.setAlignmentX((float) 0.5);
        spacer5.setAlignmentY((float) 0.0);
        spacer5.setOpaque(true);
        spacer5.setRequestFocusEnabled(false);
        spacer5.setIconTextGap(0);
        spacer5.setText("  ");
        testButton.setIcon(new ImageIcon(MapEditor1.getImage("CASL/images/test.gif")));
        testButton.setToolTipText("Run LOS tests");
		testButton.setEnabled(false);
        testButton.setRequestFocusEnabled(false);
        testButton.setPreferredSize(new Dimension(25, 25));
        testButton.setMinimumSize(new Dimension(25, 25));
        testButton.setMaximumSize(new Dimension(25, 25));
        testButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				testButton(e);
			}
		});
        testButton.setMargin(new Insets(2, 2, 2, 2));

        menuFile.add(menuFileNew);
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileClose);
		menuFile.addSeparator();
		menuFile.add(menuFileSave);
		menuFile.add(menuFileSaveAs);
		menuFile.addSeparator();
		menuFile.add(menuFileImport);
		menuFile.add(menuFileImportAll);
		menuFile.add(menuFileExport);
		menuFile.addSeparator();
		menuFile.add(menuFileVASLConvert);
		menuFile.add(menuFileImportTerrain);
		menuFile.addSeparator();
		menuFile.add(menuFilePrint);
		menuFile.addSeparator();
		menuFile.add(menuFileExit);
		menuEdit.add(menuEditFlip);
		menuEdit.add(menuEditInsert);
		menuHelp.add(menuHelpAbout);
		menuBar1.add(menuFile);
		menuBar1.add(menuEdit);
		menuBar1.add(menuHelp);
		this.setJMenuBar(menuBar1);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		this.getContentPane().add(buttonBar, BorderLayout.NORTH);
		buttonBar.add(toolBar, null);
		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveButton, null);
        toolBar.add(saveAllButton, null);
		toolBar.add(spacer, null);
		toolBar.add(updateMapButton, null);
		toolBar.add(undoFunctionButton, null);
		toolBar.add(spacer3, null);
		toolBar.add(contoursButton, null);
		toolBar.add(shadowsButton, null);
		toolBar.add(spacer4, null);
		buttonBar.add(jLabel3, null);
		buttonBar.add(brushComboBox, null);
		buttonBar.add(brushshapeButton, null);
		buttonBar.add(customBuildingButton, null);
		buttonBar.add(pointerButton, null);
		buttonBar.add(rotationTextField, null);
		buttonBar.add(jLabel4, null);
		buttonBar.add(zoomComboBox, null);
        buttonBar.add(spacer5, null);
        buttonBar.add(testButton, null);
		buttonBar.add(functionSelector, null);
		functionSelector.add(jLabel1, null);
		functionSelector.add(functionComboBox, null);
		functionSelector.add(jLabel2, null);
		functionSelector.add(terrainComboBox, null);
		functionSelector.add(toArrowButton, null);
		functionSelector.add(toTerrainComboBox, null);
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(MapEditor1, null);

		// set the home directories
		homeDirectory = CASLProperties.getCASLHome() + System.getProperty("file.separator","\\") + "MapBuilder" ;
		mapDirectory  = CASLProperties.getMapDirectory();

		// set the status bar
		setStatusBarText("  ");

		// setup the windows
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - 200);
//		setSize(screenSize.width, 130);
		setLocation(0, 0);
		setVisible(true);

		// set up the message frame
		messageFrame = new MessageFrame();
		messageFrame.setSize(screenSize.width, 200);

		// tile the window below the main window
		messageFrame.setLocation(0, getHeight());
		messageFrame.setVisible(true);
		messageFrame.validate();

		validate();
		requestFocus();;
	}

	// is application
	public void 	setIsApplication(boolean i) { isApplication = i;}
	public boolean 	isApplication() {return isApplication;}

	//File | Exit action performed
	public void fileExit(ActionEvent e) {

		// shut down the message window
		messageFrame.dispose();

		// Map changed?
		if (MapEditor1.isMapChanged()) {

			int response = this.AskYesNo("Save changes?");
			if (response == JOptionPane.YES_OPTION){

				saveMap();
				closeMapVariables();

				if (isApplication) System.exit(0);
			}
			else if (response == JOptionPane.NO_OPTION){

				if (isApplication) System.exit(0);
			}
			else if (response == JOptionPane.CANCEL_OPTION){

				return;
			}
		}
		if (isApplication) System.exit(0);
	}

	//Help | About action performed
	public void helpAbout_actionPerformed(ActionEvent e) {
		MapEditFrame_AboutBox dlg = new MapEditFrame_AboutBox(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.show();
	}

	public void editFlip(ActionEvent e) {

		MapEditor1.flipMap();
	}

	//Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if(e.getID() == WindowEvent.WINDOW_CLOSING) {
			fileExit(null);
		}
	}

	public void setMessageFrame(MessageFrame frame){

		messageFrame = frame;
	}

	public void addMessage(String message){

		messageFrame.addMessage(message);

	}

 	public void showMapDialog(String function){

   		MapDialog dialog = null;

		// set the function
		if (function.equals("Open")){
			dialog = new MapDialog(this, "Open map", true);
		}
		else if (function.equals("Save as")){
			dialog = new MapDialog(this, "Save map as...", true);
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

	void newMap() {

		showNewMapDialog();
	}

 	public void showNewMapDialog(){

   		NewMapDialog dialog = new NewMapDialog(this, "Create a new map", true);

		//Center the dialog box
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		if (frameSize.height > screenSize.height)	frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)  	frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		dialog.show();

	}

	void customBuildingAction() {

		showCustomBuildingDialog();
	}

 	public void showCustomBuildingDialog(){

		// set the rotation to zero
		MapEditor1.setRotation(0);
		rotationTextField.setText("0");

		if (functionComboBox.getSelectedItem().equals("Add bridge")){

	   		CustomBridgeDialog dialog = new CustomBridgeDialog(
				this,
				"Set custom bridge parameters",
				true,
				MapEditor1.getCurrentTerrain(),
				MapEditor1.getCustomBridgeRoadElevation()
			);

			//Center the dialog box
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = dialog.getSize();
			if (frameSize.height > screenSize.height)	frameSize.height = screenSize.height;
			if (frameSize.width > screenSize.width)  	frameSize.width = screenSize.width;
			dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
			dialog.show();
		}
		else if (functionComboBox.getSelectedItem().equals("Add terrain")){

			if (terrainComboBox.getSelectedItem().equals("Custom Building")){

		   		CustomBuildingDialog dialog = new CustomBuildingDialog(
					this,
					"Set custom building parameters",
					true,
					MapEditor1.getCurrentTerrain(),
					MapEditor1.getCustomBuildingWidth(),
					MapEditor1.getCustomBuildingHeight()
				);

				//Center the dialog box
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension frameSize = dialog.getSize();
				if (frameSize.height > screenSize.height)	frameSize.height = screenSize.height;
				if (frameSize.width > screenSize.width)  	frameSize.width = screenSize.width;
				dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
				dialog.show();
			}
			else if (terrainComboBox.getSelectedItem().equals("Prefab Building")){

		   		PrefabBuildingDialog dialog = new PrefabBuildingDialog(
					this,
					"Set prefab building parameters",
					true,
					MapEditor1.getPrefabBuildingList(),
					MapEditor1.getCurrentPrefabBuilding()
				);

				//Center the dialog box
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension frameSize = dialog.getSize();
				if (frameSize.height > screenSize.height)	frameSize.height = screenSize.height;
				if (frameSize.width > screenSize.width)  	frameSize.width = screenSize.width;
				dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
				dialog.show();
			}
		}
	}

	public void setCustomBuildingParameters(String terr, int width, int height){

		MapEditor1.setCustomBuildingParameters(terr, width, height);
	}

	public void setPrefabBuildingParameters(String buildingName){

		MapEditor1.setPrefabBuildingParameters(buildingName);
	}

	public void setCustomBridgeParameters(String terr, int roadElevation){

		MapEditor1.setCustomBridgeParameters(terr, roadElevation);
	}

	public void saveMapAs() {


		// create the file filter
		MapFileFilter filter = new MapFileFilter("map", "Map files");
		filter.addExtension("map");
		filter.setDescription("Map files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory);
		fileChooser.setFileFilter(filter);
		int selected = fileChooser.showSaveDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			//if the filename doesn't contains a '.', add '.map' to it
			if (fileChooser.getSelectedFile().getName().indexOf('.') == -1) {

				MapEditor1.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName() + ".map");
			}
			else{
				MapEditor1.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());
			}

			MapEditor1.saveMap();
			this.setTitle("MapBuilder - " + MapEditor1.getFileName());
		}
	}

	public void saveMap() {

		if (!MapEditor1.isMapOpen()) return;

		if(!(MapEditor1.getFileName() == null || MapEditor1.getFileName().equals(""))){
			MapEditor1.saveMap();
		}
		else {

			// create the file filter
			MapFileFilter filter = new MapFileFilter("map", "Map files");
			filter.addExtension("map");
			filter.setDescription("Map files");

			// show the file chooser
			JFileChooser fileChooser = new JFileChooser(mapDirectory);
			fileChooser.setFileFilter(filter);
			int selected = fileChooser.showSaveDialog(this);

			if(selected == JFileChooser.APPROVE_OPTION){

				//if the filename doesn't contains a '.', add '.map' to it
				if (fileChooser.getSelectedFile().getName().indexOf('.') == -1) {

					MapEditor1.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName() + ".map");
				}
				else{
					MapEditor1.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());
				}

				MapEditor1.saveMap();
				this.setTitle("MapBuilder - " + MapEditor1.getFileName());
			}
		}
	}

	public void saveAllFiles() {

		// save the basic map
		saveMap();

		// save the .gif image
		MapEditor1.saveMapImage();

		// export the map
		if(!(MapEditor1.getFileName() == null || MapEditor1.getFileName().equals(""))){
			MapEditor1.exportMap(MapEditor1.getFileName().substring(0, MapEditor1.getFileName().indexOf('.')) + ".mpx");
		}
	}

	public void openMap() {

		// create the file filter
		MapFileFilter filter = new MapFileFilter("map", "Map files");
		filter.addExtension("map");
		filter.setDescription("Map files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory);
		fileChooser.setFileFilter(filter);
   		int selected = fileChooser.showOpenDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			// abort if user chooses not to close the current map
			if (!closeMap()) return;

			MapEditor1.setFileName(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());

			// trap errors
			try{
				setStatusBarText("Opening map...");
				MapEditor1.openMap();
			} catch (Exception e){

				MapBuilder.writeError("Cannot open the map: " + MapEditor1.getFileName());
				MapEditor1.setFileName(null);
				return;
			}
			// enable menus/buttons
			if (MapEditor1.isMapOpen()){
				saveButton.setEnabled(true);
				saveAllButton.setEnabled(true);
				menuFileClose.setEnabled(true);
				menuFileSave.setEnabled(true);
				menuFileSaveAs.setEnabled(true);
				menuFileExport.setEnabled(true);
				menuFileVASLConvert.setEnabled(true);
				menuFileImportTerrain.setEnabled(true);
				menuEdit.setEnabled(true);

				// set function
				setFunction("LOS");

				// make the map the active component
				MapEditor1.requestFocus();
				this.setTitle("MapBuilder - " + MapEditor1.getFileName());

			}
		}
	}

	public void openMap(String mapName) {

		MapEditor1.setFileName(mapDirectory + System.getProperty("file.separator","\\") + mapName);

		// this is all redundant code
		// trap errors
		try{
			setStatusBarText("Opening map...");
			MapEditor1.openMap();
		} catch (Exception e){

			MapBuilder.writeError("Cannot open the map: " + MapEditor1.getFileName());
			MapEditor1.setFileName(null);
			return;
		}

		// enable menus/buttons
		if (MapEditor1.isMapOpen()){
			saveButton.setEnabled(true);
			saveAllButton.setEnabled(true);
			menuFileClose.setEnabled(true);
			menuFileSave.setEnabled(true);
			menuFileSaveAs.setEnabled(true);
			menuFileExport.setEnabled(true);
			menuFileVASLConvert.setEnabled(true);
			menuFileImportTerrain.setEnabled(true);
			menuEdit.setEnabled(true);

			// set function
			setFunction("LOS");

			// make the map the active component
			MapEditor1.requestFocus();
			this.setTitle("MapBuilder - " + MapEditor1.getFileName());
		}
	}

	public void insertMap() {

		// create the file filter
		MapFileFilter filter = new MapFileFilter("map", "Map files");
		filter.addExtension("map");
		filter.setDescription("Map files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory);
		fileChooser.setFileFilter(filter);
   		int selected = fileChooser.showOpenDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			String fileName = fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName();

			// trap errors
			try{
				setStatusBarText("Opening map...");
				GameMap tempMap = GameMap.readMap(fileName);

				// show the dialog
		  		InsertMapDialog dialog = new InsertMapDialog(this, "Insert a map", true, tempMap);

				//Center the dialog box
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension frameSize = dialog.getSize();
				if (frameSize.height > screenSize.height)	frameSize.height = screenSize.height;
				if (frameSize.width > screenSize.width)  	frameSize.width = screenSize.width;
				dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
				dialog.show();

			} catch (Exception e){

				JOptionPane.showMessageDialog(null, "Cannot open the map: " + fileName, "File not found or invalid", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	public void importTerrain() {

		// create the file filter
		MapFileFilter filter = new MapFileFilter(new String[]{"gif", "png"}, "Terrain image files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory);
		fileChooser.setFileFilter(filter);
 		int selected = fileChooser.showOpenDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			String fileName = fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName();

			// trap errors
			try{
				setStatusBarText("Opening image...");
				ImageIcon img = new ImageIcon(fileName);
				int h = img.getIconHeight();
				int w = img.getIconWidth();
				if (h != VASL_MAP_HEIGHT && w != VASL_MAP_WIDTH) {
					JOptionPane.showMessageDialog(null, "Imported Image must be "+VASL_MAP_HEIGHT+"x"+VASL_MAP_WIDTH+". "+fileName +" is "+h+"x"+w, "", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Get the level
				Object[] levels = {"0", "1", "2", "3"};
				String s = (String) JOptionPane.showInputDialog(
				    				null,
				                    "Terrain in image will be imported on to Level?",
				    				"Select Terrain Level",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    levels,
				                    "0");
				if (s != null) {
				  try {
				    importTerrain (img, Integer.parseInt(s));
				  }
				  catch (Exception e) {
				    return;
				  }
				}

			} catch (Exception e){

				JOptionPane.showMessageDialog(null, "Cannot open the image file: " + fileName, e.getMessage(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	public void importTerrain(ImageIcon img, int level) {
	  PixelGrabber grabber = null;
	  ColorModel model = null;
	  int[] pixels = null;
	  messageFrame.addMessage("Starting Terrain Grid Import");
	  MapEditor1.repaint();
       
	  for (int h = 0; h < VASL_MAP_HEIGHT; h++) {
	    grabber = new PixelGrabber(img.getImage(), 0, h, VASL_MAP_WIDTH, 1, true);
		  try {
	        grabber.grabPixels();
	      }
	      catch (InterruptedException e) {
	        return;
	      }
	      
	      model = grabber.getColorModel();
	      pixels = (int[]) grabber.getPixels();
	      
	    for (int w = 0; w < VASL_MAP_WIDTH; w++) {
	      int pixel = pixels[w];
	      int alpha = model.getAlpha(pixel);
	      if (alpha > 0) {
	        int red = model.getRed(pixel);
	        int green = model.getGreen(pixel);
	        int blue = model.getBlue(pixel); 
	        Color c = new Color(red, green, blue);
	        Terrain terrain = Terrain.getTerrain(c);
	        if (terrain == null) {
	          messageFrame.addMessage("\nUnkown Color "+c.toString()+"("+h+","+w+")");
	          MapEditor1.repaint();
	        } 
	        else {
	          MapEditor1.map.setRawTerrain(w, h, terrain.getType());
	          MapEditor1.map.setGridGroundLevel(w, h, (byte) (level + terrain.getHeight()));
	        }
	      }
	    }
	  }
	}
	
	public void insertMap(GameMap insertMap, String upperLeft) {

		MapEditor1.insertGEOMap(insertMap, upperLeft);
	}

	private void closeMapVariables(){

		// no function
		setFunction(null);

		// disable menus/buttons
		saveButton.setEnabled(false);
		saveAllButton.setEnabled(false);
		menuFileClose.setEnabled(false);
		menuFileSave.setEnabled(false);
		menuFileSaveAs.setEnabled(false);
		menuFileExport.setEnabled(false);
		menuFileVASLConvert.setEnabled(false);
		menuFileImportTerrain.setEnabled(false);
		menuEdit.setEnabled(false);

		//close the map
		MapEditor1.closeMap();
		this.setTitle("MapBuilder");
	}

	boolean closeMap() {

		// Map changed?
		if (MapEditor1.isMapChanged()) {

			int response = this.AskYesNo("Save changes?");
			if (response == JOptionPane.YES_OPTION){

				saveMap();
				closeMapVariables();
				return true;
			}
			else if (response == JOptionPane.NO_OPTION){

				closeMapVariables();
				return true;
			}
			else if (response == JOptionPane.CANCEL_OPTION){

				return false;
			}
		}
		else {
			closeMapVariables();
			return true;
		}
		return false;
	}
	
	

	void setStatusBarText(String s){

		statusBar.setText(s);
	}

	void functionComboBox(ItemEvent e) {

		setFunction((String) functionComboBox.getSelectedItem());
	}

	void terrainComboBox(ItemEvent e) {

		String selection = (String) terrainComboBox.getSelectedItem();

		// using custom building?
		if (selection != null && (selection.equals("Custom Building") || selection.equals("Custom Bridge"))){

			customBuildingButton.setEnabled(true);
			MapEditor1.setCustomBuildingOn(true);
		}
		else if (selection != null && (selection.equals("Prefab Building"))){

			customBuildingButton.setEnabled(true);
			MapEditor1.setPrefabBuildingOn(true);
		}
		else {
			customBuildingButton.setEnabled(false);
			MapEditor1.setCustomBuildingOn(false);
			MapEditor1.setPrefabBuildingOn(false);
		}

		if (selection != null){
			MapEditor1.setCurrentTerrain(selection);
		}
	}

	void toTerrainComboBox(ItemEvent e) {

		String selection = (String) toTerrainComboBox.getSelectedItem();

		// using custom building?
		if (selection != null && (selection.equals("Custom Building") || selection.equals("Custom Bridge") || selection.equals("Prefab Building"))){

		}
		else if (selection != null){
			MapEditor1.setCurrentToTerrain(selection);
		}
	}

	void brushComboBox(ItemEvent e) {

		MapEditor1.setCurrentBrush((String) brushComboBox.getSelectedItem());
	}

	void zoomComboBox(ItemEvent e) {


		setStatusBarText("The feature has not been implemented");
	}

	void brushshapeButton(ChangeEvent e) {

		if(brushshapeButton.isSelected()){

			MapEditor1.setRoundBrush(true);
	 		brushshapeButton.setIcon(roundBrushIcon);
		}
		else {
			MapEditor1.setRoundBrush(false);
			brushshapeButton.setIcon(squareBrushIcon);
		}
		repaint();
	}

	void shadowsButton(ChangeEvent e) {

		if(shadowsButton.isSelected()){

			MapEditor1.setShadows(true);
		}
		else {
			MapEditor1.setShadows(false);
		}
		repaint();
	}

	public boolean shadowsSelected(){

	    return shadowsButton.isSelected();
	}

	void contoursButton(ChangeEvent e) {

		if(contoursButton.isSelected()){

			MapEditor1.setContours(true);
		}
		else {
			MapEditor1.setContours(false);
		}
		repaint();
	}

	public boolean contoursSelected(){

	    return contoursButton.isSelected();
	}

	void pointerButton(ChangeEvent e) {

		if(pointerButton.isSelected()){

			MapEditor1.setPointer(true);
	 		pointerButton.setIcon(pointerWithSquareIcon);
		}
		else {
			MapEditor1.setPointer(false);
			pointerButton.setIcon(pointerIcon);
		}
		repaint();
	}

	void createNewMap(int width, int height){

		// abort if user chooses not to close the current map
		if (!closeMap()) return;

		MapEditor1.createNewMap(width, height);

		// enable menus/buttons
		saveButton.setEnabled(true);
		saveAllButton.setEnabled(true);
		menuFileClose.setEnabled(true);
		menuFileSave.setEnabled(true);
		menuFileImport.setEnabled(true);
		menuFileExport.setEnabled(true);
		menuFileVASLConvert.setEnabled(true);
		menuFileImportTerrain.setEnabled(true);
		menuFileSaveAs.setEnabled(true);
		menuEdit.setEnabled(true);
		testButton.setEnabled(true);
		setFunction("LOS");
		validate();

		// make the map the active component
		MapEditor1.requestFocus();
	}

	void updateMapButton(MouseEvent e) {

		MapEditor1.updateMap();
	}

	void setFunction(String newFunction){

		// no function?
		if(newFunction == null){

			// disable widgets
			functionComboBox.setSelectedIndex(0);
			functionComboBox.setEnabled(false);
			terrainComboBox.setEnabled(false);
			toArrowButton.setEnabled(false);
			toTerrainComboBox.setEnabled(false);
			brushComboBox.setEnabled(false);
			zoomComboBox.setEnabled(false);
			updateMapButton.setEnabled(false);
			brushshapeButton.setEnabled(false);
			pointerButton.setEnabled(false);
			rotationTextField.setEnabled(false);
			undoFunctionButton.setEnabled(false);
			testButton.setEnabled(false);
			return;
		}
		else {
			functionComboBox.setEnabled(true);
		}

		MapEditor1.setCurrentFunction(newFunction);

		// clear the combo boxes
		if(terrainComboBox.getItemCount() != 0){
			terrainComboBox.removeAllItems();
		}
		if(toTerrainComboBox.getItemCount() != 0){
			toTerrainComboBox.removeAllItems();
		}

		// set the function, add appropriate list for function
		if (newFunction.equals("LOS")){

			// setup the map editor
			MapEditor1.setCurrentTerrain("");

			// disable widgets
			terrainComboBox.setEnabled(false);
			toArrowButton.setEnabled(false);
			toTerrainComboBox.setEnabled(false);
			brushComboBox.setEnabled(false);
			updateMapButton.setEnabled(true);
			brushshapeButton.setEnabled(false);
			pointerButton.setEnabled(false);
			rotationTextField.setEnabled(false);
			zoomComboBox.setEnabled(false);
			undoFunctionButton.setEnabled(false);
			testButton.setEnabled(true);

		}
		else if (newFunction.equals("Set ground level")){

			// set widgets
			terrainComboBox.setEnabled(true);
			toArrowButton.setEnabled(true);
			toTerrainComboBox.setEnabled(true);
			brushComboBox.setEnabled(true);
			updateMapButton.setEnabled(true);
			brushshapeButton.setEnabled(true);
			pointerButton.setEnabled(true);
			rotationTextField.setEnabled(true);
			zoomComboBox.setEnabled(false);
			undoFunctionButton.setEnabled(true);
			testButton.setEnabled(true);

			terrainComboBox.addItem("Hill Level 0");
			terrainComboBox.addItem("Hill Level 1");
			terrainComboBox.addItem("Hill Level 2");
			terrainComboBox.addItem("Hill Level 3");
			terrainComboBox.addItem("Hill Level 4");
			terrainComboBox.addItem("Hill Level 5");
			terrainComboBox.addItem("Hill Level 6");
			terrainComboBox.addItem("Hill Level 7");
			terrainComboBox.addItem("Hill Level 8");
			terrainComboBox.addItem("Hill Level 9");
			terrainComboBox.addItem("Hill Level 10");
			terrainComboBox.addItem("Valley -1");
			terrainComboBox.addItem("Valley -2");
			terrainComboBox.addItem("Gully");
			terrainComboBox.addItem("Dry Stream");
			terrainComboBox.addItem("Shallow Stream");
			terrainComboBox.addItem("Deep Stream");
//			terrainComboBox.addItem("Wadi");

			toTerrainComboBox.addItem("Hill Level 0");
			toTerrainComboBox.addItem("Hill Level 1");
			toTerrainComboBox.addItem("Hill Level 2");
			toTerrainComboBox.addItem("Hill Level 3");
			toTerrainComboBox.addItem("Hill Level 4");
			toTerrainComboBox.addItem("Hill Level 5");
			toTerrainComboBox.addItem("Hill Level 6");
			toTerrainComboBox.addItem("Hill Level 7");
			toTerrainComboBox.addItem("Hill Level 8");
			toTerrainComboBox.addItem("Hill Level 9");
			toTerrainComboBox.addItem("Hill Level 10");
			toTerrainComboBox.addItem("Valley -1");
			toTerrainComboBox.addItem("Valley -2");
			toTerrainComboBox.addItem("Gully");
			toTerrainComboBox.addItem("Dry Stream");
			toTerrainComboBox.addItem("Shallow Stream");
			toTerrainComboBox.addItem("Deep Stream");
//			toTerrainComboBox.addItem("Wadi");

			// setup the map editor
			MapEditor1.setCurrentTerrain("Hill Level 0");
		}
		else if (newFunction.equals("Add hexside terrain")){

			// set widgets
			terrainComboBox.setEnabled(true);
			toArrowButton.setEnabled(true);
			toTerrainComboBox.setEnabled(true);
			brushComboBox.setEnabled(false);
			updateMapButton.setEnabled(true);
			brushshapeButton.setEnabled(false);
			pointerButton.setEnabled(false);
			rotationTextField.setEnabled(false);
			zoomComboBox.setEnabled(false);
			undoFunctionButton.setEnabled(true);
			testButton.setEnabled(true);

			terrainComboBox.addItem("Wall");
			terrainComboBox.addItem("Road Block");
			terrainComboBox.addItem("Hedge");
			terrainComboBox.addItem("Bocage");
			terrainComboBox.addItem("Cliff");
			terrainComboBox.addItem("Rowhouse Wall");
/*
			terrainComboBox.addItem("Deir Lip");
			terrainComboBox.addItem("Dune, Crest Low");
			terrainComboBox.addItem("Dune, Crest, High");
			terrainComboBox.addItem("Cactus Hedge");
*/
			terrainComboBox.addItem("Panji");
			terrainComboBox.addItem("Rice Paddy Bank");
			terrainComboBox.addItem("Remove");

			toTerrainComboBox.addItem("Wall");
			toTerrainComboBox.addItem("Road Block");
			toTerrainComboBox.addItem("Hedge");
			toTerrainComboBox.addItem("Bocage");
			toTerrainComboBox.addItem("Cliff");
			toTerrainComboBox.addItem("Rowhouse Wall");
/*
			toTerrainComboBox.addItem("Deir Lip");
			toTerrainComboBox.addItem("Dune Crest, Low");
			toTerrainComboBox.addItem("Dune Crest, High");
			toTerrainComboBox.addItem("Cactus Hedge");
*/
			toTerrainComboBox.addItem("Panji");
			toTerrainComboBox.addItem("Rice Paddy Bank");

			// setup the map editor
			MapEditor1.setCurrentTerrain("Wall");
		}
		else if (newFunction.equals("Add terrain")){

			// set widgets
			terrainComboBox.setEnabled(true);
			toArrowButton.setEnabled(true);
			toTerrainComboBox.setEnabled(true);
			brushComboBox.setEnabled(true);
			updateMapButton.setEnabled(true);
			brushshapeButton.setEnabled(true);
			pointerButton.setEnabled(true);
			rotationTextField.setEnabled(true);
			zoomComboBox.setEnabled(false);
			undoFunctionButton.setEnabled(true);
			testButton.setEnabled(true);

			terrainComboBox.addItem("Open Ground");
			terrainComboBox.addItem("Plowed Field");
			terrainComboBox.addItem("Snow");
			terrainComboBox.addItem("Deep Snow");
			terrainComboBox.addItem("Ice");
			terrainComboBox.addItem("Mud");
			terrainComboBox.addItem("Mudflats");
			terrainComboBox.addItem("Water");
			terrainComboBox.addItem("Shallow Water");
			terrainComboBox.addItem("River");
			terrainComboBox.addItem("Shallow River");
			terrainComboBox.addItem("Ford");
			terrainComboBox.addItem("Canal");
			terrainComboBox.addItem("Marsh");
			terrainComboBox.addItem("Dirt Road");
			terrainComboBox.addItem("Paved Road");
			terrainComboBox.addItem("Runway");
			terrainComboBox.addItem("Path");
			terrainComboBox.addItem("Shell Holes");

			terrainComboBox.addItem("Grain");
			terrainComboBox.addItem("Brush");
			terrainComboBox.addItem("Crags");
			terrainComboBox.addItem("Debris");
			terrainComboBox.addItem("Grave Yard");
			terrainComboBox.addItem("Woods");
			terrainComboBox.addItem("Forest");
			terrainComboBox.addItem("Pine Woods");
			terrainComboBox.addItem("Orchard");
			terrainComboBox.addItem("Orchard, Out of Season");
			terrainComboBox.addItem("Lumber Yard");
			terrainComboBox.addItem("Custom Building");
			terrainComboBox.addItem("Prefab Building");
			terrainComboBox.addItem("Wooden Rubble");
			terrainComboBox.addItem("Wooden Building");
			terrainComboBox.addItem("Wooden Building, 1 Level");
			terrainComboBox.addItem("Wooden Building, 2 Level");
			terrainComboBox.addItem("Wooden Building, 3 Level");
			terrainComboBox.addItem("Wooden Building, 4 Level");
			terrainComboBox.addItem("Wooden Factory, 1.5 Level");
			terrainComboBox.addItem("Wooden Factory, 2.5 Level");
			terrainComboBox.addItem("Wooden Market Place");
			terrainComboBox.addItem("Stone Rubble");
			terrainComboBox.addItem("Stone Building");
			terrainComboBox.addItem("Stone Building, 1 Level");
			terrainComboBox.addItem("Stone Building, 2 Level");
			terrainComboBox.addItem("Stone Building, 3 Level");
			terrainComboBox.addItem("Stone Building, 4 Level");
			terrainComboBox.addItem("Stone Factory, 1.5 Level");
			terrainComboBox.addItem("Stone Factory, 2.5 Level");
			terrainComboBox.addItem("Stone Market Place");
/*
			terrainComboBox.addItem("Scrub");
			terrainComboBox.addItem("Hammada");
			terrainComboBox.addItem("Deir");
			terrainComboBox.addItem("Hillock");
			terrainComboBox.addItem("Hillock Summit");
			terrainComboBox.addItem("Sand");
			terrainComboBox.addItem("Sand Dune");
			terrainComboBox.addItem("Tracks");
			terrainComboBox.addItem("Mausoleum");
			terrainComboBox.addItem("Bedouin Camp");
			terrainComboBox.addItem("Building Cluster");
			terrainComboBox.addItem("Cactus Patch");
			terrainComboBox.addItem("Olive Grove");
			terrainComboBox.addItem("Vineyard");
			terrainComboBox.addItem("Broken Ground");
*/
			terrainComboBox.addItem("Temple");
			terrainComboBox.addItem("Light Jungle");
			terrainComboBox.addItem("Dense Jungle");
			terrainComboBox.addItem("Bamboo");
			terrainComboBox.addItem("Palm Trees");
			terrainComboBox.addItem("Huts");
			terrainComboBox.addItem("Collapsed Huts");
			terrainComboBox.addItem("Kunai");
			terrainComboBox.addItem("Swamp");
			terrainComboBox.addItem("Rice Paddy, Drained");
			terrainComboBox.addItem("Rice Paddy, Irrigated");
			terrainComboBox.addItem("Rice Paddy, In Season");
/*
			terrainComboBox.addItem("Ocean");
			terrainComboBox.addItem("Shallow Ocean");
			terrainComboBox.addItem("Effluent");
			terrainComboBox.addItem("Soft Sand");
			terrainComboBox.addItem("Beach, Slight Slope");
			terrainComboBox.addItem("Beach, Moderate Slope");
			terrainComboBox.addItem("Beach, Steep Slope");
			terrainComboBox.addItem("Exposed Reef");
			terrainComboBox.addItem("Submerged Reef");
*/
			toTerrainComboBox.addItem("Open Ground");
			toTerrainComboBox.addItem("Plowed Field");
			toTerrainComboBox.addItem("Snow");
			toTerrainComboBox.addItem("Deep Snow");
			toTerrainComboBox.addItem("Ice");
			toTerrainComboBox.addItem("Mud");
			toTerrainComboBox.addItem("Mudflats");
			toTerrainComboBox.addItem("Water");
			toTerrainComboBox.addItem("Shallow Water");
			toTerrainComboBox.addItem("River");
			toTerrainComboBox.addItem("Shallow River");
			toTerrainComboBox.addItem("Ford");
			toTerrainComboBox.addItem("Canal");
			toTerrainComboBox.addItem("Marsh");
			toTerrainComboBox.addItem("Dirt Road");
			toTerrainComboBox.addItem("Paved Road");
			toTerrainComboBox.addItem("Runway");
			toTerrainComboBox.addItem("Path");
			toTerrainComboBox.addItem("Shell Holes");

			toTerrainComboBox.addItem("Grain");
			toTerrainComboBox.addItem("Brush");
			toTerrainComboBox.addItem("Crags");
			toTerrainComboBox.addItem("Debris");
			toTerrainComboBox.addItem("Grave Yard");
			toTerrainComboBox.addItem("Woods");
			toTerrainComboBox.addItem("Forest");
			toTerrainComboBox.addItem("Pine Woods");
			toTerrainComboBox.addItem("Orchard");
			toTerrainComboBox.addItem("Orchard, Out of Season");
			toTerrainComboBox.addItem("Lumber Yard");
			toTerrainComboBox.addItem("Custom Building");
			toTerrainComboBox.addItem("Prefab Building");
			toTerrainComboBox.addItem("Wooden Rubble");
			toTerrainComboBox.addItem("Wooden Building");
			toTerrainComboBox.addItem("Wooden Building, 1 Level");
			toTerrainComboBox.addItem("Wooden Building, 2 Level");
			toTerrainComboBox.addItem("Wooden Building, 3 Level");
			toTerrainComboBox.addItem("Wooden Building, 4 Level");
			toTerrainComboBox.addItem("Wooden Factory, 1.5 Level");
			toTerrainComboBox.addItem("Wooden Factory, 2.5 Level");
			toTerrainComboBox.addItem("Wooden Market Place");
			toTerrainComboBox.addItem("Stone Rubble");
			toTerrainComboBox.addItem("Stone Building");
			toTerrainComboBox.addItem("Stone Building, 1 Level");
			toTerrainComboBox.addItem("Stone Building, 2 Level");
			toTerrainComboBox.addItem("Stone Building, 3 Level");
			toTerrainComboBox.addItem("Stone Building, 4 Level");
			toTerrainComboBox.addItem("Stone Factory, 1.5 Level");
			toTerrainComboBox.addItem("Stone Factory, 2.5 Level");
			toTerrainComboBox.addItem("Stone Market Place");
/*
			toTerrainComboBox.addItem("Scrub");
			toTerrainComboBox.addItem("Hammada");
			toTerrainComboBox.addItem("Deir");
			toTerrainComboBox.addItem("Hillock");
			toTerrainComboBox.addItem("Hillock Summit");
			toTerrainComboBox.addItem("Sand");
			toTerrainComboBox.addItem("Sand Dune");
			toTerrainComboBox.addItem("Tracks");
			toTerrainComboBox.addItem("Mausoleum");
			toTerrainComboBox.addItem("Bedouin Camp");
			toTerrainComboBox.addItem("Building Cluster");
			toTerrainComboBox.addItem("Cactus Patch");
			toTerrainComboBox.addItem("Olive Grove");
			toTerrainComboBox.addItem("Vineyard");
			toTerrainComboBox.addItem("Broken Ground");
*/
			toTerrainComboBox.addItem("Temple");
			toTerrainComboBox.addItem("Light Jungle");
			toTerrainComboBox.addItem("Dense Jungle");
			toTerrainComboBox.addItem("Bamboo");
			toTerrainComboBox.addItem("Palm Trees");
			toTerrainComboBox.addItem("Huts");
			toTerrainComboBox.addItem("Collapsed Huts");
			toTerrainComboBox.addItem("Kunai");
			toTerrainComboBox.addItem("Swamp");
			toTerrainComboBox.addItem("Rice Paddy, Drained");
			toTerrainComboBox.addItem("Rice Paddy, Irrigated");
			toTerrainComboBox.addItem("Rice Paddy, In Season");
/*
			toTerrainComboBox.addItem("Ocean");
			toTerrainComboBox.addItem("Shallow Ocean");
			toTerrainComboBox.addItem("Effluent");
			toTerrainComboBox.addItem("Soft Sand");
			toTerrainComboBox.addItem("Beach, Slight Slope");
			toTerrainComboBox.addItem("Beach, Moderate Slope");
			toTerrainComboBox.addItem("Beach, Steep Slope");
			toTerrainComboBox.addItem("Exposed Reef");
			toTerrainComboBox.addItem("Submerged Reef");
*/
			// setup the map editor
			MapEditor1.setCurrentTerrain("Open Ground");
		}
		else if (newFunction.equals("Add bridge")){

			// set widgets
			terrainComboBox.setEnabled(true);
			toTerrainComboBox.setEnabled(false);
			brushComboBox.setEnabled(false);
			updateMapButton.setEnabled(true);
			brushshapeButton.setEnabled(false);
			pointerButton.setEnabled(true);
			zoomComboBox.setEnabled(false);
			rotationTextField.setEnabled(true);
			undoFunctionButton.setEnabled(true);
			customBuildingButton.setEnabled(true);
			testButton.setEnabled(true);

			terrainComboBox.addItem("Custom Bridge");
			terrainComboBox.addItem("Remove");

			// setup the map editor
			MapEditor1.setCustomBridgeParameters("Single Hex Wooden Bridge", 0);
		}
		else if (newFunction.equals("Add road")){

			// set widgets
			terrainComboBox.setEnabled(true);
			toArrowButton.setEnabled(true);
			toTerrainComboBox.setEnabled(true);
			brushComboBox.setEnabled(false);
			updateMapButton.setEnabled(true);
			brushshapeButton.setEnabled(false);
			pointerButton.setEnabled(false);
			rotationTextField.setEnabled(false);
			zoomComboBox.setEnabled(false);
			undoFunctionButton.setEnabled(true);
			customBuildingButton.setEnabled(false);
			testButton.setEnabled(true);

			terrainComboBox.addItem("Dirt Road");
			terrainComboBox.addItem("Paved Road");
			terrainComboBox.addItem("Runway");
			terrainComboBox.addItem("Sunken Road");
			terrainComboBox.addItem("Elevated Road");

			toTerrainComboBox.addItem("Dirt Road");
			toTerrainComboBox.addItem("Paved Road");
			toTerrainComboBox.addItem("Runway");
			toTerrainComboBox.addItem("Sunken Road");
			toTerrainComboBox.addItem("Elevated Road");
		}
		else if (newFunction.equals("Add objects")){

			// set widgets
			terrainComboBox.setEnabled(true);
			toTerrainComboBox.setEnabled(false);
			brushComboBox.setEnabled(false);
			zoomComboBox.setEnabled(false);
			updateMapButton.setEnabled(true);
			pointerButton.setEnabled(false);
			rotationTextField.setEnabled(false);
			brushshapeButton.setEnabled(false);
			undoFunctionButton.setEnabled(true);
			customBuildingButton.setEnabled(false);
			testButton.setEnabled(true);

			terrainComboBox.addItem("Foxholes");
			terrainComboBox.addItem("Trench");
			terrainComboBox.addItem("Antitank Trench");
			terrainComboBox.addItem("Sangar");
			terrainComboBox.addItem("Stairway");
			terrainComboBox.addItem("Sewer");
			terrainComboBox.addItem("Tunnel");
			terrainComboBox.addItem("Smoke");
			terrainComboBox.addItem("Vehicle");
			terrainComboBox.addItem("Remove Stairway");
			terrainComboBox.addItem("Remove Tunnel/Sewer");
			terrainComboBox.addItem("Remove Entrenchment");
			terrainComboBox.addItem("Remove Smoke");
			terrainComboBox.addItem("Remove Vehicle");
		}
	}

	void undoButton() {
		MapEditor1.undoSelections();
	}

 	public int AskYesNo(String question){


		// get reponse
		int result = JOptionPane.showConfirmDialog(this, question);
		return result;
	}

	public void paintImmediately(){

		statusBar.paintImmediately(0,0,statusBar.getWidth(), statusBar.getHeight());

	}

	void rotationTextField_keyReleased(KeyEvent e) {

		try {
			MapEditor1.setRotation(Integer.parseInt(rotationTextField.getText()));
		}
		catch(Exception exp) {
			MapEditor1.setRotation(0);
		}
	}

	void toArrowButton_actionPerformed() {

		int response = this.AskYesNo(
			"Are you sure you want to convert all " +
			terrainComboBox.getSelectedItem() + " terrain to "  +
			toTerrainComboBox.getSelectedItem() + "?");

		if (response == JOptionPane.YES_OPTION){

			MapEditor1.changeAllTerrain();
		}
		else if (response == JOptionPane.NO_OPTION){

		}
		else if (response == JOptionPane.CANCEL_OPTION){

		}
	}

	public void importMap() {

		// create the file filter
		MapFileFilter filter = new MapFileFilter("mpx", "Map export files");
		filter.addExtension("mpx");
		filter.setDescription("Map export files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory);
		fileChooser.setFileFilter(filter);
   		int selected = fileChooser.showOpenDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			// abort if user chooses not to close the map
	    	if (!closeMap()) return;

			// trap errors
			try{
				setStatusBarText("Importing map...");
				MapEditor1.importMap(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());
			} catch (Exception e){

				MapBuilder.writeError("Cannot open the map: " + MapEditor1.getFileName());
				MapEditor1.setFileName(null);
				return;
			}

			// enable menus/buttons
			if (MapEditor1.isMapOpen()){
				saveButton.setEnabled(true);
				saveAllButton.setEnabled(true);
				menuFileClose.setEnabled(true);
				menuFileSave.setEnabled(true);
				menuFileSaveAs.setEnabled(true);
				menuFileExport.setEnabled(true);
				menuFileVASLConvert.setEnabled(true);
				menuFileImportTerrain.setEnabled(true);
				menuEdit.setEnabled(true);
				testButton.setEnabled(true);

				// set function
				setFunction("LOS");

				// make the map the active component
				MapEditor1.requestFocus();
				this.setTitle("MapBuilder - " + MapEditor1.getFileName());
			}
		}

	}

	public void importAll() {

		// see if they really want to do this!
		int response = this.AskYesNo(
			"Are you sure you want to import all '.mpx' files in " + mapDirectory +
			"? Existing '.map' files with the same name will be overwritten!");
		if (response != JOptionPane.YES_OPTION){

			return;
		}

		// abort if user chooses not to close the map
		if (!closeMap()) return;

		// Get the file list
	    File[] fileList = new File(mapDirectory).listFiles();
		for (int x = 0; x < fileList.length; x++){

		    // convert each export file
			if (fileList[x].toString().toLowerCase().endsWith(".mpx")){

				// import the file
				String fileName = fileList[x].toString().substring(0, fileList[x].toString().toLowerCase().indexOf(".mpx")) + ".map";
				try{
					setStatusBarText("Importing map..." + fileList[x]);
					MapEditor1.importMap(fileList[x].toString());
				    MapEditor1.setFileName(fileName);

				} catch (Exception e){

					MapBuilder.writeError("Cannot open the map: " + fileList[x]);
					MapEditor1.setFileName(null);
					return;
				}

				// save the map
				setStatusBarText("Saving map " + fileName);
				this.paintImmediately();
				MapEditor1.saveMap();

				// save the map image
				setStatusBarText("Saving image for map " + fileName);
				this.paintImmediately();
				MapEditor1.saveMapImage();
			}
		}

		// close the last converted map
		if (!closeMap()) return;

	}

	public void exportMap() {

		if (!MapEditor1.isMapOpen()) return;

		// create the file filter
		MapFileFilter filter = new MapFileFilter("mpx", "Map export files");
		filter.addExtension("mpx");
		filter.setDescription("Map export files");

		// show the file chooser
		JFileChooser fileChooser = new JFileChooser(mapDirectory);
		fileChooser.setFileFilter(filter);
		int selected = fileChooser.showSaveDialog(this);

		if(selected == JFileChooser.APPROVE_OPTION){

			//if the filename doesn't contains a '.', add '.mpx' to it
			if (fileChooser.getSelectedFile().getName().indexOf('.') == -1) {

				MapEditor1.exportMap(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName() + ".mpx");
			}
			else{
				MapEditor1.exportMap(fileChooser.getCurrentDirectory().getPath() + System.getProperty("file.separator","\\") + fileChooser.getSelectedFile().getName());
			}
		}
	}

	public void showConvertVASLMapDialog(){

   		VASLMapConverterDialog dialog = new VASLMapConverterDialog(this, true);

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
	
	public void convertVASLImage(int board){

		if (!MapEditor1.isMapOpen()) return;

		MapEditor1.convertVASLImage(board);
	}

    void testButton(ActionEvent e) {

		MapEditor1.runLosTest();
    }
    void saveAll_actionPerformed(ActionEvent e) {

    }
}

