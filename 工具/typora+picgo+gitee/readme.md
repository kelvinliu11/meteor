<!-- TOC -->

- [1. 要用什么工具写？为什么？](#1-要用什么工具写为什么)
- [2. 推荐的整体方案](#2-推荐的整体方案)
- [3. 达成的效果](#3-达成的效果)
    - [3.1. 微信截图](#31-微信截图)
    - [3.2. 通过picgo客户端的快捷键ctrl+shift+p上传图片到gitee](#32-通过picgo客户端的快捷键ctrlshiftp上传图片到gitee)
    - [3.3. 复制picgo中图片的路径，粘贴到typora中即可](#33-复制picgo中图片的路径粘贴到typora中即可)
    - [3.4. 可以直接粘贴markdown文本到csnd](#34-可以直接粘贴markdown文本到csnd)

<!-- /TOC -->
# 1. 要用什么工具写？为什么？

* 首先，写文章建议用markdown来写，这样的话，更多的精力可以放在内容上而非排版布局上。

* 然后，用什么工具写markdown呢？程序员的idea有插件、vscode也有插件、有道云笔记、typora都可以。
* markdown工具一般都可以通过toc显示目录。但是有个问题，无法给目录自动编号。
* 写出来的文章，有时候想发到csdn上，然后发现图片需要从本地一张张上传上去，简直无法忍受。

以上种种，促使我们需要用一个方案来专心写文章。

# 2. 推荐的整体方案

如果是typora来纯粹写本地文件是没有问题的，效果也还不错，但是如果想要上传到csdn、github上，就会有个大麻烦 --- 图片无法显示。因为图片都是本地的。

网上比较多的，是typora+picgo+github，即把图片通过typora的picgo插件上传到github上。

然后使用过程中发现，这种方式存在弊端：1是github不是特别稳定（你懂的），2是picgo内置的有时候配置总是不生效（后面附问题解决办法）

那么怎么解决这些弊端呢？方案如下：

* picgo使用外置app方式，exe百度下载即可
* 使用gitee替换github。github经过验证总是会有上传失败的问题，考虑用gitee，速度提升，怎么替换见后文
* picgo安装gitee插件，在picgo插件里搜索gitee-uploader，下载并进行配置如下图（网上有比较多的教程，可以自行百度，或按照这个来一步步操作https://blog.csdn.net/Goinhn/article/details/104837863）

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200518140432.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200518140556.png)

* 配置完成后，可以在typora的 文件 | 偏好设置 | 图片，此处进行上传验证

  ![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200518142151.png)

# 3. 达成的效果

## 3.1. 微信截图

一般都会通过微信快捷键截图或者其他工具截图，截图存放在剪贴板中，无需保存到特定目录。

## 3.2. 通过picgo客户端的快捷键ctrl+shift+p上传图片到gitee

ctrl+shift+p快捷键，是快速把剪贴板中的图片上传到gitee中的

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200520175204.png)ctrl+shift+p

## 3.3. 复制picgo中图片的路径，粘贴到typora中即可

复制picgo客户端中刚才上传完成的图片路径

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200520175346.png)

## 3.4. 可以直接粘贴markdown文本到csnd

因为现在文章中的图片其实已经是gitee的路径了，csdn可以直接展示。github上也可以直接展示gitee图片路径。

