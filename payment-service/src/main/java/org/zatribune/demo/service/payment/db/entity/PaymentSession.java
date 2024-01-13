package org.zatribune.demo.service.payment.db.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "payment_session")
public class PaymentSession {

    @Id
    private String id;

    private String description;

    @DocumentReference
    private List<PaymentEvent> paymentEvents;

    @CreatedDate
    private LocalDateTime creationDate;
}
