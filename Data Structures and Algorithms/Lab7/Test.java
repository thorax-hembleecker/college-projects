import java.lang.reflect.Array;

public class Test
{
	public static void main(String[] args)
	{
		Heap grant = new Heap();
		System.out.println("Expected: true " + grant.isEmpty());
		System.out.println("Expected: 0 " + grant.size());
		grant.insert(53);
		System.out.println("Expected: false " + grant.isEmpty());
		System.out.println("Expected: 1 " + grant.size());
		grant.insert(24);
		System.out.println("Expected: 2 " + grant.size());
		grant.insert(45);
		System.out.println("Expected: 3 " + grant.size());
		grant.insert(63);
		System.out.println("Expected: 4 " + grant.size());
		grant.insert(12);
		System.out.println("Expected: 5 " + grant.size());
		grant.insert(24);
		System.out.println("Expected: 6 " + grant.size());
		grant.printHeap();

		while (!grant.isEmpty())
		{
			if (grant.size() > 1)
				System.out.print(grant.deleteMin() + ", ");
			else
				System.out.print(grant.deleteMin());
		}
		System.out.println();

		System.out.println("Expected: 0 " + grant.size());
		System.out.println("Expected: true " + grant.isEmpty());
		grant.printHeap();

		Heap grond = new Heap(3);
		grond.insert(9);
		System.out.println("Expected: 1 " + grond.size());
		grond.insert(21);
		System.out.println("Expected: 2 " + grond.size());
		grond.insert(10);
		System.out.println("Expected: 3 " + grond.size());
		grond.insert(2);
		System.out.println("Expected: 4 " + grond.size());
		grond.insert(5);
		System.out.println("Expected: 5 " + grond.size());
		grond.printHeap();
		grond.insert(14);
		System.out.println("Expected: 6 " + grond.size());
		grond.insert(4);
		System.out.println("Expected: 7 " + grond.size());
		grond.insert(7);
		System.out.println("Expected: 8 " + grond.size());
		grond.insert(23);
		System.out.println("Expected: 9 " + grond.size());
		grond.insert(34);
		System.out.println("Expected: 10 " + grond.size());
		grond.printHeap();

		while (!grond.isEmpty())
		{
			if (grond.size() > 1)
				System.out.print(grond.deleteMin() + ", ");
			else
				System.out.print(grond.deleteMin());
		}
		System.out.println();
		System.out.println("Expected: 0 " + grond.size());
		grond.printHeap();

		Comparable[] ted = {(int)(Math.random()*50), (int)(Math.random()*50), (int)(Math.random()*50), (int)(Math.random()*50), (int)(Math.random()*50), (int)(Math.random()*50), (int)(Math.random()*50), (int)(Math.random()*50)};
		for (int i=0; i<ted.length; i++)
		{
			if (i+1 < ted.length && ted[i+1] != null)
				System.out.print(ted[i] + ", ");
			else if (ted[i] != null)
				System.out.print(ted[i]);
		}
		System.out.println();
		Heap grunkeldorf = new Heap(ted);
		System.out.println("Expected: 8 " + grunkeldorf.size());
		grunkeldorf.printHeap();

		while (!grunkeldorf.isEmpty())
		{
			if (grunkeldorf.size() > 1)
				System.out.print(grunkeldorf.deleteMin() + ", ");
			else
				System.out.print(grunkeldorf.deleteMin());
		}
	}
}