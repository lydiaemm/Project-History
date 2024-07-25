/*COP 3503C Assignment 2
This program is written by: Lydia Emmons*/

/* Reads in a word search board and words to be searched for. Outputs whether
 * the word is found and where on the board it exists. */

import java.util.*;

public class Main {
	public static int m, n, s;
	
	/*Function to print solution array*/
	static void printSolution(char sol[][]) {
		for(int row = 0; row < m; row++) {
			System.out.print("[");
			for(int col = 0; col < n; col++) {
				System.out.print(sol[row][col]);
				if(col != n-1)
					System.out.print(", ");
			}
			System.out.print("]\n");
		}
	}
	
	/*Function to initialize solution matrix to all spaces
	with the same width and height as board*/
	static char[][] initSolution() {
		char[][] sol = new char[m][n];
		for(int row = 0; row < m; row++) {
			for (int col = 0; col < n; col++) {
				sol[row][col] = ' ';
			}
		}
		return sol;
	}
	
	/*Function to find index of the first letter of the word
	being searched for*/
	static boolean firstLetterExists(char[][] board, char[][] sol, String word) {
	    int index = 0;

	    for(int i = 0; i < m; i++) {
	      for(int j = 0; j < n; j++) {
	        if(board[i][j] == word.charAt(index)) {
	          if (solveWordSearchUtil(board, sol, word, i, j, index)) {
	            return true;
	          }
	        }
	      }
	    }

	    return false;
	}
	
	/*This function solves the Word Search problem using Backtracking.
	Mainly uses solveWordSearchUtil() to solve the problem. it returns
	false if no path is possible, otherwise returns true and prints
	the path of each letter in the word in the correct spot from the
	original word search board. This function does not necessarily
	print the only solution, it also does not print all possible
	solutions. However, it does print one of the feasible solutions.*/
	static boolean solveWordSearch(char board[][], String input) {
		char sol[][] = initSolution();
		int row = 0, col = 0;
		
		if(firstLetterExists(board, sol, input) == false) {
			System.out.println(input + " not found!");
			return false;
		}
		
		printSolution(sol);
		return true;
	}
	
	/*Recursive utility function to solve word search problem*/
	static boolean solveWordSearchUtil(char[][] board, char[][] sol, String word, int r, int c, int index) {
		
		if(index == word.length())
		      return true;

		    if(r >= 0 && r < m && c >= 0 && c < n && board[r][c] == word.charAt(index)) {
		      if(sol[r][c] != ' ')
		        return false;

		      sol[r][c] = word.charAt(index);
		      index += 1;
		      
		      if (solveWordSearchUtil(board, sol, word, r, c + 1, index)) { // try moving right
		        return true;
		      }
		      if(solveWordSearchUtil(board, sol, word, r, c - 1, index)) { // try moving left
		        return true;
		      }
		      if(solveWordSearchUtil(board, sol, word, r + 1, c, index)) { // try moving down
		        return true;
		      }
		      if(solveWordSearchUtil(board, sol, word, r - 1, c, index)) { // try moving up
		        return true;
		      }
		      if(solveWordSearchUtil(board, sol, word, r + 1, c + 1, index)) { //try moving diagonally (down and right)
			        return true;
			  }
		      if(solveWordSearchUtil(board, sol, word, r - 1, c + 1, index)) { //try moving diagonally (up and right)
		        return true;
		      }
		      if(solveWordSearchUtil(board, sol, word, r - 1, c - 1, index)) { //try moving diagonally (up and left)
		        return true;
		      }
		      if(solveWordSearchUtil(board, sol, word, r + 1, c - 1, index)) { //try moving diagonally (down and left)
			        return true;	
			  }

		      // if none of these directions work, reset the solution index and return that this index is not the correct route
		      sol[r][c] = ' ';
		      
		      return false;
		    }

		    return false;
	}

	/*Driver function which creates the word search board from input
	and calls the functions to solve for each input word to find.*/
	public static void main(String[] args) {
		char board[][];
		String temp;
		
		Scanner inp = new Scanner(System.in);
		
		m = inp.nextInt(); // number of rows in the matrix
		n = inp.nextInt(); // number of columns in the matrix
		s = inp.nextInt(); // number of words you need to find from this matrix
		
		board = new char[m][n];
		
		for(int row = 0; row < m; row++) {
			for(int col = 0; col < n; col++) {
				temp = inp.next();
				board[row][col] = temp.charAt(0);
			}
		}
		
		while(s != 0) {
		      temp = inp.next();
		      System.out.println("Looking for " + temp);
		      solveWordSearch(board, temp);
		      s--;
		      System.out.println();
		}
		
		inp.close();
	}
}
