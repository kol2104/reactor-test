package com.example.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    public static List<WebSocketSession> sessionList = new ArrayList<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        sessionList.add(session);
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

    @Scheduled(fixedDelay = 2000L)
    public void sched() {
        System.out.println("Sched");
        for (WebSocketSession session : sessionList) {
            System.out.println(session.getId());
            Mono<WebSocketMessage> responseMono = Mono.just(session.textMessage("Session id: " + session.getId() + " Local: " + LocalDateTime.now()));
            session.send(responseMono).block();
        }
    }
}
