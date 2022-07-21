package com.optimagrowth.license;

import com.optimagrowth.license.service.utils.UserContext;
import com.optimagrowth.license.service.utils.UserContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableDiscoveryClient
@EnableFeignClients
@RefreshScope
@SpringBootApplication
public class LicensingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        final var messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasename("messages");
        return messageSource;
    }

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        final var template = new RestTemplate();
        final var interceptors = template.getInterceptors();
        if (interceptors.isEmpty()) {
            template.setInterceptors(Collections.singletonList(interceptor()));
        } else {
            interceptors.add(interceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }

    public ClientHttpRequestInterceptor interceptor() {
        return (request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
            headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
            return execution.execute(request, body);
        };
    }

}
