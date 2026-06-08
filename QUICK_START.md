# 🚀 快速入门指南

本指南将帮助你在5分钟内运行待办事项应用。

## 📋 前置条件检查

在开始之前，请确保已安装：

```bash
# 检查 Node.js 版本（需要 16+）
node --version

# 检查 npm 版本
npm --version

# 检查 Java 版本（需要 17+）
java -version

# 检查 Maven 版本（需要 3.6+）
mvn --version
```

## 💻 一键启动（推荐）

### 方式一：同时启动前后端

**终端1 - 启动后端：**
```bash
cd backend
mvn clean spring-boot:run
```

等待看到 `Started TodoAppApplication` 消息。

**终端2 - 启动前端：**
```bash
cd frontend
npm install
npm run dev
```

打开浏览器访问：**http://localhost:5173**

### 方式二：分别启动

**步骤1：启动后端**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
✅ 后端运行在：http://localhost:8080/api

**步骤2：启动前端**
```bash
cd frontend
npm install
npm run dev
```
✅ 前端运行在：http://localhost:5173

## 🎮 使用教程

### 基本操作

1. **添加任务**
   - 在输入框中输入任务内容
   - 按 Enter 或点击"添加"按钮

2. **完成任务**
   - 勾选任务前的复选框
   - 已完成的任务会显示删除线

3. **删除任务**
   - 点击任务右侧的🗑️按钮

4. **过滤任务**
   - 点击顶部的过滤按钮：全部/未完成/已完成

5. **清空任务**
   - 清空已完成：删除所有已勾选的任务
   - 全部清空：删除所有任务

### 数据持久化

✅ 前端本地存储：自动保存到浏览器 localStorage  
✅ 后端数据库：所有数据同步保存到 H2 数据库

## 🔧 环境配置

### 前端配置

`frontend/vite.config.js`：
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

### 后端配置

`backend/src/main/resources/application.properties`：
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
```

## 📊 常用命令

### 前端命令
```bash
cd frontend

# 开发模式（带热加载）
npm run dev

# 生产构建
npm run build

# 预览生产构建
npm run preview

# 清理依赖
npm clean cache --force
```

### 后端命令
```bash
cd backend

# 清理并构建
mvn clean install

# 运行应用
mvn spring-boot:run

# 运行测试
mvn test

# 跳过测试构建
mvn clean install -DskipTests
```

## 🐛 故障排除

### 问题1：前端无法连接到后端

**症状**：浏览器控制台显示 CORS 错误

**解决方案**：
```bash
# 1. 确认后端已启动
curl http://localhost:8080/api/todos

# 2. 检查 vite.config.js 中的代理配置
# 3. 重启前端开发服务器
```

### 问题2：Maven 依赖下载缓慢

**解决方案**：
```bash
# 1. 清理 Maven 缓存
rm -rf ~/.m2/repository

# 2. 使用阿里云镜像（可选）
# 编辑 ~/.m2/settings.xml，添加镜像配置

# 3. 重新构建
mvn clean install
```

### 问题3：端口被占用

**症状**：`Address already in use`

**解决方案**：
```bash
# 前端端口被占用（5173）
# 修改 vite.config.js：
export default defineConfig({
  server: {
    port: 5174  // 改为其他端口
  }
})

# 后端端口被占用（8080）
# 修改 application.properties：
server.port=8081  # 改为其他端口
```

### 问题4：npm 安装速度慢

**解决方案**：
```bash
# 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 或使用 yarn
npm install -g yarn
cd frontend
yarn install
yarn dev
```

### 问题5：Java 版本过低

**症状**：`Unsupported class-file format` 错误

**解决方案**：
```bash
# 检查当前 Java 版本
java -version

# 需要 Java 17+，升级 JDK
# 然后设置 JAVA_HOME 环境变量
```

## 📱 访问应用

| 平台 | URL |
|------|-----|
| 前端 | http://localhost:5173 |
| 后端 API | http://localhost:8080/api |
| H2 数据库控制台 | http://localhost:8080/api/h2-console |

## 🔍 调试技巧

### 查看浏览器本地存储
```javascript
// 在浏览器控制台执行
console.log(localStorage.getItem('todos'))
```

### 查看数据库中的数据
1. 访问：http://localhost:8080/api/h2-console
2. JDBC URL：`jdbc:h2:mem:testdb`
3. 用户名：`sa`
4. 密码：留空
5. 点击 Connect

### 检查 API 响应
```bash
# 获取所有任务
curl http://localhost:8080/api/todos

# 创建新任务
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"text":"测试任务","completed":false}'
```

## 💡 常见问题

**Q：数据会保存到数据库吗？**  
A：会的。前端会自动同步到本地存储，后端会持久化到 H2 数据库。

**Q：关闭浏览器后数据会丢失吗？**  
A：不会。数据保存在本地存储中，刷新页面后仍然存在。

**Q：可以切换到 MySQL 吗？**  
A：可以。修改 `application.properties` 配置即可。

**Q：如何修改端口？**  
A：
- 前端：修改 `vite.config.js` 中的 `server.port`
- 后端：修改 `application.properties` 中的 `server.port`

## 🎉 成功标志

当你看到以下内容时，说明应用已成功运行：

✅ 前端：http://localhost:5173 显示待办事项界面  
✅ 后端：终端显示 `Started TodoAppApplication`  
✅ 可以添加、编辑、删除任务  
✅ 刷新页面后任务仍然存在

## 📞 获取帮助

如遇到问题：

1. 查看本指南的"故障排除"部分
2. 检查 GitHub Issues
3. 查看详细的 README.md
4. 检查控制台的错误信息

---

祝你使用愉快！🎉
