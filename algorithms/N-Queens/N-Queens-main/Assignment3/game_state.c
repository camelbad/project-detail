/*
*	Game State ADT Implementation
*	
*	Version: May 2019
*
*	This file holds the game_state ADT.  It comprises
*	the board (as a two-dimensional array called "board").
*
*
*/


#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include "game_state.h"
#include "assig_three119.h"


// types
struct game_state_int {
	square_state board[DIMENSION][DIMENSION];
};


/*
	*	init_game_state
	*	initialiser function.
	*	Pre-condition: none
	*	Post-condition: a game_state variable is created and pointed
	*					to by the first parameter.  Each element in 
	*					the board table is created empty
	*	Informally: creates an initial game_state
*/


void init_game_state(game_state *gp)
{
	
	(*gp) = malloc(sizeof(struct game_state_int));//Allocate memory space
	
	for (int counter_row = 1; counter_row <= DIMENSION; counter_row++) {//travel all rows
		for (int counter_column = 1; counter_column <= DIMENSION; counter_column++) {//traver all column 
			init_square_state(&((*gp)->board[counter_row -1][counter_column -1]), counter_row, counter_column);//Initialize each node
		}	
	}

}


/*
	* 	get_square
	*	Get function for a square_state.
	*	Pre-condition: the given coordinate is within the bounds of the board
	*	Post-condition: the value of the indicated square_state variable
	*					is returned
	*	Informally: return the square_state in the game_state variable's board
	*
	*	Param int r the row component of the desired square's coordinate
	*	Param int c the column component of the desired square's coordinate
	*
	*	return square_state the nominated square_state
*/
square_state get_square(game_state g, int r, int c)
{
	return (g->board[r - 1][c - 1]);
}


/*
	* 	set_square
	*	Set function for a square_state.
	*	Pre-condition: the given square_state value is defined and is within the
	*				   dimension of the board array
	*	Post-condition: the square of the board at the given coordinate is updated
	*					to the given square_state value
	*	Informally: update the game_state board variable's element to the given value
	*
	*	Param set_square s value to be placed into the game_state's board field
*/
void set_square(game_state g, square_state s)
{
	int r, c;	// row and column of given square_state

	trace("set_square: set_square starts");

	r = get_row(s);
	c = get_column(s);

	g->board[r - 1][c - 1] = s;

	trace("set_square: set_square ends");
}


/*
	* 	valid
	*	Check whether a square is on the board
	*	Pre-condition: the given game_state is defined 
	*	Post-condition: true is returned if (r,c) is within the bounds
	*					of the given game_state variable's board field,
	*					and false is returned otherwise
	*	Informally: Return whether or not a square is on the board
	*
	*	Param r int row value to check
	*	Param c int column value to check
	*
	*	return bool whether the coordinate is on the board
	*/

bool valid(game_state g, int r, int c)
{

	if ( (c >= 1 && c <= DIMENSION) && (r >= 1 && r <= DIMENSION)) {//Determine whether a given coordinate is within the legal range
		return true;
	}
	else {
		return false;
	}



}


/*
* 	row_clear
*	Check whether the indicated row is clear on the given board
*	Pre-condition: the given game_state is defined and the given row
*				   number is within the board of the given game_state
*	Post-condition: true is returned if row r contains no queens, and
*					false is returned otherwise
*	Informally: Return whether or not a row is clear of queens
*
*	Param r int row value to check
*
*	return bool whether the row is clear
*/

bool row_clear(game_state g, int r)
{

	for (int column = 1; column <= DIMENSION; column++) {//Check each column in this line
		if (taken(g, r, column)) {//Return to false if the queen is found
			return false;
		}	
	}

	return true;//Return true if there is no queen in this line


}


/*
* 	column_clear
*	Check whether the indicated column is clear on the given board
*	Pre-condition: the given game_state is defined and the given
*				   column number is within the board of the given
*				   game_state
*	Post-condition: true is returned if column c contains no queens,
*					and false is returned otherwise
*	Informally: Return whether or not a column is clear of queens
*
*	Param c int column value to check
*
*	return bool whether the column is clear
*/
bool column_clear(game_state g, int c)
{

	for (int row = 1; row <= DIMENSION; row++) {//Check each row of this column

		if (taken(g, row, c)) {//Return to false if the queen is found
			return false;
		}

	}

	return true;//Return true if there is no queen in this column


}


