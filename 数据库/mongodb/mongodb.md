<!-- TOC -->

- [1. 为什么mongo？](#1-为什么mongo)
- [2. 工具 - 工欲善其事必先利其器](#2-工具---工欲善其事必先利其器)
    - [2.1. nosqlbooster](#21-nosqlbooster)
        - [2.1.1. 破解](#211-破解)
        - [2.1.2. 连接mongo](#212-连接mongo)
        - [2.1.3. shell命令](#213-shell命令)
    - [2.2. 创建测试数据TestData](#22-创建测试数据testdata)
- [3. mongo语法](#3-mongo语法)
    - [3.1. 创建db](#31-创建db)
    - [3.2. 创建collection集合](#32-创建collection集合)
    - [3.3. 创建document文档](#33-创建document文档)
    - [3.4. 查询db](#34-查询db)
    - [3.5. 查询collection集合](#35-查询collection集合)
    - [3.6. 查询document文档](#36-查询document文档)
    - [3.7. 通过工具生成查询语句](#37-通过工具生成查询语句)
    - [3.8. 聚合aggregate](#38-聚合aggregate)
    - [3.9. 像sql一样自由](#39-像sql一样自由)
    - [3.10. 创建db用户和密码](#310-创建db用户和密码)
- [4. 参考手册](#4-参考手册)
- [5. Springboot项目访问mongo数据](#5-springboot项目访问mongo数据)
- [6. 自动生成查询mongo数据的代码](#6-自动生成查询mongo数据的代码)
- [7. mysql -> mongo (kettle)制作宽表](#7-mysql---mongo-kettle制作宽表)
- [8. mongo -> mysql (kettle)导出数据](#8-mongo---mysql-kettle导出数据)
- [9. 待完成](#9-待完成)
    - [9.1. 列族模型](#91-列族模型)
    - [9.2. 数据字段](#92-数据字段)

<!-- /TOC -->
# 1. 为什么mongo？

主要的场景是在做BI报表的时候，由于现在主流的微服务架构，经常会需要关联多个库表的数据，进而经过清洗、转换后做成DM数据集市，供驾驶舱报表使用。为了简化sql而提出使用mongo作为中间工具，在做完清洗进而生成DM数据集市的时候，用mongo来做数据集市。

但是，我们不一定在DM中只有一张宽表，是可以根据业务形态做多张宽表的。应该根据实际情况随机应变。

# 2. 工具 - 工欲善其事必先利其器

## 2.1. nosqlbooster

* nosqlbooster是一个mongo的客户端，用来执行mongo的CURD操作，简单的CRUD可以参考菜鸟教程：[mongo菜鸟教程CRUD](https://www.runoob.com/mongodb/mongodb-tutorial.html)

* 这个工具还有比较多的黑科技，后续一一列出，在这里，我们需要做的，就是连接好mongo之后，通过shell来执行mongo命令。做个剧透，此工具有可视化、Query工具化、Query语句转Java代码、命令参考手册、创建测试数据、可视化编辑数据等功能。

### 2.1.1. 破解

使用的5.x版本，暂时还没有去做破解的工作

### 2.1.2. 连接mongo

根据实际情况来，我们这个case里没有设置mongo的账号密码，所以只要填写Server和Port即可

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715161321.png)

### 2.1.3. shell命令

打开后，就可以在里面写mongo命令了

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715161423.png)

## 2.2. 创建测试数据TestData

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715155218.png)

双击testCollection，可以打开这个集合，如下图，可以切换到table视图，更加直观

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715161654.png)

# 3. mongo语法

相信到这里，应该对mongo有了简单的认知，再加深一下印象：

| mysql    | mongo          |
| -------- | -------------- |
| db数据库 | db数据库       |
| table表  | collection集合 |
| row行    | document文档   |

## 3.1. 创建db

```shell
use mytest
```

通过use关键字来切换或创建db，不过需要注意的是，use完可能不能立刻看见，需要继续执行db.createCollection("runoob")来创建集合，才能看到db

## 3.2. 创建collection集合

```shell
db.createCollection("runoob")
```

## 3.3. 创建document文档

```shell
db.runoob.insert({"name" : "菜鸟教程"})
db.runoob.insert([{"name":"hello world1", "user":"liukun", "age":"18","add_time":"2020-07-01 12:11:13"}])
db.runoob.insert([{"name":"hello world2", "user":"liukun", "salary":10000, "add_time":"2020-07-02 12:11:13"}])
db.runoob.insert([{"name":"hello world3", "user":"liukun", "salary":30000, "add_time":"2020-07-03 12:11:13"}])
db.runoob.insert([{"name":"hello world4", "user":"kelvin", "age":"20","salary":20000, "add_time":"2020-07-04 12:11:13"}])
db.runoob.insert([{"name":"hello world5", "user":"jixueying", "add_time":"2020-07-05 12:11:13"}])
```

## 3.4. 查询db

```shell
show dbs
```

## 3.5. 查询collection集合

查看此db下的所有collection集合

```shell
show collections
```

## 3.6. 查询document文档

```shell
//查看runoob这个collection里面的所有数据
db.runoob.find()
// 查询runoob集合里 user=liukun的数据
db.runoob.find({
	"user" : 'liukun'
});
// 时间查询
db.getCollection('runoob').find({"add_time":{$gte:"2020-07-15"}})
// 下面的语句是使用Query工具生成的，注意的是，比较符选择greater后，数据类型选any，时间值加双引号
db.runoob.find({
    add_time: {
        $gt: "2020-07-18"
    }
})
// 简单的查询可以通过Query工具来生成，省去自己编写的过程。并且，可以通过Code工具，自动生成Java代码。参考下一小节说明
db.runoob.find({
	"age":{ "$gt" : 10 }
});
```

## 3.7. 通过工具生成查询语句

首先是打开一个shell窗口，在空行的地方，点解Query按钮，填写查询条件，就会生成箭头所指的查询语句。

另外，选中查询语句，点击Query按钮，也会打开Query窗口并且填充好条件和值。

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715163236.png)

## 3.8. 聚合aggregate

```shell
// 聚合。根据user字段分组，求和salary这个字段的值，$sum是固定语法，还有其他的语法，如$max等。num_tutorial是执行后的列名
db.runoob.aggregate([{$group : {_id : "$user", num_tutorial : {$sum : "$salary"}}}])
// 按照user和age分组，计算其salary值
db.runoob.aggregate([{$group : {_id : {user:"$user",age:"$age"}, num_tutorial : {$sum : "$salary"}}}])
// 聚合。取salary字段，存在此字段的，按照user字段分组（group by），对其salary进行sum
db.runoob.aggregate([
    { $match : { "salary":{$exists:true} } },
  {
     $group: {
        _id: "$user",
        salarySum: { $sum: "$salary" }
     }
  }
])
```

第一个语句的执行结果如下

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715163639.png)

## 3.9. 像sql一样自由

可能你感觉到了，如果要写简单的查询语句，那么Query工具就可以实现了查询，但是如果复杂，呵呵~所以nosqlbooster提供了类似sql的功能，赶紧先看一下

```shell
// 基于sql模式访问数据，例如聚合sum
mb.runSQLQuery(`
  SELECT name, SUM(salary) AS total FROM runoob GROUP BY user, name
`);
// 基于sql模式的like示例。Double quotes quote object names (e.g. "field"). Single quotes are for strings 'string'
mb.runSQLQuery(`
       SELECT * FROM runoob WHERE "user" LIKE '%liu%'
`).sort({_id:-1})
  .limit(100);
  
// 基于sql模式的count示例。Double quotes quote object names (e.g. "field"). Single quotes are for strings 'string'
mb.runSQLQuery(`
        SELECT COUNT(*) as total FROM runoob where "user" = 'liukun'
`);
mb.runSQLQuery(`
        SELECT sum(salary) as total FROM runoob where "user" = 'liukun'
`);
```

发现了吧，里面基本上都是sql，并且是可以执行的！那么这个黑科技的在这里：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715163831.png)

操作后，会生成如下的语句，那么只要会写sql，就会写查询了（不过要注意还是有些不一样的，它的英文注释写了，自己看）

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715163908.png)

然后我们可能会想，那java api是不是也可以用sql来写？对不起，目前还没有找到解决的办法~

## 3.10. 创建db用户和密码

创建好的db，可以为其创建对应的账号密码。为什么会突然要创建账号密码呢？是因为用kettle来连接mongo的时候，如果不填写账号密码，会报错，所以这里回过头来创建了一下。更多的相关功能，主要是在运维侧，不深入讨论。

```shell
// 创建liukun这个db的用户名密码，分别是liukun/123456，这个账号的角色是dbOwner，有这个db的一切权限
db.createUser({user: "liukun",pwd: "123456",roles: [ { role: "dbOwner", db: "liukun" } ]})
// 查看有哪些用户
show users
```

# 4. 参考手册

写的再多，其实最好还是根据手册来，又或者是在百度之前，多看看手册，提升对手册的熟悉程度，并且手册也基本能解决80%的问题，总比各种抄的百度来的快。

有两个入口可以速查，如下图：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200715165305.png)

# 5. Springboot项目访问mongo数据

# 6. 自动生成查询mongo数据的代码

# 7. mysql -> mongo (kettle)制作宽表

2张mysql表   -> 1张mongo的collection

# 8. mongo -> mysql (kettle)导出数据



# 9. 待完成

## 9.1. 列族模型

## 9.2. 数据字段

