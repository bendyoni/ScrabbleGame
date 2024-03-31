package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

        for (int i=0; i<tiles.length; i++) {
            r = vertical ? row + i : row;
            c = vertical ? col : col + i;
           
            // Checks if the user is trying to use a existing letter on the board
            if (word.tiles[i] == null && GameBoard[r][c] == null)
                return false;   
            
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



        public ArrayList<Word> getWords(Word word) {
            ArrayList<Word> words = new ArrayList<>();
            int row = word.getRow();
            int col = word.getCol();
            Tile[] tiles = word.getTiles();
            boolean vertical = word.isVertical();
        
            words.add(word);
        
            // // Check for new words formed by the placed tiles
            // for (int i = 0; i < tiles.length; i++) {
            //     int r = vertical ? row + i : row;
            //     int c = vertical ? col : col + i;
        
            //     if (vertical) {
            //         addWordIfValid(words, tiles[i], r, c, false);
            //     } else {
            //         addWordIfValid(words, tiles[i], r, c, true);
            //     }
            // }
        
            // Check for existing words that intersect with the placed tiles
            for (int i = 0; i < tiles.length; i++) {
                int r = vertical ? row + i : row;
                int c = vertical ? col : col + i;
        
                // Check for horizontal words
                if (c > 0 && GameBoard[r][c - 1] != null) {
                    addWordIfValid(words, GameBoard[r][c - 1], r, c - 1, false);
                }
                if (c < 14 && GameBoard[r][c + 1] != null) {
                    addWordIfValid(words, GameBoard[r][c + 1], r, c + 1, false);
                }
        
                // Check for vertical words
                if (r > 0 && GameBoard[r - 1][c] != null) {
                    addWordIfValid(words, GameBoard[r - 1][c], r - 1, c, true);
                }
                if (r < 14 && GameBoard[r + 1][c] != null) {
                    addWordIfValid(words, GameBoard[r + 1][c], r + 1, c, true);
                }
            }
        
            return words;
        }
    
        private void addWordIfValid(ArrayList<Word> words, Tile tile, int row, int col, boolean vertical) {
            ArrayList<Tile> wordTilesDown = new ArrayList<>();
            ArrayList<Tile> wordTilesRight = new ArrayList<>();
            ArrayList<Tile> wordTilesTop = new ArrayList<>();
            ArrayList<Tile> wordTilesLeft = new ArrayList<>();
           
            wordTilesDown.add(tile);
            wordTilesRight.add(tile);
            wordTilesTop.add(tile);
            wordTilesLeft.add(tile);

            int r = row;
            int c = col;
            StringBuilder sbForword = new StringBuilder();
            StringBuilder sbBack = new StringBuilder();
            sbForword.append(tile.letter);
            sbBack.append(tile.letter);

    
            while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r][c] != null) {
                r++;
                wordTilesDown.add(GameBoard[r][c]);                   
            }

            if (wordTilesDown.size() > 1) {
                    Tile[] wordTilesArray = wordTilesDown.toArray(new Tile[0]);
                    Word newWord = new Word(wordTilesArray, vertical ? row : r, vertical ? c : col, vertical);
                    if (dictionaryLegal(newWord)) {
                        words.add(newWord);
                    }
                }
            r = row;
            c = col;

            while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r][c] != null) {
                    c++;
                    wordTilesRight.add(GameBoard[r][c]);
                }                   

            if (wordTilesRight.size() > 1) {
                    Tile[] wordTilesArray = wordTilesRight.toArray(new Tile[0]);
                    Word newWord = new Word(wordTilesArray, vertical ? row : r, vertical ? c : col, vertical);
                    if (dictionaryLegal(newWord)) {
                        words.add(newWord);
                    }
                }
    
            r = row;
            c = col;
    
            while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r][c] != null) {
                r--;
                wordTilesTop.add(GameBoard[r][c]);
            }

                if (wordTilesTop.size() > 1) {
                    Collections.reverse(wordTilesTop);
                    Tile[] wordTilesArray = wordTilesTop.toArray(new Tile[0]);
                    Word newWord = new Word(wordTilesArray, vertical ? row : r, vertical ? c : col, vertical);
                    if (dictionaryLegal(newWord)) {
                        words.add(newWord);
                    }
            }

            r = row;
            c = col;

            while (r >= 0 && r < 15 && c >= 0 && c < 15 && GameBoard[r][c] != null) {
                c--;
                wordTilesLeft.add(GameBoard[r][c]);
            }

                if (wordTilesLeft.size() > 1) {
                    Collections.reverse(wordTilesLeft);
                    Tile[] wordTilesArray = wordTilesLeft.toArray(new Tile[0]);
                    Word newWord = new Word(wordTilesArray, vertical ? row : r, vertical ? c : col, vertical);
                    if (dictionaryLegal(newWord)) {
                        words.add(newWord);
                    }
            }

        }
    

    //    public ArrayList<Word> getWords(Word word){
    //    ArrayList<Word> wordsArr = new ArrayList<>();
    //    Tile[] tiles = word.getTiles();
    //    int row = word.getRow();
    //    int col = word.getCol();
    //    boolean vertical = word.isVertical();
    //    int r;
    //    int c;

    //    wordsArr.add(word); //Add the current word to the list

    //    for (int i=0; i<tiles.length; i++) {
    //         r = vertical ? row + i : row;
    //         c = vertical ? col : col + i;

    //         if(vertical) {
    //             checkVerticalWords(wordsArr, r, c, GameBoard[r][c]);
    //             col++;
    //         }
    //          else {
    //              checkBalancedWords(wordsArr, r, c, GameBoard[r][c]);
    //              row++;
    //         }
    //      }
        
    //     return wordsArr;
    // }

    // private void checkVerticalWords(ArrayList<Word> wordsArr, int row, int col, Tile tile) {
    //     // search for new vertical words form the given tile
    //     StringBuilder sb = new StringBuilder();
    //     int startRow = row;
    //     while (0 <= startRow && GameBoard[startRow][col] != null) {
    //         startRow--;
    //     }
    //     startRow++;

    //     int endRow = row;
    //     while (endRow < 15 && GameBoard[endRow][col] != null) {
    //         endRow++;
    //     }

    //     for (int r = startRow; r < endRow; r++) {
    //         sb.append(GameBoard[r][col].letter);
    //     }

    //     if (1 < sb.length()) {
    //         wordsArr.add(new Word(getTilesFromString(sb.toString()), startRow, col, true));
    //     }
    // }

    // private void checkBalancedWords(ArrayList<Word> wordsArr, int row, int col, Tile tile) {
    //     // search for new horizontal words formed by the given tile
    //     StringBuilder sb = new StringBuilder();
    //     int startCol = col;
    //     while (0 <= startCol && GameBoard[row][startCol] != null) {
    //         startCol--;
    //     }
    //     startCol++;

    //     int endCol = col;
    //     while (endCol < 15 && GameBoard[row][endCol] != null) {
    //         endCol++;
    //     }

    //     for (int c = startCol; c < endCol; c++) {
    //         sb.append(GameBoard[row][c].letter);
    //     }

    //     if (sb.length() > 1) {
    //         wordsArr.add(new Word(getTilesFromString(sb.toString()), row, startCol, false));
    //     }
    // }

    // private Tile[] getTilesFromString(String str) {
    //     // Convert a string to an array of Tile objects
    //     Tile[] tiles = new Tile[str.length()];
    //     for (int i = 0; i < str.length(); i++) {
    //         tiles[i] = Tile.Bag.getBag().getTile(str.charAt(i));
    //     }
    //     return tiles;
    // }
    
    public int getScore(Word word) {
        // Calculate the score for the given word, considering bonus squares
        int score = 0;
        int doublingWord = 1;
        boolean newWordFlag = true;
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
                newWordFlag = false;
            }

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
        if (newWordFlag)
        score *= doublingWord;

        return score;
    }

    // public int tryPlaceWord(Word word) {
    //     if (!boardLegal(word)) {
    //         return 0;
    //     }
    
    //     ArrayList<Word> words = getWords(word);
    //     int score = 0;
    
    //     for (Word w : words) {
    //         if (!dictionaryLegal(w)) {
    //             return 0;
    //         }
    //         score += getScore(w);
    //     }
    
    //     int row = word.getRow();
    //     int col = word.getCol();
    //     Tile[] tiles = word.getTiles();
    //     boolean vertical = word.isVertical();
    
    //     for (int i = 0; i < tiles.length; i++) {
    //         int r = vertical ? row + i : row;
    //         int c = vertical ? col : col + i;
    //         GameBoard[r][c] = tiles[i];
    //     }
    
    //     return score;
    // }
        
 public int tryPlaceWord(Word word) {
        // Try to place the word on the board and calculate the total score
        if (!boardLegal(word) || !dictionaryLegal(word)) {
            return 0; // Word placement is illegal
        }
        ArrayList<Word> words;
        words = getWords(word);
        int score = 0;
        for (Word word2 : words) {
            score += getScore(word2);
        }
        //score += getScore(word);

        int row = word.getRow();
        int col = word.getCol();
        // Place the tile on the board
        for (int i=0; i < word.getTiles().length; i++){
            if (word.tiles[i] != null)
                GameBoard[row][col] = word.tiles[i];
            
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



       

	


   


   
    