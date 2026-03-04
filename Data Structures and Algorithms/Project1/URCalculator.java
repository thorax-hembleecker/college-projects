import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.lang.Math;
import java.io.IOException;

public class URCalculator
{
	public static void main(String[] args)
	{
		String[] allInputs = new String[25];
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(args[0]));
			String input = in.readLine();
			int i = 0;
			while (input != null && i<allInputs.length)
			{
				allInputs[i] = input;
				input = in.readLine();
				i++;
			}
			in.close();
		}
		catch (IOException todd)
		{
			System.out.println("File cannot be read.");
			return;
		}

		int maxLength = 1;
		int arrayLength = 1;

		for (int a=0; a<allInputs.length; a++)
		{
			arrayLength = 1;
			for (int i=0; i<allInputs[a].length(); i++)
			{
				if (allInputs[a].charAt(i) == ' ' || allInputs[a].charAt(i) == '(' || allInputs[a].charAt(i) == ')')
					arrayLength++;
			}
			if (allInputs[a].charAt(allInputs[a].length()-1) == ' ')
				arrayLength--;
			if (arrayLength > maxLength)
				maxLength = arrayLength;
		}

		String[][] complete = new String[allInputs.length][maxLength];

		for (int a=0; a<allInputs.length; a++)
		{
			arrayLength = 1;
			for (int i=0; i<allInputs[a].length(); i++)
			{
				if (allInputs[a].charAt(i) == ' ' || allInputs[a].charAt(i) == '(' || allInputs[a].charAt(i) == ')')
					arrayLength++;
			}
			if (allInputs[a].charAt(allInputs[a].length()-1) == ' ')
				arrayLength--;

			String[] grant = new String[arrayLength];
			String temp = "";
			int j = 0;
			for (int i=0; i<allInputs[a].length(); i++)
			{
				if (allInputs[a].charAt(i) == ' ')
				{	
					grant[j] = temp;
					j++;
					temp = "";
				}
				else if (allInputs[a].charAt(i) == '(')
				{
					grant[j] = "(";
					j++;
					temp = "";
				}
				else if (allInputs[a].charAt(i) == ')')
				{
					grant[j] = temp;
					j++;
					while (i < allInputs[a].length() && allInputs[a].charAt(i) == ')')
					{
						grant[j] = ")";
						j++;
						i++;
					}
					temp = "";
				}
				else
					temp = temp + allInputs[a].charAt(i);
			}
			if (temp != " " && temp != null && j < grant.length)
				grant[j] = temp;
			temp = "";
			complete[a] = grant;
		}

		//		System.out.println("complete.length = " + complete.length);
		//		System.out.println("maxLength = " + maxLength);

		for (int i=0; i<complete.length; i++)
		{
			//			for (int j=0; j<complete[i].length; j++)
			//				System.out.print(complete[i][j] + " ");
			//			System.out.println();

			//			for (int j=0; j<inToPost(complete[i]).length; j++)
			//				System.out.print(inToPost(complete[i])[j] + " ");
			//			System.out.println();

			//			System.out.print(evaluate(inToPost(complete[i])));
			//			System.out.println();
		}

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(args[1], false));
			for (int i=0; i<complete.length; i++)
			{
				out.write(evaluate(inToPost(complete[i])) + "");
				out.newLine();
//				System.out.println("File successfully written.");
			}
			out.close();
		}
		catch (IOException todd)
		{
			System.out.println("File cannot be written.");
			return;
		}
	}

	public static boolean isOperand(String s)
	{
		try
		{
			double grant = Double.parseDouble(s);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	public static String[] inToPost(String[] input)
	{	
		String[] output = new String[input.length];
		Stack<String> s = new Stack<String>();
		Queue<String> q = new Queue<String>();

		for (int i=0; i<input.length; i++)
		{
			if (isOperand(input[i]))
				q.enqueue(input[i]);
			else
			{
				if (input[i].equals(")"))
				{
					output = new String[output.length-1];
					while (s.size() > 0)
					{
						String grant = s.pop();
						if (grant.equals("("))
							break;
						q.enqueue(grant);
					}
				}
				else if (input[i].equals("("))
				{
					s.push(input[i]);
				}
				else if (input[i].equals("+") || input[i].equals("-") || input[i].equals("*") || input[i].equals("/") || input[i].equals("=") || input[i].equals("<") || input[i].equals(">") || input[i].equals("&") || input[i].equals("|") || input[i].equals("!") || input[i].equals("^"))
				{
					while (s.size() > 0)
					{
						String grant = s.pop();

						if (input[i].equals("|"))
						{

						}
						else if (input[i].equals("&"))
						{
							if (grant.equals("|"))
							{
								s.push(grant);
								break;
							}
						}
						else if (input[i].equals("="))
						{
							if (grant.equals("|") || grant.equals("&"))
							{
								s.push(grant);
								break;
							}
						}
						else if (input[i].equals("<") || input[i].equals(">"))
						{
							if (grant.equals("|") || grant.equals("&") || grant.equals("="))
							{
								s.push(grant);
								break;
							}
						}
						else if (input[i].equals("+") || input[i].equals("-"))
						{
							if (grant.equals("|") || grant.equals("&") || grant.equals("=") || grant.equals("<") || grant.equals(">"))
							{
								s.push(grant);
								break;
							}
						}
						else if (input[i].equals("*") || input[i].equals("/"))
						{
							if (grant.equals("|") || grant.equals("&") || grant.equals("=") || grant.equals("<") || grant.equals(">") || grant.equals("+") || grant.equals("-"))
							{
								s.push(grant);
								break;
							}
						}
						else if (input[i].equals("^"))
						{
							if (grant.equals("|") || grant.equals("&") || grant.equals("=") || grant.equals("<") || grant.equals(">") || grant.equals("+") || grant.equals("-") || grant.equals("*") || grant.equals("/") || grant.equals("^"))
							{
								s.push(grant);
								break;
							}
						}
						else if (input[i].equals("!"))
						{
							if (grant.equals("|") || grant.equals("&") || grant.equals("=") || grant.equals("<") || grant.equals(">") || grant.equals("+") || grant.equals("-") || grant.equals("*") || grant.equals("/") || grant.equals("^") || grant.equals("!"))
							{
								s.push(grant);
								break;
							}
						}

						if (!grant.equals("(") && !grant.equals(")"))
							q.enqueue(grant);
						else
						{

							s.push(grant);
							break;
						}

					}
					s.push(input[i]);
				}
				else
				{
					System.out.println("Invalid input.");
				}
			}
		}

		while (s.size() > 0)
		{
			q.enqueue(s.pop());
		}

		for (int i=0; i<output.length; i++)
			output[i] = q.dequeue();

		return output;
	}

	public static double evaluate(String[] input)
	{
		Stack<String> s = new Stack<String>();
		Queue<String> q = new Queue<String>();

		for (int i=0; i<input.length; i++)
		{
			if (input[i] == null)
				break;

			double value;
			double temp;

			if (isOperand(input[i]))
				s.push(input[i]);
			else if (input[i].equals("+") || input[i].equals("-") || input[i].equals("*") || input[i].equals("/") || input[i].equals("=") || input[i].equals("<") || input[i].equals(">") || input[i].equals("&") || input[i].equals("|") || input[i].equals("!") || input[i].equals("^"))
			{
				if (input[i].equals("+"))
				{
					value = Double.parseDouble(s.pop()) + Double.parseDouble(s.pop());
					s.push("" + value);
				}
				if (input[i].equals("-"))
				{
					temp = Double.parseDouble(s.pop());
					value = Double.parseDouble(s.pop()) - temp;
					s.push("" + value);
				}
				if (input[i].equals("*"))
				{
					value = Double.parseDouble(s.pop()) * Double.parseDouble(s.pop());
					s.push("" + value);
				}
				if (input[i].equals("/"))
				{
					temp = Double.parseDouble(s.pop());
					value = Double.parseDouble(s.pop()) / temp;
					s.push("" + value);
				}
				if (input[i].equals("="))
				{
					if ((Double.parseDouble(s.pop()) == Double.parseDouble(s.pop())) == true)
						value = 1;
					else
						value = 0;
					s.push("" + value);
				}
				if (input[i].equals("<"))
				{
					if ((Double.parseDouble(s.pop()) > Double.parseDouble(s.pop())) == true)
						value = 1;
					else
						value = 0;
					s.push("" + value);
				}
				if (input[i].equals(">"))
				{
					if ((Double.parseDouble(s.pop()) < Double.parseDouble(s.pop())) == true)
						value = 1;
					else
						value = 0;
					s.push("" + value);
				}
				if (input[i].equals("&"))
				{
					if (Double.parseDouble(s.pop()) == 1 && Double.parseDouble(s.pop()) == 1)
						value = 1;
					else
						value = 0;
					s.push("" + value);
				}
				if (input[i].equals("|"))
				{
					if (Double.parseDouble(s.pop()) == 1 || Double.parseDouble(s.pop()) == 1)
						value = 1;
					else
						value = 0;
					s.push("" + value);
				}
				if (input[i].equals("!"))
				{
					double grant = Double.parseDouble(s.pop());
					if (grant == 1)
						value = 0;
					else if (grant == 0)
						value = 1;
					else
					{
						System.out.println("Wow, you tried to do a thing you weren't supposed to.");
						break;
					}
					s.push("" + value);
				}
				if (input[i].equals("^"))
				{
					temp = Double.parseDouble(s.pop());
					value = Math.pow(Double.parseDouble(s.pop()), temp);
					s.push("" + value);
				}
			}
		}

		String resultString = s.pop();
		if (resultString != null)
		{
			try
			{
				double grant = Double.parseDouble(resultString);
			}
			catch (NumberFormatException e)
			{
				System.out.println("WOAH. THIS IS A PROBLEM.");
				return -1;
			}
			double result = Double.parseDouble(resultString);
			return result;
		}
		else
		{
			System.out.println("WOAH. THIS IS A PROBLEM.");
			return -1;
		}
	}
}
