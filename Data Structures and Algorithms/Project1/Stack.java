public class Stack<Comparable> extends URLinkedList<Comparable>
{
	
	public Stack()
	{
		super();
	}
	
	public void push(Comparable grant)
	{
		add(grant);
	}
	public Comparable pop()
	{
		return pollLast();
	}
}
