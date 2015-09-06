package com.baidu.ub.msoa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by pippo on 15/7/27.
 */
public class JarPreferClassLoader extends URLClassLoader {

    private static Logger logger = LoggerFactory.getLogger(JarPreferClassLoader.class);
    private URL expectJarURL;
    //    private String expectJarPath;
    //    private URLClassLoader jarClassLoader;

    //    public JarPreferClassLoader(ClassLoader parent, String clazz4Jar) {
    //        super(parent);
    //
    //        String classFile = clazz4Jar.replaceAll("\\.", "\\/") + ".class";
    //        URL url = Thread.currentThread().getContextClassLoader().getResource(classFile);
    //
    //        expectJarPath = url != null ? url.getPath().replace(classFile, "") : null;
    //        try {
    //            jarClassLoader = expectJarPath != null ? new URLClassLoader(new URL[] { new URL(url.toString()
    //                    .replace(classFile, "")) }, null) : null;
    //        } catch (MalformedURLException e) {
    //
    //        }
    //
    //        Thread.currentThread().setContextClassLoader(this);
    //
    //    }

    public JarPreferClassLoader(String clazz4Jar, ClassLoader parent) {
        super(new URL[] {}, parent != null ? parent : Thread.currentThread().getContextClassLoader());
        addJarURL(clazz4Jar);
    }

    private void addJarURL(String clazz4Jar) {
        String classFile = clazz4Jar.replaceAll("\\.", "\\/") + ".class";
        URL url = Thread.currentThread().getContextClassLoader().getResource(classFile);

        try {
            expectJarURL = new URL(url.toString().replace(classFile, ""));
            addURL(expectJarURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        ClassNotFoundException ex = null;

        // && !name.equals("com.sirius.component.container.support.ComponentContainer")

        if (c == null) {
            if (!name.equals("com.sirius.component.container.support.ComponentContainer")) {
                try {
                    c = this.findClass(name);
                } catch (ClassNotFoundException e) {
                    ex = e;
                }
            }

            if (c != null) {
                logger.info("load class:[{}] from jar:[{}]", name, expectJarURL);
            }
        }

        if (c == null && getParent() != null) {
            try {
                c = getParent().loadClass(name);
            } catch (ClassNotFoundException e) {
                ex = e;
            }

            if (c != null) {
                logger.info("load class:[{}] from parent:[{}]", name, getParent());
            }
        }

        if (c == null && ex != null) {
            throw ex;
        }

        if (resolve) {
            resolveClass(c);
        }

        return c;
    }

    //    private Class<?> loadFromURL(String name) {
    //        if (jarClassLoader == null) {
    //            return null;
    //        }
    //
    //        Class<?> c = null;
    //
    //        try {
    //            if (matchJarPath(name)) {
    //                logger.info("load class:[{}] from url:[{}]", name, expectJarPath);
    //                c = jarClassLoader.loadClass(name);
    //            }
    //        } catch (Throwable e) {
    //            // logger.warn("load class due to error", e);
    //        }
    //
    //        return c;
    //    }
    //
    //    private boolean matchJarPath(String name) throws IOException {
    //        String path = name.replaceAll("\\.", "\\/");
    //        Enumeration<URL> enumeration = getResources(path + ".class");
    //
    //        URL selected;
    //
    //        while (enumeration.hasMoreElements()) {
    //            selected = enumeration.nextElement();
    //            String currentJarPath = selected.getPath().replace(path + ".class", "");
    //
    //            if (currentJarPath.equals(expectJarPath)) {
    //                return true;
    //            }
    //        }
    //
    //        return false;
    //    }
    //
    //    private Set<Pattern> _patterns = new HashSet<>();
    //
    //    public void addExcludes(String... patterns) {
    //        if (patterns != null) {
    //            for (String pattern : patterns) {
    //                _patterns.add(Pattern.compile(pattern));
    //            }
    //        }
    //    }
    //
    //    private boolean isExclude(String name) {
    //
    //        for (Pattern pattern : _patterns) {
    //            if (pattern.matcher(name).find()) {
    //                logger.info("exclude loader for class:[{}]", name);
    //                return true;
    //            }
    //        }
    //
    //        logger.info("default class loader for class:[{}]", name);
    //        return false;
    //    }
}
