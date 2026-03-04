import java.util.*;
import java.util.function.*;

import bn.core.*;
import bn.base.*;
import util.*;

public class Inferinator
{
	public Inferinator()
	{

	}

	/* Query: X
	 * Evidence: e
	 * Network: bn
	 */

	// function ENUMERATION-ASK(X, e, bn) returns a distribution over X
	// Q(X) <- a distribution over X, initially empty
	// for each value x.i of X do
	//		Q(x.i) <- enumerate-all(bn.VARS, e.x.i)
	// 			where e.x.i is e extended with X = x.i
	// return NORMALIZE(Q(X))

	// function ENUMERATION-ASK(X, e, bn) returns a distribution over X
	public bn.base.Distribution exactInference(bn.base.NamedVariable X, bn.base.Assignment evidence, bn.base.BayesianNetwork bn)
	{
		// Q(X) <- a distribution over X, initially empty
		bn.base.Distribution q = new bn.base.Distribution(X);

		// for each value x.i of X do
		bn.base.Assignment e = evidence.copy();
		bn.base.Value<Object> xVal = new bn.base.Value<Object>(null);
		for (Iterator grant = X.getRange().iterator(); grant.hasNext(); )
		{
			// Q(x.i) <- enumerate-all(bn.VARS, e.x.i)
			// where e.x.i is e extended with X = x.i
			xVal = (bn.base.Value<Object>)grant.next();

			//			System.out.println("Query: " + X + " (Range: " + X.getRange() + ")");
			//			System.out.println("xVal: " + xVal);
			//			System.out.println("Evidence: " + e);

			NamedVariable newX = new NamedVariable(X.getName(), new bn.base.Range(xVal));

			//			System.out.println("x.i: " + newX + " (Range: " + newX.getRange() + ")");

			e.put(newX, xVal);

			//			System.out.println("E-PUT: " + e);
			//			System.out.println("Evidence + x.i: " + e);

			double finalprob = enumerateAll(bn.getVariablesSortedTopologically(), e, bn);

			//			System.out.println("FINAL PROB: " + finalprob);

			q.set(xVal, finalprob);

			//			System.out.println("DIST: " + q);

			e.remove(newX);

			//			System.out.println("E-REMOVE: " + e);

			e = evidence.copy();
		}
		// return NORMALIZE(Q(X))
		//		System.out.println("UNNORMALIZED DIST: " + q);
		q.normalize();
		//		System.out.println("NORMALIZED DIST: " + q);
		return q;
	}

	// function ENUMERATE-ALL(vars, e) returns a real number
	// if EMPTY?(vars) then return 1.0
	// Y <- FIRST(vars)
	// if Y has value y in e
	// 		then return P(y|parents(Y)) * ENUMERATE-ALL(REST(vars), e)
	//		else return SUM over y [P(y|parents(Y)) * ENUMERATE-ALL(REST(vars), e.y)]
	//			where e.y is e extended with Y = y

