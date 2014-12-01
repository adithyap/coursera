/**
 * Created by adithya on 08/11/14.
 */
public class Solver
{

    //region "Fields"

    // Private fields
    private MinPQ<TraceableBoard> _initialGameTree;
    private MinPQ<TraceableBoard> _twinGameTree;
    private TraceableBoard _solutionBoard;

    private int _moves;

    // Constants - DO NOT MODIFY
    private enum Heuristic{ MANHATTAN, HAMMING }
    private static final int INVALID_MOVES = -1;

    // Environment settings - SET IN STATIC CONSTRUCTOR
    private static final Heuristic HEURISTIC;
    private static final boolean DEBUG_MODE;

    //endregion


    //region "Traceable Board (Internal Wrapper Class for Board)"

    private class TraceableBoard implements Comparable<TraceableBoard>
    {
        //region "Fields"

        private final int MOVES;
        private final TraceableBoard PARENT;
        private final Board BOARD;

        //endregion

        //region "Initialization"

        public TraceableBoard(Board board, TraceableBoard parent)
        {
            BOARD = board;
            PARENT = parent;

            if(parent == null)
                MOVES = 0;
            else
                MOVES = parent.MOVES + 1;
        }

        //endregion

        //region "Wrappers"

        public boolean isGoal()
        {
            return BOARD.isGoal();
        }

        public boolean equals(Object y)
        {
            return BOARD.equals(((TraceableBoard)y).BOARD);
        }

        public Iterable<Board> neighbors()
        {
            return BOARD.neighbors();
        }

        @Override
        public String toString()
        {
            if (DEBUG_MODE)
                return BOARD.toString() + "\nHeuristic : " + getHeuristicValue();

            return BOARD.toString();
        }

        //endregion

        //region "Comparable"

        @Override
        public int compareTo(TraceableBoard that)
        {
            if (that == null)
                throw new NullPointerException("Argument targetBoard is null in compareTo");

            // Return compare to based on heuristic of choice

            int h1 = this.getHeuristicValueWithMoves();
            int h2 = that.getHeuristicValueWithMoves();

            // Break ties using just heuristic (without factoring in moves taken to reach state)
            if(h1 == h2)
                return this.getHeuristicValue().compareTo(that.getHeuristicValue());
            else
                return ((Integer)h1).compareTo(h2);
        }

        //endregion

        //region "Helpers"

        private Integer getHeuristicValue()
        {
            int heuristicWeight = 1;

            if (HEURISTIC == Heuristic.MANHATTAN)
                return (BOARD.manhattan() * heuristicWeight);

            if (HEURISTIC == Heuristic.HAMMING)
                return (BOARD.hamming() * heuristicWeight);

            // Invalid
            return Integer.MAX_VALUE;
        }

        private Integer getHeuristicValueWithMoves()
        {
            int heuristicWeight = 1;

            if (HEURISTIC == Heuristic.MANHATTAN)
                return (BOARD.manhattan() * heuristicWeight) + MOVES;

            if (HEURISTIC == Heuristic.HAMMING)
                return (BOARD.hamming() * heuristicWeight) + MOVES;

            // Invalid
            return Integer.MAX_VALUE;
        }

        //endregion
    }

    //endregion


    //region "Initialization"

    public Solver(Board initial)
    {
        // Find a solution to the initial board (using the A* algorithm)

        initializeObjects(initial);

        solveBoards();
    }

    private void initializeObjects(Board initial)
    {
        // Stop here if initial is null :D
        if(initial == null)
            throw new NullPointerException("Initial board is null");

        // Initialize PQ and insert initial
        _initialGameTree = new MinPQ<TraceableBoard>();
        _initialGameTree.insert(new TraceableBoard(initial, null));

        // Initialize PQ and insert initial board's twin
        _twinGameTree = new MinPQ<TraceableBoard>();
        _twinGameTree.insert(new TraceableBoard(initial.twin(), null));

        // Init board as unsolvable - is this required??
        _moves = INVALID_MOVES;

        printTrace("Initialization Complete");
    }

    //endregion


    //region "Solver"

