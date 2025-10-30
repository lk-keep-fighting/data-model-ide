# 数据模型设计器 UI

基于 React + TypeScript + Vite + shadcn/ui 的数据模型管理界面。

## 功能特性

### 1. 数据库管理
- 查看数据库列表
- 浏览数据库表
- 从数据库表自动创建数据模型

### 2. 数据模型管理
- 创建、编辑、删除数据模型
- 查看模型列表
- 配置模型字段和属性
- 设置模型元数据（类型、分组、主表等）

### 3. 数据CRUD操作
- 基于数据模型的数据查询（支持分页）
- 新增数据
- 编辑数据
- 删除数据
- 数据搜索

## 技术栈

- **React 19** - UI框架
- **TypeScript** - 类型安全
- **Vite** - 构建工具
- **Tailwind CSS** - 样式框架
- **shadcn/ui** - UI组件库
- **Axios** - HTTP客户端
- **Lucide React** - 图标库

## 开发

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

开发服务器将在 `http://localhost:3000` 启动，并自动代理 `/api` 请求到后端服务 `http://localhost:8080`。

### 构建生产版本
```bash
npm run build
```

## API端点

### 数据库管理
- `GET /api/ide/database/list` - 获取数据库列表
- `GET /api/ide/database/{dbId}/tables` - 获取数据库表列表

### 数据模型管理
- `GET /api/data-model/list` - 获取模型列表
- `GET /api/data-model/get/{dataModelId}` - 获取模型详情
- `POST /api/data-model/createOrSave/{dataModelId}` - 创建或保存模型
- `DELETE /api/data-model/delete/{dataModelId}` - 删除模型
- `POST /api/data-model/autoCreate/db/{dbName}/table/{tableName}` - 从数据库表创建模型

### CRUD操作
- `POST /api/data-model/crud/{dataModelId}/add` - 新增数据
- `PUT /api/data-model/crud/{dataModelId}/edit/{dataId}` - 编辑数据
- `GET /api/data-model/crud/{dataModelId}/get/{dataId}` - 获取单条数据
- `POST /api/data-model/crud/{dataModelId}/query` - 查询数据
- `POST /api/data-model/crud/{dataModelId}/query-page` - 分页查询数据
- `DELETE /api/data-model/crud/{dataModelId}/delete/{dataId}` - 删除数据

## 项目结构

```
src/
├── components/
│   ├── ui/           # shadcn UI组件
│   ├── database/     # 数据库管理组件
│   ├── datamodel/    # 数据模型组件
│   └── crud/         # CRUD操作组件
├── lib/              # 工具函数
├── pages/            # 页面组件
├── services/         # API服务
├── types/            # TypeScript类型定义
├── App.tsx           # 应用根组件
├── main.tsx          # 应用入口
└── index.css         # 全局样式
```

## 配置

### Vite配置
- 开发服务器端口: 3000
- API代理: `/api` -> `http://localhost:8080`

### TypeScript配置
- 严格模式已启用
- 支持路径别名 `@/*` -> `./src/*`
