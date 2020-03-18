
# 一、概念
## （一）渐进时间复杂度（asymptotic time complexity）
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

# 二、算法
## 冒泡排序
https://github.com/kelvinliu11/meteor/blob/master/bubblesort/readme.md
## 选择排序
https://github.com/kelvinliu11/meteor/blob/master/bubblesort/readme.md
## 插入排序
https://github.com/kelvinliu11/meteor/blob/master/bubblesort/readme.md
## 小和问题
https://github.com/kelvinliu11/meteor/blob/master/littlesum/readme.md
## 快速排序
https://github.com/kelvinliu11/meteor/blob/master/quicksort/readme.md