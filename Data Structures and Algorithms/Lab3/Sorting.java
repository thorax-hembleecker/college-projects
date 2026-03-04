/******************************************************************************
 *  Compilation:  javac Sorting.java
 *  Execution:    java Sorting input.txt AlgorithmUsed
 *  Dependencies: StdOut.java In.java Stopwatch.java
 *  Data files:   http://algs4.cs.princeton.edu/14analysis/1Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/2Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/4Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/8Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/16Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/32Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/1Mints.txt
 *
 *  A program to play with various sorting algorithms. 
 *
 *
 *  Example run:
 *  % java Sorting 2Kints.txt  2
 *
 ******************************************************************************/
package Lab3;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.io.IOException;

public class Sorting {


	/**
	 * 
	 * Sorts the numbers present in the file based on the algorithm provided.
	 * 0 = Arrays.sort() (Java Default)
	 * 1 = Bubble Sort
	 * 2 = Selection Sort 
	 * 3 = Quicksort
	 * 4 = Mergesort
	 * 5 = Insertion Sort
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args)  { 
		In in = new In(args[0]);

		// Storing file input in an array
		int[] a = in.readAllInts();

		// TODO: Generate 3 other arrays, b, c, d where
		// b contains sorted integers from a (You can use Java Arrays.sort() method)

		int[] b = a.clone();
		Arrays.sort(b);
		// c contains all integers stored in reverse order
		// (you can have your own O(n) solution to get c from b

		int[] c = b.clone();
		for (int i=0; i<(c.length)/2; i++)
		{
			int placeholder = c[i];
			c[i] = c[c.length-1 - i];
			c[c.length-1 - i] = placeholder;
		}

		// d contains almost sorted array
		//(You can copy b to a and then perform (0.1 * d.length) many swaps to acheive this.

		int[] d = b.clone();
		for (int i=0; i<0.1*d.length; i++)
		{
			int placeholder = d[i];
			d[i] = d[d.length-1 - i];
			d[d.length-1 - i] = placeholder;
		}

		//TODO: 
		// Read the second argument and based on input select the sorting algorithm
		//  * 0 = Arrays.sort() (Java Default)
		//  * 1 = Bubble Sort
		//  * 2 = Selection Sort 
		//  * 3 = Quicksort 
		//  * 4 = Mergesort
		//  * 5 = Insertion Sort
		//  Perform sorting on a,b,c,d. Print runtime for each case along with timestamp and record those. 
		// For runtime and printing, you can use the same code from Lab 4 as follows:

		// TODO: For each array, a, b, c, d:  
		Stopwatch timer = new Stopwatch();

		int[][] arrays = {a, b, c, d};
		int[] sortedArray = new int[a.length];

		// TODO: Perform Sorting and store the result in an array

		double time = timer.elapsedTimeMillis();

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String ID = "jimothy";
		//TODO: Replace with the algorithm used
		String algorithmUsed = "";
		//TODO: Replace with the array used 
		String arrayUsed = "a";
		for (int i=0; i<4; i++)
		{
			if (i==0) arrayUsed = "a";
			if (i==1) arrayUsed = "b";
			if (i==2) arrayUsed = "c";
			if (i==3) arrayUsed = "d";

			switch (args[1])
			{
			case "0":
				sortedArray = arraysSort(arrays[i]);
				algorithmUsed = "Arrays.sort";
				break;
			case "1":
				sortedArray = bubbleSort(arrays[i]);
				algorithmUsed = "Bubble Sort";
				break;
			case "2":
				sortedArray = selectionSort(arrays[i]);
				algorithmUsed = "Selection Sort";
				break;
			case "3":
				quicksort(arrays[i], 0, a.length-1);
				sortedArray = arrays[i];
				algorithmUsed = "Quicksort";
				break;
			}

			StdOut.printf("%s %s %8.1f   %s  %s  %s\n", algorithmUsed, arrayUsed, time, timeStamp, ID, args[0]);
			// Write the resultant array to a file (Each time you run a program 4 output files should be generated. (one for each a, b, c, and d))
			
			String printable = "";
			for (int j=0; j<sortedArray.length; j++)
				printable += sortedArray[j] + ", ";
			
			FileWriter grant = null;
			try
			{
				grant = new FileWriter("C:\\" + arrayUsed + ".txt");
				grant.write(printable);
			}
			catch (IOException io)
			{
				System.out.println("Exception: " + io);
			}
			finally
			{
				try
				{
					grant.close();
				}
				catch (IOException io)
				{
					System.out.println("Exception: " + io);
				}
			}
		}
	}

	// Citation: some of these algorithms are adapted from the versions in the textbook.
	
	public static int[] arraysSort(int[] arr)
	{
		Arrays.sort(arr);
		return arr;
	}
	public static int[] bubbleSort(int[] arr)
	{
		boolean allGood = false;
		while (!allGood)
		{
			allGood = true;
			for (int j=1; j<arr.length; j++)
			{
				if (arr[j] < arr[j-1])
				{
					allGood = false;
					int placeholder = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = placeholder;
				}
			}
		}
		return arr;
	}
	public static int[] selectionSort(int[] arr)
	{
		for (int i=0; i<arr.length; i++)
		{
			int start = arr[i];
			for (int j=i; j<arr.length; j++)
			{
				if (arr[j] < start)
				{
					start = arr[j];
					arr[j] = arr[i];
					arr[i] = start;
				}
			}
		}
		return arr;
	}
	public static void quicksort(int[] arr, int lowIndex, int highIndex)
	{
		if (lowIndex >= highIndex)
			return;
		int end = Partition(arr, lowIndex, highIndex);
		quicksort(arr, lowIndex, end);
		quicksort(arr, end+1, highIndex);
	}

	public static int Partition(int[] arr, int lowIndex, int highIndex)
	{
		int pivot = arr[lowIndex + (highIndex - lowIndex)/2];
		boolean done = false;
		while (!done)
		{
			while (arr[lowIndex] < pivot)
				lowIndex += 1;
			while (arr[highIndex] > pivot)
				highIndex -= 1;
			if (lowIndex >= highIndex)
				done = true;
			else
			{
				int temp = arr[lowIndex];
				arr[lowIndex] = arr[highIndex];
				arr[highIndex] = temp;
				lowIndex += 1;
				highIndex -= 1;
			}
		}
		return highIndex;
	}

	//	Merge(numbers, i, j, k) {
	//		   mergedSize = k - i + 1                // Size of merged partition
	//		   mergePos = 0                          // Position to insert merged number
	//		   leftPos = 0                           // Position of elements in left partition
	//		   rightPos = 0                          // Position of elements in right partition
	//		   mergedNumbers = new int[mergedSize]   // Dynamically allocates temporary array
	//		                                         // for merged numbers
	//		   
	//		   leftPos = i                           // Initialize left partition position
	//		   rightPos = j + 1                      // Initialize right partition position
	//		   
	//		   // Add smallest element from left or right partition to merged numbers
	//		   while (leftPos <= j && rightPos <= k) {
	//		      if (numbers[leftPos] <= numbers[rightPos]) {
	//		         mergedNumbers[mergePos] = numbers[leftPos]
	//		         ++leftPos
	//		      }
	//		      else {
	//		         mergedNumbers[mergePos] = numbers[rightPos]
	//		         ++rightPos
	//		         
	//		      }
	//		      ++mergePos
	//		   }
	//		   
	//		   // If left partition is not empty, add remaining elements to merged numbers
	//		   while (leftPos <= j) {
	//		      mergedNumbers[mergePos] = numbers[leftPos]
	//		      ++leftPos
	//		      ++mergePos
	//		   }
	//		   
	//		   // If right partition is not empty, add remaining elements to merged numbers
	//		   while (rightPos <= k) {
	//		      mergedNumbers[mergePos] = numbers[rightPos]
	//		      ++rightPos
	//		      ++mergePos
	//		   }
	//		   
	//		   // Copy merge number back to numbers
	//		   for (mergePos = 0; mergePos < mergedSize; ++mergePos) {
	//		      numbers[i + mergePos] = mergedNumbers[mergePos]
	//		   }
	//		}
	//
	//		MergeSort(numbers, i, k) {
	//		   j = 0
	//		   
	//		   if (i < k) {
	//		      j = (i + k) / 2  // Find the midpoint in the partition
	//		      
	//		      // Recursively sort left and right partitions
	//		      MergeSort(numbers, i, j)
	//		      MergeSort(numbers, j + 1, k)
	//		      
	//		      // Merge left and right partition in sorted order
	//		      Merge(numbers, i, j, k)
	//		   }
	//		}
} 


