/*
 *Project Name: Wordoku
 *Author: Patva Viraj (19IT117)
 * 		   Shah Axat (19IT126)
 *Project Version: 1.1.4
 */
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
public class Wordoku {
    static char[][] wordoku =new char[9][9];
    static char[][] reset =new char[9][9];
    static char[][] cheat =new char[9][9];
    static char[] toBeFilled=new char[9];
    final static int numberOfElementsToBeRemoved=54;
    static int[] fixedAddresses=new int[81-numberOfElementsToBeRemoved];
    static int stepCounter=0;
    static float finalScore=0;

    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_BRIGHT_RED    = "\u001B[91m";

    public static void main(String[] args)/*The MAIN main. The Project Starts from here.*/
    {
        clearScreen();
        int n = 0;
        printHomeScreen();
        Scanner scan=new Scanner(System.in);
        try
        {
            n = scan.nextInt();
        }
        catch(InputMismatchException Exe)
        {
            System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t    PLEASE ENTER INTEGER VALUE ONLY!\n");
            System.out.print("\t\t\t\t\t\t     PRESS ANY CHARACTER TO RESTART\n"+ANSI_RESET);
            scan.next();
            scan.next();
            main(args);
        }
        switch (n) {
            /*Game play starts here*/
            case 1 -> startGameplay();/*Generates Puzzle*/
            /*Displays Set of Rules*/
            case 2 -> printRules();
            /*Exits the Game*/
            case 0 -> System.exit(0);
            /*For Invalid inputs*/
            default -> {
                System.out.print(ANSI_BRIGHT_RED + "\n\t\t\t\t\t\t PLEASE ENTER VALID INTEGER VALUE ONLY!\n");
                System.out.print("\t\t\t\t\t\t     PRESS ANY CHARACTER TO RESTART\n" + ANSI_RESET);
                scan.next();
                main(args);
            }
        }
        scan.close();
    }

