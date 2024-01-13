package org.zatribune.demo.service.payment.dev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.zatribune.demo.service.payment.db.entity.Customer;
import org.zatribune.demo.service.payment.db.entity.PaymentEvent;
import org.zatribune.demo.service.payment.db.entity.PaymentSession;
import org.zatribune.demo.service.payment.db.repository.CustomerRepository;
import org.zatribune.demo.service.payment.db.repository.PaymentEventRepository;
import org.zatribune.demo.service.payment.db.repository.PaymentSessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Component
public class DevBootstrap implements CommandLineRunner {

    private final PaymentSessionRepository paymentSessionRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final CustomerRepository customerRepository;


    @Override
    public void run(String... args) throws Exception {
        clearOldData();
        initEvents();

    }

    void clearOldData() {
        paymentSessionRepository.deleteAll().subscribe();
        paymentEventRepository.deleteAll().subscribe();
        customerRepository.deleteAll().subscribe();
    }

    void initEvents() {
        PaymentEvent e1 = PaymentEvent.builder()
                .id(ObjectId.get())
                .content("***click on button")
                .creationDate(LocalDateTime.now())
                .sessionId("***123")
                .build();
        PaymentEvent e2 = PaymentEvent.builder()
                .id(ObjectId.get())
                .content("***Hover on payment box")
                .creationDate(LocalDateTime.now())
                .sessionId("***123")
                .build();
        PaymentEvent e3 = PaymentEvent.builder()
                .id(ObjectId.get())
                .content("***click on exit")
                .creationDate(LocalDateTime.now())
                .sessionId("***123")
                .build();
        PaymentEvent e4 = PaymentEvent.builder()
                .id(ObjectId.get())
                .content("***input cvc")
                .creationDate(LocalDateTime.now())
                .sessionId("***123")
                .build();
        PaymentEvent e5 = PaymentEvent.builder()
                .id(ObjectId.get())
                .content("***select method")
                .creationDate(LocalDateTime.now())
                .sessionId("***123")
                .build();
        List<PaymentEvent> eventList = List.of(e1, e2, e3, e4, e5);

        paymentEventRepository.saveAll(eventList)
                .subscribe();

        PaymentSession session = PaymentSession.builder()
                .id("***123")
                .description("***testing session")
                .creationDate(LocalDateTime.now())
                .paymentEvents(eventList)
                .build();

        Set<PaymentSession> paymentSessions = Set.of(session);

        paymentSessionRepository.saveAll(paymentSessions)
                .subscribe();

        Customer customer = Customer.builder()
                .id(ObjectId.get())
                .name("Muhammad Ali")
                .paymentSessions(paymentSessions)
                .creationDate(LocalDateTime.now())
                .build();

        Set<Customer> customers = Set.of(customer);

        customerRepository.saveAll(customers).blockLast();
    }
}
