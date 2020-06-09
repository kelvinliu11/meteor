<!-- TOC -->

- [1. 概念](#1-概念)
    - [1.1. JDBC](#11-jdbc)
        - [1.1.1. JDBC驱动程序共分四种类型：](#111-jdbc驱动程序共分四种类型)
            - [1.1.1.1. 类型1](#1111-类型1)
            - [1.1.1.2. 类型2（Java代码加载驱动，常用方式）](#1112-类型2java代码加载驱动常用方式)
            - [1.1.1.3. 类型3](#1113-类型3)
            - [1.1.1.4. 类型4](#1114-类型4)
- [2. 工具](#2-工具)
    - [2.1. IDEA安装Sequence Diagram插件](#21-idea安装sequence-diagram插件)
- [3. Mybatis源码](#3-mybatis源码)
    - [3.1. 通过SqlSessionFactoryBuild.build()方法，创建SqlSessionFactory对象](#31-通过sqlsessionfactorybuildbuild方法创建sqlsessionfactory对象)
    - [3.2. 解析配置文件的骨干](#32-解析配置文件的骨干)
        - [3.2.1. properties标签解析](#321-properties标签解析)
        - [3.2.2. settings标签解析](#322-settings标签解析)
        - [3.2.3. typeAliases标签解析](#323-typealiases标签解析)
        - [3.2.4. plugins标签解析](#324-plugins标签解析)
        - [3.2.5. environments标签解析](#325-environments标签解析)
        - [3.2.6. typeHandler标签解析](#326-typehandler标签解析)
    - [3.3. 映射文件解析](#33-映射文件解析)
        - [3.3.1. resultMap解析](#331-resultmap解析)
    - [3.4. SQL执行流程](#34-sql执行流程)
    - [3.5. 需要学习的技术点](#35-需要学习的技术点)

<!-- /TOC -->

# 1. 概念

## 1.1. JDBC

Java数据库连接，（Java Database Connectivity，简称JDBC）是[Java语言](https://baike.baidu.com/item/Java语言)中用来规范客户端程序如何来访问数据库的[应用程序接口](https://baike.baidu.com/item/应用程序接口/10418844)，提供了诸如查询和更新数据库中数据的方法。

### 1.1.1. JDBC驱动程序共分四种类型：

#### 1.1.1.1. 类型1

JDBC-ODBC桥

这种类型的驱动把所有JDBC的调用传递给ODBC，再让后者调用数据库本地驱动代码（也就是数据库厂商提供的数据库操作二进制代码库，例如[Oracle](https://baike.baidu.com/item/Oracle)中的oci.dll）。

#### 1.1.1.2. 类型2（Java代码加载驱动，常用方式）

本地API驱动

这种类型的驱动通过客户端加载数据库厂商提供的本地代码库（C/[C++](https://baike.baidu.com/item/C%2B%2B)等）来访问数据库，而在[驱动程序](https://baike.baidu.com/item/驱动程序)中则包含了Java代码。

JDBC API主要位于JDK中的[java.sql](https://baike.baidu.com/item/java.sql)包中（之后扩展的内容位于javax.sql包中）

#### 1.1.1.3. 类型3

[网络协议](https://baike.baidu.com/item/网络协议)驱动

这种类型的驱动给客户端提供了一个网络API，客户端上的JDBC[驱动程序](https://baike.baidu.com/item/驱动程序)使用[套接字](https://baike.baidu.com/item/套接字)（Socket）来调用服务器上的[中间件](https://baike.baidu.com/item/中间件)程序，后者在将其请求转化为所需的具体API调用。

#### 1.1.1.4. 类型4

本地协议驱动

这种类型的驱动使用Socket，直接在客户端和数据库间通信。

```java
Class.forName(driverClass)
//加载MySql驱动
Class.forName("com.mysql.jdbc.Driver")
//加载Oracle驱动
Class.forName("oracle.jdbc.driver.OracleDriver")
DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/imooc", "root", "root");
conn.createStatement();
conn.prepareStatement(sql);
```

# 2. 工具

## 2.1. IDEA安装Sequence Diagram插件

安装Sequence Diagram插件->右键选中要查看的方法-> Sequence Diagram 选项

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200523160918.png)



# 3. Mybatis源码

## 3.1. 通过SqlSessionFactoryBuild.build()方法，创建SqlSessionFactory对象

关键的方法流程通过Sequence Diagram插件可以看到。

主要是通过XMLConfigBuilder来解析Mybatis的配置文件内容（Properties、Settings等xml元素）从而生成Configration对象，使用Configration对象来构建DefaultSqlSessionFactory对象。

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200523161244.png)

想要build，那么需要知道用什么来build，也就是要把配置文件解析出来，生成Configuration对象，流程见下一节。

## 3.2. 解析配置文件的骨干

图中的parse是核心，里面包含了全部的解析步骤，如下：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200523162106.png)

### 3.2.1. properties标签解析

核心方法是XMLConfigBuilder#propertiesElement

背景是mybatis-config.xml

```xml
<configuration>
    <properties resource="jdbc.properties"/>

    <typeAliases>
        <typeAlias alias="Article" type="xyz.coolblog.chapter1.model.Article"/>
    </typeAliases>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    
    <mappers>
        <mapper resource="chapter1/mapper/ArticleMapper.xml"/>
    </mappers>
</configuration>
```

其中<properties resource="jdbc.properties"/>的内容是

```xml
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/myblog?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&useSSL=false&serverTimezone=UTC
jdbc.username=root
jdbc.password=123456
```

OK，进入主题，看看怎么解析mybatis-config.xml的configuration标签下的properties子标签内容

```java
private void propertiesElement(XNode context) throws Exception {
        if (context != null) {
            // mybatis-config.xml中configuration标签下的properties子标签没有children
            Properties defaults = context.getChildrenAsProperties();
            // 有resource属性
            String resource = context.getStringAttribute("resource");
            // 没有url属性
            String url = context.getStringAttribute("url");
            if (resource != null && url != null) {
                throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.");
            }

            if (resource != null) {
                // 这里其实就是读取jdbc.properties的内容，存储到Properties defaults中
                defaults.putAll(Resources.getResourceAsProperties(resource));
            } else if (url != null) {
                defaults.putAll(Resources.getUrlAsProperties(url));
            }

            Properties vars = this.configuration.getVariables();
            if (vars != null) {
                defaults.putAll(vars);
            }

            this.parser.setVariables(defaults);
            // 其实是向Configuration的属性（protected Properties variables）设置值
            this.configuration.setVariables(defaults);
        }

    }
```

上面就是<properties>节点解析过程，不是很复杂。主要包含三个步骤：

一是解析<properties>节点的子节点，并将解析结果设置到 Properties 对象中。

二是从文件系统或通过网络读取属性配置，这取决于<properties>节点的 resource 和 url 是否为空。第二步对应的代码比较简单，这里就不分析了。

最后一步则是将包含属性信息的 Properties 对象设置到XPathParser 和 Configuration 中。

### 3.2.2. settings标签解析

主要是把settings中的配置放入到configuration对象中

### 3.2.3. typeAliases标签解析

2中typeAliases配置方式

```xml
<typeAliases>
	<package name="xyz.coolblog.chapter2.model1"/>
	<package name="xyz.coolblog.chapter2.model2"/>
</typeAliases>
```

```xml
<typeAliases>
	<typeAlias alias="article" type="xyz.coolblog.chapter2.model.Article" />
	<typeAlias alias="author" type="xyz.coolblog.chapter2.model.Author" />
</typeAliases>
```

重要代码：**查找某个包下的父类为 superType 的类**

```java
public void registerAliases(String packageName) {
    // 调用重载方法注册别名
    registerAliases(packageName, Object.class);
}
public void registerAliases(String packageName, Class<?> superType) {
    ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
    // 查找某个包下的父类为 superType 的类。从调用栈来看，这里的
    // superType = Object.class，所以 ResolverUtil 将查找所有的类。
    // 查找完成后，查找结果将会被缓存到内部集合中。
    resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
    // 获取查找结果
    Set<Class<? extends Class<?>>> typeSet = resolverUtil.getClasses();
    for (Class<?> type : typeSet) {
        // 忽略匿名类，接口，内部类
        if (!type.isAnonymousClass() && !type.isInterface() &&
            !type.isMemberClass()) {
            // 为类型注册别名
            registerAlias(type);
        }
    } 
}
```

### 3.2.4. plugins标签解析

plugins主要是一些拦截器，比如分页拦截器，用来在SQL执行过程中增加一些自定义操作。

```xml
<plugins>
    <plugin interceptor="xyz.coolblog.mybatis.ExamplePlugin">
    	<property name="key" value="value"/>
    </plugin>
</plugins>
```

### 3.2.5. environments标签解析

environment标签主要配置事务和数据源信息。

```xml
<environments default="development">
    <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <property name="driver" value="${jdbc.driver}"/>
            <property name="url" value="${jdbc.url}"/>
            <property name="username" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </dataSource>
    </environment>
</environments>
```

```java
private void environmentsElement(XNode context) throws Exception {
        if (context != null) {
            if (environment == null) {
                // 获取 default 属性
                environment = context.getStringAttribute("default");
            }
            for (XNode child : context.getChildren()) {
                // 获取 id 属性
                String id = child.getStringAttribute("id");
                // 检测当前 environment 节点的 id 与其父节点 environments 的
                // 属性 default 内容是否一致，一致则返回 true，否则返回 false
                if (isSpecifiedEnvironment(id)) {
                    // 解析 transactionManager 节点
                    TransactionFactory txFactory = transactionManagerElement(
                            child.evalNode("transactionManager"));
                    // 解析 dataSource 节点，逻辑和插件的解析逻辑很相似，不在赘述
                    DataSourceFactory dsFactory =
                            dataSourceElement(child.evalNode("dataSource"));
                    // 创建 DataSource 对象
                    DataSource dataSource = dsFactory.getDataSource();
                    Environment.Builder environmentBuilder =
                            new Environment.Builder(id)
                                    .transactionFactory(txFactory)
                                    .dataSource(dataSource);
                    // 构建 Environment 对象，并设置到 configuration 中
                    configuration.setEnvironment(environmentBuilder.build());
                }
            }
        }
    }
```

### 3.2.6. typeHandler标签解析

此标签是用于DB类型和Java类型的转换。例如varchar和String；但是有些要定制，比如tinyint和java中的enum。

```xml
<!-- 自动扫描 -->
<typeHandlers>
	<package name="xyz.coolblog.handlers"/>
</typeHandlers>
<!-- 手动配置 -->
<typeHandlers>
    <typeHandler jdbcType="TINYINT"
    javaType="xyz.coolblog.constant.ArticleTypeEnum"
    handler="xyz.coolblog.mybatis.ArticleTypeHandler"/>
</typeHandlers>
```

## 3.3. 映射文件解析

### 3.3.1. resultMap解析

```xml
<resultMap id="articleResult" type="Article">
    <id property="id" column="id"/>
    <result property="title" column="article_title"/>
    <association property="article_author" column="article_author_id" resultMap="authorResult"/>
</resultMap>
<resultMap id="authorResult" type="Author">
    <id property="id" column="author_id"/>
    <result property="name" column="author_name"/>
</resultMap>
```



## 3.4. SQL执行流程

```java
ArticleMapper articleMapper = session.getMapper(ArticleMapper.class);
Article article = articleMapper.findOne(1);	
```

* 为Mapper接口创建代理对象

```java

// -☆- DefaultSqlSession
public <T> T getMapper(Class<T> type) {
	return configuration.<T>getMapper(type, this);
}
// -☆- Configuration
public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
	return mapperRegistry.getMapper(type, sqlSession);
}
// -☆- MapperRegistry
public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    // 从 knownMappers 中获取与 type 对应的 MapperProxyFactory
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
    	throw new BindingException("……");
     }
     try {
        // 创建代理对象
        return mapperProxyFactory.newInstance(sqlSession);
     } catch (Exception e) {
    	throw new BindingException("……");
     } 
 }
// -☆- MapperProxyFactory
public T newInstance(SqlSession sqlSession) {
    // 创建 MapperProxy 对象，MapperProxy 实现了 InvocationHandler 接口，
    // 代理逻辑封装在此类中
    final MapperProxy<T> mapperProxy =
    new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
    return newInstance(mapperProxy);
}
protected T newInstance(MapperProxy<T> mapperProxy) {
    // 通过 JDK 动态代理创建代理对象
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), 
    new Class[]{mapperInterface}, mapperProxy);
}
```

* 执行代理逻辑

```java
public Object invoke(Object proxy, 
Method method, Object[] args) throws Throwable {
    try {
        // 如果方法是定义在 Object 类中的，则直接调用
        if (Object.class.equals(method.getDeclaringClass())) {
        	return method.invoke(this, args);
        /*
        * 下面的代码最早出现在 mybatis-3.4.2 版本中，用于支持 JDK 1.8 中的
        * 新特性 - 默认方法。这段代码的逻辑就不分析了，有兴趣的同学可以
        * 去 Github 上看一下相关的相关的讨论（issue #709），链接如下：
        * 
        * https://github.com/mybatis/mybatis-3/issues/709
        */
         } else if (isDefaultMethod(method)) {
            return invokeDefaultMethod(proxy, method, args);
         }
     } catch (Throwable t) {
    	throw ExceptionUtil.unwrapThrowable(t);
     }
    // 从缓存中获取 MapperMethod 对象，若缓存未命中，则创建 MapperMethod 对象
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    // 调用 execute 方法执行 SQL
    return mapperMethod.execute(sqlSession, args);
}
```





## 3.5. 需要学习的技术点

* 解析xml配置

* 解析properties配置，PropertyTokenizer

* 反射，ReflectorFactory， Reflector获取类的信息

* MetaClass，类的元信息

* 获取在某个x包下，所有y类型的子类

  

---> 4.2 查询语句执行过程