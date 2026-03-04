
import java.lang.reflect.Array;

public class Heap<T extends Comparable<T>> implements UR_Heap<T>
{
	T[] heap;
	int size;
	int capacity;

	public Heap()
	{
		capacity = 10;
		size = 0;
		heap = (T[])Array.newInstance(Comparable.class, capacity);
	}

	public Heap(int cap)
	{
		if (cap > 0)
			capacity = cap;
		else
			capacity = 1;		// Sets a default capacity of 1 if given an illegal negative input.
		size = 0;
		heap = (T[])Array.newInstance(Comparable.class, capacity);
	}

	public Heap (Comparable[] nonHeap)
	{
		capacity = nonHeap.length;
		size = 0;
		for (int i=0; i<nonHeap.length; i++)
		{
			if (nonHeap[i] != null)
				size++;
		}
		heap = (T[])nonHeap;
		heap = heapify((T[])nonHeap);
	}

	public void insert(T item)		// Inserts the item as a leaf node, then bubbles it up to the right place.
	{
		if (size == heap.length)
			resize();
		int index = 0;
		for (int i=heap.length-1; i>=0; i--)
		{
			if (heap[i] != null)
			{
				index = i+1;
				break;
			}
		}
		heap[index] = item;
		bubbleUp(item);
		size++;
	}

	public boolean isEmpty()		// Checks if the heap is empty.
	{
		return size == 0;
	}

	public int size()
	{
		return size;
	}

	public T deleteMin()		// Deletes the root node, which should always have the lowest key value, and returns it.
	{
		int index = heap.length-1;
		for (int i=heap.length-1; i>=0; i--)
		{
			if (heap[i] != null)
			{
				index = i;
				break;
			}
		}
		T grant = heap[0];
		heap[0] = heap[index];
		heap[index] = null;
		size--;
		if (index > 0)
			bubbleDown(heap[0]);
		return grant;
	}

	public void printHeap()		// Prints the heap array.
	{
		for (int i=0; i<heap.length; i++)
		{
			if (i+1 < heap.length && heap[i+1] != null)
				System.out.print(heap[i] + ", ");
			else if (heap[i] != null)
				System.out.print(heap[i]);
		}
		System.out.println();
	}

	private void resize()		// Doubles the size of the heap.
	{
		capacity *= 2;
		T[] grant = heap.clone();
		heap = (T[])Array.newInstance(Comparable.class, capacity);
		for (int i=0; i<grant.length; i++)
			heap[i] = grant[i];
	}

	private void bubbleUp(T grant)		// Swaps the node with its parent until the min-heap property is met.
	{
		if (grant == null)
			return;
		int index = 0;
		for (int i=heap.length-1; i>=0; i--)
		{
			if (heap[i] != null && heap[i].equals(grant))
			{
				index = i;
				break;
			}
		}
		while (index > 0 && grant.compareTo(heap[(index-1)/2]) < 0)
		{
			heap[index] = heap[(index-1)/2];
			heap[(index-1)/2] = grant;
			index = (index-1)/2;
		}
	}

	private void bubbleDown(T grant)		// Swaps the node with its least child until the min-heap property is met.
	{
		int index = 0;
		for (int i=0; i<heap.length; i++)
		{
			if (heap[i].equals(grant))
			{
				index = i;
				break;
			}
		}
		while (2*index+1 < size() && grant.compareTo(heap[2*index+1]) > 0 || 2*index+2 < size() && grant.compareTo(heap[2*index+2]) > 0)
		{
			T least;
			if (2*index+2 >= size() || heap[2*index+1].compareTo(heap[2*index+2]) < 0)
				least = heap[2*index+1];
			else
				least = heap[2*index+2];
			if (grant.compareTo(least) > 0)
			{
				if (least.equals(heap[2*index+1]))
				{
					heap[index] = heap[2*index+1];
					heap[2*index+1] = grant;
					index = 2*index+1;
				}
				else
				{
					heap[index] = heap[2*index+2];
					heap[2*index+2] = grant;
					index = 2*index+2;
				}
			}
		}
	}

	private T[] heapify(T[] grant)		// Turns an array into a heap by bubbling down all internal nodes, then returns the heap.
	{
		int index = grant.length-1;
		for (int i=grant.length-1; i>=0; i--)
		{
			if (2*i+1 < grant.length && grant[2*i+1] != null || 2*i+2 < grant.length && grant[2*i+2] != null)
			{
				index = i;
				break;
			}
		}
		for (int i=index; i>=0; i--)
		{
			bubbleDown(grant[i]);
		}
		return grant;
	}

}
