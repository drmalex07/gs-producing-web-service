package hello.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;

@Configuration
public class Jaxb2MarshallerConfiguration
{
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller()
    {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetCountryRequest.class));
        return marshaller;
    }
}
