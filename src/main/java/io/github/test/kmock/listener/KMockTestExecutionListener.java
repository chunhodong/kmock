package io.github.test.kmock.listener;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@ToString
public class KMockTestExecutionListener extends AbstractTestExecutionListener {


    /**
     * 모킹필드 reset
     * @param testContext
     */
    @Override
    public void afterTestExecution(TestContext testContext)  {
        Field[] fields = testContext.getTestClass().getDeclaredFields();
        Arrays.stream(fields)
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(testContext.getTestInstance());
                    } catch (IllegalAccessException e) {
                        return null;
                    }

                })
                .filter(o -> MockUtil.isMock(o))
                .forEach(o -> Mockito.reset(o));
    }

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

    /**
     * 테스트클래스 필드에 모킹객체주입
     * @param testContext 테스트컨텍스트
     * @param annotationType 확인할 어노테이션타입
     */
    private void registerKMock(TestContext testContext,Class<?> annotationType){
        Field[] fields = testContext.getTestClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations()).filter(annotation -> annotation.annotationType().equals(annotationType))
                        .count() > 0)
                .forEach(field -> {
                    try {
                        Object object = testContext.getApplicationContext().getBean(field.getType());
                        field.setAccessible(true);
                        field.set(testContext.getTestInstance(),object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }



}
