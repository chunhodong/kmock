package io.github.test.kmock.controller;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import io.github.test.kmock.service.JestService;
import io.github.test.kmock.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(TestController.class)
@Slf4j
public class TestControllerTest {


    @KMockBean
    private TestService testService;

    @KSpyBean
    private JestService jestService;


    @BeforeAll
    static void 전체전(){
        log.info("before Each");

    }

    @BeforeEach
    void 실행전(){
        log.info("before Each");
    }

    @Test
    void 컨트롤러테스트1(){
        System.out.println("ttt11");
    }


    @Test
    void 컨트롤러테스트2(){
        System.out.println("ttt22");
    }

}
