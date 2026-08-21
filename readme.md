# Tengo-SSO 使用文档

## 项目简介

Tengo-SSO 是一套基于 Spring Boot 实现的轻量级单点登录（Single Sign-On）系统，支持一次登录、多应用共享认证。系统由 **SSO 认证中心** 和 **业务系统接入包** 两大部分组成，帮助研发团队快速为现有系统统一认证入口。

---

## 一、项目优点

### 1. 真正的分布式会话支持
Token 会话数据全部存储于 **Redis**，多节点部署时任意服务器都能共享同一会话状态，彻底解决集群环境下 Session 不一致的问题，支持水平扩容。

### 2. 客户端"零配置"接入
tengo-client 采用 Spring Boot Starter 模式，提供 `AutoConfiguration.imports` 自动装配。业务系统只需在 `application.yml` 里填一行 SSO 服务端地址，Token 过滤就自动生效，**无需手动声明任何 Bean**，对业务代码完全无侵入。

### 3. Token 安全双重保障
- Token 格式为 `userId@UUID`，不直接携带明文密码等敏感信息
- 每次验证成功后自动刷新过期时间（续期机制），活跃会话不断线，长期不活跃的会话自动过期

### 4. 内置登录限流，防暴力破解
登录接口集成基于 Redis 的滑动窗口限流器，默认 **60 秒内同一用户名最多请求 30 次**，超出即拒绝，有效抵御撞库攻击。

### 5. 客户端无状态验证
业务系统不保存任何 Token 数据，每次请求通过 `WebClient` 异步调用 SSO 服务端的 `/verify` 接口验证，认证逻辑始终与 SSO 服务端保持一致，避免数据不一致问题。

### 6. 统一响应封装
全项目使用 `R<T>` 统一响应格式（code + message + data），前后端协作成本低，异常处理通过全局异常处理器统一收口。

---

## 二、系统架构图

> 架构图





### 

> 登录认证时序







## 三、模块说明

项目为 Maven 多模块结构，共三个模块：

| 模块 | 说明 |
|------|------|
| **tengo-server** | SSO 认证中心，提供登录、注销、Token 验证等 HTTP 接口，独立部署 |
| **tengo-client** | 业务系统接入包（Spring Boot Starter），提供 Token 过滤器，无侵入接入 |
| **tengo-core** | 公共核心模块，包含数据模型、Redis 缓存、限流器、认证服务等共享代码 |

---

## 四、API 接口一览

SSO 服务端接口路径前缀：`/tengo-sso-server/sso`

### 4.1 登录
```
GET /tengo-sso-server/sso/login?username={username}&password={password}
```
成功返回：
```json
{
  "code": 200,
  "data": {
    "accessToken": "admin@3f8a2b1c-7d6e-4f5a-9c8b-1e2d3f4a5b6c"
  }
}
```

### 4.2 验证 Token
```
GET /tengo-sso-server/sso/verify
Header: Authorization: {accessToken}
```
成功返回：
```json
{
  "code": 200,
  "data": {
    "userId": "admin",
    "username": "admin",
    "sessionId": "admin@3f8a2b1c-7d6e-4f5a-9c8b-1e2d3f4a5b6c",
    "expired": 1724064000000
  }
}
```

### 4.3 注销
```
GET /tengo-sso-server/sso/logout
Header: Authorization: {accessToken}
```
成功后从 Redis 删除 Token，该 Token 立即失效，返回：
```json
{"code": 200, "data": null}
```

### 4.4 获取用户信息
```
GET /tengo-sso-server/sso/userinfo
Header: Authorization: {accessToken}
```
返回用户详细信息（用户名、邮箱、手机、部门、头像等）。

---

## 五、快速接入指南

### 5.1 环境准备

| 组件 | 版本要求 | 用途 |
|------|---------|------|
| JDK | 1.8+ | 运行环境 |
| Maven | 3.6+ | 构建工具 |
| MySQL | 5.7+ | 用户数据存储 |
| Redis | 4.0+ | Token 会话存储 + 限流计数 |

### 5.2 初始化数据库

执行 `tengo-server/src/main/resources/schema.sql`，脚本会自动：
1. 创建 `sso_db` 数据库和 `sso_user` 用户表
2. 插入默认管理员账号（用户名：`admin`，密码：`admin123`）

### 5.3 部署 SSO 服务端

修改 `tengo-server/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://your-host:3306/sso_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your-password
  redis:
    host: your-redis-host
    port: 6379
```

