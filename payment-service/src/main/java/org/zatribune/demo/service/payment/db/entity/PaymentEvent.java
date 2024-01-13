package org.zatribune.demo.service.payment.db.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "payment_event")
public class PaymentEvent {

    @Id
    private ObjectId id;

    private String content;

    private String sessionId;

    @CreatedDate
    private LocalDateTime creationDate;

}
