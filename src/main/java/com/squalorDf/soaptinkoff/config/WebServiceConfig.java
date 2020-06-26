package com.squalorDf.soaptinkoff.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * SOAP web-service Spring configuration class.
 */
@EnableWs
@Configuration
public class WebServiceConfig {

    /**
     * Servlet Mapping configuration method.
     * @param applicationContext container with information of Spring beans.
     * @return Bean with servlet mapping.
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    /**
     * WSDL configuration method.
     * @param numbersSchema XSD schema containing wsdl layout
     * @return WSDL bean
     */
    @Bean(name = "numbers")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema numbersSchema) {

        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();

        wsdl11Definition.setPortTypeName("NumbersPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://squalodf.org/ws/numbers");
        wsdl11Definition.setSchema(numbersSchema);

        return wsdl11Definition;
    }

    /**
     * Get XSD schema method.
     * @return XSD schema bean from resource directory
     */
    @Bean
    public XsdSchema numbersSchema() {
        return new SimpleXsdSchema(new ClassPathResource("numbers.xsd"));
    }
}
