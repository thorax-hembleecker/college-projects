import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.LinkedHashSet;

public class Graph
{
	Hashtable<String, Node> nodes = new Hashtable<String, Node>();
	Hashtable<String, Edge> edges = new Hashtable<String, Edge>();

	public Graph()
	{

	}

	public void addVertex(Node grant)
	{
		nodes.put(grant.getID(), grant);
	}
	public Node getVertex(String id)
	{
		return nodes.get(id);
	}

	public void addEdge(Edge grant)
	{
		edges.put(grant.getID(), grant);
	}
	public Edge getEdge(String id)
	{
		return edges.get(id);
	}
	public LinkedHashSet<Edge> connectedEdges(Node source)
	{
		LinkedHashSet<Edge> connected = new LinkedHashSet<Edge>();
		Iterator<Edge> grant = edges.values().iterator();
		while (grant.hasNext())
		{
			Edge david = grant.next();
			if (david.getVert1().equals(source) || david.getVert2().equals(source))
				connected.add(david);
		}
		return connected;	
	}
	public LinkedHashSet<Node> adj(Node source)
	{
		LinkedHashSet<Node> adjacent = new LinkedHashSet<Node>();
		Iterator<Edge> grant = connectedEdges(source).iterator();
		while (grant.hasNext())
		{
			Edge david = grant.next();
			if (david.getVert1().equals(source))
				adjacent.add(david.getVert2());
			else
				adjacent.add(david.getVert1());
		}
		return adjacent;
	}
	public Hashtable<String, Node> allNodes()
	{
		return nodes;
	}

	public Graph dijkstra(Node start, Node end)
	{
		PriorityQueue<Node> losNodes = new PriorityQueue<Node>();
		Graph path = new Graph();
		
		start.setDistance(0);
		losNodes.add(start);		

		while(!losNodes.peek().equals(end))
		{
			Node current = losNodes.poll();
			current.setVisited();

			if (current.getDistance() < 25000)
			{
				Iterator<Edge> adjacents = connectedEdges(current).iterator();
				while (adjacents.hasNext())
				{
					Edge connectyBoi = adjacents.next();
					double pathDistance = current.getDistance() + connectyBoi.getWeight();
					Node next;
					if (!connectyBoi.getVert1().equals(current))
						next = connectyBoi.getVert1();
					else
						next = connectyBoi.getVert2();
					if (next.unvisited())
					{
						losNodes.add(next);
						next.setVisited();
					}
					if (pathDistance < next.getDistance())
					{
						losNodes.remove(next);
						next.setDistance(pathDistance);
						next.setPredecessor(current);
						losNodes.add(next);
					}
				}
			}
		}
		
		if (end.getPredecessor() != null)
		{
			PriorityQueue<Node> inOrder = new PriorityQueue<Node>();
			for (Node jeff = end; jeff != null; jeff = jeff.getPredecessor())
			{
				path.addVertex(jeff);
				inOrder.add(jeff);
				Enumeration<Edge> dmitri = edges.elements();
				while(dmitri.hasMoreElements())
				{
					Edge david = dmitri.nextElement();
					if (david.getVert1().equals(jeff) && david.getVert2().equals(jeff.getPredecessor()) || david.getVert1().equals(jeff.getPredecessor()) && david.getVert2().equals(jeff))
						path.addEdge(david);
				}
			}
			while(!inOrder.isEmpty())
				System.out.println(inOrder.poll().getID());
			System.out.println("\nTotal distance: " + end.getDistance() + " miles.");
		}
		else
		{
			System.out.println("Something is afoot here.");
		}
		return path;
	}
}
