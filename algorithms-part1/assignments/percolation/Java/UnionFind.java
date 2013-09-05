public class UnionFind{
	
	private int[] id;
	private int[] subElementCount;
	int count;

	public UnionFind(int n){
		int i;

		id = new int[n];
		subElementCount = new int[n];
		count = n;

		for(i = 0; i < n; ++i){
			id[i] = i;
		}

		for(i = 0; i < n; ++i){
			subElementCount[i] = 1;
		}		
	}

	public boolean union(int p, int q){
		int pRoot = root(p);
		int qRoot = root(q);

		if(p >= count || q >= count){
			return false;
		}

		if(subElementCount[p] < subElementCount[q]){
			id[pRoot] = qRoot;
			subElementCount[qRoot] += subElementCount[pRoot];
		} else{
			id[qRoot] = pRoot;
			subElementCount[pRoot] += subElementCount[qRoot];
		}

		return true;
	}

	public boolean find(int p, int q){
		if(p >= count || q >= count){
			return false;
		}

		return (root(p) == root(q));
	}

	private int root(int p){
		while(id[p] != p){
			id[p] = id[id[p]];
			p = id[p];
		}

		return p;
	}
}
