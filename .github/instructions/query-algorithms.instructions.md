---
description: "Use when: implementing or optimizing search algorithms; writing AND/OR query logic; upgrading scoring from SimpleSorter to TF-IDF or BM25; implementing phrase search with positional intersection; working with posting list traversal; modifying IndexSearcher.search(); adding ranking strategies via the Sort interface."
applyTo: "search-engine-core/src/main/java/**/query/**/*.java"
---

# Search Algorithm & Performance Rules

## Complexity Constraints

### Posting List Intersection → $O(M+N)$
The existing `IndexSearcher` uses a **double-pointer linear scan** for AND/OR queries. When modifying this logic:
- **ALWAYS** use $O(M+N)$ double-pointer or skip-pointer
- **NEVER** use nested `for` loops ($O(M \times N)$) to find matching docIds
- PostingLists are guaranteed sorted by docId via `Index.optimize()`

### Positional Intersection (Phrase Search) → $O(P_1 + P_2)$
When checking if two terms appear adjacently in a document, `AbstractPosting.positions` is a sorted list. Use double-pointer:
```java
int i = 0, j = 0;
while (i < pos1.size() && j < pos2.size()) {
    int diff = pos2.get(j) - pos1.get(i);
    if (diff == 1) return true;      // found adjacent positions
    else if (diff < 1) j++;          // advance pos2
    else i++;                        // advance pos1
}
```

## Scoring Models

### Current: Frequency Sum (SimpleSorter)
```
score = Σ freq(t, D) for all query terms t
```
Simple but not information-theoretically sound. Upgrade path below.

### Target: TF-IDF
```
score(D, Q) = Σ(t∈Q) [1 + log(freq(t,D))] × log(1 + N / df(t))
```
Requires: total document count N, document frequency df(t) per term.

### Target: BM25
```
score(D, Q) = Σ(t∈Q) IDF(t) × [freq × (k1+1)] / [freq + k1 × (1-b + b × |D|/avgDL)]
k1 = 1.5, b = 0.75
```
Requires: document length |D|, average document length avgDL.

## Sort Interface Contract

The `Sort` interface uses the **Strategy Pattern**:
- `score(AbstractHit)` — compute score, write it into hit via `hit.setScore()`
- `sort(List<AbstractHit>)` — sort in-place by score descending, tie-break by docId ascending

When creating a new Sorter:
1. Implement `Sort` interface
2. In `score()`, read from `hit.getTermPostingMapping()` to get per-term Posting data
3. Score is written into the Hit object, not returned
4. `sort()` uses `Collections.sort()` with a custom `Comparator`
5. Wire the new sorter in `TestSearchIndex` or the Controller instead of `SimpleSorter`
