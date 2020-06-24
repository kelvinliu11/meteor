<!-- TOC -->

- [1. 需要探讨的内容](#1-需要探讨的内容)
- [2. 常用命令（可略过）](#2-常用命令可略过)
    - [2.1. Linux开防火墙端口](#21-linux开防火墙端口)
    - [2.2. docker服务没有启动](#22-docker服务没有启动)
- [3. 使用IDEA在vm docker中快速部署eureka-server](#3-使用idea在vm-docker中快速部署eureka-server)
    - [3.1. 虚拟机，2个](#31-虚拟机2个)
    - [3.2. docker环境，配置远程访问](#32-docker环境配置远程访问)
    - [3.3. idea，安装docker插件](#33-idea安装docker插件)
    - [3.4. idea，编写eureka-server代码](#34-idea编写eureka-server代码)
    - [3.5. idea，改写pom复制jar包到指定目录](#35-idea改写pom复制jar包到指定目录)
    - [3.6. idea，配置docker](#36-idea配置docker)
    - [3.7. idea，运行，在目标机器构建容器](#37-idea运行在目标机器构建容器)
- [4. 在两个vm docker上部署sc集群](#4-在两个vm-docker上部署sc集群)
    - [4.1. docker网络知识](#41-docker网络知识)
    - [4.2. 避坑命令 - 开docker端口](#42-避坑命令---开docker端口)
    - [4.3. 创建swarm集群](#43-创建swarm集群)
    - [4.4. 创建overlay网络](#44-创建overlay网络)
    - [4.5. 部署sc服务](#45-部署sc服务)
        - [4.5.1. 部署eureka-server](#451-部署eureka-server)
        - [4.5.2. 部署service-provider](#452-部署service-provider)
        - [4.5.3. 确认vm2 docker中的service-provider是否可以注册到vm1 docker中的eureka-server](#453-确认vm2-docker中的service-provider是否可以注册到vm1-docker中的eureka-server)
        - [4.5.4. 考虑在vm1上部署一个service-consumer](#454-考虑在vm1上部署一个service-consumer)
    - [4.6. 部署sc服务结论，swarm与overlay](#46-部署sc服务结论swarm与overlay)

<!-- /TOC -->
# 1. 需要探讨的内容

* 如何在vm中部署一个springboot jar包应用
* 在vm1中部署注册中心eureka，在vm2中部署service-provider
* 分在2个vm中的服务是否可以互相访问？
* 在vm1中部署service-consumer，来访问vm2中的service-provider

# 2. 常用命令（可略过）

## 2.1. Linux开防火墙端口

firewall-cmd --permanent --zone=public --add-port=8888/tcp 
systemctl restart firewalld.service

过程中不要关闭防火墙，不然有些场景验证不出来，待生产配置的时候会各种报错。

## 2.2. docker服务没有启动

```shell
[root@localhost ~]# docker ps
Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?
[root@localhost ~]# systemctl restart docker
```

# 3. 使用IDEA在vm docker中快速部署eureka-server

> 考虑的场景是在做原型，不是生产部署，所以暂时没有集成工具如Jenkins。

传统的思路，在idea里编写eureka server，打jar包，上传到vm，制作镜像、容器、启动容器。

缺点：修改代码、打包、上传、制作、启动比较繁琐，不适合原型测试。

考虑：使用idea，一键将代码打包、制作镜像、上传并启动。

参考：为了解决这个问题，参考了https://www.jianshu.com/p/0dcc2e43963b 下文也会一步步来搭建环境

## 3.1. 虚拟机，2个

我这里安装的2个vm，ip地址分别是172.18.100.129和172.18.100.66，centos7

## 3.2. docker环境，配置远程访问

开启docker服务，开启tcp端口，操作如下：

```shell
sudo vim /lib/systemd/system/docker.service
```

找到如下配置

```shell
ExecStart=/usr/bin/dockerd
```

修改为

```shell
ExecStart=/usr/bin/dockerd -H unix:///var/run/docker.sock -H tcp://0.0.0.0:2375
```

重启docker网络

```shell
sudo systemctl daemon-reload 
sudo systemctl restart docker
```

测试

```shell
[root@localhost ~]# curl http://localhost:2375/verion
{"message":"page not found"}
```

能够看到这个也是OK的，有的是可以看到具体的版本信息

**记得开2375端口防火墙，2个vm都要开**

## 3.3. idea，安装docker插件

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623154609.png)

具体的docker配置在后面，不着急配

## 3.4. idea，编写eureka-server代码

在创建eureka-server之前，有必要了解一下你要做demo的springcloud与springboot版本的关系

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200619143953.png)

这里测试，我们用的SpringCloud是Hoxton.SR5，Springboot是2.3.1.RELEASE

有一个系列文章写的还行，比较简洁，当比较熟练的就直接照抄：https://my.oschina.net/mdxlcj/blog/2995192

至此，相当于是普通的eureka-server工程就创建好了，下面是跟idea的docker插件结合，来实现一键从idea部署到vm的过程

## 3.5. idea，改写pom复制jar包到指定目录

在项目的`pom.xml`文件中加入以下代码：

```xml
<!--复制jar包到指定目录-->
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <executions>
        <execution>
            <id>gen-webadmin</id>
            <phase>package</phase>
            <configuration>
                <tasks>
                    <copy todir="docker" file="target/${project.artifactId}-${project.version}.${project.packaging}" />
                </tasks>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

在项目根目录下建立一个`docker`文件夹，在`docker`文件夹下新建一个`Dockerfile`文件，写入一下内容：

```bash
FROM java:8u111
VOLUME /tmp
ADD *.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# Ubuntu 时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```

上面两步的作用，就是在项目根目录的docker文件夹下，创建了一个Dockerfile，并且maven打包的时候会把对应的jar包复制到这个目录下。

## 3.6. idea，配置docker

我们打开`settings`可以看到`docker`

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623160031.png)

点击`+`，添加一个连接。上图已经添加好了，如果有2台vm可以添加两个，比如我的是Docker66和Docker129，分别对应2个vm带上的docker服务。

然后配置`docker`启动项
 选择编辑：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623160240.png)



添加一个启动项：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623160312.png)

填写`docker`相关的参数：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623160609.png)



## 3.7. idea，运行，在目标机器构建容器

直接运行就是了，可以看到开始构建了（上一步配置docker的时候，server选到“Docker129”，代表目标容器要在129上）

![image-20200623160807403](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200623160807403.png)

去129上看一下，docker ps

```shell
[root@localhost ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                CREATED             STATUS              PORTS                    NAMES
8f032e353cd8        d16121b844eb        "java -jar /app.jar"   2 hours ago         Up 2 hours          0.0.0.0:8761->8761/tcp   eureka-server
```

至此，就可以在本地idea改代码，直接点击debug，就可以重新生成镜像并启动容器~



# 4. 在两个vm docker上部署sc集群

目标：在vm1中部署注册中心eureka，在vm2中部署service-provider

## 4.1. docker网络知识

由于我们是想在vm1里面部署eureka-server，在vm2里面部署service-provider，那么问题来了，service-provider是否可以注册到eureka-server？通过什么ip注册？更进一步，当我们再启动service-consumer之后，service-consumer用什么调用service-provider？

* 这里就是需要我们把两个vm连接起来，这种连接的方式，简单的说就是构建一个swarm集群，集群内，可以建立overlay网络。

* swarm网络需要选择一个vm做manager，比如我选择的是vm1，在上面执行创建swarm集群的命令，并得到加入swarm网络的命令
* 将上一步得到的“加入swarm网络的命令”，在vm2上执行，那么vm2就加到swarm网络了，这样vm1和vm2组成了一个集群
* overlay网络是在swarm集群的基础上创建出来的虚拟网络，可以有自己的网段、网关等。
* 我们在swarm manager节点（vm1）执行创建overlay网络的命令，那么vm2上通过docker network ls是可以看到在vm1上创建的overlay网络的。

> 创建swarm集群，参考：https://blog.csdn.net/wangxw1803/article/details/90782463  下面也会一步步创建swarm和overlay

## 4.2. 避坑命令 - 开docker端口

为了下面的操作不踩坑，建议先在2个vm上执行下面的开启防火墙端口的命令

```shell
firewall-cmd --zone=public --add-port=7946/tcp --permanent
firewall-cmd --zone=public --add-port=7946/udp --permanent
firewall-cmd --zone=public --add-port=4789/udp --permanent
firewall-cmd --zone=public --add-port=4789/tcp --permanent
firewall-cmd --zone=public --add-port=2375/tcp --permanent
firewall-cmd --zone=public --add-port=2375/udp --permanent
firewall-cmd --zone=public --add-port=2377/udp --permanent
firewall-cmd --zone=public --add-port=2377/tcp --permanent
firewall-cmd --reload
```

## 4.3. 创建swarm集群

> 创建swarm集群，参考：https://www.cnblogs.com/xiangsikai/p/9938374.html

我们选择vm1位manager节点，在manager节点上执行

docker swarm init --advertise-addr 172.18.100.129

可以得到一串加入此swarm集群的命令。

在worker节点上执行上述操作获得命令。

docker swarm join --token SWMTKN-1-3emp34nuyoo2imoeh0z4n3ukgpa62l31qlmf9d2r27lol24rr9-1me1d6hjif1f3695l2ioi46ir 172.18.100.129:2377

在manager节点上查看swarm节点情况

```shell
[root@localhost ~]# docker node ls
ID                            HOSTNAME                STATUS              AVAILABILITY        MANAGER STATUS      ENGINE VERSION
dbqfnbvywasa6gl4s1nr1q921     localhost.localdomain   Ready               Active                                  19.03.5
mg5zdsuqeuo9bvh4z2q2i6tpg *   localhost.localdomain   Ready               Active              Leader              19.03.1
```

至此，swarm集群就创建好了，非常简单。

## 4.4. 创建overlay网络

大白话，overlay网络要解决的问题就是，分布在两个vm里的docker容器，怎么样可以相互访问。

举个栗子：

在vm1里docker容器ip是10.0.1.5，在vm2里docker容器ip是10.0.1.6。但是vm1的ip是172.18.100.129，vm2是172.18.100.66，如果我们从10.0.1.5这个容器里直接ping 10.0.1.6，是不通的，怎么样才可以访问？那就是建立overlay网络。

> 创建overlay网络，参考：https://www.cnblogs.com/xiangsikai/p/9938374.html

具体创建overlay网络操作命令如下：

* 在swarm manager节点，执行创建命令

  ```shell
  docker network create -d overlay --attachable my-network2
  ```

* 在vm2上，执行docker network ls，可以看到my-network2网络

  ```shell
  [root@localhost ~]# docker network ls
  NETWORK ID          NAME                DRIVER              SCOPE
  1f722f207cac        bridge              bridge              local
  3aadcb52b3df        docker_gwbridge     bridge              local
  3744ee928016        host                host                local
  587fwg44wbnk        ingress             overlay             swarm
  sqi2konhybf9        my-network2         overlay             swarm
  a184e66697b0        none                null                local
  ```

至此，overlay网络也构建好了。可以通过idea来构建服务看看eureka-server和service-provider的连通性。见下面内容。

## 4.5. 部署sc服务

有一个系列文章写的还行，比较简洁，当比较熟练的就直接照抄：https://my.oschina.net/mdxlcj/blog/2995192

### 4.5.1. 部署eureka-server

要验证eureka-server和service-provider的连通性，其实就是在idea里面的docker运行配置里，加上--network=my-network2这个运行配置加上即可，如下：

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623163825.png)

确认eureka-server使用了my-network2这个overlay网络，可以执行命令

```
docker network inspect my-network2
```

可以看到eureka-server是加入了overlay网络，并且其ip地址是10.0.1.13，如下图。那么后面一步要做的，就是把service-provider部署在vm2，也加入my-network2这个overlay网络，其注册中心地址就是10.0.1.13，如果可以注册上，那代表两个容器是互通了。

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623164143.png)

### 4.5.2. 部署service-provider

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200623164611.png)

注意：eureka-client的pom中要加web模块，否则启动之后会直接退出unregistered

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 4.5.3. 确认vm2 docker中的service-provider是否可以注册到vm1 docker中的eureka-server

![image-20200623165619967](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200623165619967.png)

### 4.5.4. 考虑在vm1上部署一个service-consumer

在vm1上部署一个service-consumer，注册中eureka-server，并调用service-provider的接口

* 创建一个springboot的service-consumer工程

  具体service-consumer代码上的改造，参考：https://my.oschina.net/mdxlcj/blog/3139142

  ![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200624093819.png)

  同样构建好容器，并加入my-network2网络，通过 docker network inspect my-network2查看此网络上的容器及ip

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200624102432.png)

![image-20200624102454451](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20200624102454451.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200624103903.png)

此时从10.0.1.12 ping 10.0.1.10不通，怀疑是昨天重启vm后，119机器的ip变到了223，所以重新建swarm和overlay再试一下。（剧透一下ping不同的确跟vm ip漂移有关，重建swarm和overlay网络之后就可以了）

这里有个小插曲：在建立swarm集群后，新建的overlay网络可能没那么快在vm2上显示出来，就是说在vm2上执行docker network ls看不到my-network2网络，为了确定情况，可以执行下面的命令来尝试网络是否OK

```
docker service create --replicas 1 --name mytest --network my-network2 --constraint 'node.hostname == vm2' alpine ping baidu.com
```

* 把service-provider的注册中心改到docker ip，看是否可以注册上

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200624105217.png)

结果可以注册上，并且调用http://172.18.100.223:8076/test1也是可以看到页面输出Hello world！

此时，service-provider和service-consumer注册的eureka地址都是其docker容器的内部ip，为10.0.0.5，从service-consumer调用service-provider是OK的。

* 再尝试一下把service-provider和service-consumer使用eureka的虚拟机ip来注册，看是否可以，即把eureka-server的地址改到。这么做的意义是生产上可以在应用里只指定eureka的地址，其他的服务，都是通过docker ip相互调用，不需要指定确定ip

```
eureka.client.serviceUrl.defaultZone=http://172.18.100.223:8761/eureka/
```

注册没有问题的，相互调用也是OK的

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200624105954.png)

![](https://gitee.com/kelvin11/cloudimg/raw/master/img/20200624110026.png)

## 4.6. 部署sc服务结论，swarm与overlay

* 各vm情况：

vm1:  171.18.100.223。其上docker：eureka-server(10.0.0.5),eureka-consumer(10.0.0.10)

vm2：172.18.100.66。其上docker：service-provider(10.0.0.9)

* sc配置：

eureka-server不需要配置ip

service-provider的注册中心，可以是vm1的171.18.100.223，也可以是docker的10.0.0.5

service-consumer的注册中心，可以是vm1的171.18.100.223，也可以是docker的10.0.0.5

访问service-consumer，http://172.18.100.223:8076/test1，都可以拿到结果

* 创建swarm集群命令

  docker swarm init --advertise-addr 172.18.100.223

  172.18.100.223是vm的ip，要注意修改，然后获取类似下面的命令，在swarm work节点上去执行

  docker swarm join --token SWMTKN-1-51p9uylvwzfzwt7qqu9e1mlcfyt60py8icg1tvoqtm5pz6f0nh-e1lkiunldyrt7o0eany0sa4yx 172.18.100.223:2377

* 创建overlay网络

  docker network create -d overlay --attachable my-network2

  --attachable比较重要，不然容器可能加入不了网络

  在manager节点创建overlay网络后，可能没有那么快同步到work节点上，就是说在work节点上执行docker network ls看不到my-network2，可以通过执行下面的命令，再次确认网络是否建好了

  ```shell
  docker service create --replicas 1 --name mytest --network my-network2 --constraint 'node.hostname == vm2' alpine ping baidu.com
  ```

* 避坑命令，最好在vm上都执行一遍

  ```shell
  firewall-cmd --zone=public --add-port=7946/tcp --permanent
  firewall-cmd --zone=public --add-port=7946/udp --permanent
  firewall-cmd --zone=public --add-port=4789/udp --permanent
  firewall-cmd --zone=public --add-port=4789/tcp --permanent
  firewall-cmd --zone=public --add-port=2375/tcp --permanent
  firewall-cmd --zone=public --add-port=2375/udp --permanent
  firewall-cmd --zone=public --add-port=2377/udp --permanent
  firewall-cmd --zone=public --add-port=2377/tcp --permanent
  firewall-cmd --reload
  ```

  