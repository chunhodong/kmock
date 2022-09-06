package io.github.test.kmock.listener;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
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

        //mock주입
        Arrays.stream(fields)
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .filter(annotation -> annotation.annotationType() == KMockBean.class)
                        .count() > 0)
                .forEach(field -> {
                    try {
                        Object object = testContext.getApplicationContext().getBean(field.getType());
                        field.setAccessible(true);
                        field.set(testContext.getTestInstance(), Mockito.mock(object.getClass()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        //spy주입
        Arrays.stream(fields)
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .filter(annotation -> annotation.annotationType() == KSpyBean.class)
                        .count() > 0)
                .forEach(field -> {
                    try {
                        field.getType();
                        Object object = new ObjenesisStd().getInstantiatorOf(field.getType()).newInstance();
                        field.setAccessible(true);
                        field.set(testContext.getTestInstance(),object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });


    }


}
