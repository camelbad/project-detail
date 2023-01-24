# N-Queens
nqueens
N-Queens is a problem from the board game of Chess which has many such real-life applications.

In N-Queens, a board of N rows and N columns must have N Queens placed on the NxN

squares of the board such that there is only one Queen on each row, each column, and 

on the diagonals of each square. An example of 8-Queens is shown below.

Task

Implement the N-Queens problem in C.

The computer will select moves by creating a game tree. A game tree consists of all the 

possible states of the game and the computer determines any possible olution to be the best 

one. Each node of the game tree has children that indicate the states of the game that follow 
from the state of the parent for each possible move, i.e. each node in the tree possesses at
most NxN children of any node but as more moves are made, or if a smaller board is used, then 
there will become less.Uses many data structures (trees, linked-lists, stacks, and queues) to 
solve the puzzle.Create and traverse the game tree using a stack and queue as intermediate data 
structures. When a solution is found the solution will be displayed. 


note: there is not enough memory granted by the operating system to the running 
program to solve the depth-first problem on an 8x8 board or larger, or the breadth-first 
problem on a 6x6 board or larger.
