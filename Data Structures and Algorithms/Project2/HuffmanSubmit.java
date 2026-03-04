
// Import any package as required
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.PriorityQueue;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class HuffmanSubmit implements Huffman {
	
	// Feel free to add more methods and variables as required.

	public static class Node implements Comparable<Node>		// It's a node! Huzzah!
	{
		private int val;
		private Integer letter = null;
		private Node left, right, parent;

		public Node()
		{

		}
		public Node(int v)
		{
			val = v;
		}
		public Node(int v, Node l, Node r)
		{
			val = v;
			left = l;
			right = r;
		}
		public Node(int v, int i)
		{
			val = v;
			letter = i;
		}
		public int getVal()
		{
			return val;
		}
		public void setVal(int grant)
		{
			val = grant;
		}
		public Integer getLetter()
		{
			return letter;
		}
		public Node getRight()
		{
			return right;
		}
		public Node getLeft()
		{
			return left;
		}
		public Node getParent()
		{
			return parent;
		}
		public void setRight(Node n)
		{
			right = n;
		}
		public void setLeft(Node n)
		{
			left = n;
		}
		public void setParent(Node n)
		{
			parent = n;
		}

		public int compareTo(Node other)
		{
			return val - other.getVal();
		}
	}

	public void encode(String inputFile, String outputFile, String freqFile)
	{
		BinaryIn input = new BinaryIn(inputFile);

		Hashtable<Integer, Integer> freq = new Hashtable();

		int chaar = (int)input.readByte();
		while (!input.isEmpty())		// Creates frequency table.
		{
			if (freq.containsKey(chaar))
				freq.put(chaar, freq.get(chaar)+1);
			else
				freq.put(chaar, 1);
			chaar = (int)input.readByte();
		}

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(freqFile, false));
			int i = 0;
			while (i < 256)	// Writes to freqFile. I did a weird thing where the line number is the char; hope that's alright.
			{
				out.write(freq.get(i) + "");
				out.newLine();
				i++;
			}
			out.close();
		}
		catch (IOException todd)
		{
			System.out.println("File cannot be written.");
			return;
		}

		PriorityQueue<Node> queue = new PriorityQueue<Node>();

		Enumeration<Integer> grant = freq.keys();
		while (grant.hasMoreElements())		// Runs through the frequency table and puts nodes in the queue.
		{
			int ted = grant.nextElement();
			queue.add(new Node(freq.get(ted), ted));
		}

		while (queue.size() > 1)		// Creates a fun little tree.
		{
			Node left = queue.poll();
			Node right = queue.poll();
			Node parent = new Node(right.getVal() + left.getVal(), left, right);
			right.setParent(parent);
			left.setParent(parent);
			queue.add(parent);
		}
		Node root = queue.poll();

		Hashtable<Comparable, Comparable> codes = new Hashtable();
		encodeTree(root, "", codes);		// Puts the codes in the hash table.

		BinaryIn in = new BinaryIn(inputFile);
		BinaryOut out = new BinaryOut(outputFile);
		int character = in.readByte();
		while (!in.isEmpty())		// Writes encoded text/images if images worked to the output file.
		{
			String code = codes.get(character) + "";
			for (int i=0; i<code.length(); i++)
			{
				if (code.charAt(i) == '0')
					out.write(false);
				else if (code.charAt(i) == '1')
					out.write(true);
				else
					System.out.println("WHAT HAVE YOU DONE, YOU FIEND");

			}
			character = in.readByte();
		}
		out.close();
	}


	public void decode(String inputFile, String outputFile, String freqFile)
	{
		Hashtable<Integer, Integer> freq = new Hashtable();

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(freqFile));

			String frequency = in.readLine();
			int i = 0;

			while (i < 256)
			{
				if (getInt(frequency) != null)
				{
					freq.put(i, getInt(frequency));
				}
				frequency = in.readLine();
				i++;
			}
			in.close();
		}
		catch (IOException todd)
		{
			System.out.println("File cannot be read.");
			return;
		}

		PriorityQueue<Node> queue = new PriorityQueue<Node>();

		Enumeration<Integer> grant = freq.keys();
		while (grant.hasMoreElements())
		{
			int ted = grant.nextElement();
			queue.add(new Node(freq.get(ted), ted));
		}

		while (queue.size() > 1)
		{
			Node left = queue.poll();
			Node right = queue.poll();
			Node parent = new Node(right.getVal() + left.getVal(), left, right);
			right.setParent(parent);
			left.setParent(parent);
			queue.add(parent);
		}
		Node root = queue.poll();

		Hashtable<Comparable, Comparable> codes = new Hashtable();
		encodeTree(root, "", codes);

		Node current = root;
		
		// Up to here is just recreating the tree structure from the frequency file and all that.

		BinaryIn in = new BinaryIn(inputFile);		// Write the decoded file! Huzzah!
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFile, false));
			while (!in.isEmpty())
			{
				if (current.getLetter() != null)
				{
					out.write(current.getLetter());
					current = root;
				}
				if (in.readBoolean() == false)
					current = current.getLeft();
				else
					current = current.getRight();
			}
			out.close();
		}
		catch (IOException e)
		{
			System.out.println("File cannot be written.");
			return;
		}
	}

	public void encodeTree(Node n, String s, Hashtable<Comparable, Comparable> codes)	// Nice little recursive tree encoder.
	{
		if (n.getLetter() != null)
		{
			codes.put(n.getLetter(), s);
			return;
		}
		encodeTree(n.getLeft(), s + "0", codes);
		encodeTree(n.getRight(), s + "1", codes);
	}

	public static Integer getInt(String input)		// Just a lovely little helper function.
	{
		try
		{
			int grant = Integer.parseInt(input);
			return grant;
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}


	public static void main(String[] args) {
		Huffman huffman = new HuffmanSubmit();
		huffman.encode("alice30.txt", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.txt", "freq.txt");

		huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
	}

}