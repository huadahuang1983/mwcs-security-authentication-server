# MWC Security Authentication Server

## 简介

MWC Security Authentication Server是一个基于Spring Boot、Spring Security的登录认证服务组件，支持生成JWT格式Token，为其他应用提供安全的登录认证、登出和Token管理功能。

## 核心功能

- **登录认证**：支持用户名/密码登录，返回JWT格式的访问令牌
- **Token刷新**：支持使用刷新令牌获取新的访问令牌
- **用户登出**：支持用户登出，实现Token黑名单机制
- **RSA密钥管理**：提供RSA密钥对的生成、存储和加载功能
- **灵活扩展**：支持自定义用户加载逻辑和认证流程

## 技术栈

- Spring Boot 3.5.x
- Spring Security 6.5.x
- Nimbus JOSE JWT 10.x
- Java 17+

## clone项目

```bash
git clone https://github.com/huadahuang1983/mwc-security-authentication-server.git
cd mwc-security-authentication-server
mvn clean install
```

## Maven 引入

```xml
<dependency>
    <groupId>com.reebake.mwc.security</groupId>
    <artifactId>mwc-security-authentication-server</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 快速开始

### 1. 配置RSA密钥

在应用启动时，确保RSA密钥已生成并正确加载，系统默认会生成RSA密钥对并存储到`.mwc/`目录下。密钥一旦生成请勿轻易变动导致登录认证失败。可以使用内置的`RsaUtil`工具类生成密钥。

### 2. 实现UserDetailsService

必须实现`UserDetailsService`接口来定义用户加载逻辑：

```java

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        // 实现用户加载逻辑，例如从数据库查询用户
    }
}
```

### 3. 配置JWT属性（可选默认）

在`application.properties`中配置JWT相关属性：

```properties
# JWT配置
mwc.jwt.issuer=您的签发者名称   # JWT签发者
mwc.jwt.expiration-time-in-minutes=30    # 访问令牌有效期（分钟）
```

### 4. 启动应用

直接启动Spring Boot应用即可，认证服务会自动初始化并提供以下API：

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/refresh` - 刷新访问令牌
- `POST /api/auth/logout` - 用户登出

## 自定义扩展

如果请求体中包含`username`和`password`字段，系统会自动使用`UsernameLoginAuthenticationProvider`进行认证。如果需要自定义认证逻辑，可实现`UsernameLoginAuthenticationProvider`，`LoginAuthenticationConverter`类。

### 自定义LoginAuthenticationConverter

如果登录请求格式与默认格式不同，可以自定义`LoginAuthenticationConverter`：

```java

public class CustomLoginAuthenticationConverter extends LoginAuthenticationConverter {
    
    @Override
    public Authentication convert(jakarta.servlet.http.HttpServletRequest request) {
        // 自定义登录请求解析逻辑
        // 返回UsernameLoginAuthenticationToken或自定义Authentication对象
    }
}
```

### 自定义UsernameLoginAuthenticationProvider

如果需要自定义认证逻辑，可以实现`UsernameLoginAuthenticationProvider`：

```java

public class CustomUsernameLoginAuthenticationProvider extends UsernameLoginAuthenticationProvider {
    
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        // 自定义认证检查逻辑
    }
}
```

### 自定义LoginAuthenticationSuccessHandler

如果需要自定义登录成功后的响应格式，可以实现`LoginAuthenticationSuccessHandler`：

```java

public class CustomLoginAuthenticationSuccessHandler implements LoginAuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) {
        // 自定义登录成功响应逻辑
    }
}
```

## 认证流程

1. **用户登录**：发送POST请求到`/api/auth/login`，包含用户名和密码
2. **认证处理**：系统验证用户名和密码，生成访问令牌和刷新令牌
3. **返回响应**：返回包含访问令牌、刷新令牌和用户信息的JSON响应
5. **Token刷新**：当访问令牌过期时，使用刷新令牌请求新的访问令牌
6. **用户登出**：发送POST请求到`/api/auth/logout`，使当前令牌失效

## 数据传输对象(DTOs)

- **LoginRequest**：登录请求，包含用户名和密码
- **AuthResponse**：认证响应，包含访问令牌、刷新令牌和用户信息

## 异常处理

组件提供了统一的异常处理机制，返回标准化的错误响应：

```json
{
  "status": 401,
  "message": "认证失败",
  "error": "Unauthorized",
  "timestamp": "2023-09-15T10:30:00.000+00:00"
}
```

## 安全特性

- 使用RS256算法进行JWT签名，确保令牌的完整性和不可伪造性
- 支持Token黑名单机制，实现安全的登出功能
- 密码使用BCrypt加密存储，确保用户密码安全
- 提供细粒度的权限控制支持

## 依赖说明

- **spring-boot-starter-web**：提供Web应用支持
- **spring-boot-starter-security**：提供Spring Security支持
- **nimbus-jose-jwt**：提供JWT生成和验证功能
- **hutool-all**：提供工具类支持
- **lombok**：通过注解简化Java代码

## 版本历史

- 1.0.0-SNAPSHOT：初始版本，提供基本的JWT认证功能

## 贡献

欢迎提交Issue和Pull Request！

## 许可证

MIT License
