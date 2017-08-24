/**
 * Final Project: Image Editor Image
 *
 * @author Bilawal Samad Shaikh
 * @version 4th December 2015
 */


import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;


public class PPMImage {

    int [][] redArray;
    int [][] greenArray;
    int [][] blueArray;
    int numCols;
    int numRows;
    
    public PPMImage(String filename) throws Exception {

        readPPMImage(filename);
	
    }
    
    public PPMImage(int[][] reds, int[][] grns, int[][] blus) {

        numRows = reds.length;
	numCols = reds[0].length;
	
	redArray = new int [numRows][numCols];
	greenArray = new int [numRows][numCols];
	blueArray = new int [numRows][numCols];
	
	    for(int i = 0; i < numRows; i++){
		for(int j = 0; j< numCols; j++){
			redArray[i][j] = reds[i][j];
			greenArray[i][j] = grns[i][j];
			blueArray[i][j] = blus[i][j];
		}
	}
    }

    //Accessors (getters)
    public int getNumRows() {

        return numRows; 
    }

    public int getNumCols() {

        return numCols;
    }

    public int[][] getRedPixels() {
        
       int [][] redPixels = new int [numRows][numCols];
       for(int i = 0; i < numRows; i++){
		for(int j = 0; j< numCols; j++){
			redPixels [i][j] = redArray [i][j];
    }
       }
          return redPixels;
          }

    public int[][] getGreenPixels() {

        int [][] greenPixels = new int [numRows][numCols];
       for(int i = 0; i < numRows; i++){
		for(int j = 0; j< numCols; j++){
			greenPixels [i][j] = greenArray [i][j];
    }
       }
          return greenPixels;
          }

    public int[][] getBluePixels() {

       int [][] bluePixels = new int [numRows][numCols];
       for(int i = 0; i < numRows; i++){
		for(int j = 0; j< numCols; j++){
			bluePixels [i][j] = blueArray [i][j];
    }
       }
          return bluePixels;
          }

    
    // Mutators (setters)
    public void setRedPixels(int[][] reds) {
     
        for(int i = 0; i < numRows; i++){
		for(int j = 0; j< numCols; j++){
			redArray [i][j] = reds [i][j];
		}
            }
	}
    public void setGreenPixels(int[][] greens) {

        for(int i = 0; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			greenArray [i][j] = greens [i][j];
		}
	}
    }
         
    public void setBluePixels(int[][] blues) {

        for(int i = 0; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			blueArray [i][j] = blues [i][j];
		}
            }

    }

//======================================================================
    //* private void readPPMImage( String filename )
    //* 
    //* This method should read a file whose contents are presumed to follow
    //* the PPM image format and store those contents into an array.  The
    //* method should construct and populate the redArray, greenArray and 
    //* blueArray data from the PPM file stored on disk.  
    //* Recall that the PPM image format is:
    //*        P3
    //*        # comment
    //*        width(cols) height(rows)
    //*        maxColorLevel(probably 255)
    //*        pixel1R pixel1G pixel1B pixel2R pixel2G pixel2B pixel3R ...
    //======================================================================
    private void readPPMImage(String filename) throws Exception {

        FileReader inFW = new FileReader (filename);
	Scanner fin = new Scanner (inFW);
	
	String magicNum = fin.nextLine();
	String comment  = fin.nextLine();
        numCols  = fin.nextInt();
        numRows  = fin.nextInt();
        int maxColorLevel = fin.nextInt();
	
	redArray = new int [numRows][numCols];
	greenArray = new int [numRows][numCols];
	blueArray = new int [numRows][numCols];
	
	
	for (int i = 0; i < numRows; i++) {
		for (int j = 0; j < numCols; j++) {
			
		redArray [i][j] = fin.nextInt();
		greenArray [i][j] = fin.nextInt();
		blueArray [i][j] = fin.nextInt();
	}
	}
 
    }

    //======================================================================
    //* private void writePPMImage( String filename )
    //* 
    //* This method should write to a file, whose name is given by the String
    //* parameter, the contents of the redArray, greenArray and blueArray data. 
    //* The contents should be written following the PPM image format:
    //*        P3
    //*        # comment
    //*        width(cols) height(rows)
    //*        maxColorLevel(probably 255)
    //*        pixel1R pixel1G pixel1B 
    //*        pixel2R pixel2G pixel2B 
    //*        pixel3R ...
    //======================================================================
    public void writePPMImage(String filename) throws Exception {
          FileWriter outFW = new FileWriter(filename);
          BufferedWriter fout = new BufferedWriter(outFW);
	  
	  fout.write("P3\n");
	  fout.write("# created by me!\n");
	  fout.write(numCols + " "+numRows+"\n");
          fout.write("255\n");
	  
	  for (int i = 0; i < numRows; i++) {
		for (int j = 0; j < numCols; j++) {
			fout.write(redArray[i][j]+"\n"); 
			fout.write(greenArray[i][j]+"\n"); 
			fout.write(blueArray[i][j]+"\n"); 
			
		}
		
        
	  }
	  fout.close();
    }
}
