package com.baidu.ub.msoa.utils;

import java.lang.annotation.Annotation;

/**
 * Created by pippo on 15/8/20.
 */
public class AnnotationUtil {

    /**
     * find annotation bind info
     *
     * @param targetClazz
     * @param annotationType
     * @param <A>
     * @return AnnotationBindInfo<A>
     */
    public static <A extends Annotation> AnnotationBindInfo<A> find(Class<?> targetClazz, Class<A> annotationType) {
        A annotation = targetClazz.getAnnotation(annotationType);
        if (annotation != null) {
            return new AnnotationBindInfo<>(annotation, targetClazz);
        }

        Class<?>[] interfaces = targetClazz.getInterfaces();
        for (Class<?> iClazz : interfaces) {
            annotation = iClazz.getAnnotation(annotationType);
            if (annotation != null) {
                return new AnnotationBindInfo<>(annotation, iClazz);
            }
        }

        return null;
    }

    public static class AnnotationBindInfo<A extends Annotation> {

        public AnnotationBindInfo(A annotation, Class<?> bindClazz) {
            this.annotation = annotation;
            this.bind = bindClazz;
        }

        public final A annotation;
        public final Class<?> bind;

    }
}
