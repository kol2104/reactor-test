package com.example.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(message -> {
                    String reversedMessage = new StringBuilder(message).reverse().toString();
                    Flux<WebSocketMessage> responseFlux = Flux.interval(Duration.ofSeconds(1))
                            .take(5)
                            .map(tick -> session.textMessage("Reversed: " + reversedMessage + " - " + tick));
                    return session.send(responseFlux);
                }).then();
    }
}
