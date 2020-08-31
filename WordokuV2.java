/*
 *Project Name: Wordoku
 *Author: Patva Viraj (19IT117)
 * 		   Shah Axat (19IT126)
 *Project Version: 2.0.0.0
 */
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
public class WordokuV2 {
     static char wordoku[][]=new char[9][9];
     static char[] toBeFilled=new char[9];
     final static int numberOfElementsToBeRemoved=45;
     static int[] fixedAddresses=new int[81-numberOfElementsToBeRemoved];
    public static void main(String[] args)/*The MAIN main. The Project Starts from here.*/
    {
        clearScreen();
        int n=0;
        printHomeScreen();
        Scanner scan=new Scanner(System.in);
        n=scan.nextInt();
        switch(n)
        {
            case 1:/*Game play starts here*/
                startGameplay();/*Generates Puzzle*/
                break;
            case 2:/*Displays Set of Rules*/
                printRules();
                n=scan.nextInt();/*To continue flow of the program*/
                if(n==1)
                    main(args);/*Return to HomeScreen*/
                else
                    System.exit(0);
                break;
            case 0:/*Exits the Game*/
                System.exit(0);
            default:/*For Invalid inputs*/
            	System.out.print("\n\t\t\t\t\t\t\t    INVALID INPUT!\n");
        		System.out.print("\t\t\t\t\t\t\t  PRESS R TO RESTART\n");
                scan.next();
                main(args);
                break;
        }
        scan.close();
    }
    
    public static void clearScreen(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }
    
    public static void printHomeScreen()/*Prints the Home Screen.*/
    {
    	clearScreen();
		System.out.print("\n\n\t\t\t\t\t\t\t <<<<<<<<<<<<<<<<<<<<<\n");
		System.out.print("\t\t\t\t\t\t\t  LET'S PLAY WORDOKU!\n");
		System.out.print("\t\t\t\t\t\t\t >>>>>>>>>>>>>>>>>>>>>\n");
		System.out.print("\n\n\n");
		System.out.print("\t\t\t\t\t\t\t    Press 1: Play\n");
		System.out.print("\t\t\t\t\t\t\t    Press 2: Rules\n");
		System.out.print("\t\t\t\t\t\t\t    Press 0: Exit\n\n\n");
		System.out.print("\t\t\t\t\t\t\t\tEnter: ");
    }

