---
description: "Use when: creating or editing Java files in search-engine-core; implementing index/parse/query features; adding filters, document builders, index structures; following the abstract-class-to-impl pattern."
applyTo: "search-engine-core/src/main/java/**/*.java"
---

# Core Engine Java Conventions

## File Organization

| Package | Contains |
|---------|----------|
| `index/` | Abstract data model: AbstractTerm, AbstractPosting, AbstractIndex, AbstractDocument, AbstractTermTuple |
| `index/impl/` | Concrete implementations: Term, Posting, Index, Document, TermTuple, DocumentBuilder, IndexBuilder |
| `parse/` | Abstract parsing: AbstractTermTupleStream, AbstractTermTupleScanner, AbstractTermTupleFilter |
| `parse/impl/` | Concrete parsing: TermTupleScanner, PatternTermTupleFilter, StopWordTermTupleFilter, LengthTermTupleFilter |
| `query/` | Abstract search: AbstractHit, AbstractIndexSearcher, Sort |
| `query/impl/` | Concrete search: Hit, IndexSearcher, SimpleSorter |
| `util/` | Utilities: Config, StopWords, StringSplitter, FileUtil |
| `run/` | Entry points: TestBuildIndex, TestSearchIndex |

## Concrete Class Template

Every impl class MUST follow this pattern:
1. Extend the corresponding `Abstract*` class
2. Override `equals(Object obj)` — compare all fields
3. Override `toString()` — human-readable representation
4. Implement `FileSerializable` methods (`writeObject`, `readObject`) if the parent implements it
5. Provide both default and parameterized constructors

## Adding a New Filter

To add a new TermTuple filter:
1. Create `parse/impl/XxxTermTupleFilter.java` extending `AbstractTermTupleFilter`
2. Constructor takes `AbstractTermTupleStream input`, calls `super(input)`
3. Override `next()` — loop calling `input.next()`, return matching tuples, skip non-matching
4. Register in `DocumentBuilder.build()` by wrapping the stream:
   ```java
   stream = new XxxTermTupleFilter(stream);
   ```

## Adding a New DocumentBuilder Feature

Modify `DocumentBuilder.build(int docId, String docPath, File file)`:
- All filter wrapping happens here before calling `build(docId, docPath, stream)`
- Order matters: Scanner first, then filters in sequence

## Index Serialization

`Index.writeObject(ObjectOutputStream)` and `readObject(ObjectInputStream)`:
- Write/read `docIdToDocPathMapping`: size → key-value pairs
- Write/read `termToPostingListMapping`: size → Term → PostingList
- Each element delegates to its own `writeObject`/`readObject`
- **Must be symmetric** — read in the exact order written

## Anti-Patterns (DO NOT DO)

### ❌ Monolithic Token Processing
Never process tokens in a single monolithic loop. The parsing pipeline uses the **Decorator Pattern** for a reason:
```java
// WRONG — monolithic, hard to extend
while (token != null) {
    if (pattern.matches(token) && !isStopWord(token) && lenOK(token)) {
        // all logic in one place
    }
}
// CORRECT — each concern is a separate Filter in the chain
stream = new TermTupleScanner(reader);
stream = new PatternTermTupleFilter(stream);
stream = new StopWordTermTupleFilter(stream);
stream = new LengthTermTupleFilter(stream);
// Add new filters by appending to the chain, never by modifying existing filters
```

### ❌ Adding Concrete Methods to Abstract Classes
Abstract classes in `index/`, `parse/`, `query/` define the **contract only**. Never add implementation logic to them:
```java
// WRONG — logic in abstract class
public abstract class AbstractIndex {
    public void optimize() { /* concrete logic here */ }
}
// CORRECT — logic in impl/ subclass
public class Index extends AbstractIndex {
    @Override
    public void optimize() { /* concrete logic here */ }
}
```
The ONLY exception is when the abstract class already has a concrete method in the original design (e.g., `AbstractTerm.hashCode()`).

### ❌ Breaking Serialization Symmetry
`FileSerializable` requires that `writeObject` and `readObject` are exact mirrors:
- If you write `out.writeInt(size)` first, you MUST read `in.readInt()` first
- If you add a field to `writeObject`, you MUST add the corresponding read to `readObject`
- A mismatch causes `IOException` or silently corrupt data

### ❌ Skipping `equals()` and `toString()`
Every `impl/*` concrete class MUST implement both. These are used by:
- `TreeMap` key comparison (equals)
- `Index.toString()` for human-readable output
- `Hit.toString()` for search result display
