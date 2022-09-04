package io.github.test.kmock.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class KMockBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    /**
     * beanfactory에 등록된 bean이름추출
     * controller의존객체 mocking처리
     * @param beanFactory
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        
        //생성자추출
        List<Constructor<?>> constructorList = Arrays.stream(beanFactory.getBeanDefinitionNames())
                .map(beanDefinitionName -> beanFactory.getBeanDefinition(beanDefinitionName))
                .filter(beanDefinition -> {

                    System.out.println(beanDefinition.getBeanClassName());
                    return beanDefinition instanceof ScannedGenericBeanDefinition;

                })
                .map(beanDefinition -> {
                    try {
                        Constructor<?> constructor = BeanUtils.getResolvableConstructor(this.getClass().getClassLoader().loadClass(beanDefinition.getBeanClassName()));

                        return constructor;
                    } catch (ClassNotFoundException e) {

                        return null;

                    }
                })
                .collect(Collectors.toList());

        //생성자파라미터추출
        List<Class<?>> parameterClasses = constructorList
                .stream()
                .flatMap(constructor -> Arrays.stream(constructor.getParameterTypes()))
                .distinct()
                .collect(Collectors.toList());


        //mock등록
        parameterClasses.forEach(aClass -> {
            String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(new RootBeanDefinition(aClass),(BeanDefinitionRegistry) beanFactory);
        /*    try {
                Constructor<?> constructor = aClass.getConstructors()[0];

               // Mockito.mock(aClass);



            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }*/
            beanFactory.registerSingleton(beanName,Mockito.mock(aClass));


            //((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(beanName,new RootBeanDefinition(aClass));

        });
    }
    
}
