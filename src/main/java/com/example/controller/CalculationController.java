package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class CalculationController {

    @GetMapping("/calculate")
    public Mono<String> calculateSquare(@RequestParam int number) {
        return Mono.fromCallable(() -> number * number)
                .map(result -> "Square: " + result);
    }

    @GetMapping("/countdown")
    public Flux<String> countdown(@RequestParam int number) {
        return Flux.range(0, number + 1)
                .map(i -> number - i)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Countdown: " + i + "\n");
    }
}
