package io.github.test.kmock.controller;

import io.github.test.kmock.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private TestService testService;


}
