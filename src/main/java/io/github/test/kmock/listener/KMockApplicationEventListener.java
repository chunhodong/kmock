package io.github.test.kmock.listener;

import io.github.test.kmock.util.KMockFieldStore;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.lang.reflect.Field;

@Slf4j
@ToString
public class KMockApplicationEventListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        SpringApplication source = (SpringApplication) event.getSource();
        Class<?> sourceClass = source.getMainApplicationClass();
        Field[] fields = sourceClass.getDeclaredFields();

        KMockFieldStore properties = KMockFieldStore
                .builder()
                .fields(fields)
                .build();


        String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(new RootBeanDefinition(KMockFieldStore.class), (BeanDefinitionRegistry) event.getApplicationContext().getBeanFactory());

        event.getApplicationContext().getBeanFactory()
                .registerSingleton(beanName,properties);




    }
}
