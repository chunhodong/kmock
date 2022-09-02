package io.github.test.kmock.config;

import io.github.test.kmock.postprocessor.KMockBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KMockConfig {

    @Bean
    public KMockBeanFactoryPostProcessor kMockBeanFactoryPostProcessor(){
        return new KMockBeanFactoryPostProcessor();
    }
}
