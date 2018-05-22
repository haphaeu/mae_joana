import java.util.*;
 
/**
 * Peg Solitaire Solver
 * Copyright (C) 2014 blackflux.com <pegsolitaire@blackflux.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
/**
 * Solver for the English peg solitaire.
 * This program finds a random solution for peg solitaire game by using brute force.
 *
 * -- Runtime
 * A solution is typically found in less than two seconds, but the time does highly
 * fluctuate (I've seen everything from a few milliseconds to several seconds).
 *
 * -- Implementation
 *
 * The implementation is highly optimized and uses bit operators to efficiently find
 * a solution. The idea is as following: Since there exists 33 slots on the board, it
 * can not be represented by using an integer (32 bits), but we can use a long (64 bits).
 * The first 49 bits (7 x 7) of the long represent the board. However there are some bits
 * that are not valid and never used, i.e. 0,1,5,6,7,8,12,13 and so on. Checking of
 * possible moves and applying them can be done by using simple bit operations.
 *
 * A recursive function is then used to check all possible moves for a given board,
 * applying each valid move and calling itself with the resulting board. The recursion is
 * done "in reverse", starting from the goal board. While this is not conceptually faster [a],
 * it allows for a minimum amount of bit operations in the recursion:
 *
 * To reverse a move we can simply check
 * - (board & twoBalls) == 0 and
 * - (board & oneBall) != 0
 * where "twoBalls" indicates the two ball that would need to be added for this reversed move.
 * If we instead used the intuitive search direction, the same check would require additional
 * binary operations, since a simple inversion of the check would not work [b].
 *
 * Paper [1] shows how the moves can be ordered to almost instantly find a solution.
 * Website [2] gives a nice overview of binary operations and some tricks that
 * can be applied.
 *
 * [a] Playing the game in reverse is simply the inversion of the original game - just remove all
 * balls from the board and place ball where there were none before and you'll understand
 * what I mean.
 * [b] There is no "single" binary operation to check if two specific bits are set, but there
 * is one to check if they are both zero. There is further a binary operation to check if a specific
 * bit is set.
 *
 * [1] http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.6.4826 (download at the top)
 * [2] http://graphics.stanford.edu/~seander/bithacks.html
 *
 * https://blackflux.wordpress.com/2014/04/30/peg-solitaire-brute-force/
 *
 */
public class RestaUm {
 
    // list of seen boards - this is used to prevent rechecking of paths
    private static final HashSet<Long> seenBoards = new HashSet<Long>();
 
    // list of solution boards in ascending order - filled in once the solution is found
    private static final ArrayList<Long> solution = new ArrayList<Long>();
 
    // -------
 
    // goal board (one marble in center)
    private static final long GOAL_BOARD = 16777216L;
 
    // initial board (one marble free in center)
    private static final long INITIAL_BOARD = 124141717933596L;
 
    // board that contains a ball in every available slot, i.e. GOAL_BOARD | INITIAL_BOARD
    private static final long VALID_BOARD_CELLS = 124141734710812L;
 
    // holds all 76 moves that are possible
    // the inner array is structures as following:
    // - first entry holds the peg that is added by the move
    // - second entry holds the two pegs that are removed by the move
    // - third entry holds all three involved pegs
    private static final long[][] moves = new long[76][];
 
    // -------
 
    // print the board
    private static void printBoard(long board) {
        // loop over all cells (the board is 7 x 7)
        for (int i = 0; i < 49; i++) {
            boolean validCell = ((1L << i) & VALID_BOARD_CELLS) != 0L;
            System.out.print(validCell ? (((1L << i) & board) != 0L ? "X " : "O ") : "  ");
            if (i % 7 == 6) System.out.println();
        }
        System.out.println("-------------");
    }
 
    // create the two possible moves for the three added pegs
    // (this function assumes that the pegs are in one continuous line)
    private static void createMoves(int bit1, int bit2, int bit3, ArrayList<long[]> moves) {
        moves.add(new long[]{(1L << bit1), (1L << bit2) | (1L << bit3),
                (1L << bit1) | (1L << bit2) | (1L << bit3)});
        moves.add(new long[]{(1L << bit3), (1L << bit2) | (1L << bit1),
                (1L << bit1) | (1L << bit2) | (1L << bit3)});
    }
 
    // do the calculation recursively by starting from
    // the "GOAL_BOARD" and doing moves in reverse
    private static boolean search(long board) {
        // for all possible moves
        for (long[] move : moves) {
            // check if the move is valid
            // Note: we place "two ball" check first since it is more
            // likely to fail. This saves about 20% in run time (!)
            if ((move[1] & board) == 0L && (move[0] & board) != 0L) {
                // calculate the board after this move was applied
                long newBoard = board ^ move[2];
                // only continue processing if we have not seen this board before
                if (!seenBoards.contains(newBoard)) {
                    seenBoards.add(newBoard);
                    // check if the initial board is reached
                    if (newBoard == INITIAL_BOARD || search(newBoard)) {
                        solution.add(board);
                        return true;
                    }
                }
            }
        }
        return false;
    }
 
    // the main method
    public static void main(String[] args) {
        // to measure the overall runtime of the program
        long time = System.currentTimeMillis();
 
        // add starting board (as this board is not added by the recursive function)
        solution.add(INITIAL_BOARD);
 
        // generate all possible moves
        ArrayList<long[]> moves = new ArrayList<long[]>();
        // holds all starting positions in west-east direction
        int[] startsX = new int[] {2,9,14,15,16,17,18,21,22,23,24,25,28,29,30,31,32,37,44};
        for (int x : startsX) {
            createMoves(x, x + 1, x + 2, moves);
        }
        // holds all starting positions in north-south direction
        int[] startsY = new int[] {2,3,4,9,10,11,14,15,16,17,18,19,20,23,24,25,30,31,32};
        for (int y : startsY) {
            createMoves(y, y + 7, y + 14, moves);
        }
        // randomize the order of the moves (this highly influences the resulting runtime)
        Collections.shuffle(moves);
        // fill in the global moves variable that is used by the solver
        moves.toArray(RestaUm.moves);
 
        // start recursively search for the initial board from the goal (reverse direction!)
        search(GOAL_BOARD);
 
        // print required time
        System.out.println("Completed in " + (System.currentTimeMillis() - time) + " ms.");
 
        // print the found solution
        for (long step : solution) {
            printBoard(step);
        }
    }
}