#include <stdio.h>
#include "percolationStats.h"

int main(int argc, char *argv[]){

	int n;
	int t;
	char **ptr;

	// Init random
	srand(time(NULL));

	percolationStats ps;

	if(argc - 1 == 2){
		n = strtol(argv[1], NULL, 10);
		t = strtol(argv[2], NULL, 10);

		if(n <= 0 || t <= 0){
			return -1;
		}

		percolationStats_init(&ps, n, t);
		percolationStats_runSimulation(&ps);

		printf("mean\t= %lf\n", percolationStats_mean(&ps));
		printf("stddev\t= %lf\n", percolationStats_stddev(&ps));
		printf("95%% confidence interval\t= %lf, %lf\n", percolationStats_confidenceLo(&ps), percolationStats_confidenceHi(&ps));

		percolationStats_dispose(&ps);
	}

	return 0;
}