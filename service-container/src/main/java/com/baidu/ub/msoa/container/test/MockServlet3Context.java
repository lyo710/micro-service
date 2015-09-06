package com.baidu.ub.msoa.container.test;

import org.springframework.mock.web.MockServletContext;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletSecurityElement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by pippo on 15/7/3.
 */
public class MockServlet3Context extends MockServletContext {

    @Override
    public Dynamic addServlet(String servletName, Servlet servlet) {
        return new MockServletRegistrationDynamic(servletName, servlet);
    }

    @Override
    public String getVirtualServerName() {
        return null;
    }

    public class MockServletRegistrationDynamic implements Dynamic {

        public MockServletRegistrationDynamic(String name, Servlet servlet) {
            this.name = name;
            this.servlet = servlet;
        }

        private String name;

        private Servlet servlet;

        @Override
        public void setLoadOnStartup(int loadOnStartup) {

        }

        @Override
        public Set<String> setServletSecurity(ServletSecurityElement constraint) {
            return null;
        }

        @Override
        public void setMultipartConfig(MultipartConfigElement multipartConfig) {

        }

        @Override
        public void setRunAsRole(String roleName) {

        }

        @Override
        public void setAsyncSupported(boolean isAsyncSupported) {

        }

        @Override
        public Set<String> addMapping(String... urlPatterns) {
            return null;
        }

        @Override
        public Collection<String> getMappings() {
            return null;
        }

        @Override
        public String getRunAsRole() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getClassName() {
            return null;
        }

        @Override
        public boolean setInitParameter(String name, String value) {
            return false;
        }

        @Override
        public String getInitParameter(String name) {
            return null;
        }

        @Override
        public Set<String> setInitParameters(Map<String, String> initParameters) {
            return null;
        }

        @Override
        public Map<String, String> getInitParameters() {
            return null;
        }
    }
}
