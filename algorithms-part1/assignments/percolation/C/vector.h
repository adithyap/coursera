#include <stdio.h>
#include <stdlib.h>

typedef struct{
	int *value;
	int used;
	int size;
} vector;

// Declare all functions
void vector_init(vector *, int);
int vector_size(vector *);
int vector_element(vector *, int);
int vector_assign(vector *, int, int);
void vector_append(vector *, int);
void vector_dispose(vector *);

// Define functions

void vector_init(vector *v, int initialSize){
	// printf("Allocating memory, size: %d\n", initialSize);

	v->value = (int *)malloc(initialSize * sizeof(int));
	v->used = 0;
	v->size = initialSize;
}

int vector_size(vector *v){
	return v->used;
}

int vector_element(vector *v, int index){
	if(index < v->used)
		return v->value[index];

	return -1;
}

int vector_assign(vector *v, int index, int value){
	if(index < v->used){
		v->value[index] = value;
		return 1;
	}

	return 0;
}

void vector_append(vector *v, int element){
	if(v->used == v->size){
		printf("Reallocating memory, size: %d\n", v->size);

		v->size *= 2;
		v->value = (int *)realloc(v->value, v->size * sizeof(int));
	}

	v->value[v->used] = element;
	v->used++;
}

void vector_dispose(vector *v){
	v->used = 0;
	v->size = 0;
	free(v->value);
	v->value = NULL;
}
