import java.io.*;
import java.util.Random;

public class PercolationStats {

	private int n;
	private int t;

	private int[] threshold;

	public PercolationStats(int N, int T){
		this.n = N;
		this.t = T;

		threshold = new int[t];
	}

	public double mean(){
		double sum = 0.0;

		for(int i = 0; i < t; i++){
			sum += (double)threshold[i] / (n * n);
		}

		return sum / ((double)t);
	}

	public double stddev(){
		double sum = 0.0;
		double sampleMean = mean();

		for(int i = 0; i < t; i++){
			double temp = ((double)(threshold[i]) / (double)(n*n)) - sampleMean;
			sum += (temp * temp);
		}

		if(t > 1)
			return Math.sqrt(sum / (t - 1));

		return Math.sqrt(sum);
	}

	public double confidenceLo(){
		double sampleMean = mean();
		double sampleStddev = stddev();

		return (sampleMean - (1.96 * sampleStddev / Math.sqrt(t)));
	}

	public double confidenceHi(){
		double sampleMean = mean();
		double sampleStddev = stddev();

		return (sampleMean + (1.96 * sampleStddev / Math.sqrt(t)));
	}

	public void runSimulation(){

		for(int k = 0; k < t; k++){

			Percolation p = new Percolation(n);
			int openSites = 0;

			int i, j;
			int limit = n * n;

			while(limit > 0){
				
				i = randInt(0, n-1);
				j = randInt(0, n-1);

				if(!p.isOpen(i, j)){
					
					p.open(i,j);
					openSites++;

					if(p.percolates()){
						threshold[k] = openSites;
						break;
					}

					limit--;
				}
			}
		}
	}

	public void displayTreshold(){
		for(int i = 0; i < t; i++){
			System.out.print(threshold[i] + ", ");
		}
		System.out.println();
	}

	private int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	public static void main(String[] args){

		if(args.length == 2){
			int n = Integer.parseInt(args[0]);
			int t = Integer.parseInt(args[1]);

			PercolationStats ps = new PercolationStats(n, t);
			ps.runSimulation();

			System.out.println("mean\t= " + ps.mean());
			System.out.println("stddev\t= " + ps.stddev());
			System.out.println("95% confidence interval\t= " + ps.confidenceLo() + ", " + ps.confidenceHi());
		}

	}
}
