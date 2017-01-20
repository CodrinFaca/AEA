package runner;

import java.io.IOException;

import main.BoardGenerator;
import main.SudokuEngine;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Runner {
	
	private static int extractNumericalOption(CommandLine line, String optionName){
		String optionValue = line.getOptionValue(optionName);
    	return Integer.parseInt(optionValue);
	}
	
	public static void main(String[] args) throws IOException {
		Options options = new Options();
		
		options.addOption("help", false, "print this message");
		
		options.addOption("short", true, "short term tabu list size");
		options.addOption("long", true, "long term tabu list size, if not given there is no long term list");
		options.addOption("maxIt", true, "max iterations count");
		options.addOption("useAsp", false, "sets use of apiration criterion");
		
		options.addOption("seed", true,
				"sets seed of generating Sudoku board, if not present different board is generated every time");
		options.addOption("leave", true, "how many numbers are left in initial board");
		
		options.addOption("print", false, "sets printing of Sudoku board during algorithm run");
		
	    CommandLineParser parser = new BasicParser();
	    CommandLine line = null;
	    try {
	        line = parser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	        System.err.println( "Parsing failed. Reason: " + exp.getMessage() );
	    }
	    
	    int shortTermTabuListSize = 0;
	    int longTermTabuListSize = 0;
	    int maxIterationsCount = 0;
	    boolean useAspirationCriterion = false;
	    
	    int seedForGeneratingSudokuBoard = 0;
	    boolean withSeed = false;
	    int numbersToRemoveFromInitialBoardCount = 0;
	    
	    boolean printBoardDuringRun = false;
	    
	    if(line.hasOption("help")){
	    	HelpFormatter formatter = new HelpFormatter();
	    	formatter.printHelp("tabu search sudoku solver", options);
	    	return;
	    }
	    
	    if(!line.hasOption("short") || !line.hasOption("maxIt") || !line.hasOption("remove")) {
	    	System.out.println("One of obligatory arguments not given!");
	    	return;
	    }
	    else {
	    	shortTermTabuListSize = extractNumericalOption(line, "short");
	    	if(line.hasOption("long"))
		    	longTermTabuListSize = extractNumericalOption(line, "long");
	    	else
	    		longTermTabuListSize = 0;
	    	maxIterationsCount = shortTermTabuListSize = extractNumericalOption(line, "maxIt");
	    	useAspirationCriterion = line.hasOption("useAsp");
	    	
	    	if(line.hasOption("seed")){
	    		seedForGeneratingSudokuBoard = shortTermTabuListSize = extractNumericalOption(line, "seed");
	    		withSeed = true;
	    	}
	    	else
	    		withSeed = false;
	    	
	    	numbersToRemoveFromInitialBoardCount = shortTermTabuListSize = extractNumericalOption(line, "leave");
	    	
	    	printBoardDuringRun = line.hasOption("print");
	    }
	    
	    BoardGenerator boardGenerator = null;
	    if(withSeed)
	    	boardGenerator = new BoardGenerator(seedForGeneratingSudokuBoard);
	    else
	    	boardGenerator = new BoardGenerator();
	    
	    boardGenerator.generateCorrectBoard(9);
	    boardGenerator.removeNumbers(numbersToRemoveFromInitialBoardCount);
	    int [][] initialBoard = boardGenerator.getCurrentBoard(); 
		
	    SudokuEngine sudokuEngine = null;
	    
		try {
			sudokuEngine = new SudokuEngine(initialBoard, shortTermTabuListSize, useAspirationCriterion,
					longTermTabuListSize, maxIterationsCount, printBoardDuringRun);
			sudokuEngine.runTabuSearch();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
		System.out.println("===");
		System.out.println("Short term list size: " + shortTermTabuListSize);
		System.out.println("Long term list size: " + longTermTabuListSize);
		System.out.println("Use of aspiration criterion: " + useAspirationCriterion);
		System.out.println("Removed numbers count: " + numbersToRemoveFromInitialBoardCount);
		
		System.out.println("===");
		System.out.println("Iterations count: " + sudokuEngine.getIterationsCount());
		System.out.println("Conflicts number: " + sudokuEngine.getCurrentCostFunctionValue());
	}
}