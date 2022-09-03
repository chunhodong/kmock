package io.github.test.kmock.listener;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

@Slf4j
@ToString
public class KMockTestExecutionListener extends AbstractTestExecutionListener {


    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        //어노테이션추가해서 처리
    }
}
