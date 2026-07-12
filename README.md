[中文](./README.md) | [English](./README.en.md)

# SearchEngineForStudent

`SearchEngineForStudent` 是一个从命令行搜索引擎逐步演进而来的全栈搜索系统。项目以倒排索引为核心，围绕“文档导入、索引构建、关键词搜索、结果展示”这一条完整链路，扩展出了可直接运行的 Spring Boot 后端和 React 前端。

这个项目的价值不只是“能搜”，还在于它比较完整地展示了一个课程式 Java 项目如何升级为工程化 Web 应用：

- 保留原有纯 Java 搜索核心，方便理解底层实现
- 通过 Spring Boot 暴露标准 REST API，完成服务化封装
- 通过 React 前端提供可交互的搜索、上传、管理和详情查看界面
- 提供 Docker 化部署入口，便于本地演示与后续扩展

## 项目目标

这个项目主要解决三个层面的事情：

1. 让原本只能在命令行中运行的搜索引擎具备 Web 化能力
2. 将核心算法、后端接口、前端页面分层，形成更清晰的模块结构
3. 为后续继续扩展排序算法、中文分词、权限系统或生产化部署预留空间

如果你正在学习 Java、Spring Boot、前后端分离，或者想把课程作业逐步升级成可展示项目，这个仓库会比较有参考价值。

## 核心功能

- 文档上传：支持将文本文件导入系统并参与索引
- 索引维护：后端负责索引初始化、加载、保存和必要时重建
- 关键词搜索：支持基于倒排索引的关键词检索
- 搜索结果展示：前端展示命中文档、分数和摘要信息
- 文档管理：支持查看已导入文档列表、详情和删除操作
- 文档详情：可查看文档原文内容，帮助验证搜索结果
- 状态查看：后端提供索引统计与运行状态信息

## 整体架构

项目采用典型的前后端分离结构，但搜索引擎本体依然保持独立：

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

### 架构说明

- `search-engine-core` 是底层核心模块
  负责文档解析、分词过滤、倒排索引构建、查询执行与结果组织
- `search-engine-server` 是服务层模块
  负责把核心能力包装成 HTTP 接口，并处理上传、删除、状态查询等业务
- `search-engine-web` 是用户交互层
  负责把搜索、文档管理和详情查看以页面形式呈现出来
- `data` 是运行时数据目录
  用于存放上传的文档以及序列化后的索引文件

## 技术栈

### 后端

- Java 21
- Maven 多模块工程
- Spring Boot 3.2.5
- RESTful API

### 前端

- React
- TypeScript
- Vite
- React Router

### 工程与部署

- Docker
- Docker Compose
- GitHub Actions 配置目录

## 模块说明

### 1. `search-engine-core`

这是整个项目最重要的基础模块，也是最接近“搜索引擎原理”的部分。它主要负责：

- 文档内容解析
- 词项过滤与规范化
- 倒排索引的建立与维护
- 查询执行
- 命中文档的排序与返回

如果你想理解项目的搜索原理，建议优先阅读：

- [`search-engine-core/README.md`](./search-engine-core/README.md)
- [`search-engine-core/src/main/java/hust/cs/javacourse/search/index/README.md`](./search-engine-core/src/main/java/hust/cs/javacourse/search/index/README.md)
- [`search-engine-core/src/main/java/hust/cs/javacourse/search/parse/README.md`](./search-engine-core/src/main/java/hust/cs/javacourse/search/parse/README.md)
- [`search-engine-core/src/main/java/hust/cs/javacourse/search/query/README.md`](./search-engine-core/src/main/java/hust/cs/javacourse/search/query/README.md)

### 2. `search-engine-server`

这是对核心模块的服务化封装。它的职责是将搜索能力包装成前端可调用的接口，并管理运行时文件与索引状态。

当前后端主要包含：

- `controller`：接口入口，负责接收请求并返回统一响应
- `service`：核心业务逻辑，协调文档、索引和搜索流程
- `dto`：接口请求和响应对象
- `config`：项目运行配置，例如资源路径和跨域等

可进一步查看：

- [`search-engine-server/README.md`](./search-engine-server/README.md)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/README.md`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/README.md)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/service/README.md`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/service/README.md)

### 3. `search-engine-web`

这是项目的前端界面，负责把后端搜索能力变成用户可以直接操作的页面。

当前主要页面包括：

- 首页搜索页：输入关键词并查看搜索结果
- 文档管理页：查看文档列表并执行上传或删除
- 文档详情页：查看指定文档内容

前端目录重点可以看：

- [`search-engine-web/README.md`](./search-engine-web/README.md)
- [`search-engine-web/src/pages/README.md`](./search-engine-web/src/pages/README.md)
- [`search-engine-web/src/api/README.md`](./search-engine-web/src/api/README.md)
- [`search-engine-web/src/components/README.md`](./search-engine-web/src/components/README.md)

## 当前目录总览

- [`.github`](./.github/README.md)：CI、自动化与仓库协作配置
- [`.vscode`](./.vscode/README.md)：本地开发编辑器设置
- [`bin`](./bin/README.md)：历史产物或辅助脚本目录
- [`data`](./data/README.md)：运行期文档与索引数据
- [`docs`](./docs/README.md)：项目文档、升级计划与阶段资料
- [`index`](./index/README.md)：旧版索引目录或历史输出
- [`model`](./model/README.md)：模型图、结构图等设计资料
- [`search-engine-core`](./search-engine-core/README.md)：核心搜索引擎模块
- [`search-engine-server`](./search-engine-server/README.md)：后端服务模块
- [`search-engine-web`](./search-engine-web/README.md)：前端应用模块