    private void solveBoards()
    {

        // Handle is already goal case
        if(_initialGameTree.min().isGoal())
        {
            _moves = 0;
            return;
        }

        while(true)
        {
            TraceableBoard lastBoard;

            // Move original board
            lastBoard = moveSingleStep(_initialGameTree);

            if(lastBoard.isGoal())
            {
                // Goal Reached for initial
                _solutionBoard = lastBoard;
                computeMinimumMoves(lastBoard);
                break;
            }

            // Move twin board
            lastBoard = moveSingleStep(_twinGameTree);

            if(lastBoard.isGoal())
            {
                // Goal Reached for twin
                // Invalidate solution
                _moves = INVALID_MOVES;
                break;
            }

//            printTrace("Completed move " + _moves);
        }
    }

    private TraceableBoard moveSingleStep(MinPQ<TraceableBoard> gameTree)
    {
        if (gameTree.isEmpty() == false)
        {
            TraceableBoard currentBoard = gameTree.delMin();

            // No point in proceeding if goal
            if(currentBoard.isGoal())
                return currentBoard;

            // Get all neighbors
            for (Board neighbor : currentBoard.neighbors())
            {
                // Check if we are not adding the parent again
                TraceableBoard parent =  currentBoard.PARENT;
                boolean insertRequired = true;

                while (parent != null)
                {
                    if(neighbor.equals(parent.BOARD))
                    {
                        insertRequired = false;
                        break;
                    }

                    parent = parent.PARENT;
                }

                if(insertRequired)
                {
                    // Add this board to game tree
                    TraceableBoard tb = new TraceableBoard(neighbor, currentBoard);
                    gameTree.insert(tb);

                    // No point in proceeding if goal
                    if (tb.isGoal())
                        return tb;
                }
            }

            return currentBoard;
        }
        else
        {
            return null;
        }
    }

    private void computeMinimumMoves(TraceableBoard state)
    {
        _moves = 0;

        while(state != null)
        {
            state = state.PARENT;

            if(state != null)
                _moves++;
        }
    }

    //endregion


    //region "Public Functions"

    public boolean isSolvable()
    {
        // is the initial board solvable?
        return _moves != INVALID_MOVES;
    }

    public int moves()
    {
        // min number of moves to solve initial board; -1 if unsolvable
        return _moves;
    }

    public Iterable<Board> solution()
    {
        // sequence of boards in a shortest solution; null if unsolvable
        if(_moves == INVALID_MOVES)
            return null;

        Stack<Board> moves = new Stack<Board>();

        TraceableBoard state = _solutionBoard;
        while(state != null)
        {
            moves.push(state.BOARD);
            state = state.PARENT;
        }

        return moves;
    }

    //endregion


    //region "Debugging Helpers"

    private void printTrace(String trace)
    {
        if(DEBUG_MODE)
            StdOut.println("Trace: " + trace);
    }

    //endregion


    //region "Static"

    static
    {
        HEURISTIC = Heuristic.MANHATTAN;
        DEBUG_MODE = false;
    }

    // solve a slider puzzle
    public static void main(String[] args)
    {

        // create initial board from file

        int N;
        int[][] blocks;

        if(DEBUG_MODE)
        {
            // Solvable 4 steps
//            blocks = new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};

            // Solvable 4 steps board's twin
//            blocks = new int[][]{{0, 1, 3}, {2, 4, 5}, {7, 8, 6}};

            // Solvable, 5 steps
//            blocks = new int[][]{{4, 1, 3}, {0, 2, 6}, {7, 5, 8}};

            // Solvable, 11 steps
            blocks = new int[][]{{1, 0, 2}, {7, 5, 4}, {8, 6, 3}};

            // Unsolvable
//            blocks = new int[][]{{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};

            // Unsolvable board's twin
//            blocks = new int[][]{{2, 1, 3}, {4, 5, 6}, {8, 7, 0}};
        }
        else
        {
            In in = new In(args[0]);
            N = in.readInt();

            blocks = new int[N][N];

            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    blocks[i][j] = in.readInt();
        }

        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
        {
            StdOut.println("No solution possible");
        }
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    //endregion
}
