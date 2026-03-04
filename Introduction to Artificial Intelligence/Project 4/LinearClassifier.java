
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LinearClassifier {
	/* input vectors
	 * outputs
	 * weight vectors
	 * update rules
	 */


	// House votes: 1, -1, 0 is voting history, last 0 or 1 is Democrat or Republican - AI should guess party




	double[] weight;
	double learningRate;

	/* Subclasses: 
	 * - PerceptronClassifier (hard)
	 * - LogisticClassifier
	 */

	// y = w0 + w1x1 + ... + wixi

	public LinearClassifier()
	{

	}
	public LinearClassifier(double l)
	{
		learningRate = l;
	}
	public LinearClassifier(int num, double l)
	{
		weight = new double[num];
		learningRate = l;
	}

	public void initializeWeight(int num)
	{
		weight = new double[num];
	}
	public double[] getWeight()
	{
		return weight;
	}
	public double getLearningRate()
	{
		return learningRate;
	}

	public String toString()
	{
		String s = "w = [";
		for (int i=0; i<weight.length-1; i++)
		{
			s = s + weight[i] + ", ";
		}
		s = s + weight[weight.length-1] + "]\na = " + learningRate; 
		System.out.println(s);
		return s;
	}

	public double totalLoss(List<Sample> samples)
	{
		double total = 0;
		for (int i=0; i<samples.size(); i++)
		{			
			double sum = 0;
			for (int j=0; j<2; j++)
			{
				sum += weight[j] * samples.get(i).getInput()[j];
			}
			total += Math.pow((samples.get(i).getOutput() - sum), 2);
		}
		return total / samples.size();
	}
	
	public double[] gradientDescent(List<Sample> x, int steps)
	{
		//===FOR TEST PURPOSES===\\
		double[] twoWeight = new double[2];
		if (weight.length == 2)
		{
			twoWeight[0] = weight[0];
			twoWeight[1] = weight[1];
		}
		
		
		for (int n=0; n<steps; n++)
		{	// for each wi in w do:
//			for (int i=0; i<weight.length; i++)
//			{
//				double hypothesis = 0;	// Calculates hypothesis [hw(xi) = w0 + w1x1 + ... wjxi,j] for later sum.
//				for (int j=0; j<x.size(); j++)
//				{
//					hypothesis += weight[i] * x.get(j).getInput()[i];
//				}
//				double sum = 0;
//				for (int j=0; j<x.size(); j++)
//				{
//					sum += x.get(j).getInput()[i] * (x.get(j).getOutput() - hypothesis);
//				}
//				weight[i] = weight[i] + learningRate * sum;
//
//				System.out.println("Sum: " + sum);				
//				System.out.print("w = [");
//				for (int j=0; j<weight.length-1; j++)
//				{
//					System.out.print(weight[j] + ", ");
//				}
//				System.out.println(weight[weight.length-1] + "]");
//			}
			
			/* The sum is the problem.
			 * When the sums are the same, the weights are adjusted the same.
			 * Fix the sum.
			 */

			if (weight.length == 2)
			{
				double sum = 0;
				for (int i=0; i<x.size(); i++)	// Iterating through all samples.
				{
					sum += x.get(i).getOutput() - (twoWeight[0] + twoWeight[1] * x.get(i).getInput()[1]);
				}
				twoWeight[0] = twoWeight[0] + learningRate * sum;

				System.out.println("   2-Sum: " + sum);
				System.out.println("   2-w = [" + twoWeight[0] + ", " + twoWeight[1] + "]");

				sum = 0;
				for (int i=0; i<x.size(); i++)	// Iterating through all samples.
				{
					sum += (x.get(i).getOutput() - (twoWeight[0] + twoWeight[1] * x.get(i).getInput()[1])) * x.get(i).getInput()[1];
				}
				twoWeight[1] = twoWeight[1] + learningRate * sum;
			}
		}
//		System.out.println();
//		System.out.print("y = " + weight[0]);
//		for (int i=1; i<x.get(0).getInput().length; i++)
//		{
//			System.out.print(" + " + weight[i] + " * x" + i);
//		}
		
		if (weight.length == 2)
		{
			System.out.println();
			System.out.print("   y = " + twoWeight[0]);
			for (int i=1; i<x.get(0).getInput().length; i++)
			{
				System.out.print(" + " + twoWeight[i] + " * x" + i);
			}
		}

		return weight;		// May want to replace weight with a separate variable so it doesn't change stuff outside of the function.
	}

	public LinkedList<Sample> addInput(String filename)
	{
		try
		{
			int numInputs = 0;

			BufferedReader grant = new BufferedReader(new FileReader(filename));
			String val = "";

			LinkedList<Sample> data = new LinkedList<Sample>();
			String input = grant.readLine();

			while (input != null)
			{
				LinkedList<Double> in = new LinkedList<Double>();
				in.add(1.0);
				Double out = null;

				for (int i=0; input != null && i<input.length(); i++)
				{
					if (input.charAt(i) == ',')
					{
						try
						{
							in.add(Double.parseDouble(val));
						}
						catch (NumberFormatException alas)
						{
							System.out.println("Input " + val + " not a number.");
						}
						val = "";
					}
					else
						val = val + input.charAt(i);

					if (i == input.length() - 1)
					{
						try
						{
							out = Double.parseDouble(val);
						}
						catch (NumberFormatException alas)
						{
							System.out.println("Output " + val + " not a number.");
						}
						val = "";
					}
				}
				double[] inArray = new double[in.size()];
				for (int i=0; i<in.size(); i++)
				{
					inArray[i] = in.get(i);
				}
				Sample todd = new Sample(inArray, out);
				data.add(todd);

				input = grant.readLine();
				numInputs = in.size();
			}

			System.out.print("File has been read. ");
			grant.close();

			weight = new double[numInputs];
			return data;
		}
		catch (IOException alas)
		{
			System.out.println("File cannot be read.");
			return null;
		}
	}
}