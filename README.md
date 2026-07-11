[中文](./README.md) | [English](./README.en.md)

# SearchEngineForStudent

这是一个从命令行搜索引擎逐步升级而来的全栈项目，当前包含以下三个核心模块：

- `search-engine-core`：纯 Java 实现的搜索引擎核心能力
- `search-engine-server`：基于 Spring Boot 的后端接口服务
- `search-engine-web`：基于 React 的前端页面

## 目录概览

- [`.github`](./.github/README.md)：CI、自动化与仓库辅助配置
- [`.vscode`](./.vscode/README.md)：本地编辑器配置
- [`bin`](./bin/README.md)：历史构建产物或脚本输出目录
- [`data`](./data/README.md)：运行期文档与索引数据
- [`docs`](./docs/README.md)：项目文档与升级计划
- [`index`](./index/README.md)：旧版索引输出目录
- [`model`](./model/README.md)：类图与结构设计资料
- [`search-engine-core`](./search-engine-core/README.md)：核心检索引擎模块
- [`search-engine-server`](./search-engine-server/README.md)：后端接口模块
- [`search-engine-web`](./search-engine-web/README.md)：前端页面模块

## 快速开始

### 后端

```bash
mvn -pl search-engine-server -am package -DskipTests
java -jar search-engine-server/target/search-engine-server-1.0.0-SNAPSHOT.jar
```

建议使用 Java 21 运行。

### 前端

```bash
cd search-engine-web
npm run build
```

开发模式下，前端通过 Vite 代理访问 `http://localhost:8080/api`。

## 当前状态

- 前后端已可独立构建
- 上传、搜索、详情、删除主链路已经打通
- 文档结构已补齐，便于继续迭代和维护
