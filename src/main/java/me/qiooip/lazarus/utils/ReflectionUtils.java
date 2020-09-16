package me.qiooip.lazarus.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Field setAccessibleAndGet(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field;
    }
}
