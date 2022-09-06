package io.github.test.kmock.listener;

import io.github.test.kmock.annotation.KMockBean;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@ToString
public class KMockTestExecutionListener extends AbstractTestExecutionListener {


    /**
     * 테스트클래스내부 어노테이션필드 mocking처리
     * @param testContext
     * @throws Exception
     */
    @Override
    public void prepareTestInstance(TestContext testContext) {
        //빈으로찾기
        Field[] fields = testContext.getTestClass().getDeclaredFields();

        Arrays.stream(fields)
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .filter(annotation -> annotation.annotationType() == KMockBean.class)
                        .count() > 0)
                .forEach(field -> {
                    Object object = testContext.getApplicationContext().getBean(field.getType());

                    field.setAccessible(true);
                    try {
                        field.set(testContext.getTestInstance(), Mockito.mock(object.getClass()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });



                /*.forEach(aClass -> {
                    Object object = testContext.getApplicationContext().getBean(aClass);
                    Class c = object.getClass();

                });*/
        //각 테스트실행전 reset

    }
}
