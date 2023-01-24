/*
*	Square State ADT Implementation

*	Version: May 2019
*
*	This file holds the square_state ADT.  It comprises
*	the coordinate of square (as "row" and "column"),
*	and whether or not the square is occupied.
*
*
*/


#include <stdio.h>
#include <stdlib.h>
#include "square_state.h"
#include "assig_three119.h"


// types
struct square_state_int {
	int row;
	int column;
	bool visited;
};


/*
*	init_square_state
*	initialiser function.
*	Pre-condition: none
*	Post-condition: a square_state variable is created and pointed
*					to by the first parameter.  Its row field
*					is set to r, its column field is set to c,
*					and it is set to unoccupied.
*	Informally: creates an unoccupied square space
*
*	Param r int intended row number for square
*	Param c int intended column number for square
*/
void init_square_state(square_state *sp, int r, int c)
{
	trace("init_square_state: init_square_state starts");

	*sp = (square_state)malloc(sizeof(struct square_state_int));
	(*sp)->row = r;
	(*sp)->column = c;
	(*sp)->visited = false;

	trace("init_square_state: init_square_state ends");
}


/*
* 	get_row
*	Get function for row field.
*	Pre-condition: none
*	Post-condition: the value of the square_state variable's row
*					field is returned
*	Informally: return the square_state variable's row
*
*	return int the current row number of the square
*/
int get_row(square_state s)
{
	trace("get_row: get_row starts and finishes");

	return (s->row);
}


/*
* 	get_column
*	Get function for column field.
*	Pre-condition: none
*	Post-condition: the value of the square_state variable's column
*					field is returned
*	Informally: return the square_state variable's column
*
*	return int the current column number of the square
*/

int get_column(square_state s)
{

	trace("get_column: get_column starts and finishes");

	return (s->column);

}


/*
* 	set_row
*	Set function for row field.
*	Pre-condition: the given square_state is defined 
*	Post-condition: the value of the square_state variable's row
*					field is updated to the given int value
*	Informally: update the square_state variable's row
*
* Param r int value to be placed into the square_state's row field
*/
void set_row(square_state s, int r)
{
	trace("set_row: set_row starts");

	s->row = r;

	trace("set_row: set_row ends");



}


/*
* 	set_column
*	Set function for column field.
*	Pre-condition: the given square_state is defined 
*	Post-condition: the value of the square_state variable's column
*					field is updated to the given int value
*	Informally: update the square_state variable's column
*
*	Param c int value to be placed into the square_state's column field
*/
void set_column(square_state s, int c)
{
	trace("set_column: set_column starts");

	s->column = c;

	trace("set_column: set_column ends");
}


/*
* 	occupied
*	Check whether the square_state has already been occupied
*	Pre-condition: the given square_state is defined 
*	Post-condition: true is returned if the square has been visited
*					(i.e. is true, and false is returned otherwise)
*	Informally: Return whether or not a square has been taken
*/

bool occupied(square_state s)
{

	if (s->visited==true) {
		return true;
	}
	else {	
		return false;
	}

}


/*
* 	occupy
*	Set function for visited field.
*	Pre-condition: the given square_state is defined
*	Post-condition: the value of the square_state variable's visited
*					field is updated to true
*	Informally: vist this square_state variable
*/

void occupy(square_state s)
{

	trace("occupy: occupy starts");

	s->visited = true;

	trace("occupy: occupy ends");


}


/*
* 	show_square_state
*	Display a game state
*	Pre-condition: the given square_state is defined
*	Post-condition: the given square_state has been displayed on
*					the standard output stream
*	Informally: Print the square
*/
void show_square_state(square_state s)
{
	trace("show_square_state: show_square_state starts");

	if (occupied(s))	// queen is there
	{
		printf(" Q ");
	}
	else				// empty
	{
		printf("   ");
	}

	trace("show_square_state: show_square_state ends");
}
