

import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@SuppressWarnings("deprecation")
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
	    
	    	    int[][] s1 = {
	    		{3, 0, 2, 0, 5, 1, 0, 0, 8},
	    		{0, 0, 0, 0, 9, 0, 1, 3, 0},
	    		{4, 0, 9, 0, 8, 7, 0, 0, 2},
	    		{0, 0, 4, 0, 3, 2, 0, 0, 0},
	    		{1, 0, 6, 0, 0, 0, 2, 0, 7},
	    		{0, 0, 0, 7, 6, 0, 3, 0, 0},
	    		{7, 0, 0, 6, 2, 0, 5, 0, 3},
	    		{0, 8, 5, 0, 7, 0, 0, 0, 0},
	    		{6, 0, 0, 8, 1, 0, 7, 0, 9},
	    			    		
	    };
	    	    int [][] s2 = {
	    	    		{8, 3, 0, 0, 2, 9, 0, 0, 0},
	    	    		{0, 9, 0, 7, 0, 0, 0, 6, 0},
	    	    		{4, 0, 0, 0, 1, 0, 2, 0, 0},
	    	    		{0, 4, 8, 0, 0, 2, 0, 1, 9},
	    	    		{0, 0, 9, 0, 0, 0, 4, 0, 0},
	    	    		{1, 2, 0, 9, 0, 0, 3, 5, 0},
	    	    		{0, 0, 4, 0, 6, 0, 0, 0, 7},
	    	    		{0, 5, 0, 0, 0, 1, 0, 2, 0},
	    	    		{0, 0, 0, 3, 5, 0, 0, 4, 1},		
	    	    };   	    

	    	    int [][] s3 = {
	    	    		{0, 0, 0, 0, 0, 6, 0, 8, 0},
	    	    		{0, 0, 9, 1, 0, 5, 3, 7, 2},
	    	    		{0, 8, 0, 7, 0, 0, 0, 1, 6},
	    	    		{0, 0, 0, 0, 0, 0, 0, 3, 4},
	    	    		{0, 0, 0, 3, 5, 1, 0, 0, 0},
	    	    		{7, 3, 0, 0, 0, 0, 0, 0, 0},
	    	    		{6, 1, 0, 0, 0, 8, 0, 2, 0},
	    	    		{8, 2, 3, 9, 0, 4, 6, 0, 0},
	    	    		{0, 7, 0, 6, 0, 0, 0, 0, 0},	
	    	    };
	    	    	    
	    try {
	        line = parser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	        System.err.println( "Parsing failed. Reason: " + exp.getMessage() );
	    }
	    
	   
	    int seedForGeneratingSudokuBoard = 0;
	    boolean withSeed = false;
	    int numbersToRemoveFromInitialBoardCount = 0;
	    
	    boolean printBoardDuringRun = true;
	    
	    if(line.hasOption("help")){
	    	HelpFormatter formatter = new HelpFormatter();
	    	formatter.printHelp("tabu search sudoku solver", options);
	    	return;
	    }
	    
//	    if(!line.hasOption("short") || !line.hasOption("maxIt") || !line.hasOption("remove")) {
//	    	System.out.println("One of obligatory arguments not given!");
//	    	return;
//	    }
//	    else {
//	    	shortTermTabuListSize = extractNumericalOption(line, "short");
//	    	if(line.hasOption("long"))
//		    	longTermTabuListSize = extractNumericalOption(line, "long");
//	    	else
//	    		longTermTabuListSize = 0;
//	    	maxIterationsCount = shortTermTabuListSize = extractNumericalOption(line, "maxIt");
//	    	useAspirationCriterion = line.hasOption("useAsp");
//	    	
//	    	if(line.hasOption("seed")){
//	    		seedForGeneratingSudokuBoard = shortTermTabuListSize = extractNumericalOption(line, "seed");
//	    		withSeed = true;
//	    	}
//	    	else
//	    		withSeed = false;
//	    	
//	    	numbersToRemoveFromInitialBoardCount = shortTermTabuListSize = extractNumericalOption(line, "leave");
//	    	
//	    	printBoardDuringRun = line.hasOption("print");
//	    }
	    
	    BoardGenerator boardGenerator = null;
	    if(withSeed)
	    	boardGenerator = new BoardGenerator(seedForGeneratingSudokuBoard);
	    else
	    	boardGenerator = new BoardGenerator();
	    
	    boardGenerator.generateCorrectBoard(9);
	    boardGenerator.removeNumbers(numbersToRemoveFromInitialBoardCount);
	    int [][] initialBoard = boardGenerator.getCurrentBoard(); 
		
	    int shortTermTabuListSize = 50;
	    int longTermTabuListSize = 5;
	    int maxIterationsCount = 300;
	    boolean useAspirationCriterion = true;
	    
	    
	    
	    SudokuEngine sudokuEngine = null;
	    long start;
	    start = System.currentTimeMillis();
		try {
			sudokuEngine = new SudokuEngine(s1, shortTermTabuListSize, useAspirationCriterion,
					longTermTabuListSize, maxIterationsCount, printBoardDuringRun);
			sudokuEngine.runTabuSearch();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		long end = System.currentTimeMillis();
		System.out.println("===");
		System.out.println("Short term list size: " + shortTermTabuListSize);
		System.out.println("Long term list size: " + longTermTabuListSize);
		System.out.println("Use of aspiration criterion: " + useAspirationCriterion);
		System.out.println("Removed numbers count: " + numbersToRemoveFromInitialBoardCount);
		
		System.out.println("===");
		System.out.println("Iterations count: " + sudokuEngine.getIterationsCount());
		System.out.println("Conflicts number: " + sudokuEngine.getCurrentCostFunctionValue());
		System.out.println("Time Required: " + (end -start));
		
		System.out.println("Result: "); 
		Helpers.printBoard(s1);
		
	}
}