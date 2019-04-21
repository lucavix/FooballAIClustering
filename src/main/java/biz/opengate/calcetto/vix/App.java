package biz.opengate.calcetto.vix;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * Hello world!
 *
 */
public class App {
	final static Player[] PLAYERS = new Player[] { 
			new Player("L'aura", "Difesa", 5.0),
			new Player("Lightman", "Difesa", 8.0), 
			new Player("LaPomè", "Duttile", 6.0),
			new Player("L'Ale", "Attacco", 10.0), 
			new Player("LoVix", "Duttile", 10.0),
			new Player("La Lady B.", "Difesa", 4.0), 
			new Player("Il Valca", "Difesa", 7.0),
			new Player("Vincenzì", "Duttile", 5.0), 
			new Player("Cobalto", "Duttile", 5.0),
			new Player("Mr Fats", "Attacco", 8.0), 
			new Player("L'ocatelli", "Duttile", 7.0),
			new Player("Cinzia", "Duttile", 7.0), 
			new Player("Dux", "Difesa", 8.0), 
			new Player("Fale", "Duttile", 6.0),
			new Player("Sara", "Duttile", 7.0), 
			new Player("Presidente", "Duttile", 4.0)
		};
	
	final static int[] INDEXES = new int[PLAYERS.length];
	static {
		for (int i = 0; i < INDEXES.length; i++)
			INDEXES[i] = i;
	}

	private final static Double PENALTY_FOR_SAME_ROLE = 10d;

	private static Double score(int[] input) {
		double res = 0.0;
		double max = 0.0;
		double min = 20.0;
		for (int i = 0; i < input.length; i += 2) {
			Player a = PLAYERS[input[i]];
			Player b = PLAYERS[input[i + 1]];
			String roleA = a.role;
			String roleB = b.role;
			if (roleA.equals(roleB) && !roleA.equals("Duttile")) {
				res += PENALTY_FOR_SAME_ROLE;
			}
			Double s = a.score + b.score;
			if (s > max)
				max = s;
			if (s < min)
				min = s;

		}

		res = res + (max - min) * (max - min);
		return res;
	}

	static Double bestScore = 10000d;
	static int[] best = null;

	private static void prettyPrint(int[] sol) {
		for (int i = 0; i < sol.length; i += 2) {
			System.out.print("[ (" + (PLAYERS[sol[i]].score + PLAYERS[sol[i + 1]].score) + ") - " + PLAYERS[sol[i]].name
					+ "," + PLAYERS[sol[i + 1]].name + "]");
		}
		System.out.println("");
	}

	private static void descend(int[] sol) {
		Double actualScore = score(sol);
		
		//Se il punteggio attuale è peggiore di quello migliore già trovato
		//inutile proseguire su questa strada... può solo peggiorare
		if (actualScore > bestScore)
			return;
		

		//Se la soluzione contiene tutti i giocatori è una soluzione finale,
		//e non parziale
		if (sol.length == INDEXES.length) {
			//Se il punteggio è migliore del migliore trovato fino a ad ora
			//prendo questa soluzione come migliore fino ad ora.
			if (actualScore < bestScore) {
				bestScore = actualScore;
				best = sol.clone();
			}
			return;
		}
		
		
		//Per le soluzioni parziali guardo quali giocatori devo
		//ancora inserire, tra questi scelgo tutte le coppie possibili e
		//creo una nuova soluzione composta dalla soluzione attuale più
		//una coppia e discendo ricorsivamente.
		int[] remaining = ArrayUtils.removeAll(INDEXES, sol);
		Iterator<int[]> it = CombinatoricsUtils.combinationsIterator(remaining.length, 2);
		while (it.hasNext()) {
			int[] newTeam = it.next();
			int[] newSol = ArrayUtils.add(sol, remaining[newTeam[0]]);
			newSol = ArrayUtils.add(newSol, remaining[newTeam[1]]);

			descend(newSol);
		}
	}
	// https://en.wikipedia.org/wiki/Branch_and_bound
	public static void main(String[] args) {
		int n = PLAYERS.length;
		int k = 4;
		Iterator<int[]> it = CombinatoricsUtils.combinationsIterator(n, k);
		while (it.hasNext()) {
			int[] sol = it.next();
			descend(sol);
		}
		System.out.println("Best Score: " + bestScore);
		System.out.println(Arrays.toString(best));
		prettyPrint(best);
	}

}
