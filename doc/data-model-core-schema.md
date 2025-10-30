# data-model-core 统一数据模型定义

为了同时覆盖 **数据存储（数据库）**、**数据查询（查询条件、输出表格）**、**数据提交（提交表单）**、**数据统计（统计条件、输出表格）** 四大场景，并让模型结构对人类与 AI 友好，`data-model-core` 新增了一组简洁的 Schema 定义：

```
ModelDefinition
 ├── StoreSchema      物理存储定义（数据库 / 表 / 列）
 ├── ViewSchema       逻辑视图与字段定义，解耦业务字段与物理字段
 ├── QuerySchema      查询默认列、固化筛选、排序与分页约束
 ├── SubmitSchema     表单提交约束（必填、只读、默认值、权限）
 └── AggregateSchema  统计指标、分组与固化筛选
```

## 1. 顶层模型 `ModelDefinition`

- 作为统一入口，包含模型的 `id`、`name` 等元信息，以及四大子模块的 schema。
- 提供 `toViewModel()`、`newQueryModel()`、`newSubmitModel(type)`、`newAggregateModel()` 快捷方法，将声明式定义转化为运行时模型（`ViewModel` / `QueryModel` / `SubmitModel` / `AggregateModel`），供 SQL 构建器等下游组件直接使用。
- 统一校验主表是否存在等关键约束，避免运行期缺失配置。

## 2. `StoreSchema` —— 面向数据库的存储定义

| 字段              | 说明                                           |
|-------------------|-----------------------------------------------|
| `mainTable`        | 主表逻辑别名                                   |
| `tables`           | 逻辑别名 -> `StoreTable`（库名、表名、主键等） |
| `columns`          | 物理列名 -> `StoreColumn`（类型、默认值、注释）|

- `putTable(alias, table)`：注册表，并在首次调用时自动设置 `mainTable`。
- `putColumn(column, meta)`：注册列元信息，若未指定 `meta.name` 会自动补齐。
- `requireMainStoreTable()`：校验主表是否存在。

## 3. `ViewSchema` —— 逻辑视图定义

| 字段          | 说明                                       |
|---------------|-------------------------------------------|
| `columns`      | 逻辑列名 -> `ViewColumn`（显示类型、标签、是否可查询/编辑/统计等） |
| `mappings`     | 逻辑列名 -> `StoreMapping`（物理表/列映射 + 列元信息）          |

- `putColumn(name, column)`：注册逻辑字段，自动补齐 `column.name`。
- `mapToStore(column, mapping)`：绑定物理存储，自动补齐 `mapping.viewColumn`。
- `toViewModel(ModelDefinition)`：在同一处保证视图/存储映射完整，生成 `ViewModel`。

## 4. `QuerySchema` —— 查询层定义

- `defaultSelectColumns`：默认输出列（输出表格结构）。
- `presetFilters`：固定过滤条件（如逻辑删除、租户隔离）。
- `defaultOrderBy`：默认排序。
- `sortableColumns`：允许排序的列集合，便于前端/AI 限制输入。
- `defaultPagination`：默认分页参数。

`newQueryModel()` 会复制以上定义，生成可进一步扩展的 `QueryModel`，同时避免共享引用导致的串改。

## 5. `SubmitSchema` —— 表单提交定义

- `allowInsert` / `allowUpdate`：操作权限开关。
- `requiredColumns`：必填字段集合。
- `readOnlyColumns`：前端不可编辑字段集合。
- `defaultValues`：初始化默认值，自动写入 `SubmitModel`。

`newSubmitModel(type)` 会在运行期校验权限，并按照默认值完成 `SubmitModel` 初始化。

## 6. `AggregateSchema` —— 统计定义

- `metrics`：`AggregateField` 列表，定义聚合函数、目标列及别名（输出表结构）。
- `groupBy`：分组列。
- `presetFilters`：统计场景独有的固定条件。

`newAggregateModel()` 自动将这些定义复制给 `AggregateModel`，配合 `MySqlBuilder` 即可生成统计 SQL。

## 7. 快速示例

```java
ModelDefinition user = new ModelDefinition()
        .setId("user")
        .setName("用户信息")
        .setMemo("主数据")
        .setVersion("v1");

user.getStore()
        .setMainTable("user")
        .putTable("user", new StoreTable().setDatabase("core").setTable("t_user").setPrimaryKey("id"));

user.getView()
        .putColumn("id", new ViewColumn().setLabel("编号"))
        .putColumn("name", new ViewColumn().setLabel("姓名"))
        .mapToStore("id", new StoreMapping().setStoreTable("user").setStoreColumn("id"))
        .mapToStore("name", new StoreMapping().setStoreTable("user").setStoreColumn("name"));

user.getQuery()
        .addDefaultColumn("id")
        .addDefaultColumn("name")
        .addPresetFilter(QueryCondition.eq("status", "ENABLED"))
        .addOrderBy("id DESC");

user.getSubmit()
        .require("name")
        .defaultValue("status", "ENABLED");

user.getAggregate()
        .addMetric(AggregateField.of(AggregateFunc.COUNT, "id", "total"))
        .addGroupBy("status");

ViewModel viewModel = user.toViewModel();
QueryModel queryModel = user.newQueryModel();
SubmitModel submitModel = user.newSubmitModel(SubmitType.INSERT);
AggregateModel aggregateModel = user.newAggregateModel();
```

## 8. 对 AI/人类友好的设计要点

1. **单一入口**：所有行为从 `ModelDefinition` 出发，避免同时追踪多份 JSON。
2. **强类型 + Builder 辅助**：通过链式写法减少样板代码，并借助 Lombok 保持简洁。
3. **复制语义**：在生成运行时模型时深拷贝关键结构，防止不同流程互相污染状态。
4. **默认值约束**：通过 `require`、`defaultValue`、`allow*` 等 API 直观表达业务规则，为前端/AI 提供清晰提示。
5. **渐进扩展**：旧的 DSL (`com.aims.datamodel.core.dsl`) 仍可继续使用；新 Schema 可逐步替换或通过适配器桥接。

通过以上梳理与优化，`data-model-core` 的数据模型结构能够在统一语义下同时覆盖存储、查询、提交、统计场景，且保持清晰、简洁，便于团队成员与 AI 代理协同构建和理解。