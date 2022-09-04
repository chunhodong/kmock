package io.github.test.kmock.listener;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

@Slf4j
@ToString
public class KMockTestExecutionListener extends AbstractTestExecutionListener {


    /**
     * 테스트클래스내부 어노테이션필드 mocking처리
     * @param testContext
     * @throws Exception
     */
    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        
    }
}
