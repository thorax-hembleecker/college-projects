Instructions for running:

===TIMING CHARACTERIZATION===

1. Compile the program:
	gcc latency.c -o latency
   And run it on a specified core (I used core 1 for my measurements):
	taskset -c 1 ./latency
2. The program will output the names of 4 files generated, which you can download to view the data.

===SEND/RECEIVE===

Note: I *did* have a version of this that signaled the start of a message, making the initial sync easy, but it got messed up while I was trying to make other optimizations, and I wasn't able to get it working again :( Because of that, this version has the sender program give a countdown before it starts sending, and then runs the sync function on a large interval (the receiver program does the same when you press ENTER). This provides a sizable window for the receiver to press ENTER in which the two will be synced, but I sincerely apologize for how annoying it is. I swear, it was better at one point lol.

1. Log into 2 separate sessions on the server. One is the sender, and one is the receiver.

2. On the sender side, compile the code:
	gcc run_sender.c -o run_sender
   And run it on one core:
	taskset -c 1 ./run_sender

3. On the receiver side, compile the code:
	gcc run_receiver.c -o run_receiver
   And run it on a different core:
	taskset -c 2 ./run_receiver

4. On the sender side, type a message as prompted. Pressing ENTER will start a countdown to the first bit being sent.

5. On the receiver side, press ENTER to start receiving. The two programs should automatically sync, exchange the message, print outputs, and stop running by themselves.