
import java.util.List;

public class Sample {
	
	double[] input;
	double output;		// Known because this is training data.
	
	public Sample(double[] in, double out)
	{
		input = in;
		output = out;
		if (input[0] != 1)
			System.out.println("WARNING: CONSTANT TERM NOT EQUAL TO 1");
	}
	
//	public Sample()
//	{
//		input = new double[2];		// Since this is a linear classifier, we can assume the form y = w0 + w1x.
//		input[0] = 1.0;
//	}
//	public Sample(double i)
//	{
//		input = new double[2];
//		input[0] = 1.0;
//		input[1] = i;
//	}
//	public Sample(double i, double o)
//	{
//		input = new double[2];
//		input[0] = 1.0;
//		input[1] = i;
//		output = o;
//	}
	
	public double[] getInput()
	{
		return input;
	}
	public double getOutput()
	{
		return output;
	}
	
	public String toString()
	{
		String s = "Input: [" + input[0] + ", " + input[1] + "]\nOutput: " + output;
		System.out.println(s);
		return s;
	}
}