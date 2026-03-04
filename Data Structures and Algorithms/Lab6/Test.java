import java.util.Iterator;

public class Test
{
	public static void main(String[] args)
	{
		Tree grant = new Tree();
		System.out.println("Expected: 0 " + grant.size());
		System.out.println("Expected: true " + grant.isEmpty());
		grant.put(1, "ZJORNT");
		System.out.println("Expected: 1 " + grant.size());
		grant.put(2, "ZUBLEROO");
		System.out.println("Expected: 2 " + grant.size());
		grant.put(3, "ZIMMERMAN");
		System.out.println("Expected: 3 " + grant.size());
		grant.put(4, "ZIMMERMAN");
		System.out.println("Expected: 4 " + grant.size());
		System.out.println("Expected: ZJORNT " + grant.get(1));
		System.out.println("Expected: ZUBLEROO " + grant.get(2));
		System.out.println("Expected: ZIMMERMAN " + grant.get(3));
		System.out.println("Expected: ZIMMERMAN " + grant.get(4));
		System.out.println("Expected: false " + grant.isEmpty());
		System.out.println("Expected: null " + grant.get(5));
		grant.delete(1);
		System.out.println("Expected: null " + grant.get(1));
		grant.delete(2);
		System.out.println("Expected: null " + grant.get(2));
		grant.delete(3);
		System.out.println("Expected: null " + grant.get(3));
		grant.delete(4);
		System.out.println("Expected: null " + grant.get(4));
		System.out.println("Expected: 0 " + grant.size());
		System.out.println("Expected: true " + grant.isEmpty());
		grant.put(3, "JERONZO");
		System.out.println("Expected: JERONZO " + grant.get(3));
		System.out.println("Expected: 1 " + grant.size());
		System.out.println("Expected: false " + grant.isEmpty());
		grant.put(5, "JZERINGO");
		grant.put(24, "ULUBINGO");
		grant.put(1, "SHMINGISHMONGO");
		grant.put(13, "TOODLE-LOO");
		System.out.println("Expected: SHMINGISHMONGO " + grant.get(1));
		System.out.println("Expected: ULUBINGO " + grant.get(24));
		grant.deleteMax();
		grant.deleteMin();
		System.out.println("Expected: null " + grant.get(1));
		System.out.println("Expected: null " + grant.get(24));
		
		Tree marvin = new Tree(23, "Bibbity");
		System.out.println("Expected: 0 " + marvin.height());
		marvin.put(13, "Tibbity");
		System.out.println("Expected: 1 " + marvin.height());
		marvin.put(45, "Flibbity");
		System.out.println("Expected: 1 " + marvin.height());
		marvin.put(8, "Skibbity");
		System.out.println("Expected: 2 " + marvin.height());
		marvin.put(19, "Hibbity");
		marvin.put(32, "Jibbity");
		System.out.println("Expected: 6 " + marvin.size());
		System.out.println("Expected: null " + marvin.get(1));
		System.out.println("Expected: Bibbity " + marvin.get(23));
		System.out.println("Expected: Tibbity " + marvin.get(13));
		System.out.println("Expected: Flibbity " + marvin.get(45));
		System.out.println("Expected: Skibbity " + marvin.get(8));
		System.out.println("Expected: Hibbity " + marvin.get(19));
		System.out.println("Expected: Jibbity " + marvin.get(32));
		System.out.println("Expected: false " + marvin.isEmpty());
		System.out.println("Expected: 2 " + marvin.height());
		marvin.put(37, "Plibbity");
		System.out.println("Expected: 3 " + marvin.height());
		
		Iterator<Comparable> ted = grant.keys().iterator();			// This *almost* works.
		while (ted.hasNext())
		{
			System.out.print((grant.get((Comparable)ted.next())) + ", ");
		}
		
		System.out.println();
		
		Iterator<Comparable> dave = grant.levelOrder().iterator();	// This does not work, and I'm not totally sure why.
		while (dave.hasNext())
		{
			System.out.print((grant.get((Comparable)dave.next())) + ", ");
		}
		
		marvin.deleteMax();	// This is the point where the recursion becomes too much and Test.java has a mental breakdown.
		System.out.println("Expected: null " + marvin.get(45));
		marvin.deleteMin();
		System.out.println("Expected: null " + marvin.get(8));
	}
}
