import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.*;
import bn.base.*;
import bn.parser.*;
import util.*;

public class Test
{
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		String filename = "";
		if (args.length > 0)
		{
			filename = args[0];
		}
		XMLBIFParser parser = new XMLBIFParser();
		bn.base.BayesianNetwork network = parser.readNetworkFromFile(filename);
		
		String query = "";
		if (args.length > 1)
			query = args[1];
		
		bn.base.Assignment evidence = new bn.base.Assignment();
		for (int i=2; i<args.length-1; i+=2)
		{
			BooleanValue value = null;
			if (args[i+1].equals("true"))
				value = BooleanValue.TRUE;
			else if (args[i+1].equals("false"))
				value = BooleanValue.FALSE;
			NamedVariable grant = new NamedVariable(args[i], new bn.base.Range(value));
			evidence.put(grant, value);
		}
//		System.out.println("EVIDENCE: " + evidence);
//		
//		System.out.println("NETWORK: " + network);
		
		Inferinator doof = new Inferinator();
		
		bn.base.Distribution fullDist = doof.exactInference((NamedVariable)network.getVariableByName(query), evidence, network);
		System.out.println("EXACT INFERENCE DISTRIBUTION: " + fullDist);
		
		bn.base.Distribution rsDist = doof.rejectionSampling((NamedVariable)network.getVariableByName(query), evidence, network, 1000);
		System.out.println("REJECTION SAMPLING DISTRIBUTION: " + rsDist);
		
//		bn.base.Distribution lwDist = doof.likelihoodWeighting((NamedVariable)network.getVariableByName(query), evidence, network, 1000);
//		System.out.println("LIKELHOOD WEIGHTING DISTRIBUTION: " + lwDist + "\n");
	}
}