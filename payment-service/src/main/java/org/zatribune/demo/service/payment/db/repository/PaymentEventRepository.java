package org.zatribune.demo.service.payment.db.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.zatribune.demo.service.payment.db.entity.PaymentEvent;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentEventRepository extends ReactiveMongoRepository<PaymentEvent, ObjectId> {

    Flux<PaymentEvent>findAllBySessionId(String sessionId);
}
