package hello;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter 
{    
    @Bean
	public ServletRegistrationBean<?> messageDispatcherServlet(ApplicationContext applicationContext) 
    {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}

    // Note: The name of the Wsdl11Definition bean will be the one used for the WSDL document
    // e.g. http://localhost:8080/ws/countriesService.wsdl
    
	@Bean
	public DefaultWsdl11Definition countriesService(XsdSchema countriesSchema) 
	{
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		
		wsdl11Definition.setPortTypeName(CountryEndpoint.SERVICE_PORT_NAME);
		wsdl11Definition.setServiceName(CountryEndpoint.SERVICE_NAME);
		wsdl11Definition.setTargetNamespace(CountryEndpoint.NAMESPACE_URI);
		wsdl11Definition.setSchema(countriesSchema);
		
		// Note: The following sets the soap:address value under WSDL. 
		// The host part will be derived by the "Host" header of each request
		wsdl11Definition.setLocationUri("/ws");
		
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema countriesSchema() 
	{
		return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
	}
}
