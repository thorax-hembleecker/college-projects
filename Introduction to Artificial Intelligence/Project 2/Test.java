import java.util.Scanner;

public class Test
{
	public static void main(String[] args)
	{	
		System.out.println("Part 1: Representing Clauses");
		System.out.println("Please input the name of the file you would like to read. (Make sure the file is in the \"cnf\" folder.)");
		Scanner input = new Scanner(System.in);
		String grant = input.next();
		KnowledgeBase cnf = new KnowledgeBase();
		cnf.addInput("cnf\\" + grant);
		System.out.println(grant + ": ");
		cnf.printKB();
		System.out.println("\n");
		String moveOn = "";
		while (!moveOn.equals("Y") && !moveOn.equals("y"))
		{
			System.out.println("Move on to Part 2? Input Y or N.");
			moveOn = input.next();
			if (moveOn.equals("N") || moveOn.equals("n"))
			{
				System.out.println("Please input the name of the file you would like to read. (Make sure the file is in the src folder.)");
				grant = input.next();
				cnf = new KnowledgeBase();
				cnf.addInput("cnf\\" + grant);
				System.out.println(grant + ": ");
				cnf.printKB();
				System.out.println("\n");
				moveOn = "";
			}
		}

		System.out.println("Part 2: Model Checking");
		modusPonens();
		wumpus();	// This one has the slight issue of falsely saying P2,2 is entailed. Not 100% sure of the cause.
		unicorn();
		System.out.println();

		System.out.println("Part 3: Satisfiability Testing");
		prob1(input);
		prob2(input);
		prob3(input);

		input.close();
	}

