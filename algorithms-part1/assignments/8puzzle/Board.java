import java.util.ArrayList;

/**
 * Created by adithya on 08/11/14.
 */
public class Board
{

    //region "Properties and Fields"

    // Read only - values should not be modified after init
    private final int _n;
    private final int[][] _blocks;
    private int _manhattan;
    private int _hamming;

    //endregion


    //region "Initialization"

    public Board(int[][] blocks)
    {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)

        _blocks = cloneBlocks(blocks);

        // Initialize N
        if (_blocks != null)
            _n = _blocks.length;
        else
            _n = -1; // Invalid input (null), set N to invalid size

        // Check for invalid input
        // Exception in constructor?
        if(_n < 2)
            throw new NullPointerException("Invalid size of N");

        computeHamming();
        computeManhattan();
    }

    private void computeHamming()
    {
        // Count of number of blocks out of place

        _hamming = 0;

        if(_blocks == null)
//            throw new NullPointerException("Blocks are null");
            return;

        int expectedNumber = 1;
        // Loop over all elements and compute blocks out of place
        for (int i = 0; i < _n; i++)
            for (int j = 0; j < _n; j++, expectedNumber++)
                if (_blocks[i][j] != 0)
                    _hamming += (expectedNumber == _blocks[i][j]) ? 0 : 1;
    }

    private void computeManhattan()
    {
        _manhattan = 0;

        // Sum of Manhattan distances between blocks and goal

        if (_blocks == null)
            return;

        for (int i = 0; i < _n; i++)
            for (int j = 0; j < _n; j++)
            {
                if (_blocks[i][j] == 0)
                    continue;

                int expectedI = (_blocks[i][j] - 1) / _n;
                int expectedJ = (_blocks[i][j] - 1) % _n;

                _manhattan += Math.abs(i - expectedI) + Math.abs(j - expectedJ);
            }
    }

    //endregion


    //region "Public Functions"

    public int dimension()
    {
        // Return board dimension N

        if(_blocks == null)
            throw new NullPointerException("Blocks are null");

        return _n;
    }

    public int hamming()
    {
        if(_blocks == null)
            throw new NullPointerException("Blocks are null");

        return _hamming;
    }

    public int manhattan()
    {
        if(_blocks == null)
            throw new NullPointerException("Blocks are null");

        return _manhattan;
    }

    public boolean isGoal()
    {
        // Is this board the goal board?

        return hamming() == 0;
    }

    public Board twin()
    {
        // A board that is obtained by exchanging two adjacent blocks in the same row
        // Generate this board as follows:
        // 1. Swap element 1 and 2 in row 1 if neither of them are 0's (0 indicates blank space)
        // 2. If there was a 0 detected in row 1 using step #1, do the same in row 2
        //      (will succeed here as there is only one blank)

        if(_blocks == null)
            throw new NullPointerException("Blocks are null");

        int[][] twinBlocks = cloneBlocks(_blocks);

        int row = 0;
        if(twinBlocks[0][0] == 0 || twinBlocks[0][1] == 0)
            row++;

        // Swap blocks
        swapElements(twinBlocks, row, 0, row, 1);

        return new Board(twinBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if(_blocks == null)
            throw new NullPointerException("Blocks are null");

        if(y == null)
            return false;

        if(y.getClass().equals(Board.class) == false)
            return false;

        // Cast object "y" to Board
        Board otherBoard = (Board)y;

        if(otherBoard.dimension() != this.dimension())
            return false;

        // Check all elements for mismatch
        for (int i = 0; i < _n; i++)
            for (int j = 0; j < _n; j++)
                if(_blocks[i][j] != otherBoard._blocks[i][j])
                    return false;

        // If control reached here, there are no discrepancies
        return true;
    }

    public Iterable<Board> neighbors()
    {
        // Generate (possibly) 4 boards
        ArrayList<Board> neighborBoards = new ArrayList<Board>();

        ///////////////////////////////////
        // Part 1 - Identify blank
        // Loop over all
        int blankI = -1, blankJ = -1;

        for (int i = 0; i < _n; i++)
        {
            for (int j = 0; j < _n; j++)
                if (_blocks[i][j] == 0)
                {
                    blankI = i;
                    blankJ = j;
                    break;
                }

            // Check if any of the indices changed from -1 (initial value)
            // If it has changed, blank space has been identified, break out
            if(blankI != -1)
                break;
        }

        ///////////////////////////////////////////
        // Part 2 - Neighbor generation
        // Use blank indices to generate neighbors
        Board tempBoard = null;

        // Move block below
        if((tempBoard = generateNewBoard(_blocks, blankI, blankJ, blankI + 1, blankJ)) != null)
            neighborBoards.add(tempBoard);

        // Move block above
        if((tempBoard = generateNewBoard(_blocks, blankI, blankJ, blankI - 1, blankJ)) != null)
            neighborBoards.add(tempBoard);

        // Move block right
        if((tempBoard = generateNewBoard(_blocks, blankI, blankJ, blankI, blankJ + 1)) != null)
            neighborBoards.add(tempBoard);

        // Move block left
        if((tempBoard = generateNewBoard(_blocks, blankI, blankJ, blankI, blankJ - 1)) != null)
            neighborBoards.add(tempBoard);

        // Return our now generated array list of boards
        return neighborBoards;
    }

    // string representation of this board (in the output format specified below)
    public String toString()
    {

        StringBuilder sb = new StringBuilder();
        sb.append(_n + "\n");

        for (int i = 0; i < _n; i++)
        {
            sb.append(" ");

            for (int j = 0; j < _n - 1; j++)
            {
                sb.append(_blocks[i][j] + " ");
            }

            // Unnecessarily stupid optimization?
            sb.append(_blocks[i][_n - 1] + "\n");
        }

        return sb.toString();
    }

    //endregion


    //region "Internal Helpers"

    private Board generateNewBoard(int[][] blocks, int sourceI, int sourceJ, int targetI, int targetJ)
    {
        // Validate all indices provided
        // Return null object if any of them are found to be invalid
        // Source indices are expected to be valid (since this is an internal call, this assumption is safe)
        if (!isIndexValid(targetI) || !isIndexValid(targetJ))
            return null;

        // Copy _blocks into a new array (deep copy)
        int[][] newBlocks = cloneBlocks(_blocks);

        // Swap source with target
        swapElements(newBlocks, sourceI, sourceJ, targetI, targetJ);

        // Return new board
        return new Board(newBlocks);
    }

    private boolean isIndexValid(int index){

        return (index >= 0) && (index < _n);
    }

    private void swapElements(int[][] blocks, int sourceI, int sourceJ, int targetI, int targetJ)
    {
        int temp = blocks[sourceI][sourceJ];
        blocks[sourceI][sourceJ] = blocks[targetI][targetJ];
        blocks[targetI][targetJ] = temp;
    }

    private int[][] cloneBlocks(int[][] blocks)
    {
        int [][] clonedBlocks = new int[blocks.length][blocks.length];
        for(int i = 0; i < blocks.length; i++)
            clonedBlocks[i] = blocks[i].clone();

        return clonedBlocks;
    }

    //endregion


    //region "Static"

    public static void main(String[] args)
    {
        // unit tests :P
    }

    //endregion
}
