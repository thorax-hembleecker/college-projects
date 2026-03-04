Greetings, traveler, and welcome to this README file.
To build the project, unzip the folder and navigate (through the command prompt or IDE terminal) to the correct folder.
Then, enter "javac Clause.java KnowledgeBase.java PossibleWorld.java Test.java" to build the program, and "java Test" to run it.

Note: Possible worlds are printed in the format <N, T, F, F, T, T...>, with T and F replaceable with whatever the actual truth value is. N stands for "null", and represents the 0th index, which contains nothing for obvious reasons (i.e. 0 can't be positive or negative/true or false).
Note part 2: The terminal will probably tell you KnowledgeBase.java "uses unchecked or unsafe operations." This is due to a cast I made from Object to ArrayList when using the .clone() operation on another ArrayList, and causes no actual issues from what I can see.