import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

//====CLASSES====\\

public class State implements Comparable<State>		// Oh yeah comment your code or something (don't forget to do this)
{
	private String[] state;
	private Board board;
	private UtilMovePair pair;
	private int util;
	private PriorityQueue<State> children;
	private int player;
	private String[] actions;
	private int winner;
	private boolean mainGame;

	public State()
	{
		mainGame = false;
		util = 0;
		children = new PriorityQueue<State>();
		winner = -1;
	}
	public State(String[] s, int p)
	{
		mainGame = false;
		state = s;
		board = new Board(s.length/2);
		util = 0;
		children = new PriorityQueue<State>();
		player = p;
		actions = new String[4*s.length];
		for (int i=0; i<actions.length; i++)
		{
			actions[i] = "";
		}
		winner = -1;
	}

	public class Board
	{
		private String[][] board;

		public Board(int size)
		{
			board = new String[size][size];
			for (int i=0; i<size; i++)
			{
				for (int j=0; j<size; j++)
				{
					if (i/2 == i/2.0)
					{
						if (j/2 == j/2.0)
							board[i][j] = "+ ";
						else
							board[i][j] = "= ";
					}
					else
					{
						if (j/2 == j/2.0)
							board[i][j] = "= ";
						else
							board[i][j] = "+ ";
					}
				}
			}
		}
		public String[][] getBoard()
		{
			return board;
		}
	}

	public class UtilMovePair
	{
		private int util;
		private String move;

		public UtilMovePair()
		{

		}

		public int getUtil()
		{
			return util;
		}
		public String getMove()
		{
			return move;
		}
		public void setUtil(int u)
		{
			util = u;
		}
		public void setMove(String m)
		{
			move = m;
		}
	}

	//====BASIC STATE FUNCTIONS====\\

	public int getUtil()
	{
		return util;
	}
	public void setUtil(int u)
	{
		util = u;
	}
	public PriorityQueue<State> getChildren()
	{
		return children;
	}
	public int getPlayer()
	{
		return player;
	}
	public int getWinner()
	{
		return winner;
	}
	public String[] getActions()
	{
		return actions;
	}
	public void setAction(int i, String a)
	{
		actions[i] = a;
	}
	public String[] getState()
	{
		return state;
	}
	public Board getBoard()
	{
		return board;
	}
	public boolean getMainGame()
	{
		return mainGame;
	}
	public void setMainGame(boolean mg)
	{
		mainGame = mg;
	}
	public int compareTo(State grant)
	{
		return grant.getUtil() - this.util;	// Returns least value for priority queue.
	}

	//====COMPLEX STATE FUNCTIONS====\\

