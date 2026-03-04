
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

public class LogisticClassifier extends LinearClassifier {

	double[] weight;
	double learningRate;

	//	public LogisticClassifier()
	//	{
	//
	//	}
	//	public LogisticClassifier(double l)
	//	{
	//		super(l);
	//	}
	//	public LogisticClassifier(int num, double l)
	//	{
	//		super(num, l);
	//	}
	//
	//	public void initializeWeight(int num)
	//	{
	//		super.initializeWeight(num);
	//	}
	//	public double[] getWeight()
	//	{
	//		return super.getWeight();
	//	}
	//	public double getLearningRate()
	//	{
	//		return super.getLearningRate();
	//	}
	//
	//	public String toString()
	//	{
	//		return super.toString();
	//	}
	//
	//	public double totalLoss(List<Sample> samples)
	//	{
	//		return super.totalLoss(samples);
	//	}

	public double threshold(Sample x)
	{
		double wx = 0;
		for (int j=0; j<weight.length; j++)
		{
			wx += weight[j] * x.getInput()[j];
			//			System.out.println("wx += " + weight[j] + " * "+ x.getInput()[j]);
		}
		double logistic = 1/(1 + Math.pow(Math.E, -1*wx));
		//		System.out.println("Threshold returned " + logistic + " with wx = " + wx + ".");
		return logistic;
	}

	public double[] updateRule(List<Sample> x, int steps, String filename)
	{
		int numUpdates = 0;
		if (filename.length() > 0)
		{
			try
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(filename, false));

				for (int n=0; n<steps; n++)
				{
					double numCorrect = 0.0;
					int numTotal = 0;

					for (int i=0; i<weight.length; i++)
					{
						for (int j=0; j<x.size(); j++)
						{
							double hw = threshold(x.get(j));
							if (learningRate == 0)
								weight[i] = weight[i] + getLearningRate(numUpdates) * (x.get(j).getOutput() - hw) * hw * (1 - hw) * x.get(j).getInput()[i];
							else
								weight[i] = weight[i] + learningRate * (x.get(j).getOutput() - hw) * hw * (1 - hw) * x.get(j).getInput()[i];
							if (Math.round(hw) == x.get(j).getOutput())
								numCorrect++;
							numUpdates++;
							numTotal++;
						}
						out.write(numUpdates + ", " + numCorrect/numTotal);
						out.newLine();
					}
				}
				out.close();
			}
			catch (IOException todd)
			{
				System.out.println("File cannot be written.");
			}
		}
		else
		{
			for (int n=0; n<steps; n++)
			{
				for (int i=0; i<weight.length; i++)
				{
					for (int j=0; j<x.size(); j++)
					{
						double hw = threshold(x.get(j));
						if (learningRate == 0)
							weight[i] = weight[i] + getLearningRate(numUpdates) * (x.get(j).getOutput() - hw) * hw * (1 - hw) * x.get(j).getInput()[i];
						else
							weight[i] = weight[i] + learningRate * (x.get(j).getOutput() - hw) * hw * (1 - hw) * x.get(j).getInput()[i];
						numUpdates++;
					}
				}
			}
		}
		System.out.println();
		System.out.print("y = " + weight[0]);
		for (int i=1; i<x.get(0).getInput().length; i++)
		{
			System.out.print(" + " + weight[i] + " * x" + i);
		}
		return weight;
	}

	//	public LinkedList<Sample> addInput(String filename)
	//	{
	//		return super.addInput(filename);
	//	}


	public LogisticClassifier()
	{

	}
	public LogisticClassifier(double l)
	{
		learningRate = l;
	}
	public LogisticClassifier(int num, double l)
	{
		weight = new double[num];
		learningRate = l;
	}

	public void initializeWeight(int num)
	{
		weight = new double[num];
		for (int i=1; i<weight.length; i++)
		{
			weight[i] = Math.random()*-0.5 + 0.5;
		}
	}
	public double[] getWeight()
	{
		return weight;
	}
	public double getLearningRate()
	{
		return learningRate;
	}
	public double getLearningRate(int t)
	{
		return 1000.0/(1000.0 + t);
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





	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("To read from a file, enter the filename. To enter data manually instead, press ENTER.");
		String filename = input.nextLine();
		System.out.println("To output to a file, enter the filename. Otherwise, press ENTER.");
		String outFilename = input.nextLine();
		if (filename.length() == 0)
		{
			System.out.println("Please input the number of samples.");
			int numSamples = input.nextInt();
			int numInputs = 0;
			LinkedList<Sample> data = new LinkedList<Sample>();

			input.nextLine();

			for (int i=0; i<numSamples; i++)
			{
				System.out.println("Please input a sample in the format \"input input input ... output\".");
				String line = input.nextLine();

				LinkedList<Double> in = new LinkedList<Double>();
				in.add(1.0);
				Double out = null;
				String current = "";

				for (int j=0; j<line.length(); j++)
				{
					if (line.charAt(j) == ' ' || j == line.length()-1)
					{
						try
						{
							if (j == line.length()-1)
							{
								current = current + line.charAt(j);
								out = Double.parseDouble(current);
							}
							else
							{
								in.add(Double.parseDouble(current));
							}
						}
						catch (NumberFormatException alas)
						{
							System.out.println("Parsing error.");
						}
						current = "";
					}
					if (j != line.length()-1)
						current = current + line.charAt(j);
				}

				double[] inArray = new double[in.size()];
				for (int j=0; j<in.size(); j++)
				{
					inArray[j] = in.get(j);
				}

				data.add(new Sample(inArray, out));
			}
			System.out.println("Please input the learning rate. For a decaying learning rate, enter 0.");
			double rate = input.nextDouble();
			System.out.println("Please input the number of steps.");
			int steps = input.nextInt();

			System.out.println("Performing gradient descent.");
			LogisticClassifier grant = new LogisticClassifier(data.get(0).getInput().length, rate);
			grant.updateRule(data, steps, outFilename);

		}
		else
		{
			System.out.println("Please input the learning rate. For a decaying learning rate, enter 0.");
			double rate = input.nextDouble();
			System.out.println("Please input the number of steps.");
			int steps = input.nextInt();

			LogisticClassifier grant = new LogisticClassifier(rate);
			LinkedList<Sample> data = grant.addInput(filename);
			grant.initializeWeight(data.get(0).getInput().length);

			System.out.println("Performing gradient descent.");
			grant.updateRule(data, steps, outFilename);
		}
	}
}