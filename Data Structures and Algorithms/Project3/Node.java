public class Node implements Comparable<Node>
{
	private String id;
	private double latitude;
	private double longitude;
	private double distance;
	private boolean visited;
	private Node predecessor;
	
	public Node(String i, double t, double g)
	{
		id = i;
		latitude = t;
		longitude = g;
		distance = 25000;
		visited = false;
		predecessor = null;
	}
	
	public String getID()
	{
		return id;
	}
	public double getLat()
	{
		return latitude;
	}
	public double getLong()
	{
		return longitude;
	}
	public void setDistance(double grant)
	{
		distance = grant;
	}
	public double getDistance()
	{
		return distance;
	}
	public void setPredecessor(Node grant)
	{
		predecessor = grant;
	}
	public Node getPredecessor()
	{
		return predecessor;
	}
	public boolean unvisited()
	{
		return !visited;
	}
	public void setVisited()
	{
		visited = true;
	}
	
	public int compareTo(Node other)
	{
		if (this.distance > other.getDistance())
			return 1;
		else if (this.distance < other.getDistance())
			return -1;
		return 0;
	}
}
