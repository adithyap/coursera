public class PercolationStats{

    /**
     * Edge size of the grid
     */
	private int n;

    /**
     * Number of operations in the simulation
      */
	private int t;

    /**
     * Holds the number of open sites at the I-th index when I operations were performed
     */
	private int[] threshold;

    private double mean;
    private double stdDev;
    private double confidenceLo;
    private double confidenceHi;

	public PercolationStats(int N, int T){
		this.n = N;
		this.t = T;

		threshold = new int[t];

        // Run simulation to compute thresholds
        runSimulation();

        // Store mean and standard deviation
        this.mean = StdStats.mean(threshold);
        this.stdDev = StdStats.stddev(threshold);

        // Compute and store confidenceHi and confidenceLo
        double common = (1.96 * stdDev / Math.sqrt(t));
        confidenceLo = mean - common;
        confidenceHi = mean + common;
	}

	public double mean(){

        return mean;
	}

	public double stddev(){

        return  stdDev;
	}

	public double confidenceLo(){

        return confidenceLo;
	}

	public double confidenceHi(){

        return confidenceHi;
	}

	private void runSimulation(){

		Percolation p;
		int openSites;
		int i, j;

        // Perform 't' simulation steps
		for(int k = 0; k < t; k++){

            // Initialize values for the current simulation run
			p = new Percolation(n);
			openSites = 0;

            while(p.percolates() == false){

                // Generate i and j at random (for opening)
                // Indices are generated as 1-indexed
                i = StdRandom.uniform(1, n + 1);
                j = StdRandom.uniform(1, n + 1);

                // Open (i,j) if it is not yet open
                if(p.isOpen(i, j) == false) {

                    openSites++;
                    p.open(i, j);

                }

                // Optimize generation of random numbers - since it is the bottleneck
                // Try to use the generated ones in a reverse fashion
                // Open (j,i) if it is not yet open
                // Will this affect the randomness? - Check
                if(p.isOpen(j, i) == false) {

                    openSites++;
                    p.open(j, i);

                }
            }

            threshold[k] = openSites;
		}
	}

    public static void main(String[] args){

        // Debug use case
//        if(args.length == 0) {
//            args = new String[2];
//            args[0] = "20";
//            args[1] = "100";
//        }

        if(args.length == 2){

            try{

                // Read args
                int n = Integer.parseInt(args[0]);
                int t = Integer.parseInt(args[1]);

                // Validate args
                if (n <= 0 || t <= 0){
                    throw new IllegalArgumentException("n = " + n + ", t = " + t);
                }

                // Initialize object
                // Simulation will be performed immediately upon init
                PercolationStats ps = new PercolationStats(n, t);

                // Output stats from simulation run
                System.out.println("mean\t= " + ps.mean());
                System.out.println("stddev\t= " + ps.stddev());
                System.out.println("95% confidence interval\t= " + ps.confidenceLo() + ", " + ps.confidenceHi());

            } catch(NumberFormatException ex){
                throw ex;
            } catch(IllegalArgumentException ex){
                throw ex;
            }

        }
    }
}
