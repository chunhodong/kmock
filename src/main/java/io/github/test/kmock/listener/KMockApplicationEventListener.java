package io.github.test.kmock.listener;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import io.github.test.kmock.util.KMockFieldProperties;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@ToString
public class KMockApplicationEventListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        SpringApplication source = (SpringApplication) event.getSource();
        Class<?> sourceClass = source.getMainApplicationClass();
        Field[] fields = sourceClass.getDeclaredFields();

        KMockFieldProperties properties = KMockFieldProperties
                .builder()
                .fields(fields)
                .build();


        String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(new RootBeanDefinition(sourceClass), (BeanDefinitionRegistry) event.getApplicationContext().getBeanFactory());

        event.getApplicationContext().getBeanFactory()
                .registerSingleton(beanName,properties);




    }
}
