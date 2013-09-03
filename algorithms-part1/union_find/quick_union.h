// Sep 3, 13

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

int __uf_root(uf *, int);

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

	int p_root = __uf_root(obj, p);
	int q_root = __uf_root(obj, q);

	// Return 0 if p (or) q >= size of list
	if(p >= obj->size || q >= obj->size){
		return -1;
	}

	obj->id.value[p_root] = q_root;

	return 1;
}

int uf_find(uf *obj, int p, int q){

	// Return -1 if p (or) q >= size of list
	if(p >= obj->size || q >= obj->size){
		return -1;
	}

	// Return 1 if root[p] == root[q]
	if(__uf_root(obj, p) == __uf_root(obj, q)){
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

// Internal functions

int __uf_root(uf *obj, int p){
	
	while(obj->id.value[p] != p)
		p = obj->id.value[p];
	
	return p;
}
