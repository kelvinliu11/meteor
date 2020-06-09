/**
 *
 * 小和问题
 * 在一个数组中， 每一个数左边比当前数小的数累加起来， 叫做这个数组的小和。 求一个数组
 * 的小和。
 * 例子：
 * [1,3,4,2,5]
 * 1左边比1小的数， 没有；
 * 3左边比3小的数， 1；
 * 4左边比4小的数， 1、 3；
 * 2左边比2小的数， 1；
 * 5左边比5小的数， 1、 3、 4、 2；
 * 所以小和为1+1+3+1+1+3+4+2=16
 *
 *
 * @Author: liukun
 * @Description:
 * @Date: 2020/3/16.10:07
 */
public class LittleSum {


    /**
     *
     * 将[left,right]分成2个子列->[left,right/2],[right/2+1,right]，将第二个子列从right/2+1开始，逐一和第一个子列的元素进行比较，如果左侧比右侧元素小，则累加到tmpSum中
     * @param origion
     * @param left 数组的左侧下标
     * @param right 数组的右侧下标
     */
    public static int splitSum(int[] origion, int left, int right) {
        int tmpSum = 0;
        int middle = (left+right)/2;
        System.out.println("left="+left+";middle="+middle+";right="+right);
        if (left == right) {
            System.out.println("只有一个元素，不需要求和，元素是" + origion[left]);
            return 0;
        }
        for (int i=middle + 1;i <= right; i++) {
            // i为右侧子列的坐标
            for (int j = left; j <= middle; j ++) {
                // j为左侧子列的坐标
                if (origion[j] < origion[i]){
                    tmpSum += origion[j];
                    System.out.println(origion[j]);
                }
            }
        }
        return tmpSum;
    }

    /**
     * 递归调用，主要的逻辑就是把数组拆分成2个子列，分别对子列进行小和计算，并且把子列小和计算的结果进行累加
     * @param origion
     * @param left
     * @param right
     * @return
     */
    public static int littleSum(int[] origion, int left, int right) {
        int allSum = 0;
        // 当前[left,right]数组求小和，之后再拆分为子列数组继续求小和
        allSum += splitSum(origion, left, right);
        int middle = (left+right)/2;
        if (middle > left) {
            // 左边子列求小和
            int leftSum = littleSum(origion, left, middle);
            // 右边子列求小和
            int rightSum = littleSum(origion, middle + 1, right);
            allSum += leftSum + rightSum;
        }
        return allSum;
    }

    public static void main(String[] args) {
        int[] origion = {1,3,4,2,5};
        int littleSum = littleSum(origion, 0, origion.length-1);
        System.out.println("小和是" + littleSum);
    }
}
