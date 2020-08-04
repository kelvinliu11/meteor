<!-- TOC -->

- [1. 目标](#1-目标)
- [2. 组织架构、用户、角色、权限梳理](#2-组织架构用户角色权限梳理)
    - [2.1. 用户与角色](#21-用户与角色)
    - [2.2. 功能点、角色、菜单、角色功能授权、角色数据授权](#22-功能点角色菜单角色功能授权角色数据授权)
    - [2.3. 角色数据授权](#23-角色数据授权)
        - [2.3.1. 角色数据授权（页面配置）](#231-角色数据授权页面配置)
            - [2.3.1.1. 只查看自己 --- 有异常](#2311-只查看自己-----有异常)
            - [2.3.1.2. 查看同公司（不含子公司）](#2312-查看同公司不含子公司)
            - [2.3.1.3. 同结构](#2313-同结构)
            - [2.3.1.4. 部门和子部门下所有](#2314-部门和子部门下所有)
            - [2.3.1.5. 所有机构](#2315-所有机构)
            - [2.3.1.6. 集团下所有 --- 有异常](#2316-集团下所有-----有异常)
            - [2.3.1.7. 母公司](#2317-母公司)
            - [2.3.1.8. 集团部门](#2318-集团部门)
        - [2.3.2. 角色数据授权（怎么更新Update）](#232-角色数据授权怎么更新update)
        - [2.3.3. 角色数据授权，是怎么生效的？](#233-角色数据授权是怎么生效的)
- [3. beetl相关技术-SQL](#3-beetl相关技术-sql)
    - [3.1. 尝试写一个beetlsql分页查询功能](#31-尝试写一个beetlsql分页查询功能)
    - [3.2. 新表：生成PO、创建Dao、执行查询流程](#32-新表生成po创建dao执行查询流程)
        - [3.2.1. 生成PO](#321-生成po)
        - [3.2.2. 创建Dao](#322-创建dao)
        - [3.2.3. 执行查询](#323-执行查询)
    - [3.3. 自定义sql查询](#33-自定义sql查询)
        - [3.3.1. 查询单表](#331-查询单表)
        - [3.3.2. 查询返回特定POJO](#332-查询返回特定pojo)
- [4. 生成子系统](#4-生成子系统)
    - [4.1. 生成新模块](#41-生成新模块)
    - [4.2. 导入模块module](#42-导入模块module)
    - [4.3. 在子系统中做一个查询接口](#43-在子系统中做一个查询接口)
- [5. 生成页面+service+dao+PO](#5-生成页面servicedaopo)
    - [5.1. 自动生成代码](#51-自动生成代码)
    - [5.2. 新建“机器管理”功能点](#52-新建机器管理功能点)
    - [5.3. 新建“机器管理”菜单](#53-新建机器管理菜单)
    - [5.4. 访问“机器管理”页面](#54-访问机器管理页面)

<!-- /TOC -->
# 1. 目标

是为了使用一个“工具”来完成CURD的绝大部分工作，提供一个可以快速完成功能的小系统。

以下是一个开源框架，Springboot+layui，已经包含组织、用户、菜单、权限、自动生成代码等功能。（fork的，也有部分功能的测试）
https://gitee.com/kelvin11/springboot-plus
E:\gitlab\kelvin\springboot-layui-management
已完成的一个case：E:\tmp\办公\设计\xiandafu-springboot-plus-master\springboot-plus(内部MQTT)

# 2. 组织架构、用户、角色、权限梳理



一些查询的cache，给注释掉了

什么是角色数据授权？

数据权限怎么实现的？对应的数字怎么理解？

* 功能点与菜单权限
* 功能点与按钮权限

* 配置了“含”数据权限的菜单，里面的管理功能比如修改等按钮展示不出来



选择“只查看自己”之后，会执行下面的sql

```
┏━━━━━ Debug [coreRoleFunction._gen_selectByTemplate_page] ━━━
┣ SQL：	 select * from `core_role_function` where 1=1 and `ROLE_ID`=? and `FUNCTION_ID`=? limit ? , ?
┣ 参数：	 [174, 185, 0, 1]
┣ 位置：	 com.ibeetl.admin.console.service.FunctionConsoleService.updateFunctionAccessByRole(FunctionConsoleService.java:140)
┣ 时间：	 1ms
┣ 结果：	 [1]
┗━━━━━ Debug [coreRoleFunction._gen_selectByTemplate_page] ━━━

┏━━━━━ Debug [coreRoleFunction._gen_updateById] ━━━
┣ SQL：	 update `core_role_function` set `ROLE_ID`=?,`FUNCTION_ID`=?,`DATA_ACCESS_TYPE`=?,`DATA_ACCESS_POLICY`=? where `ID` = ?
┣ 参数：	 [174, 185, 1, null, 208]
┣ 位置：	 com.ibeetl.admin.console.service.FunctionConsoleService.updateFunctionAccessByRole(FunctionConsoleService.java:143)
┣ 时间：	 2ms
┣ 更新：	 [1]
┗━━━━━ Debug [coreRoleFunction._gen_updateById] ━━━
```

## 2.1. 用户与角色

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200615140442.png)

然后再进行新增的操作

## 2.2. 功能点、角色、菜单、角色功能授权、角色数据授权

功能点是菜单和角色权限的基础，想做菜单、菜单权限、数据权限，需要：

1. 先建立对应的功能点。如果是想建菜单，那么需要先建一个功能点（ps：“功能地址”是菜单地址，“功能类型”是普通功能）
2. 菜单项新增。“菜单地址”选择对应的功能点；“菜单类型”如果是“导航”，那么代表的是此项下有下一级菜单，如果是“菜单”代表是实际的菜单级别页面。
3. 在“角色功能授权”，授权角色可以访问的功能点，那么功能点对应的菜单也就可见了
4. 在“角色数据授权”，如果功能点的“功能类型”是“含数据权限”，那么在给角色授权完功能点之后，可以在“角色数据授权”进一步设置功能点的数据权限，详细功能参考下一小节内容。

## 2.3. 角色数据授权

背景是数据权限分以下多个可选：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200610175936.png)

对角色“华邦管理员”，其数据权限“用户列表”，设定为“查看同公司（不含子公司）”



### 2.3.1. 角色数据授权（页面配置）

#### 2.3.1.1. 只查看自己 --- 有异常

这个case有报错，回头再分析出错原因

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164717.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165424.png)

#### 2.3.1.2. 查看同公司（不含子公司）

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164717.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164902.png)





#### 2.3.1.3. 同结构

只能看到跟自己同一层的用户

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164717.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164741.png)

此时，如果在“江苏华邦网络科技”这个分公司下新增一个“华邦用户2”，那么可见如下：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611171827.png)

#### 2.3.1.4. 部门和子部门下所有

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165647.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165728.png)

#### 2.3.1.5. 所有机构

![image-20200611165808572](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200611165808572.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165818.png)

#### 2.3.1.6. 集团下所有 --- 有异常

![image-20200611165901373](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200611165901373.png)

#### 2.3.1.7. 母公司

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165947.png)

#### 2.3.1.8. 集团部门

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611170021.png)

### 2.3.2. 角色数据授权（怎么更新Update）

修改数据权限如下图，看执行了什么sql语句

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200610175936.png)

```
┏━━━━━ Debug [coreRoleFunction._gen_selectByTemplate_page] ━━━
┣ SQL：	 select * from `core_role_function` where 1=1 and `ROLE_ID`=? and `FUNCTION_ID`=? limit ? , ?
┣ 参数：	 [174, 2, 0, 1]
┣ 位置：	 com.ibeetl.admin.console.service.FunctionConsoleService.updateFunctionAccessByRole(FunctionConsoleService.java:140)
┣ 时间：	 1ms
┣ 结果：	 [1]
┗━━━━━ Debug [coreRoleFunction._gen_selectByTemplate_page] ━━━

┏━━━━━ Debug [coreRoleFunction._gen_updateById] ━━━
┣ SQL：	 update `core_role_function` set `ROLE_ID`=?,`FUNCTION_ID`=?,`DATA_ACCESS_TYPE`=?,`DATA_ACCESS_POLICY`=? where `ID` = ?
┣ 参数：	 [174, 2, 2, null, 212]
┣ 位置：	 com.ibeetl.admin.console.service.FunctionConsoleService.updateFunctionAccessByRole(FunctionConsoleService.java:143)
┣ 时间：	 10ms
┣ 更新：	 [1]
┗━━━━━ Debug [coreRoleFunction._gen_updateById] ━━━

```

就是将core_role_function表中这个id，设置其DATA_ACCESS_TYPE。DATA_ACCESS_TYPE的枚举见上图

### 2.3.3. 角色数据授权，是怎么生效的？

就是比如在“用户管理”菜单页面中，有查询“用户列表”的功能（function_id为2，code为user.query），探索是如何生效的。

在DataAccessFunction中

# 3. beetl相关技术-SQL

http://ibeetl.com/guide/#/beetlsql/quickstart

======> 考虑beetlsql可能并不是特别常用的ORM框架，替换此框架可能需要比较多的时间（需要改Dao类，Dao类继承了BaseMapper....）

======> 考虑引入Mybatis-plus，对于既有的dao代码不做改造，新功能通过新的mybatis方案实现

**综合来看：可能需要熟悉beetlsql来快速实现功能，另外也需要尝试接入mybatis**

## 3.1. 尝试写一个beetlsql分页查询功能

查询user_console表数据，需要实现以下功能：
1. 根据构建的CoreUser对象，动态的设置查询条件（为null的不作为查询条件，不为null的才会查询）
2. 分页查询，每页3条，查询第1页
3. 模糊查询，CoreUser中name包含“1”的用户查出来
4. 根据用户姓名升序排序
解决思路：通过LambdaQuery来进行查询（或者是考虑通过BaseMapper来实现----这种方式作者不推荐了）

```java
    @Test
    public void testOne() throws Exception {
        CoreUser queryTemplateDto = new CoreUser();
        queryTemplateDto.setJobType0("JT_01");//JT_01 JT_02;如果不传就没有这个查询条件
        LambdaQuery<CoreUser> query = userConsoleDao.createLambdaQuery()
                .andLike(CoreUser::getName,"%1%")   // 实现功能点3
                .andEq(CoreUser::getJobType0, Query.filterEmpty(queryTemplateDto.getJobType0()))   // 实现功能点1
                .asc(CoreUser::getName);   // 实现功能点4
        query.page(1, 3);   // 实现功能点2
//        List<CoreUser> userList = query.page(2, 2).getList();
//        for (CoreUser user: userList) {
//            System.out.println(user);
//        }
    }
```

## 3.2. 新表：生成PO、创建Dao、执行查询流程

### 3.2.1. 生成PO

```java
    @Test
    public void testGenerateCode() throws Exception {
        // 或者直接生成java文件
        GenConfig config = new GenConfig();
        config.preferBigDecimal(true);
        config.setPreferDate(true);
        sqlManager.genPojoCodeToConsole("test_product");// 快速生成，显示到控制台
    }
```

### 3.2.2. 创建Dao

直接继承BaseMapper即可，注意设置泛型类

### 3.2.3. 执行查询

```java
@Test
    public void testNewPO() {
        LambdaQuery<TestProduct> query = testProductDao.createLambdaQuery()
                .desc(TestProduct::getDelFlag);
        List<TestProduct> productList = query.select();
        for (TestProduct tmp : productList) {
            System.out.println(tmp.getProductName());
        }

    }
```

## 3.3. 自定义sql查询

### 3.3.1. 查询单表

```java
List<TestMachine> machineList = testMachineDao.execute("SELECT * FROM test_machine WHERE machine_name LIKE ?", "%Dell%");
```

### 3.3.2. 查询返回特定POJO

* 新建dto类，用来接收查询结果

  ```java
  public class MyMachine{
      private String machineName;
  
      public String getMachineName() {
          return machineName;
      }
  
      public void setMachineName(String machineName) {
          this.machineName = machineName;
      }
  
      @Override
      public String toString() {
          return "MyMachine{" +
                  "machineName='" + machineName + '\'' +
                  '}';
      }
  }
  ```

* 在src/main/resource目录下，建testMachine.md文件，内容是

  ```markdown
  getTestMachineA
  ===
  
  	select * from test_machine
  	
  ```

* 在testMachineDao.java新增getMachineA()方法

  ```java
  @SqlResource("springbootplus.testMachine")
  public interface TestMachineDao extends BaseMapper<TestMachine>{
      public PageQuery<TestMachine> queryByCondition(PageQuery query);
      public void batchDelTestMachineByIds( List<Long> ids);
  
      public List<MyMachine> getTestMachineA();
  }
  ```

* 运行测试方法

  ```java
  @RunWith(SpringRunner.class)
  @SpringBootTest(classes = {MainApplication.class})// 指定启动类
  public class ApplicationTests {
  
      @Autowired
      TestMachineDao machineDao;
  
      /**
       * 生成PO类的代码
       * @throws Exception
       */
      @Test
      public void testSql() throws Exception {
          List<MyMachine> getTestMachineA = machineDao.getTestMachineA();
          System.out.println("111");
      }
  
  }
  ```

* 目录结构与运行情况截图如下

  ![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618144146.png)

# 4. 生成子系统

原则上作者不建议在admin-console或admin-core修改代码。推荐是先自动生成一个项目（其实是module），此module是依赖admin-console和admin-core的，然后导入此module

## 4.1. 生成新模块

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200617172114.png)

## 4.2. 导入模块module

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200617172204.png)

从新的module启动即可

但是，发现项目目录有点奇怪

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200617172449.png)

所以考虑重新生成一下，然后新module目录选择到当前admin-core的同级目录（目的是git管理比较简单一点，项目代码都在一起）

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200617172725.png)

然后一样，导入module即可，测试启动新项目MainApplication，可访问管理系统

## 4.3. 在子系统中做一个查询接口

无需额外的配置，可以直接使用LambdaQuery

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200617175433.png)

# 5. 生成页面+service+dao+PO

## 5.1. 自动生成代码

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618090248.png)

生成的路径选择在“E:\gitlab\kelvin\springboot-layui-management\springbootplus”，这个就是我新建的项目目录，这样的话，新建出来的文件会自动放在此目录下，需要注意的是自动生成的代码package，分别是dao、entity、service、web这些目录

按此方式，从html&js到controller、service、dao、entity都已经生成好了

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618090919.png)

## 5.2. 新建“机器管理”功能点

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618091050.png)

## 5.3. 新建“机器管理”菜单

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618091225.png)

## 5.4. 访问“机器管理”页面

![image-20200618091302500](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200618091302500.png)

看后台报错

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618091443.png)

发现是没有重启服务.....重启后即可访问....

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200618093952.png)

> 有个小问题就是有些字段不需要在页面上展示，比如“增加”的时候，不需要填写“添加时间”和“更新时间”
>
> 目前这个还没有看到解决的方法，估计是需要手动修改，毕竟如果不配置展示，就需要在db中改为可空或插入一些默认值，这个可能需要自行实现。