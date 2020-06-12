<!-- TOC -->

- [1. 目标](#1-目标)
- [2. 组织架构、用户、角色、权限梳理](#2-组织架构用户角色权限梳理)
    - [2.1. 角色数据授权](#21-角色数据授权)
        - [2.1.1. 角色数据授权（页面配置）](#211-角色数据授权页面配置)
            - [2.1.1.1. 只查看自己 --- 有异常](#2111-只查看自己-----有异常)
            - [2.1.1.2. 查看同公司（不含子公司）](#2112-查看同公司不含子公司)
            - [2.1.1.3. 同结构](#2113-同结构)
            - [2.1.1.4. 部门和子部门下所有](#2114-部门和子部门下所有)
            - [2.1.1.5. 所有机构](#2115-所有机构)
            - [2.1.1.6. 集团下所有 --- 有异常](#2116-集团下所有-----有异常)
            - [2.1.1.7. 母公司](#2117-母公司)
            - [2.1.1.8. 集团部门](#2118-集团部门)
        - [2.1.2. 角色数据授权（怎么更新Update）](#212-角色数据授权怎么更新update)
        - [2.1.3. 角色数据授权，是怎么生效的？](#213-角色数据授权是怎么生效的)
- [3. beetl相关技术](#3-beetl相关技术)

<!-- /TOC -->
# 1. 目标

是为了使用一个“工具”来完成CURD的绝大部分工作，提供一个可以快速完成功能的小系统。

以下是一个开源框架，Springboot+layui，已经包含组织、用户、菜单、权限、自动生成代码等功能。（fork的）
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

## 2.1. 角色数据授权

背景是数据权限分以下多个可选：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200610175936.png)

对角色“华邦管理员”，其数据权限“用户列表”，设定为“查看同公司（不含子公司）”



### 2.1.1. 角色数据授权（页面配置）

#### 2.1.1.1. 只查看自己 --- 有异常

这个case有报错，回头再分析出错原因

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164717.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165424.png)

#### 2.1.1.2. 查看同公司（不含子公司）

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164717.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164902.png)





#### 2.1.1.3. 同结构

只能看到跟自己同一层的用户

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164717.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611164741.png)

此时，如果在“江苏华邦网络科技”这个分公司下新增一个“华邦用户2”，那么可见如下：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611171827.png)

#### 2.1.1.4. 部门和子部门下所有

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165647.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165728.png)

#### 2.1.1.5. 所有机构

![image-20200611165808572](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200611165808572.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165818.png)

#### 2.1.1.6. 集团下所有 --- 有异常

![image-20200611165901373](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200611165901373.png)

#### 2.1.1.7. 母公司

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611165947.png)

#### 2.1.1.8. 集团部门

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200611170021.png)

### 2.1.2. 角色数据授权（怎么更新Update）

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

### 2.1.3. 角色数据授权，是怎么生效的？

就是比如在“用户管理”菜单页面中，有查询“用户列表”的功能（function_id为2，code为user.query），探索是如何生效的。

在DataAccessFunction中

# 3. beetl相关技术

