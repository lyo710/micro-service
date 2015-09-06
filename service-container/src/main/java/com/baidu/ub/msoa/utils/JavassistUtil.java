package com.baidu.ub.msoa.utils;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.Loader;

/**
 * Created by pippo on 15/7/8.
 */
public class JavassistUtil {

    static ClassPool defaultPool = createPool();
    static ClassLoader defaultLoader = createLoader(Thread.currentThread().getContextClassLoader(), defaultPool);

    /**
     * javaassist 默认的classpool默认搜索路径是使用jvm的System ClassLoader的ClassPath
     * 当在容器中启动时会搜索不到lib
     *
     * @return 已经添加了容器lib路径的javaassist pool
     */
    public static ClassPool getDefault() {
        return defaultPool;
    }

    /**
     * 加载javaassist创建的class
     *
     * @param name
     * @return class if exists
     */
    public static Class<?> loadClass(String name) {
        try {
            return defaultLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * @return created new class pool
     */
    public static ClassPool createPool() {
        ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        pool.insertClassPath(new ClassClassPath(JavassistUtil.class));
        return pool;
    }

    /**
     * @param pool
     * @return created new class loader with given class pool
     */
    public static Loader createLoader(ClassPool pool) {
        return new CustomLoader(pool);
    }

    /**
     * @param parent
     * @param pool
     * @return created new class loader with given class pool
     */
    public static Loader createLoader(ClassLoader parent, ClassPool pool) {
        return new CustomLoader(parent, pool);
    }

    public static class CustomLoader extends Loader {

        public CustomLoader() {
        }

        public CustomLoader(ClassPool cp) {
            super(cp);
        }

        public CustomLoader(ClassLoader parent, ClassPool cp) {
            super(parent, cp);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException {
            Class<?> clazz = super.loadClass(name, resolve);

            /* 因为javassist在defineClass的时候用了反射,没有调用到definePackage,会导致define出来的class的package为空 */
            /* 所以在load的时候,额外做一次definePackage的动作,稍微影响影响 */
            if (clazz != null && clazz.getPackage() == null) {

                int i = name.lastIndexOf('.');
                if (i != -1) {
                    String pname = name.substring(0, i);
                    if (getPackage(pname) == null) {
                        try {
                            definePackage(pname, null, null, null, null, null, null, null);
                        } catch (IllegalArgumentException e) {
                            // ignore.  maybe the package object for the same
                            // name has been created just right away.
                        }
                    }
                }
            }

            return clazz;
        }
    }
}
