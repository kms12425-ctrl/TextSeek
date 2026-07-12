[中文](./README.md) | [English](./README.en.md)

# SearchEngineForStudent

`SearchEngineForStudent` is a full-stack search project that evolved from a command-line search engine. It keeps the inverted-index-based Java core, then builds a Spring Boot backend and a React frontend around the full workflow of document import, index construction, keyword search, and result presentation.

The project is useful not only because it "works", but because it shows how a course-style Java project can be upgraded into an engineering-oriented web application:

- keep the original pure Java search core for algorithm learning
- expose standard REST APIs through Spring Boot
- provide interactive search, upload, management, and detail pages with React
- include Docker-based deployment entry points for local demos and future extension

## Project Goals

This project mainly aims to:

1. turn a command-line search engine into a web-accessible application
2. separate the system into core logic, backend service, and frontend UI layers
3. leave room for future work such as ranking upgrades, Chinese tokenization, auth, and production deployment

If you are learning Java, Spring Boot, frontend-backend separation, or how to modernize a coursework project into a portfolio-ready system, this repository is a practical reference.

## Core Features

- Document upload for importing text files into the system
- Index management for initialization, loading, saving, and rebuilds
- Keyword search based on an inverted index
- Search result presentation with matched documents, scores, and snippets
- Document management for listing, viewing, and deleting indexed files
- Document detail view for checking the original content
- Status inspection for basic index or system statistics

## Architecture

The project uses a typical separated frontend/backend structure while keeping the search engine core independent:

```text
React Web (search-engine-web)
        |
        | HTTP / JSON
        v
Spring Boot API (search-engine-server)
        |
        | Java service calls
        v
Search Core (search-engine-core)
        |
        | read / write
        v
data/ documents + index files
```

### Architecture Notes

- `search-engine-core` is the foundational layer
  It handles document parsing, token filtering, inverted index building, query execution, and result assembly.
- `search-engine-server` is the service layer
  It exposes the core engine through HTTP APIs and manages uploads, deletion, and runtime state.
- `search-engine-web` is the user interaction layer
  It presents search, document management, and detail pages.
- `data` is the runtime data directory
  It stores uploaded documents and serialized index files.

## Tech Stack

### Backend

- Java 21
- Maven multi-module project
- Spring Boot 3.2.5
- RESTful APIs

### Frontend

- React
- TypeScript
- Vite
- React Router

### Engineering and Deployment

- Docker
- Docker Compose
- GitHub Actions configuration directory

## Module Guide

### 1. `search-engine-core`

This is the most important foundational module and the closest part to actual search-engine internals. It is responsible for:

- document content parsing
- term filtering and normalization
- inverted index construction and maintenance
- query execution
- matched document ranking and return

Recommended entry points:

- [`search-engine-core/README.en.md`](./search-engine-core/README.en.md)
- [`search-engine-core/src/main/java/hust/cs/javacourse/search/index/README.en.md`](./search-engine-core/src/main/java/hust/cs/javacourse/search/index/README.en.md)
- [`search-engine-core/src/main/java/hust/cs/javacourse/search/parse/README.en.md`](./search-engine-core/src/main/java/hust/cs/javacourse/search/parse/README.en.md)
- [`search-engine-core/src/main/java/hust/cs/javacourse/search/query/README.en.md`](./search-engine-core/src/main/java/hust/cs/javacourse/search/query/README.en.md)

### 2. `search-engine-server`

This module turns the search core into a service. It wraps the engine with frontend-friendly APIs and manages runtime documents and index state.

The backend currently centers around:

- `controller`: HTTP entry points and unified responses
- `service`: business logic coordinating documents, index operations, and search
- `dto`: request and response models
- `config`: runtime configuration such as paths and cross-origin settings

Useful references:

- [`search-engine-server/README.en.md`](./search-engine-server/README.en.md)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/README.en.md`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/README.en.md)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/service/README.en.md`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/service/README.en.md)

### 3. `search-engine-web`

This is the frontend application. It turns backend search capabilities into directly usable pages.

The main pages currently include:

- home search page
- document management page
- document detail page

Useful references:

- [`search-engine-web/README.en.md`](./search-engine-web/README.en.md)
- [`search-engine-web/src/pages/README.en.md`](./search-engine-web/src/pages/README.en.md)
- [`search-engine-web/src/api/README.en.md`](./search-engine-web/src/api/README.en.md)
- [`search-engine-web/src/components/README.en.md`](./search-engine-web/src/components/README.en.md)

## Directory Overview

