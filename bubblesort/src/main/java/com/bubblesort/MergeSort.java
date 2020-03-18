package com.bubblesort;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author: liukun
 * @Description:
 * @Date: 2020/3/12.10:49
 */
public class MergeSort {

    /**
     * [left ~ middle]和[middle+1, right]进行merge，并保持left ~ right之间由小到大排序
     * 新建一个长度为right-left+1长度的数组newArray
     * 设2个指针pLeft和pRight，初始pLeft=left，pRight=middle+1。每次找到pLeft和pRight中较小的一个，放入到newArray中，放入newArray用下标targetIndex
     * 待pLeft=middle&&pRight=right，表明merge结束，此时需要将newArray中的数据拷贝到origin中的left ~ right下标段处。
     * @param origion 原始数组
     * @param left 待排序的左下标
     * @param middle 中间切割的下标
     * @param right 最右边的下标
     */
    public static void merge(int[] origion, int left, int middle, int right) {
        int pLeft = left, pRight = middle + 1, targetIndex = 0;

        int[] newArray = new int[right-left+1];
        while (pLeft <= middle || pRight <= right) {

            if (origion[pLeft] <= origion[pRight]) {
                // 左侧指向数据比右侧指向的数据小，那么把左侧数据放入newArray，并且左侧指向+1。但是如果pLeft==middle，那么就不再增加pLeft
                newArray[targetIndex] = origion[pLeft];
                targetIndex ++;
                if (pLeft < middle) {
                    pLeft ++;
                } else {
                    // 因为左侧已经全部结束，所以直接把右侧的数组拷贝到newArray即可
                    for (;pRight <= right;pRight ++) {
                        newArray[targetIndex] = origion[pRight];
                        targetIndex ++;
                    }
                    break;
                }
            } else {
                // 右侧指向数据比左侧指向的数据小，那么把右侧数据放入newArray，并且右侧指向+1。但是如果pRight==right，那么就不再增加pRight
                newArray[targetIndex] = origion[pRight];
                targetIndex ++;
                if (pRight < right) {
                    pRight ++;
                } else {
                    // 因为右侧已经全部结束，所以直接把左侧的数组拷贝到newArray即可
                    for (;pLeft <= middle;pLeft ++) {
                        newArray[targetIndex] = origion[pLeft];
                        targetIndex ++;
                    }
                    break;
                }
            }

        }

        // 此时需要将newArray中的数据拷贝到origin中的left ~ right下标段处。
        for (int tmp : newArray) {
            origion[left++] = tmp;
        }
    }

    public static void mergeSort(int[] origion, int left, int middle, int right){
        if (right - left < 2) {
            return;
        }
        System.out.println("左侧排序,left=" + left + "--middle=" + ((left+middle)/2) + "--right=" + middle);
        mergeSort(origion, left, ((left+middle)/2), middle);
        System.out.println("右侧排序,left=" + (middle + 1) + "--middle=" + ((middle+1+right)/2) + "--right=" + right);
        mergeSort(origion, middle + 1, ((middle+1+right)/2), right);
        merge(origion, left, middle, right);
    }

    public static void main(String[] args) {
//        int[] origion = {1,2,3,4,5,6,7,8};
        int[] origion = {8,7,6,5};
        mergeSort(origion, 0, (origion.length - 1)/2, origion.length - 1);
        System.out.println(Arrays.toString(origion));
    }
}
