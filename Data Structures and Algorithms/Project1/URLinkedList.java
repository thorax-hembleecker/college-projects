import java.util.Collection;
import java.util.Iterator;

public class URLinkedList<E> implements URList<E>
{
	URNode<E> head;
	URNode<E> tail;

	public URLinkedList()
	{
		head = null;
		tail = null;
	}
	// Inserts the specified element at the beginning of this list.
	void addFirst(E e)
	{
		head = new URNode<E>(e, null, head); // Makes a new node as the head.
		if (head.next() != null)
			head.next().setPrev(head); // Sets the previous head's previous node to the new head.
		else
			tail = head;
	}

	// Appends the specified element to the end of this list.
	void addLast(E e)
	{
		tail = new URNode<E>(e, tail, null); // Makes a new node as the tail.
		if (tail.prev() != null)
			tail.prev().setNext(tail); // Sets the previous tail's next node as the new table.
		else
			head = tail;
	}

	// Retrieves, but does not remove, the first element of this list, or returns null if
	// this list is empty.
	E peekFirst()
	{
		if (head != null)
			return head.element();
		return null;
	}

	// Retrieves, but does not remove, the last element of this list, or returns null if
	// this list is empty.
	E peekLast()
	{
		if (tail != null)
			return tail.element();
		return null;
	}

	// Retrieves and removes the first element of this list, or returns null if this list
	// is empty.
	E pollFirst()
	{
		if (isEmpty())
			return null;
		E grant = head.element();
		remove(0);
		return grant;
	}

	// Retrieves and removes the last element of this list, or returns null if this list
	// is empty.

	E pollLast()
	{
		if (isEmpty())
			return null;
		E grant = tail.element();
		remove(size()-1);
		return grant;
	}



	// Appends the specified element to the end of this list
	public boolean add(E e)
	{
		addLast(e);
		return true;
	}

	// Inserts the specified element at the specified position in this list
	public void add(int index, E element)
	{
		URNode<E> current = head;
		if (current == null)
		{
			if (index == 0)
			{
				head = new URNode<E>(element, null, null);
				tail = head;
			}
			return;
		}
		for (int i=0; i<index-1; i++)
		{
			if (current.next() != null)
				current = current.next();
			else
				return;
		}
		if (current.next() != null)
			current.next().setPrev(new URNode<E>(element, current, current.next()));
		current.setNext(new URNode<E>(element, current, null));
		tail = current.next();
	}

	// Appends all of the elements in the specified collection to the end of this list,
	// in the order that they are returned by the specified collection's iterator
	public boolean addAll(Collection<? extends E> c)
	{
		for (E grant : c)
			add(grant);
		return true;
	}