启动服务：
```bash
cd tengo-server && mvn spring-boot:run
```
服务默认运行在 `http://localhost:8080`，SSO 接口路径前缀为 `/tengo-sso-server`。

### 5.4 业务系统接入（以 Spring Boot 项目为例）

**第一步：** 在 `pom.xml` 引入客户端依赖：
```xml
<dependency>
    <groupId>com.tego.sso</groupId>
    <artifactId>tengo-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**第二步：** 在 `application.yml` 配置 SSO 服务端地址：
```yaml
client:
  tengo:
    sso:
      server-url: http://localhost:8080/tengo-sso-server
```

**第三步：** 启动即可，`SsoTokenFilter` 会自动对所有请求进行 Token 验证。未登录用户访问受保护接口时，自动返回 `{"code":401,"message":"请先登录"}`。

---

## 六、配置项说明

### 服务端配置（tengo-server/application.yml）

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `server.port` | 8080 | SSO 服务端口 |
| `server.servlet.context-path` | `/tengo-sso-server` | 接口路径前缀 |
| `tengo.sso.token-expire` | 1800（秒） | Token 有效期，默认 30 分钟 |
| `tengo.sso.refresh-token-expire` | 86400（秒） | Token 最大续期时间，默认 1 天 |

### 客户端配置（tengo-client/application.yml）

| 配置项 | 说明 |
|--------|------|
| `client.tengo.sso.server-url` | SSO 认证中心地址，必须填写 |

---

## 七、目录结构

```
tengo-sso/
├── pom.xml                                    # 根 POM，统一管理三个子模块
├── tengo-server/                              # SSO 认证中心（主服务）
│   ├── controller/
│   │   └── TengoSsoController.java            # 登录/验证/注销/用户信息接口
│   ├── service/
│   │   └── JwtTokenManager.java               # Token 的创建、验证、吊销
│   ├── config/
│   │   └── CorsConfig.java                    # 跨域配置
│   ├── handler/
│   │   └── GlobalExceptionHandler.java        # 全局异常处理
│   └── resources/
│       ├── application.yml                    # 服务配置（端口、数据库、Redis 等）
│       └── schema.sql                         # 建表脚本（sso_user 表 + 默认管理员）
├── tengo-client/                              # 业务系统接入 Starter
│   ├── filter/
│   │   └── SsoTokenFilter.java                # 请求拦截器，验证 Token 有效性
│   ├── config/
│   │   └── ClientConfig.java                  # WebClient 配置，指向 SSO 服务端
│   ├── service/
│   │   └── ClientTokenManager.java            # 远程调用 SSO 服务端 /verify 接口
│   └── resources/
│       ├── application.yml                    # 客户端配置（SSO 服务端地址）
│       └── META-INF/spring/
│           └── AutoConfiguration.imports      # Spring Boot 自动配置声明
└── tengo-core/                                # 公共核心模块
    ├── config/
    │   ├── KeyConf.java                       # Redis Key 前缀、Header 名等常量
    │   ├── UrlConf.java                       # 路由路径常量（/login /logout /verify）
    │   ├── ServerSsoProperties.java           # 服务端配置属性（Token 有效期等）
    │   ├── ClientSsoProperties.java           # 客户端配置属性（SSO 服务端地址）
    │   ├── RedisConfig.java                   # Redis 序列化配置
    │   └── PasswordEncoderConfig.java         # 密码加密配置
    ├── cache/
    │   ├── TokenCache.java                    # Token 缓存接口
    │   └── RedisTokenCache.java               # 基于 Redis 的 Token 缓存实现
    ├── rate/
    │   ├── RateLimiter.java                   # 限流接口
    │   └── RedisRateLimiter.java              # 基于 Redis 的滑动窗口限流（60s 内 30 次）
    ├── xi/
    │   ├── UserAuthenticationService.java     # 用户认证接口
    │   ├── TokenManager.java                  # Token 管理接口
    │   └── impl/
    │       └── DatabaseUserAuthenticationService.java  # 基于 MySQL 的认证实现
    ├── mapper/
    │   └── UserRepository.java                # 用户数据访问层
    ├── pojo/
    │   ├── User.java                          # 用户实体（对应 sso_user 表）
    │   ├── TengoSsoToken.java                 # Token 会话对象
    │   └── TengoAuthUser.java                 # 认证用户信息（返回给业务系统）
    ├── R.java                                 # 统一响应封装（code + message + data）
    └── exception/
        └── TengoSsoException.java             # SSO 自定义异常
```
