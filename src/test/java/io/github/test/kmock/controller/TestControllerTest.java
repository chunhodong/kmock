package io.github.test.kmock.controller;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest
@Slf4j
public class TestControllerTest {


    @KMockBean
    private TestService testService;



    //3453
    @BeforeEach
    void 실행전(){
        log.info("before Each");
    }
    @Test
    void 컨트롤러테스트1(){
        log.info("run(1)");
        AutowiredAnnotationBeanPostProcessor awe;
    }

    @Test
    void 컨트롤러테스트2(){
        log.info("run2()");

    }

    @Test
    void 컨트롤러테스트3(){
        log.info("run3()");

    }
}