- [`.github`](./.github/README.en.md): CI, automation, and repository collaboration settings
- [`.vscode`](./.vscode/README.en.md): local editor settings
- [`bin`](./bin/README.en.md): historical outputs or helper scripts
- [`data`](./data/README.en.md): runtime documents and index data
- [`docs`](./docs/README.en.md): project docs, upgrade plans, and milestone materials
- [`index`](./index/README.en.md): legacy index directory or historical output
- [`model`](./model/README.en.md): diagrams and design materials
- [`search-engine-core`](./search-engine-core/README.en.md): core search engine module
- [`search-engine-server`](./search-engine-server/README.en.md): backend service module
- [`search-engine-web`](./search-engine-web/README.en.md): frontend application module

## Environment Requirements

Recommended local environment:

- JDK 21
- Maven 3.9 or a compatible version
- Node.js 18+ or newer
- npm

You can run only the backend if you want API access only, or run both frontend and backend for the full experience.

## Quick Start

### Option 1: Local Development

#### 1. Build the backend

```bash
mvn -pl search-engine-server -am package -DskipTests
```

This builds the backend together with its dependent core module.

#### 2. Start the backend

```bash
java -jar search-engine-server/target/search-engine-server-1.0.0-SNAPSHOT.jar
```

Default backend address:

- Backend: `http://localhost:8080`

#### 3. Install frontend dependencies

```bash
cd search-engine-web
npm install
```

#### 4. Start the frontend dev server

```bash
npm run dev
```

Default frontend address:

- Frontend: `http://localhost:5173`

In development mode, Vite proxies `/api` requests to `http://localhost:8080`.

### Option 2: Build Frontend Assets Only

```bash
cd search-engine-web
npm run build
```

This produces the production frontend bundle and is useful for validating the frontend build.

### Option 3: Docker Deployment

If Docker and Docker Compose are installed locally, you can try:

```bash
docker-compose up --build
```

The repository already includes:

- [`Dockerfile`](./Dockerfile)
- [`docker-compose.yml`](./docker-compose.yml)

This is a good starting point for a one-command demo environment.

## Runtime Data

The project relies on the `data` directory during execution:

- `data/documents/`: uploaded source documents
- `data/index/`: stored index files

Backend configuration lives in:

- [`search-engine-server/src/main/resources/application.yml`](./search-engine-server/src/main/resources/application.yml)

Default settings include:

- server port `8080`
- document directory `./data/documents/`
- index directory `./data/index/`
- upload size limit `10MB`

## API Capability Overview

Based on the current code structure, the backend mainly exposes three groups of capabilities:

### Search APIs

- keyword search
- search result pagination and presentation support

### Document APIs

- upload documents
- list documents
- view a single document
- delete a document

### Status APIs

- inspect current index or system status

Implementation references:

- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/SearchController.java`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/SearchController.java)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/DocumentController.java`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/DocumentController.java)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/StatusController.java`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/StatusController.java)

## Suggested Reading Order

If this is your first time opening the repository, this order works well:

1. read this README to understand the goals and module boundaries
2. read [`docs/全栈升级计划.md`](./docs/%E5%85%A8%E6%A0%88%E5%8D%87%E7%BA%A7%E8%AE%A1%E5%88%92.md) for the overall modernization path
3. inspect `search-engine-core` to understand the search internals
4. inspect `search-engine-server` to see how the core becomes APIs
5. inspect `search-engine-web` to see how the UI is organized

## Current Progress

Given the current repository state, the project has already completed the key upgrade from a course-style search engine into a runnable full-stack application:

- multi-module structure is in place
- backend APIs are implemented
- frontend pages are implemented
- the main flows for upload, search, detail view, and deletion are connected
- bilingual README coverage has been added across the project

So at this stage, it is no longer only an algorithm exercise. It is a solid base for a more polished portfolio project.

## Future Improvements

Strong next steps would be:

- ranking upgrades from simple term-frequency scoring to TF-IDF or BM25
- Chinese search support with tokenization instead of English-only term filtering
- better search UX with highlighting, snippets, related terms, and empty-state guidance
- more efficient indexing to avoid full rebuilds after every content change
- authentication and authorization
- moving some metadata persistence from plain files to a database
- stronger automated tests for the core, API, and frontend flows
- more production-ready Docker, CI/CD, and environment management

## Related Documents

- [`docs/README.en.md`](./docs/README.en.md)
- [`docs/全栈升级计划.md`](./docs/%E5%85%A8%E6%A0%88%E5%8D%87%E7%BA%A7%E8%AE%A1%E5%88%92.md)
- [`search-engine-core/README.en.md`](./search-engine-core/README.en.md)
- [`search-engine-server/README.en.md`](./search-engine-server/README.en.md)
- [`search-engine-web/README.en.md`](./search-engine-web/README.en.md)

## License

The repository includes a [`LICENSE`](./LICENSE) file. If you plan to publish the project more broadly, it would be helpful to also clarify usage and contribution expectations in the repository description or additional docs.
