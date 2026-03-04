import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KnowledgeBase
{
	private ArrayList<Clause> base;

	public KnowledgeBase()
	{
		base = new ArrayList<Clause>();
	}
	public KnowledgeBase(Clause c)
	{
		base = new ArrayList<Clause>();
		base.add(c);
	}

	public void addClause(Clause c)
	{
		base.add(c);
	}
	public ArrayList<Clause> getKB()
	{
		return base;
	}
	public void printKB()
	{
		System.out.print("[");
		if (base.size() > 0)
			base.get(0).printClause();
		for (int i=1; base.size() > i; i++)
		{
			System.out.print(", ");
			base.get(i).printClause();
		}
		System.out.print("]");
	}

	public void addInput(String filename)	// Finds and reads the CNF file you input, then writes it into a KnowledgeBase.
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String input = in.readLine();
			Clause james = new Clause();

			while (input != null && input.length() > 0)
			{
				//	System.out.println("Input: " + input);
				if (input.charAt(0) != 'c' && input.length() > 5 && !input.substring(0, 5).equals("p cnf"))
				{
					String grant = "";
					for (int i=0; i<input.length(); i++)
					{
						//	System.out.println("i=" + i);
						if (input.charAt(i) == '0' && grant.equals(""))
						{
							addClause(james);
							james = new Clause();
						}
						else if (input.charAt(i) == ' ')
						{
							try
							{
								james.addLiteral(Integer.parseInt(grant));
							}
							catch (NumberFormatException john)
							{
								//	System.out.println(grant + ": That ain't a number, sonny boy.");
							}
							grant = "";
						}
						else
						{
							grant = grant + input.charAt(i);
							//	System.out.println(grant);
						}
					}
					try
					{
						james.addLiteral(Integer.parseInt(grant));
						//	System.out.println("SPECIAL GRANT: " + grant);
					}
					catch (NumberFormatException john)
					{
						//	System.out.println(grant + ": That ain't a number, sonny boy.");
					}
				}
				input = in.readLine();
			}
			in.close();
		}
		catch (IOException alas)
		{
			System.out.println("File cannot be read.");
			return;
		}
	}

	public Boolean entails(KnowledgeBase query)
	{
		ArrayList<Integer> symbols = new ArrayList<Integer>();
		for (int i=0; i<base.size(); i++)
		{
			if (base.get(i) != null)
			{
				for (int j=0; j<base.get(i).getClause().size(); j++)
				{
					if (!symbols.contains(base.get(i).getClause().get(j)))
					{
						symbols.add(base.get(i).getClause().get(j));
					}
				}
			}
		}
		for (int i=0; i<query.getKB().size(); i++)
		{
			if (query.getKB().get(i) != null)
			{
				for (int j=0; j<query.getKB().get(i).getClause().size(); j++)
				{
					if (!symbols.contains(query.getKB().get(i).getClause().get(j)))
					{
						symbols.add(query.getKB().get(i).getClause().get(j));
					}
				}
			}
		}
		PossibleWorld model = new PossibleWorld();
		return checkAll(query, symbols, model, null);
	}
	public Boolean checkAll(KnowledgeBase query, ArrayList<Integer> symbols, PossibleWorld model, Integer proposition)
	{
		//		System.out.println("Running. Input: proposition " + proposition + ".");
		if (proposition != null)
			model.addProp(proposition);
		if (symbols.isEmpty())
		{
			if (model.checkTrue(this))
			{
				return model.checkTrue(query);
			}
			else
			{
				return true;	// A default value so that possible worlds that are not models of kb don't impact results.
			}
		}
		else
		{
			int first = Math.abs(symbols.remove(0));
			ArrayList<Integer> rest = (ArrayList<Integer>)symbols.clone();
			return (checkAll(query, rest, model, first)) && (checkAll(query, rest, model, first*-1));
		}
	}

	/* (game show host voice)
	 * Is... it... SATISFIABLE!??
	 */

	public PossibleWorld GSAT(KnowledgeBase query, int maxFlips, int maxTries)
	{
		for (int i=1; i<maxTries; i++)
		{
			PossibleWorld t = randomAssignment(query);
			for (int j=1; j<maxFlips; j++)
			{
				if (t.checkTrue(query))
					return t;
				t.addProp(bestFlip(t, query));
			}
		}
		System.out.println("No satisfying assignment found.");
		return null;
	}
	public PossibleWorld walkSAT(KnowledgeBase query, double p, int maxFlips, int maxTries)
	{
		for (int i=1; i<maxTries; i++)
		{
			PossibleWorld t = randomAssignment(query);
			for (int j=1; j<maxFlips; j++)
			{
				if (t.checkTrue(query))
					return t;
				if (Math.random() >= p)
				{
					int clause = randomFalse(t, query);
					Clause random = query.getKB().get(clause);
					int flip = Math.abs(random.getClause().get((int)(Math.random()*(random.getClause().size())-1)));
					if (t.getProp(flip) == true)
						t.addProp(flip*-1);
					else
						t.addProp(flip);
				}
				else
				{
					int clause = randomFalse(t, query);
					t.addProp(bestFlipClause(t, query, clause));
				}
			}
		}
		System.out.println("No satisfying assignment found.");
		return null;
	}

	public PossibleWorld randomAssignment(KnowledgeBase query)
	{
		if (query == null)
		{
			System.out.println("Error: Null query.");
			return null;
		}
		PossibleWorld random = new PossibleWorld();
		for (int i=0; i<query.getKB().size(); i++)
		{
			if (query.getKB().get(i) != null)
			{
				for (int j=0; j<query.getKB().get(i).getClause().size(); j++)
				{
					if (random.getProp(query.getKB().get(i).getClause().get(j)) == null)
					{
						if ((int)(Math.random()*2) == 0)
							random.addProp(query.getKB().get(i).getClause().get(j));
						else
							random.addProp(-1*query.getKB().get(i).getClause().get(j));
					}
				}
			}
		}
		return random;
	}
	public int bestFlip(PossibleWorld t, KnowledgeBase query)
	{
		int best = 0;
		int bestVal = -1;
		for (int i=1; i<t.getSize(); i++)
		{
			if (t.getProp(i) != null)
			{
				if (t.getProp(i) == true)
				{
					t.addProp(i*-1);
					int howTrue = t.howTrue(query);
					if (howTrue > bestVal)
					{
						best = i*-1;
						bestVal = howTrue;
					}
				}
				else
				{
					t.addProp(i);
					int howTrue = t.howTrue(query);
					if (howTrue > bestVal)
					{
						best = i;
						bestVal = howTrue;
					}
				}
			}
		}
		return best;
	}
	public int randomFalse(PossibleWorld t, KnowledgeBase query)
	{
		int clause = (int)(Math.random()*query.getKB().size());
		while (t.checkTrue(new KnowledgeBase(new Clause(clause))) != false)
		{
			clause = (int)(Math.random()*query.getKB().size());
		}
		return clause;
	}
	public int bestFlipClause(PossibleWorld t, KnowledgeBase query, int c)
	{
		int best = 0;
		int bestVal = -1;
		Clause clause = query.getKB().get(c);
		for (int i=1; i<clause.getClause().size(); i++)
		{
			int j = clause.getClause().get(i);
			if (t.getProp(j) != null)
			{
				if (t.getProp(j) == true)
				{
					t.addProp(j*-1);
					int howTrue = t.howTrue(query);
					if (howTrue > bestVal)
					{
						best = j*-1;
						bestVal = howTrue;
					}
				}
				else
				{
					t.addProp(j);
					int howTrue = t.howTrue(query);
					if (howTrue > bestVal)
					{
						best = j;
						bestVal = howTrue;
					}
				}
			}
		}
		return best;
	}
}
