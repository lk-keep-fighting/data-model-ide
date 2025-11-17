# 数据模型设计器 - 完整实施指南

## 项目概述

本项目实现了一个完整的数据模型管理系统，包含后端API和前端UI界面。

### 技术架构

**后端 (已有)**
- Spring Boot 3.5.5
- Java 17
- Maven 多模块项目

**前端 (新增)**
- React 19 + TypeScript
- Vite 7 (构建工具)
- Tailwind CSS (样式)
- shadcn/ui (组件库)
- Axios (HTTP客户端)

## 项目结构

```
data-model-solution/
├── data-model-core/          # 核心数据模型定义
├── data-model-sdk/           # SDK服务层
├── data-model-ide/           # 后端API服务
│   └── src/main/java/
│       └── controller/
│           ├── DatabaseController.java    # 数据库管理API
│           ├── DataModelController.java   # 数据模型API
│           └── CrudController.java        # CRUD操作API
└── data-model-ui/            # 前端UI (新增)
    ├── src/
    │   ├── components/
    │   │   ├── ui/             # shadcn UI组件
    │   │   ├── database/       # 数据库管理组件
    │   │   ├── datamodel/      # 数据模型组件
    │   │   └── crud/           # CRUD组件
    │   ├── services/           # API服务封装
    │   ├── types/              # TypeScript类型
    │   └── pages/              # 页面组件
    ├── package.json
    ├── vite.config.ts
    └── tailwind.config.js
```

## 功能模块

### 1. 数据库管理模块

**后端API**
- `GET /api/ide/database/list` - 获取数据库列表
- `GET /api/ide/database/{dbId}/tables` - 获取数据库表列表

**前端组件**
- `DatabaseList.tsx` - 数据库和表列表展示
  - 显示所有数据库
  - 点击数据库查看表列表
  - 从表创建数据模型

### 2. 数据模型管理模块

**后端API**
- `GET /api/data-model/list` - 获取数据模型列表
- `GET /api/data-model/get/{id}` - 获取单个模型详情
- `POST /api/data-model/createOrSave/{id}` - 创建/保存模型
- `DELETE /api/data-model/delete/{id}` - 删除模型
- `POST /api/data-model/autoCreate/db/{dbName}/table/{tableName}` - 从数据库表自动创建模型
- `POST /api/data-model/autoCreate/table/{tableName}` - 从表自动创建模型

**前端组件**
- `DataModelList.tsx` - 模型列表展示
  - 显示所有数据模型
  - 编辑/删除模型
  - 进入数据管理界面
- `DataModelEditor.tsx` - 模型编辑器
  - 创建新模型
  - 编辑模型属性
  - 配置字段定义

### 3. CRUD数据管理模块

**后端API**
- `POST /api/data-model/crud/{dataModelId}/add` - 新增数据
- `POST /api/data-model/crud/{dataModelId}/batch-add` - 批量新增
- `PUT /api/data-model/crud/{dataModelId}/edit/{dataId}` - 编辑数据
- `GET /api/data-model/crud/{dataModelId}/get/{dataId}` - 获取单条数据
- `POST /api/data-model/crud/{dataModelId}/query` - 查询数据
- `POST /api/data-model/crud/{dataModelId}/query-page` - 分页查询
- `DELETE /api/data-model/crud/{dataModelId}/delete/{dataId}` - 删除数据

**前端组件**
- `CrudDataManager.tsx` - 数据管理界面
  - 分页数据展示
  - 新增数据
  - 编辑数据
  - 删除数据
  - 搜索功能

## 使用指南

### 启动后端服务

```bash
cd data-model-ide
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 启动前端开发服务器

```bash
cd data-model-ui
npm install  # 首次运行需要安装依赖
npm run dev
```

前端服务将在 `http://localhost:3000` 启动，并自动代理API请求到后端。

### 构建前端生产版本

```bash
cd data-model-ui
npm run build
```

构建产物将在 `dist/` 目录下生成，可以部署到任何静态资源服务器。

## 用户操作流程

