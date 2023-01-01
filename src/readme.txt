What data structure do you expect to have the best (fastest) performance? Which one do you
expect to be the slowest? Do the results of timing your program’s execution match your
expectations? If so, briefly explain the correlation. If not, what run times deviated and briefly explain
why you think this is the case.

I expect hash map to have the best performance as it runs in an average of theta(1) runtime for put() and get(),
while bst and avl both both have an average of theta(lg(n)) for put() and get(). In order to ascertain whether avl or
bst will be faster, we need to take into account that while bst has worst case theta(n) when entries are inserted in
sorted order, avl only has worst case theta(lg(n)). However, another factor to consider is that the words in these texts
are ordered quite randomly so the structure of the bst will likely stay relatively balanced and close to theta(lg(n)),
and that the rebalancing of the avl may add extra runtime for these operations. So while I am not completely sure, I
would probably order the runtimes from smallest to largest like this: hash, bst, avl.

Times:
bst (average over 3): (0.558+0.596+0.571)/3 = 0.575s
avl (average over 3): (0.683+0.634+0.620)/3 = 0.646s
hash (average over 3): (0.434+0.510+0.467)/3 = 0.470s

As I expected, hash ran the fastest, followed by bst and then avl.