## 运行环境要求

在本地启动前，建议准备以下环境：

- JDK 21
- Maven 3.9 或兼容版本
- Node.js 18+ 或更高版本
- npm

如果你只想运行后端接口，可以不先启动前端；如果你想体验完整页面，需要同时启动前后端。

## 快速开始

### 方式一：本地开发启动

#### 1. 构建后端

```bash
mvn -pl search-engine-server -am package -DskipTests
```

这个命令会同时构建父工程依赖的核心模块和后端模块。

#### 2. 启动后端

```bash
java -jar search-engine-server/target/search-engine-server-1.0.0-SNAPSHOT.jar
```

启动后，默认服务地址为：

- 后端服务：`http://localhost:8080`

#### 3. 安装前端依赖

```bash
cd search-engine-web
npm install
```

#### 4. 启动前端开发环境

```bash
npm run dev
```

启动后，默认前端地址为：

- 前端页面：`http://localhost:5173`

开发模式下，Vite 会把 `/api` 请求代理到 `http://localhost:8080`。

### 方式二：只构建前端产物

```bash
cd search-engine-web
npm run build
```

这个命令会输出生产构建文件，适合做前端打包验证。

### 方式三：Docker 部署

如果你的本地已安装 Docker 与 Docker Compose，可以尝试：

```bash
docker-compose up --build
```

项目根目录已经提供：

- [`Dockerfile`](./Dockerfile)
- [`docker-compose.yml`](./docker-compose.yml)

适合后续继续完善为一键演示环境。

## 运行数据说明

项目运行过程中会依赖 `data` 目录中的内容：

- `data/documents/`：上传后的原始文档
- `data/index/`：索引文件存储位置

后端配置文件位于：

- [`search-engine-server/src/main/resources/application.yml`](./search-engine-server/src/main/resources/application.yml)

其中可以看到默认配置，例如：

- 服务端口为 `8080`
- 默认文档目录为 `./data/documents/`
- 默认索引目录为 `./data/index/`
- 上传大小限制为 `10MB`

## 接口能力概览

从当前代码结构来看，后端主要围绕三类能力组织：

### 搜索接口

- 关键词搜索
- 搜索结果分页与结果展示支持

### 文档接口

- 上传文档
- 获取文档列表
- 查看单篇文档详情
- 删除文档

### 状态接口

- 查询当前索引或系统状态

具体实现可查看：

- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/SearchController.java`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/SearchController.java)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/DocumentController.java`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/DocumentController.java)
- [`search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/StatusController.java`](./search-engine-server/src/main/java/hust/cs/javacourse/search/server/controller/StatusController.java)

## 适合怎样阅读这个项目

如果你是第一次接触这个仓库，可以按下面顺序看：

1. 先读本 README，理解项目目标和模块关系
2. 再看 [`docs/全栈升级计划.md`](./docs/%E5%85%A8%E6%A0%88%E5%8D%87%E7%BA%A7%E8%AE%A1%E5%88%92.md)，了解这次升级的整体思路
3. 接着看 `search-engine-core`，理解搜索引擎底层数据结构与处理流程
4. 然后看 `search-engine-server`，理解核心能力如何变成 API
5. 最后看 `search-engine-web`，理解前端如何组织页面和调用接口

## 当前完成度

结合目前仓库状态，可以认为这个项目已经完成了从“课程式搜索引擎”到“可运行全栈应用”的关键升级：

- 多模块工程结构已建立
- 后端 API 已落地
- 前端页面已落地
- 上传、搜索、详情、删除等主链路已打通
- 双语 README 文档框架已补齐

这意味着它已经不只是算法练习，而是一个可以继续打磨成作品集项目的基础版本。

## 后续可改进方向

如果你想继续提升这个项目，比较值得优先考虑的方向有：

- 排序优化：从简单词频排序进一步升级到 TF-IDF 或 BM25
- 中文支持：加入中文分词能力，而不是只面向英文词项过滤
- 搜索体验：增加高亮、摘要、相关词提示和空结果引导
- 索引效率：减少删除或新增文档后的全量重建成本
- 权限系统：加入登录、权限控制和用户级数据隔离
- 持久化升级：将部分元数据从文件系统迁移到数据库
- 测试补强：为核心模块、接口层和前端关键流程补齐自动化测试
- 部署完善：补齐生产级 Docker、CI/CD 与环境变量管理

## 相关文档

- [`docs/README.md`](./docs/README.md)
- [`docs/全栈升级计划.md`](./docs/%E5%85%A8%E6%A0%88%E5%8D%87%E7%BA%A7%E8%AE%A1%E5%88%92.md)
- [`search-engine-core/README.md`](./search-engine-core/README.md)
- [`search-engine-server/README.md`](./search-engine-server/README.md)
- [`search-engine-web/README.md`](./search-engine-web/README.md)

## License

项目根目录包含 [`LICENSE`](./LICENSE) 文件；如果你准备对外发布，建议在 README 或仓库说明中进一步明确使用和贡献方式。
