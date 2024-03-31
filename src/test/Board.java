package test;

import java.util.ArrayList;

public class Board {
    private static Board b = null; // for singleton
    Tile[][] GameBoard = new Tile[15][15];
   
    private Board()
    {
        for (int i=0; i<15; i++)
            for (int j=0; j<15; j++)
                 GameBoard[i][j] = null;
    }


    public static Board getB() {
        return b;
    }
    
    public Tile[][] getTiles() {
        return GameBoard;
    }


    // gets the Board (Board is singleton)
	public static Board getBoard() { 
		if (null == b)
			b = new Board();
		return b;
				
	}

    public boolean boardLegal(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        Tile[] tiles = word.getTiles();

        int r = 0;
        int c = 0;

       for (int i=1; i<tiles.length+1; i++) {
        r = vertical ? row + i : row;
        c = vertical ? col : col + i;
       }
       if (r < 0 || r >= 15 || c < 0 || c >= 15 || row < 0 || 15 <= row || col < 0 || 15 <= col) {
        return false; // Out of bounds
    }
    
        // checks if first Tile is on the star square
        if (null == GameBoard[7][7]) {
            if (vertical && (col != 7 || (row + r <= 7 && 7 <= r - row) ))
                return false; 
            if (!vertical && (row != 7 || (col + c <= 7 && 7 <= c - col)))  
                return false;
        }

        for (int i=0; i<tiles.length; i++) {
            r = vertical ? row + i : row;
            c = vertical ? col : col + i;
           
            // Checks if the user is trying to use a existing letter on the board
            if (word.tiles[i] == null && GameBoard[r][c] == null)
                return false;   
        } 
        return true;
    }

       public boolean dictionaryLegal(Word word){
        /*
         Set<String> seenWords = new HashSet<>();
          String str = new String(getTilesAsString(w.getTiles()));
          if (!seenWords.contains(str) && dictionaryLegal(w)) {
             seenWords.add(str);
            }
         */
        return true;
       }

       /*
        private String getTilesAsString(Tile[] tiles) {
        // Convert an array of Tile objects to a string
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles) {
            sb.append(tile.letter);
        }
        return sb.toString();
    }
        */



        public ArrayList<Word> getWords(Word word) {
            ArrayList<Word> words = new ArrayList<>();
            int row = word.getRow();
            int col = word.getCol();
            Tile[] tiles = word.getTiles();
            boolean vertical = word.isVertical();
        
            words.add(word);
        
            // Check for existing words that intersect with the placed tiles
            for (int i = 0; i < tiles.length; i++) {
                int r = vertical ? row + i : row;
                int c = vertical ? col : col + i;
        
            if (tiles[i] == null) continue; // we're using a preexisting letter, not creating new words

                addWordIfValid(words, tiles[i], r, c, vertical);
            }
        
            return words;
        }   
    