	// function ENUMERATE-ALL(vars, e) returns a real number
	public double enumerateAll(List<RandomVariable> vars, bn.base.Assignment e, bn.base.BayesianNetwork bn)
	{
		// if EMPTY?(vars) then return 1.0
		if (vars.isEmpty())
		{
			//			System.out.println("	Vars is empty.");
			return 1.0;
		}
		//		System.out.println("	VARS: " + vars);
		// Y <- FIRST(vars)
		RandomVariable Y = vars.get(0);
		//		System.out.println("	Y: " + Y + " (Range: " + Y.getRange() + ")");
		ArrayList<RandomVariable> rest = new ArrayList<RandomVariable>();
		for (int i=1; i<vars.size(); i++)
		{
			rest.add(vars.get(i));
		}
		//		System.out.println("	REST: " + rest);
		//		System.out.println("	EVIDENCE: " + e);
		// if Y has value y in e
		boolean eContainsY = false;
		bn.base.Assignment parents = new bn.base.Assignment();
		for (Map.Entry<RandomVariable, bn.core.Value> evidenceVariable : e.entrySet())
		{
			if (evidenceVariable.getKey().toString().equals(Y.toString()))
			{
				eContainsY = true;
				//				parents.put(Y, evidenceVariable.getValue()); // NEW
			}
		}
		if (eContainsY)
		{
			//			System.out.println("   ===EVIDENCE VAR DETECTED===");
			// then return P(y|parents(Y)) * ENUMERATE-ALL(REST(vars), e)
			double prob;
			Set<RandomVariable> allParents = bn.getParents(Y);
			Iterator p = allParents.iterator();
			while (p.hasNext())
			{
				RandomVariable next = (RandomVariable)p.next();
				//				System.out.println("	next: " + next + " (Range: " + next.getRange() + ")");
				//				for (Map.Entry<RandomVariable, bn.core.Value> evidenceVariable : e.entrySet())	// NEW
				//				{
				//					if (evidenceVariable.getKey().toString().equals(p.toString()))
				//					{
				//						parents.put(next, evidenceVariable.getValue());
				//					}
				//				}
				parents.put(next, next.getRange().iterator().next());
			}
			parents.put(Y, Y.getRange().iterator().next());

			//			System.out.println("bn.getProbability(Y, parents): " + bn.getProbability(Y, parents));

			prob = bn.getProbability(Y, parents) * enumerateAll(rest, e, bn);
			//			System.out.println("		P = " + prob);
			return prob;
		}
		else
		{
			//			System.out.println("   ===NOT AN EVIDENCE VAR===");
			// else return SUM over y [P(y|parents(Y)) * ENUMERATE-ALL(REST(vars), e.y)]
			// where e.y is e extended with Y = y
			double sum = 0;

			Set<RandomVariable> allParents = bn.getParents(Y);
			Iterator p = allParents.iterator();
			while (p.hasNext())
			{
				RandomVariable next = (RandomVariable)p.next();
				//				System.out.println("	next: " + next + " (Range: " + next.getRange() + ")");
				//				for (Map.Entry<RandomVariable, bn.core.Value> evidenceVariable : e.entrySet())	// NEW
				//				{
				//					if (evidenceVariable.getKey().toString().equals(p.toString()))
				//					{
				//						parents.put(next, evidenceVariable.getValue());
				//					}
				//				}
				parents.put(next, next.getRange().iterator().next());
			}
			Iterator jeff = Y.getRange().iterator();
			if (jeff.hasNext())
			{
				bn.base.Value<Object> evidence = (bn.base.Value<Object>)jeff.next();
				parents.put(Y, evidence);
				while(jeff.hasNext())
				{
					e.put(Y, evidence);
					//					System.out.println("	EVIDENCE + y: " + e + "\n");
					//					System.out.println("bn.getProbability(Y, parents): " + bn.getProbability(Y, parents));

					sum += bn.getProbability(Y, parents) * enumerateAll(rest, e, bn);
					e.remove(Y);
					evidence = (bn.base.Value<Object>)jeff.next();
				}
				e.put(Y, evidence);
			}
			//			System.out.println("		SUM: " + sum);
			return sum;
		}
	}



	// function PRIOR-SAMPLE(bn) returns an event sampled from the prior specified by bn
	// x <- an event with n elements
	// foreach variable X.i in X.1, ..., X.n do
	//		x[i] <- a random sample from P(X.i|parents(X.i))
	// return x

	public bn.base.Assignment priorSample(bn.base.BayesianNetwork bn)
	{
		// event <- an event with n elements
		bn.base.Assignment event = new bn.base.Assignment();
		// foreach variable X.i in X.1, ..., X.n do
		for (RandomVariable x : bn.getVariablesSortedTopologically())
		{
			// event[i] <- a random sample from P(X.i|parents(X.i))

			Iterator xVals = x.getRange().iterator();
			bn.core.Value v = (bn.core.Value)xVals.next();
			bn.base.Assignment parents = new bn.base.Assignment();
			Set<RandomVariable> allParents = bn.getParents(x);
			Iterator p = allParents.iterator();
			while (p.hasNext())
			{
				RandomVariable next = (RandomVariable)p.next();
				parents.put(next, next.getRange().iterator().next());
			}
			parents.put(x, v);

			if (Math.random() < bn.getProbability(x, parents))
			{
				event.put(x, v);
			}
			else
			{
				event.put(x, (bn.core.Value)xVals.next());
				//				System.out.println("False expected: " + event.getValue(x));
			}
			//			System.out.println(event);
		}
		// return event
		return event;
	}

	// function REJECTION-SAMPLING(X, e, bn, N) returns an estimate of P(X|e)
	// for j=1 to N do
	// 		x <- PRIOR-SAMPLE(bn)
	// 		if x is consistent with e then
	// 			n[x.v] <- n[x.v}+1 where x.v is the value of X in x
	// 		return NORMALIZE(n)

	public bn.base.Distribution rejectionSampling(bn.base.NamedVariable X, bn.base.Assignment e, bn.base.BayesianNetwork bn, int N)
	{
		bn.base.Distribution n = new bn.base.Distribution(X);
		// for j=1 to N do
		for (int i=0; i<N; i++)
		{
			// x <- PRIOR-SAMPLE(bn)
			bn.base.Assignment sample = priorSample(bn);
//			System.out.println(sample);

			// if x is consistent with e then
//			System.out.println("evidence: " + e);

//			boolean consistent = true;
//			for (Map.Entry<RandomVariable, bn.core.Value> evidenceVariable : e.entrySet())
//			{
//				for (Map.Entry<RandomVariable, bn.core.Value> x : sample.entrySet())
//				{
//					if (evidenceVariable.getKey().toString().equals(sample.toString()) && !evidenceVariable.getValue().toString().equals(x.getValue().toString()))
//					{
//						consistent = false;
//					}
//				}
//			}


//			System.out.println(sample.containsAll(e));
//			if (consistent)
			if (sample.containsAll(e))
			{
				// n[x.v] <- n[x.v}+1 where x.v is the value of X in x
				n.set(sample.get(X), n.get(sample.get(X))+1);
				System.out.println("Yay!");
			}
		}
		// return NORMALIZE(n)
		n.normalize();
		//		System.out.println(bn);
		return n;
	}

