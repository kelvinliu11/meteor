* 题设
    * 假定就是一堆0,1,2的数字散列在数组中，要求排序后，按照0,1,2的顺序排列。
* 思路
    * 对于给定的数组arr[]，设定3个指针，begin， current，end。其中begin指向起始位置，current也指向起始位置，end指向末尾。current从头开始遍历整个数组。
    * current不断前移，当current为0的时候，将current和begin所指的元素交换。交换后，begin++， current++
    * current继续前移，当current为1的时候，current++
    * current继续前移，当current为2的时候，将current和end所指的元素交换。交换后，current不动，end--
* 参考文档
    * https://www.cnblogs.com/liuzhen1995/p/6439429.html