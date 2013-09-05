#include <stdio.h>
#include <stdlib.h>

#ifndef VECTOR_INCLUDED
#define VECTOR_INCLUDED
#include "vector.h"
#endif

typedef struct{
	vector id;
	vector sub_elements;
	int count;
} uf;

// Declare all functions
void uf_init(uf *, int);
int uf_union(uf *, int, int);
int uf_find(uf *, int, int);
void uf_display(uf *);
void uf_dispose(uf *);

int __uf_root(uf *, int);
int __uf_root_recursion(uf *, int);

// Define functions

void uf_init(uf *obj, int n){
	int i;

	// Initialize id and sub_elements
	vector_init(&obj->id, n);
	vector_init(&obj->sub_elements, n);
	obj->count = n;

	// Assign id values
	for(i = 0; i < n; i++){
		vector_append(&obj->id, i);
	}

	// Assign sub_elements values
	for(i = 0; i < n; i++){
		vector_append(&obj->sub_elements, 1);
	}
}

int uf_union(uf *obj, int p, int q){

	int p_root = __uf_root(obj, p);
	int q_root = __uf_root(obj, q);

	int p_sub_elements = obj->sub_elements.value[p_root];
	int q_sub_elements = obj->sub_elements.value[q_root];

	// Return 0 if p (or) q >= count of list
	if(p >= obj->count || q >= obj->count){
		return -1;
	}

	if(p_sub_elements < q_sub_elements){
		
		obj->id.value[p_root] = q_root;

		obj->sub_elements.value[q_root] += p_sub_elements;

	} else{
		
		obj->id.value[q_root] = p_root;

		obj->sub_elements.value[p_root] += q_sub_elements;
	}

	return 1;
}

int uf_find(uf *obj, int p, int q){

	// Return -1 if p (or) q >= count of list
	if(p >= obj->count || q >= obj->count){
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
		printf("%d => Root:%d, Tree_Size:%d\n", i, obj->id.value[i], obj->sub_elements.value[i]);
	}
}

void uf_dispose(uf *obj){
	vector_dispose(&obj->id);
	vector_dispose(&obj->sub_elements);
}

// Internal functions

int __uf_root(uf *obj, int p){
	
	while(obj->id.value[p] != p){
		obj->id.value[p] = obj->id.value[obj->id.value[p]];
		p = obj->id.value[p];

	}
	
	return p;
}

int __uf_root_recursion(uf *obj, int p){
	
	int root;
	
	if(obj->id.value[p] == p){
		return p;
	}
	else{
		root = __uf_root(obj, obj->id.value[p]);
		obj->id.value[p] = root;
		return root;
	}

}
