# 数据模型设计器 - 前端UI实现

## 项目概述

本项目为数据模型管理系统提供了完整的前端UI界面，基于React + TypeScript + shadcn/ui构建，实现了数据库管理、数据模型设计和CRUD操作的全流程可视化管理。

## 快速开始

### 前提条件
- Node.js 16+ 
- npm 7+
- 后端服务运行在 `http://localhost:8080`

### 安装和运行

```bash
# 进入前端目录
cd data-model-ui

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 或使用快捷脚本
./start-dev.sh
```

访问 `http://localhost:3000` 查看应用。

### 构建生产版本

```bash
npm run build
```

构建产物在 `dist/` 目录下。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| React | 19.x | UI框架 |
| TypeScript | 5.x | 类型系统 |
| Vite | 7.x | 构建工具 |
| Tailwind CSS | 4.x | 样式框架 |
| Axios | 1.x | HTTP客户端 |
| Lucide React | 0.x | 图标库 |

## 项目结构

```
data-model-ui/
├── src/
│   ├── components/          # 组件目录
│   │   ├── ui/             # 基础UI组件（shadcn风格）
│   │   │   ├── button.tsx
│   │   │   ├── card.tsx
│   │   │   ├── dialog.tsx
│   │   │   ├── input.tsx
│   │   │   ├── table.tsx
│   │   │   ├── tabs.tsx
│   │   │   └── textarea.tsx
│   │   ├── database/       # 数据库管理组件
│   │   │   └── DatabaseList.tsx
│   │   ├── datamodel/      # 数据模型组件
│   │   │   ├── DataModelList.tsx
│   │   │   └── DataModelEditor.tsx
│   │   └── crud/           # CRUD操作组件
│   │       └── CrudDataManager.tsx
│   ├── lib/                # 工具函数
│   │   └── utils.ts        # 通用工具（cn等）
│   ├── services/           # API服务
│   │   └── api.ts          # API客户端封装
│   ├── types/              # TypeScript类型定义
│   │   └── api.ts          # API相关类型
│   ├── pages/              # 页面组件
│   │   └── MainPage.tsx    # 主页面
│   ├── App.tsx             # 应用根组件
│   ├── main.tsx            # 应用入口
│   └── index.css           # 全局样式
├── index.html              # HTML模板
├── vite.config.ts          # Vite配置
├── tsconfig.json           # TypeScript配置
├── tailwind.config.js      # Tailwind配置
├── postcss.config.js       # PostCSS配置
├── package.json            # 项目依赖
├── README.md               # 使用说明
├── FEATURES.md             # 功能说明
└── start-dev.sh            # 快速启动脚本
```

## 核心功能

### 1. 数据库管理 (DatabaseList.tsx)
- ✅ 查看所有数据库
- ✅ 浏览数据库表列表
- ✅ 从数据库表快速创建模型

### 2. 数据模型管理
#### DataModelList.tsx
- ✅ 显示所有数据模型
- ✅ 编辑模型
- ✅ 删除模型
- ✅ 进入数据管理

#### DataModelEditor.tsx  
- ✅ 创建新模型
- ✅ 编辑模型属性
- ✅ 配置字段列表
- ✅ 设置模型元数据

### 3. 数据CRUD (CrudDataManager.tsx)
- ✅ 分页数据展示
- ✅ 新增数据
- ✅ 编辑数据
- ✅ 删除数据
- ✅ 搜索框（UI已实现，可扩展逻辑）

## API集成

### 后端API端点

所有API都通过 `/api` 前缀访问，开发环境下自动代理到 `http://localhost:8080`。

#### 数据库管理
```
GET  /api/ide/database/list              # 获取数据库列表
GET  /api/ide/database/{dbId}/tables     # 获取表列表
```

#### 数据模型管理
```
GET    /api/data-model/list                              # 获取模型列表
GET    /api/data-model/get/{id}                          # 获取模型详情
POST   /api/data-model/createOrSave/{id}                 # 创建/保存模型
DELETE /api/data-model/delete/{id}                       # 删除模型
POST   /api/data-model/autoCreate/db/{db}/table/{table}  # 从DB表创建
```

#### CRUD操作
```
POST   /api/data-model/crud/{modelId}/add           # 新增
POST   /api/data-model/crud/{modelId}/batch-add     # 批量新增
PUT    /api/data-model/crud/{modelId}/edit/{id}     # 编辑
GET    /api/data-model/crud/{modelId}/get/{id}      # 查询单条
POST   /api/data-model/crud/{modelId}/query         # 查询
POST   /api/data-model/crud/{modelId}/query-page    # 分页查询
DELETE /api/data-model/crud/{modelId}/delete/{id}   # 删除
```

### API响应格式