	public void move(String m)
	{
		int location = -1;
		String start = "";
		String end = "";
		int capture = -1;
		for (int scan=0; scan<m.length(); scan++)
		{
			if (m.charAt(scan) == ' ')
			{
				start = m.substring(0, scan);
				end = m.substring(scan+1);
			}
		}
		for (int i=0; i<state.length; i++)
		{
			if (!state[i].equals("") && start.equals(state[i]))
				location = i;
		}
		if (start.length() == 0 || end.length() == 0)
		{
			System.out.println(m + " is an invalid input. Input a different move.");
			Scanner input = new Scanner(System.in);
			move(input.nextLine());
			return;
		}
		if (location == -1)
		{
			System.out.println("No pawn exists on " + start + ". Input a different move.");
			Scanner input = new Scanner(System.in);
			move(input.nextLine());
			return;
		}
		if (player == 1 && location < state.length/2)	// White pawns.
		{
			if (start.substring(1).equals("2") && start.charAt(0) == end.charAt(0) && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) - 2)		// Forward 2.
			{
				for (int p=0; p<state.length; p++)
				{
					if (!state[p].equals("") && state[p].charAt(0) == start.charAt(0) && (Integer.parseInt(state[p].substring(1)) == 3 || Integer.parseInt(state[p].substring(1)) == 4))
					{
						System.out.println("Pawn blocked. Input a different move.");
						Scanner input = new Scanner(System.in);
						move(input.nextLine());
						return;
					}
				}
			}
			else if (Integer.parseInt(start.substring(1)) != state.length/2 && start.charAt(0) == end.charAt(0) && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) - 1)		// Forward 1.
			{
				for (int p=0; p<state.length; p++)
				{
					if (!state[p].equals("") && state[p].charAt(0) == start.charAt(0) && Integer.parseInt(state[p].substring(1)) == Integer.parseInt(start.substring(1)) + 1)
					{
						System.out.println("Pawn blocked. Input a different move.");
						Scanner input = new Scanner(System.in);
						move(input.nextLine());
						return;
					}
				}
			}
			else if (number(end.charAt(0)) == number(start.charAt(0)) - 1 && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) - 1)	// Capture left.
			{
				boolean legal = false;
				for (int p=state.length/2; p<state.length; p++)	// You can only capture black pawns.
				{
					if (!state[p].equals("") && number(state[p].charAt(0)) == number(start.charAt(0)) - 1 && Integer.parseInt(state[p].substring(1)) == Integer.parseInt(start.substring(1)) + 1)
					{
						legal = true;
						capture = p;
					}
				}
				if (!legal)
				{
					System.out.println("No pawn to capture. Input a different move.");
					Scanner input = new Scanner(System.in);
					move(input.nextLine());
					return;
				}
			}
			else if (number(end.charAt(0)) == number(start.charAt(0)) + 1 && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) - 1)	// Capture right.
			{
				boolean legal = false;
				for (int p=state.length/2; p<state.length; p++)	// You can only capture black pawns.
				{
					if (!state[p].equals("") && number(state[p].charAt(0)) == number(start.charAt(0)) + 1 && Integer.parseInt(state[p].substring(1)) == Integer.parseInt(start.substring(1)) + 1)
					{
						legal = true;
						capture = p;
					}
				}
				if (!legal)
				{
					System.out.println("No pawn to capture. Input a different move.");
					Scanner input = new Scanner(System.in);
					move(input.nextLine());
					return;
				}
			}
			else
			{
				System.out.println("Invalid move. Input a different move.");
				Scanner input = new Scanner(System.in);
				move(input.nextLine());
				return;
			}
		}
		else if (player == 2 && location >= state.length/2)	// Black pawns.
		{
			if (start.substring(1).equals("" + (state.length/2 - 1)) && start.charAt(0) == end.charAt(0) && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) + 2)		// Forward 2.
			{
				for (int p=0; p<state.length; p++)
				{
					if (!state[p].equals("") && state[p].charAt(0) == start.charAt(0) && (Integer.parseInt(state[p].substring(1)) == (state.length/2 - 2) || Integer.parseInt(state[p].substring(1)) == (state.length/2 - 3)))
					{
						System.out.println("Pawn blocked. Input a different move.");
						Scanner input = new Scanner(System.in);
						move(input.nextLine());
						return;
					}
				}
			}
			else if (Integer.parseInt(start.substring(1)) != 1 && start.charAt(0) == end.charAt(0) && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) + 1)		// Forward 1.
			{
				for (int p=0; p<state.length/2; p++)	// You can only capture white pawns.
				{
					if (!state[p].equals("") && state[p].charAt(0) == start.charAt(0) && Integer.parseInt(state[p].substring(1)) == Integer.parseInt(start.substring(1)) - 1)
					{
						System.out.println("Pawn blocked. Input a different move.");
						Scanner input = new Scanner(System.in);
						move(input.nextLine());
						return;
					}
				}
			}
			else if (number(end.charAt(0)) == number(start.charAt(0)) - 1 && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) + 1)	// Capture left.
			{
				boolean legal = false;
				for (int p=0; p<state.length/2; p++)	// You can only capture white pawns.
				{
					if (!state[p].equals("") && number(state[p].charAt(0)) == number(start.charAt(0)) - 1 && Integer.parseInt(state[p].substring(1)) == Integer.parseInt(start.substring(1)) - 1)
					{
						legal = true;
						capture = p;
					}
				}
				if (!legal)
				{
					System.out.println("No pawn to capture. Input a different move.");
					Scanner input = new Scanner(System.in);
					move(input.nextLine());
					return;
				}
			}
			else if (number(end.charAt(0)) == number(start.charAt(0)) + 1 && Integer.parseInt(start.substring(1)) == Integer.parseInt(end.substring(1)) + 1)	// Capture right.
			{
				boolean legal = false;
				for (int p=0; p<state.length; p++)
				{
					if (!state[p].equals("") && number(state[p].charAt(0)) == number(start.charAt(0)) + 1 && Integer.parseInt(state[p].substring(1)) == Integer.parseInt(start.substring(1)) - 1)
					{
						legal = true;
						capture = p;
					}
				}
				if (!legal)
				{
					System.out.println("No pawn to capture. Input a different move.");
					Scanner input = new Scanner(System.in);
					move(input.nextLine());
					return;
				}
			}
			else
			{
				System.out.println("Invalid move. Input a different move.");
				Scanner input = new Scanner(System.in);
				move(input.nextLine());
				return;
			}
		}
		else
		{
			System.out.println("Invalid move. Input a different move.");
			Scanner input = new Scanner(System.in);
			move(input.nextLine());
			return;
		}
		state[location] = end;
		if (capture != -1)
		{
			if (mainGame == true)
				System.out.println(start + "x" + end);
			state[capture] = "";
		}
		else if (mainGame == true)
			System.out.println(start + "-" + end);
		if (player == 1)
		{
			if (Integer.parseInt(end.substring(1)) == state.length/2)
				winner = 1;
			else
				player = 2;
		}
		else
		{
			if (Integer.parseInt(end.substring(1)) == 1)
				winner = 2;
			else
				player = 1;
		}
		if (winner == -1)
		{
			setValidActions(this);
			boolean draw = true;
			for (int i=0; i<getActions().length; i++)
			{
				if (getActions()[i] != "")
					draw = false;
			}
			if (draw)
				winner = 0;
		}
		if (mainGame == true)
			print();
	}
	public State init(int size)		// Sets the starting location of each pawn (first digit 1=a, 2=b, etc.; second as usual).
	{
		State grant;
		String[] state = new String[size*2];
		for (int i=0; i<size; i++)
		{
			state[i] = "" + letter(i+1) + 2;
		}
		for (int i=size; i<size*2; i++)
		{
			state[i] = "" + letter(i-size+1) + (size - 1);
		}
		for (int i=0; i<4*size; i++)
		{

		}
		grant = new State(state, 1);
		return grant;
	}
	public void setValidActions(State s)
	{
		if (s.getWinner() != -1)
		{
			for (int i=0; i<s.getState().length; i++)
			{
				s.setAction(i*4, "");
				s.setAction(i*4+1, "");
				s.setAction(i*4+2, "");
				s.setAction(i*4+3, "");
			}
			return;
		}
		if (s.getPlayer() == 1)	// White pawns.
		{
			for (int i=s.getState().length/2; i<s.getState().length; i++)
			{
				s.setAction(i*4, "");
				s.setAction(i*4+1, "");
				s.setAction(i*4+2, "");
				s.setAction(i*4+3, "");
			}
			for (int i=0; i<s.getState().length/2; i++)
			{
				if (s.getState()[i] != "")
				{
					// Default: action works.
					s.setAction(i*4, s.getState()[i] + " " + s.getState()[i].charAt(0) + (Integer.parseInt(s.getState()[i].substring(1)) + 2));		// Forward 2.
					if (Integer.parseInt(s.getState()[i].substring(1)) != s.getState().length/2)
						s.setAction(i*4+1, s.getState()[i] + " " + s.getState()[i].charAt(0) + (Integer.parseInt(s.getState()[i].substring(1)) + 1));	// Forward 1.
					if (Integer.parseInt(s.getState()[i].substring(1)) != s.getState().length/2 && s.getState()[i].charAt(0) != 'a')
						s.setAction(i*4+2, s.getState()[i] + " " + s.letter(s.number(s.getState()[i].charAt(0))-1) + (Integer.parseInt(s.getState()[i].substring(1)) + 1));	// Left capture.
					if (Integer.parseInt(s.getState()[i].substring(1)) != s.getState().length/2 && s.getState()[i].charAt(0) != s.letter(s.getState().length/2))
						s.setAction(i*4+3, s.getState()[i] + " " + s.letter(s.number(s.getState()[i].charAt(0))+1) + (Integer.parseInt(s.getState()[i].substring(1)) + 1));	// Right capture.

					// Check if action *actually* works.
					if (s.getState()[i].substring(1).equals("2"))		// Forward 2.
					{
						for (int p=0; p<s.getState().length; p++)
						{
							if (!s.getState()[p].equals("") && s.getState()[p].charAt(0) == s.getState()[i].charAt(0) && (Integer.parseInt(s.getState()[p].substring(1)) == 3 || Integer.parseInt(s.getState()[p].substring(1)) == 4))
							{
								s.setAction(i*4, "");
							}
						}
					}
					else
						s.setAction(i*4, "");
					if (Integer.parseInt(s.getState()[i].substring(1)) != s.getState().length/2)		// Forward 1.
					{
						for (int p=0; p<s.getState().length; p++)
						{
							if (!s.getState()[p].equals("") && s.getState()[p].charAt(0) == s.getState()[i].charAt(0) && Integer.parseInt(s.getState()[p].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
							{
								s.setAction(i*4+1, "");
							}
						}
					}
					else
						s.setAction(i*4+1, "");
					boolean legal = false;	// Left capture.
					for (int p=s.getState().length/2; p<s.getState().length; p++)	// You can only capture black pawns.
					{
						if (!s.getState()[p].equals("") && number(s.getState()[p].charAt(0)) == number(s.getState()[i].charAt(0)) - 1 && Integer.parseInt(s.getState()[p].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
						{
							legal = true;
						}
					}
					if (!legal)
					{
						s.setAction(i*4+2, "");
					}
					legal = false;	// Right capture.
					for (int p=s.getState().length/2; p<s.getState().length; p++)	// You can only capture black pawns.
					{
						if (!s.getState()[p].equals("") && number(s.getState()[p].charAt(0)) == number(s.getState()[i].charAt(0)) + 1 && Integer.parseInt(s.getState()[p].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
						{
							legal = true;
						}
					}
					if (!legal)
					{
						s.setAction(i*4+3, "");
					}
				}
			}
		}
		else if (s.getPlayer() == 2)	// Black pawns.
		{
			for (int i=0; i<s.getState().length/2; i++)
			{
				s.setAction(i*4, "");
				s.setAction(i*4+1, "");
				s.setAction(i*4+2, "");
				s.setAction(i*4+3, "");
			}
			for (int i=s.getState().length/2; i<s.getState().length; i++)
			{
				if (s.getState()[i] != "")
				{
					// Default: action works.
					s.setAction(i*4, s.getState()[i] + " " + s.getState()[i].charAt(0) + (Integer.parseInt(s.getState()[i].substring(1)) - 2));		// Forward 2.
					if (Integer.parseInt(s.getState()[i].substring(1)) != 1)
						s.setAction(i*4+1, s.getState()[i] + " " + s.getState()[i].charAt(0) + (Integer.parseInt(s.getState()[i].substring(1)) - 1));	// Forward 1.
					if (Integer.parseInt(s.getState()[i].substring(1)) != 1 && s.getState()[i].charAt(0) != 'a')
						s.setAction(i*4+2, s.getState()[i] + " " + s.letter(s.number(s.getState()[i].charAt(0))-1) + (Integer.parseInt(s.getState()[i].substring(1)) - 1));	// Left capture.
					if (Integer.parseInt(s.getState()[i].substring(1)) != 1 && s.getState()[i].charAt(0) != s.letter(s.getState().length/2))
						s.setAction(i*4+3, s.getState()[i] + " " + s.letter(s.number(s.getState()[i].charAt(0))+1) + (Integer.parseInt(s.getState()[i].substring(1)) - 1));	// Right capture.

					// Check if action *actually* works.
					if (s.getState()[i].substring(1).equals("" + (s.getState().length/2-1)))		// Forward 2.
					{
						for (int p=0; p<s.getState().length; p++)
						{
							if (!s.getState()[p].equals("") && s.getState()[p].charAt(0) == s.getState()[i].charAt(0) && (Integer.parseInt(s.getState()[p].substring(1)) == s.getState().length/2 - 2 || Integer.parseInt(s.getState()[p].substring(1)) == s.getState().length/2 - 3))
							{
								s.setAction(i*4, "");
							}
						}
					}
					else
						s.setAction(i*4, "");
					if (Integer.parseInt(s.getState()[i].substring(1)) != s.getState().length/2)		// Forward 1.
					{
						for (int p=0; p<s.getState().length; p++)
						{
							if (!s.getState()[p].equals("") && s.getState()[p].charAt(0) == s.getState()[i].charAt(0) && Integer.parseInt(s.getState()[p].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
							{
								s.setAction(i*4+1, "");
							}
						}
					}
					else
						s.setAction(i*4+1, "");
					boolean legal = false;	// Left capture.
					for (int p=0; p<s.getState().length/2; p++)	// You can only capture white pawns.
					{
						if (!s.getState()[p].equals("") && number(s.getState()[p].charAt(0)) == number(s.getState()[i].charAt(0)) - 1 && Integer.parseInt(s.getState()[p].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
						{
							legal = true;
						}
					}
					if (!legal)
					{
						s.setAction(i*4+2, "");
					}
					legal = false;	// Right capture.
					for (int p=0; p<s.getState().length/2; p++)	// You can only capture white pawns.
					{
						if (!s.getState()[p].equals("") && number(s.getState()[p].charAt(0)) == number(s.getState()[i].charAt(0)) + 1 && Integer.parseInt(s.getState()[p].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
						{
							legal = true;
						}
					}
					if (!legal)
					{
						s.setAction(i*4+3, "");
					}
				}
			}
		}
	}
	public State result(State s, String a)
	{
		State r = new State(s.getState().clone(), s.getPlayer());
		r.move(a);
		return r;
	}
	public int cost(State s, int a, State s1)
	{
		// Don't think this matters here.
		return 1;
	}
	public char letter(int i)
	{
		String letters = "abcdefghijklmnopqrstuvwxyz";
		if (i<1 || i>26)
		{
			System.out.println("Input not supported.");
			return ' ';
		}
		return letters.charAt(i-1);
	}
	public int number(char c)
	{
		String letters = "abcdefghijklmnopqrstuvwxyz";
		for (int i=0; i<26; i++)
		{
			if (letters.charAt(i) == c)
				return i+1;
		}
		System.out.println("Input not supported.");
		return 0;
	}
	public void printState()
	{
		System.out.print("Current state: [");
		for (int i=0; i<state.length-1; i++)
		{
			System.out.print(state[i] + ", ");
		}
		System.out.println(state[state.length-1] + "]");
	}
	public void print()
	{
		Board b = getBoard();
		System.out.print("  ");
		for (int coords=0; coords<b.getBoard().length; coords++)
		{
			System.out.print("  " + letter(coords+1) + " ");
		}
		System.out.println();
		for (int i=b.getBoard().length-1; i>=0; i--)
		{
			System.out.print("  ");
			for (int border=0; border<b.getBoard().length; border++)
			{
				System.out.print("+---");
			}
			System.out.println("+");
			if (i+1<10)
				System.out.print((i+1) + " ");
			else
				System.out.print(i+1);
			for (int j=0; j<b.getBoard().length; j++)
			{
				boolean hasWhite = false;
				boolean hasBlack = false;
				for (int k=0; k<state.length; k++)
				{
					if (!state[k].equals("") && number(getState()[k].charAt(0)) == j+1 && Integer.parseInt(getState()[k].substring(1)) == i+1)
					{
						if (k<getState().length/2)
							hasWhite = true;
						else
							hasBlack = true;
					}
				}
				if (hasWhite)
					System.out.print("| W ");
				else if (hasBlack)
					System.out.print("| B ");
				else
					System.out.print("|   ");
			}
			System.out.print("| " + (i+1));
			System.out.println();
		}
		System.out.print("  ");
		for (int border=0; border<b.getBoard().length; border++)
		{
			System.out.print("+---");
		}
		System.out.println("+");
		System.out.print("  ");
		for (int coords=0; coords<b.getBoard().length; coords++)
		{
			System.out.print("  " + letter(coords+1) + " ");
		}
		System.out.println();
		if (winner == -1)
		{
			if (player == 1)
				System.out.println("White to move.");
			else
				System.out.println("Black to move.");
		}
		else
		{
			if (winner == 1)
				System.out.println("White wins! Game over.");
			else if (winner == 2)
				System.out.println("Black wins! Game over.");
			else
				System.out.println("Draw! Game over.");
		}
	}
	public void print(int depth)
	{
		Board b = getBoard();
		for (int d=0; d<depth; d++)
			System.out.print("	");
		System.out.print("  ");
		for (int coords=0; coords<b.getBoard().length; coords++)
		{
			System.out.print("  " + letter(coords+1) + " ");
		}
		System.out.println();
		for (int i=b.getBoard().length-1; i>=0; i--)
		{
			for (int d=0; d<depth; d++)
				System.out.print("	");
			System.out.print("  ");
			for (int border=0; border<b.getBoard().length; border++)
			{
				System.out.print("+---");
			}
			System.out.println("+");
			for (int d=0; d<depth; d++)
				System.out.print("	");
			if (i+1<10)
				System.out.print((i+1) + " ");
			else
				System.out.print(i+1);
			for (int j=0; j<b.getBoard().length; j++)
			{
				boolean hasWhite = false;
				boolean hasBlack = false;
				for (int k=0; k<state.length; k++)
				{
					if (!state[k].equals("") && number(getState()[k].charAt(0)) == j+1 && Integer.parseInt(getState()[k].substring(1)) == i+1)
					{
						if (k<getState().length/2)
							hasWhite = true;
						else
							hasBlack = true;
					}
				}
				if (hasWhite)
					System.out.print("| W ");
				else if (hasBlack)
					System.out.print("| B ");
				else
					System.out.print("|   ");
			}
			System.out.print("| " + (i+1));
			System.out.println();
		}
		for (int d=0; d<depth; d++)
			System.out.print("	");
		System.out.print("  ");
		for (int border=0; border<b.getBoard().length; border++)
		{
			System.out.print("+---");
		}
		System.out.println("+");
		for (int d=0; d<depth; d++)
			System.out.print("	");
		System.out.print("  ");
		for (int coords=0; coords<b.getBoard().length; coords++)
		{
			System.out.print("  " + letter(coords+1) + " ");
		}
		System.out.println();
		for (int d=0; d<depth; d++)
			System.out.print("	");
		if (winner == -1)
		{
			if (player == 1)
				System.out.println("White to move.");
			else
				System.out.println("Black to move.");
		}
		else
		{
			if (winner == 1)
				System.out.println("White wins! Game over.");
			else if (winner == 2)
				System.out.println("Black wins! Game over.");
			else
				System.out.println("Draw! Game over.");
		}
	}

	//====MINIMAX ALGORITHMS====\\

	public String minimax(State s, String cpuPlayer)
	{
		HashMap<String, Integer> visited = new HashMap<String, Integer>();
		UtilMovePair result;
		if (cpuPlayer.equals("W"))
			result = minValue(s, 0, visited);
		else
			result = maxValue(s, 0, visited);	// Treats the human player as having already moved.
//		System.out.println("\n\nFINAL DETERMINATION: " + result.getMove());
//		System.out.println("Utility: " + result.getUtil());
		return result.getMove();
	}
	public UtilMovePair maxValue(State s, int depth, HashMap<String, Integer> visited)	// This will always simulate the CPU move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) "human" move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(-1);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(-1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//				next.print(depth);
				UtilMovePair p = minValue(next, depth+1, visited);	// Best move the human could make.
				if (p.getUtil() > result.getUtil())		// Is it the least bad option for the computer?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public UtilMovePair minValue(State s, int depth, HashMap<String, Integer> visited)	// This will always simulate the human move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) computer move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(1);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//				next.print(depth);
				UtilMovePair p = maxValue(next, depth+1, visited);	// Best move the computer could make.
				if (p.getUtil() < result.getUtil())		// Is it least bad option for the human?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public String abMinimax(State s, String cpuPlayer)
	{
		HashMap<String, Integer> visited = new HashMap<String, Integer>();
		UtilMovePair result;
		if (cpuPlayer.equals("W"))
			result = abMinValue(s, 0, -1000000, 1000000, visited);
		else
			result = abMaxValue(s, 0, -1000000, 1000000, visited);	// Treats the human player as having already moved.
//		System.out.println("\n\nFINAL DETERMINATION: " + result.getMove());
//		System.out.println("Utility: " + result.getUtil());
		return result.getMove();
	}
	public UtilMovePair abMaxValue(State s, int depth, int alpha, int beta, HashMap<String, Integer> visited)	// This will always simulate the CPU move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) "human" move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(-1);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(-1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//				next.print(depth);
				UtilMovePair p = abMinValue(next, depth+1, alpha, beta, visited);	// Best move the human could make.
				if (p.getUtil() > result.getUtil())		// Is it the least bad option for the computer?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
					alpha = Math.max(alpha, result.getUtil());
					if (beta <= alpha)
						break;
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public UtilMovePair abMinValue(State s, int depth, int alpha, int beta, HashMap<String, Integer> visited)	// This will always simulate the human move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) computer move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(1);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//				next.print(depth);
				UtilMovePair p = abMaxValue(next, depth+1, alpha, beta, visited);	// Best move the computer could make.
				if (p.getUtil() < result.getUtil())		// Is it least bad option for the human?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
					beta = Math.min(beta, result.getUtil());
					if (beta <= alpha)
						break;
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public String hMinimax(State s, String cpuPlayer, int maxDepth)
	{
		HashMap<String, Integer> visited = new HashMap<String, Integer>();
		UtilMovePair result;
		if (cpuPlayer.equals("W"))
			result = hMinValue(s, 0, maxDepth, visited);
		else
			result = hMaxValue(s, 0, maxDepth, visited);	// Treats the human player as having already moved.
//		System.out.println("\n\nFINAL DETERMINATION: " + result.getMove());
//		System.out.println("Utility: " + result.getUtil());
		return result.getMove();
	}
	public UtilMovePair hMaxValue(State s, int depth, int maxDepth, HashMap<String, Integer> visited)	// This will always simulate the CPU move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) "human" move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(-100000);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(-1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		if (depth >= maxDepth)
		{
			result = new UtilMovePair();
			int estUtil = heuristic(s, s.getPlayer(), 3 - s.getPlayer());
			result.setUtil(estUtil);
			result.setMove("");
			return result;
		}
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//				next.print(depth);
				UtilMovePair p = hMinValue(next, depth+1, maxDepth, visited);	// Best move the human could make.
				if (p.getUtil() > result.getUtil())		// Is it the least bad option for the computer?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public UtilMovePair hMinValue(State s, int depth, int maxDepth, HashMap<String, Integer> visited)	// This will always simulate the human move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) computer move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(100000);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		if (depth >= maxDepth)
		{
			result = new UtilMovePair();
			int estUtil = heuristic(s, s.getPlayer(), 3 - s.getPlayer());
			result.setUtil(estUtil);
			result.setMove("");
			return result;
		}
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//	next.print(depth);
				UtilMovePair p = hMaxValue(next, depth+1, maxDepth, visited);	// Best move the computer could make.
				if (p.getUtil() < result.getUtil())		// Is it least bad option for the human?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public String abhMinimax(State s, String cpuPlayer, int maxDepth)
	{
		HashMap<String, Integer> visited = new HashMap<String, Integer>();
		UtilMovePair result;
		if (cpuPlayer.equals("W"))
			result = abhMinValue(s, 0, maxDepth, -1000000, 1000000, visited);
		else
			result = abhMaxValue(s, 0, maxDepth, -1000000, 1000000, visited);	// Treats the human player as having already moved.
//		System.out.println("\n\nFINAL DETERMINATION: " + result.getMove());
//		System.out.println("Utility: " + result.getUtil());
		return result.getMove();
	}
	public UtilMovePair abhMaxValue(State s, int depth, int maxDepth, int alpha, int beta, HashMap<String, Integer> visited)	// This will always simulate the CPU move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) "human" move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(-1000000);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(-1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		if (depth >= maxDepth)
		{
			result = new UtilMovePair();
			int estUtil = heuristic(s, s.getPlayer(), 3 - s.getPlayer());
			result.setUtil(estUtil);
			result.setMove("");
			return result;
		}
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//				next.print(depth);
				UtilMovePair p = abhMinValue(next, depth+1, maxDepth, alpha, beta, visited);	// Best move the human could make.
				if (p.getUtil() > result.getUtil())		// Is it the least bad option for the computer?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
					alpha = Math.max(alpha, result.getUtil());
					if (beta <= alpha)
						break;
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public UtilMovePair abhMinValue(State s, int depth, int maxDepth, int alpha, int beta, HashMap<String, Integer> visited)	// This will always simulate the human move.
	{
		if (s.getWinner() != -1)	// You should only receive this from a winning (or drawing) computer move.
		{
			UtilMovePair result = new UtilMovePair();
			if (s.getWinner() == 0)
				result.setUtil(0);
			else
				result.setUtil(100000);
			result.setMove("");
			return result;
		}
		UtilMovePair result = new UtilMovePair();
		result.setUtil(1000000);
		String check = "";
		for (int i=0; i<s.getState().length; i++)
		{
			check = check + s.getState()[i];
		}
		if (visited.containsKey(check))
		{
			result.setMove("");
			return result;
		}
		else
			visited.put(check, 0);
		if (depth >= maxDepth)
		{
			result = new UtilMovePair();
			int estUtil = heuristic(s, s.getPlayer(), 3 - s.getPlayer());
			result.setUtil(estUtil);
			result.setMove("");
			return result;
		}
		s.setValidActions(s);
		boolean draw = true;
		for (int a=0; a<s.getActions().length; a++)
		{
			if (s.getActions()[a] != "")
			{
				draw = false;
				State next = s.result(s, s.getActions()[a]);
				//	next.print(depth);
				UtilMovePair p = abhMaxValue(next, depth+1, maxDepth, alpha, beta, visited);	// Best move the computer could make.
				if (p.getUtil() < result.getUtil())		// Is it least bad option for the human?
				{
					result.setMove(s.getActions()[a]);
					result.setUtil(p.getUtil());
					beta = Math.min(beta, result.getUtil());
					if (beta <= alpha)
						break;
				}
			}
		}
		if (draw)		// I don't think this should ever happen.
		{
			result.setUtil(0);
			result.setMove("");
			return result;
		}
		return result;
	}
	public int heuristic(State s, int player, int currentPlayer)
	{
		int util = 0;

		if (player == 1)	// If CPU is playing as white.
		{
			for (int i=0; i<s.getState().length/2; i++)
			{
				if (s.getState()[i+s.getState().length/2].equals(""))
				{
					util += 5;		// Points for opponent missing pawns.
				}
				if (s.getState()[i].equals(""))
				{
					util -= 5;		// Penalty for missing pawns.
				}
				else
				{
					if (Integer.parseInt(s.getState()[i].substring(1)) == s.getState().length/2-1)	// One move away from a win.
						return 1000;
					util += Integer.parseInt(s.getState()[i].substring(1)) - 2;		// Points for being further up board.
					for (int j=s.getState().length/2; j<s.getState().length; j++)
					{
						if (!s.getState()[j].equals(""))
						{
							util += 2 - Integer.parseInt(s.getState()[j].substring(1));		// Penalty for opponent being further up board.
							if (s.getState()[j].charAt(0) == s.getState()[i].charAt(0) && Integer.parseInt(s.getState()[j].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
							{
								util--;		// Penalty for deadlocked pawn.
							}
							if (number(s.getState()[j].charAt(0)) == number(s.getState()[i].charAt(0)) - 1 && Integer.parseInt(s.getState()[j].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
							{
								if (player == currentPlayer)
									util++;		// Points for threatening a pawn.
								else
									util--;		// Penalty for pawn under threat (left).
							}
							if (number(s.getState()[j].charAt(0)) == number(s.getState()[i].charAt(0)) + 1 && Integer.parseInt(s.getState()[j].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
							{
								if (player == currentPlayer)
									util++;		// Points for threatening a pawn.
								else
									util--;		// Penalty for pawn under threat (right).
							}
						}
					}
					for (int i2=0; i2<s.getState().length/2; i2++)
					{
						if (s.getState()[i2] != "" && i != i2)
						{
							if (number(s.getState()[i2].charAt(0)) == number(s.getState()[i].charAt(0)) - 1 && Integer.parseInt(s.getState()[i2].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
								util += 2;	// Points for protected pawn.
							if (number(s.getState()[i2].charAt(0)) == number(s.getState()[i].charAt(0)) + 1 && Integer.parseInt(s.getState()[i2].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) + 1)
								util += 2;	// Points for protected pawn.
						}
					}
				}
			}
		}
		else		// If CPU is playing as black.
		{
			for (int i=s.getState().length/2; i<s.getState().length; i++)
			{
				if (s.getState()[i-s.getState().length/2].equals(""))
				{
					util += 5;		// Points for opponent missing pawns.
				}
				if (s.getState()[i].equals(""))
				{
					util -= 5;		// Penalty for missing pawns.
				}
				else
				{
					if (Integer.parseInt(s.getState()[i].substring(1)) == 2)	// One move away from a win.
						return 1000;
					util += s.getState().length/2 - 1 - Integer.parseInt(s.getState()[i].substring(1));		// Points for being further up board.
					for (int j=0; j<s.getState().length/2; j++)
					{
						if (!s.getState()[j].equals(""))
						{
							util += 2 - Integer.parseInt(s.getState()[j].substring(1));		// Penalty for opponent being further up board.
							if (s.getState()[j].charAt(0) == s.getState()[i].charAt(0) && Integer.parseInt(s.getState()[j].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
							{
								util--;		// Penalty for deadlocked pawn.
							}
							if (number(s.getState()[j].charAt(0)) == number(s.getState()[i].charAt(0)) - 1 && Integer.parseInt(s.getState()[j].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
							{
								if (player == currentPlayer)
									util++;		// Points for threatening a pawn.
								else
									util--;		// Penalty for pawn under threat (left).
							}
							if (number(s.getState()[j].charAt(0)) == number(s.getState()[i].charAt(0)) + 1 && Integer.parseInt(s.getState()[j].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
							{
								if (player == currentPlayer)
									util++;		// Points for threatening a pawn.
								else
									util--;		// Penalty for pawn under threat (right).
							}
						}
					}
					for (int i2=0; i2<s.getState().length/2; i2++)
					{
						if (s.getState()[i2] != "" && i != i2)
						{
							if (number(s.getState()[i2].charAt(0)) == number(s.getState()[i].charAt(0)) - 1 && Integer.parseInt(s.getState()[i2].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
								util += 2;	// Points for protected pawn.
							if (number(s.getState()[i2].charAt(0)) == number(s.getState()[i].charAt(0)) + 1 && Integer.parseInt(s.getState()[i2].substring(1)) == Integer.parseInt(s.getState()[i].substring(1)) - 1)
								util += 2;	// Points for protected pawn.
						}
					}
				}
			}
		}
		return util;
	}

	//====READ-EVAL-PRINT LOOP===\\

	public static void repl(State grant)
	{	
		Scanner input = new Scanner(System.in);

		// Ask for settings: board size, etc.
		System.out.println("Input your desired board size: 8 for 8x8, 4 for 4x4, etc.");

		int check = input.nextInt();
		while (check > 26 || check < 4)
		{
			if (check > 26)
				System.out.println("Your board is too large to handle! Choose a number 26 or below.");
			else
				System.out.println("Your board is too tiny! Choose a number 4 or greater.");
			check = input.nextInt();
		}

		grant = grant.init(check);
		grant.setMainGame(true);

		System.out.println("Choose your opponent.\n"
				+ "1. An agent that plays randomly\n"
				+ "2. An agent that uses MINIMAX\n"
				+ "3. An agent that uses MINIMAX with alpha-beta pruning\n"
				+ "4. An agent that uses H-MINIMAX with a fixed depth cutoff\n"
				+ "5. An agent that uses H-MINIMAX with a fixed depth cutoff and alpha-beta pruning");

		int sam = input.nextInt();
		input.nextLine();

		System.out.println("Do you want to play black (B) or white (W)? White plays first.");

		String player = input.nextLine();
		while (!player.equals("B") && !player.equals("b") && !player.equals("W") && !player.equals("w"))
		{
			System.out.println("Invalid input. Do you want to play black (B) or white (W)? White plays first.");
			player = input.nextLine();
		}

		if (sam == 1)
		{
			String james = " ";
			grant.print();
			if (player.equals("W") || player.equals("w"))
			{
				james = input.nextLine();
				if (james != "")
					grant.move(james);
			}
			while(james != "" && grant.getWinner() == -1)
			{
				grant.printState();
				grant.setValidActions(grant);

				String randomMove = "";
				while (randomMove.equals(""))
					randomMove = grant.getActions()[(int)(Math.random()*grant.getActions().length)];
				grant.move(randomMove);

				if (james != "" && grant.getWinner() == -1)
				{
					james = input.nextLine();
					if (james != "")
						grant.move(james);
					grant.setValidActions(grant);
				}
			}
			if (grant.getWinner() == -1)
				System.out.println("Game aborted.");
		}
		else if (sam == 2)
		{
			String james = " ";
			grant.print();
			String cpuPlayer = "W";
			if (player.equals("W") || player.equals("w"))
			{
				cpuPlayer = "B";
				james = input.nextLine();
				if (james != "")
					grant.move(james);
			}
			while(james != "" && grant.getWinner() == -1)
			{
				String cpuMove = grant.minimax(grant, cpuPlayer);
				grant.move(cpuMove);

				if (james != "" && grant.getWinner() == -1)
				{
					james = input.nextLine();
					if (james != "")
						grant.move(james);
					grant.setValidActions(grant);
				}
			}
			if (grant.getWinner() == -1)
				System.out.println("Game aborted.");
		}
		else if (sam == 3)
		{
			String james = " ";
			grant.print();
			String cpuPlayer = "W";
			if (player.equals("W") || player.equals("w"))
			{
				cpuPlayer = "B";
				james = input.nextLine();
				if (james != "")
					grant.move(james);
			}
			while(james != "" && grant.getWinner() == -1)
			{
				String cpuMove = grant.abMinimax(grant, cpuPlayer);
				grant.move(cpuMove);

				if (james != "" && grant.getWinner() == -1)
				{
					james = input.nextLine();
					if (james != "")
						grant.move(james);
					grant.setValidActions(grant);
				}
			}
			if (grant.getWinner() == -1)
				System.out.println("Game aborted.");
		}
		else if (sam == 4)
		{
			System.out.println("What would you like the cutoff depth to be? Recommended: 5 or less.");
			int cutoff = input.nextInt();
			input.nextLine();
			String james = " ";
			grant.print();
			String cpuPlayer = "W";
			if (player.equals("W") || player.equals("w"))
			{
				cpuPlayer = "B";
				james = input.nextLine();
				if (james != "")
					grant.move(james);
			}
			while(james != "" && grant.getWinner() == -1)
			{
				String cpuMove = grant.hMinimax(grant, cpuPlayer, cutoff);
				grant.move(cpuMove);

				if (james != "" && grant.getWinner() == -1)
				{
					james = input.nextLine();
					if (james != "")
						grant.move(james);
					grant.setValidActions(grant);
				}
			}
			if (grant.getWinner() == -1)
				System.out.println("Game aborted.");
		}
		else if (sam == 5)
		{
			System.out.println("What would you like the cutoff depth to be? Recommended: 7 or less.");
			int cutoff = input.nextInt();
			input.nextLine();
			String james = " ";
			grant.print();
			String cpuPlayer = "W";
			if (player.equals("W") || player.equals("w"))
			{
				cpuPlayer = "B";
				james = input.nextLine();
				if (james != "")
					grant.move(james);
			}
			while(james != "" && grant.getWinner() == -1)
			{
				String cpuMove = grant.abhMinimax(grant, cpuPlayer, cutoff);
				grant.move(cpuMove);

				if (james != "" && grant.getWinner() == -1)
				{
					james = input.nextLine();
					if (james != "")
						grant.move(james);
					grant.setValidActions(grant);
				}
			}
			if (grant.getWinner() == -1)
				System.out.println("Game aborted.");
		}
		else
		{
			System.out.println("Extra-secret bonus option: PLAY AGAINST YOURSELF");
			String james = " ";
			grant.print();
			while(james != "" && grant.getWinner() == -1)
			{
				james = input.nextLine();
				if (james != "")
					grant.move(james);
			}
			if (grant.getWinner() == -1)
				System.out.println("Game aborted.");
		}
		input.close();
	}
	public static void main(String[] args)
	{
		State grant = new State();
		repl(grant);
	}
}