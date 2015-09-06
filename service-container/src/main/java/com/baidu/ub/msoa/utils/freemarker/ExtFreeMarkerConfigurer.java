package com.baidu.ub.msoa.utils.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by pippo on 14-3-23.
 */
public class ExtFreeMarkerConfigurer extends FreeMarkerConfigurer implements WebApplicationInitializer {

    protected TaglibFactory taglibFactory;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        taglibFactory = new TaglibFactory(servletContext);
    }

    @Override
    public TaglibFactory getTaglibFactory() {
        return taglibFactory;
    }

}
