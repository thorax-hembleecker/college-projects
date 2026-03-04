
abstract public class UR_HashTable<Key,Value>
{
	private static final int INIT_CAPACITY = 4;
	private int n;
	private int m = 17;
	private Key[] keys;
	Value[] vals;
	int inserts, collisions;
	// Constructors
	// public UR_HashTable() {}
	// public UR_HashTable(int cap) { }
	abstract public void put (Key key, Value val) ;
	abstract public Value get (Key key) ;
	abstract public void delete(Key key) ;
	abstract public int size() ;
	abstract public boolean isEmpty() ;
	abstract public boolean contains(Key key);
	abstract public Iterable<Key> keys() ;
	// Useful helpers
	// private int hash(Key key) ;
	// private void resize(int capacity) ;	
}
