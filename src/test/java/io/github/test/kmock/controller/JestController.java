package io.github.test.kmock.controller;

import io.github.test.kmock.service.JestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JestController {
    private final JestService jestService;


}
