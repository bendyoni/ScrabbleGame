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

       // if (null == GameBoard[7][7]) {
       //     return word.getRow() == 7 && word.getCol() == 7;
       // }

        for (Tile tile : tiles) {
            int currentRow = vertical ? row : row + (col - word.getCol());
            int currentCol = vertical ? col + (row - word.getRow()) : col;
           
            // Checks if the user is trying to use a existing letter on the board
            if (tile == null && GameBoard[currentRow][currentCol] == null)
                return false;
            // Tile currentTile = GameBoard[currentRow][currentCol];
            // if (currentTile != null)
            //     if (tile.letter != currentTile.letter)
            //         return false;    
            
///////////////////////////////////////////////////////////////////////////
           /*
            if (GameBoard[currentRow][currentCol] == null) {
                boolean anchored = false;
                if (vertical) {
                    anchored = (currentCol > 0 && GameBoard[currentRow][currentCol - 1] != null) ||
                               (currentCol < 14 && GameBoard[currentRow][currentCol + 1] != null);
                } else {
                    anchored = (currentRow > 0 && GameBoard[currentRow - 1][currentCol] != null) ||
                               (currentRow < 14 && GameBoard[currentRow + 1][currentCol] != null);
                }
                if (!anchored) {
                    return false;
                }
                
                
            }
            
            if (vertical) {
                row++;
            } else {
                col++;
            } 
            
            ////////////////////////////////////////////////////////////////////
            */
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


       public ArrayList<Word> getWords(Word word){
       ArrayList<Word> wordsArr = new ArrayList<>();
       Tile[] tiles = word.getTiles();
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       int r;
       int c;

       wordsArr.add(word); //Add the current word to the list

       for (int i=0; i<tiles.length; i++) {
            r = vertical ? row + i : row;
            c = vertical ? col : col + i;

            if(vertical) {
                checkVerticalWords(wordsArr, r, c, GameBoard[r][c]);
                col++;
            }
             else {
                 checkBalancedWords(wordsArr, r, c, GameBoard[r][c]);
                 row++;
            }
         }
        
        return wordsArr;
    }

    private void checkVerticalWords(ArrayList<Word> wordsArr, int row, int col, Tile tile) {
        // search for new vertical words form the given tile
        StringBuilder sb = new StringBuilder();
        int startRow = row;
        while (0 <= startRow && GameBoard[startRow][col] != null) {
            startRow--;
        }
        startRow++;

        int endRow = row;
        while (endRow < 15 && GameBoard[endRow][col] != null) {
            endRow++;
        }

        for (int r = startRow; r < endRow; r++) {
            sb.append(GameBoard[r][col].letter);
        }

        if (1 < sb.length()) {
            wordsArr.add(new Word(getTilesFromString(sb.toString()), startRow, col, true));
        }
    }

    private void checkBalancedWords(ArrayList<Word> wordsArr, int row, int col, Tile tile) {
        // search for new horizontal words formed by the given tile
        StringBuilder sb = new StringBuilder();
        int startCol = col;
        while (0 <= startCol && GameBoard[row][startCol] != null) {
            startCol--;
        }
        startCol++;

        int endCol = col;
        while (endCol < 15 && GameBoard[row][endCol] != null) {
            endCol++;
        }

        for (int c = startCol; c < endCol; c++) {
            sb.append(GameBoard[row][c].letter);
        }

        if (sb.length() > 1) {
            wordsArr.add(new Word(getTilesFromString(sb.toString()), row, startCol, false));
        }
    }

    private Tile[] getTilesFromString(String str) {
        // Convert a string to an array of Tile objects
        Tile[] tiles = new Tile[str.length()];
        for (int i = 0; i < str.length(); i++) {
            tiles[i] = Tile.Bag.getBag().getTile(str.charAt(i));
        }
        return tiles;
    }

    public String print(){
        String str = "";
        for (int i = 0; i < 15; i++) str += i;
        str += "\n\n";
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
                tiles[i] = GameBoard[r][c];
                //continue;
            //score += GameBoard[r][c].score;

            if (r == 7 && c == 7) {
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
            if (r == 1 && c == 5 || r == 1 && c == 9 ||
                r == 5 && c == 1 || r == 5 && c == 5 ||  r == 5 && c == 9 || r == 5 && c == 13 ||
                r == 9 && c == 1 || r == 9 && c == 5 ||  r == 9 && c == 9 || r == 9 && c == 13 ||
                r == 13 && c == 5 || r == 13 && c == 9) {
                score += tiles[i].score * 3; // Triple letter score - blue
            }

            if (r == 1 && c == 1 || r == 1 && c == 13 ||
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

            if (r == 0 && c == 0 || r == 0 && c == 7 || r == 0 && c == 14 ||
                r == 7 && c == 0 || r == 7 && c == 14 ||
                r == 14 && c == 0 || r == 14 && c == 7 || r == 14 && c == 14) {
                doublingWord *= 3; // Triple word score - red
                score += tiles[i].score;
            }

           
            else {
                score += tiles[i].score;
            }
/*
            if (!vertical) {
                row++;
            } else {
                col++;
            }*/
        }
        score *= doublingWord;

        return score;
    }
        
 public int tryPlaceWord(Word word) {
        // Try to place the word on the board and calculate the total score
        if (!boardLegal(word) || !dictionaryLegal(word)) {
            return 0; // Word placement is illegal
        }
        int score = 0;
        score += getScore(word);
        int row = word.getRow();
        int col = word.getCol();
        //Tile t;
        
        // Place the tile on the board
        for (int i=0; i < word.getTiles().length; i++){
            if (word.tiles[i] != null){
                //t = (Tile)Tile.Bag.getBag().getTile(word.getTiles()[i].letter);
                //t = word.getTiles()[i];
                //GameBoard[row][col] = t;
                //Board.getBoard().getTiles()[row][col] = t;
                GameBoard[row][col] = word.tiles[i];    //למה זה לא מכניס לי את המילה???????
            }
            
            if (word.isVertical())
                row++;
            else
                col++;
        }

        return score;
    }    
}



       

	


   


   
    