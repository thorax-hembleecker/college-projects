class URNode<E> // Doubly linked list node
{
	private E e; // Value for this node
	private URNode<E> n; // Reference to next node in list
	private URNode<E> p; // Reference to previous node
	// Constructors
	URNode(E it, URNode<E> inp, URNode<E> inn)
	{
		e = it;
		p = inp;
		n = inn;
	}
	URNode(URNode<E> inp, URNode<E> inn) // Get and set methods for the data members
	{
		p = inp;
		n = inn;
	}

	public E element() // Return the value
	{
		return e;
	}
	public E setElement(E it) // Set element value
	{
		return e = it;
	}
	public URNode<E> next() // Return next link
	{
		return n;
	}
	public URNode<E> setNext(URNode<E> nextval) // Set next link
	{
		return n = nextval;
	}
	public URNode<E> prev() // Return prev link
	{
		return p;
	}
	public URNode<E> setPrev(URNode<E> prevval) // Set prev link
	{
		return p = prevval;
	}
}