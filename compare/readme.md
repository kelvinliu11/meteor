# 比较器
* 一般可以分为内部比较器和外部比较器。
    * 内部比较器comparable接口。为什么叫内部比较器是因为待比较的类要实现这个接口，对于代码有一定的侵入性。
    * 外部比较器comparator接口。为什么叫外部比较器是因为待比较的类无需实现此接口，对于代码无侵入性。
    * 内置比较器通过Collections.sort(List list)为列表排序；外置比较器通过Collections.sort(List list, Comparator c)为列表排序

