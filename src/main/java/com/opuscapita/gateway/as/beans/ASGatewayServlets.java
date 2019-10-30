package com.opuscapita.gateway.as.beans;

import com.helger.as2servlet.AS2ReceiveServlet;
import com.helger.as2servlet.AS2WebAppListener;
import com.helger.as2servlet.AbstractAS2ReceiveXServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;
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

    @Bean
    public ServletRegistrationBean<AS2ReceiveServlet> as2Servlet() throws IOException {
        AS2WebAppListener.staticInit(context);

        final ServletRegistrationBean<AS2ReceiveServlet> servlet = new ServletRegistrationBean<>(new AS2ReceiveServlet(),
                as2UrlMapping);

        final Map<String, String> params = new HashMap<>();
        params.put(
                AbstractAS2ReceiveXServletHandler.SERVLET_INIT_PARAM_AS2_SERVLET_CONFIG_FILENAME,
                as2Config.getFile().getAbsolutePath()
        );
        servlet.setInitParameters(params);

        return servlet;
    }

}


