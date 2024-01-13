package org.zatribune.demo.service.payment.db.repository;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.zatribune.demo.service.payment.db.entity.PaymentSession;
import reactor.core.publisher.Flux;


@Repository
public interface PaymentSessionRepository extends ReactiveMongoRepository<PaymentSession, String> {

    @Query("{}")
    Flux<PaymentSession> findAllParentsAndPopulateChildren();

}
