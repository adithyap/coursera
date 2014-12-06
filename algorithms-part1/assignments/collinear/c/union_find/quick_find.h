// Sep 2, 13

#include <stdio.h>
#include <stdlib.h>
#include "..\lib\vector.h"

typedef struct{
	vector id;
	int size;
} uf;

// Declare all functions
void uf_init(uf *, int);
int uf_union(uf *, int, int);
int uf_find(uf *, int, int);
void uf_display(uf *);
void uf_dispose(uf *);

// Define functions

void uf_init(uf *obj, int n){
	int i;

	// Initialize id
	vector_init(&obj->id, n);
	obj->size = n;

	// Assign id values
	for(i = 0; i < n; i++){
		vector_append(&obj->id, i);
	}
}

int uf_union(uf *obj, int p, int q){

	int i;
	int size = vector_size(&obj->id);

	// Return 0 if p (or) q >= size of list
	if(p >= obj->size || q >= obj->size){
		return -1;
	}

	// Set all value of id[q] to id[p]
	for(i = 0; i < size; i++){
		if(q == obj->id.value[i]){
			obj->id.value[i] = obj->id.value[p];
		}
		else if(p == obj->id.value[i]){
			obj->id.value[i] = obj->id.value[q];
		}
	}

	return 1;
}

int uf_find(uf *obj, int p, int q){

	// Return -1 if p (or) q >= size of list
	if(p >= obj->size || q >= obj->size){
		return -1;
	}

	// Return 1 if id[p] == id[q]
	if(obj->id.value[p] == obj->id.value[q]){
		return 1;
	}

	// Return 0 if not found
	return 0;
}

void uf_display(uf *obj){
	int i, size;

	size = vector_size(&obj->id);

	for(i = 0; i < size; i++){
		printf("%d => Root:%d\n", i, obj->id.value[i]);
	}
}

void uf_dispose(uf *obj){
	vector_dispose(&obj->id);
}
