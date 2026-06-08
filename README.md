# 📝 待办事项应用 (Todo App)

一个全栈的待办事项应用，包含 Java Spring Boot 后端和 Vue 3 前端，支持本地存储功能。

## 🎯 功能特性

### 前端 (Vue 3 + Vite)
- ✅ 完整的 CRUD 操作
- ✅ 本地存储（localStorage）
- ✅ 智能过滤（全部/未完成/已完成）
- ✅ 实时统计
- ✅ 流畅的动画效果
- ✅ 响应式设计（移动端友好）
- ✅ 时间戳记录

### 后端 (Spring Boot 3 + JPA)
- ✅ RESTful API 设计
- ✅ 数据库持久化
- ✅ 完整的业务逻辑
- ✅ 错误处理
- ✅ CORS 跨域支持
- ✅ 统计信息接口

## 📦 项目结构

```
.
├── frontend/                 # Vue 3 前端应用
│   ├── src/
│   │   ├── App.vue          # 主应用组件
│   │   └── main.js          # 应用入口
│   ├── index.html           # HTML 入口
│   ├── vite.config.js       # Vite 配置
│   └── package.json         # 依赖管理
│
└── backend/                  # Spring Boot 后端应用
    ├── src/
    │   ├── main/java/com/example/todoapp/
    │   │   ├── TodoAppApplication.java      # 应用主类
    │   │   ├── controller/TodoController.java
    │   │   ├── service/TodoService.java
    │   │   ├── repository/TodoRepository.java
    │   │   ├── entity/Todo.java
    │   │   └── dto/TodoDTO.java
    │   └── resources/
    │       └── application.properties
    └── pom.xml              # Maven 配置
```

## 🚀 快速开始

### 前置要求
- Node.js 16+
- Java 17+
- Maven 3.6+

### 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 开发模式（http://localhost:5173）
npm run dev

# 生产构建
npm run build

# 预览生产构建
npm run preview
```

### 后端启动

```bash
cd backend

# 使用 Maven 运行
mvn clean spring-boot:run

# 或使用 IDE 运行 TodoAppApplication.java
```

后端将在 `http://localhost:8080/api` 启动

## 🔌 API 接口

### 基础 URL
```
http://localhost:8080/api/todos
```

### 端点列表

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | `/todos` | 获取所有待办事项 |
| GET | `/todos/{id}` | 获取单个待办事项 |
| POST | `/todos` | 创建待办事项 |
| PUT | `/todos/{id}` | 更新待办事项 |
| DELETE | `/todos/{id}` | 删除待办事项 |
| GET | `/todos/stats` | 获取统计信息 |
| DELETE | `/todos/completed/clear` | 清空已完成的项 |
| DELETE | `/todos/all/clear` | 清空所有项 |

### 请求/响应示例

**创建待办事项：**
```bash
POST /api/todos
Content-Type: application/json

{
  "text": "完成项目文档",
  "completed": false
}
```

**响应：**
```json
{
  "id": 1,
  "text": "完成项目文档",
  "completed": false,
  "createdAt": "2026-06-08T13:30:00",
  "updatedAt": "2026-06-08T13:30:00"
}
```

## 💾 数据库

### 开发环境
- 数据库：H2（内存数据库）
- H2 控制台：http://localhost:8080/api/h2-console

### 生产环境
- 数据库：MySQL
- 修改 `application.properties` 配置数据库连接

### 数据库表结构
```sql
CREATE TABLE todos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  text VARCHAR(255) NOT NULL,
  completed BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🎨 前端特性详解

### 本地存储
```javascript
// 自动保存到 localStorage
localStorage.setItem('todos', JSON.stringify(todos))

// 自动加载
const saved = localStorage.getItem('todos')
```

### 智能过滤
- **全部**：显示所有待办事项
- **未完成**：只显示未完成的项
- **已完成**：只显示已完成的项

### 响应式设计
- 桌面版本（600px+）：宽卡片布局
- 平板版本（600px-）：优化的紧凑布局
- 移动版本：触摸友好的界面

## 🔐 安全特性

- ✅ 输入验证
- ✅ CORS 跨域配置
- ✅ SQL 注入防护（使用 JPA）
- ✅ XSS 防护（Vue 自动转义）

## 📊 性能优化

- ✅ 分页支持（可扩展）
- ✅ 按时间倒序排序
- ✅ 异步操作
- ✅ 组件级缓存
- ✅ 生产级构建优化

## 🛠️ 技术栈

### 前端
- Vue 3（Composition API）
- Vite 4
- ES6+

### 后端
- Spring Boot 3
- Spring Data JPA
- Lombok
- H2 / MySQL

## 📝 许可证

GNU General Public License v3.0

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📞 支持

如有问题或建议，请提交 Issue。

---

**开发者**: yourui  
**创建日期**: 2026-06-08  
**最后更新**: 2026-06-08