### 流程1：从数据库创建模型

1. 打开应用，进入"数据库管理"标签
2. 点击查看某个数据库
3. 在表列表中找到目标表
4. 点击"创建模型"按钮
5. 系统自动从表结构创建数据模型
6. 切换到"数据模型"标签查看新创建的模型

### 流程2：手动创建模型

1. 进入"数据模型"标签
2. 点击"新建模型"按钮
3. 填写模型信息：
   - 模型ID（必填，唯一标识）
   - 模型名称（必填）
   - 类型、分组、主表（可选）
   - 字段列表
4. 点击"保存"
5. 模型创建完成

### 流程3：管理模型数据

1. 在"数据模型"标签的列表中
2. 找到目标模型，点击"管理数据"
3. 进入数据管理界面，可以：
   - 查看数据列表（支持分页）
   - 点击"新增数据"添加记录
   - 点击编辑图标修改记录
   - 点击删除图标删除记录
   - 使用搜索框过滤数据
4. 点击"返回"回到模型列表

## UI组件说明

### shadcn/ui 组件

项目使用了以下shadcn组件：
- `Button` - 按钮组件
- `Card` - 卡片容器
- `Table` - 数据表格
- `Dialog` - 对话框/模态窗口
- `Input` - 文本输入框
- `Textarea` - 多行文本框
- `Tabs` - 标签页切换

所有组件都基于Tailwind CSS构建，支持完全自定义样式。

## API请求格式

### 标准响应格式

```typescript
{
  code: number,      // 0表示成功，其他表示错误
  msg?: string,      // 错误消息
  data?: T,          // 响应数据
  error?: {          // 错误详情
    code: number,
    msg: string,
    detail: any
  }
}
```

### 查询输入格式

```typescript
{
  conditions?: any[],    // 查询条件
  orderBy?: string[],    // 排序字段
  page?: number,         // 页码（从1开始）
  pageSize?: number      // 每页条数
}
```

### 分页响应格式

```typescript
{
  list: T[],           // 数据列表
  total: number,       // 总记录数
  page: number,        // 当前页码
  pageSize: number     // 每页条数
}
```

## 开发注意事项

### 前端开发

1. **类型安全**：所有API调用都有TypeScript类型定义
2. **错误处理**：使用try-catch处理异步操作
3. **用户提示**：使用alert进行简单提示（可替换为toast组件）
4. **状态管理**：当前使用React useState，大型应用可考虑引入状态管理库

### 后端开发

1. **API路径**：遵循RESTful规范
2. **数据模型**：使用DataModel核心类
3. **异常处理**：controller层已处理异常并返回统一格式

## 扩展建议

### 短期改进

1. 添加Toast通知替代alert
2. 实现搜索功能的实际逻辑
3. 添加数据验证
4. 支持更多查询条件

### 长期规划

1. 添加用户认证和权限管理
2. 实现数据模型的版本控制
3. 支持复杂查询构建器
4. 添加数据导入/导出功能
5. 实现模型关系可视化
6. 支持自定义表单布局

## 部署方案

### 前端部署

**方案1：集成到Spring Boot**
将`npm run build`生成的dist目录复制到`data-model-ide/src/main/resources/static`

**方案2：独立部署**
- 使用Nginx部署前端静态资源
- 配置反向代理到后端API
- 支持前后端分离

### 后端部署

标准Spring Boot部署方式：
```bash
mvn clean package
java -jar data-model-ide/target/data-model-ide-*.jar
```

## 故障排查

### 前端无法连接后端

检查vite.config.ts中的proxy配置是否正确：
```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
  },
}
```

### 构建失败

确保已安装正确的依赖版本：
```bash
rm -rf node_modules package-lock.json
npm install
```

### TypeScript错误

运行类型检查：
```bash
npm run build
```

## 总结

本项目实现了一个完整的数据模型管理系统，涵盖了从数据库探索、模型设计到数据CRUD的全流程。前端采用现代化的React技术栈，提供了友好的用户界面和良好的用户体验。
