<!-- TOC -->

- [1. 工具](#1-工具)
    - [1.1. idea中查看一个Class类的Diagram：](#11-idea中查看一个class类的diagram)
    - [1.2. 查看类的Type Hierarchy](#12-查看类的type-hierarchy)
- [2. 容器的基本实现](#2-容器的基本实现)
    - [2.1. 核心类DefaultListableBeanFactory](#21-核心类defaultlistablebeanfactory)
        - [2.1.1. DefaultListableBeanFactory的Diagram](#211-defaultlistablebeanfactory的diagram)
        - [2.1.2. DefaultListableBeanFactory的Type Hierarchy](#212-defaultlistablebeanfactory的type-hierarchy)
    - [2.2. XmlBeanDefinitionReader](#22-xmlbeandefinitionreader)
    - [2.3. XmlBeanFactory](#23-xmlbeanfactory)

<!-- /TOC -->
# 1. 工具

## 1.1. idea中查看一个Class类的Diagram：

右击Class文件 | Diagram | show diagram popup

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200609153812.png)

## 1.2. 查看类的Type Hierarchy

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200609155624.png)

# 2. 容器的基本实现

## 2.1. 核心类DefaultListableBeanFactory

### 2.1.1. DefaultListableBeanFactory的Diagram

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200609154558.png)

### 2.1.2. DefaultListableBeanFactory的Type Hierarchy

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200609155825.png)

## 2.2. XmlBeanDefinitionReader

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200609171236.png)

1. 通过继 AbstractBeanDefinitionReader 中的方法，来使用 ResourLoader 将资源文件路径转换为对应的 Resource 文件

2. 通过 DocumentLoader Resource 文件进行转换，将 Resource 文件转换为 Document文件

3. 通过实现接口 BeanDefinitionDocumentReader的DefaultBeanDefinitionDocumentReader类对Document 进行解析，并使用 BeanDefinitionParserDe egate对Element 进行解析

## 2.3. XmlBeanFactory

通过XmlBeanFactory获取bean的时序图

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200609172559.png)

------> 2.5