
/**
 * Final Project: Image Editor Viewer
 *

import javax.swing.*;  // for JLabel, JButton, JFileChooser
import squint.SImage;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.logging.Level;
 * @author Bilawal Samad Shaikh
 * @version 4th December 2015
 */

import javax.swing.*;  // for JLabel, JButton, JFileChooser
import squint.SImage;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Fauxtoshop extends WindowManager {

    private PPMImage thePPMImage;
    private PPMImage theWorkingImage;
    private PPMImage newImage;

    private JLabel imageLabel; // used to display the image

    private JLabel leftSliderLabel;
    private JLabel rightSliderLabel;
    private JSlider theSlider;

    private JButton acceptButton;
    private JButton cancelButton;

    private JPanel bottomPanel;
    private JPanel buttonPanel;
    private JPanel sliderPanel;

    private JMenuBar myMenuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu toolsMenu;
    private JMenuItem loadMenuItem;
    private JMenuItem revertMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem quitMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem invertMenuItem;
    private JMenuItem grayscaleMenuItem;
    private JMenuItem doubleSizeMenuItem;
    private JMenuItem halveSizeMenuItem;
    private JMenuItem brightnessMenuItem;
    private JMenuItem contrastMenuItem;
    private JMenuItem levelsMenuItem;
    private JMenuItem chromaKeyMenuItem;
    private JMenuItem blurMenuItem;
    private JMenuItem cropMenuItem;

    private String filename;
    private boolean askToQuit = true;

    private JFileChooser chooser;   // used to make file open/save easier

    //======================================================================
    //* public Fauxtoshop()
    //* Default constructor for the PPM Viewer class.  Calls constructor
    //* of superclass (WindowManager).
    //======================================================================
    public Fauxtoshop() {
        // call the parent class (WindowManager) constructor
        super("Fauxtoshop", 400, 400);

        // tell the window manager how we want the windows managed
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setAlwaysOnTop(true);

        // construct a label to hold the image (SImage)
        imageLabel = new JLabel("", SwingConstants.CENTER);

        //construct the buttons
        acceptButton = new JButton("Accept");
        cancelButton = new JButton("Cancel");

        // create a new panel
        bottomPanel = new JPanel();
        sliderPanel = new JPanel();
        buttonPanel = new JPanel();
        bottomPanel.add(sliderPanel);
        bottomPanel.add(buttonPanel);
        leftSliderLabel = new JLabel("");
        theSlider = new JSlider();
        theSlider.addChangeListener(this);
        rightSliderLabel = new JLabel();
        sliderPanel.add(leftSliderLabel);
        sliderPanel.add(theSlider);
        sliderPanel.add(rightSliderLabel);
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);

        bottomPanel.setVisible(false);

        // costruct a menu bar
        myMenuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        toolsMenu = new JMenu("Tools");
        myMenuBar.add(fileMenu);
        myMenuBar.add(editMenu);
        myMenuBar.add(toolsMenu);
        loadMenuItem = new JMenuItem("Load...");
        revertMenuItem = new JMenuItem("Revert");
        saveAsMenuItem = new JMenuItem("Save As...");
        quitMenuItem = new JMenuItem("Quit");
        undoMenuItem = new JMenuItem("Undo");
        invertMenuItem = new JMenuItem("Invert");
        grayscaleMenuItem = new JMenuItem("Grayscale");
        doubleSizeMenuItem = new JMenuItem("Double Size");
        halveSizeMenuItem = new JMenuItem("Halve Size");
        brightnessMenuItem = new JMenuItem("Adjust Brightness...");
        contrastMenuItem = new JMenuItem("Adjust Contrast...");
        levelsMenuItem = new JMenuItem("Change Levels...");
        chromaKeyMenuItem = new JMenuItem("Chroma Key...");
        blurMenuItem = new JMenuItem("Blur...");
        cropMenuItem = new JMenuItem("Crop...");
        fileMenu.add(loadMenuItem);
        fileMenu.add(revertMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);
        editMenu.add(undoMenuItem);
        toolsMenu.add(invertMenuItem);
        toolsMenu.add(grayscaleMenuItem);
        toolsMenu.add(doubleSizeMenuItem);
        toolsMenu.add(halveSizeMenuItem);
        toolsMenu.add(brightnessMenuItem);
        toolsMenu.add(contrastMenuItem);
        toolsMenu.add(levelsMenuItem);
        toolsMenu.add(chromaKeyMenuItem);
        toolsMenu.add(blurMenuItem);
        toolsMenu.add(cropMenuItem);
        loadMenuItem.setActionCommand("load");
        loadMenuItem.addActionListener(this);
        revertMenuItem.setActionCommand("revert");
        revertMenuItem.addActionListener(this);
        saveAsMenuItem.setActionCommand("Save As");
        saveAsMenuItem.addActionListener(this);
        quitMenuItem.setActionCommand("quit");
        quitMenuItem.addActionListener(this);
        undoMenuItem.setActionCommand("undo");
        undoMenuItem.addActionListener(this);
        invertMenuItem.setActionCommand("invert");
        invertMenuItem.addActionListener(this);
        grayscaleMenuItem.setActionCommand("grayscale");
        grayscaleMenuItem.addActionListener(this);
        doubleSizeMenuItem.setActionCommand("Double");
        doubleSizeMenuItem.addActionListener(this);
        halveSizeMenuItem.setActionCommand("Halve");
        halveSizeMenuItem.addActionListener(this);
        brightnessMenuItem.setActionCommand("Brightness");
        brightnessMenuItem.addActionListener(this);
        contrastMenuItem.setActionCommand("Contrast");
        contrastMenuItem.addActionListener(this);
        levelsMenuItem.setActionCommand("Levels");
        levelsMenuItem.addActionListener(this);
        chromaKeyMenuItem.setActionCommand("ChromaKey");
        chromaKeyMenuItem.addActionListener(this);
        blurMenuItem.setActionCommand("blur");
        blurMenuItem.addActionListener(this);
        cropMenuItem.setActionCommand("crop");
        cropMenuItem.addActionListener(this);

        acceptButton.setActionCommand("accept");
        acceptButton.addActionListener(this);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        // gray-out items until an image is loaded
        revertMenuItem.setEnabled(false);
        saveAsMenuItem.setEnabled(false);
        toolsMenu.setEnabled(false);
        editMenu.setEnabled(false);

        // construct a new instance of JFileChooser to be used in methods below
        chooser = new JFileChooser(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ppm", "PPM");
        chooser.setFileFilter((javax.swing.filechooser.FileFilter) filter);

        // set the layout of this window, drop the image and button panel in
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.add(myMenuBar, BorderLayout.NORTH);
    }

    //======================================================================
    //* public void buttonClicked( JButton whichButton )
    //* This method overrides the stub method in WindowManager.
    //* whichButton will be a reference to either the load or save button.
    //======================================================================
    @Override
    public void selectionMade(JMenuItem whichItem) {
        String action = whichItem.getActionCommand();

        if (action.equals(loadMenuItem.getActionCommand())) {
            // user pressed load button
            loadImage();
            revertMenuItem.setEnabled(false);
            askToQuit = false;

        } else if (action.equals(revertMenuItem.getActionCommand())) {
            revert();
            revertMenuItem.setEnabled(false);
            askToQuit = false;

        } else if (action.equals(saveAsMenuItem.getActionCommand())) {
            saveImage();
            askToQuit = false;

        } else if (action.equals(quitMenuItem.getActionCommand())) {
            quit();

        } else if (action.equals(grayscaleMenuItem.getActionCommand())) {
            grayScale();
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(invertMenuItem.getActionCommand())) {
            invert();
            revertMenuItem.setEnabled(true);
            askToQuit = true;
            

        } else if (action.equals(doubleSizeMenuItem.getActionCommand())) {
            doubleImage();
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(halveSizeMenuItem.getActionCommand())) {
            halveImage();
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(brightnessMenuItem.getActionCommand())) {
            leftSliderLabel.setText("Brightness:");
            theSlider.setMinimum(-255);
            theSlider.setMaximum(255);
            theSlider.setValue(0);
            bottomPanel.setVisible(true);

            adjustBrightness(theSlider.getValue());
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(contrastMenuItem.getActionCommand())) {
            leftSliderLabel.setText("Contrast:");
            theSlider.setMinimum(0);
            theSlider.setMaximum(255);
            theSlider.setValue(127);
            bottomPanel.setVisible(true);

            adjustContrast(theSlider.getValue());
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(levelsMenuItem.getActionCommand())) {
            leftSliderLabel.setText("Change Levels:");
            theSlider.setMinimum(2);
            theSlider.setMaximum(256);
            theSlider.setValue(256);
            bottomPanel.setVisible(true);

            changeLevels(theSlider.getValue());
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(chromaKeyMenuItem.getActionCommand())) { 
            chromaKey();
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(blurMenuItem.getActionCommand())) {
            leftSliderLabel.setText("Radius:");
            theSlider.setMinimum(0);
            theSlider.setMaximum(10);
            theSlider.setValue(0);
            bottomPanel.setVisible(true);

            blur(theSlider.getValue());
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        } else if (action.equals(cropMenuItem.getActionCommand())) {
            leftSliderLabel.setText("Cropping Percentage:");
            theSlider.setMinimum(1);
            theSlider.setMaximum(100);
            theSlider.setValue(100);
            bottomPanel.setVisible(true);

            crop(theSlider.getValue());
            revertMenuItem.setEnabled(true);
            askToQuit = true;

        }
    }

    @Override
    public void buttonClicked(JButton whichButton) {
        String action = whichButton.getActionCommand();

        if (action.equals(acceptButton.getActionCommand())) {

            thePPMImage = new PPMImage (theWorkingImage.getRedPixels(),theWorkingImage.getGreenPixels(),theWorkingImage.getBluePixels());

            fileMenu.setEnabled(true);
            toolsMenu.setEnabled(true);
            bottomPanel.setVisible(false);

        } else if (action.equals(cancelButton.getActionCommand())) {
            imageLabel.setIcon(new SImage(
                    transposeArray(thePPMImage.getRedPixels()),
                    transposeArray(thePPMImage.getGreenPixels()),
                    transposeArray(thePPMImage.getBluePixels())
            ));

            fileMenu.setEnabled(true);
            toolsMenu.setEnabled(true);
            bottomPanel.setVisible(false);
        }
    }

    @Override
    public void sliderChanged(JSlider whichSlider) {
        int amount = whichSlider.getValue();

        rightSliderLabel.setText(Integer.toString(amount));

        if (leftSliderLabel.getText().equals("Brightness:")) {
            adjustBrightness(amount);

        } else if (leftSliderLabel.getText().equals("Contrast:")) {
            adjustContrast(amount);

        } else if (leftSliderLabel.getText().equals("Change Levels:")) {
            changeLevels(amount);

        } else if (leftSliderLabel.getText().equals("Radius:")) {
            blur(amount);

        } else if (leftSliderLabel.getText().equals("Cropping Percentage:")) {
            crop(amount);
        }

    }

    public void loadImage() {

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile().getAbsolutePath();
            if (filename.endsWith(".ppm") || filename.endsWith(".PPM")) {
                try {
                    // The selected image is a PPM.  Create a new PPMImage instance.
                    thePPMImage = new PPMImage(filename);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Exception in opening file",
                            "Read Error", JOptionPane.ERROR_MESSAGE);
                }

                int numRows = thePPMImage.getNumRows();
                int numCols = thePPMImage.getNumCols();

                // Now that we know the actual image size, resize the window
                super.setSize(numCols + 20, numRows + 75);

                    // Create an SImage from the PPMImage object and display it in the label
                // NOTE: SImage presumes the first dimension is cols, second rows!  
                imageLabel.setIcon(new SImage(
                        transposeArray(thePPMImage.getRedPixels()),
                        transposeArray(thePPMImage.getGreenPixels()),
                        transposeArray(thePPMImage.getBluePixels())
                ));

                revertMenuItem.setEnabled(true);
                saveAsMenuItem.setEnabled(true);
                revertMenuItem.setEnabled(true);
                toolsMenu.setEnabled(true);
                grayscaleMenuItem.setEnabled(true);

            } else {
                // this can't read anything but ppm!
                JOptionPane.showMessageDialog(this, "Can only read PPM!",
                        "Read Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveImage() {

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // we presume that we must write as a ppm
            String filename = chooser.getSelectedFile().getAbsolutePath();

            if (!filename.endsWith(".ppm") && !filename.endsWith(".PPM")) {
                filename += ".ppm";
            }
            try {
                if (new File(filename).exists()) {
                    JOptionPane.showMessageDialog(this, "File Already Exists",
                            "Write Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    thePPMImage.writePPMImage(filename);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Exception in opening file",
                        "Write Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void quit() {
        if (askToQuit) {
            Object[] options = {"Yes", "No"};
            int answer = JOptionPane.showOptionDialog(this,
                    "Do you really want to quit?", "Unsave Warning", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (answer == 0) {
                System.exit(0);
            } else {
                return;
            }
        } else {
            System.exit(0);
        }
    }

    public void grayScale() {
        // user pressed grayscale button
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();
        int[][] grayVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];

        for (int i = 0; i < thePPMImage.getNumRows(); i++) {
            for (int j = 0; j < thePPMImage.getNumCols(); j++) {
                grayVals[i][j] = (int) (0.2126 * redVals[i][j] + 0.7152 * greenVals[i][j] + 0.0722 * blueVals[i][j]);
            }
        }
        thePPMImage.setRedPixels(grayVals);
        thePPMImage.setGreenPixels(grayVals);
        thePPMImage.setBluePixels(grayVals);

        imageLabel.setIcon(new SImage(
                transposeArray(thePPMImage.getRedPixels()),
                transposeArray(thePPMImage.getGreenPixels()),
                transposeArray(thePPMImage.getBluePixels())
        ));
        grayscaleMenuItem.setEnabled(false);
    }

    public void invert() {
        // user pressed invert button
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] iRedVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] iGreenVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] iBlueVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];

        for (int i = 0; i < thePPMImage.getNumRows(); i++) {
            for (int j = 0; j < thePPMImage.getNumCols(); j++) {
                iRedVals[i][j] = 255 - redVals[i][j];
                iGreenVals[i][j] = 255 - greenVals[i][j];
                iBlueVals[i][j] = 255 - blueVals[i][j];

            }
        }

        thePPMImage = new PPMImage (iRedVals, iGreenVals, iBlueVals);

        imageLabel.setIcon(new SImage(
                transposeArray(thePPMImage.getRedPixels()),
                transposeArray(thePPMImage.getGreenPixels()),
                transposeArray(thePPMImage.getBluePixels())
        ));

    }

    public void doubleImage() {
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[2 * thePPMImage.getNumRows()][2 * thePPMImage.getNumCols()];
        int[][] newGreenVals = new int[2 * thePPMImage.getNumRows()][2 * thePPMImage.getNumCols()];
        int[][] newBlueVals = new int[2 * thePPMImage.getNumRows()][2 * thePPMImage.getNumCols()];

        for (int i = 0; i < thePPMImage.getNumRows(); i++) {
            for (int j = 0; j < thePPMImage.getNumCols(); j++) {

                newRedVals[2 * i][2 * j] = redVals[i][j];
                newRedVals[2 * i + 1][2 * j] = redVals[i][j];
                newRedVals[2 * i][2 * j + 1] = redVals[i][j];
                newRedVals[2 * i + 1][2 * j + 1] = redVals[i][j];
                newGreenVals[2 * i][2 * j] = greenVals[i][j];
                newGreenVals[2 * i + 1][2 * j] = greenVals[i][j];
                newGreenVals[2 * i][2 * j + 1] = greenVals[i][j];
                newGreenVals[2 * i + 1][2 * j + 1] = greenVals[i][j];
                newBlueVals[2 * i][2 * j] = blueVals[i][j];
                newBlueVals[2 * i + 1][2 * j] = blueVals[i][j];
                newBlueVals[2 * i][2 * j + 1] = blueVals[i][j];
                newBlueVals[2 * i + 1][2 * j + 1] = blueVals[i][j];

            }
        }
        thePPMImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);
        

        imageLabel.setIcon(new SImage(
                transposeArray(thePPMImage.getRedPixels()),
                transposeArray(thePPMImage.getGreenPixels()),
                transposeArray(thePPMImage.getBluePixels())
        ));
    }

    public void halveImage() {
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[thePPMImage.getNumRows() / 2][thePPMImage.getNumCols() / 2];
        int[][] newGreenVals = new int[thePPMImage.getNumRows() / 2][thePPMImage.getNumCols() / 2];
        int[][] newBlueVals = new int[thePPMImage.getNumRows() / 2][thePPMImage.getNumCols() / 2];

        /*for(int i = 0; i < thePPMImage.getNumRows(); i++){
         for(int j = 0; j< thePPMImage.getNumCols(); j++) {
                     
         newRedVals[i/2][j/2] = redVals[i][j];
         newGreenVals[i/2][j/2] = greenVals[i][j];
         newBlueVals[i/2][j/2] = blueVals[i][j];
                     
         }
         }*/
        for (int i = 0; i < thePPMImage.getNumRows() / 2; i++) {
            for (int j = 0; j < thePPMImage.getNumCols() / 2; j++) {

                newRedVals[i][j] = (redVals[2 * i][2 * j] + redVals[2 * i + 1][2 * j] + redVals[2 * i][2 * j + 1] + redVals[2 * i + 1][2 * j + 1]) / 4;
                newGreenVals[i][j] = (greenVals[2 * i][2 * j] + greenVals[2 * i + 1][2 * j] + greenVals[2 * i][2 * j + 1] + greenVals[2 * i + 1][2 * j + 1]) / 4;
                newBlueVals[i][j] = (blueVals[2 * i][2 * j] + blueVals[2 * i + 1][2 * j] + blueVals[2 * i][2 * j + 1] + blueVals[2 * i + 1][2 * j + 1]) / 4;

            }
        }

        thePPMImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);
       

        imageLabel.setIcon(new SImage(
                transposeArray(thePPMImage.getRedPixels()),
                transposeArray(thePPMImage.getGreenPixels()),
                transposeArray(thePPMImage.getBluePixels())
        ));
    }

    public void chromaKey() {
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                newImage = new PPMImage(filename);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Exception in opening file",
                        "Read Error", JOptionPane.ERROR_MESSAGE);
            }

            int[][] newRedVals = newImage.getRedPixels();
            int[][] newGreenVals = newImage.getGreenPixels();
            int[][] newBlueVals = newImage.getBluePixels();

            for (int i = 0; i < newImage.getNumRows() && i < thePPMImage.getNumRows() ; i++) {
                for (int j = 0; j < newImage.getNumCols() && j < thePPMImage.getNumCols(); j++) {
                    if (redVals[i][j] < 10 && blueVals[i][j] < 100 && greenVals[i][j] > 200) {
                        
                        redVals[i][j] = newRedVals[i][j];
                        greenVals[i][j] = newGreenVals[i][j];
                        blueVals[i][j] = newBlueVals[i][j];
                        
                    } newRedVals [i][j]= redVals [i][j];

                }
            }
            
                thePPMImage = new PPMImage(redVals, greenVals, blueVals);
                

                imageLabel.setIcon(new SImage(
                        transposeArray(thePPMImage.getRedPixels()),
                        transposeArray(thePPMImage.getGreenPixels()),
                        transposeArray(thePPMImage.getBluePixels())
                ));
            
        }
    }

    public void adjustBrightness(int amount) { 
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newGreenVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newBlueVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];

        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        toolsMenu.setEnabled(false);
        theWorkingImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);

        for (int i = 0; i < theWorkingImage.getNumRows(); i++) {
            for (int j = 0; j < theWorkingImage.getNumCols(); j++) {

                newRedVals[i][j] = redVals[i][j] + amount;
                newGreenVals[i][j] = greenVals[i][j] + amount;
                newBlueVals[i][j] = blueVals[i][j] + amount;

                if (newRedVals[i][j] > 255) {
                    newRedVals[i][j] = 255;
                }
                if (newGreenVals[i][j] > 255) {
                    newGreenVals[i][j] = 255;
                }
                if (newBlueVals[i][j] > 255) {
                    newRedVals[i][j] = 255;
                }
                if (newRedVals[i][j] < 0) {
                    newRedVals[i][j] = 0;
                }
                if (newGreenVals[i][j] < 0) {
                    newGreenVals[i][j] = 0;
                }
                if (newBlueVals[i][j] < 0) {
                    newBlueVals[i][j] = 0;
                }

            }
        }

        theWorkingImage = new PPMImage(newRedVals,newGreenVals, newBlueVals);

        imageLabel.setIcon(new SImage(
                transposeArray(theWorkingImage.getRedPixels()),
                transposeArray(theWorkingImage.getGreenPixels()),
                transposeArray(theWorkingImage.getBluePixels())
        ));
    }

    public void adjustContrast(int amount) { //check this
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newGreenVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newBlueVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];

        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        toolsMenu.setEnabled(false);

        theWorkingImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);

        for (int i = 0; i < theWorkingImage.getNumRows(); i++) {
            for (int j = 0; j < theWorkingImage.getNumCols(); j++) {

                newRedVals[i][j] = (int) (((amount * (redVals[i][j] - 128.0)) / (256.0 - amount)) + 128.0);
                newGreenVals[i][j] = (int) (((amount * (greenVals[i][j] - 128.0)) / (256.0 - amount)) + 128.0);
                newBlueVals[i][j] = (int) (((amount * (blueVals[i][j] - 128.0)) / (256.0 - amount)) + 128.0);

            }
        }

        theWorkingImage = new PPMImage(newRedVals,newGreenVals, newBlueVals);

        imageLabel.setIcon(new SImage(
                transposeArray(theWorkingImage.getRedPixels()),
                transposeArray(theWorkingImage.getGreenPixels()),
                transposeArray(theWorkingImage.getBluePixels())
        ));
    }

    public void changeLevels(int numLevels) {
        int binWidth = (int) (256.0 / theSlider.getValue());

        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newGreenVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newBlueVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];

        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        toolsMenu.setEnabled(false);

        theWorkingImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);

        for (int i = 0; i < theWorkingImage.getNumRows(); i++) {
            for (int j = 0; j < theWorkingImage.getNumCols(); j++) {
                newRedVals[i][j] = (redVals[i][j] / binWidth) * binWidth;
                newGreenVals[i][j] = (greenVals[i][j] / binWidth) * binWidth;
                newBlueVals[i][j] = (blueVals[i][j] / binWidth) * binWidth;
            }
        }

        theWorkingImage = new PPMImage(newRedVals,newGreenVals, newBlueVals);

        imageLabel.setIcon(new SImage(
                transposeArray(theWorkingImage.getRedPixels()),
                transposeArray(theWorkingImage.getGreenPixels()),
                transposeArray(theWorkingImage.getBluePixels())
        ));
    }

    public void blur(int blurRadius) {
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newGreenVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];
        int[][] newBlueVals = new int[thePPMImage.getNumRows()][thePPMImage.getNumCols()];

        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        toolsMenu.setEnabled(false);

        int sumRed;
        int sumGreen;
        int sumBlue;
        int numRows = thePPMImage.getNumRows();
        int numCols = thePPMImage.getNumCols();

        theWorkingImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);

        for (int i = 0; i < theWorkingImage.getNumRows(); i++) {
            for (int j = 0; j < theWorkingImage.getNumCols(); j++) {
                sumRed = 0;
                sumGreen = 0;
                sumBlue = 0;

                for (int k = i - blurRadius; k <= i + blurRadius; k++) {
                    for (int l = j - blurRadius; l <= j + blurRadius; l++) {
                        sumRed = sumRed + redVals[(k + numRows) % numRows][(l + numCols) % numCols];
                        sumGreen = sumGreen + greenVals[(k + numRows) % numRows][(l + numCols) % numCols];
                        sumBlue = sumBlue + blueVals[(k + numRows) % numRows][(l + numCols) % numCols];
                    }
                }
                newRedVals[i][j] = (int) (sumRed / Math.pow((blurRadius * 2 + 1), 2));
                newGreenVals[i][j] = (int) (sumGreen / Math.pow((blurRadius * 2 + 1), 2));
                newBlueVals[i][j] = (int) (sumBlue / Math.pow((blurRadius * 2 + 1), 2));
            }
        }

        theWorkingImage = new PPMImage(newRedVals,newGreenVals, newBlueVals);

        imageLabel.setIcon(new SImage(
                transposeArray(theWorkingImage.getRedPixels()),
                transposeArray(theWorkingImage.getGreenPixels()),
                transposeArray(theWorkingImage.getBluePixels())
        ));
    }

    public void crop(int amount) {
        int[][] redVals = thePPMImage.getRedPixels();
        int[][] greenVals = thePPMImage.getGreenPixels();
        int[][] blueVals = thePPMImage.getBluePixels();

        int[][] newRedVals = new int[amount * thePPMImage.getNumRows() / 100][amount * thePPMImage.getNumCols() / 100];
        int[][] newGreenVals = new int[amount * thePPMImage.getNumRows() / 100][amount * thePPMImage.getNumCols() / 100];
        int[][] newBlueVals = new int[amount * thePPMImage.getNumRows() / 100][amount * thePPMImage.getNumCols() / 100];

        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        toolsMenu.setEnabled(false);
        theWorkingImage = new PPMImage(newRedVals, newGreenVals, newBlueVals);

        for (int i = 0; i < theWorkingImage.getNumRows(); i++) {
            for (int j = 0; j < theWorkingImage.getNumCols(); j++) {
                newRedVals[i][j] = redVals[i][j];
                newGreenVals[i][j] = greenVals[i][j];
                newBlueVals[i][j] = blueVals[i][j];
            }

        }

        theWorkingImage = new PPMImage (newRedVals,newGreenVals, newBlueVals);

        imageLabel.setIcon(new SImage(
                transposeArray(theWorkingImage.getRedPixels()),
                transposeArray(theWorkingImage.getGreenPixels()),
                transposeArray(theWorkingImage.getBluePixels())
        ));
    }

    public void revert() {

        if (filename.endsWith(".ppm") || filename.endsWith(".PPM")) {
            try {
                // The selected image is a PPM.  Create a new PPMImage instance.
                thePPMImage = new PPMImage(filename);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Exception in opening file",
                        "Read Error", JOptionPane.ERROR_MESSAGE);
            }

            int numRows = thePPMImage.getNumRows();
            int numCols = thePPMImage.getNumCols();

            // Now that we know the actual image size, resize the window
            super.setSize(numCols + 20, numRows + 75);

        // Create an SImage from the PPMImage object and display it in the label
            // NOTE: SImage presumes the first dimension is cols, second rows!  
            imageLabel.setIcon(new SImage(
                    transposeArray(thePPMImage.getRedPixels()),
                    transposeArray(thePPMImage.getGreenPixels()),
                    transposeArray(thePPMImage.getBluePixels())
            ));

            // Now that an image is displayed; enbable the button
            revertMenuItem.setEnabled(false);
            saveAsMenuItem.setEnabled(false);
            toolsMenu.setEnabled(true);
            grayscaleMenuItem.setEnabled(true);

        } else {
            // this can't read anything but ppm!
            JOptionPane.showMessageDialog(this, "Can only read PPM!",
                    "Read Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    // Utility function for transposing a 2-D array of integers
    private int[][] transposeArray(int[][] inArray) {
        int[][] outArray = new int[inArray[0].length][inArray.length];
        for (int i = 0; i < inArray.length; i++) {
            for (int j = 0; j < inArray[0].length; j++) {
                outArray[j][i] = inArray[i][j];
            }
        }
        return outArray;
    }

    public static void main(String[] args) {
        Fauxtoshop pViewer = new Fauxtoshop();  // construct instance of the class
    }
}
