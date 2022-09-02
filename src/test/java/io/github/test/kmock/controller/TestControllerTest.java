package io.github.test.kmock.controller;

import io.github.test.kmock.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest
public class TestControllerTest {


    @MockBean
    private TestService testService;

    @Test
    void 컨트롤러테스트(){

    }
}
