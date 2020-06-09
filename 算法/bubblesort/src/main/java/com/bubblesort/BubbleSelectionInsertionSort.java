package com.bubblesort;

import java.util.Arrays;

/**
 * @Author: liukun
 * @Description:
 * @Date: 2020/3/10.11:03
 */
public class BubbleSelectionInsertionSort {

    /**
     * 选择排序，由小到大输出：每次都是从底最后一个，跟其上面的所有数据做比较，每次比较过程中，用minIndex来记录这一轮最小数的下标，待结束后放在较小的存在0号位置。
     * 注意的就是不要没一个比较的时候直接交换，只记录下标，等到内层循环结束之后再做交换
     * 总共比较的次数是一定的：(n-1)+(n-2)+...+1 = (n-1)*n/2 = 1/2*n² - 1/2*n，那么时间复杂度为O(n²)
     * @param origion
     * @return
     */
    public static int[] selectSort(int [] origion) {
        System.out.println("排序前数组是：" + Arrays.toString(origion));
        int minIndex = 0;
        for (int i = 0; i < origion.length -1; i++) {
            minIndex = i;
            for (int j =  i + 1; j < origion.length; j++) {
                if (origion[minIndex] > origion[j]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                origion[i] = origion[i] + origion[minIndex];
                origion[minIndex] = origion[i] - origion[minIndex];
                origion[i] = origion[i] - origion[minIndex];
            }
        }
        System.out.println("排序后的结果是：" + Arrays.toString(origion));
        return origion;
    }

    /**
     * 冒泡排序，每次比较都是从0号元素开始，一次比较0-1,1-2,2-3...，一次循环结束，最大的元素放在n-1号最大的位置。
     * 下一次比较也是从0号元素开始，只是最大的元素已经在n-1位置，那么n-1位置不需要参与循环比较
     * 相比与普通的冒泡排序，可以减少比较的次数： 如果在一次冒泡过程中，一次交换都没有执行，那么整个数组就已经是排序的状态了
     * 总共比较的次数是不定的：最多是(n-1)+(n-2)+...+1 = (n-1)*n/2 = 1/2*n² - 1/2*n，那么最大的时间复杂度为O(n²)；最少是n-1次，最小的时间复杂度是O(n)
     * @param origion
     * @return
     */
    public static int[] bubbleSort(int[] origion) {
        System.out.println("排序前数组是：" + Arrays.toString(origion));
        // 是否交换的标识，如果交换了，那么就是true，如果没有交换，那就是false
        boolean swithFlag =  false;
        // 每次都是从最小编号开始
        for (int i = 0; i < origion.length -1; i++) {
            // 每执行完一次循环，最大的数在最后面，并且下一次，最后一个数就不需要比较了
            for (int j =  0; j < origion.length - i - 1; j++) {
                // 举例：比较完origion[0]和origion[1]之后，继续比较origion[1]和origion[2]
                if (origion[j] > origion[j + 1]) {
                    origion[j] = origion[j] + origion[j + 1];
                    origion[j + 1] = origion[j] - origion[j + 1];
                    origion[j] = origion[j] - origion[j + 1];
                    swithFlag = true;
                }
            }
            // 如果执行完内层循环，发现无需交换，那么代表整个数组已经是有序状态
            if (swithFlag == false) {
                System.out.println("数组已经有序，无需继续冒泡");
                break;
            }
        }
        System.out.println("排序后的结果是：" + Arrays.toString(origion));
        return origion;
    }

    /**
     * 插入排序：让元素i之前的元素都保持由小到大排序，每次比较都是跟当前元素之前的所有元素进行比较。好比从牌堆里面摸牌，摸出来的牌每次都要从手上的牌从后往前理一遍。
     * 思路是从下标1元素开始，跟之前的所有元素比较，如果当前元素比前一个元素小，那么就进行交换。这样，每一轮下来，位置i的元素会被安排到合适的位置。
     * tips：如果当前比对的元素，比之前的一个元素要大，那么就可以不继续了，因为前前一个元素一定是比前一个元素小的
     * 图形化的演示http://cmsblogs.com/?p=4688
     * 总共比较的次数是不定的：最多是(n-1)+(n-2)+...+1 = (n-1)*n/2 = 1/2*n² - 1/2*n，那么最大的时间复杂度为O(n²)；最少是n-1次，最小的时间复杂度是O(n)
     * @param origion
     * @return
     */
    public static int[] insertionSelection(int[] origion) {
        System.out.println("排序前数组是：" + Arrays.toString(origion));
        for (int i = 1; i < origion.length; i ++) {
            System.out.println();
            for (int j = i; j >= 1; j--) {
                System.out.println("开始比对：" + (j) + "与" + (j-1));
                if (origion[j] >= origion[j - 1]) {
                    System.out.println("位置为" + j + "的元素无需再比较");
                    break;
                }
                else {
                    System.out.println("--->需要交换：" + (j) + "与" + (j-1));
                    // 当前元素比之前的元素要小，那么需要交换
                    origion[j] = origion[j] + origion[j - 1];
                    origion[j - 1] = origion[j] - origion[j - 1];
                    origion[j] = origion[j] - origion[j - 1];
                }
            }
        }
        System.out.println("排序后的结果是：" + Arrays.toString(origion));
        return origion;
    }

    public static void main(String[] args) {
        // 冒泡排序测试
//        int[] origion = {5,4,3,2,1};
//        BubbleSort.bubbleSort(origion);
        // 选择排序测试
//        int[] origion = {5,4,3,2,1};
//        BubbleSelectionInsertionSort.selectSort(origion);
        // 插入排序测试
        int[] origion = {1,4,3,2,5};
        BubbleSelectionInsertionSort.insertionSelection(origion);
    }
}
