package org.zatribune.demo.service.payment.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.zatribune.demo.service.payment.db.entity.PaymentEvent;
import org.zatribune.demo.service.payment.db.entity.PaymentSession;
import org.zatribune.demo.service.payment.db.repository.PaymentEventRepository;
import org.zatribune.demo.service.payment.db.repository.PaymentSessionRepository;
import org.zatribune.demo.service.payment.domain.Event;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {


    private final PaymentSessionRepository paymentSessionRepository;

    private final PaymentEventRepository paymentEventRepository;

    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public @NonNull Mono<Void> handle(WebSocketSession session) {
        log.info("WebSocket connection is established - handling session with id: [{}]", session.getId());

        return session.receive()
                .flatMap(webSocketMessage -> {
                    String message = webSocketMessage.getPayloadAsText();
                    //log.info("message: {}",message);
                    Event event;
                    try {
                        event = mapper.readValue(message, Event.class);
                        log.info("Received an event: {}", event);
                    } catch (JsonProcessingException e) {
                        log.error("error mapping payload: {}", message, e);
                        event = Event.builder()
                                .key("FAILED")
                                .value("UNABLE_TO_PROCESS")
                                .build();
                    }

                    switch (event.getKey()) {
                        case "OPEN_SESSION":
                            log.info("Adding a session");
                            PaymentSession paymentSession = PaymentSession.builder()
                                    .id(session.getId())
                                    .creationDate(LocalDateTime.now())
                                    .description(event.getValue())
                                    .build();
                            Event response=Event.builder()
                                    .key("OPEN_SESSION")
                                    .value(session.getId())
                                    .build();
                            return paymentSessionRepository.save(paymentSession)
                                    .and(session.send(
                                            Mono.just(session.textMessage(response.tojsonString()))));
                        default:
                        case "ADD_EVENT":
                            log.debug("Adding an event: {}",session.getId());
                            PaymentEvent paymentEvent = PaymentEvent.builder()
                                    .id(ObjectId.get())
                                    .creationDate(LocalDateTime.now())
                                    .content(event.getValue())
                                    .sessionId(session.getId())
                                    .build();
                            return paymentEventRepository.save(paymentEvent)
                                    .doOnError(error -> log.error("Error saving event", error));
                    }
                })
                .doOnError(error -> log.error("Error in WebSocket processing", error))
                .then();
    }
}