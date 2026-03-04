import java.util.ArrayList;

public class PossibleWorld
{
	ArrayList<Boolean> world;
	
	public PossibleWorld()
	{
		world = new ArrayList<Boolean>();
	}
	
	public void addProp(int p)
	{
		while (world.size() <= Math.abs(p))
		{
			world.add(null);
		}
		if (p == Math.abs(p))
		{
			world.remove(p);
			world.add(p, true);
		}
		else
		{
			world.remove(Math.abs(p));
			world.add(Math.abs(p), false);
		}
	}
	public Boolean getProp(int i)
	{
		if (Math.abs(i) < world.size())
			return world.get(Math.abs(i));
		else
			return null;
	}
	public int getSize()
	{
		return world.size();
	}
	
	public Boolean checkTrue(KnowledgeBase sentence)
	{
//		printPW();	
		for (int c=0; sentence.getKB().size() > c; c++)
		{
			boolean clauseTrue = false;
			for (int p=0; sentence.getKB().get(c).getClause().size() > p; p++)
			{
				int prop = sentence.getKB().get(c).getClause().get(p);
				boolean propVal = (prop == Math.abs(prop));
				if (getProp(prop) != null && getProp(prop) == propVal)	// If one proposition in the disjunction is true, the clause is true.
					clauseTrue = true;
			}
			if (clauseTrue == false)	// If one clause in the conjunction is false, then the sentence is false.
			{
//				System.out.print(": False for ");
//				sentence.printKB();
//				System.out.println();
				return false;
			}
		}
//		System.out.print(": True for ");
//		sentence.printKB();
//		System.out.println();
		return true;
	}
	public int howTrue(KnowledgeBase sentence)
	{
		int howManyTrue = 0;
		for (int c=0; sentence.getKB().size() > c; c++)
		{
			boolean clauseTrue = false;
			for (int p=0; sentence.getKB().get(c).getClause().size() > p; p++)
			{
				int prop = sentence.getKB().get(c).getClause().get(p);
				boolean propVal = (prop == Math.abs(prop));
				if (getProp(prop) != null && getProp(prop) == propVal)	// If one proposition in the disjunction is true, the clause is true.
				{
					clauseTrue = true;
					howManyTrue++;
				}
			}
		}
		return howManyTrue;
	}
	
	public void printPW()
	{
		System.out.print("<");
		if (world.size() > 0)
		{
			if (world.get(0) == null)
				System.out.print("N");
			else if (world.get(0) == true)
				System.out.print("T");
			else
				System.out.print("F");
		}
		for (int i=1; i<world.size(); i++)
		{
			if (world.get(i) == null)
				System.out.print(", N");
			else if (world.get(i) == true)
				System.out.print(", T");
			else
				System.out.print(", F");
		}
		System.out.print(">");
	}
}
