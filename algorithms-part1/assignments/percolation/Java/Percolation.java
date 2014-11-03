public class Percolation{

    private boolean[] openState;
	private WeightedQuickUnionUF percolationGrid;
	private int rowSize;
	private int virtualTopNode;
	private int virtualBottomNode;

	public Percolation(int N){

		int i, j;

		rowSize = N;

		virtualTopNode = linearIndex(N, N) + 1;	// virtual root to top nodes (N*N + 1)
		virtualBottomNode = linearIndex(N, N) + 2;	// virtual root to bottom nodes (N*N + 2)

        // States are to be held only for the elements, not for top and bottom virtual nodes
        openState = new boolean[N * N];

        // Initialize grid with size accommodating top and bottom elements
		percolationGrid = new WeightedQuickUnionUF((N * N) + 2);
	}

	public void open(int i, int j){

        ValidateIndex(i, j);

        // Get i,j in 1-D
        int currentIndex = linearIndex(i, j);

        // Set state of (i,j) as opened
        openState[currentIndex] = true;

        // Check if top row and union top
        if(i == 1){
            percolationGrid.union(virtualTopNode, currentIndex);
        }

        // Check if bottom row and union bottom
        if(i == rowSize){
            percolationGrid.union(virtualBottomNode, currentIndex);
        }

        // Check if element to the:
        // 1. Left of (i,j) is open
        // 2. Right of (i,j) is open
        // 3. Bottom of (i,j) is open
        // 4. Top of (i,j) is open
        // For each of them which is already open, perform a union with currentIndex

		if(i  > 1 && isOpen(i - 1, j)){
			percolationGrid.union(linearIndex(i - 1, j), currentIndex);
		}

		if(i < rowSize && isOpen(i + 1, j)){
			percolationGrid.union(linearIndex(i + 1, j), currentIndex);
		}

		if(j > 1 && isOpen(i, j - 1)){
			percolationGrid.union(linearIndex(i, j - 1), currentIndex);
		}

		if(j < rowSize && isOpen(i, j + 1)){
			percolationGrid.union(linearIndex(i, j + 1), currentIndex);
		}
	}

	public boolean isOpen(int i, int j) {

        ValidateIndex(i, j);

        return openState[linearIndex(i, j)];
    }

	public boolean isFull(int i, int j){

		ValidateIndex(i, j);

        // Check if (i,j) is connected to the top
		return percolationGrid.find(linearIndex(i, j)) == virtualTopNode;
	}

	public boolean percolates(){

        // Percolates if virtualBottomNode is connected to the virtualTopNode
        return percolationGrid.connected(virtualBottomNode, virtualTopNode);
	}

	private int linearIndex(int i, int j){

        ValidateIndex(i, j);

        // Flatten 2-D index to 1-D
        // Shift from 1-index to 0-index
        return (i - 1) * rowSize + (j - 1);
	}

	private void ValidateIndex(int i, int j){

        // Check for positive indices
        // Check if indices are less than initialized row sizes
        boolean valid =  (i > 0 && i <= rowSize && j > 0 && j <= rowSize);

        if(valid == false) {
            String error = String.format("(%1$s, %2$s) out of bounds for %3$s^2 grid.", i, j, rowSize);
            throw new IndexOutOfBoundsException(error);
        }
	}
}
