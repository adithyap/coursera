public class Percolation{

	private boolean[][] state;
	private UnionFind grid;
	private int rowSize;
	private int top;
	private int bottom;
   
	public Percolation(int n){
		int i, j;

		rowSize = n;

		top = linearIndex(n-1, n-1) + 1;	// virtual root to top nodes (n*n)
		bottom = linearIndex(n-1, n-1) + 2;	// virtual root to bottom nodes (n*n + 1)

		state = new boolean[n][n];

		for(i = 0; i < n; i++){
			for(j = 0; j < n; j++){
				state[i][j] = false;
			}
		}
		
		grid = new UnionFind((n * n) + 2);

		for(i = 0; i < n; i++){
			grid.union(linearIndex(0, i), top);
		}

		for(i = 0; i < n; i++){
			grid.union(linearIndex(rowSize - 1, i), bottom);
		}
	}

	public void open(int i, int j){
		if(isOpen(i-1, j)){
			grid.union(linearIndex(i-1, j), linearIndex(i, j));
		}

		if(isOpen(i+1, j)){
			grid.union(linearIndex(i+1, j), linearIndex(i, j));
		}

		if(isOpen(i, j-1)){
			grid.union(linearIndex(i, j-1), linearIndex(i, j));
		}

		if(isOpen(i, j+1)){
			grid.union(linearIndex(i, j+1), linearIndex(i, j));
		}

		if(isValidIndex(i, j)){
			state[i][j] = true;
		}
	}

	public boolean isOpen(int i, int j){
		if(isValidIndex(i, j)){
			return state[i][j];
		}

		return false;
	}

	public boolean isFull(int i, int j){
		if(!isValidIndex(i, j)){
			return false;
		}

		return grid.find(linearIndex(i, j), top);
	}

	public boolean percolates(){
		return grid.find(top, bottom);
	}

	private int linearIndex(int i, int j){
		return (i * rowSize) + j;
	}

	private boolean isValidIndex(int i, int j){
		return (i >= 0 && i < rowSize && j >= 0 && j < rowSize);
	}
}