        private void addWordIfValid(ArrayList<Word> words, Tile tile, int row, int col, boolean vertical) {
            ArrayList<Tile> wordTilesTopBottom = new ArrayList<>();
            ArrayList<Tile> wordTilesLeftToRight = new ArrayList<>();

            int r = row;
            int c = col;

            if (!vertical) { // if we're vertical we don't want to add vertical words - they're our new word!
                // Only add horizontals
                wordTilesTopBottom.add(tile);
                while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r + 1][c] != null) {
                    r++;
                    wordTilesTopBottom.add(GameBoard[r][c]);
                }
                r = row;
                while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r - 1][c] != null) {
                    r--;
                    wordTilesTopBottom.add(0, GameBoard[r][c]);
                }

                if (wordTilesTopBottom.size() > 1) {
                    Tile[] wordTilesArray = wordTilesTopBottom.toArray(new Tile[0]);
                    Word newWord = new Word(wordTilesArray, r, c, true);
                    if (dictionaryLegal(newWord)) {
                        words.add(newWord);
                    }
                }
            } else { // if our new word is horizontal, only add verticals
                wordTilesLeftToRight.add(tile);
                while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r][c + 1] != null) {
                    c++;
                    wordTilesLeftToRight.add(GameBoard[r][c]);
                }
                c = col;
                while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r][c - 1] != null) {
                    c--;
                    wordTilesLeftToRight.add(0, GameBoard[r][c]);
                }


                if (wordTilesLeftToRight.size() > 1) {
                    Tile[] wordTilesArray = wordTilesLeftToRight.toArray(new Tile[0]);
                    Word newWord = new Word(wordTilesArray, r, c, false);
                    if (dictionaryLegal(newWord)) {
                        words.add(newWord);
                    }
                }
            }
        }
    
    public int getScore(Word word) {
        // Calculate the score for the given word, considering bonus squares
        int score = 0;
        int doublingWord = 1;
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        Tile[] tiles = word.getTiles();

        for (int i=0; i<tiles.length; i++) {
            int r = vertical ? row + i : row;
            int c =vertical ? col : col + i;
            
            if (tiles[i] == null)
            {
                tiles[i] = GameBoard[r][c];
            }

            if (r == 7 && c == 7 && GameBoard[7][7] == null) {
                doublingWord *= 2; // Center square (double word score) - star
            }
            if (r == 0 && c == 3 || r == 0 && c == 11 ||
                r == 2 && c == 6 || r == 2 && c == 8 ||
                r == 3 && c == 0 || r == 3 && c == 7 || r == 3 && c == 14 ||
                r == 6 && c == 2 || r == 6 && c == 6 ||  r == 6 && c == 8 ||  r == 6 && c == 12 ||
                r == 7 && c == 3 || r == 7 && c == 11 ||
                r == 8 && c == 2 || r == 8 && c == 6 ||  r == 8 && c == 8 ||  r == 8 && c == 12 ||
                r == 11 && c == 0 || r == 11 && c == 7 || r == 11 && c == 14 ||
                r == 12 && c == 6 || r == 12 && c == 8 ||
                r == 14 && c == 3 || r == 14 && c == 11) {
                score += tiles[i].score * 2; // Double letter score - light blue
            }
            else if (r == 1 && c == 5 || r == 1 && c == 9 ||
                r == 5 && c == 1 || r == 5 && c == 5 ||  r == 5 && c == 9 || r == 5 && c == 13 ||
                r == 9 && c == 1 || r == 9 && c == 5 ||  r == 9 && c == 9 || r == 9 && c == 13 ||
                r == 13 && c == 5 || r == 13 && c == 9) {
                score += tiles[i].score * 3; // Triple letter score - blue
            }

            else if (r == 1 && c == 1 || r == 1 && c == 13 ||
                r == 2 && c == 2 || r == 2 && c == 12 ||
                r == 3 && c == 3 || r == 3 && c == 11 ||
                r == 4 && c == 4 || r == 4 && c == 10 ||
                r == 10 && c == 4 || r == 10 && c == 10 ||
                r == 11 && c == 3 || r == 11 && c == 11 ||
                r == 12 && c == 2 || r == 12 && c == 12 ||
                r == 13 && c == 1 || r == 13 && c == 13) {
                doublingWord *= 2; // Double word score - yellow
                score += tiles[i].score;
            }

            else if (r == 0 && c == 0 || r == 0 && c == 7 || r == 0 && c == 14 ||
                r == 7 && c == 0 || r == 7 && c == 14 ||
                r == 14 && c == 0 || r == 14 && c == 7 || r == 14 && c == 14) {
                doublingWord *= 3; // Triple word score - red
                score += tiles[i].score;
            }

           
            else {
                score += tiles[i].score;
            }

        }

        score *= doublingWord;

        return score;
    }
        
 public int tryPlaceWord(Word word) {
        // Try to place the word on the board and calculate the total score
        if (!boardLegal(word) || !dictionaryLegal(word)) {
            return 0; // Word placement is illegal
        }
         
        ArrayList<Word> words = getWords(word);
        int score = 0;
        for (Word word2 : words) {
            score += getScore(word2);
        }

        int row = word.getRow();
        int col = word.getCol();
        // Place the tile on the board
        for (int i=0; i < word.getTiles().length; i++){
            if (word.tiles[i] != null){
                GameBoard[row][col] = word.tiles[i]; 
            }
            
            if (word.isVertical())
                row++;
            else
                col++;
        }

        return score;
    } 
    
    public String print(){
        String str = "";
        str += "\n\n";
        for (int i = 0; i < 15; i++) str += i;
        str += "\n";
        for (int i = 0; i < 15; i++) {
            str += i;
            str += " ";
            for (int j = 0; j < 15; j++) {
                Tile tile = GameBoard[i][j];
                if (tile == null) str += ".";
                else str += tile.letter;
            }
            str += "\n";
        }
        return str;
    }
}



       

	


   


   
    