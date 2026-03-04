import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;

public class StreetMap extends JFrame
{
	private static Graph input = new Graph();
	private static Graph dijkstra = new Graph();
	private static JFrame window = new JFrame();
	private static JPanel windowsXP = new JPanel();
	private static double minLat = 91;
	private static double maxLat = -91;
	private static double minLong = 181;
	private static double maxLong = -181;

	public static void main(String[] args)
	{			
		Graph map = new Graph();

		if (1 < args.length && args[0].equals("java") && args[1].equals("StreetMap"))
		{
			String fileName = "";
			for (int i=0; i < args[2].length(); i++)
				fileName = fileName + args[2].charAt(i);

			try
			{
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				String input = in.readLine();

				while (input != null)
				{
					String id = "";
					int here = 0;
					for (int i=2; input.charAt(i) != '	'; i++)
					{
						id = id + input.charAt(i);
						here = i+2;
					}
					if (input.charAt(0) == 'i')
					{
						String t = "";
						String g = "";
						for (int i=here; input.charAt(i) != '	'; i++)
						{
							t = t + input.charAt(i);
							here = i+2;
						}
						for (int i=here; i<input.length(); i++)
						{
							g = g + input.charAt(i);
						}

						double latitude = 0;
						try
						{
							latitude = Double.parseDouble(t);
						}
						catch (NumberFormatException e)
						{

						}

						double longitude = 0;
						try
						{
							longitude  = Double.parseDouble(g);
						}
						catch (NumberFormatException e)
						{

						}
						map.addVertex(new Node(id, latitude, longitude));
						if (latitude < minLat)
							minLat = latitude;
						if (latitude > maxLat)
							maxLat = latitude;
						if (longitude < minLong)
							minLong = longitude;
						if (longitude > maxLong)
							maxLong = longitude;
					}
					else if (input.charAt(0) == 'r')
					{
						String v1 = "";
						String v2 = "";
						for (int i=here; input.charAt(i) != '	'; i++)
						{
							v1 = v1 + input.charAt(i);
							here = i+2;
						}
						for (int i=here; i<input.length(); i++)
						{
							v2 = v2 + input.charAt(i);
						}
						map.addEdge(new Edge(id, map.getVertex(v1), map.getVertex(v2)));
					}
					input = in.readLine();
				}

				in.close();
			}
			catch (IOException aaaaaaaaaaaaaaaaaawhathaveyoudone)
			{
				System.out.println("File cannot be read.");
				return;
			}

			input = map;
			int next = 3;
			boolean show = false;

			if (next < args.length && args[next].equals("--show"))
			{
				show = true;
				next++;
			}
			if (next < args.length)
			{
				if (args[next].equals("--directions"))
				{
					if (next + 2 < args.length && map.allNodes().containsKey(args[next+1]) && map.allNodes().containsKey(args[next+2]))
						dijkstra = map.dijkstra(map.getVertex(args[next+1]), map.getVertex(args[next+2]));
					else if (map.allNodes().containsKey(args[next+2]))
						System.out.println("Invalid start location.");
					else if (map.allNodes().containsKey(args[next+1]))
						System.out.println("Invalid end location.");
					else
						System.out.println("Invalid directions.");
					next += 3;
					if (next < args.length && args[next].equals("--show"))
						show = true;
				}
				else
					System.out.println("My good fellow, what kind of input is this?");
			}
			if (show == true)
			{
				window.add(windowsXP);
				window.setSize((int)(700*(maxLong - minLong)/(maxLat - minLat)), 700);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setLocationRelativeTo(null);
				window.getContentPane().add(new Paint());
				window.setVisible(true);
			}
		}
		else
			System.out.println("Invalid input.");
	}

	static class Paint extends JPanel
	{
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			paintMap(input, g);
		}
		public void paintMap(Graph grant, Graphics g)
		{
			Iterator<Node> map = grant.allNodes().values().iterator();
			while (map.hasNext())
			{
				Node current = map.next();
				Iterator<Edge> edges = grant.connectedEdges(current).iterator();
				{
					while (edges.hasNext())
					{
						Edge brad = edges.next();
						if (dijkstra.getEdge(brad.getID()) != null)
							g.setColor(Color.RED);
						else
							g.setColor(Color.BLACK);
						g.drawLine(20 + (int)((brad.getVert2().getLong() - minLong)*(window.getWidth() - 50)/(maxLong - minLong)), window.getHeight() - 50 - (int)((brad.getVert2().getLat() - minLat)*(window.getHeight() - 75)/(maxLat - minLat)), 20 + (int)((brad.getVert1().getLong() - minLong)*(window.getWidth() - 50)/(maxLong - minLong)), window.getHeight() - 50 - (int)((brad.getVert1().getLat() - minLat)*(window.getHeight() - 75)/(maxLat - minLat)));
					}
				}
			}
		}
	}
}
