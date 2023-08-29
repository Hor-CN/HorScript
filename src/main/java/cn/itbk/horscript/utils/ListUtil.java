package cn.itbk.horscript.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     */
    public static <T> List<T>
    sub(List<T> list, int start, int end) {
        return sub(list, start, end, 1);
    }


    /**
     * 截取集合的部分<br>
     * 此方法与{@link List#subList(int, int)} 不同在于子列表是新的副本，操作子列表不会影响原列表。
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @param step  步进
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     * @since 4.0.6
     */
    public static <T> List<T> sub(List<T> list, Integer start, Integer end, int step) {
        if (list == null) {
            return null;
        }
        if (step == 0) {
            step = 1;
        }

        if (list.isEmpty()) {
            return new ArrayList<>(0);
        }
        final int size = list.size();

        if (step < 0) {
            // 从右往左取值，而startIndex省略时，表示从端点开始，因此这里的端点是“终点”
            if (start == null) {
                start = size - 1;
            } else if (start < 0) {
                start += size;
            }
            if (end == null) {
                end = -1;
            } else if (end < 0) {
                end += size;
            }
        } else {
            // 从左往右取值，而startIndex省略时，表示从端点开始，因此这里的端点是“起点”
            if (start == null) {
                start = 0;
            } else if (start < 0) {
                start += size;
            }
            if (end == null) {
                end = size;
            }else if (end < 0) {
                end += size;
            }
        }


        if (start == size) {
            return new ArrayList<>(0);
        }

//        if (start > end) {
//            int tmp = start;
//            start = end;
//            end = tmp;
//        }
//        if (end > size) {
//            if (start >= size) {
//                return new ArrayList<>(0);
//            }
//            end = size;
//        }

        final List<T> result = new ArrayList<>();

        for (int i = start; step > 0 ? i < end : i > end; i += step) {

            result.add(list.get(i));
        }
        return result;
    }


}
