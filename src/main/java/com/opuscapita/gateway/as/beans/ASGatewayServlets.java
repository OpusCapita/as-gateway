package com.opuscapita.gateway.as.beans;

import com.helger.as2servlet.AS2ReceiveServlet;
import com.helger.as2servlet.AS2WebAppListener;
import com.helger.as2servlet.AbstractAS2ReceiveXServletHandler;
import com.helger.phase4.servlet.AS4Servlet;
import com.helger.photon.core.servlet.WebAppListener;
import com.helger.web.scope.mgr.WebScopeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ASGatewayServlets {

    @Autowired
    private ServletContext context;

    @Value("classpath:as/as2config.xml")
    private Resource as2Config;

    @Value("${as2.urlmapping:/as2}")
    private String as2UrlMapping;

    @Value(("${as4.urlmapping:/as4}"))
    private String as4UrlMapping;

    @PostConstruct
    public void initServletContext() {
        AS2WebAppListener.staticInit(context);
    }

    @Bean
    public ServletRegistrationBean<AS2ReceiveServlet> as2Servlet() throws IOException {
        final ServletRegistrationBean<AS2ReceiveServlet> servlet =
                new ServletRegistrationBean<>(new AS2ReceiveServlet(), as2UrlMapping);
        final Map<String, String> params = new HashMap<>();
        params.put(
                AbstractAS2ReceiveXServletHandler.SERVLET_INIT_PARAM_AS2_SERVLET_CONFIG_FILENAME,
                as2Config.getFile().getAbsolutePath()
        );
        servlet.setInitParameters(params);

        return servlet;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> as4ServletBean(ServletContext context) {
        return new ServletRegistrationBean<>(new AS4Servlet(), as4UrlMapping);
    }

}


