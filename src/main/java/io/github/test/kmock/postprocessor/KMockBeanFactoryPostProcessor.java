package io.github.test.kmock.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class KMockBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //생성자 리스트조회
        //생성자에서 객체추가
        List<Constructor<?>> constructorList = Arrays.stream(beanFactory.getBeanDefinitionNames())
                .map(beanDefinitionName -> beanFactory.getBeanDefinition(beanDefinitionName))
                .filter(beanDefinition -> beanDefinition instanceof ScannedGenericBeanDefinition)
                .map(beanDefinition -> {
                    try {
                        return BeanUtils.getResolvableConstructor(
                                this.getClass().getClassLoader().loadClass(beanDefinition.getBeanClassName()));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
