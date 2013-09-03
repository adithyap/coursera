#include <stdio.h>

#define UNION_FIND

#ifdef UNION_FIND
#include "union_find.h"

#elif defined QUICK_FIND
#include "quick_find.h"

#elif defined QUICK_UNION
#include "quick_union.h"

#else
#include "union_find.h"

#endif

int main(){
	int n;
	uf uf_obj;

	n = 10;

	// Initialize
	uf_init(&uf_obj, n);

	// Union (Generates fully connected tree)
	// uf_union(&uf_obj, 4, 3);
	// uf_union(&uf_obj, 3, 8);
	// uf_union(&uf_obj, 6, 5);
	// uf_union(&uf_obj, 9, 4);
	// uf_union(&uf_obj, 2, 1);
	// uf_union(&uf_obj, 5, 0);
	// uf_union(&uf_obj, 7, 2);
	// uf_union(&uf_obj, 6, 1);
	// uf_union(&uf_obj, 7, 3);

	uf_union(&uf_obj, 0, 8);
	uf_union(&uf_obj, 7, 3);
	uf_union(&uf_obj, 9, 8);
	uf_union(&uf_obj, 1, 5);
	uf_union(&uf_obj, 3, 1);
	uf_union(&uf_obj, 0, 2);
	uf_union(&uf_obj, 8, 4);
	uf_union(&uf_obj, 8, 7);
	uf_union(&uf_obj, 6, 2);

	// Find
	// printf("%d\n", uf_find(&uf_obj, 8, 9));
	// printf("%d\n", uf_find(&uf_obj, 0, 2));
	// printf("%d\n", uf_find(&uf_obj, 7, 5));
	// printf("%d\n", uf_find(&uf_obj, 1, 11));

	uf_display(&uf_obj);

	// Dispose
	uf_dispose(&uf_obj);

	return 0;
}