	public static void modusPonens()
	{
		// 1.
		Clause p = new Clause(1);
		Clause pImpliesQ = new Clause();
		pImpliesQ.addLiteral(-1);
		pImpliesQ.addLiteral(2);
		KnowledgeBase jackson = new KnowledgeBase();
		jackson.addClause(p);
		jackson.addClause(pImpliesQ);
		KnowledgeBase q = new KnowledgeBase(new Clause(2));
		System.out.print("KB = P, P->Q: ");
		jackson.printKB();
		System.out.println();
		System.out.print("Query = Q: ");
		q.getKB().get(0).printClause();
		System.out.println();
		System.out.println("   P, P->Q |= Q: " + jackson.entails(q));

		System.out.println();
	}
	public static void wumpus()
	{
		// 2.
		/* P11 = 1
		 * 
		 * B11 = 2
		 * P12 = 3
		 * P21 = 4
		 * 
		 * B21 = 5
		 * P11 = 1
		 * P22 = 6
		 * P31 = 7
		 * 
		 * B12 = 8
		 * P11 = 1
		 * P22 = 6
		 * P13 = 9
		 */

		Clause r1 = new Clause(-1);

		Clause r2a = new Clause(-2);
		r2a.addLiteral(3);
		r2a.addLiteral(4);
		Clause r2b = new Clause(-3);
		r2b.addLiteral(2);
		Clause r2c = new Clause(-4);
		r2c.addLiteral(2);

		Clause r3a = new Clause(-5);
		r3a.addLiteral(1);
		r3a.addLiteral(6);
		r3a.addLiteral(7);
		Clause r3b = new Clause(-1);
		r3b.addLiteral(5);
		Clause r3c = new Clause(-6);
		r3c.addLiteral(5);
		Clause r3d = new Clause(-7);
		r3d.addLiteral(5);

		Clause r7a = new Clause(-8);
		r7a.addLiteral(1);
		r7a.addLiteral(6);
		r7a.addLiteral(9);
		Clause r7b = new Clause(-1);
		r7b.addLiteral(8);
		Clause r7c = new Clause(-6);
		r7c.addLiteral(8);
		Clause r7d = new Clause(-9);
		r7d.addLiteral(8);

		KnowledgeBase wumpus = new KnowledgeBase(r1);
		wumpus.addClause(r2a);
		wumpus.addClause(r2b);
		wumpus.addClause(r2c);
		wumpus.addClause(r3a);
		wumpus.addClause(r3b);
		wumpus.addClause(r3c);
		wumpus.addClause(r3d);
		wumpus.addClause(r7a);
		wumpus.addClause(r7b);
		wumpus.addClause(r7c);
		wumpus.addClause(r7d);

		Clause r4 = new Clause(-2);
		wumpus.addClause(r4);

		System.out.print("KB = !P11, B11 <=> P12 v P21, B21 <=> P11 v P22 v P31, B12 <=> P11 v P22 v P13, !B11: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = !P12: ");
		new Clause(-3).printClause();
		System.out.println();
		System.out.println("   KB |= !P12: " + wumpus.entails(new KnowledgeBase(new Clause(-3))));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = !P21: ");
		new Clause(-4).printClause();
		System.out.println();
		System.out.println("   KB |= !P21: " + wumpus.entails(new KnowledgeBase(new Clause(-4))));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = P22: ");
		new Clause(6).printClause();
		System.out.println();
		System.out.println("   KB |= P22: " + wumpus.entails(new KnowledgeBase(new Clause(6))));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = !P22: ");
		new Clause(-6).printClause();
		System.out.println();
		System.out.println("   KB |= !P22: " + wumpus.entails(new KnowledgeBase(new Clause(-6))));

		Clause r5 = new Clause(5);
		wumpus.addClause(r5);

		System.out.print("KB = KB, B21: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = P22 v P31: ");
		Clause p22vp31 = new Clause(6);
		p22vp31.addLiteral(7);
		p22vp31.printClause();
		System.out.println();
		System.out.println("   KB |= P22 v P31: " + wumpus.entails(new KnowledgeBase(p22vp31)));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = P22: ");
		new Clause(6).printClause();
		System.out.println();
		System.out.println("   KB |= P22: " + wumpus.entails(new KnowledgeBase(new Clause(6))));	// Something's funky about this one and I don't know what's causing it.
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = !P22: ");
		new Clause(-6).printClause();
		System.out.println();
		System.out.println("   KB |= !P22: " + wumpus.entails(new KnowledgeBase(new Clause(-6))));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = P31: ");
		new Clause(7).printClause();
		System.out.println();
		System.out.println("   KB |= P31: " + wumpus.entails(new KnowledgeBase(new Clause(7))));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = !P31: ");
		new Clause(-7).printClause();
		System.out.println();
		System.out.println("   KB |= !P31: " + wumpus.entails(new KnowledgeBase(new Clause(-7))));

		Clause r6 = new Clause(-8);
		wumpus.addClause(r6);

		System.out.print("KB = KB, !B12: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = !P22: ");
		new Clause(-6).printClause();
		System.out.println();
		System.out.println("   KB |= !P22: " + wumpus.entails(new KnowledgeBase(new Clause(-6))));
		System.out.print("KB: ");
		wumpus.printKB();
		System.out.println();
		System.out.print("Query = P31: ");
		new Clause(7).printClause();
		System.out.println();
		System.out.println("   KB |= P31: " + wumpus.entails(new KnowledgeBase(new Clause(7))));

		System.out.println();
	}
	public static void unicorn()
	{
		// 3.
		/* The unicorn is mythical: 1
		 * The unicorn is immortal: 2
		 * The unicorn is a mammal: 3
		 * The unicorn is horned: 4
		 * The unicorn is magical: 5
		 */

		Clause u1 = new Clause(-1);
		u1.addLiteral(2);
		Clause u2 = new Clause(1);
		u2.addLiteral(-2);
		Clause u3 = new Clause(1);
		u3.addLiteral(3);
		Clause u4 = new Clause(4);
		u4.addLiteral(-2);
		Clause u5 = new Clause(4);
		u5.addLiteral(-3);
		Clause u6 = new Clause(-4);
		u6.addLiteral(5);

		KnowledgeBase unicorn = new KnowledgeBase(u1);
		unicorn.addClause(u2);
		unicorn.addClause(u3);
		unicorn.addClause(u4);
		unicorn.addClause(u5);
		unicorn.addClause(u6);

		System.out.print("KB = !myth v imm, myth v !imm, myth v mamm, horn v !imm, horn v !mamm, !horn v mag: ");
		unicorn.printKB();
		System.out.println();
		System.out.print("Query = myth: ");
		new Clause(1).printClause();
		System.out.println();
		System.out.println("   KB |= myth: " + unicorn.entails(new KnowledgeBase(new Clause(1))));

		System.out.print("KB: ");
		unicorn.printKB();
		System.out.println();
		System.out.print("Query = mag: ");
		new Clause(5).printClause();
		System.out.println();
		System.out.println("   KB |= mag: " + unicorn.entails(new KnowledgeBase(new Clause(5))));

		System.out.print("KB: ");
		unicorn.printKB();
		System.out.println();
		System.out.print("Query = horn: ");
		new Clause(4).printClause();
		System.out.println();
		System.out.println("   KB |= horn: " + unicorn.entails(new KnowledgeBase(new Clause(4))));
	}

