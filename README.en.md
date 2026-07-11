[中文](./README.md) | [English](./README.en.md)

# SearchEngineForStudent

This project evolved from a command-line search engine into a full-stack application with three main modules:

- `search-engine-core`: the pure Java search engine core
- `search-engine-server`: the Spring Boot backend API
- `search-engine-web`: the React frontend

## Directory Overview

- [`.github`](./.github/README.en.md): CI, automation, and repository helpers
- [`.vscode`](./.vscode/README.en.md): local editor settings
- [`bin`](./bin/README.en.md): historical build outputs or generated scripts
- [`data`](./data/README.en.md): runtime documents and index data
- [`docs`](./docs/README.en.md): project documents and upgrade plans
- [`index`](./index/README.en.md): legacy index output directory
- [`model`](./model/README.en.md): class diagrams and structure notes
- [`search-engine-core`](./search-engine-core/README.en.md): core search engine module
- [`search-engine-server`](./search-engine-server/README.en.md): backend service module
- [`search-engine-web`](./search-engine-web/README.en.md): frontend module

## Quick Start

### Backend

```bash
mvn -pl search-engine-server -am package -DskipTests
java -jar search-engine-server/target/search-engine-server-1.0.0-SNAPSHOT.jar
```

Java 21 is recommended.

### Frontend

```bash
cd search-engine-web
npm run build
```

In development mode, the frontend reaches the backend through the Vite proxy at `http://localhost:8080/api`.

## Current Status

- Backend and frontend can both be built successfully
- The main flows for upload, search, detail view, and deletion are connected
- The documentation structure is now ready for continued iteration
