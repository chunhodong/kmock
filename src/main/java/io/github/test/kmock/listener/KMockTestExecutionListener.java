package io.github.test.kmock.listener;

import io.github.test.kmock.annotation.KMockBean;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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


        //필드주입
        long count = Arrays.stream(fields)
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .filter(annotation -> annotation.annotationType() == KMockBean.class)
                        .count() > 0)
                .count();

        //각 테스트실행전 reset

    }
}
