package io.github.test.kmock.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
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
        List<Constructor<?>> constructorList = getConstructors(beanFactory);

        //생성자파라미터추출
        List<Class<?>> parameterClasses = getConstructorParameters(constructorList);


        //1.같은 클래스에 다른변수명
        //2. 같은 인터페이스에 다른 클래스



        //객체등록
        parameterClasses.forEach(aClass -> {
            String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(new RootBeanDefinition(aClass),(BeanDefinitionRegistry) beanFactory);
            Object object = new ObjenesisStd().getInstantiatorOf(aClass).newInstance();
            beanFactory.registerSingleton(beanName,object);
        });
    }

    private List<Constructor<?>> getConstructors(ConfigurableListableBeanFactory beanFactory){
        return Arrays.stream(beanFactory.getBeanDefinitionNames())
                .map(beanDefinitionName -> beanFactory.getBeanDefinition(beanDefinitionName))
                .filter(beanDefinition -> beanDefinition instanceof ScannedGenericBeanDefinition)
                .map(beanDefinition -> {
                    try {
                        Constructor<?> constructor = BeanUtils.getResolvableConstructor(this.getClass().getClassLoader().loadClass(beanDefinition.getBeanClassName()));

                        return constructor;
                    } catch (ClassNotFoundException e) {

                        return null;

                    }
                })
                .collect(Collectors.toList());
    }

    private List<Class<?>> getConstructorParameters(List<Constructor<?>> constructorList){
        return constructorList
                .stream()
                .flatMap(constructor -> Arrays.stream(constructor.getParameterTypes()))
                .distinct()
                .collect(Collectors.toList());
    }
  

    
}
