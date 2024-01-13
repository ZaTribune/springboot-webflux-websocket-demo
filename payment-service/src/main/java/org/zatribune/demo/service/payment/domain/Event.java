package org.zatribune.demo.service.payment.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Event {
    private String key;
    private String value;



    public String tojsonString() {
        return "{\"key\":\"" + key + "\" ,\"value\":\"" + value + "\"}";
    }
}
