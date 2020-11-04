/*
 * This file is responsible for calculating the time.
 * Created on: 13-10-2020
 * Author: Patva Viraj(19IT117)
 * Last Modified on: 01-11-2020
 */

package Wordoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Wordoku {
    public static final int GRID_6X6 = 6;
    public static final int GRID_9X9 = 9;
    public static final int GAME_MODE_EXPERT = 75;
    public static final int GAME_MODE_MEDIUM = 65;
    public static final int GAME_MODE_EASY = 50;
    private static final String SET_VALUE_9X9 = "123456789";
    private static final String SET_VALUE_6X6 = "123456";
    private static ArrayList<Character> convertBack;
    public static char[] toBeFilled;
    private static char[][] puzzle;
    private static int[][] miniPuzzle;
    private static char duplicatepuzzle[][];
    private Random random = new Random();

    public static final int[][] VALID_BOARD_6X6 = {
            {1, 2, 3, 4, 5, 6},
            {4, 5, 6, 1, 2, 3},
            {2, 3, 4, 5, 6, 1},
            {5, 6, 1, 2, 3, 4},
            {3, 4, 5, 6, 1, 2},
            {6, 1, 2, 3, 4, 5}};

    /*this method is used to create new puzzle will accept two arguments grid(9 or 6) and gamemode(out of 100)*/
    public char[][] getNewPuzzle(int grid, int gameMode) {
        puzzle = generatePuzzle(grid, gameMode);
        duplicatepuzzle = puzzle;
        return puzzle;
    }

    /*this method is used to get number of empty box and select which puzzle is to be created and will return puzzle*/
    private char[][] generatePuzzle(int grid, int gamemode) {
        if (grid == 9) {
            int emptyBox = getNumberOfEmptyBlock(grid * grid, gamemode);
            // int emptyBox = 0;
            return generateWordoku(emptyBox);
        } else if (grid == 6) {
            int emptyBox = getNumberOfEmptyBlock(grid * grid, gamemode);
            // int emptyBox = 0;
            return generateMiniWordoku(emptyBox, gamemode);
        }
        return puzzle;
    }

    /*the below method will count number of empty box according to difficulty level and grid size*/
    private int getNumberOfEmptyBlock(int grid, int mode) {
        int numOfEmptyBlock = 0;
        int numOfBlock = grid;

        if (mode >= GAME_MODE_EASY && mode < GAME_MODE_MEDIUM) {
            numOfEmptyBlock = (int) (grid * 0.5);
        } else if (mode >= GAME_MODE_MEDIUM && mode < GAME_MODE_EXPERT) {
            numOfEmptyBlock = (int) (grid * 0.625);
        } else {
            numOfEmptyBlock = (int) (grid * 0.75);
        }


        return numOfEmptyBlock;
    }

    /*below to method will take care of generating wordoku*/
    private char[][] generateWordoku(int empytbox) {
        int sudoku[][] = new int[9][9];
        char wordoku[][] = new char[9][9];
        createSudoku(sudoku);
        removeElement(empytbox, sudoku, 9);
        convertToAlphabets(sudoku, wordoku, 9);
        return wordoku;
    }

    public static void convertToAlphabets(int sudoku[][], char wordoku[][], int gridsize) {
        Random rand = new Random();
        char alpahabets[];
        //char[] toBeFilled;
        alpahabets = new char[gridsize + 1];
        toBeFilled = new char[gridsize];
        alpahabets[0] = ' ';
        char character;
        for (int i = 1; i < gridsize + 1; i++) {
            do {
                character = (char) ('A' + rand.nextInt(26));
            }
            while (!inArray(alpahabets, i, character));
            alpahabets[i] = character;
        }

        for (int i = 1; i < gridsize + 1; i++) {
            toBeFilled[i - 1] = alpahabets[i];
        }

        int num;
        for (int i = 0; i < gridsize; i++) {
            for (int j = 0; j < gridsize; j++) {
                num = sudoku[i][j];
                wordoku[i][j] = alpahabets[num];
            }
        }
        ArrayList<Character> list = new ArrayList<>();
        // System.out.println ("Character list that will be use to fill Wordoku : " + Arrays.toString (toBeFilled));
        for (int i = 0; i < gridsize; i++) {
            //System.out.println (toBeFilled[i]);
            list.add(toBeFilled[i]);
        }
        // System.out.println (list);
        convertBack = list;
        System.out.println(convertBack);

    }

    /*inArray method :
                this method will insure that none of the char in the wordoku is repeated.
     */
    public static boolean inArray(char alpha[], int i, char alphabet) {
        for (int j = 0; j < i; j++)
            if (alpha[j] == alphabet)
                return false;
        return true;
    }

    /* createSudoku method :
                        will take sudoku as a parameter
                        will fill different boxes of sudoku
                        step 1:it will fill diagonal boxes
                        step 2:will fill remaining boxes using recursion
    */
    public static void createSudoku(int sudoku[][]) {
        fillDiagonal(sudoku);
        fillOther(0, 3, sudoku);
    }

    /*
        fillDiagonal method:
                will fill only diagonal boxes using fillBox method
    */

    public static void fillDiagonal(int sudoku[][]) {
        for (int i = 0; i < 9; i = i + 3)
            fillBox(i, i, sudoku);
    }

    /* fillOther method:
                will fill only fillOther boxes using fillBox method
    */

    public static boolean fillOther(int i, int j, int sudoku[][]) {
        if (j >= 9 && i < 9 - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= 9 && j >= 9)
            return true;
        if (i < 3) {
            if (j < 3)
                j = 3;
        } else if (i < 6) {
            if (j == (int) (i / 3) * 3)
                j = j + 3;
        } else {
            if (j == 6) {
                i = i + 1;
                j = 0;
                if (i >= 9)
                    return true;
            }
        }

        for (int num = 1; num <= 9; num++) {
            if (checkIfSafe(i, j, num, sudoku)) {
                sudoku[i][j] = num;
                if (fillOther(i, j + 1, sudoku))
                    return true;

                sudoku[i][j] = 0;
            }
        }
        return false;
    }

    /* chechIfSafe method:
                    will check be used in fillOther method to check the non Diagonal box are having
                    values which are repeating in Row , Column ,Box

     */
    public static boolean checkIfSafe(int i, int j, int num, int sudoku[][]) {

        return (inRow(i, num, sudoku) && inCol(j, num, sudoku) && inBox(i - i % 3, j - j % 3, num, sudoku));
    }

    /* fillbox method will :
                set values for a box of sudoku and index of first element
                will be used from fillDiagonal and fillother
                will be used from fillDiagonal and fillother
    */

    public static void fillBox(int row, int column, int sudoku[][]) {
        Random rand = new Random();
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = rand.nextInt(10);
                }
                while (!inBox(row, column, num, sudoku));
                sudoku[row + i][column + j] = num;
            }
        }
    }

    /*
    inBox method:
            will check that the number is in the same box or not.
            if it is in the row will return true else false
     */
    public static boolean inBox(int row, int column, int num, int sudoku[][]) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (sudoku[row + i][column + j] == num)
                    return false;
        return true;
    }

    /*
        inRow method:
                will check that the number is in the same Row or not.
                if it is in the row it will return true else false
    */
    public static boolean inRow(int i, int num, int sudoku[][]) {
        for (int j = 0; j < 9; j++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    /*
            inCol method:
                    will check that the number is in the same col or not.
                    if it is in the col it will return true else false
     */
    public static boolean inCol(int i, int num, int sudoku[][]) {
        for (int j = 0; j < 9; j++)
            if (sudoku[j][i] == num)
                return false;
        return true;
    }

    /*
    removeElement method:
                   use to remove k number of elment from the sudoku.
     */
    public static void removeElement(int numberofemptyspace, int sudoku[][], int gridsize) {
        Random rand = new Random();
        int remove = numberofemptyspace;

        while (remove != 0) {
            int removeblock = rand.nextInt((gridsize * gridsize) + 1);
            int j = removeblock / gridsize;
            int k = removeblock % gridsize;
            //if(k!=0)
            if (j == gridsize) {
                if (sudoku[gridsize - 1][gridsize - 1] != 0) {
                    remove--;
                    sudoku[gridsize - 1][gridsize - 1] = 0;
                }
            } else {
                if (sudoku[j][k] != 0) {
                    remove--;
                    sudoku[j][k] = 0;
                }
            }
        }
    }

    /*the method will generate mini wordoku*/
    private char[][] generateMiniWordoku(int empytbox, int gamemode) {
        char[][] miniWordoku = new char[6][6];
        int[][] miniSudoku = createPuzzle(createBoard(VALID_BOARD_6X6), gamemode);
        convertToAlphabets(miniSudoku, miniWordoku, 6);
        return miniWordoku;
    }

    // Create a copy of 2D array.
    private int[][] copyOf(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    // Swap rows of an 2D array.
    private int[][] swapRows(int[][] board, int row1, int row2) {
        for (int j = 0; j < board.length; j++) {
            int temp = board[row1][j];
            board[row1][j] = board[row2][j];
            board[row2][j] = temp;
        }
        return board;
    }

    // Swap columns of an 2D array.
    private int[][] swapCols(int[][] board, int col1, int col2) {
        for (int i = 0; i < board.length; i++) {
            int temp = board[i][col1];
            board[i][col1] = board[i][col2];
            board[i][col2] = temp;
        }
        return board;
    }

    // This method swaps rows and columns of an valid board.
    // Swaping process for rows must be done same horizontal grid and
    // also for column swaping process must be in vertical grid.
    private int[][] swapRowsAndCols(int[][] board) {

        int range = 5;
        // define number of rows per horizontal group.
        int rowsInGrid = 2;
        // For both 9X9 and and 9X6 number of columns in vertical grid is 3.
        int colsInGrid = 3;

        for (int a = 0; a < range; a += rowsInGrid) {
            int row[] = getTwoRanNum(a, rowsInGrid);
            swapRows(board, row[0], row[1]);
        }

        for (int a = 0; a < range; a += colsInGrid) {
            int[] col = getTwoRanNum(a, colsInGrid);
            swapCols(board, col[0], col[1]);
        }
        return board;
    }

    // Swap only horizontal groups.
    private int[][] swapGrids(int[][] board) {
        int firstgrid = 1 + random.nextInt(3);
        int secondgrid = 1 + random.nextInt(3);
        int numRowsInGrid = board.length == GRID_9X9 ? 3 : 2;

        if ((firstgrid == 1 && secondgrid == 2) || (firstgrid == 2 && secondgrid == 1)) {
            for (int i = 0; i < numRowsInGrid; i++) {
                swapRows(board, i, i + numRowsInGrid);
            }
        } else if ((firstgrid == 2 && secondgrid == 3) || (firstgrid == 3 && secondgrid == 2)) {
            for (int i = numRowsInGrid; i < numRowsInGrid * 2; i++) {
                swapRows(board, i, i + numRowsInGrid);
            }
        } else if ((firstgrid == 1 && secondgrid == 3) || (firstgrid == 3 && secondgrid == 1)) {
            for (int i = 0; i < numRowsInGrid; i++) {
                swapRows(board, i, i + (numRowsInGrid * 2));
            }
        }
        return board;
    }

    // swap numbers for each rows.
    private int[][] swapNums(int[][] board) {
        int[] num = getTwoRanNum(1, board.length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == num[0]) {
                    board[i][j] = num[1];
                } else if (board[i][j] == num[1]) {
                    board[i][j] = num[0];
                }
            }
        }
        return board;
    }

    // provide two random number as an array with length two.
    private int[] getTwoRanNum(int min, int tolerance) {
        int a[] = new int[2];
        a[0] = min + random.nextInt(tolerance);
        a[1] = min + random.nextInt(tolerance);
        return a;
    }

    // Create an validsudoku board.
    private int[][] createBoard(int[][] board) {
        for (int i = 0; i < 10; i++) {
            swapRowsAndCols(board);
            swapGrids(board);
            swapNums(board);
        }
        return board;
    }

    // Hide some numbers to create puzzle.
    private int[][] createPuzzle(int[][] board, int mode) {
        this.miniPuzzle = copyOf(board);
        int numOfEmptyBlock = getNumberOfEmptyBlock(36, mode);
        removeElement(numOfEmptyBlock, miniPuzzle, 6);
        return copyOf(this.miniPuzzle);
    }

    public boolean check(char[][] puzzle) {
        boolean isCorrect = true;
        int board[][] = convertBacktoNumbers(puzzle, puzzle.length);
        int numOfRowsInGrid = puzzle.length == 9 ? 3 : 2;
        final String setValues = board.length == 9 ? SET_VALUE_9X9 : SET_VALUE_6X6;
        // check rows
        for (int i = 0; i < board.length; i++) {
            String set = setValues;
            for (int j = 0; j < board.length; j++) {
                set = set.replace("" + board[i][j], "");
            }
            if (!set.isEmpty()) {
                isCorrect = false;
                return isCorrect;
            }
        }

        // check columns
        for (int j = 0; j < board.length; j++) {
            String set = setValues;
            for (int i = 0; i < board.length; i++) {
                set = set.replace("" + board[i][j], "");
            }
            if (!set.isEmpty()) {
                isCorrect = false;
                return isCorrect;
            }
        }

        //check Horizontal and vertical grids
        for (int hg = 0; hg < board.length; hg += numOfRowsInGrid) {
            for (int vg = 0; vg < board[0].length; vg += 3) {
                String set = setValues;
                for (int i = hg; i < (hg + numOfRowsInGrid); i++) {
                    for (int j = vg; j < vg + 3; j++) {
                        set = set.replace("" + board[i][j], "");
                    }
                }
                if (!set.isEmpty()) {
                    isCorrect = false;
                    return isCorrect;
                }
            }
        }

        return isCorrect;
    }


    //this is for reseting the puzzle
    public char[][] resetPuzzle() {
        return duplicatepuzzle;
    }


    public static int[][] convertBacktoNumbers(char[][] wordoku, int gridsize) {
        int board[][] = new int[gridsize][gridsize];
        for (int i = 0; i < gridsize; i++) {
            for (int j = 0; j < gridsize; j++) {
                int index = convertBack.indexOf(wordoku[i][j]);
                board[i][j] = index + 1;
            }
        }
        return board;
    }
}