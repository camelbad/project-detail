# pathf
 
the path-finding system identify the path between two points of least energy cost base on a given a digital elevation map of the terrain.
 
These are two different cost functions for calculating the energy use from moving between two adjacent points in the DEM(digital elevation map) with the given height 

difference. The first will be used for part A and will always produce a positive number. The second is used for part B and includes a term for regenerative breaking –

hence it can generate a negative value when going downhill.

first task is to generate an adjacency list graph representation of the terrain. Start by generating a DEM, then use the cost functions to build the weighted graph. 

The vertex number of a given x,y position in the map should be calculated as 

x*size+y. 

So, for example, the following DEM:

49 51 51 56 57

46 47 50 52 54

45 47 48 50 49

46 46 47 47 47

45 45 45 45 47


Part A

For part A always produces positive edge weights to generate the adjacency list.

Implement Dijkstra’s algorithm. While a priority-queue implementation would be best for this scenario.

contain a main function that:

 generates a DEM

 converts it to an adjacency list 

 calculates the shortest paths from vertex 0 (top left in the DEM)

 reconstructs the path to the last vertex (bottom right in the DEM)

 creates a copy of the DEM with the heights changed to -1 for any vertex on the path

 prints the DEM to show the path


Part B

For part B may produce negative edge weights (but no negatively weighted cycles, to build a new adjacency list.

Implement the Floyd-Warshall algorithm based.

contain a main function that:

 calculates a new adjacency list 

 calculates all shortest paths using Floyd-Warshall

 reconstructs the path from the first to last vertex (top-left to bottom-right)

 creates a copy of the DEM with the heights changed to -1 for any vertex on the path

 prints the DEM to show the path

