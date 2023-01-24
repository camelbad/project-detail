/*
 *	Game Tree ADT Implementation
 *	
 *	Version: May 2019
 *	
 *	This file holds the game_tree ADT which is a
 *	general game tree.  The game_tree is built using
 *	t_node ADTs.  A game_tree variable consists of a
 *	"root" field which refers to a t_node variable
 *	which has a "data" field and "child" and
 *	"sibling" references, and a "level" value.
 *	
 *
*/


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "t_node.h"
#include "game_state.h"
#include "game_tree.h"
#include "assig_three119.h"


// types
struct game_tree_int
{
	t_node root;			// the node at the top of the tree
};


// global variable
int thought = 0;	// to show 'thinking' -- i.e. that the program hasn't crashed


/*
	*	init_game_tree
	*	initialiser function.
	*	Pre-condition: none
	*	Post-condition: if the parameter value (e) is true then the 
	*					game_tree variable's "root" field is set to NULL
	*					otherwise the game_tree variable's "root" field
	*					refers to a new t_node variable containing the
	*					parameter value (o) of level with parameter 
	*					value (l) with a NULL child, and a NULL sibling
	*	Informally: creates either an empty tree or a leaf node as
	*				required
*/
void init_game_tree(game_tree *tp,bool e,void *o,int l)
{
	trace("game_tree: initialiser starts");
	
	*tp=(game_tree)malloc(sizeof(struct game_tree_int));
	if (e)
	{
		(*tp)->root = NULL;
	}
	else
	{
		init_t_node(&((*tp)->root), o, l);
	}
		
	trace("game_tree: initialiser ends");
}
	
	
/*
	*	is_empty_game_tree
	*	Emptiness test.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: true is returned if the game_tree variable is
	*					empty, false is returned otherwise
	*	Informally: indicate if the game_tree contains no nodes
	*
	*	return boolean whether or not the game tree is empty
*/

bool is_empty_game_tree(game_tree t)
{

	if (t->root!=NULL) {//if the root is null
		return false;//return false
	}
	else {
		return true;
	}
}


/**
	*	get_data
	*	Get function for "root" instance variable's data value.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the value of the game_tree variable's data
	*					field is returned
	*	Informally: return the value within the root node
	*
	*	return void * the data item of the root node
*/
void *get_data(game_tree t)
{
	trace("get_data: get_data starts");
		
	if (is_empty_game_tree(t))
	{
		fprintf(stderr,"get_data: empty game tree");
		exit(1);
	}

	trace("get_data: get_data ends");
	return get_t_node_data(t->root);
}
	
	
/*
	*	get_level
	*	Get function for "root" instance variable's level value.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the value of the game_tree variable's data
	*					field's level is returned
	*	Informally: return the level value within the root node
	*
	*	return int the level number of the root node
*/

int get_level(game_tree t)
{

	trace("get_level: get_level starts");

	if (is_empty_game_tree(t))
	{
		fprintf(stderr, "get_level: empty game tree");
		exit(1);
	}

	trace("get_level: get_level ends");
	return get_t_node_level(t->root);//Return the level field of the root


}
	

/*
*	get_child
*	Get function for "root" instance variable's child value.
*	Pre - condition: the game_tree variable is defined and valid
*	Post - condition : the value of the game_tree variable's child
*					field is returned in a newly
*					constructed game_tree variable
*	Informally : return the game_tree variable's child
*
*	return game_tree the eldest child of the current node
*/
game_tree get_child(game_tree t)
{
	game_tree c;

	trace("get_child: get_child starts");

	if (is_empty_game_tree(t))
	{
		fprintf(stderr, "get_child: empty game tree");
		exit(1);
	}

	init_game_tree(&c, true, NULL, -1);
	c->root = get_t_node_child(t->root);

	trace("get_child: get_child ends");
	return c;
}


/*
	* 	get_sibling
	*	Get function for "root" instance variable's sibling value.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the value of the game_tree variable's sibling
	*					field is returned in a newly constructed 
	*					game_tree variable
	*	Informally: return the game_tree variable's sibling
	*
	*	return game_tree the next sibling of the current node
*/

game_tree get_sibling(game_tree t) 
{

	game_tree c;

	trace("get_sibling: get_sibling starts");

	if (is_empty_game_tree(t))
	{
		fprintf(stderr, "get_sibling: empty game tree");
		exit(1);
	}

	init_game_tree(&c, true, NULL, -1);//new tree
	c->root = get_t_node_sibling(t->root);//Assigning sibling to a new tree

	trace("get_sibling: get_sibling ends");
	return c;



}


