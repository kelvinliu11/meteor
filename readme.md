<!-- TOC -->

- [1. 概念](#1-概念)
    - [1.1. 渐进时间复杂度（asymptotic time complexity）](#11-渐进时间复杂度asymptotic-time-complexity)
    - [1.2. 二叉树](#12-二叉树)
- [2. 算法](#2-算法)
    - [2.1. 冒泡、选择、插入、快排、小和、荷兰国旗问题、堆排序](#21-冒泡选择插入快排小和荷兰国旗问题堆排序)
- [3. 常用工具代码](#3-常用工具代码)
    - [3.1. 比较器](#31-比较器)
- [4. 工具使用](#4-工具使用)
    - [4.1. 从0开始Powerdesign类图](#41-从0开始powerdesign类图)
    - [4.2. hasor dataway的使用](#42-hasor-dataway的使用)
    - [4.3. Springboot+layui的管理系统](#43-springbootlayui的管理系统)
- [5. 项目管理](#5-项目管理)
    - [5.1. 项目管理Project](#51-项目管理project)
- [6. 源码类](#6-源码类)
    - [6.1. Mybatis源码学习](#61-mybatis源码学习)
    - [6.2. Spring源码学习](#62-spring源码学习)
- [7. 工具](#7-工具)
    - [7.1. typora+picgo+gitee写作，支持csdn](#71-typorapicgogitee写作支持csdn)

<!-- /TOC -->


# 1. 概念

## 1.1. 渐进时间复杂度（asymptotic time complexity）
官方的定义如下：  
若存在函数 f（n），使得当n趋近于无穷大时，T（n）/ f（n）的极限值为不等于零的常数，则称 f（n）是T（n）的同数量级函数。
记作 T（n）= O（f（n）），称O（f（n）） 为算法的渐进时间复杂度，简称时间复杂度。
渐进时间复杂度用大写O来表示，所以也被称为大O表示法。  

* T（n） = 3n   
最高阶项为3n，省去系数3，转化的时间复杂度为：  
T（n） =  O（n）
* T（n） = 5logn   
最高阶项为5logn，省去系数5，转化的时间复杂度为：  
T（n） =  O（logn）
* T（n） = 2  
只有常数量级，转化的时间复杂度为：  
T（n） =  O（1）
* T（n） = 0.5n^2 + 0.5n  
最高阶项为0.5n^2，省去系数0.5，转化的时间复杂度为：    
T（n） =  O（n^2）  
这四种时间复杂度究竟谁用时更长，谁节省时间呢？稍微思考一下就可以得出结论：  
O（1）< O（logn）< O（n）< O（n^2）  
在编程的世界中有着各种各样的算法，除了上述的四个场景，还有许多不同形式的时间复杂度，比如：  
O（nlogn）, O（n^3）, O（m*n），O（2^n），O（n！）  
## 1.2. 二叉树
* 满二叉树  
一棵二叉树的结点要么是叶子结点，要么它有两个子结点（如果一个二叉树的层数为K，且结点总数是(2^k) -1，则它就是满二叉树。）   
![](.readme_images/满二叉树.png)
* 完全二叉树  
若设二叉树的深度为k，除第 k 层外，其它各层 (1～k-1) 的结点数都达到最大个数，第k 层所有的结点都连续集中在最左边，这就是完全二叉树。  
![](.readme_images/完全二叉树.png)


# 2. 算法
## 2.1. 冒泡、选择、插入、快排、小和、荷兰国旗问题、堆排序
含排序算法的稳定性
https://github.com/kelvinliu11/meteor/blob/master/算法.md

# 3. 常用工具代码
## 3.1. 比较器
https://github.com/kelvinliu11/meteor/blob/master/代码块/compare
* 一般可以分为内部比较器和外部比较器。
    * 内部比较器comparable接口。为什么叫内部比较器是因为待比较的类要实现这个接口，对于代码有一定的侵入性。
    * 外部比较器comparator接口。为什么叫外部比较器是因为待比较的类无需实现此接口，对于代码无侵入性。
    * 内置比较器通过Collections.sort(List list)为列表排序；外置比较器通过Collections.sort(List list, Comparator c)为列表排序

# 4. 工具使用
## 4.1. 从0开始Powerdesign类图
https://github.com/kelvinliu11/meteor/blob/master/从0开始Powerdesign类图
## 4.2. hasor dataway的使用
一个网上的小工具，sb写的，可以通过web可视化页面写DataQL语句（类似js）来动态生成接口
https://github.com/kelvinliu11/meteor/blob/master/工具/dataway
## 4.3. Springboot+layui的管理系统SpringbootPlus
基于此，来做一些简单的管理系统  
https://gitee.com/kelvin11/springboot-plus
E:\gitlab\kelvin\springboot-layui-management
已完成的一个case：E:\tmp\办公\设计\xiandafu-springboot-plus-master\springboot-plus(内部)

# 5. 项目管理
## 5.1. 项目管理Project
https://github.com/kelvinliu11/meteor/blob/master/项目管理/使用Project进行项目管理.md

# 6. 源码类
## 6.1. Mybatis源码学习
https://github.com/kelvinliu11/meteor/blob/master/数据库/Mybatis源码学习
## 6.2. Spring源码学习
https://github.com/kelvinliu11/meteor/blob/master/Spring/Spring源码/Spring源码分析.md
E:\公司材料\0023-学习\辅助代码\SpringSourceTest(内部)


# 7. 工具
## 7.1. typora+picgo+gitee写作，支持csdn
https://github.com/kelvinliu11/meteor/blob/master/工具/typora+picgo+gitee