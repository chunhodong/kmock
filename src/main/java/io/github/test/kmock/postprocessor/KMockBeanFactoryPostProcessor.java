package io.github.test.kmock.postprocessor;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import io.github.test.kmock.util.KMockFieldStore;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
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
     *
     * @param beanFactory
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        KMockFieldStore kMockFieldProperties = beanFactory.getBean(KMockFieldStore.class);


        //생성자추출
        List<Constructor<?>> constructorList = getConstructors(beanFactory);

        //생성자파라미터추출
        List<Class<?>> parameterClasses = getConstructorParameters(constructorList);

        //객체등록
        parameterClasses.stream()
                .forEach(aClass -> {


                    String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(new RootBeanDefinition(aClass), (BeanDefinitionRegistry) beanFactory);
                    Object object = new ObjenesisStd().getInstantiatorOf(aClass).newInstance();


                    if(kMockFieldProperties.isContainsAnnotation(aClass, KMockBean.class)){
                        beanFactory.registerSingleton(beanName, Mockito.mock(aClass));
                    }
                    else if(kMockFieldProperties.isContainsAnnotation(aClass, KSpyBean.class)){
                        beanFactory.registerSingleton(beanName, Mockito.spy(object));
                    }
                    else {
                        beanFactory.registerSingleton(beanName, object);

                    }


                });
    }

    /**
     * 사용자가 작성한 빈오브젝트에서 생성자 추출
     *
     * @param beanFactory
     * @return 생성자리스트
     */
    private List<Constructor<?>> getConstructors(ConfigurableListableBeanFactory beanFactory) {
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

    /**
     * 생성자의 파라미터타입 추출
     *
     * @param constructorList
     * @return 파라미터 클래스리스트
     */
    private List<Class<?>> getConstructorParameters(List<Constructor<?>> constructorList) {
        return constructorList
                .stream()
                .flatMap(constructor -> Arrays.stream(constructor.getParameterTypes()))
                .distinct()
                .collect(Collectors.toList());
    }


}
