#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct{
	vector state;
	uf grid;

	int rowSize;
	int top;
	int bottom;
} percolation;

// Declare all functions
void percolation_init(percolation *, int);
void percolation_open(percolation *, int, int);
int percolation_isOpen(percolation *, int, int);
int percolation_isFull(percolation *, int, int);
int percolation_percolates(percolation *);
void percolation_dispose(percolation *);


int __linearIndex(percolation *, int, int);
int __isValidIndex(percolation *, int, int);

// Define functions
void percolation_init(percolation *p, int N){
	int i, j;

	p->rowSize = N;

	p->top = __linearIndex(p, N-1, N-1) + 1;	// virtual root to top nodes (N*N)
	p->bottom = __linearIndex(p, N-1, N-1) + 2;	// virtual root to bottom nodes (N*N + 1)

	vector_init(&p->state, __linearIndex(p, N-1, N-1));

	for(i = 0; i < N; i++){
		for(j = 0; j < N; j++){
			p->state.value[__linearIndex(p, i, j)] = 0;
		}
	}
	
	uf_init(&p->grid, N*N + 2);

	for(i = 0; i < N; i++){
		uf_union(&p->grid, __linearIndex(p, 0, i), p->top);
	}

	for(i = 0; i < N; i++){
		uf_union(&p->grid, __linearIndex(p, p->rowSize - 1, i), p->bottom);
	}
}

void percolation_open(percolation *p, int i, int j){

	if(percolation_isOpen(p, i-1, j)){
		uf_union(&p->grid, __linearIndex(p, i-1, j), __linearIndex(p, i, j));
	}

	if(percolation_isOpen(p, i+1, j)){
		uf_union(&p->grid, __linearIndex(p, i+1, j), __linearIndex(p, i, j));
	}

	if(percolation_isOpen(p, i, j-1)){
		uf_union(&p->grid, __linearIndex(p, i, j-1), __linearIndex(p, i, j));
	}

	if(percolation_isOpen(p, i, j+1)){
		uf_union(&p->grid, __linearIndex(p, i, j+1), __linearIndex(p, i, j));
	}

	p->state.value[__linearIndex(p, i, j)] = 1;
}

int percolation_isOpen(percolation *p, int i, int j){
	if(__isValidIndex(p, i, j)){
		return p->state.value[__linearIndex(p, i, j)];
	}

	return 0;
}

int percolation_isFull(percolation *p, int i, int j){
	if(!__isValidIndex(p, i, j)){
		return 0;
	}
	
	return uf_find(&p->grid, __linearIndex(p, i, j), p->top);
}

int percolation_percolates(percolation *p){
	return uf_find(&p->grid, p->top, p->bottom);
}

void percolation_dispose(percolation *p){
	vector_dispose(&p->state);
	uf_dispose(&p->grid);
}

int __linearIndex(percolation *p, int i, int j){
	return (i * p->rowSize) + j;
}

int __isValidIndex(percolation *p, int i, int j){
	return (i >= 0 && i < p->rowSize && j >= 0 && j < p->rowSize);
}
