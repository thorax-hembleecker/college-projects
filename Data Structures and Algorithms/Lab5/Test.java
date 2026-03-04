import java.util.Iterator;

public class Test
{
	public static void main(String[] args)
	{
		HashTable<Integer, Integer> grant = new HashTable<Integer, Integer>();
		System.out.println("Expected: 0 " + grant.size());
		grant.put(35, 53);
		System.out.println("Expected: 1 " + grant.size());
		grant.put(12, 90);
		System.out.println("Expected: 2 " + grant.size());
		grant.put(14, 111);
		System.out.println("Expected: 3 " + grant.size());
		grant.put(75, 22);
		System.out.println("Expected: 4 " + grant.size());
		grant.put(11, 13);
		System.out.println("Expected: 5 " + grant.size());
		System.out.println("Expected: 53 " + grant.get(35));
		System.out.println("Expected: true " + grant.contains(35));
		System.out.println("Expected: 90 " + grant.get(12));
		System.out.println("Expected: true " + grant.contains(12));
		System.out.println("Expected: 111 " + grant.get(14));
		System.out.println("Expected: true " + grant.contains(14));
		System.out.println("Expected: 22 " + grant.get(75));
		System.out.println("Expected: true " + grant.contains(75));
		System.out.println("Expected: 13 " + grant.get(11));
		System.out.println("Expected: true " + grant.contains(11));

		grant.delete(35);
		System.out.println("Expected: null " + grant.get(35));
		System.out.println("Expected: false " + grant.contains(35));
		System.out.println("Expected: 4 " + grant.size());
		System.out.println("Expected: " + grant.contains(35));

		grant.put(35, 43);
		System.out.println("Expected: 43 " + grant.get(35));
		System.out.println("Expected: true " + grant.contains(35));
		grant.put(66, 74);
		System.out.println("Expected: 74 " + grant.get(66));
		System.out.println("Expected: true " + grant.contains(66));
		grant.put(41, 55);
		System.out.println("Expected: 55 " + grant.get(41));
		System.out.println("Expected: true " + grant.contains(41));
		grant.put(3, 4);
		System.out.println("Expected: 4 " + grant.get(3));
		System.out.println("Expected: true " + grant.contains(3));

		System.out.println("Expected: 8 " + grant.size());

		grant.put(4, 3);
		System.out.println("Expected: 3 " + grant.get(4));
		System.out.println("Expected: true " + grant.contains(4));

		Iterator dave = grant.keys().iterator();
		while (dave.hasNext())
		{
			System.out.println(grant.get((Integer)dave.next()));
		}

		HashTable<Integer, String> todd = new HashTable<Integer, String>();
		todd.put(1, "Hello there");
		System.out.println("Expected: 1 " + todd.size());
		todd.put(2, "General Kenobi");
		System.out.println("Expected: 2 " + todd.size());
		todd.put(3, "");
		System.out.println("Expected: 3 " + todd.size());
		todd.put(4, "W E E Z E R");
		System.out.println("Expected: 4 " + todd.size());
		System.out.println("Expected: Hello there " + todd.get(1));
		System.out.println("Expected: true " + todd.contains(1));
		System.out.println("Expected: General Kenobi " + todd.get(2));
		System.out.println("Expected: true " + todd.contains(2));
		System.out.println("Expected:  " + todd.get(3));
		System.out.println("Expected: true " + todd.contains(3));
		System.out.println("Expected: W E E Z E R " + todd.get(4));
		System.out.println("Expected: true " + todd.contains(4));

		System.out.println("Expected: Duplicate key, don't do that.");
		todd.put(1, "Uh oh");
		System.out.println("Expected: Hello there " + todd.get(1));
		System.out.println("Expected: 4 " + todd.size());
		System.out.print("Expected: Nonexistent key, you doofus. ");
		todd.delete(25);
		System.out.println("Expected: 4 " + todd.size());
	}
}
