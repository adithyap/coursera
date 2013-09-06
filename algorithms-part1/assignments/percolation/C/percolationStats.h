#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

#ifndef VECTOR_INCLUDED
#define VECTOR_INCLUDED
#include "vector.h"
#endif

#include "union_find.h"
#include "percolation.h"


typedef struct{
	int n;
	int t;
	vector threshold;
}percolationStats;

// Declare all functions
void percolationStats_init(percolationStats *, int, int);
double percolationStats_mean(percolationStats *);
double percolationStats_confidenceLo(percolationStats *);
double percolationStats_confidenceHi(percolationStats *);
void percolationStats_runSimulation(percolationStats *);
void percolationStats_dispose(percolationStats *);

int __random_number(int min_num, int max_num);

// Define functions
void percolationStats_init(percolationStats *ps, int N, int T){
	srand(time(NULL)); // Seed random

	ps->n = N;
	ps->t = T;

	vector_init(&ps->threshold, T);
}

double percolationStats_mean(percolationStats *ps){
	int i;
	long long total_elements = ps->n * ps->n;
	double sum = 0.0;

	for(i = 0; i < ps->t; i++){
		sum += (double)(ps->threshold.value[i]) / (double)(total_elements);
	}

	return sum / ((double)ps->t);
}

double percolationStats_stddev(percolationStats *ps){
	int i;
	long long total_elements = ps->n * ps->n;
	double sum = 0.0;
	double sampleMean = percolationStats_mean(ps);
	double temp;

	for(i = 0; i < ps->t; i++){
		temp = ((double)(ps->threshold.value[i]) / (double)(total_elements)) - sampleMean;
		sum += (temp * temp);
	}

	if(ps->t > 1)
		return sqrt(sum / (ps->t - 1));

	return sqrt(sum);
}

double percolationStats_confidenceLo(percolationStats *ps){
	double sampleMean = percolationStats_mean(ps);
	double sampleStddev = percolationStats_stddev(ps);

	return ((double)sampleMean - (double)(1.96 * sampleStddev / sqrt((double)ps->t)));
}

double percolationStats_confidenceHi(percolationStats *ps){
	double sampleMean = percolationStats_mean(ps);
	double sampleStddev = percolationStats_stddev(ps);

	return ((double)sampleMean + (double)(1.96 * sampleStddev / sqrt((double)ps->t)));
}

void percolationStats_runSimulation(percolationStats *ps){

	percolation p;
	int openSites;
	int i, j, k;
	int elements;
	int opened;

	for(k = 0; k < ps->t; k++){

		percolation_init(&p, ps->n);
		openSites = 0;
		
		elements = ps->n * ps->n;

		while(elements > 0){
			opened = 0;

			i = __random_number(0, ps->n - 1);
			j = __random_number(0, ps->n - 1);

			if(!percolation_isOpen(&p, i, j)){
				
				percolation_open(&p, i, j);
				openSites++;

				opened = 1;
			}
			else if(!percolation_isOpen(&p, j, i)){

				percolation_open(&p, j, i);
				openSites++;

				opened = 1;
			}

			if(opened && percolation_percolates(&p)){
				vector_append(&ps->threshold, openSites);
				break;
			}

			if(opened)	elements--;
		}

		percolation_dispose(&p);
	}
}

void percolationStats_dispose(percolationStats *ps){
	vector_dispose(&ps->threshold);
}

int __random_number(int min_num, int max_num)
{
    int result = (rand() % (max_num - min_num + 1)) + min_num;
    return result;
}
