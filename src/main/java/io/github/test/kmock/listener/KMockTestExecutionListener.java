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

        registerKMock(testContext,KMockBean.class);
        registerKMock(testContext,KSpyBean.class);



    }

    private void registerKMock(TestContext testContext,Class<?> annotationType){
        Field[] fields = testContext.getTestClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .filter(annotation -> annotation.annotationType() == annotationType)
                        .count() > 0)
                .forEach(field -> {
                    try {
                        Object instance = new ObjenesisStd().getInstantiatorOf(field.getType()).newInstance();
                        field.setAccessible(true);
                        Object object = annotationType == KMockBean.class ? Mockito.mock(instance.getClass()) : instance;
                        field.set(testContext.getTestInstance(),object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }


}
