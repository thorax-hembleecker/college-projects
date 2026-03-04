	My code uses the shunting-yard algorithm to process a String array 
input, changing it from infix notation to postfix notation, and then 
evaluating it mathematically/logically to produce a result. Additionally, it
is designed to catch errors and print an error message in the console instead
of breaking the code. It was rather difficult to work with mathematical and 
logical operators at the same time, as they didn't seem to want to play nice 
with one another, but I got it working eventually. I also realized at the 
penultimate minute that reaching a lower-precedence operator meant that 
operator shouldn't be enqueued, which was very helpful, and then at the last 
minute I fixed an issue with parentheses that had been bugging me forever. 
Huzzah.
	Files included:
		- README.txt (this)
		- OUTPUT.txt (pretty much what it says)
		- URCalculator.java (the main class, run for a fun time)
		- Stack.java (my stack implementation)
		- Queue.java (my queue implementation)
		- URLinkedList.java (the foundation of Stack and Queue)
		- URList.java (the foundation of URLinkedList)
		- URNode.java (the other foundation of URLinkedList)