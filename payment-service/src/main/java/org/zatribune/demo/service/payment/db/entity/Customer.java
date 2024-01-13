package org.zatribune.demo.service.payment.db.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "customer")
public class Customer {

    @Id
    private ObjectId id;

    private String name;

    @CreatedDate
    private LocalDateTime creationDate;


    @DBRef
    private Set<PaymentSession> paymentSessions;

}