```typescript
interface ApiResult<T> {
  code: number;      // 0=成功，其他=失败
  msg?: string;      // 错误消息
  data?: T;          // 响应数据
  error?: {
    code: number;
    msg: string;
    detail: any;
  };
}
```

## 组件设计

### shadcn/ui 组件实现

本项目包含了自定义实现的shadcn风格组件，基于Tailwind CSS：

- **Button**: 支持多种变体（default, outline, ghost, destructive等）
- **Card**: 卡片容器及子组件（Header, Title, Description, Content, Footer）
- **Dialog**: 模态对话框及子组件（Header, Title, Footer）
- **Input**: 文本输入框
- **Textarea**: 多行文本框
- **Table**: 数据表格及子组件（Header, Body, Row, Cell等）
- **Tabs**: 标签页切换组件

所有组件都使用了：
- TypeScript类型安全
- forwardRef支持ref传递
- className合并（使用cn工具函数）
- 一致的样式风格

### 业务组件架构

```
MainPage (主页面)
├── Tabs (标签切换)
│   ├── DatabaseList (数据库管理)
│   │   └── 显示数据库和表
│   └── DataModelList (模型列表)
│       ├── 显示模型列表
│       └── DataModelEditor (模型编辑器)
└── CrudDataManager (数据管理)
    ├── 数据表格
    └── 编辑对话框
```

## 开发指南

### 添加新组件

1. 在 `src/components/` 下创建组件文件
2. 使用TypeScript和函数组件
3. 遵循现有的命名约定
4. 使用Tailwind CSS类名

```tsx
import React from 'react';
import { Button } from '@/components/ui/button';

interface MyComponentProps {
  title: string;
}

export const MyComponent: React.FC<MyComponentProps> = ({ title }) => {
  return (
    <div className="p-4">
      <h2 className="text-xl font-bold">{title}</h2>
      <Button>Click Me</Button>
    </div>
  );
};
```

### 添加新API

1. 在 `src/types/api.ts` 添加类型定义
2. 在 `src/services/api.ts` 添加API函数

```typescript
// types/api.ts
export interface MyData {
  id: string;
  name: string;
}

// services/api.ts
export const myApi = {
  getData: () => api.get<any, ApiResult<MyData[]>>('/my-endpoint'),
};
```

### 样式定制

修改 `tailwind.config.js` 来定制主题：

```javascript
export default {
  theme: {
    extend: {
      colors: {
        primary: '#3b82f6',  // 自定义颜色
      },
    },
  },
}
```

## 部署

### 方案1：集成到Spring Boot

```bash
# 构建前端
cd data-model-ui
npm run build

# 复制到Spring Boot静态资源目录
cp -r dist/* ../data-model-ide/src/main/resources/static/

# 构建和运行Spring Boot
cd ../data-model-ide
mvn clean package
java -jar target/data-model-ide-*.jar
```

访问 `http://localhost:8080` 即可。

### 方案2：独立部署（Nginx）

```nginx
server {
    listen 80;
    server_name yourdomain.com;
    
    # 前端静态资源
    root /path/to/data-model-ui/dist;
    index index.html;
    
    # SPA路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 常见问题

### Q: 前端无法连接后端？
A: 检查后端服务是否在8080端口运行，查看vite.config.ts中的proxy配置。

### Q: 构建失败？
A: 删除node_modules和package-lock.json，重新运行npm install。

### Q: TypeScript报错？
A: 确保所有类型定义正确，运行 `npm run build` 查看详细错误。

### Q: 样式不生效？
A: 确保已正确配置Tailwind CSS和PostCSS，检查postcss.config.js。

## 性能优化建议

1. **代码分割**: 使用React.lazy()进行路由级代码分割
2. **虚拟滚动**: 大数据量表格使用虚拟滚动
3. **缓存策略**: 使用React Query或SWR进行数据缓存
4. **图片优化**: 使用WebP格式，懒加载图片
5. **Bundle优化**: 分析bundle大小，移除未使用的依赖

## 扩展功能建议

### 短期
- [ ] 添加Toast通知组件
- [ ] 实现搜索功能逻辑
- [ ] 添加加载状态指示器
- [ ] 表单验证增强
- [ ] 错误边界处理

### 中期
- [ ] 支持暗色主题
- [ ] 国际化(i18n)
- [ ] 导出Excel功能
- [ ] 批量操作
- [ ] 高级查询构建器

### 长期
- [ ] 用户认证和权限
- [ ] 模型版本控制
- [ ] 关系图可视化
- [ ] 自定义表单布局
- [ ] 插件系统

## 贡献指南

1. Fork项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 许可证

[根据项目需要添加许可证信息]

## 联系方式

[根据项目需要添加联系方式]

---

**最后更新**: 2025-10-29  
**版本**: 1.0.0
