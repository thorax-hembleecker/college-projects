import java.util.Iterator;

public class Tree<Key extends Comparable<Key>, Value> extends UR_BST<Key, Value>
{
	private UR_Node root; // root of BST

	private class UR_Node
	{
		private Key key; // sorted by key
		private Value val; // associated data
		private UR_Node left, right; // left and right subtrees
		private int size; // number of nodes in subtree

		private int depth; // Added these for convenience; hope that's okay.
		private int height;
	}

	public Tree()
	{
		root = new UR_Node();
		root.depth = -1;
		root.height = -1;
	}

	public Tree(Key key, Value val)
	{
		root = new UR_Node();
		root.key = key;
		root.val = val;
		root.size = 0;
		root.depth = 0;
		root.height = 0;
	}

	public boolean isEmpty()
	{
		return size() == 0;		// Pretty self-explanatory.
	}

	public int size()  
	{
		if (root.key == null)	// Probably also self-explanatory.
			return 0;
		return root.size + 1;
	}

	public boolean contains(Key key)
	{
		UR_Node current = root;		// It basically looks for the thing and then gives you the OK if it finds it.
		while (current.key != null)
		{
			if (current.key.equals(key))
				return true;
			else if (current.key.compareTo(key) > 0)
				current = current.right;
			else
				current = current.left;
		}
		return false;
	}

	public Value get(Key key)
	{
		UR_Node current = root;		// Returns the value associated with the key input.
		while (current != null && current.key != null)
		{
			if (current.key.equals(key))
				return current.val;
			else if (current.key.compareTo(key) > 0)
				current = current.left;
			else
				current = current.right;
		}
		return null;
	}

	public void put(Key key, Value val)
	{
		UR_Node current = root;		// Finds a suitable spot in the tree, creates a new node for it, returns nothing. 
		UR_Node parent = root;
		if (root.key == null)
		{
			root = new UR_Node();
			root.key = key;
			root.val = val;
			root.size = 0;
			return;
		}
		while (current != null)
		{
			parent = current;
			if (current.key.compareTo(key) > 0)
				current = current.left;
			else
				current = current.right;
			if (parent != null)
				parent.size++;
		}
		current = new UR_Node();
		current.key = key;
		current.val = val;
		if (parent != null && parent.key != null)
		{
			current.depth = parent.depth - 1;
			if (parent.key.compareTo(key) > 0)
				parent.left = current;
			else
				parent.right = current;
			if (parent.depth == 0)
			{
				while (parent(parent) != null)
				{
					parent.depth++;
					parent = parent(parent);
				}
			}
		}
		updateDepth(root);
	}

	public void deleteMin()
	{
		UR_Node current = root;		// Deletes the node with the minimum key.
		while (current.left != null)
			current = current.left;
		delete(current.key);
	}

	public void deleteMax()
	{
		UR_Node current = root;		// Deletes the node with the maximum key.
		while (current.right != null)
			current = current.right;
		delete(current.key);
	}

	public void delete(Key key)
	{
		if (get(key) == null)		// Finds the node associated with the key, casts it into oblivion.
			return;					// (also replaces it with its successor)
		UR_Node current = root;		// (too bad I made the successor method at the last minute and didn't use it here)
		while (!current.key.equals(key))
		{
			if (current.key.compareTo(key) > 0)
				current = current.left;
			else
				current = current.right;
		}
		UR_Node newCurrent = current.right;
		if (current.right != null)
		{
			while (newCurrent.left != null)
				newCurrent = newCurrent.left;
		}
		else if (current.left != null)
		{
			newCurrent = current.left;
			while (newCurrent.right != null)
				newCurrent = newCurrent.right;
		}
		else if (parent(current) != null)
		{
			if (parent(current).key.compareTo(current.key) > 0)
				parent(current).left = null;
			else
				parent(current).right = null;
			return;
		}
		else
		{
			root.key = null;
			root.val = null;
			return;
		}
		if (current.right == null || (current.right != null && !current.right.equals(newCurrent)))
			newCurrent.right = current.right;
		if (current.left == null || (current.left != null && !current.left.equals(newCurrent)))
			newCurrent.left = current.left;
		if (parent(current) != null)
		{
			if (parent(current).key.compareTo(key) > 0)
				parent(current).left = newCurrent;
			else if (parent(current).key.compareTo(key) < 0)
				parent(current).right = newCurrent;
		}
		if (key.equals(root.key))
		{
			root = newCurrent;
		}
		updateDepth(root);
	}

