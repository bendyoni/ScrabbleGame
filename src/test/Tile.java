package test;

import java.util.Objects;
import java.util.Random;

public class Tile {
	
	final public char letter;
	final public int score;
	
	private Tile (char letter, int score) {
		this.letter = letter;
		this.score = score;
	}

	@Override
	public int hashCode() {
		return Objects.hash(letter, score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		return letter == other.letter && score == other.score;
	}
	
	public static class Bag {
		private final int[] startNumTiles = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1}; // Initialize the amount of tiles from each letter in the bug
		int [] sumOfTiles = new int[26];
		private final int [] valueOfTiles = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10}; // Initialize the value every Tile is going to get
		Tile[] letterArr = new Tile[26];
		private static Bag b = null; // for singleton
		
		private Bag() {
			char letter = 'A'; // Start from letter 'A'
			for (int i = 0; i < valueOfTiles.length; i++) 
				letterArr[i] = new Tile(letter++, valueOfTiles[i]); // Create new Tile object with letter and score, and increment letter
			
			for (int j = 0; j<startNumTiles.length; j++) //Creates the array that will store the current amount of tiles in the bag, divided by letters
				sumOfTiles[j] = startNumTiles[j];
		}
			
		
				// Draws a random tile. If the bag is empty NULL will be returned
			public Tile getRand() {
				//--checking if the bag is empty-- 
				int counter = 0;
				for (int one : sumOfTiles)
					if (0 == one)
						counter++;
				if (26 == counter)
					return null;
				
				// if not empty:
				Random r = new Random();
				int index;
				
				do {
					index = r.nextInt(26);
				}
				while (sumOfTiles[index] == 0); 
			
				//we will get her if there is a letter in index
				sumOfTiles[index]--;
				return 	letterArr[index];
			}
		
		
			//Receives a character and returns the tile if it exists
			public Tile getTile(char c) {
				
				if (c < 'A' || c > 'Z')
					return null;
				
				int n = c - 'A'; 
				if (sumOfTiles[n] == 0) // this Tile letter is finished
					return null;
				else
					sumOfTiles[n]--;
				return letterArr[n];
			}
			
			//Receives a tile and adds it to the bag, if we have not exceeded the amount allowed for this letter
			public void put(Tile t) {
				int n = t.letter - 'A';
				 if (sumOfTiles[n] < startNumTiles[n])
					 sumOfTiles[n]++;
				 return;
			}
			
			public int size() {
				int s = 0;
				for (int one : sumOfTiles)
					s += one;
				return s;
			}
			
			public int[] getQuantities() {
				return sumOfTiles.clone();
			}
			
			// gets the bag (bag is singleton)
			public static Bag getBag() { 
				if (null == b)
					b = new Bag();
				return b;
				
			}
		}
}