	// Inserts all of the elements in the specified collection into this list
	// at the specified position
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if (get(index) == null)
			return false;
		for (E grant : c)
		{
			add(index, grant);
			index++;
		}
		return true;
	}

	// Removes all of the elements from this list
	public void clear()
	{
		for (int i=0; i<size(); i++)
			remove(0);
		head = null;
		tail = null;
	}

	// Returns true if this list contains the specified element.
	public boolean contains(Object o)
	{
		if (head == null)
			return false;
		URNode<E> current = head;
		for (int i=0; i<size(); i++)
		{
			if (current.element() == o)
				return true;
			if (current.next() != null)
				current = current.next();
		}
		return false;
	}

	// Returns true if this list contains all of the elements of the specified collection
	public boolean containsAll(Collection<?> c)
	{
		boolean grant = true;
		for (Object todd : c)
		{
			if (!contains(todd))
				grant = false;
		}
		return grant;
	}

	// Compares the specified object with this list for equality.
	// Returns true if both contain the same elements. Ignore capacity
	public boolean equals(Object o)
	{
		if (o instanceof URLinkedList)
		{
			for (int i=0; i<size(); i++)
			{
				if (!((URLinkedList<E>)o).contains(get(i)))
					return false;
			}
			for (int i=0; i<((URLinkedList<E>)o).size(); i++)
			{
				if (!contains(((URLinkedList<E>)o).get(i)))
					return false;
			}
			return true;
		}
		return false;
	}

	// Returns the element at the specified position in this list.
	public E get(int index)
	{
		URNode<E> current = head;
		if (current == null)
			return null;
		for (int i=0; i<index; i++)
		{
			if (current.next() != null)
				current = current.next();
			else
				return null;
		}
		return current.element();
	}

	// Returns the index of the first occurrence of the specified element in this list,
	// or -1 if this list does not contain the element.
	public int indexOf(Object o)
	{
		URNode<E> current = head;
		for (int i=0; current.next() != null; i++)
		{
			if (current.element() == o)
				return i;
			current = current.next();
		}
		return -1;
	}

	// Returns true if this list contains no elements.
	public boolean isEmpty()
	{
		if (head == null)
			return true;
		return false;
	}

	// Returns an iterator over the elements in this list in proper sequence.
	public Iterator<E> iterator()
	{
		Iterator<E> grant = new Iterator<E>()
		{
			URNode<E> current = head;
			private boolean notFirst = false;
			public boolean hasNext()
			{
				if (current.next() != null)
					return true;
				return false;
			}

			public E next()
			{
				if (!notFirst)
				{
					notFirst = true;
					return current.element();
				}
				current = current.next();
				return current.next().element();
			}
		};
		return grant;
	}

	// Removes the element at the specified position in this list
	public E remove(int index)
	{
		URNode<E> current = head;
		if (current == null)
			return null;
		for (int i=0; i<index; i++)
		{
			if (current.next() != null)
				current = current.next();
			else
				return null;
		}
		// Sets the adjacent nodes to connect to each other, effectively excising the current node from the list.
		if (current.next() != null)
			current.next().setPrev(current.prev());
		if (current.prev() != null)
			current.prev().setNext(current.next());
		if (current == head)
			head = current.next();
		if (current == tail)
			tail = current.prev();
		E data = current.element();
		current.setElement(null);
		current.setPrev(null);
		current.setNext(null);
		return data;
	}

	// Removes the first occurrence of the specified element from this list,
	// if it is present
	public boolean remove(Object o)
	{
		URNode<E> current = head;
		while (current.element() != o)
		{
			if (current.next() != null)
				current = current.next();
			else
				return false;
		}
		if (current.next() != null)
			current.next().setPrev(current.prev());
		if (current.prev() != null)
			current.prev().setNext(current.next());
		if (current == head)
			head = current.next();
		if (current == tail)
			tail = current.prev();
		current.setElement(null);
		current.setPrev(null);
		current.setNext(null);
		return true;
	}

	// Removes from this list all of its elements that are contained
	// in the specified collection
	public boolean removeAll(Collection<?> c)
	{
		boolean grant = false;
		for (URNode<E> current = head; current.next() != null; current = current.next())
		{
			if (c.contains(current.element()))
			{
				if (current.next() != null)
					current.next().setPrev(current.prev());
				if (current.prev() != null)
					current.prev().setNext(current.next());
				grant = true;
			}
		}
		return grant;
	}

	// Replaces the element at the specified position in this list
	// with the specified element
	public E set(int index, E element)
	{
		URNode<E> current = head;
		for (int i=0; i<index; i++)
		{
			if (current.next() != null)
				current = current.next();
			else
				return null;
		}
		if (current == null)
		{
			head = new URNode<E>(element, null, null);
			tail = head;
			current = head;
		}
		return current.setElement(element);
	}

	// Returns the number of elements in this list.
	public int size()
	{
		int grant = 0;
		if (head != null)
		{
			grant++;
			for (URNode<E> current = head; current.next() != null; current = current.next())
				grant++;
		}
		return grant;
	}

	// Returns a view of the portion of this list
	// between the specified fromIndex, inclusive, and toIndex, exclusive.
	public URList<E> subList(int fromIndex, int toIndex)
	{
		URNode<E> current = head;
		URLinkedList<E> grant = new URLinkedList<E>();
		for (int i=0; i<fromIndex; i++)
		{
			if (current.next() != null)
				current = current.next();
			else
				return null;
		}
		for (int i=fromIndex; i<=toIndex; i++)
		{
			grant.add(current.element());
			if (current.next() != null)
				current = current.next();
		}
		return grant;
	}

	// Returns an array containing all of the elements in this list
	// in proper sequence (from first to the last element).
	public Object[] toArray()
	{
		Object[] grant = new Object[size()];
		URNode<E> current = head;
		for (int i=0; i<size(); i++)
		{
			grant[i] = current.element();
			if (current.next() != null)
				current = current.next();
		}
		return grant;
	}
}
