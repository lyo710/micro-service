package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.google.common.io.Files;
import javassist.CtClass;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * Created by pippo on 15/8/6.
 */
public class ServiceStubJarGenerator extends ServiceStubClassGenerator {

    private static Logger logger = LoggerFactory.getLogger(ServiceStubJarGenerator.class);

    public ServiceStubJarGenerator(String schema) {
        super(schema);
    }

    //    protected Set<String> paths = new HashSet<>();

    public File generateJar() throws Exception {
        generate();

        File file = new File(String.format("/%s/service-stub/%s/%s.%s-%s.jar",
                SystemUtils.getJavaIoTmpDir(),
                System.currentTimeMillis(),
                provider,
                service.toLowerCase() + "-stub",
                version));

        Files.createParentDirs(file);

        logger.info("create service stub jar:[{}]", file.getAbsolutePath());

        Tree tree = new Tree();
        for (Class<?> clazz : generatedClazz) {
            CtClass ctClass = pool.get(clazz.getName());
            String[] packagePath = ctClass.getPackageName().split("\\.");
            tree.add(packagePath, ctClass.getName());
        }

        //        System.out.println(tree);

        FileOutputStream out = new FileOutputStream(file);
        JarOutputStream jarOut = new JarOutputStream(out);

        try {
            tree.write(jarOut);
        } finally {
            jarOut.close();
            out.close();
        }
        //
        //        try {
        //            for (Class<?> clazz : generatedClazz) {
        //                CtClass ctClass = pool.get(clazz.getName());
        //                String[] steps = ctClass.getPackageName().split("\\.");
        //                createPath(jarOut, steps, 1, steps[0]);
        //
        //                String className = ctClass.getName().replace(".", "/") + ".class";
        //                logger.info("add class:[{}] to jar", className);
        //
        //                jarOut.putNextEntry(new ZipEntry(className));
        //                jarOut.write(ctClass.toBytecode());
        //                jarOut.closeEntry();
        //            }
        //        } finally {
        //            jarOut.remove();
        //            out.remove();
        //        }

        return file;
    }

    //    private void createPath(JarOutputStream jarOut, String[] steps, int index, String parent) throws IOException {
    //        if (index > steps.length - 1) {
    //            return;
    //        }
    //
    //        String current = steps[index];
    //        String path = parent + "/" + current;
    //        if (!paths.contains(path)) {
    //            logger.debug("add entry:[{}]", path);
    //            jarOut.putNextEntry(new ZipEntry(path));
    //            paths.add(path);
    //        }
    //
    //        createPath(jarOut, steps, index + 1, path);
    //    }

    private class Tree {

        void add(String[] packagePath, String className) {

            Node match = null;
            String root = packagePath[0];
            for (Node node : nodes) {
                if (node.path.equals(root)) {
                    match = node;
                    break;
                }
            }

            if (match == null) {
                match = new Node();
                match.path = root;
                nodes.add(match);
            }

            match.add(Arrays.copyOfRange(packagePath, 1, packagePath.length), className);
        }

        void write(JarOutputStream jarOut) throws Exception {
            for (Node node : nodes) {
                node.write(jarOut);
            }
        }

        Set<Node> nodes = new HashSet<>();

        @Override
        public String toString() {
            return String.format("Tree{ 'nodes'=%s}", nodes);
        }
    }

    private class Node {

        void add(String[] packagePath, String className) {

            if (packagePath.length == 0) {
                classes.add(className);
                return;
            }

            Node match = null;
            String root = packagePath[0];
            for (Node node : nodes) {
                if (node.path.equals(root)) {
                    match = node;
                    break;
                }
            }

            if (match == null) {
                match = new Node();
                match.parent = this;
                match.path = root;
                nodes.add(match);
            }

            match.add(Arrays.copyOfRange(packagePath, 1, packagePath.length), className);
        }

        void write(JarOutputStream jarOut) throws Exception {
            String fullPath = getFullPath();
            logger.debug("add path:[{}]", fullPath);
            // jarOut.putNextEntry(new ZipEntry(fullPath));

            for (String clazz : classes) {
                CtClass ctClass = pool.get(clazz);
                String className = ctClass.getName().replace(".", "/") + ".class";

                logger.info("add class:[{}] to jar", className);
                jarOut.putNextEntry(new ZipEntry(className));
                jarOut.write(ctClass.toBytecode());
                jarOut.closeEntry();
            }

            for (Node node : nodes) {
                node.write(jarOut);
            }
        }

        String getFullPath() {
            Node pNode = parent;
            String pPath = path;

            while (pNode != null) {
                pPath = pNode.path + "/" + pPath;
                pNode = pNode.parent;
            }

            return pPath;
        }

        Node parent;
        String path;
        Set<String> classes = new HashSet<>();
        Set<Node> nodes = new HashSet<>();

        @Override
        public String toString() {
            return String.format("Node{'path'=%s, 'classes'=%s, 'nodes'=%s}", getFullPath(), classes, nodes);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node)) {
                return false;
            }
            Node node = (Node) o;
            return Objects.equals(path, node.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }

    }
}
