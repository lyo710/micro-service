package com.baidu.ub.msoa.governance.inbound.controller;

import com.baidu.ub.msoa.container.support.governance.contact.proto.ServiceStubJarGenerator;
import com.baidu.ub.msoa.governance.service.register.ServiceRegistry;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pippo on 15/8/27.
 */
@Controller
public class MavenRepositoryController {

    private static String pomTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
            + "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 "
            + "                              http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
            + "    <modelVersion>4.0.0</modelVersion>\n"
            + "    <groupId>%s</groupId>\n"
            + "    <artifactId>%s</artifactId>\n"
            + "    <version>%s</version>\n"
            + "</project>";

    @Resource
    private ServiceRegistry serviceRegistry;

    @RequestMapping("maven/msoa/provider-{provider}/{service}/{version}/*.pom")
    @ResponseBody
    public String pom(@PathVariable("provider") String provider,
            @PathVariable("service") String service,
            @PathVariable("version") String version) {
        return String.format(pomTemplate, provider, service, version);
    }

    @RequestMapping("maven/msoa/provider-{provider}/{service}/{version}/*.pom.md5")
    @ResponseBody
    public String md5(@PathVariable("provider") String provider,
            @PathVariable("service") String service,
            @PathVariable("version") String version) {
        return "";
    }

    @RequestMapping("maven/msoa/provider-{provider}/{service}/{version}/*.jar")
    public void jar(@PathVariable("provider") int provider,
            @PathVariable("service") String service,
            @PathVariable("version") int version,
            HttpServletResponse response) throws IOException {

        String schema = serviceRegistry.getSchema(provider, service, version);
        if (Strings.isNullOrEmpty(schema)) {
            response.setStatus(404);
            return;
        }

        ServiceStubJarGenerator generator = new ServiceStubJarGenerator(schema);
        InputStream stream = null;

        try {
            File file = generator.generateJar();
            stream = new FileInputStream(file);
            ByteStreams.copy(stream, response.getOutputStream());
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write(Throwables.getStackTraceAsString(e));
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

}
