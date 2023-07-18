package utils;



import java.util.ArrayList;

public class ArrayUtil {


    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];

    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];

    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];

    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];

    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];

    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];

    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];

    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

    /**
     * 数组是否为空
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }


    /**
     * 是否包含{@code null}元素
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 是否包含{@code null}元素
     * @since 3.0.7
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasNull(T... array) {
        if (isNotEmpty(array)) {
            for (T element : array) {
                if (ObjectUtil.isNull(element)) {
                    return true;
                }
            }
        }
        return array == null;
    }

    /**
     * 数组是否为非空
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return (null != array && array.length != 0);
    }

    /**
     * 删除数组中空元素
     */
    public static Object[] clearNull(final Object[] arr) {
        ArrayList<Object> list = new ArrayList<>();
        if (arr != null) {
            for (Object item : arr) {
                if (item != null) {
                    list.add(item);
                }
            }
        }
        return list.toArray();
    }


    /**
     * <p>Converts an array of primitive booleans to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>boolean</code> array
     * @return a <code>Boolean</code> array, <code>null</code> if null array input
     */
    public static Boolean[] toObject(final boolean[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        final Boolean[] result = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE;
        }
        return result;
    }

    /**
     * <p>Converts an array of primitive bytes to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>byte</code> array
     * @return a <code>Byte</code> array, <code>null</code> if null array input
     */
    public static Byte[] toObject(final byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        final Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    /**
     * <p>Converts an array of primitive shorts to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>short</code> array
     * @return a <code>Short</code> array, <code>null</code> if null array input
     */
    public static Short[] toObject(final short[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        final Short[] result = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    /**
     * <p>Converts an array of primitive ints to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array an <code>int</code> array
     * @return an <code>Integer</code> array, <code>null</code> if null array input
     */
    public static Integer[] toObject(final int[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        final Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    /**
     * <p>Converts an array of primitive longs to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>long</code> array
     * @return a <code>Long</code> array, <code>null</code> if null array input
     */
    public static Long[] toObject(final long[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        final Long[] result = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    /**
     * <p>Converts an array of primitive chars to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>char</code> array
     * @return a <code>Character</code> array, <code>null</code> if null array input
     */
    public static Character[] toObject(final char[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        final Character[] result = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }


    /**
     * <p>Converts an array of primitive floats to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>float</code> array
     * @return a <code>Float</code> array, <code>null</code> if null array input
     */
    public static Float[] toObject(final float[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        final Float[] result = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }


    /**
     * <p>Converts an array of primitive doubles to objects.</p>
     *
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array a <code>double</code> array
     * @return a <code>Double</code> array, <code>null</code> if null array input
     */
    public static Double[] toObject(final double[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        final Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

}