	public static void prob1(Scanner input)
	{
		String grant = "Y";
		while (grant.equals("Y") || grant.equals("y"))
		{
			System.out.println("=====PROBLEM 1=====");
			System.out.println("Please input the desired value of p. Close to 0.5 recommended.");
			double p = input.nextDouble();
			System.out.println("Please input the desired value of maxFlips. 100 or less recommended.");
			int maxFlips = input.nextInt();
			System.out.println("Please input the desired value of maxTries. 100 or less recommended.");
			int maxTries = input.nextInt();

			KnowledgeBase prob1 = new KnowledgeBase(new Clause(4));
			Clause p1a = new Clause(1);
			p1a.addLiteral(3);
			p1a.addLiteral(-4);
			Clause p1c = new Clause(2);
			p1c.addLiteral(-3);
			prob1.addClause(p1a);
			prob1.addClause(p1c);
			System.out.print("Problem 1: ");
			prob1.printKB();
			System.out.println();
			PossibleWorld check = prob1.walkSAT(prob1, p, maxFlips, maxTries);
			if (check != null)
			{
				check.printPW();
				System.out.println();
			}
			System.out.println("Run problem again with new settings? Input Y or N.");
			grant = input.next();
		}
		System.out.println();
	}
	public static void prob2(Scanner input)
	{
		String grant = "Y";
		while (grant.equals("Y") || grant.equals("y"))
		{
			System.out.println("=====N-QUEENS=====");
			System.out.println("Please input the desired value of p. Close to 0.5 recommended.");
			double p = input.nextDouble();
			System.out.println("Please input the desired value of maxFlips. 500-750 recommended.");
			int maxFlips = input.nextInt();
			System.out.println("Please input the desired value of maxTries. 500-750 recommended.");
			int maxTries = input.nextInt();
			System.out.println("Please input the desired value of N. Use multiples of 4 from 4 to 16; up to 4 recommended.");
			int n = input.nextInt();

			KnowledgeBase nQueens = new KnowledgeBase();
			nQueens.addInput("cnf\\nqueens\\nqueens_" + n + ".cnf");
			System.out.print("nqueens_" + n + ".cnf: ");
			PossibleWorld check = nQueens.walkSAT(nQueens, p, maxFlips, maxTries);
			if (check != null)
			{
				check.printPW();
				System.out.println();
			}
			System.out.println("Run problem again with new settings? Input Y or N.");
			grant = input.next();
		}
		System.out.println();
	}
	public static void prob3(Scanner input)
	{
		String grant = "Y";
		while (grant.equals("Y") || grant.equals("y"))
		{
			System.out.println("=====PIGEONHOLE=====");
			System.out.println("Please input the desired value of p. Close to 0.5 recommended.");
			double p = input.nextDouble();
			System.out.println("Please input the desired value of maxFlips. 500-750 recommended.");
			int maxFlips = input.nextInt();
			System.out.println("Please input the desired value of maxTries. 500-750 recommended.");
			int maxTries = input.nextInt();
			System.out.println("Please input the desired number of pigeons/holes. Maximum 20; up to 5 recommended.");
			int n = input.nextInt();

			KnowledgeBase pigeonhole = new KnowledgeBase();
			pigeonhole.addInput("cnf\\pigeonhole\\pigeonhole_" + n + "_" + n + ".cnf");
			System.out.print("pigeonhole_" + n + "_" + n + ".cnf: ");
			PossibleWorld check = pigeonhole.walkSAT(pigeonhole, p, maxFlips, maxTries);
			if (check != null)
			{
				check.printPW();
				System.out.println();
			}
			System.out.println("Run problem again with new settings? Input Y or N.");
			grant = input.next();
		}
		System.out.println();
	}
}
