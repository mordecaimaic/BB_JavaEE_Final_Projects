# BB\_JavaEE\_Final\_Projects

这是 BB（项目负责人）的 JavaEE 最终项目仓库，包含两个独立的 Maven Web 应用：

1. **Jsp\_College\_System**：基于 Spring Boot 的框架骨架，目前提供项目结构和基本依赖，可用于后续业务开发。
2. **SpringMvc\_quick\_start**：校园小助手项目，已将最初的 BB JSP+Servlet 版本完整迁移到 SSM（Spring + Spring MVC + MyBatis）架构，实现了用户认证、课程、公告、作业、资料和日程管理等核心功能。

> **说明：** 两个模块各自为独立的 Maven 项目，并未使用父级 POM。

---

## 一、项目目录概览

```
BB_JavaEE_Final_Projects
├─ .gitignore
├─ Jsp_College_System        # Spring Boot 骨架项目
│  ├─ pom.xml
│  └─ src/...
└─ SpringMvc_quick_start     # 校园小助手 SSM 项目
   ├─ pom.xml
   ├─ src
   │  ├─ main
   │  │  ├─ java/com/example
   │  │  │  ├─ config              # Spring 根上下文配置（数据源、事务、MyBatis）
   │  │  │  ├─ controller          # Web 控制器层
   │  │  │  ├─ mapper              # MyBatis Mapper 接口
   │  │  │  ├─ model               # 实体类
   │  │  │  └─ service             # Service 层接口与实现
   │  │  ├─ resources              # 资源目录
   │  │  │  ├─ mybatis-config.xml  # MyBatis 全局配置
   │  │  │  └─ mapper/*.xml        # MyBatis XML 映射文件
   │  │  └─ webapp
   │  │     └─ WEB-INF             # Web 配置
   │  │         ├─ jsp             # JSP 页面
   │  │         ├─ web.xml         # 部署描述符
   │  │         ├─ applicationContext.xml  # Spring 核心配置
   │  │         └─ dispatcher-servlet.xml  # Spring MVC 配置
   └─ README.md                   # 本说明文档
```

---

## 二、环境准备

1. **JDK 版本**：Java 17，执行 `java -version` 应显示 17.x。
2. **数据库**：MySQL 5.7+。

    * 使用提供的 `db_school.sql` 脚本创建数据库和表结构：

      ```bash
      mysql -u root -p < db_school.sql
      ```
3. **IDE 与构建工具**：

    * IntelliJ IDEA Ultimate 或 Community
    * Maven 3.6+
4. **Web 服务器**：Apache Tomcat 9/10/11，需支持 Java 17。

---

## 三、SpringMvc\_quick\_start 模块配置与运行

下面以 `SpringMvc_quick_start` 模块为例，详细说明如何在 IDEA 中导入、配置和运行。

### 1. 导入项目

1. 打开 IDEA，选择 **File → Open...**，定位至 `SpringMvc_quick_start` 根文件夹，点击 **Open**。
2. 在右侧的 Maven 工具窗口，点击 **Reimport All Maven Projects**（或 IDEA 自动提示时选择 **Import Changes**）。
3. 确认 **External Libraries** 已包含 Spring、MyBatis、HikariCP、MySQL Connector 等依赖。

### 2. 配置数据库连接

1. 打开 `src/main/webapp/WEB-INF/applicationContext.xml`。
2. 找到 `<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">` 节点，修改属性：

   ```xml
   <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/db_school?useUnicode=true&amp;characterEncoding=utf-8&amp;allowPublicKeyRetrieval=true"/>
   <property name="username" value="root"/>
   <property name="password" value="1234"/>
   <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
   ```
3. 保存文件。

### 3. 标记资源目录

1. 在 IDEA 中，右键 `src/main/resources`，选择 **Mark Directory as → Resources Root**。
2. 确认 `mybatis-config.xml` 和 `mapper/*.xml` 都被识别为资源文件。

### 4. 配置 Tomcat 运行环境

1. 打开 **Run → Edit Configurations...**。
2. 点击左上角 **+**，选择 **Tomcat Server → Local**。
3. 在 **Server** 选项卡：

    * **Tomcat Home**：填写本地 Tomcat 安装路径（如 `D:/APP/JavaEE/apache-tomcat-11.0.4/...`）。
    * **JRE**：选择 Java 17。
4. 在 **Deployment** 选项卡：

    * 点击 **+ → Artifact**，选择 **SpringMvc\_quick\_start\:war exploded**。
    * **Application context** 填写 `/`。
5. 在 **Startup/Connection** 选项卡的 **VM options** 中添加：

   ```text
   -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8
   ```
6. 点击 **Apply** 并 **OK**。

### 5. 启动并访问

1. 在 IDEA 工具栏选择刚建的 Tomcat 配置，点击 **Run**（绿色箭头）。
2. 等待 Tomcat 完成启动，控制台出现 `Server startup in ... ms`。
3. 浏览器打开：

   ```
   http://localhost:8080/
   ```
4. 如果出现登录页面，说明部署成功。

---

## 四、常见问题排查

| 问题              | 解决方案                                                                                                                                                                                       |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 页面或控制台乱码        | 1. 在 JSP 顶部添加 `<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>`；     2. 在 `web.xml` 中配置 `CharacterEncodingFilter`；     3. 在 VM options 添加 `-Dfile.encoding=UTF-8`。 |
| Mapper XML 不生效  | 检查 `mybatis-config.xml` 中 `<mapper resource="mapper/XXXMapper.xml"/>` 路径与实际文件是否一致；并确认已标记资源目录。                                                                                              |
| 数据库连接失败（URL 为空） | 确认 `applicationContext.xml` 中 DataSource 配置正确；执行 Maven **Clean & Rebuild**；并重新部署应用。                                                                                                        |
| 端口被占用           | 修改 Tomcat HTTP 端口，或停止占用该端口的其他服务。                                                                                                                                                           |

---

*编写：校园小助手开发团队*
