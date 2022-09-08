package io.github.test.kmock.listener;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import io.github.test.kmock.util.KMockFieldStore;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@ToString
public class KMockTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        super.beforeTestMethod(testContext);
    }

    @Override
    public void beforeTestExecution(TestContext testContext) throws Exception {
        super.beforeTestExecution(testContext);
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {

        super.beforeTestClass(testContext);

    }


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

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        super.afterTestMethod(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
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
