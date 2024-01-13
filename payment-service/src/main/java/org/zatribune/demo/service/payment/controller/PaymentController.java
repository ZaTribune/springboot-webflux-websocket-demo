package org.zatribune.demo.service.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zatribune.demo.service.payment.db.entity.PaymentSession;
import org.zatribune.demo.service.payment.db.repository.PaymentEventRepository;
import org.zatribune.demo.service.payment.db.repository.PaymentSessionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payments")
@RestController
public class PaymentController {

    private final PaymentSessionRepository paymentSessionRepository;
    private final PaymentEventRepository paymentEventRepository;


    @GetMapping("/payment/{paymentId}")
    public Mono<PaymentSession> getPayment(@PathVariable(name = "paymentId")String paymentId) {
        log.info("Getting payment by Id: {}",paymentId);
        return paymentSessionRepository.findById(paymentId)
                .flatMap(paymentSession ->
                        Mono.just(paymentSession)
                                .zipWith(paymentEventRepository.findAllBySessionId(paymentSession.getId())
                                                .collectList(),
                                        (session, events) -> {
                                            session.setPaymentEvents(events);
                                            return session;
                                        })
                );
    }

    @GetMapping("/abstract")
    public Flux<PaymentSession> getAbstractedPayments() {
        log.info("Getting All payments [Abstracted]");
        return paymentSessionRepository.findAll();
    }

    @GetMapping
    public Flux<PaymentSession> getPayments() {
        log.info("Getting All payments");
        return paymentSessionRepository.findAll()
                .flatMap(paymentSession ->
                        Mono.just(paymentSession)
                                .zipWith(paymentEventRepository.findAllBySessionId(paymentSession.getId())
                                                .collectList(),
                                        (session, events) -> {
                                            session.setPaymentEvents(events);
                                            return session;
                                        })
                );
    }


}