	// function WEIGHTED-SAMPLE(bn, e) returns an event and a weight
	// w <- 1; x <- an event with n elements initialized from e
	// foreach variable X.i in X.1, ..., X.n do
	// 		if (X.i is an evidence variable with value x.i.v in e
	// 			then w <- w * P(X.i = x.i.v | parents(X.i))
	// 			else x[i] <- a random sample from P(X.i | parents(X.i))
	// return x, w

	//	public class WeightedSample
	//	{
	//		private bn.base.Assignment event;
	//		private double weight;
	//		public WeightedSample(bn.base.Assignment x, double w)
	//		{
	//			event = x;
	//			weight = w;
	//		}
	//		public bn.base.Assignment event()
	//		{
	//			return event;
	//		}
	//		public double weight()
	//		{
	//			return weight;
	//		}
	//	}
	//	
	//	public WeightedSample weightedSample(bn.base.BayesianNetwork bn, bn.base.Assignment e)
	//	{
	//		// w <- 1; event <- an event with n elements initialized from e
	//		bn.base.Assignment event = e.copy();
	//		double w = 1;
	//		// foreach variable X.i in X.1, ..., X.n do
	//		for (RandomVariable x : bn.getVariablesSortedTopologically())
	//		{
	//			// if (X.i is an evidence variable with value x.i.v in e
	//			boolean eContainsX = false;
	//			bn.base.Value<Object> xVal = new bn.base.Value<Object>(null);
	//			for (Map.Entry<RandomVariable, bn.core.Value> evidenceVariable : e.entrySet())
	//			{
	//				if (evidenceVariable.getKey().toString().equals(x.toString()))
	//				{
	//					eContainsX = true;
	//					xVal = (bn.base.Value<Object>)evidenceVariable.getValue();
	//				}
	//			}
	//			if (eContainsX)
	//			{
	//				// then w <- w * P(X.i = x.i.v | parents(X.i))
	//				bn.base.Assignment parents = new bn.base.Assignment();
	//				Set<RandomVariable> allParents = bn.getParents(x);
	//				Iterator p = allParents.iterator();
	//				while (p.hasNext())
	//				{
	//					RandomVariable next = (RandomVariable)p.next();
	//					parents.put(next, next.getRange().iterator().next());
	//				}
	//				parents.put(x, xVal);
	//				w *= bn.getProbability(x, parents);
	//			}
	//			else
	//			{
	//				// else event[i] <- a random sample from P(X.i | parents(X.i))
	//				bn.core.Value v = x.getRange().iterator().next();
	//				bn.base.Assignment parents = new bn.base.Assignment();
	//				Set<RandomVariable> allParents = bn.getParents(x);
	//				Iterator p = allParents.iterator();
	//				while (p.hasNext())
	//				{
	//					RandomVariable next = (RandomVariable)p.next();
	//					parents.put(next, next.getRange().iterator().next());
	//				}
	//				parents.put(x, v);
	//				
	//				if (Math.random() < bn.getProbability(x, parents))
	//				{
	//					event.put(x, v);
	//				}
	//				else
	//				{
	//					event.put(x, x.getRange().iterator().next());
	////					System.out.println("False expected: " + event.getValue(x));
	//				}
	//			}
	//		}
	//		// return event, w
	//		return new WeightedSample(event, w);
	//	}
	//	
	//	// function LIKELIHOOD-WEIGHTING(X, e, n, N) returns an estimate of P(X|e)
	//	// for j=1 to N do
	//	// 		x, w <- WEIGHTED-SAMPLE(bn, e)
	//	// 		W[x.v] <- W[x.v] + w where x.v is the value of X in x
	//	// return NORMALIZE(W)
	//	
	//	public bn.base.Distribution likelihoodWeighting(RandomVariable X, bn.base.Assignment e, bn.base.BayesianNetwork bn, int N)
	//	{
	//		bn.base.Distribution W = new bn.base.Distribution(X);
	//		// for j=1 to N do
	//		for (int i=0; i<N; i++)
	//		{
	//			// x, w <- WEIGHTED-SAMPLE(bn, e)
	//			WeightedSample sample = weightedSample(bn, e);
	//			// W[x.v] <- W[x.v] + w where x.v is the value of X in x
	//			W.set(sample.event().get(X), W.get(sample.event().get(X))+sample.weight());
	//		}
	//		// return NORMALIZE(W)
	//		W.normalize();
	//		return W;
	//	}
}