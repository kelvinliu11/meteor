# 概念

## JDBC

Java数据库连接，（Java Database Connectivity，简称JDBC）是[Java语言](https://baike.baidu.com/item/Java语言)中用来规范客户端程序如何来访问数据库的[应用程序接口](https://baike.baidu.com/item/应用程序接口/10418844)，提供了诸如查询和更新数据库中数据的方法。

### JDBC驱动程序共分四种类型：

#### 类型1

JDBC-ODBC桥

这种类型的驱动把所有JDBC的调用传递给ODBC，再让后者调用数据库本地驱动代码（也就是数据库厂商提供的数据库操作二进制代码库，例如[Oracle](https://baike.baidu.com/item/Oracle)中的oci.dll）。

#### 类型2（Java代码加载驱动，常用方式）

本地API驱动

这种类型的驱动通过客户端加载数据库厂商提供的本地代码库（C/[C++](https://baike.baidu.com/item/C%2B%2B)等）来访问数据库，而在[驱动程序](https://baike.baidu.com/item/驱动程序)中则包含了Java代码。

JDBC API主要位于JDK中的[java.sql](https://baike.baidu.com/item/java.sql)包中（之后扩展的内容位于javax.sql包中）

#### 类型3

[网络协议](https://baike.baidu.com/item/网络协议)驱动

这种类型的驱动给客户端提供了一个网络API，客户端上的JDBC[驱动程序](https://baike.baidu.com/item/驱动程序)使用[套接字](https://baike.baidu.com/item/套接字)（Socket）来调用服务器上的[中间件](https://baike.baidu.com/item/中间件)程序，后者在将其请求转化为所需的具体API调用。

#### 类型4

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

# 工具

## IDEA安装Sequence Diagram插件

安装Sequence Diagram插件->右键选中要查看的方法-> Sequence Diagram 选项

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200523160918.png)



# Mybatis源码

## 通过SqlSessionFactoryBuild.build()方法，创建SqlSessionFactory对象

关键的方法流程通过Sequence Diagram插件可以看到。

主要是通过XMLConfigBuilder来解析Mybatis的配置文件内容（Properties、Settings等xml元素）从而生成Configration对象，使用Configration对象来构建DefaultSqlSessionFactory对象。

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200523161244.png)

想要build，那么需要知道用什么来build，也就是要把配置文件解析出来，生成Configuration对象，流程见下一节。

## 解析配置文件的骨干

图中的parse是核心，里面包含了全部的解析步骤，如下：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200523162106.png)

### 解析Properties标签：XMLConfigBuilder#propertiesElement

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
            this.configuration.setVariables(defaults);
        }

    }
```

