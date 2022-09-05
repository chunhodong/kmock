package io.github.test.kmock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JestService {
    private final FestService festService;
}