	public Iterable<Key> keys()
	{
		Iterable<Key> grant = new Iterable<Key>()		// Well, it's *supposed* to iterate through all the keys in order.
		{
			public Iterator<Key> iterator()
			{
				Iterator<Key> todd = new Iterator<Key>()
				{
					UR_Node current = root;
					int n = 0;
					public Key next()
					{
						n++;
						if (current.equals(root))
						{
							current = successor(current);
							return root.key;
						}
						UR_Node ted = successor(current);
						current = ted;
						return ted.key;
					}
					public boolean hasNext()
					{
						return successor(current) != null;
					}
				};
				return todd;
			};
		};
		return grant;
	}

	public int height()
	{
		if (root.key == null)
			return -1;
		return root.height;
	}

	public Iterable<Key> levelOrder()
	{
		Iterable<Key> grant = new Iterable<Key>()	// This one makes me sad.
		{
			public Iterator<Key> iterator()
			{
				Iterator<Key> todd = new Iterator<Key>()
				{
					int n = 0;
					int level = 0;
					int i=0;
					public Key next()
					{
						Key jeff = findNode(root, level, i, 0);
						n++;
						i++;
						if (i > level*2 - 1)
						{
							i = 0;
							level++;
						}
						return jeff;
					}
					public boolean hasNext()
					{
						return n < size();
					}
				};
				return todd;
			};
		};
		return grant;
	}

	private UR_Node parent(UR_Node child)
	{
		if (child == null || root == null || child.equals(root))	// Finds and returns the input node's parent.
			return null;
		UR_Node current = root;
		while (current != null && current.key != null)
		{
			if (current.right != null && current.right.key.equals(child.key) || current.left != null && current.left.key.equals(child.key))
				return current;
			else if (current.key.compareTo(child.key) > 0)
				current = current.left;
			else
				current = current.right;
		}
		return null;
	}

	private void updateDepth(UR_Node current)
	{
		if (!current.equals(root))		// Updates the depth of every node, and sneakily the height of the tree as well.
			current.depth = parent(current).depth + 1;
		if (current.depth > root.height)
			root.height = current.depth;
		if (current.right != null)
			updateDepth(current.right);
		if (current.left != null)
			updateDepth(current.left);
		if (current.right == null && current.left == null)
			return;
	}

	private Key findNode(UR_Node current, int level, int index, int i)
	{
		Key thisishorriblepracticebutitsgonnahavetodo = root.key;		// I'm so sorry about this method. Really.
		if (current.depth == level)
		{
			if (i < index)
				i++;
			else if (i == index)
				root.key = current.key;
		}
		if (current.right != null && current.depth <= level && i < index)
			findNode(current.right, level, index, i);
		if (current.left != null && current.depth <= level && i < index)
			findNode(current.left, level, index, i);
		Key grant = root.key;
		root.key = thisishorriblepracticebutitsgonnahavetodo;
		return grant;
	}

	private UR_Node successor(UR_Node current)
	{
		UR_Node newCurrent = current.right;		// You know, I put this one together about midnight, so all things 
		if (current.right != null)				// considered I think it turned out quite nicely.
		{
			while (newCurrent.left != null)
				newCurrent = newCurrent.left;
			return newCurrent;
		}
		else if (parent(current) != null)
		{
			newCurrent = parent(current);
			while (parent(newCurrent) != null && parent(newCurrent).left != newCurrent)
				newCurrent = parent(newCurrent);
			if (parent(newCurrent) != null)
				return parent(newCurrent);
		}
		return null;
	}
}
