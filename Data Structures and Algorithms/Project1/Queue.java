public class Queue<Comparable> extends URLinkedList<Comparable>
{	
	public Queue()
	{
		super();
	}
	
	public void enqueue(Comparable grant)
	{
		add(grant);
	}
	public Comparable dequeue()
	{
		return pollFirst();
	}
}
