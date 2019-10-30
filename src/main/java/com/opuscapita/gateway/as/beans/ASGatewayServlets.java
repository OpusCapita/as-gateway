package com.opuscapita.gateway.as.beans;

import com.helger.as2servlet.AS2ReceiveServlet;
import com.helger.as2servlet.AS2WebAppListener;
import com.helger.as2servlet.AbstractAS2ReceiveXServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ASGatewayServlets {

    @Autowired
    private ServletContext context;

    @Bean
    public ServletRegistrationBean<AS2ReceiveServlet> as2Servlet() {
        AS2WebAppListener.staticInit(context);

        final ServletRegistrationBean<AS2ReceiveServlet> servlet = new ServletRegistrationBean<>(new AS2ReceiveServlet(),
                "/as2");
        final Map<String, String> aInitParams = new HashMap<>();

        aInitParams.put(AbstractAS2ReceiveXServletHandler.SERVLET_INIT_PARAM_AS2_SERVLET_CONFIG_FILENAME,
                "config/config.xml");
        servlet.setInitParameters(aInitParams);

        return servlet;
    }

}