/*
	*	set_data
	*	Set function for "root" instance variable's data field.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the t_node variable's data field is altered to
	*					hold the given (o) value
	*	Informally: store the given value in the root node of the
	*				game_tree variable
	*
	*	param o void * to install as data for root node
*/
void set_data(game_tree t,void *o)
{
	trace("set_data: set_data starts");
		
	if (is_empty_game_tree(t))
	{
		fprintf(stderr,"set_data: empty game tree");
		exit(1);
	}
		
	set_t_node_data(t->root,o);
		
	trace("set_data: set_data ends");
}
		
	
/*
	*	set_level
	*	Set function for "root" instance variable's level field.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the t_node variable's level field is altered
	*					to hold the given (l) value
	*	Informally: assign the given value as the level of the
	*				game_tree variable
	*
	*	param l level number for root of current game tree
*/

void set_level(game_tree t,int l)
{
	trace("set_level: set_level starts");

	if (is_empty_game_tree(t))
	{
		fprintf(stderr, "set_level: empty game tree");
		exit(1);
	}

	set_t_node_level(t->root, l);//Set level to "l"

	trace("set_level: set_level ends");


}
	
	
/*
	*	set_child
	*	Set function for "root" instance variable's child field.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the t_node variable's child field is altered
	*					to hold the given (c) value
	*	Informally: assign the given value as the child of the
	*				game_tree variable
	*
	*	param c game_tree to be set as eldest child of current game tree
*/

void set_child(game_tree t, game_tree c)
{

	trace("set_child: set_child starts");

	set_t_node_child(t->root, c->root);//Set C to the child of T

	trace("set_child: set_child ends");


}


/*
	*	set_sibling
	*	Set function for "root" instance variable's sibling field.
	*	Pre-condition: the game_tree variable is defined and valid
	*	Post-condition: the t_node variable's sibling field is altered
	*					to hold the given (s) value
	*	Informally: assign the given value as the sibling of the
	*				game_tree variable
	*
	*	param s game_tree to be set as next sibling of current game tree
*/
void set_sibling(game_tree t,game_tree s)
{
	trace("set_child: set_child starts");

	set_t_node_sibling(t->root, s->root);

	trace("set_child: set_child ends");
}
	
	
/*
*	generate_levelBF
*	Generate the next level of the tree in a breadth-first manner
*	Pre-condition: the given tree is defined, the given queue
*				   is defined
*	Post-condition: an additional level of possible moves has
*					been added to the given game tree and each
*					tree node of the new level also has been
*					added to the queue.
*	Informally: generate the next level of the game tree
*
*	param q Queue of reachable but unexpanded game trees
*/

void generate_levelBF(game_tree t, queue q)
{
	game_state first_game_state;//The original game state
	game_state sec_game_state;//the next game state
	game_tree new_level;//tree level
	int lev;

	lev = get_level(t);
	first_game_state = get_data(t);//get original game state

	for (int i = 1; i <= DIMENSION; i++) {//Traversing the entire array
		for (int j = 1; j <= DIMENSION; j++) {
			if (valid(first_game_state, i, j) && !(taken(first_game_state, i, j)) && !(clash(first_game_state, i, j)))// 
			{
				sec_game_state = clone(first_game_state);//Clone the original game state
				land(sec_game_state, i, j);//add a queen
				init_game_tree(&new_level, false, sec_game_state, lev + 1);// Initialize a new tree

				if (get_child(t) == NULL) {//Judge if it's the first child?
					set_child(t, new_level);
				}
				else {//Set to sibling
					set_sibling(new_level, get_child(t));
					set_child(t, new_level);

				}
				add(q, new_level);//Add to the queue
			}
		}

	}
}


/*
*	build_gameBF
*	Generate the game tree in a breadth-first manner
*	Pre-condition: the given queue and game tree are defined, and
*				   the given int value represents the desired depth
*				   of the tree
*	Post-condition: If the given tree isn't already deep enough, an
*					additional level of possible moves is added to
*					the given game tree and each tree node of the
*					new level also is added to the queue.
*					Finally, the next tree is determined by removing
*					the front of the queue and the process continues
*					until the queue is empty
*	Informally: generate the game tree from the current point
*				in a breadth-first manner until it is "d" levels
*				deep
*
*	param q Queue of reachable but unexpanded game trees
*	param d desired depth (number of queens) that game tree should be built to
*	return	the game tree with the first found solution, or an empty game
*			tree if there is no solution
*/
game_tree build_gameBF(game_tree t, queue q, int d)
{

	game_tree game_tree_casual;// Temporary game tree
	if (get_level(t)==DIMENSION) {// If there are enough queens
		return t;
	}
	generate_levelBF(t, q);//Add a layer

	if (is_empty_queue(q)) {//queue is empty
		init_game_tree(&game_tree_casual, true, NULL, 0);
		return game_tree_casual;
	}else {
		t = front(q);//Get the first element of the queue
		rear(q);//Delete the first element of the queue
		build_gameBF(t, q, DIMENSION);//Call itself
	}


}


