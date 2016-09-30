package com.quicklib.android.core.helper;


import java.lang.reflect.Field;

public class ReflectionHelper {

    /**
     * Recursively look into the hierarchy to android the field.
     *
     * @param clazz current class
     * @param fieldName field name to android
     * @return field if found
     */
    private static Field findField(Class clazz, String fieldName){
        Field field = null;
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null){
            field = findField(clazz.getSuperclass(), fieldName);
        }
        if (field == null){
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                // nothing to do
            }
        }
        return field;
    }



    /**
     * Write object's field with value provided.
     *
     * @param target Spring component to introspect
     * @param fieldName Field name to lookup
     * @param value new value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void writeField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
