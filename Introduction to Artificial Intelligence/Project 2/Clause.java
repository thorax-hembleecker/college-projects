import java.util.ArrayList;

public class Clause
{
	private ArrayList<Integer> clause;
	private static String filename;

	public Clause()
	{
		clause = new ArrayList<Integer>();		// Each integer represents a literal.
	}
	public Clause(int l)
	{
		clause = new ArrayList<Integer>();
		clause.add(l);
	}

	public void addLiteral(int l)
	{
		clause.add(l);
	}
	public ArrayList<Integer> getClause()
	{
		return clause;
	}
	public String getFilename()
	{
		return filename;
	}
	public void printClause()
	{
		System.out.print("[");
		if (clause.size() > 0)
			System.out.print(clause.get(0));
		for (int i=1; clause.size() > i; i++)
		{
			System.out.print(", ");
			System.out.print(clause.get(i));
		}
		System.out.print("]");
	}
}