/*
* 	diagonals_clear
*	Check whether the diagonals from the given coordinate are clear 
*		on the given board
*	Pre-condition: the given game_state is defined and the given row
*				   and column numbers are within the board of the
*				   given game_state
*	Post-condition: true is returned if the diagonals centered at
*					the given coordinate contain no queens, and
*					false is returned otherwise
*	Informally: Return whether or not the diagonals of a given
*				coordinate are clear of queens
*
*	Param r int row value for coordinate
*	Param c int column value for coordinate
*
*	return bool whether the diagonals are clear
*/
bool diagonals_clear(game_state g, int r, int c)
{
	int x, y;	// row and column of given game_state

	trace("diagonals_clear: diagonals_clear starts");

	x = r;
	y = c;
	while ((x > 1) && (y > 1))
	{
		x--;
		y--;
	}

	trace("diagonals_clear: found top left");

	while ((x <= DIMENSION) && (y <= DIMENSION))
	{
		if (taken(g, x, y))
		{
			trace("diagonals_clear: diagonals_clear finishes with false");

			return false;
		}
		x++;
		y++;
	}

	trace("diagonals_clear: diagonals_clear left diagonal clear");

	x = r;
	y = c;
	while ((x > 1) && (y < DIMENSION))
	{
		x--;
		y++;
	}

	trace("diagonals_clear: found top right");

	while ((x <= DIMENSION) && (y <= DIMENSION))
	{
		if (taken(g, x, y))
		{
			trace("diagonals_clear: diagonals_clear finishes with false");

			return false;
		}
		x++;
		y--;
	}

	trace("diagonals_clear: diagonals_clear finishes with true");

	return true;
}


/*
* 	clash
*	Check whether the row, column, and/or diagonals from the given
*		coordinate on the given board are not clear of queens
*	Pre-condition: the given game_state is defined and the given row
*				   and column numbers are within the board of the
*				   given game_state
*	Post-condition: false is returned if the row, column, and/or 
*					diagonals centered at the given coordinate contain
*					no queens, and true is returned otherwise
*	Informally: Return whether or not the row, column, anddiagonals
*				of a given coordinate are not clear of queens
*
*	Param r int row value for coordinate
*	Param c int column value for coordinate
*
*	return bool whether there is already a queen on the row, column,
*				and/or diagonals
*/
bool clash(game_state g, int r, int c)
{
	trace("clash: clash starts and finishes");

	return !(row_clear(g, r) && column_clear(g, c) && diagonals_clear(g, r, c));
}


/*
	* 	taken
	*	Check whether a square on the board has already been visited
	*	Pre-condition: the given game_state is defined and (r,c) is
	*					within the bounds of the given game_state
	*					variable's board field
	*	Post-condition: true is returned if (r,c) has been visited (i.e.
	*					is non-zero, and false is returned otherwise
	*	Informally: Return whether or not a square has been taken
	*
	*	Param r int row value to check
	*	Param c int column value to check
	*
	*	return bool whether the nominated square contains a queen
	*/

bool taken(game_state g, int r, int c)
{
	if (occupied(g->board[r - 1][c - 1])) {//if here has a queen then return true
		return true;
	}
	else
	{
		return false;
	}

}


/*
* 	land
*	Visit a square on the board
*	Pre-condition: the given game_state is defined and (r,c) is
*					within the bounds of the given game_state
*					variable's board field
*	Post-condition: the square at (r,c) has been visited 
*	Informally: Visit a square
*
*	Param r int row value to use
*	Param c int column value to use
*/
void land(game_state g, int r, int c)
{

	occupy(g->board[r-1][c-1]);//put a queen on this position


}


/*
* 	clone
*	Deeply clone a game_state
*	Pre-condition: the given game_state is defined
*	Post-condition: a deep clone of the given game_sate is returned
*	Informally: Copy a game
*
*	return game_state the independent copy of the game_state
*/

game_state clone(game_state g)
{
	game_state temp; 
	init_game_state(&temp);//create a new game state


	for (int i = 1; i <= DIMENSION; i++) {//read all the array
		for (int j = 1; j <= DIMENSION; j++) {
			if (taken(g,i,j))//If there is a queen in the current position in the source array
			{				
				land(temp,i,j);//Add Queen to the Clone Array
			}
		}		
	}

	return temp;
}

/*
	* 	show_game_state
	*	Display a game state
	*	Pre-condition: the given game_state is defined
	*	Post-condition: the given game_state has been displayed on
	*					the standard output stream
	*	Informally: Print the board
*/
void show_game_state(game_state g)
{
	int x, y;

	trace("show_game_state: show_game_state starts");

	// for all rows
	for (x = 1; x <= DIMENSION; x++)
	{
		// print row separator
		printf("+");
		for (y = 1; y <= DIMENSION * 4 - 1; y++)
		{
			printf("-");
		}
		printf("+\n");

		// print all columns
		printf("|");
		for (y = 1; y <= DIMENSION; y++)
		{
			show_square_state(g->board[x - 1][y - 1]);
			printf("|");
		}
		printf("\n");
	}
	
	// print row separator
	printf("+");
	for (y = 1; y <= DIMENSION * 4 - 1; y++)
	{
		printf("-");
	}
	printf("+\n");

	trace("show_game_state: show_game_state ends");
}