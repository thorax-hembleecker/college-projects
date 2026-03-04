package gregory;

import java.util.function.Function;

public class Lab1_Part6
{
	public static void main(String[] args)
	{
		// Section 6. Finds the maximum Character using a function.
		
		Character[] charArray = {'H','E','L', 'L', 'O' };

		Function<Character[], Character> findMax = c ->
		{
			Character grant = c[0];
			for (int i=0; i<c.length; i++)
			{
				if (c[i] > grant)
					grant = c[i];
			}
			return grant;
		};
		
		System.out.println(findMax.apply(charArray));
	}
}
