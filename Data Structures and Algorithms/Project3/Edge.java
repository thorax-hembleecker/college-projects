public class Edge
{
	private String id;
	private Node vert1;
	private Node vert2;
	private double weight;
	
	public Edge(String i, Node v1, Node v2)
	{
		id = i;
		vert1 = v1;
		vert2 = v2;	
		weight = 2*3956.969*Math.asin(Math.sqrt(Math.pow(Math.sin((v2.getLat()*Math.PI/180 - v1.getLat()*Math.PI/180)/2), 2) + Math.cos(v1.getLat()*Math.PI/180)*Math.cos(v2.getLat()*Math.PI/180)*Math.pow(Math.sin((v2.getLong()*Math.PI/180 - v1.getLong()*Math.PI/180)/2), 2)));
	}
	
	public String getID()
	{
		return id;
	}
	public double getWeight()
	{
		return weight;
	}
	public Node getVert1()
	{
		return vert1;
	}
	public Node getVert2()
	{
		return vert2;
	}
}
