package gregory;

import java.util.function.Function;

public class Lab1
{
	public static void main(String[] args)
	{
		// Section 1.
		
		Integer [] intArray = {1, 2, 3, 4, 5 };
		Double [] doubArray = {1.1, 2.2, 3.3, 4.4};
		Character [] charArray = {'H','E','L', 'L', 'O' };
		String [] strArray = {"once", "upon", "a", "time" };
		printArray(intArray);
		printArray(doubArray);
		printArray(charArray);
		printArray(strArray);
		
		// Section 4.

		System.out.println("max Integer is: " + getMax(intArray));
		System.out.println("max Double is: " + getMax(doubArray));
		System.out.println("max Character is: " + getMax(charArray));
		System.out.println("max String is: " + getMax(strArray));
		
	}

	// Section 1. Prints the arrays using an Object array as the parameter.
	
//		private static void printArray(Object[] Jeff)
//		{
//			for (int i=0; i<Jeff.length; i++)
//			{
//				System.out.print(Jeff[i]);
//				if (i < Jeff.length-1)
//					System.out.print(", ");
//			}
//			System.out.println();
//		}
	
	// Section 2. Prints the arrays using four separate methods with different parameters.

	//	private static void printArray(Integer[] grant)
	//	{
	//		for (int i=0; i<grant.length; i++)
	//		{
	//			System.out.print(grant[i]);
	//			if (i < grant.length-1)
	//				System.out.print(", ");
	//		}
	//		System.out.println();
	//	}
	//	private static void printArray(Double[] grant)
	//	{
	//		for (int i=0; i<grant.length; i++)
	//		{
	//			System.out.print(grant[i]);
	//			if (i < grant.length-1)
	//				System.out.print(", ");
	//		}
	//		System.out.println();
	//	}
	//	private static void printArray(Character[] grant)
	//	{
	//		for (int i=0; i<grant.length; i++)
	//		{
	//			System.out.print(grant[i]);
	//			if (i < grant.length-1)
	//				System.out.print(", ");
	//		}
	//		System.out.println();
	//	}
	//	private static void printArray(String[] grant)
	//	{
	//		for (int i=0; i<grant.length; i++)
	//		{
	//			System.out.print(grant[i]);
	//			if (i < grant.length-1)
	//				System.out.print(", ");
	//		}
	//		System.out.println();
	//	}
	
	// Section 3. Prints the arrays using a generic type as the parameter.

	private static <T> void printArray(T[] grant)
	{
		for (int i=0; i<grant.length; i++)
		{
			System.out.print(grant[i]);
			if (i < grant.length-1)
				System.out.print(", ");
		}
		System.out.println();
	}

	// Section 4. Finds the maximum element using a Comparable array as the parameter.
	
	/* Multiple markers at this line
	- Comparable is a raw type. References to 
	 generic type Comparable<T> should be parameterized
	- Comparable is a raw type. References to 
	 generic type Comparable<T> should be parameterized 
	 
	 Type safety: The method compareTo(Object) belongs to
	 the raw type Comparable. References to generic type 
	 Comparable<T> should be parameterized */
	
//	private static Comparable getMax(Comparable[] anArray)
//	{
//		if (anArray.length > 0)
//		{
//			Comparable max = anArray[0];
//			for (int i=0; i<anArray.length; i++)
//			{
//				if (anArray[i].compareTo(max) > 0)
//					max = anArray[i];
//			}
//			return max;
//		}
//		return null;
//	}
	
	// Section 5. Finds the maximum element using a generic type extending Comparable for type safety.
	
	private static <T extends Comparable<T>> T getMax(T[] anArray)
	{
		if (anArray.length > 0)
		{
			T max = anArray[0];
			for (int i=0; i<anArray.length; i++)
			{
				if (anArray[i].compareTo(max) > 0)
					max = anArray[i];
			}
			return max;
		}
		return null;
	}
}