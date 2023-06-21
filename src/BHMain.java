import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

// Starter by Ian Gent, Sep 2021
// This class is provided to save you writing some of the basic parts of the code
// Also to provide a uniform command line structure

// Edit history:
// V1 released 20 Sep 2021
// V2 released 21 Sep 2021
//      corrected error in usage statement (file argument is required not optional)

public class BHMain {

    public static void printUsage() {
        System.out.println("Input not recognised.  Usage is:");
        System.out.println("java BHmain GEN|CHECK|SOLVE|CHECKWORM|SOLVEWORM <arguments>"  );
        System.out.println("     GEN arguments are seed [numcards=51] [numranks=13] [numsuits=4] [numpiles=17]");
        System.out.println("                       all except seed may be omitted, defaults shown");
        System.out.println("     CHECK/CHECKWORM argument is file1 [file2]");
        System.out.println("                     if file1 - then stdin is used");
        System.out.println("                     if file2 is ommitted or is - then stdin is used");
        System.out.println("                     at least one of file1/file2 must be a filename and not stdin");
        System.out.println("     SOLVE/SOLVEWORM argument is file");
        System.out.println("                     if file - then stdin is used");
    }


    public static ArrayList<Integer> readIntArray(String filename) {
        // File opening sample code from
        // https://www.w3schools.com/java/java_files_read.asp
        ArrayList<Integer> result  ;
        Scanner reader;
        try {
            File file = new File(filename);
            reader = new Scanner(file);
            result=readIntArray(reader);
            reader.close();
            return result;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        // drop through case
        return new ArrayList<Integer>(0);

    }
        

    public static ArrayList<Integer> readIntArray(Scanner reader) {
        ArrayList<Integer> result = new ArrayList<Integer>(0);
        while( reader.hasNextInt()  ) {
            result.add(reader.nextInt());
        }
        return result;
    }

    /*
     * Plays the game out on a copy of the game, returns false if a move is an invalid one
     * @param gameOriginal
     * @param workingList
     * @return boolean that shows if the solution given is the correct one
     */
    private static boolean checkSolution(Game gameOriginal, ArrayList<Integer> workingList) {

        Game game = gameOriginal.copy();

        // check game over after the loop
        for(int i = 0; i < workingList.size(); i += 2) {
            int currentPile = workingList.get(i);
            int currentCard = workingList.get(i+1);

            if(!game.play(currentPile, currentCard)) break;
        }
        return game.gameOver();
    }

    public static DFSState depthfirstsearchrec(DFSState state) {
        if(state.game.gameOver()) {return state;}

        ArrayList<Move> possiblemoves = state.game.possibleMoves();

        for(Move move: possiblemoves) {
            DFSState currState = new DFSState(state.game.copy(), new ArrayList<>(state.moves));
            currState.game.play(move.pile, move.card);
            currState.moves.add(move);
            DFSState retState = depthfirstsearchrec(currState);
            if(retState != null) return retState;
        }
        return null;
    }

    /*
     * Iterative dfs
     * Uses a stack to store the game state
     * @param game
     * @return DFSState which is the game solution
     */
    public static DFSState depthfirstsearch(Game game) {
        Stack<DFSState> stack = new Stack<DFSState>();
        DFSState curr = null;
        stack.push(new DFSState(game));

        while(!stack.isEmpty()) {
            curr = stack.pop();
            if(curr.game.gameOver()) {return curr;}
            for(Move move: curr.game.possibleMoves()) {
                DFSState state = new DFSState(curr.game.copy(), new ArrayList<>(curr.moves));
                state.game.play(move.pile, move.card);
                state.moves.add(move);
                stack.push(state);
            }
        }
        return curr.game.gameOver() ? curr : null;
    }

    public static void main(String[] args) {

        Scanner stdInScanner = new Scanner(System.in);
        ArrayList<Integer> workingList;

        BHLayout layout;
        DFSState result;

        int seed ;
        int numcards ;
        int ranks ;
        int suits ;
        int piles ;
       
        if(args.length < 2) { printUsage(); return; };


        switch (args[0].toUpperCase()) {
            case "GEN":
                if(args.length < 2) { printUsage(); return; };
                seed = Integer.parseInt(args[1]);
                numcards = (args.length < 3 ? 51 : Integer.parseInt(args[2])) ;
                ranks = (args.length < 4 ? 13 : Integer.parseInt(args[3])) ;
                suits = (args.length < 5 ? 4 : Integer.parseInt(args[4])) ;
                piles = (args.length < 6 ? 17 : Integer.parseInt(args[5])) ;
                layout = new BHLayout(ranks,suits,piles);
                layout.randomise(seed,numcards);
                layout.print();
                stdInScanner.close();
                return;

            case "CHECK":
                if (args.length < 2 ||
                    ( args[1].equals("-") && args.length < 3) ||
                    ( args[1].equals("-") && args[2].equals("-"))
                    )
                    { printUsage(); return; };

                if (args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                } else {
                    layout = new BHLayout(readIntArray(args[1]));
                }

                if (args.length < 3 || args[2].equals("-")) {
                    workingList = readIntArray(stdInScanner);
                } else {
                    workingList = readIntArray(args[2]);
                }

                // CODE FOR CHECKING BLACK HOLE SOLUTIONS
                System.out.println(checkSolution(new BHGame(layout), workingList));

                stdInScanner.close();
                return;

            case "CHECKWORM":
                if (args.length < 2 ||
                    ( args[1].equals("-") && args.length < 3) ||
                    ( args[1].equals("-") && args[2].equals("-"))
                    )
                    { printUsage(); return; };
                if (args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                }
                else {
                    layout = new BHLayout(readIntArray(args[1]));
                }
                if (args.length < 3 || args[2].equals("-")) {
                    workingList = readIntArray(stdInScanner);
                }
                else {
                    workingList = readIntArray(args[2]);
                }

                // CODE FOR CHECKING WORM HOLE SOLUTIONS
                System.out.println(checkSolution(new WHGame(layout), workingList));

                stdInScanner.close();
                return;

            case "SOLVE":
                if (args.length<2 || args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                } else {
                    layout = new BHLayout(readIntArray(args[1]));
                }

                /// CODE FOR SOLVING BLACK HOLE
                result = depthfirstsearch(new BHGame(layout));
                if(result == null)
                    System.out.println(0);
                else {
                    System.out.print(1);
                    for(Move move: result.moves) {
                        System.out.print(" " + move.pile + " " + move.card);
                    }
                }

                stdInScanner.close();
                return;

            case "SOLVEWORM":
                if (args.length<2 || args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                }
                else {
                    layout = new BHLayout(readIntArray(args[1]));
                }

                /// CODE FOR SOLVING WORM HOLE
                result = depthfirstsearch(new WHGame(layout));
                if(result == null)
                    System.out.println(0);
                else {
                    System.out.print(1);
                    for(Move move: result.moves) {
                        System.out.print(" " + move.pile + " " + move.card);
                    }
                }

                stdInScanner.close();
                return;

            default :
                printUsage();
                return;
        }
    }
}
