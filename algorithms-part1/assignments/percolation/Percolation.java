public class Percolation{

	private boolean[][] state;
	private UnionFind grid;
	private int rowSize;
	private int top;
	private int bottom;
   
	public Percolation(int N){
		int i, j;

		rowSize = N;

		top = linearIndex(N-1, N-1) + 1;	// virtual root to top nodes (N*N)
		bottom = linearIndex(N-1, N-1) + 2;	// virtual root to bottom nodes (N*N + 1)

		state = new boolean[N][N];

		for(i = 0; i < N; i++){
			for(j = 0; j < N; j++){
				state[i][j] = false;
			}
		}
		
		grid = new UnionFind((N * N) + 2);

		for(i = 0; i < N; i++){
			grid.union(linearIndex(0, i), top);
		}

		for(i = 0; i < N; i++){
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