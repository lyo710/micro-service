package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by pippo on 15/8/6.
 */
public class ServiceStubJarGeneratorTool {

    private static Logger logger = LoggerFactory.getLogger(ServiceStubJarGeneratorTool.class);

    public static void main(String[] args) throws Exception {
        generate(Integer.parseInt(args[0]), Class.forName(args[1]), args[2]);
    }

    public static File generate(int provider, Class<?> interfaceClazz, String targetPath) throws Exception {
        ServiceStubFileGenerator fileGenerator = new ServiceStubFileGenerator(provider, interfaceClazz);
        ServiceStubJarGenerator jarGenerator = new ServiceStubJarGenerator(fileGenerator.generate().toSchema());
        jarGenerator.setWriteClazz(true);

        File file = jarGenerator.generateJar();
        logger.info("create temp jar file:[{}]", file.getAbsolutePath());

        File target = new File(targetPath + "/" + file.getName());
        logger.info("target jar file:[{}]", target.getAbsolutePath());
        Files.copy(file, target);
        return target;
    }
}
