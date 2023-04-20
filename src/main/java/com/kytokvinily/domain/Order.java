package com.kytokvinily.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("orders")
public record Order(

        @Id
        Long id,
        Long vinylId,
        String vinylTitle,
        String vinylAuthor,
        Double vinylPrice,
        Integer quantity,
        OrderStatus orderStatus,
        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate,
        @Version
        int version
) {
    public static Order of(
            Long vinylId, String vinylTitle, String vinylAuthor, Double vinylPrice, Integer quantity, OrderStatus orderStatus
    ) {
        return new Order(null, vinylId, vinylTitle, vinylAuthor, vinylPrice, quantity, orderStatus, null, null, 0);
    }
}
