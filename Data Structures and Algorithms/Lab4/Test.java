import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public class Test
{
	public static void main(String[] args)
	{
		URLinkedList<Integer> dave = new URLinkedList<Integer>();
		
		System.out.println("isEmpty(): true " + dave.isEmpty());
		System.out.println("peekFirst(): null " + dave.peekFirst());
		System.out.println("peekLast(): null " + dave.peekLast());
		System.out.println("get(i): null " + dave.get(31));
		System.out.println("size(): 0 " + dave.size());
		System.out.println("pollFirst(): null " + dave.pollFirst());
		System.out.println("pollLast(): null " + dave.pollLast());
		System.out.println("equals(L): true " + dave.equals(dave));
		
		dave.add(3);
		
		System.out.println("remove(i): 3 " + dave.remove(0));
		
		System.out.println("isEmpty(): true " + dave.isEmpty());
		
		dave.add(3);
		dave.add(5);
		
		System.out.println("add(n): true " + dave.add(11));
		System.out.println("isEmpty(): false " + dave.isEmpty());
		System.out.println("contains(n): true " + dave.contains(3));
		System.out.println("equals(L): true " + dave.equals(dave));
		
		System.out.println("peekFirst(): 3 " + dave.peekFirst());
		System.out.println("peekLast(): 11 " + dave.peekLast());
		
		dave.addFirst(23);
		dave.addLast(19);
		
		System.out.println("peekFirst(): 23 " + dave.peekFirst());
		System.out.println("peekLast(): 19 " + dave.peekLast());
		System.out.println("size(): 5 " + dave.size());
		
		Integer n = 11;
		
		System.out.println("remove(n): true " + dave.remove(n));
		System.out.println("size(): 4 " + dave.size());
		System.out.println("equals(L): true " + dave.equals(dave));
		
		URLinkedList<Comparable> aloysius = new URLinkedList<Comparable>();
		aloysius.add(41);
		
		System.out.println("equals(L): false " + dave.equals(aloysius));
		
		n = 41;
		
		System.out.println("remove(n): true " + aloysius.remove(n));
		
		System.out.println("contains(n): false " + aloysius.contains(41));
		System.out.println("equals(L): false " + dave.equals(aloysius));
		
		aloysius.add(12);
		
		System.out.println("pollFirst(): 12 " + aloysius.pollFirst());
		
		aloysius.add(15);
		
		System.out.println("pollLast(): 15 " + aloysius.pollLast());
		
		aloysius.add(1,39);
		
		System.out.println("get(i): null " + aloysius.get(0));
		
		aloysius.add(0,39);
		
		System.out.println("get(i): 39 " + aloysius.get(0));
		System.out.println("peekFirst(): 39 " + aloysius.peekFirst());
		System.out.println("contains(n): true " + aloysius.contains(39));
		
		ArrayList<Comparable> grant = new ArrayList<Comparable>();
		for (int i=0; i<dave.size(); i++)
			grant.add(dave.get(i));
		
		System.out.println("peekLast(): 39 " + aloysius.peekLast());
		System.out.println("contains(n): true " + aloysius.contains(39));
		System.out.println("addAll(c): true " + aloysius.addAll(grant));
		
		System.out.println("containsAll(c): true " + aloysius.containsAll(grant));
		System.out.println("removeAll(c): true " + aloysius.removeAll(grant));
		System.out.println("containsAll(c): false " + aloysius.containsAll(grant));
		System.out.println("addAll(c): true " + aloysius.addAll(grant));
		
		System.out.println("contains(n): true " + aloysius.contains(39));
		System.out.println("equals(L): false " + dave.equals(aloysius));
		System.out.println("equals(L): false " + aloysius.equals(dave));
		System.out.println("peekFirst(): 39 " + aloysius.peekFirst());
		System.out.println("peekLast(): 19 " + aloysius.peekLast());
		System.out.println("contains(n): true " + aloysius.contains(39));
		
		System.out.println("remove(i): 39 " + aloysius.remove(0));
		
		System.out.println("equals(L): true " + dave.equals(aloysius));
		
		dave.clear();

		System.out.println("peekFirst(): null " + dave.peekFirst());
		System.out.println("peekLast(): null " + dave.peekLast());
		System.out.println("isEmpty(): true " + dave.isEmpty());
		System.out.println("equals(L): false " + dave.equals(aloysius));
		System.out.println("equals(L): false " + aloysius.equals(dave));
		System.out.println("contains(n): false " + aloysius.contains(39));
		System.out.println("contains(n): false " + dave.contains(39));
		
		grant = new ArrayList<Comparable>();
		for (int i=0; i<aloysius.size(); i++)
			grant.add(aloysius.get(i));
		
		System.out.println("containsAll(c): false " + dave.containsAll(grant));
		
		grant = new ArrayList<Comparable>();
		for (int i=0; i<dave.size(); i++)
			grant.add(dave.get(i));
		
		System.out.println("containsAll(c): true " + aloysius.containsAll(grant));
		
		n = 32;
		
		dave.add(32);
		dave.add(54);
		dave.add(19);
		
		System.out.println("peekFirst(): 32 " + dave.peekFirst());
		System.out.println("peekLast(): 19 " + dave.peekLast());
		System.out.println("indexOf(n): 0 " + dave.indexOf(32));
		System.out.println("indexOf(n): 0 " + dave.indexOf(n));
		System.out.println("get(i): 19 " + dave.get(2));
		
		dave.addFirst(4);
		
		System.out.println("get(i): 54 " + dave.get(2));
		System.out.println("indexOf(n): 1 " + dave.indexOf(32));
		System.out.println("isEmpty(): false " + dave.isEmpty());
		System.out.println("isEmpty(): false " + aloysius.isEmpty());
		
//		Iterator<Integer> john = dave.iterator();
//		System.out.println("Dave: ");
//		while (john.hasNext())
//		{
//			System.out.print(john.next() + ", ");
//		}
		
		aloysius.add(54);
		
		grant = new ArrayList<Comparable>();
		for (int i=0; i<aloysius.size(); i++)
			grant.add(aloysius.get(i));
		
		System.out.println("removeAll(c): true " + dave.removeAll(grant));
		System.out.println("contains(n): false " + dave.contains(54));
		
		System.out.println("set(i, n): 46 " + aloysius.set(0, 46));
		System.out.println("set(i, n): 0 " + aloysius.set(3, 0));
		System.out.println("get(i): 0 " + aloysius.get(3));
		System.out.println("get(i): 3 " + aloysius.get(1));
		System.out.println("get(i): 46 " + aloysius.get(0));
		
		dave.clear();
		dave.add(4);
		dave.add(52);
		dave.add(15);
		dave.add(22);
		dave.add(81);
		dave.add(19);
		
		System.out.println("toArray(): 4, 52, 15, 22, 81, 19, ");
		for (int i=0; i<dave.size(); i++)
			System.out.print(dave.toArray()[i] + ", ");
		System.out.println();
		System.out.println("subList(i, i): 52, 15, 22, ");
		for (int i=0; i<dave.subList(1, 3).size(); i++)
			System.out.print(dave.subList(1, 3).toArray()[i] + ", ");
	}
}
