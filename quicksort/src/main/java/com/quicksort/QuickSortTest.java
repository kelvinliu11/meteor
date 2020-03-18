package com.quicksort;

import java.util.Arrays;

/**
 * 快速排序的思想：
 * 快速排序是将分治法运用到排序问题中的一个典型例子，其基本思想是：
 * 通过一个枢轴（pivot）元素将 n 个元素的序列分为左、右两个子序列 Ll 和 Lr，其中子序列 Ll中的元素均比枢轴元素小，而子序列 Lr 中的元素均比枢轴元素大，
 * 然后对左、右子序列分别进行快速排序，在将左、右子序列排好序后，则整个序列有序，
 * 而对左右子序列的排序过程直到子序列中只包含一个元素时结束，此时左、右子序列由于只包含一个元素则自然有序。
 *
 * 对待排序序列进行划分：
 * 使用两个指针 low 和 high 分别指向待划分序列 r 的范围，取 low 所指元素为枢轴，即 pivot = r[low]。
 * 划分首先从 high 所指位置的元素起向前逐一搜索到第一个比 pivot 小的元素，并将其设置到 low 所指的位置；
 * 然后从 low 所指位置的元素起向后逐一搜索到第一个比 pivot 大的元素，并将其设置到 high 所指的位置；
 * 不断重复上述两步直到 low = high 为止，最后将 pivot 设置到 low 与 high 共同指向的位置。
 */
public class QuickSortTest {
    public static void main(String[] args){
        Integer[] arr = {5,2,7,3,9,10,8,6,1,4};
//        Integer[] arr = {1,3,2};
        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }
 
    //排序方法-假设从小到大排序
    public static void quickSort(Integer[] arr,int low,int high){
        if(low < high){
            int part=partition(arr,low,high);
            //递归调用
            quickSort(arr,low,part-1);
            quickSort(arr,part+1,high);
        }
    }
 
    //划分方法
    private static int partition(Integer[] arr,int low,int high){
        //使用 r[low]作为枢轴元素
        int pivot = arr[low];
        //从两端交替向内扫描
        while(low < high){
            while(low<high && arr[high] >= pivot)
            {
                high--;
            }
            //将比 pivot 小的元素移向低端
            arr[low] = arr[high];
            while(low<high && arr[low] <= pivot)
            {
                low++;
            }
            //将比 pivot 大的元素移向高端
            arr[high] = arr[low];
        }
        //设置枢轴
        arr[low]=pivot;
        //返回枢轴元素位置
        return low;
    }
 
}