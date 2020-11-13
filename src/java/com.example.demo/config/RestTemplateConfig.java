package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Autowired
    CloseableHttpClient httpClient;

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        //Default restTemplate
        //log.info("###Use Default RestTemplate###");
        //RestTemplate restTemplate = new RestTemplate();

        //Build a RestTemplate with HttpClient
        log.info("###Use Customized RestTemplate###");
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

        //add it to the beginning of the list
        //so that it takes precedence over the default MappingJackson2HttpMessageConverter that Spring has already registered.
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(20);
        return scheduler;
    }

    public com.fasterxml.jackson.databind.ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    public MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(createObjectMapper());
        return converter;
    }
}