/*
*	generate_levelDF
*	Generate the next level of the tree in a depth-first manner
*	Pre-condition: the given tree is defined, the given stack
*				   is defined
*	Post-condition: an additional level of possible moves has
*					been added to the given game tree and each
*					tree node of the new level also has been
*					added to the stack.
*	Informally: generate the next level of the game tree
*
*	param q Stack of reachable but unexpanded game trees
*/
void generate_levelDF(game_tree t, stack s)
{
	
	game_state first_game_state;
	game_state sec_game_state;
	game_tree new_level;
	int lev;

	lev = get_level(t);
	first_game_state = get_data(t);

	for (int i = 1; i <= DIMENSION; i++) {
		for (int j = 1; j <= DIMENSION; j++) {
			if (valid(first_game_state, i, j) && !(taken(first_game_state, i, j)) && !(clash(first_game_state, i, j)))// 
			{
				sec_game_state = clone(first_game_state);
				land(sec_game_state, i, j);
				init_game_tree(&new_level, false, sec_game_state, lev + 1);


				if (get_child(t) == NULL) {
					set_child(t, new_level);
				}
				else {
					set_sibling(new_level, get_child(t));
					set_child(t, new_level);

				}
				push(s, new_level);//Put in stack
			}
		}

	}


}


/*
*	build_gameDF
*	Generate the game tree in a depth-first manner
*	Pre-condition: the given stack and game tree are defined, and
*				   the given int value represents the desired depth
*				   of the tree
*	Post-condition: If the given tree isn't already deep enough, an
*					additional level of possible moves is added to
*					the given game tree and each tree node of the
*					new level also is pushed onto the stack.
*					Finally, the next tree is determined by removing
*					the top of the stack and the process continues
*					until the stack is empty
*	Informally: generate the game tree from the current point
*				in a depth-first manner until it is "d" levels
*				deep
*
*	param s Stack of reachable but unexpanded game trees
*	param d desired depth (number of queens) that game tree should be built to
*	return	the game tree with the first found solution, or an empty game
*			tree if there is no solution
*/
game_tree build_gameDF(game_tree t, stack s, int d)
{
	game_tree game_tree_casual;
	if (get_level(t) == DIMENSION) { 
		return t;
	}
	generate_levelDF(t, s);

	if (is_empty_stack(s)) {
		init_game_tree(&game_tree_casual, true, NULL, 0);
		return game_tree_casual;
	}
	else {
		t = top(s);//Get the stack head
		pop(s);// Delete the stack head
		build_gameDF(t, s, DIMENSION);//Call itself
	}



}


/*
	*	to_string_game_tree
	*	String conversion for tree
	*	Pre-condition: none
	*	Post-condition: a string variable is returned consisting of the
	*				string representation of all items in the game_tree,
	*				from top to bottom in depth-first order, separated by
	*				" " and contained within "<" and ">"
	*	Informally: produce a string representation of the game tree
	*
	*	return (char *) printable contents of game tree
*/
char *to_string_game_tree(game_tree t)
{
	game_tree c;
	char *s;

	trace("to_string_game_tree: to_string_game_tree starts");
		
	if (is_empty_game_tree(t))
	{
		trace("to_string_game_tree: to_string_game_tree ends");
		return "<>";
	}
	else
	{
		s=(char *)malloc(200*5*sizeof(char));

		sprintf(s,"%s (%d,%d) ",s,get_row(get_data(t)),get_column(get_data(t)));
		c=get_child(t);
		if (! is_empty_game_tree(c))
		{
			sprintf(s,"%s %s ",s,to_string_game_tree(c));
		}
		c=get_sibling(t);
		if (! is_empty_game_tree(c))
		{
			sprintf(s,"%s %s ",s,to_string_game_tree(c));
		}
	}

	trace("to_string_game_tree: to_string_game_tree ends");

	return s;
}

