package io.github.test.kmock.controller;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;
import io.github.test.kmock.service.JestService;
import io.github.test.kmock.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
@Slf4j
public class JestControllerTest {


    @KSpyBean
    private JestService testService;



    @BeforeEach
    void 실행전(){
        log.info("before Each");
    }
    @Test
    void 컨트롤러테스트(){
        log.info("run()");

    }
}