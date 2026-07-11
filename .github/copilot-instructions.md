# SearchEngineForStudent — Project Guidelines

## Build & Run

```bash
# Build (from project root)
mvn clean package -f pom.xml

# Run Spring Boot server
java -jar search-engine-server/target/search-engine-server-1.0.0-SNAPSHOT.jar

# Or run directly (core CLI)
java -cp search-engine-core/target/classes hust.cs.javacourse.search.run.TestBuildIndex
java -cp search-engine-core/target/classes hust.cs.javacourse.search.run.TestSearchIndex
```

## Architecture

This is a **multi-module Maven project**:

```
search-engine-core/     → Pure Java inverted-index engine (no external deps)
search-engine-server/   → Spring Boot REST API (depends on core)
search-engine-web/      → React 18 + Vite + Ant Design frontend
```

### Core Design Pattern: Abstract Class + Impl Subclass

Every domain concept has an abstract class defining the contract, and a concrete `impl/*` class implementing it:
- `index/AbstractTerm.java` → `index/impl/Term.java`
- `index/AbstractIndex.java` → `index/impl/Index.java`
- `parse/AbstractTermTupleFilter.java` → `parse/impl/PatternTermTupleFilter.java`
- etc.

**Rule**: Never add concrete logic to `index/`, `parse/`, `query/` packages — only to `impl/` subpackages.

### Parsing Pipeline: Decorator Pattern

`AbstractTermTupleFilter` wraps another `AbstractTermTupleStream`, forming a chain:
```
TermTupleScanner → PatternFilter → StopWordFilter → LengthFilter
```

Adding a new filter: extend `AbstractTermTupleFilter`, implement `next()`, wire into `DocumentBuilder.build()`.

## Code Style

- **Java 21** source level (`--release 21`)
- **UTF-8** encoding for all source files
- All concrete classes must implement `equals()`, `toString()`, and `FileSerializable` (if applicable)
- Use `@Override` on all interface/abstract method implementations
- `TreeMap` for sorted maps (Term dictionary, docId mapping)

## Config

All configuration in `hust.cs.javacourse.search.util.Config`:
- `DOC_DIR` — text files directory
- `INDEX_DIR` — index output directory
- `STRING_SPLITTER_REGEX` — word splitting regex
- `TERM_FILTER_PATTERN` — keep only matching terms (default: `[a-zA-Z]+`)
- `TERM_FILTER_MINLENGTH` / `MAXLENGTH` — word length bounds
- `IGNORE_CASE` — lowercase all terms during indexing

## Testing

Test datasets are in `text/`:
- `功能测试数据集/` — 3 functional test files
- `真实测试数据集/` — 15 real-world test files

Run `TestBuildIndex` first to generate `index/index.dat`, then `TestSearchIndex` to verify search results.

## Chat Convention

At the end of every response, **always** append a `---` separator followed by a **"💡 下一步建议"** (Next Step Suggestion) block. This block should:

1. Provide exactly **one** actionable next step relevant to the current task
2. Be concise — one line, 15–30 Chinese characters
3. Relate to the project's overall goal (the 全栈升级计划 in `docs/全栈升级计划.md`)
4. Use one of these formats:
   - `👉 试试问我：「...」` — when suggesting the user ask a follow-up question
   - `👉 建议下一步：...` — when suggesting a concrete action
   - `👉 你可以继续：「...」` — when inviting the user to continue the current workflow

Example:
```
---
💡 下一步建议：👉 试试问我：「帮我开始阶段一，创建 Maven 多模块结构」
```