    public static void clearScreen()
    {
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) { System.out.println();}
    }

    public static void printHomeScreen()/*Prints the Home Screen.*/
    {
        clearScreen();
        System.out.print("\n\n\t\t\t\t\t\t\t <<<<<<<<<<<<<<<<<<<<<\n");
        System.out.print("\t\t\t\t\t\t\t  LET'S PLAY WORDOKU!\n");
        System.out.print("\t\t\t\t\t\t\t >>>>>>>>>>>>>>>>>>>>>\n");
        System.out.print("\n\n\n");
        System.out.print("\t\t\t\t\t\t\t     PRESS 1: PLAY\n");
        System.out.print("\t\t\t\t\t\t\t     PRESS 2: RULES\n");
        System.out.print("\t\t\t\t\t\t\t     PRESS 0: EXIT\n\n\n");
        System.out.print("\t\t\t\t\t\t\t\tENTER: ");
    }

    public static void printRules()/*Prints the rules.*/
    {
        int n=0;
        Scanner scan=new Scanner(System.in);
        clearScreen();
        System.out.println(ANSI_RED+"[PRESS 0: TO EXIT]\t\t\t\t\t\t\t\t\t\t\t\t    [PRESS 1: TO PLAY]");
        System.out.print(ANSI_RESET);
        System.out.print("\n\t\t\t\t\t\t\t<<<<<<<<<<<<<<<<<<<<<<<\n");
        System.out.print("\t\t\t\t\t\t\t RULES TO PLAY WORDOKU\n");
        System.out.print("\t\t\t\t\t\t\t>>>>>>>>>>>>>>>>>>>>>>>\n");
        System.out.print("\n\n\n");
        System.out.print("\t\t\t\t     +---------------------------------------------------------+\n");
        System.out.print("\t\t\t\t     |                                                         |\n");
        System.out.print("\t\t\t\t     | The Classic Wordoku game involves a grid of 81 squares. |\n");
        System.out.print("\t\t\t\t     |                                                         |\n");
        System.out.print("\t\t\t\t     | The Grid is Divided into Nine Blocks, each containing-- |\n");
        System.out.print("\t\t\t\t     |                      nine squares.                      |\n");
        System.out.print("\t\t\t\t     |                                                         |\n");
        System.out.print("\t\t\t\t     | Each of the Nine blocks has to contain all the alloted- |\n");
        System.out.print("\t\t\t\t     |              Alphabets within its Squares.              |\n");
        System.out.print("\t\t\t\t     |                                                         |\n");
        System.out.print("\t\t\t\t     | Each Alphabet can only Appear once in a Row, Column or- |\n");
        System.out.print("\t\t\t\t     |                            Box.                         |\n");
        System.out.print("\t\t\t\t     |                                                         |\n");
        System.out.print("\t\t\t\t     +---------------------------------------------------------+\n");
        System.out.print("\n\n");
        System.out.print("\t\t\t\t\t\t\t\tENTER: ");
        try
        {
            n = scan.nextInt();/*To continue flow of the program*/
        }
        catch(InputMismatchException Exe)
        {
            System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t    PLEASE ENTER INTEGER VALUE ONLY!\n");
            System.out.print("\t\t\t\t\t\t     PRESS ANY CHARACTER TO RESTART\n"+ANSI_RESET);
            scan.next();
            scan.next();
            printRules();
        }
        if(n==1)
            startGameplay();/*To play the Game*/
        else
            System.exit(0);
    }

    public static void startGameplay()/*Generates the Puzzle*/
    {
        int[][] sudoku =new int[9][9];

        char[] toBeFilled;
        createSudoku(sudoku);
        toBeFilled= convertToAlpahbets(sudoku,wordoku);
        removeElement(numberOfElementsToBeRemoved,wordoku);
        fixAddressIdentifier();
        inputSystem();
    }

    public static void createSudoku(int[][] sudoku)/*Will generate the sudoku by first filling diagonal and then remaining elements*/
    {
        fillDiagonal(sudoku);
        fillOther(0,3,sudoku);
    }

    public static void fillDiagonal(int[][] sudoku)/*Fills principle diagonal elements*/
    {
        for (int i = 0; i < 9; i = i + 3)
            fillBox(i, i,sudoku);
    }

    public static void fillBox(int row, int column, int[][] sudoku)/*set values for a box of sudoku and index of first element*/
    {
        Random rand=new Random();
        int num;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                do {
                    num= rand.nextInt(10);
                }
                while(!inBox(row,column,num,sudoku));
                sudoku[row+i][column+j]=num;
            }
        }
    }

    public static boolean inBox(int row, int column, int num, int[][] sudoku)/*will check that the number is in the same box or not. if it is in the row will return true else false*/
    {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(sudoku[row+i][column+j]==num)
                    return false;
        return true;
    }

    public static boolean fillOther(int i, int j, int[][] sudoku)/*Will fill all other boxes using fillbox method*/
    {
        if (j>=9 && i<9-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=9 && j>=9)
            return true;
        if (i < 3)
        {
            if (j < 3)
                j = 3;
        }
        else if (i < 6)
        {
            if (j== (i/3) *3)
                j = j + 3;
        }
        else
        {
            if (j == 6)
            {
                i = i + 1;
                j = 0;
                if (i>=9)
                    return true;
            }
        }

        for (int num = 1; num<=9; num++)
        {
            if (checkIfSafe(i, j, num,sudoku))
            {
                sudoku[i][j] = num;
                if (fillOther(i, j+1,sudoku))
                    return true;

                sudoku[i][j] = 0;
            }
        }
        return false;
    }

    public static boolean checkIfSafe(int i, int j, int num, int[][] sudoku)/*to check if elements taken at position are appropriate or not*/
    {

        return (inRow(i, num,sudoku) && inCol(j, num,sudoku) &&inBox(i-i%3, j-j%3, num,sudoku));
    }

    public static boolean inRow(int i, int num, int[][] sudoku)/*will check that the number is in the same Row or not. if it is in the row it will return true else false*/
    {
        for (int j = 0; j<9; j++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    public static boolean inCol(int i, int num, int[][] sudoku)/*same as row one but just for columns*/
    {
        for (int j = 0; j<9; j++)
            if (sudoku[j][i] == num)
                return false;
        return true;
    }

    public static char[] convertToAlpahbets(int[][] sudoku, char[][] wordoku)/*char array Sudoku will be converted to a set of random alphabhets*/
    {
        Random rand=new Random();
        char[] alpahabets;
        alpahabets=new char[10];

        alpahabets[0]=' ';
        char character;
        for(int i=1;i<10;i++)
        {
            do {
                character= (char) ('A' + rand.nextInt(26));
            }
            while(!inArray(alpahabets,i,character));
            alpahabets[i]=character;
        }

        System.arraycopy(alpahabets, 1, toBeFilled, 0, 9);

        int num;
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++)
            {
                num= sudoku[i][j];
                wordoku[i][j]=alpahabets[num];
                cheat[i][j]=wordoku[i][j];
            }
        }
        return toBeFilled;
    }

    public static void removeElement(int i, char[][] wordoku)/* use to remove k number of element from the sudoku.*/
    {
        Random rand=new Random();
        int remove=i;

        while(remove!=0) {
            int removeblock = rand.nextInt(82);
            int j = removeblock / 9;
            int k = removeblock % 9;
            //if(k!=0)
            //  k=k-1;
            if (j == 9) {
                if (wordoku[8][8] != ' ') {
                    remove--;
                    wordoku[8][8] = ' ';
                }
            } else {
                if (wordoku[j][k] != ' ') {
                    remove--;
                    wordoku[j][k] = ' ';
                }
            }
        }

        for(int j=0;j<9;j++)/*To reset when needed*/
        {
            System.arraycopy(wordoku[j], 0, reset[j], 0, 9);
        }
    }

    public static boolean inArray(char[] alpha, int i, char alphabet)/*this method will insure that none of the char in the wordoku is repeated.*/
    {
        for(int j=0;j<i;j++)
            if(alpha[j]==alphabet)
                return false;
        return true;
    }

    public static void fixAddressIdentifier()/*Identifies which elements cannot be changed*/
    {
        int k=0;
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++)
            {
                if(wordoku[i][j]!=' ')
                {
                    fixedAddresses[k]=(((i+1)*10) + (j+1));
                    k++;
                }
            }
        }
    }

    public static void displayPuzzle(char[][] wordoku, char[] toBeFilled)/*prints the puzzle*/
    {
        clearScreen();
        int columns = 10;//to form a pattern and display the puzzle
        int lines = 9;//to form a pattern and display the puzzle
        System.out.println(ANSI_RED+"[PRESS 0: TO EXIT]\t\t\t\t\t   [PRESS 1: TO SUBMIT]\t\t\t\t\t   [PRESS 2: TO RESET]");
        System.out.print(ANSI_RESET);
        System.out.print("\n");
        System.out.print(ANSI_BLUE+"\t\t\t\t    >> Use only these Alphabets to solve: "+Arrays.toString(toBeFilled)+" <<"+"\n");
        System.out.println("\t\t\t\t\t\t\t  >> Steps taken: "+stepCounter+" <<"+ANSI_RESET);
        System.out.print("\n\t\t\t\t\t\t ");
        System.out.print("  1   2   3   4   5   6   7   8   9\n");
        System.out.print("\t\t\t\t\t\t ");
        System.out.print("+-----------+-----------+-----------+\n");
        int j = 0;
        do {

            System.out.print("\t\t\t\t\t       ");//to align centrally
            System.out.print((j+1)+" |");
            for (int i = 0; i < columns - 1; i++)/*print all the elements of a row*/
            {
                boolean flagColor=true;
                if(i<9)
                {
                    for(int m=0;m<Array.getLength(fixedAddresses);m++)
                    {
                        int n= ((j+1)*10)+(i+1);
                        if(n==fixedAddresses[m])
                        {
                            flagColor=false;
                        }

                    }

                    if(flagColor) {
                        System.out.print(ANSI_GREEN);
                    }

                    System.out.print(" "+wordoku[j][i]);
                    System.out.print(ANSI_RESET+" |");//print the elements
                }


            }

            System.out.print("\n");

            if (j < lines - 1) /*print the pattern for the grid*/
            {
                System.out.print("\t\t\t\t\t\t ");//to align centrally
                if((j+1)%3==0)						//For first
                    System.out.print("+---");		//element
                else								//Of each
                    System.out.print("|---");		//Column

                for (int i = 1; i < columns-2; i++)/*For central elements*/
                {
                    if((j+1)%3!=0)
                    {
                        if(i%3==0)
                            System.out.print("|---");
                        else
                            System.out.print("+---");
                    }
                    else
                    {
                        if(i%3==0)
                            System.out.print("+---");
                        else
                            System.out.print("----");
                    }
                }

                if((j+1)%3!=0)						//For first
                    System.out.print("+---|");		//element
                else								//Of each
                    System.out.print("----+");		//Column
            }

            if(j<lines-1)//For moving to next line
                System.out.print("\n");

            ++j;//updating for the loop
        }
        while (j < lines);

        System.out.print("\t\t\t\t\t\t ");
        System.out.print("+-----------+-----------+-----------+\n\n\n");
    }

    public static void displayEndScreen(char[][] wordoku, char[] toBeFilled)/*prints the puzzle*/
    {
        clearScreen();
        int columns = 10;//to form a pattern and display the puzzle
        int lines = 9;//to form a pattern and display the puzzle
        System.out.println(ANSI_RED+"[PRESS 0: TO EXIT]\t\t\t\t\t\t\t\t\t\t\t     [PRESS 1: FOR NEW PUZZLE]");
        System.out.print(ANSI_RESET+"\n");
        System.out.print(ANSI_BLUE+"\t\t\t\t   Use only these Alphabets to solve: "+Arrays.toString(toBeFilled)+"\n\n\n");
        System.out.print(ANSI_RESET);
        System.out.print("\t\t\t\t\t\t ");
        System.out.print("  1   2   3   4   5   6   7   8   9\n");
        System.out.print("\t\t\t\t\t\t ");
        System.out.print("+-----------+-----------+-----------+\n");
        int j = 0;
        do {
            System.out.print("\t\t\t\t\t       ");//to align centrally
            System.out.print((j+1)+" |");
            for (int i = 0; i < columns - 1; i++)/*print all the elements of a row*/
            {
                boolean flagColor=true;
                if(i<9) {
                    for (int m = 0; m < Array.getLength(fixedAddresses); m++) {
                        int n = ((j + 1) * 10) + (i + 1);
                        if (n == fixedAddresses[m]) {
                            flagColor = false;
                        }

                    }

                    if (flagColor) {
                        System.out.print(ANSI_GREEN);
                    }

                    System.out.print(" " + wordoku[j][i]);
                    System.out.print(ANSI_RESET + " |");//print the elements
                }
            }

            System.out.print("\n");

            if (j < lines - 1) /*print the pattern for the grid*/
            {
                System.out.print("\t\t\t\t\t\t ");//to align centrally
                if((j+1)%3==0)						//For first
                    System.out.print("+---");		//element
                else								//Of each
                    System.out.print("|---");		//Column

                for (int i = 1; i < columns-2; i++)/*For central elements*/
                {
                    if((j+1)%3!=0)
                    {
                        if(i%3==0)
                            System.out.print("|---");
                        else
                            System.out.print("+---");
                    }
                    else
                    {
                        if(i%3==0)
                            System.out.print("+---");
                        else
                            System.out.print("----");
                    }
                }

                if((j+1)%3!=0)						//For first
                    System.out.print("+---|");		//element
                else								//Of each
                    System.out.print("----+");		//Column
            }

            if(j<lines-1)//For moving to next line
                System.out.print("\n");

            ++j;//updating for the loop
        }
        while (j < lines);

        System.out.print("\t\t\t\t\t\t ");
        System.out.print("+-----------+-----------+-----------+\n\n\n");
    }

    public static void inputSystem()
    {
        displayPuzzle(wordoku,toBeFilled);
        int n=0,row,col;
        boolean flagN=true,flagC=false;
        char c=' ';
        Scanner scan=new Scanner(System.in);
        System.out.print("\t\t\t\t\t    Enter the Element ID which you want to Edit: ");
        try
        {
            n = scan.nextInt();
        }
        catch(InputMismatchException exe)
        {
            System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t   PLEASE ENTER INTEGER VALUE ONLY!\n");
            System.out.print("\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE...\n"+ANSI_RESET);
            scan.next();
            scan.next();
            inputSystem();
        }
        if(n==0)
        {
            System.exit(0);
        }
        else if(n==1)/*for checking*/
        {
            stepCounter++;
            checkWordoku();
        }
        else if(n==2)/*to  reset*/
        {
            char confirmation;
            System.out.println("\t\t\t\t\t\t    ARE YOU SURE YOU WANT TO RESET?");
            System.out.println("\t\t\t\t\t\t    [Y]: YES                [N]: NO");
            System.out.print("\t\t\t\t\t\t\t\tENTER: ");
            confirmation=scan.next().charAt(0);
            confirmation= Character.toUpperCase(confirmation);
            if(confirmation=='Y')
            {
                stepCounter++;
                resetWordoku();
            }
            else
                inputSystem();
        }
        else if(n==1115)/*To debug and test*/
        {
            activateCheatCode();
        }
        else if(n>=11 & n<=99 & n%10!=0)/*To check validity of n does it belong to the range*/
        {
            for(int i=0;i<Array.getLength(fixedAddresses);i++)
            {
                if(fixedAddresses[i]==n)/*To check if position entered is not a predefined one*/
                {
                    flagN=false;
                }
            }

            if(flagN)
            {
                row= n/10;/*row number for element*/
                col=n%10;/*column number for element*/
                System.out.print("\t\t\t\t\t\t       Enter the Character: ");/*Input of element*/
                try
                {
                    c = scan.next().charAt(0);
                    c = Character.toUpperCase(c);
                }
                catch(InputMismatchException exe)
                {
                    System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t   PLEASE ENTER ONLY CHARACTER!\n");
                    System.out.print("\t\t\t\t\t\t PRESS ANY CHARACTER TO CONTINUE\n"+ANSI_RESET);
                    scan.next();
                    inputSystem();
                }
                for(int i=0;i<9;i++)
                {
                    if(c==toBeFilled[i])/*To check validity of character c does it belong to alloted character list*/
                    {
                        flagC=true;
                    }
                }

                if(flagC)
                {
                    wordoku[row-1][col-1]=c;
                    stepCounter++;
                }
                else
                {
                    System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t  PLEASE ENTER A VALID CHARACTER!\n");
                    System.out.print("\t\t\t\t\t\t PRESS ANY CHARACTER TO CONTINUE...\n"+ANSI_RESET);
                    scan.next();
                }
            }
            else
            {
                System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t      YOU CANNOT CHANGE ELEMENT AT THAT POSITION!!\n");
                System.out.print("\t\t\t\t\t\t   PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
                scan.next();
            }
            inputSystem();
        }
        else
        {
            System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t PLEASE ENTER VALID INTEGER VALUE ONLY!\n");
            System.out.print("\t\t\t\t\t\t   PRESS ANY CHARACTER TO CONTINUE...\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }
        scan.close();
    }

    public static void activateCheatCode()/*For developer's so that they could finish puzzle for debugging purpose*/
    {
        for(int j=0;j<9;j++)
        {
            System.arraycopy(cheat[j], 0, wordoku[j], 0, 9);
        }
        inputSystem();
    }

    public static void resetWordoku()/*This function will reset Wordoku*/
    {
        for(int j=0;j<9;j++)
        {
            System.arraycopy(reset[j], 0, wordoku[j], 0, 9);
        }
        inputSystem();
    }

    public static void checkWordoku()/*To check wordoku*/
    {
        int n=0;
        Scanner scan=new Scanner(System.in);
        int tempRowCounter=1,tempColumnCounter=1;
        char[] row;
        char[] col;
        char[] box;
        char[] ary;
        row = new char[9];
        col = new char[9];
        box = new char[9];
        ary = new char[9];

        ary = Arrays.copyOf(toBeFilled, 9);
        Arrays.sort(ary);/*Sorted alphabets to compare later*/

        for (int i = 0; i < 9; i++)/*To check rows*/
        {
            /*Create a single dimensional array of a row*/
            System.arraycopy(wordoku[i], 0, row, 0, 9);

            Arrays.sort(row);/*Sort the newly created single dimensional array*/
            if (Arrays.compare(ary, row) != 0) /*Compare both the sorted arrays to check validity*/
            {
                displayPuzzle(wordoku,toBeFilled);
                System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN ROW-" + tempRowCounter);
                System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
                scan.next();
                inputSystem();
            }
            tempRowCounter++;
        }

        for (int i = 0; i < 9; i++) /*To check columns*/
        {
            /*Create a single dimensional array of a column*/
            System.arraycopy(wordoku[i], 0, col, 0, 9);
            Arrays.sort(col);/*Sort the newly created single dimensional array*/
            if (Arrays.compare(ary, col) != 0) /*Compare both the sorted arrays to check validity*/
            {
                displayPuzzle(wordoku,toBeFilled);
                System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t OOPS! LOOKS LIKE YOU MADE A MISTAKE IN COLUMN-" + tempColumnCounter);
                System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
                scan.next();
                inputSystem();
            }
            tempColumnCounter++;
        }


        int count = 0;/*Counter for  boxes*/

        for (int i = 0; i < 3; i++) /*For Box-1*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[i][j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-1");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-2*/
        {
            for (int j = 3; j < 6; j++)
            {
                box[count] = wordoku[i][j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-2");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-3*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[i][6 + j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-3");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }


        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-4*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[3 + i][j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-4");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-5*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[3 + i][3 + j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-5");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-6*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[3 + i][6 + j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-6");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-7*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[6 + i][j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-7");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-8*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[6 + i][3 + j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-8");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        count = 0;
        for (int i = 0; i < 3; i++) /*For Box-9*/
        {
            for (int j = 0; j < 3; j++)
            {
                box[count] = wordoku[6 + i][6 + j];
                count++;
            }
        }
        Arrays.sort(box);
        if (Arrays.compare(ary, box) != 0)
        {
            displayPuzzle(wordoku,toBeFilled);
            System.out.print(ANSI_BRIGHT_RED+"\t\t\t\t\t    OOPS! LOOKS LIKE YOU MADE A MISTAKE IN BOX-9");
            System.out.print("\n\t\t\t\t\t\t  PRESS ANY CHARACTER TO CONTINUE..\n"+ANSI_RESET);
            scan.next();
            inputSystem();
        }

        displayEndScreen(wordoku,toBeFilled);
        System.out.print(ANSI_BLUE+"\t\t\t\t  CONGRATULATIONS! YOU JUST SOLVED A WORDOKU PUZZLE.. NICE WORK!!!");
        calculateFinalScore();
        System.out.println("\n\t\t\t\t\t\t    YOUR SCORE: "+finalScore+" OUT OF 10"+ANSI_RESET);
        System.out.print("\n\t\t\t\t\t\t\t\tENTER: ");
        try
        {
            n = scan.nextInt();/*To continue flow of the program*/
        }
        catch(InputMismatchException Exe)
        {
            System.out.print(ANSI_BRIGHT_RED+"\n\t\t\t\t\t\t    PLEASE ENTER INTEGER VALUE ONLY!\n");
            System.out.print("\t\t\t\t\t\t     PRESS ANY CHARACTER TO RESTART\n"+ANSI_RESET);
            scan.next();
            scan.next();
            printRules();
        }
        if(n==1)
        {
            stepCounter=0;
            finalScore=0;
            startGameplay();
        }
        else
            System.exit(0);
    }

    public static void calculateFinalScore()/*To calculate final score from the number of steps taken*/
    {
        if(stepCounter<=numberOfElementsToBeRemoved)
        {
            finalScore = (float) 10.00;
        }
        else
        {
            stepCounter=(500-stepCounter)*2;
            finalScore= (float)stepCounter/100;
        }
    }

}