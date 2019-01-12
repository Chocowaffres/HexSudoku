package hexsudoku;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/*
 * per box formula
 * this formula goes through each box instead of the natural order
 * ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3)
 *
 * get box origin formula
 * this formula gives the index of the origin of the box that contains index i (0-80)
 * ((i % 9) / 3) * 3 + (i / 27) * 27
 * 
 * get row origin formula
 * this formula gives the index of the origin of the row that contains index i (0-80)
 * (i / 9) * 9
 *
 * get column origin formula
 * this formula gives the index of the origin of the column that contains index i (0-80)
 * i % 9
 *
 * get box origin formula
 * this formula gives the index of origin of box // i (0-8)
 * (i * 3) % 9 + ((i * 3) / 9) * 27
 *
 * get row origin formula
 * this formula gives the index of origin of row // i (0-8)
 * i*9
 *
 * get box origin formula
 * this formula gives the index of origin of column // i (0-8)
 * i
 *
 * box step formula
 * this formula runs through a box shape (i must be less than 9)
 * boxOrigin + (i / 3) * 9 + (i % 3)
 *
 * row step formula
 * rowOrigin + i
 *
 * col step formula
 * colOrigin + i*9
 */
public class SudokuGridGenerator {

    private int[] grid;

    /**
     * Generates a valid 16 by 16 Sudoku grid with 1 through 16 appearing only
     * once in every box, row, and column
     *
     * @return an array of size 256 containing the grid
     */
    public int[] generateGrid() {     // Array para preencher cada quadradinho
        ArrayList<Integer> arr = new ArrayList<Integer>(16);
        // Sudoku
        grid = new int[256];
        // Números válidos
        for (int i = 1; i <= 16; i++) {
            arr.add(i);
        }

        // Carregar todas as caixas com os números válidos
        for (int i = 0; i < 256; i++) {
            // Se for uma caixa nova
            if (i % 16 == 0) {
                Collections.shuffle(arr);
            }
            // Posições correspondentes a cada caixa
            int perBox = ((i / 4) % 4) * 16 + ((i % 64) / 16) * 4 + (i / 64) * 64 + (i % 4);
            // Preencher sem repetir números dentro da caixa
            grid[perBox] = arr.get(i % 16);
        }
        //printGrid(grid);
        // Regista as linhas e as colunas que já foram arranjadas
        boolean[] sorted = new boolean[256];

        for (int i = 0; i < 16; i++) {
            boolean backtrack = false;
            // 0 é linha, 1 é coluna
            for (int a = 0; a < 2; a++) {
                // Registar todos os números que foram encontrados
                boolean[] registered = new boolean[17];
                // index 0 will intentionally be left empty since there are only number 1-16.
                int rowOrigin = i * 16;
                int colOrigin = i;

                ROW_COL:
                for (int j = 0; j < 16; j++) {
                    //row/column stepping - making sure numbers are only registered once and marking which cells have been sorted
                    int step = (a % 2 == 0 ? rowOrigin + j : colOrigin + j * 16);
                    int num = grid[step];

                    if (!registered[num]) {
                        registered[num] = true;
                    } else //if duplicate in row/column
                    {
                        //box and adjacent-cell swap (BAS method)
                        //checks for either unregistered and unsorted candidates in same box,
                        //or unregistered and sorted candidates in the adjacent cells
                        for (int y = j; y >= 0; y--) {
                            int scan = (a % 2 == 0 ? i * 16 + y : i + 16 * y);
                            if (grid[scan] == num) {
                                //box stepping
                                for (int z = (a % 2 == 0 ? (i % 4 + 1) * 4 : 0); z < 16; z++) {
                                    if (a % 2 == 1 && z % 4 <= i % 4) {
                                        continue;
                                    }
                                    int boxOrigin = ((scan % 16) / 4) * 4 + (scan / 64) * 64;
                                    int boxStep = boxOrigin + (z / 4) * 16 + (z % 4);
                                    int boxNum = grid[boxStep];
                                    if ((!sorted[scan] && !sorted[boxStep] && !registered[boxNum])
                                            || (sorted[scan] && !registered[boxNum] && (a % 2 == 0 ? boxStep % 16 == scan % 16 : boxStep / 16 == scan / 16))) {
                                        grid[scan] = boxNum;
                                        grid[boxStep] = num;
                                        registered[boxNum] = true;
                                        continue ROW_COL;
                                    } else if (z == 15) //if z == 15, then break statement not reached: no candidates available
                                    {
                                        //Preferred adjacent swap (PAS)
                                        //Swaps x for y (preference on unregistered numbers), finds occurence of y
                                        //and swaps with z, etc. until an unregistered number has been found
                                        int searchingNo = num;

                                        //noting the location for the blindSwaps to prevent infinite loops.
                                        boolean[] blindSwapIndex = new boolean[256];

                                        //loop of size 32 to prevent infinite loops as well. Max of 32 swaps are possible.
                                        //at the end of this loop, if continue or break statements are not reached, then
                                        //fail-safe is executed called Advance and Backtrack Sort (ABS) which allows the 
                                        //algorithm to continue sorting the next row and column before coming back.
                                        //Somehow, this fail-safe ensures success.
                                        for (int q = 0; q < 32; q++) {
                                            SWAP:
                                            for (int b = 0; b <= j; b++) {
                                                int pacing = (a % 2 == 0 ? rowOrigin + b : colOrigin + b * 16);
                                                if (grid[pacing] == searchingNo) {
                                                    int adjacentCell = -1;
                                                    int adjacentNo = -1;
                                                    int decrement = (a % 2 == 0 ? 16 : 1);

                                                    for (int c = 1; c < 4 - (i % 4); c++) {
                                                        adjacentCell = pacing + (a % 2 == 0 ? (c + 1) * 16 : c + 1);

                                                        //this creates the preference for swapping with unregistered numbers
                                                        if ((a % 2 == 0 && adjacentCell >= 256)
                                                                || (a % 2 == 1 && adjacentCell % 16 == 0)) {
                                                            adjacentCell -= decrement;
                                                        } else {
                                                            adjacentNo = grid[adjacentCell];
                                                            if (i % 4 != 0
                                                                    || c != 1
                                                                    || blindSwapIndex[adjacentCell]
                                                                    || registered[adjacentNo]) {
                                                                adjacentCell -= decrement;
                                                            }
                                                        }
                                                        adjacentNo = grid[adjacentCell];

                                                        //as long as it hasn't been swapped before, swap it
                                                        if (!blindSwapIndex[adjacentCell]) {
                                                            blindSwapIndex[adjacentCell] = true;
                                                            grid[pacing] = adjacentNo;
                                                            grid[adjacentCell] = searchingNo;
                                                            searchingNo = adjacentNo;

                                                            if (!registered[adjacentNo]) {
                                                                registered[adjacentNo] = true;
                                                                continue ROW_COL;
                                                            }
                                                            break SWAP;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //begin Advance and Backtrack Sort (ABS)
                                        backtrack = true;
                                        break ROW_COL;
                                    }
                                }
                            }
                        }
                    }
                }

                if (a % 2 == 0) {
                    for (int j = 0; j < 16; j++) {
                        sorted[i * 16 + j] = true; //setting row as sorted
                    }
                } else if (!backtrack) {
                    for (int j = 0; j < 16; j++) {
                        sorted[i + j * 16] = true; //setting column as sorted
                    }
                } else //reseting sorted cells through to the last iteration
                {
                    backtrack = false;
                    for (int j = 0; j < 16; j++) {
                        sorted[i * 16 + j] = false;
                    }
                    for (int j = 0; j < 16; j++) {
                        sorted[(i - 1) * 16 + j] = false;
                    }
                    for (int j = 0; j < 16; j++) {
                        sorted[i - 1 + j * 16] = false;
                    }
                    i -= 2;
                }
            }
        }

        if (!isPerfect(grid)) {
            return generateGrid();
        } else {
            return grid;
        }
    }

    /**
     * Prints a visual representation of a 16x16 Sudoku grid
     *
     * @param grid an array with length 256 to be printed
     */
    public static void printGrid(int[] grid) {
        if (grid.length != 256) {
            throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");
        }
        for (int i = 0; i < 256; i++) {
            System.out.print("[" + grid[i] + "] " + (i % 16 == 15 ? "\n" : ""));
        }
    }

    /**
     * Tests an int array of length 256 to see if it is a valid Sudoku grid.
     * i.e. 1 through 16 appearing once each in every row, column, and box
     *
     * @param grid an array with length 256 to be tested
     * @return a boolean representing if the grid is valid
     */
    public static boolean isPerfect(int[] grid) {
        if (grid.length != 256) {
            throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");
        }

        //tests to see if the grid is perfect
        //for every box
        for (int i = 0; i < 16; i++) {
            boolean[] registered = new boolean[17];
            registered[0] = true;
            int boxOrigin = (i * 4) % 16 + ((i * 4) / 16) * 64;
            for (int j = 0; j < 16; j++) {
                int boxStep = boxOrigin + (j / 4) * 16 + (j % 4);
                int boxNum = grid[boxStep];
                registered[boxNum] = true;
            }
            for (boolean b : registered) {
                if (!b) {
                    return false;
                }
            }
        }

        //for every row
        for (int i = 0; i < 16; i++) {
            boolean[] registered = new boolean[17];
            registered[0] = true;
            int rowOrigin = i * 16;
            for (int j = 0; j < 16; j++) {
                int rowStep = rowOrigin + j;
                int rowNum = grid[rowStep];
                registered[rowNum] = true;
            }
            for (boolean b : registered) {
                if (!b) {
                    return false;
                }
            }
        }

        //for every column
        for (int i = 0; i < 16; i++) {
            boolean[] registered = new boolean[17];
            registered[0] = true;
            int colOrigin = i;
            for (int j = 0; j < 16; j++) {
                int colStep = colOrigin + j * 16;
                int colNum = grid[colStep];
                registered[colNum] = true;
            }
            for (boolean b : registered) {
                if (!b) {
                    return false;
                }
            }
        }

        return true;
    }

    private ArrayList<Integer> emptycells = new ArrayList<>();
    
    public ArrayList<Integer> getEmptyCells() {
        return this.emptycells;
    }
            
    public int[] generateGridPresent(int[] iaSudokuGrid, int mode) {

        int[] iaGridPresent = Arrays.copyOf(iaSudokuGrid, iaSudokuGrid.length);
        int tot = 50;
        switch (mode) {
            case 0:
                tot = 80;
                break;
            case 1:
                tot = 115;
                break;
            case 2:
                tot = 150;
                break;
        }
        int count = 0;
        while (count < tot) {
            count += removeCell(iaGridPresent);
        }
        System.out.println("COUNT ------------------------------------"+count);
        return iaGridPresent;
    }

    // Check if a number is valid for a certain cell
    // value is between 1 and 16
    private boolean validCell(int row, int col, int val, int[] board) {
        // Empty cell
        if (val == 0) {
            return true;
        }

        // check within the same row
        int rowOrigin = row * 16;
        for (int j = 0; j < 16; j++) {
            // skip itself
            if (j == col) {
                continue;
            }

            if (board[rowOrigin + j] == val) {
                return false;
            }
        }

        // check within the same column
        for (int i = 0; i < 16; i++) {
            // skip itself
            if (i == row) {
                continue;
            }

            if (board[i * 16 + col] == val) {
                return false;
            }

        }

        // check within the same square
        int sqrow = row / 4;
        int sqcol = col / 4;
        for (int i = sqrow * 4; i < (sqrow * 4) + 4; i++) {
            // skip the row which has already been checked
            if (i == row) {
                continue;
            }
            for (int j = sqcol * 4; j < (sqcol * 4) + 4; j++) {
                // skip the column already checked
                if (j == col) {
                    continue;
                }
                if (board[i * 16 + j] == val) {
                    return false;
                }

            }
        }

        return true;

    }

// Exhaustive backtracking algorithm
// from https://www.youtube.com/watch?v=NJsZlobsQ9Y
    private boolean backtrackExhaustive(int[] board) {
        // Copy the board
        int[] boardc = Arrays.copyOf(board, board.length);

        int i = 0;
        // Go through all empty cell positions
        while (i < emptycells.size()) {
            int pos = emptycells.get(i);
            int val = boardc[pos];
            val += 1;

            while (true) {
                // Check if bigger than 16
                if (val > 16 && i == 0) // The sudoku cannot be completed
                {
                    return false;
                } else if (val > 16) {
                    // clear cell
                    boardc[pos] = 0;
                    // backtrack to previous cell
                    i -= 1;
                    break;
                }
                // Check if valid
                if (validCell(pos / 16, pos % 16, val, boardc)) {
                    boardc[pos] = val;
                    i += 1;
                    break;
                } else // increment value
                {
                    val += 1;
                }
            }
        }
        return true;
    }

// Backtracking algorithm
// created based on https://www.sudoku-solutions.com/
    private boolean backtrack(int[] board) {
        ArrayList<ArrayList<Integer>> candidates = new ArrayList<>();

        int[] boardc = Arrays.copyOf(board, board.length);

        // Get the candidates
        for (int k = 0; k < emptycells.size(); k++) {
            int[] cdd = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            int pos = emptycells.get(k);
            int row = pos / 16;
            int col = pos % 16;
            // check within the same row
            for (int j = 0; j < 16; j++) {
                // skip empty cells
                if (boardc[row * 16 + j] == 0) {
                    continue;
                }
                // remove the candidate
                cdd[boardc[row * 16 + j] - 1] = 0;
            }
            // check within the same column
            for (int i = 0; i < 16; i++) {
                // skip empty cells
                if (boardc[i * 16 + col] == 0) {
                    continue;
                }
                // remove the candidate
                cdd[boardc[i * 16 + col] - 1] = 0;
            }
            // check within the same square
            int sqrow = row / 4;
            int sqcol = col / 4;
            for (int i = sqrow * 4; i < (sqrow * 4) + 4; i++) {
                // skip the row which has already been checked
                if (i == row) {
                    continue;
                }
                for (int j = sqcol * 4; j < (sqcol * 4) + 4; j++) {
                    // skip empty cells
                    if (boardc[i * 16 + j] == 0) {
                        continue;
                    }
                    cdd[boardc[i * 16 + j] - 1] = 0;
                }
            }

            ArrayList<Integer> cdds = new ArrayList<>();
            // Get the candidates
            for (int idx = 0; idx < 16; idx++) {
                if (cdd[idx] == 1) {
                    cdds.add(idx + 1);
                }

            }
            // Check if no candidates are available for a certain cell
            if (cdds.isEmpty()) {
                return false;
            }

            candidates.add(cdds);
        }// End of - Get the candidates

        // Process candidate cells
        boolean done = false;
        boolean noOnes = false;
        ArrayList<Integer> ec = new ArrayList<>(emptycells);
        int k = 0;
        while (!done) {
            // Reached the end
            if (candidates.isEmpty()) {
                done = true;
            } else if (k == candidates.size()) {
                noOnes = true;
                // check again for single candidate cells
                for (int ks = 0; ks < candidates.size(); ks++) {
                    if (candidates.get(ks).isEmpty() || candidates.get(ks).size() == 1) {
                        k = ks;
                        noOnes = false;
                        break;
                    } else {
                        k = 0;
                    }
                }
            } // Cell without any candidates
            else if (candidates.get(k).isEmpty()) {
                return false;
            } // Cells with a single candidate
            else if (candidates.get(k).size() == 1) {
                int pos = ec.get(k);
                // update the cell on the board
                int val = candidates.get(k).get(0);
                boardc[pos] = val;
                int row = pos / 16;
                int col = pos % 16;
                // parameters for the square which the cell belongs to
                int sqrowmin = row / 4;
                int sqcolmin = col / 4;
                int sqrowmax = sqrowmin + 3;
                int sqcolmax = sqcolmin + 3;
                // update other cells that can be affected by this change
                for (int ks = 0; ks < ec.size(); ks++) {
                    int p = ec.get(ks);
                    int r = p / 16;
                    int c = p % 16;
                    // skip itself
                    if (r == row && c == col) {
                        continue;
                    } // is affected, within the same row, column or square
                    else if (r == row || c == col || (r >= sqrowmin && r <= sqrowmax && c >= sqcolmin && c <= sqcolmax)) {
                        // remove the candidate
                        for (int ksc = 0; ksc < candidates.get(ks).size(); ksc++) {
                            if (candidates.get(ks).get(ksc) == val) {
                                candidates.get(ks).remove(ksc);
                                break;
                            }
                        }
                    }
                }
                // remove cell from the lists
                ec.remove(k);
                candidates.remove(k);
            } else {
                // If there are no cells with only one candidate
                // meaning that there could be multiple solutions
                // as it requires more complex plays
                if (noOnes) {
                    return false;
                } else {
                    // if there are cells with 2 or more candidates
                    k += 1;
                }
            }
            // End of - Process candidate cells
        }
        return true;
    }

// Random cell remover
// returns 0 if it couldn't make changes to the board
// returns 1 if it could
    private int removeCell(int[] board) {
        // Generate a random position
        int pos = (int) ((Math.random() * (256 - 0)) + 0);
        // If cell is already empty
        if (board[pos] == 0) // Could not make changes
        {
            return 0;
        }

        // keep the value if the sudoku is no longer completable
        int val = board[pos];

        // add cell to the empty cell list
        emptycells.add(pos);
        // remove the number
        board[pos] = 0;

        // backtrack to check if the sudoku is still completable
        boolean bt = backtrack(board);

        if (!bt) {
            board[pos] = val;
            emptycells.remove(emptycells.size() - 1);
            return 0;
        }
        // Reallocate the value

        return 1;
    }
}