    public static void printRules()/*Prints the rules.*/
    {
    	clearScreen();
		System.out.print("\n\n\t\t\t\t\t\t\t<<<<<<<<<<<<<<<<<<<<<<<\n");
		System.out.print("\t\t\t\t\t\t\t RULES TO PLAY WORDOKU\n");
		System.out.print("\t\t\t\t\t\t\t>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.print("\n\n\n");
		System.out.print("\t\t\t\t     +---------------------------------------------------------+\n");
		System.out.print("\t\t\t\t     |                                                         |\n");
		System.out.print("\t\t\t\t     | The Classic Wordoku game involves a grid of 81 squares. |\n");
		System.out.print("\t\t\t\t     |                                                         |\n");
		System.out.print("\t\t\t\t     | The Grid is Divided into Nine Blocks, each containing-- |\n");
		//System.out.print("\t\t\t\t     | nine squares.                                           |\n");
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
		System.out.print("\t\t\t\t\t\t\t    Press 1: Return\n");
		System.out.print("\t\t\t\t\t\t\t    Press 0: Exit\n\n\n");
		System.out.print("\t\t\t\t\t\t\t\tEnter: ");
    }

    public static void startGameplay()/*Generates the Puzzle*/
    {
        int sudoku[][]=new int[9][9];

        char[] toBeFilled;
        createSudoku(sudoku);
        removeElement(numberOfElementsToBeRemoved,sudoku);
        toBeFilled= convertToAlpahbets(sudoku,wordoku);
        fixAddressIdentifier();
        inputSystem();
    }

    public static void createSudoku(int sudoku[][])/*Will generate the sudoku by first filling diagonal and then remaining elements*/
    {
        fillDiagonal(sudoku);
        fillOther(0,3,sudoku);
    }

    public static void fillDiagonal(int sudoku[][])/*Fills principle diagonal elements*/
    {
        for (int i = 0; i < 9; i = i + 3)
            fillBox(i, i,sudoku);
    }

    public static void fillBox(int row,int column,int sudoku[][])/*set values for a box of sudoku and index of first element*/
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

    public static boolean inBox(int row,int column,int num,int sudoku[][])/*will check that the number is in the same box or not. if it is in the row will return true else false*/
    {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(sudoku[row+i][column+j]==num)
                    return false;
        return true;
    }

    public static boolean fillOther(int i,int j,int sudoku[][])/*Will fill all other boxes using fillbox method*/
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
            if (j==(int)(i/3)*3)
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

    public static boolean checkIfSafe(int i,int j,int num,int sudoku[][])/*to check if elements taken at position are appropriate or not*/
    {

        return (inRow(i, num,sudoku) && inCol(j, num,sudoku) &&inBox(i-i%3, j-j%3, num,sudoku));
    }

    public static boolean inRow(int i,int num,int sudoku[][])/*will check that the number is in the same Row or not. if it is in the row it will return true else false*/
    {
        for (int j = 0; j<9; j++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    public static boolean inCol(int i,int num,int sudoku[][])/*same as row one but just for columns*/
    {
        for (int j = 0; j<9; j++)
            if (sudoku[j][i] == num)
                return false;
        return true;
    }

    public static void removeElement(int i,int sudoku[][])/* use to remove k number of element from the sudoku.*/
    {
        Random rand=new Random();
        int remove=i;

        while(remove!=0)
        {
            int removeblock=rand.nextInt(82);
            int j=removeblock/9;
            int k=removeblock%9;
            //if(k!=0)
            //  k=k-1;
            if(j==9) {
                if (sudoku[8][8] != 0) {
                    remove--;
                    sudoku[8][8] = 0;
                }
            }

            else {
                if (sudoku[j][k] != 0) {
                    remove--;
                    sudoku[j][k] = 0;
                }
            }
        }
    }

    public static char[] convertToAlpahbets(int sudoku[][],char wordoku[][])/*char array Sudoku will be converted to a set of random alphabhets*/
    {
        Random rand=new Random();
        char alpahabets[];
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

        for(int i=1;i<10;i++)
        {
            toBeFilled[i-1]=alpahabets[i];
        }

        int num;
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++)
            {
                num= sudoku[i][j];
                wordoku[i][j]=alpahabets[num];
            }
        }
        return toBeFilled;
    }

    public static boolean inArray(char alpha[],int i,char alphabet)/*this method will insure that none of the char in the wordoku is repeated.*/
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

    public static void displayPuzzle(char wordoku[][],char toBeFilled[])/*prints the puzzle*/
    {
        clearScreen();
        int columns = 10;//to form a pattern and display the puzzle
        int lines = 9;//to form a pattern and display the puzzle
        System.out.println("\n\n\n");
        System.out.printf("\t\t\t\t Use only these Alphabets to solve: "+Arrays.toString(toBeFilled)+"\n\n\n");
        
        System.out.printf("\t\t\t\t\t\t ");
        System.out.printf("  1   2   3   4   5   6   7   8   9\n");
        System.out.printf("\t\t\t\t\t\t ");
        System.out.printf("+-----------+-----------+-----------+\n");
        int j = 0;
        do {
        	System.out.printf("\t\t\t\t\t       ");//to align centrally
       		System.out.print((j+1)+" |");	
            for (int i = 0; i < columns - 1; i++)/*print all the elements of a row*/
            {
                if(i<9)
                {
                    System.out.print(" "+wordoku[j][i]+" |");//print the elements
                }
                

            }

            System.out.printf("\n");

            if (j < lines - 1) /*print the pattern for the grid*/
            {
            	System.out.printf("\t\t\t\t\t\t ");//to align centrally
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
            	System.out.printf("%n");
            
            ++j;//updating for the loop
        } 
        while (j < lines);
        
        System.out.printf("\t\t\t\t\t\t ");
        System.out.printf("+-----------+-----------+-----------+\n\n\n");
    }
    
    public static void inputSystem()
    {
    	displayPuzzle(wordoku,toBeFilled);
    	int n=0,row=0,col=0;
    	boolean flagN=true,flagC=false;
    	char c;
    	Scanner scan=new Scanner(System.in);
    	System.out.print("\t\t\t\t\t    Enter the Element ID which you want to Edit: ");
       	n=scan.nextInt();
       	if(n==0)
       	{
       		System.exit(0);
       	}
       	if(n>=11 & n<=99 & n%10!=0)/*To check validity of n does it belong to the range*/
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
       			System.out.print("\n\t\t\t\t\t\t       Enter the Character: ");/*Input of element*/
       	       	c=scan.next().charAt(0);
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
       	       		inputSystem();
       	       	}
       	       	else
       	       	{
       	       	System.out.print("\n\t\t\t\t\t\t\t INVALID CHARACTER!\n");
       	       	System.out.print("\t\t\t\t\t\t\tPRESS C TO CONTINUE..\n");
                scan.next();
                inputSystem();
       	       	}
       		}
       		else
       		{
       			System.out.print("\n\t\t\t\t\t     YOU CANNOT CHANGE ELEMENT AT THAT POSITION!!\n");
       			System.out.print("\t\t\t\t\t\t\t  PRESS C TO CONTINUE..\n");
                scan.next();
                inputSystem();
       		}
       	}
       	else
       	{
    		System.out.print("\n\t\t\t\t\t\t\t     INVALID INPUT!\n");
    		System.out.print("\t\t\t\t\t\t\t  PRESS C TO CONTINUE..\n");
            scan.next();
            inputSystem();
       	}
       	scan.close();
    }
    
}
