	Basically, my code reads the input, finds the commands and file name, 
reads the file, constructs a graph out of it, and then executes the commands 
from the input. The expected runtime is somewhere in the typical O(V log V) 
range of Dijkstra's algorithm (which I'm reasonably certain is the slowest 
part of the code, so I think it's safe to apply it to the whole thing). If you
want a number, anyway, it took a little more than 10 minutes to generate the 
map of Monroe County, UR campus is almost instant, and NY State I ran once and
it took quite a considerable time but I neglected to record it, and running it
again now would be a great sadness.
	One obstacle I faced was my complete inability to realize for several 
weeks that the reason my Dijkstra's algorithm wasn't working was just that I had
forgotten to remove and re-add the vertices when modifying them. Rather 
unfortunate, really. I did notice eventually, though, and it got fixed and then 
I improved a bunch of other stuff about my algorithm and life is great now. A 
couple important optimizations were having the algorithm stop once the end 
vertex got dequeued (rather than running until the queue was empty), and then 
adding the nodes to the queue as they were visited and became relevant as 
adjacent nodes (rather than adding them all at once at the start). It's still 
slow, but overall? A jolly good time.
	
	Extra credit(?!): I did some fun error handling stuff. Basically, the 
				program can recognize invalid inputs (and prints a 
				relevant error message) to prevent the whole messy 
				business of things not working.

	Files included:
		- README.txt (this)
		- StreetMap.java (the main project file, takes the input file and
		  turns it into a graph and all that)
		- Graph.java (my graph implementation, includes my implementation
		  of Dijkstra's algorithm)
		- Edge.java (a class to represent the edges in a graph, includes
		  my implementation of the Haversine formula)
		- Node.java (a class to represent the vertices in a graph)