package com.example.hollandflagproblem;

import java.util.Arrays;

/**
 * 荷兰国旗问题，描述参考readme
 * @Author: liukun
 * @Description:
 * @Date: 2020/4/7.14:21
 */
public class HollandFlagProblem {

    /**
     * * 题设
     *     * 假定就是一堆0,1,2的数字散列在数组中，要求排序后，按照0,1,2的顺序排列。
     * * 思路
     *     * 对于给定的数组arr[]，设定3个指针，begin， current，end。其中begin指向起始位置，current也指向起始位置，end指向末尾。current从头开始遍历整个数组。
     *     * current不断前移，当current为0的时候，将current和begin所指的元素交换。交换后，begin++, current++
     *     * current继续前移，当current为1的时候，current++
     *     * current继续前移，当current为2的时候，将current和end所指的元素交换。交换后，current不动，end--
     * @param origion
     * @return
     */
    public static int[] hollandFlagProblem(int[] origion) {
        int begin = 0, current = 0, end = origion.length - 1;

        for (;current < origion.length -1 && current < end;) {
            if (origion[current] == 0) {
                // 将current和begin所指的元素交换
                origion[begin] = origion[begin] + origion[current];
                origion[current] = origion[begin] - origion[current];
                origion[begin] = origion[begin] - origion[current];
                begin++;
                current++;
                continue;
            }
            if (origion[current] == 1) {
                current++;
                continue;
            }
            if (origion[current] == 2) {
                origion[end] = origion[end] + origion[current];
                origion[current] = origion[end] - origion[current];
                origion[end] = origion[end] - origion[current];
                end --;
                continue;
            }
        }
        return origion;
    }

    public static void main(String[] args) {
        int[] origion = new int[]{0,2,1,1,2,0,0,1,2,1,2,0,2};
        HollandFlagProblem.hollandFlagProblem(origion);
        System.out.println(Arrays.toString(origion));
    }
}
