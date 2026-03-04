import java.lang.reflect.Array;
import java.util.Iterator;

public class HashTable<Key extends Comparable<Key>,Value> extends UR_HashTable<Key,Value>
{
	private static final int INIT_CAPACITY = 4;
	private int n;
	private int m = 17;
	private Key[] keys;
	Value[] vals;
	int inserts, collisions;

	public HashTable()
	{
		n = 0;
		m = INIT_CAPACITY;
		vals = (Value[])Array.newInstance(Comparable.class, m);
		keys = (Key[])Array.newInstance(Comparable.class, m);
	}
	public HashTable(int cap)
	{
		n = 0;
		m = cap;
		vals = (Value[])Array.newInstance(Comparable.class, m);
		keys = (Key[])Array.newInstance(Comparable.class, m);
	}

	public void put(Key key, Value val)		// Adds a value to the table.
	{
		if (search(key) == -1)
		{
			if ((double)n/m > 0.75)
				resize(m*2);
			int grant = hash(key);
			vals[grant] = val;
			keys[grant] = key;
			n++;
		}
		else
		{
			System.out.println("Duplicate key, don't do that.");
			return;
		}
	}

	public Value get(Key key)		// Returns the value associated with the key.
	{
		if (search(key) == -1)
			return null;
		return vals[search(key)];
	}

	public void delete(Key key)		// Deletes a value from the table.
	{
		if (search(key) == -1)
		{
			System.out.println("Nonexistent key, you doofus.");
			return;
		}
		vals[search(key)] = (Value)"Empty after removal.";
		keys[search(key)] = null;
		n--;
	}

	public int size()		// Returns the number of elements in the table.
	{
		return n;
	}

	public boolean isEmpty()		// Returns a boolean indicating whether the table has 0 elements.
	{
		return n < 1;
	}

	public boolean contains(Key key)		// Returns a boolean indicating whether a value exists for the key in the table.
	{
		return search(key) != -1;
	}

	public Iterable<Key> keys()		// Creates a nice fun iterator.
	{
		Iterable<Key> grant = new Iterable<Key>()
		{
			public Iterator<Key> iterator()
			{
				Iterator<Key> todd = new Iterator<Key>()
				{
					int i = 0;
					int j = -1;
					public Key next()
					{
						j++;
						while (j<keys.length)
						{
							if (keys[j] != null)
							{
								i++;
								return keys[j];
							}
							j++;
						}
						System.out.println("You shouldn't get here, but if you do, you're not using hasNext() properly.");
						return null;
					}
					public boolean hasNext()
					{
						return i < n;
					}
				};
				return todd;
			};
		};
		return grant;
	}

	private int hash(Key key)		// Returns an empty location in the table where a value should go.
	{
		if (vals[key.hashCode() % m] == null || vals[key.hashCode() % m] == (Value)"Empty after removal.")
			return key.hashCode() % m;
		else
		{
			for (int i=0; i<m; i++)
			{
				if (vals[(key.hashCode() + i) % m] == null  || vals[(key.hashCode() + i) % m] == (Value)"Empty after removal.")
					return (key.hashCode() + i) % m;
			}
		}
		System.out.println("You should never see this error, but if you do, you messed up because there ain't any space.");
		return -1;
	}

	private void resize(int capacity)		// Increases the size of the table to accomodate more values.
	{
		m = capacity;
		Value[] grant = vals.clone();
		Key[] grond = keys.clone();
		vals = (Value[])Array.newInstance(Comparable.class, m);
		keys = (Key[])Array.newInstance(Comparable.class, m);
		for (int i=0; i<grant.length; i++)
		{
			if (grant[i] != null && grond[i] != null)
			{
				int gretel = hash(grond[i]);
				vals[gretel] = grant[i];
				keys[gretel] = grond[i];
			}
			else if ((grant[i] == null || grant[i] == (Value)"Empty after removal.") == !(grond[i] == null))
				System.out.println("WHY IS ONE OF THE VALUES NULL AND THE OTHER ISN'T");
		}
	}

	private int search(Key key)		// Returns the table index of the value associated with the key.
	{
		if (vals[key.hashCode() % m] == null)
			return -1;
		else
		{
			if (keys[key.hashCode() % m] == key)
				return key.hashCode() % m;
			for (int i=0; i<m; i++)
			{
				if (vals[(key.hashCode() + i) % m] == null)
					return -1;
				else if (keys[(key.hashCode() + i) % m] == key)
					return (key.hashCode() + i) % m;
			}
		}
		return -1;
	}
}
